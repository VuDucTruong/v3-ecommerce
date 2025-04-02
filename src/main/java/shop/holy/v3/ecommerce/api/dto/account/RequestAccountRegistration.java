package shop.holy.v3.ecommerce.api.dto.account;

import jakarta.validation.constraints.NotBlank;

public record RequestAccountRegistration(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String fullName
)
{

}
