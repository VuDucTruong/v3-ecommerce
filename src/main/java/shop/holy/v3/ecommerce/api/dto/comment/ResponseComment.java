package shop.holy.v3.ecommerce.api.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProductMetadata;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public record ResponseComment(
        long id,
        ResponseProductMetadata product,
        Long parentCommentId,
        ResponseProfile author,
        String role,
        Date createdAt,
        Date deletedAt,
        String content,
        ResponseReply[] replies
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResponseComment that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ResponseComment{" +
                "id=" + id +
                ", product=" + product +
                ", parentCommentId=" + parentCommentId +
                ", author=" + author +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                ", content='" + content + '\'' +
                ", replies=" + Arrays.toString(replies) +
                '}';
    }

    public record Light(
            long id,
            ResponseProfile author,
            String role,
            Date createdAt,
            Date deletedAt,
            String content,
            ResponseReply[] replies
    ) {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Light light)) return false;
            return id == light.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public String toString() {
            return "Light{" +
                    "id=" + id +
                    ", author=" + author +
                    ", role='" + role + '\'' +
                    ", createdAt=" + createdAt +
                    ", deletedAt=" + deletedAt +
                    ", content='" + content + '\'' +
                    ", replies=" + Arrays.toString(replies) +
                    '}';
        }
    }


    public record Flattened(
            long id,
            ResponseProductMetadata product,
            Long parentCommentId,
            String role,
            @JsonProperty("parentComment") Parent parent,
            ResponseProfile author,
            Date createdAt,
            Date deletedAt,
            String content
    ) {
        public record Parent(
                long id,
                ResponseProfile author,
                String role,
                Date createdAt,
                Date deletedAt,
                String content
        ) {
        }

    }

}
