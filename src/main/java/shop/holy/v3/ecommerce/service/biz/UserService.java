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
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.exception.ForbiddenException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;
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
        if (authAccount.getRole() != RoleEnum.ROLE_ADMIN) {
            throw new ForbiddenException("You are not authorized to create a user");
        }
        Account account = accountMapper.fromUserCreateRequestToAccountEntity(request);
        MultipartFile file = request.profile().avatar();
        if (file != null) {
            String blobUrl = cloudinaryFacadeService.uploadAccountBlob(file);
            account.getProfile().setImageUrlId(blobUrl);
            account.getProfile().setId(account.getId());
        }
        Account savedAccount = accountRepository.save(account);
        return accountMapper.fromEntityToResponseAccountDetail(savedAccount);
    }

    public ResponseUser getById(Long id, boolean deleted) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (!authAccount.isAdmin() && authAccount.isNotSelf(id)) {
            throw new UnAuthorisedException("You are not authorized to view this account");
        }
        if (deleted) {
            return accountRepository.findById(id)
                    .map(accountMapper::fromEntityToResponseAccountDetail)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        }
        return accountRepository.findByIdAndDeletedAtIsNull(id)
                .map(accountMapper::fromEntityToResponseAccountDetail)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public ResponsePagination<ResponseUser> search(RequestUserSearch requestSearch) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (!authAccount.isAdmin())
            throw new UnAuthorisedException("You are not authorized to search users");

        Specification<Account> spec = accountMapper.fromRequestToSpecification(requestSearch);
        Pageable pageable = accountMapper.fromRequestPageableToPageable(requestSearch.pageRequest());
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
        accountRepository.deleteById(id);
    }


}
