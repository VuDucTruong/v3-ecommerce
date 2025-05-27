package shop.holy.v3.ecommerce.service.biz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.account.RequestProfileUpdate;
import shop.holy.v3.ecommerce.api.dto.account.ResponseUser;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserCreate;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserSearch;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.entity.Profile;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProfileRepository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

@RequiredArgsConstructor
@Service
public class UserService {

    private final IAccountRepository accountRepository;
    private final IProfileRepository profileRepository;
    private final AccountMapper accountMapper;
    private final CloudinaryFacadeService cloudinaryFacadeService;

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

        Account savedAccount = accountRepository.save(account);
        profile.setAccountId(savedAccount.getId());
        profileRepository.save(profile);
        return accountMapper.fromEntityToResponseAccountDetail(savedAccount);
    }

    public ResponseUser getById(Long id, boolean deleted) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (!authAccount.isAdmin() && id != null) {
            throw BizErrors.FORBIDDEN_ACTION.exception();
        } else if (id == null)
            id = authAccount.getId();

        if (authAccount.isAdmin() && deleted) {
            return accountRepository.findById(id)
                    .map(accountMapper::fromEntityToResponseAccountDetail)
                    .orElseThrow(BizErrors.ACCOUNT_NOT_FOUND::exception);
        }
        return accountRepository.findByIdAndDeletedAtIsNull(id)
                .map(accountMapper::fromEntityToResponseAccountDetail)
                .orElseThrow(BizErrors.ACCOUNT_NOT_FOUND::exception);
    }

    public ResponsePagination<ResponseUser> search(RequestUserSearch requestSearch) {
        Specification<Account> spec = accountMapper.fromRequestToSpecification(requestSearch);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(requestSearch.pageRequest());
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return ResponsePagination.fromPage(accounts.map(accountMapper::fromEntityToResponseAccountDetail));
    }


    @Transactional
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
    public int deleteAccount() {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        long id = authAccount.getId();
        return deleteAccountById(id);
    }

    @Transactional
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
        return deletedAccs;
    }


}
