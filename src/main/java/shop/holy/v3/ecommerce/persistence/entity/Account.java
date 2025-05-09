package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Getter
@Setter
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

    private String verificationCode;

    @OneToOne(mappedBy = "account")
    private Profile profile;

    @Size(max = 6)
    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_expiry")
    private Date otpExpiry;

}
