package shop.holy.v3.ecommerce.shared.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheKeys {
    public static final String PRODUCTS = "products";
    public static final String MOST_LIKE_PRODUCTS = "MOST_LIKE_PRODUCTS";

    public static final String CATEGORY = "product_category";
    public static final String ACCOUNTS = "accounts";
    public static final String RECENT_BLOGS = "FIRST_LOAD_BLOGS";

    public static String[] values() {
        return new String[]{PRODUCTS, CATEGORY, ACCOUNTS, RECENT_BLOGS};
    }

}
