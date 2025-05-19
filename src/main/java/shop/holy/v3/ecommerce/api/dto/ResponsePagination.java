package shop.holy.v3.ecommerce.api.dto;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record ResponsePagination<T>(
        long totalInstances,
        int totalPages,
        int currentPage,
        List<T> data
) {

    public static <I, O> ResponsePagination<O> fromPage(Page<I> page, Function<I, O> mapper) {
        return new ResponsePagination<O>(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getContent().stream().map(mapper).toList());
    }

    public static <T> ResponsePagination<T> fromPage(@Nonnull Page<T> page) {
        return new ResponsePagination<>(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getContent());
    }

}
