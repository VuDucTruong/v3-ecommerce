package shop.holy.v3.ecommerce.api.dto.user.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDate;

public record ResponseProfile (
        long id,
        String fullName,
        LocalDate createdAt,
        String imageUrl
) {
    public record Detailed(
            long id,
            String fullName,
            LocalDate createdAt,
            String imageUrl,
            @JsonUnwrapped AccountLight account
    ){}
    public record AccountLight(@JsonProperty("accountId") long id, String email){}

}
