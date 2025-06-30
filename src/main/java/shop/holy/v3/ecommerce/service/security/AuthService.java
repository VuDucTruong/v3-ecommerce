package shop.holy.v3.ecommerce.service.security;


import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.account.token.RequestLogin;
import shop.holy.v3.ecommerce.api.dto.account.token.ResponseLogin;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.service.CacheService;
import shop.holy.v3.ecommerce.service.biz.user.UserQuery;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.CacheKeys;
import shop.holy.v3.ecommerce.shared.constant.CookieKeys;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserQuery userQueryService;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;
    private final CacheService cacheService;

    @Value("${same-site}")
    private String SAME_SITE;
    @Value("${is-secure}")
    private Boolean IS_SECURE;

    private final IAccountRepository accountRepository;

    public AuthAccount findAccountById(long id) {
        AuthAccount authAccount = cacheService.getSafe(CacheKeys.AUTH, id, AuthAccount.class);
        if (authAccount != null) {
            return authAccount;
        }

        Account account = accountRepository.findById(id).orElseThrow(BizErrors.AUTHORISATION_INVALID::exception);
        authAccount = accountMapper.fromAccountToAuthAccount(account);
        cacheService.cacheSafe(CacheKeys.AUTH, id, authAccount);
        return authAccount;
    }

    public ResponseLogin authenticateAccount(RequestLogin request) {
        Account account = userQueryService.findByEmail(request.email());
        if (account == null)
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();
        if (!account.isVerified())
            throw BizErrors.FORBIDDEN_NOT_VERIFIED.exception();
        if (!account.getPassword().equals(request.password()))
            throw BizErrors.AUTHORISATION_INVALID.exception();

        AuthAccount authAccount = accountMapper.fromAccountToAuthAccount(account);
        cacheService.cacheSafe(CacheKeys.AUTH, authAccount.getId(), authAccount);
        return new ResponseLogin(jwtService.generateAccessToken(authAccount),
                jwtService.generateRefreshToken(String.valueOf(authAccount.getId())),
                accountMapper.fromEntityToResponseAccountDetail(account)
        );
    }

    public ResponseCookie[] makeCookies(ResponseLogin loginResponse) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(CookieKeys.ACCESS_TOKENS, loginResponse.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(CookieKeys.REFRESH_TOKENS, loginResponse.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        return new ResponseCookie[]{accessTokenCookie, refreshTokenCookie};
    }

    public ResponseCookie[] removeAuthCookies() {
        ResponseCookie accessTokenCookie = ResponseCookie.from(CookieKeys.ACCESS_TOKENS, "")
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(CookieKeys.REFRESH_TOKENS, "")
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        return new ResponseCookie[]{accessTokenCookie, refreshTokenCookie};
    }
}
