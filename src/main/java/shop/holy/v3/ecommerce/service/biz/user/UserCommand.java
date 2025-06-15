package shop.holy.v3.ecommerce.service.biz.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.account.RequestProfileUpdate;
import shop.holy.v3.ecommerce.api.dto.account.ResponseUser;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserCreate;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.entity.Profile;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProfileRepository;
import shop.holy.v3.ecommerce.service.CacheService;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.CacheKeys;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class UserCommand {

    private final IAccountRepository accountRepository;
    private final IProfileRepository profileRepository;
    private final AccountMapper accountMapper;
    private final CloudinaryFacadeService cloudinaryFacadeService;
    private final CacheService cacheService;


    @Transactional
    public ResponseUser createUser(RequestUserCreate request) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (authAccount.getRole() != RoleEnum.ADMIN) {
            throw BizErrors.FORBIDDEN_ACTION.exception();
        }
        Account account = accountMapper.fromUserCreateRequestToAccountEntity(request);
        Profile profile = accountMapper.fromProfileRequestToEntity(request.profile());
        MultipartFile file = request.profile().avatar();
        if (file != null) {
            String blobUrl = cloudinaryFacadeService.uploadAccountBlob(file);
            profile.setImageUrlId(blobUrl);
            profile.setId(account.getId());
        }
        account.setVerified(true);

        Account savedAccount = accountRepository.save(account);
        profile.setAccountId(savedAccount.getId());
        profileRepository.save(profile);
        return accountMapper.fromEntityToResponseAccountDetail(savedAccount);
    }

    @Transactional
    @CacheEvict(cacheNames = CacheKeys.AUTH, key = "#securityUtil.authAccountId")
    public ResponseProfile updateProfile(RequestProfileUpdate request) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        Profile profile = accountMapper.fromProfileUpdateRequestToEntity(request);
        profile.setAccountId(authAccount.getId());
        int changes;

        if (request.image() != null) {
            String blobUrl = cloudinaryFacadeService.uploadAccountBlob(request.image());
            profile.setImageUrlId(blobUrl);
        }
        if ((request.id() == null))
            changes = profileRepository.updateProfileByAccountId(profile);
        else
            changes = profileRepository.updateProfileById(profile);
        if (changes <= 0)
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();

        return accountMapper.fromEntityToResponseProfile(profile);
    }

    @Transactional
    @CacheEvict(cacheNames = CacheKeys.AUTH, key = "#securityUtil.authAccountId")
    public int deleteAccount() {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        long id = authAccount.getId();
        return deleteAccountById(id);
    }

    @Transactional
    @CacheEvict(cacheNames = CacheKeys.AUTH, key = "#securityUtil.authAccountId")
    public int deleteAccountById(long id) {
        int deletedPros = profileRepository.updateDeletedAtByAccountId(id);
        int deletedAccs = accountRepository.updateDeletedAtById(id);
        if (deletedAccs == 0 && deletedPros == 0) {
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();
        }
        return deletedAccs;
    }


    @Transactional
    public int deleteAccounts(long[] ids) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (ids == null || ids.length == 0)
            return 0;
        for (long id : ids) {
            if (id == authAccount.getId())
                throw BizErrors.FORBIDDEN_ACTION.exception();
        }


        int deletedPros = profileRepository.updateDeletedAtByAccountIdIn(ids);
        int deletedAccs = accountRepository.updateDeletedAtByIdIn(ids);
        if (deletedAccs == 0 && deletedPros == 0) {
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();
        }
        cacheService.evictIn(CacheKeys.AUTH, Arrays.stream(ids).boxed().toList());
        return deletedAccs;
    }


}
