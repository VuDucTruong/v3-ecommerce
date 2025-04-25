package shop.holy.v3.ecommerce.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.shared.property.JwtProperties;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static shop.holy.v3.ecommerce.shared.constant.DefaultValues.ClaimKeys;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {
    private final JwtProperties jwtProperties;
    private final Key acccessSecretKey;
    private final Key refreshSecretKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        acccessSecretKey = Keys.hmacShaKeyFor(jwtProperties.getAccessTokenSecret().getBytes());
        refreshSecretKey = Keys.hmacShaKeyFor(jwtProperties.getRefreshTokenSecret().getBytes());
    }

    public String generateAccessToken(AuthAccount account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ClaimKeys.ID, account.getId());
        claims.put(ClaimKeys.ROLE, account.getRole());
        claims.put(ClaimKeys.PROFILE_ID, account.getProfileId());
        // Convert LocalDate to ISO-8601 string format
        if (account.getEnableDate() != null)
            claims.put(ClaimKeys.ENABLE_DATE, account.getEnableDate().toString());
        if (account.getDisableDate() != null)
            claims.put(ClaimKeys.DISABLE_DATE, account.getDisableDate().toString());
        if (account.getDeletedAt() != null)
            claims.put(ClaimKeys.DELETED_AT, account.getDeletedAt().toString());
        claims.put(ClaimKeys.IS_VERIFIED, account.getIsVerified());


        return Jwts.builder()
                .addClaims(claims)
                .setSubject(String.valueOf(account.getId()))
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationMinutes() * 60 * 1000))
                .signWith(acccessSecretKey)
                .compact();
    }

    public String generateRefreshToken(String id) {
        return Jwts.builder().setSubject(id).setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationMinutes() * 60 * 1000))
                .signWith(refreshSecretKey)
                .compact();
    }

    public Claims extractClaims(String token, boolean isAccessToken) {
        Key secret = isAccessToken ? acccessSecretKey : refreshSecretKey;
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build().parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token, boolean isAccessToken) {
        return extractClaims(token, isAccessToken).getExpiration().before(new Date());
    }

    public String extractId(String token, boolean isAccessToken) {
        return extractClaims(token, isAccessToken).getSubject();
    }

}