package shop.holy.v3.ecommerce.service.biz.user;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.account.RequestAccountRegistration;
import shop.holy.v3.ecommerce.api.dto.account.RequestMailVerification;
import shop.holy.v3.ecommerce.api.dto.account.RequestOTP;
import shop.holy.v3.ecommerce.api.dto.account.RequestPasswordUpdate;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.entity.Profile;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProfileRepository;
import shop.holy.v3.ecommerce.service.security.AuthService;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountCommand {

    private final IAccountRepository accountRepository;
    private final IProfileRepository profileRepository;
    private final AuthService authService;
    private final AccountMapper accountMapper;
    private final SmtpService smtpService;
    private final Random random = new Random();

    @Value("${app.strict.enable}")
    private boolean enableStrict;
    @Value("${app.strict-password-change.enable}")
    private boolean enableStrictPasswordChange;


    @Transactional
    public void saveOtp(RequestOTP otpRequest) throws MessagingException {
        int ran = random.nextInt(999999);
        String otp = String.format("%06d", ran);

        int changes = accountRepository.saveOtp(otpRequest.email(), otp);
        if (changes == 0 && enableStrict) {
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();
        }

        smtpService.sendOTPEmail(otpRequest.email(), otp);
    }


    @Transactional
    public ResponseCookie[] changePassword(RequestPasswordUpdate request) {
        Account account = accountRepository.findByEmail(request.email());
        if (account == null)
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();

        if (enableStrictPasswordChange && !account.getRole().equals(RoleEnum.CUSTOMER.name())) {

            if (enableStrict && account.isVerified())
                throw BizErrors.FORBIDDEN_NOT_VERIFIED.exception();
            checkOtp(account.getOtp(), account.getOtpExpiry(), request.otp());
        }

        int changes = accountRepository.changePassword(account.getId(), request.password());
        if (changes == 0) {
            throw BizErrors.INVALID_OTP.exception();
        }
        return authService.removeAuthCookies();
    }

    @Transactional
    public ResponseCookie[] verifyEmail(RequestMailVerification request) {
        Account account = accountRepository.findByEmail(request.email());
        if (account == null)
            throw BizErrors.ACCOUNT_NOT_FOUND.exception();
        checkOtp(account.getOtp(), account.getOtpExpiry(), request.otp());
        int changes = accountRepository.verifyMail(account.getId());
        if (changes == 0) {
            throw BizErrors.INVALID_OTP.exception();
        }
        return authService.removeAuthCookies();
    }

    public void checkOtp(String accountOtp, Date accountOtpExpiry, String _requestOtp) {
        if (!StringUtils.hasLength(accountOtp) || !Objects.equals(accountOtp, _requestOtp) || accountOtpExpiry == null)
            throw BizErrors.INVALID_OTP.exception();

        Date now = new Date();
        if (accountOtpExpiry.before(now))
            throw BizErrors.OTP_EXPIRED.exception();
    }


    @Transactional
    public void registerAccount(RequestAccountRegistration registration) {
        var queriedAccount = accountRepository.findByEmail(registration.email());

        if (queriedAccount == null) {
            Account account = accountMapper.fromRegistrationRequestToEntity(registration);
            accountRepository.save(account);
            Profile profile = account.getProfile();
            profile.setAccountId(account.getId());
            profileRepository.save(profile);
            return;
        }

        if (queriedAccount.isVerified())
            throw BizErrors.EMAIL_ALREADY_EXISTS.exception();

        /// ACC EXIST, NOT VERIFIED
        Date now = new Date();
        if (StringUtils.hasLength(queriedAccount.getOtp()) && queriedAccount.getOtpExpiry() != null && queriedAccount.getOtpExpiry().after(now))
        /// ACC EXIST, NOT VERIFIED, STILL HAS OTP ==> currently Verifying
        ///  either complete the verification OR request new OTP
            throw BizErrors.FORBIDDEN_UNCOMPLETED_VERIFICATION.exception();

        ///  ACC EXIST, NOT VERIFIED, NOT IN VERIFICATION STEP (0 OTPs)

        queriedAccount.setRole(RoleEnum.CUSTOMER.name());
        queriedAccount.setVerified(false);
        queriedAccount.setPassword(registration.password());
        queriedAccount.getProfile().setFullName(registration.fullName());

        accountRepository.save(queriedAccount);
        var profile = queriedAccount.getProfile();
        profileRepository.save(profile);
    }

}