package com.flickcrit.app.infrastructure.api.model.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.time.Year;

/**
 * This record represents a request to create a new movie
 */
@Builder
@Validated
public record MovieCreateRequest(

    @NotBlank(message = "Movie title cannot be blank")
    @Size(max = 255, message = "Movie title must not exceed 255 characters")
    String title,

    @Min(value = 1888, message = "Movie release year must be after {1}")
    @Max(value = 2099, message = "Movie release year must be before {1}")
    @NotNull(message = "Movie release year is required")
    Integer year

) {
    /**
     * Converts the release year from an integer to a {@code Year} object.
     *
     * @return the release year as a {@code Year} object
     */
    @JsonIgnore
    public Year getYearAsObject() {
        return Year.of(year);
    }
}
