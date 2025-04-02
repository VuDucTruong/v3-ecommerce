package shop.holy.v3.ecommerce.service.biz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

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

    public ResponseUser getById(long id, boolean deleted) {
        if(deleted){
            return accountRepository.findByIdAndDeletedAtIsNotNull(id)
                    .map(accountMapper::fromEntityToResponseAccountDetail)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
        }
        return accountRepository.findById(id)
                .map(accountMapper::fromEntityToResponseAccountDetail)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }


    public ResponsePagination<ResponseUser> search(RequestUserSearch requestSearch) {
        Specification<Account> spec = accountMapper.fromRequestToSpecification(requestSearch);
        Page<Account> accounts = accountRepository.findAll(spec, requestSearch.pageRequest());
        return ResponsePagination.fromPage(accounts.map(accountMapper::fromEntityToResponseAccountDetail));
    }


    @Transactional
    public ResponseProfile updateProfile(RequestProfileUpdate request) throws IOException {
        Profile profile = accountMapper.fromProfileUpdateRequestToEntity(request);
        if (request.image() != null) {
            String blobUrl = cloudinaryFacadeService.uploadAccountBlob(request.image());
            profile.setImageUrlId(blobUrl);
            profileRepository.updateProfileById(profile);
            return accountMapper.fromEntityToResponseProfile(profile);
        }
        profileRepository.updateProfileExcludeId(profile);
        return accountMapper.fromEntityToResponseProfile(profile);
    }

    @Transactional
    public void deleteAccount(long id) {
        accountRepository.deleteById(id);
    }


}
