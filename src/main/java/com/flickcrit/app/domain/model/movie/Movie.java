package com.flickcrit.app.domain.model.movie;

import com.flickcrit.app.utils.ObjectUtils;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.Year;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movie {

    private final MovieId id;

    @EqualsAndHashCode.Include
    private final Isan isan;

    private String title;
    private Year year;

    public Movie(String title, Year year) {
        this(null, Isan.generate(), title, year);
    }

    @Builder
    Movie(@Nullable MovieId id, Isan isan, String title, Year year) {
        this.isan = ObjectUtils.requireNonNull(isan, "ISAN must be defined for a movie");
        this.id = id;
        setTitle(title);
        setYear(year);
    }

    /**
     * Sets the title of the movie. The title must not be blank.
     *
     * @param title the title of the movie to set
     * @return the instance of the Movie with the updated title
     * @throws IllegalArgumentException if the provided title is blank
     */
    public Movie setTitle(String title) {
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("Title must not be blank");
        }

        this.title = title.trim();
        return this;
    }

    /**
     * Sets the release year of the movie. The year must not be null.
     *
     * @param year the release year of the movie to set
     * @return the instance of the Movie with the updated year
     * @throws IllegalArgumentException if the provided year is null
     */
    public Movie setYear(Year year) {
        if (year == null) {
            throw new IllegalArgumentException("Year must not be null");
        }

        this.year = year;
        return this;
    }
}
