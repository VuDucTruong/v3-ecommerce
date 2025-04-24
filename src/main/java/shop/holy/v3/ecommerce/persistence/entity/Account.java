package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "accounts")
public class Account extends EntityBase {

    @Size(max = 100)
    private String email;
    @Size(max = 50)
    private String password;
    @Size(max = 50)
    private String role;
    private LocalDate enableDate;
    private LocalDate disableDate;
    private boolean isVerified;

    @OneToOne(mappedBy = "account")
    private Profile profile;

    @Size(max = 6)
    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_expiry")
    private Date otpExpiry;

}
