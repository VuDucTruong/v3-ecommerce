package shop.holy.v3.ecommerce.shared.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.util.Optional;

public class MappingUtils {
    private MappingUtils() {
    }

    public static Pageable fromRequestPageableToPageable(RequestPageable requestPageable) {
        if (requestPageable == null) {
            return PageRequest.of(
                    0,
                    10,
                    Sort.by(Sort.Direction.ASC, "id")
            );
        }
        Sort.Direction direction = Sort.Direction.fromOptionalString(
                        requestPageable.sortDirection())
                .orElse(Sort.Direction.ASC);
        return PageRequest.of(
                requestPageable.page(),
                requestPageable.size(),
                Sort.by(direction,
                        Optional.of(requestPageable.sortBy())
                                .orElse("id")
                )
        );
    }

}
