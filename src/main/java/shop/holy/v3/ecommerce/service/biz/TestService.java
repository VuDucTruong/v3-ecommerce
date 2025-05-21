package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.account.token.ResponseLogin;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.repository.test.ITestRepository;
import shop.holy.v3.ecommerce.service.security.JwtService;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

@RequiredArgsConstructor
@Service
public class TestService {
    private final ITestRepository testRepository;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;

    public ResponseLogin authenticate(String role) {
        Account account = testRepository.findFirstByRoleEquals(role).orElse(new Account());
        AuthAccount authAccount = accountMapper.fromAccountToAuthAccount(account);
        return new ResponseLogin(jwtService.generateAccessToken(authAccount),
                jwtService.generateRefreshToken(String.valueOf(authAccount.getId())),
                accountMapper.fromEntityToResponseAccountDetail(account)
        );
    }
}
