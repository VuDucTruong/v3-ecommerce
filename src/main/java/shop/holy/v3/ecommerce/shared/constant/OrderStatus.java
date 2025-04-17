package shop.holy.v3.ecommerce.shared.constant;

public enum OrderStatus {
    // when: order is created
    PENDING,
   // admin is sending email + payment status = SUCCESS
    PROCESSING,
    // when: mail sent + payment status = SUCCESS
    COMPLETED,
    // ELSE..
    FAILED
    ;
}



// bán keys