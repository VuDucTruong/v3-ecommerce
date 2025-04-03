package shop.holy.v3.ecommerce.service.biz;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentCallback;
import shop.holy.v3.ecommerce.persistence.repository.IPaymentRepository;
import shop.holy.v3.ecommerce.shared.mapper.PaymentMapper;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IPaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public String updatePaymentTransaction(RequestPaymentCallback request){


//        VNPUpdateDTO dto = paymentMapper.fromVNPCallbackToEntity(request);
//        if (!Objects.equals(request.getResponseCode(), "00")){
//            paymentRepository.updatePaymentFAILED(dto);
//            return "Payment failed";
//        }
//        int vnpChanges = vnpPaymentRepository.updateVnpPayment(dto);
//        int paymentChanges = paymentRepository.updatePaymentPAID(dto);
//        if (vnpChanges !=1 || paymentChanges != 1){
//            throw new BadRequestException("Payment update failed");
//        }
        return "Payment success";
    }
}
