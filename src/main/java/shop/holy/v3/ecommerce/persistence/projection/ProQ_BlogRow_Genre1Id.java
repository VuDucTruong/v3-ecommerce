package shop.holy.v3.ecommerce.persistence.projection;

import java.util.Date;

public record ProQ_BlogRow_Genre1Id(
        Long genre1Id,
        Long blogId,
        String title,
        String subtitle,
        String content,
        Date createdAt,
        Date publishedAt,
        Date approvedAt,
        String blogImageUrlId,
        Long profileId,
        String fullName,
        Date profileCratedAt,
        String profileImageUrlId,
        Long g2Id
) {
}