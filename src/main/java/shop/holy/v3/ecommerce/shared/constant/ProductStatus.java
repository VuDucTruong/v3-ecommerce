package shop.holy.v3.ecommerce.shared.constant;

public enum ProductStatus {
    IN_STOCK,
    OUT_OF_STOCK;


    public static ProductStatus fromCnt(long cnt) {
        return cnt > 0 ? IN_STOCK : OUT_OF_STOCK;
    }


}
