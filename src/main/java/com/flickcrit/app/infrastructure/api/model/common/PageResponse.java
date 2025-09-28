package com.flickcrit.app.infrastructure.api.model.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Represents a paginated response containing a list of items and its related metadata.
 *
 * @param <T> The type of elements in the paginated list.
 */
@Getter
@Builder
@RequiredArgsConstructor
public final class PageResponse<T> {

    private static final int INITIAL_PAGE = 0;

    private final List<T> items;
    private final long total;
    private final int page;
    private final int pageSize;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
            .items(page.getContent())
            .total(page.getTotalElements())
            .page(page.getNumber())
            .pageSize(page.getSize())
            .build();
    }

    public static <T> PageResponse<T> of(List<T> items) {
        return PageResponse.<T>builder()
            .pageSize(items.size())
            .total(items.size())
            .items(items)
            .page(INITIAL_PAGE)
            .build();
    }
}
