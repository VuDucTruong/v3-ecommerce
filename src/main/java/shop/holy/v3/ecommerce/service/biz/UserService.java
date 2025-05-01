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
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final IAccountRepository accountRepository;
    private final IProfileRepository profileRepository;
    private final AccountMapper accountMapper;
    private final CloudinaryFacadeService cloudinaryFacadeService;

    @Transactional
    public ResponseUser createUser(RequestUserCreate request) throws IOException {
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
        if (!authAccount.isAdmin() && authAccount.isNotSelf(id)) {
            throw BizErrors.FORBIDDEN_ACTION.exception();
        }
        if (deleted) {
            return accountRepository.findById(id)
                    .map(accountMapper::fromEntityToResponseAccountDetail)
                    .orElseThrow(BizErrors.ACCOUNT_NOT_FOUND::exception);
        }
        return accountRepository.findByIdAndDeletedAtIsNull(id)
                .map(accountMapper::fromEntityToResponseAccountDetail)
                .orElseThrow(BizErrors.ACCOUNT_NOT_FOUND::exception);
    }

    public ResponsePagination<ResponseUser> search(RequestUserSearch requestSearch) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (!authAccount.isAdmin())
            throw new UnAuthorisedException("You are not authorized to code users");

        Specification<Account> spec = accountMapper.fromRequestToSpecification(requestSearch);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(requestSearch.pageRequest());
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return ResponsePagination.fromPage(accounts.map(accountMapper::fromEntityToResponseAccountDetail));
    }


    @Transactional
    public ResponseProfile updateProfile(RequestProfileUpdate request) throws IOException {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        Profile profile = accountMapper.fromProfileUpdateRequestToEntity(request);
        if (authAccount.isAdmin()) {
            profile.setId(request.id());
        } else if (authAccount.isNotSelf(request.id())) {
            throw new UnAuthorisedException("You are not authorized to update this profile");
        }

        accountMapper.fromProfileUpdateRequestToEntity(request);
        if (request.image() != null) {
            String blobUrl = cloudinaryFacadeService.uploadAccountBlob(request.image());
            profile.setImageUrlId(blobUrl);
            /// UPDATING HERE
            profileRepository.updateProfileById(profile);
            return accountMapper.fromEntityToResponseProfile(profile);
        }
        /// UPDATING HERE
        profileRepository.updateProfileExcludeImage(profile);
        return accountMapper.fromEntityToResponseProfile(profile);
    }

    @Transactional
    public void deleteAccount(long id) {
        int deletedPros = profileRepository.updateDeletedAtByAccountId(id);
        int deletedAccs = accountRepository.updateDeletedAtById(id);
        if (deletedAccs == 0 && deletedPros == 0) {
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();
        }
    }


}
