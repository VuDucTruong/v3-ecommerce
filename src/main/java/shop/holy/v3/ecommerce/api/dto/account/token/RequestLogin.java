package shop.holy.v3.ecommerce.api.dto.account.token;

import org.hibernate.validator.constraints.Length;

public record RequestLogin(@Length(min = 6,max = 40) String email,
                           @Length(min = 6,max = 40) String password) {

}
