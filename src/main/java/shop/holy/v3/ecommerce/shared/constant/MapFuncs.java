package shop.holy.v3.ecommerce.shared.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapFuncs {

    public static final String GEN_URL = "genUrl";
    public static final String EXTRACT_ACCOUNT_PUBLIC_ID = "extractAccountPublicId";
    public static final String EXTRACT_PRODUCT_PUBLIC_ID = "extractProductPublicId";
    public static final String EXTRACT_CATEGORY_PUBLIC_ID = "extractCategoryPublicId";
    public static final String EXTRACT_BLOG_PUBLIC_ID = "extractBlogPublicId";
    public static final String EXTRACT_COUPON_PUBLIC_ID = "extractCouponPublicId";

    /// PRODUCTS

    public static final String FROM_LIST_STRING_TO_TAG_ENTITIES = "fromStringTagsToProductTagsEntity";
    public static final String FROM_TAG_ENTITY_TO_STRING_ARRAY = "fromStringTagsToProductTagsEntityListString";

}