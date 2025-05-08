package shop.holy.v3.ecommerce.service.security;


import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.account.token.RequestLogin;
import shop.holy.v3.ecommerce.api.dto.account.token.ResponseLogin;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.entity.Profile;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.CookieKeys;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;


    public ResponseLogin authenticateAccount(RequestLogin request) {
        Account account = accountRepository.findByEmail(request.email());

        if (account != null && account.getPassword().equals(request.password())) {
            Profile profile = account.getProfile();
            AuthAccount authAccount = accountMapper.fromAccountToAuthAccount(account);
            return new ResponseLogin(jwtService.generateAccessToken(authAccount),
                    jwtService.generateRefreshToken(String.valueOf(authAccount.getId())),
                    accountMapper.fromEntityToResponseProfile(profile)
            );
        }
        throw BizErrors.LOGIN_FAILED.exception();
    }

    public Cookie[] makeCookies(ResponseLogin loginResponse) {
        Cookie accessTokenCookie = new Cookie(CookieKeys.ACCESS_TOKENS, loginResponse.accessToken());
        Cookie refreshTokenCookie = new Cookie(CookieKeys.REFRESH_TOKENS, loginResponse.refreshToken());
        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);

//        accessTokenCookie.setSecure(true);
//        refreshTokenCookie.setSecure(true);

        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        return new Cookie[]{accessTokenCookie, refreshTokenCookie};
    }

    public Cookie[] removeAuthCookies() {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);

//        accessTokenCookie.setSecure(true);
//        refreshTokenCookie.setSecure(true);

        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        refreshTokenCookie.setMaxAge(0);
        return new Cookie[]{accessTokenCookie, refreshTokenCookie};
    }

}
