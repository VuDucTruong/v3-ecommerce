package shop.holy.v3.ecommerce.shared.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VnpResCodes {
    SUCCESS("00", "Giao dịch thành công"),
    SUCCESS_WITH_WARNING("07","Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)."),

    FAIL_IBANKING_NOT_REGISTERED("09","Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng."),
    FAIL_CARD_VERIFICATION_FAILED("10","Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"),
    FAIL_TRANS_TIMEOUT("11","Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch."),
    FAIL_TRANS_TIMEOUT2("15", "giao dịch hết hiệu lực thời gian thanh toán"),
    FAIL_CARD_LOCKED("12","Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa."),
    FAIL_OTP_INVALID("13","Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch."),
    FAIL_TRANS_CANCELLED("24","Giao dịch không thành công do: Khách hàng hủy giao dịch"),
    FAIL_BALANCE_INSUFFICIENT("51","Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch."),
    FAIL_LIMIT_EXCEEDED("65","Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày."),
    FAIL_BANK_MAINTENANCE("75","Ngân hàng thanh toán đang bảo trì."),
    FAIL_PASSWORD_INVALID("79","Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch"),
    FAIL_OTHER("99","Lỗi Không xác định");

    private final String code;
    private final String message;
    public String code(){return code;}
    public String message(){return message;}
    public static VnpResCodes fromCode(String code) {
        var enums = VnpResCodes.values();
        for (VnpResCodes v : enums) {
            if (v.code().equals(code)) {
                return v;
            }
        }
        return null;
    }

}
