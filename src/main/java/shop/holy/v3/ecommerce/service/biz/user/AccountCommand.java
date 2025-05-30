package shop.holy.v3.ecommerce.service.biz.user;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.account.RequestAccountRegistration;
import shop.holy.v3.ecommerce.api.dto.account.RequestOTP;
import shop.holy.v3.ecommerce.api.dto.account.RequestPasswordUpdate;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProfileRepository;
import shop.holy.v3.ecommerce.service.security.AuthService;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountCommand {

    private final IAccountRepository accountRepository;
    private final IProfileRepository profileRepository;
    private final AuthService authService;
    private final AccountMapper accountMapper;
    private final SmtpService smtpService;

    @Transactional
    public void saveOtp(RequestOTP otpRequest) throws MessagingException {
        int ran = new Random().nextInt(999999);
        String otp = String.format("%06d", ran);
        int changes = accountRepository.saveOtp(otpRequest.email(), otp);
        if (changes == 0) {
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();
        }

        smtpService.sendOTPEmail(otpRequest.email(), otp);

    }

    @Transactional
    public Cookie[] changePassword(RequestPasswordUpdate request) {
        int changes = accountRepository.changePassword(request.otp(), request.email(), request.password());
        if (changes == 0) {
            throw BizErrors.OTP_NOT_FOUND.exception();
        }
        return authService.removeAuthCookies();
    }

    @Transactional
    public void registerAccount(RequestAccountRegistration registration) {
        var emailExist = accountRepository.isEmailExist(registration.email());

        if (emailExist.isPresent())
            throw BizErrors.EMAIL_ALREADY_EXISTS.exception();

        Account account = accountMapper.fromRegistrationRequestToEntity(registration);
        account.setRole(RoleEnum.CUSTOMER.name());
        var acc = accountRepository.save(account);
        var profile = account.getProfile();
        profile.setAccountId(acc.getId());
        profileRepository.save(profile);
    }

}