package shop.holy.v3.ecommerce.api.filter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.service.security.AuthService;
import shop.holy.v3.ecommerce.service.security.JwtService;
import shop.holy.v3.ecommerce.shared.property.JwtProperties;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends org.springframework.web.filter.OncePerRequestFilter {

    private final JwtService jwtUtil;
    private final JwtProperties jwtProperties;
    private final AuthService authAccountService;
//    private final String[] whiteListedEndpoints = {"/accounts/tokens", "/accounts"};

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;
        boolean hitRefreshToken = false;

        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }
        AuthAccount authAccount = null;

        // Validate and Trust access token at first
        if (accessToken != null && !jwtUtil.isTokenExpired(accessToken, true)) {
            long userId = Long.parseLong(jwtUtil.extractId(accessToken, true));
            authAccount = authAccountService.findAccountById(userId);
        }
        /// If access token is expired, try to trust refresh token
        else {
            if (refreshToken != null && !jwtUtil.isTokenExpired(refreshToken, false)) {
                hitRefreshToken = true;
                long userId = Long.parseLong(jwtUtil.extractId(refreshToken, true));
                authAccount = authAccountService.findAccountById(userId);
            }
        }
        // If both tokens are invalid, continue without authentication
        // if next chain allow anonymous access -> then OK
        // if next chain requires authentication -> then 401
        if (authAccount == null) {
            chain.doFilter(request, response);
            return;
        }

        grantAuthorisationBeforeNextChain(request, authAccount);
        chain.doFilter(request, response);
        if (hitRefreshToken) {
            grantNewTokensOnResponse(response, authAccount);
        }
    }

    private void grantAuthorisationBeforeNextChain(HttpServletRequest request, AuthAccount authAccount) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authAccount, null, authAccount.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void grantNewTokensOnResponse(HttpServletResponse response, AuthAccount authAccount) {

        String newAccessToken = jwtUtil.generateAccessToken(authAccount);
        String newRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(authAccount.getId()));

        Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge((int) jwtProperties.getAccessTokenExpirationMinutes() * 60);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) jwtProperties.getRefreshTokenExpirationMinutes() * 60);
        response.addCookie(refreshTokenCookie);

    }

}