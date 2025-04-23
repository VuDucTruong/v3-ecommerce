package shop.holy.v3.ecommerce.shared.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    SUCCESS(0),
    PENDING(1),
    FAILED(2);

    private final int value;
    public int value(){return value;}

    public boolean equals(int value){return this.value == value;}
    public boolean equals(PaymentStatus status){return this.value == status.value;}
}
