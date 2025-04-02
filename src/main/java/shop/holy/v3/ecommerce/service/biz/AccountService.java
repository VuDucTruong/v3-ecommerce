package shop.holy.v3.ecommerce.service.biz;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.account.RequestOTP;
import shop.holy.v3.ecommerce.api.dto.account.RequestAccountRegistration;
import shop.holy.v3.ecommerce.api.dto.account.RequestPasswordUpdate;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.AccountMapper;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final SmtpService smtpService;

    @Transactional
    public void saveOtp(RequestOTP otpRequest) throws MessagingException {
        int ran = new Random().nextInt(999999);
        String otp = String.format("%06d", ran);
        int changes = accountRepository.saveOtp(otpRequest.email(), otp);

        smtpService.sendOTPEmail(otpRequest.email(), otp);
        if (changes == 0) {
            throw new BadRequestException("Account not found");
        }
    }

    @Transactional
    public void changePassword(RequestPasswordUpdate request) {
        int changes = accountRepository.changePassword(request.otp(), request.password());
        if (changes == 0) {
            throw new ResourceNotFoundException("Invalid OTP or Account not found");
        }
    }

    public void registerAccount(RequestAccountRegistration registration) {
        Account account = accountMapper.fromRegistrationRequestToEntity(registration);
        accountRepository.save(account);
    }

}