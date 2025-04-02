package shop.holy.v3.ecommerce.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

@Service
@RequiredArgsConstructor
public class AuthAccountService {
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AuthAccount findAccountById(long id) {
        Account account = accountRepository.findById(id).orElseThrow();
        return accountMapper.fromAccountToAuthAccount(account);
    }
}
