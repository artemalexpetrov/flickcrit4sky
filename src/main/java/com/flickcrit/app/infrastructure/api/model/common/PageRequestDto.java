package com.flickcrit.app.infrastructure.api.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PageRequestDto {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Schema(example = "0", description = "The page number")
    @Min(value = 0, message = "The min page value is {value}")
    @Max(value = Integer.MAX_VALUE, message = "The max page value is {value}")
    private int page = DEFAULT_PAGE;

    @Schema(example = "20", description = "The number of items on the page")
    @Min(value = 1, message = "The min page size value is {value}")
    @Max(value = 50, message = "The max page size value is {value}")
    private int size = DEFAULT_PAGE_SIZE;

    public static PageRequestDto of(int page, int size) {
        return new PageRequestDto(page, size);
    }

    public static PageRequestDto defaultRequest() {
        return new PageRequestDto();
    }
}
