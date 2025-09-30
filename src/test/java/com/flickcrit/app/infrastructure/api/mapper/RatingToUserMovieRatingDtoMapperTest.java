package com.flickcrit.app.infrastructure.api.mapper;


import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.rating.RatingValue;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.infrastructure.api.model.rating.UserMovieRatingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RatingToUserMovieRatingDtoMapperTest {

    private RatingToUserMovieRatingDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(RatingToUserMovieRatingDtoMapper.class);
    }

    @Test
    void givenUserMovieRatingWhenConvertToDtoExpectDtoWithSameValues() {
        // given
        Rating rating = Rating.builder()
            .userId(UserId.of(15L))
            .movieId(MovieId.of(25L))
            .value(RatingValue.of(4))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // when
        UserMovieRatingDto result = mapper.convert(rating);

        // then
        assertThat(result).isNotNull();
        assertThat(result.movieId()).isEqualTo(rating.getMovieId().value());
        assertThat(result.rating()).isEqualTo(rating.getValue().value());
        assertThat(result.createdAt()).isEqualTo(rating.getCreatedAt());
        assertThat(result.updatedAt()).isEqualTo(rating.getUpdatedAt());
    }
}