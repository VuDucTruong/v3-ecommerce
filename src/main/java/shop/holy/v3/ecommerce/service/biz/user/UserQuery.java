package shop.holy.v3.ecommerce.service.biz.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.account.ResponseUser;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserSearch;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.CacheKeys;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class UserQuery {

    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public ResponsePagination<ResponseUser> search(RequestUserSearch requestSearch) {
        Specification<Account> spec = accountMapper.fromRequestToSpecification(requestSearch);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(requestSearch.pageRequest());
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return ResponsePagination.fromPage(accounts.map(accountMapper::fromEntityToResponseAccountDetail));
    }


    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
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

}
