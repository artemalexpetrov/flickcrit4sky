package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.infrastructure.persistence.model.RatingEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class JpaRatingRepositoryIT extends BaseRepositoryIT {

    @Autowired
    private JpaRatingRepository repository;

    @Test
    void givenRatingWhenFindByUserAndMovieIdExpectRating() {
        // given
        disableForeignKeyChecks();
        long userId = 10L;
        long movieId = 20L;
        RatingEntity expectedEntity = repository.save(createRatingBuilder()
            .userId(userId)
            .movieId(movieId)
            .build());

        // when
        Optional<RatingEntity> foundRating = repository.findByUserIdAndMovieId(userId, movieId);

        // then
        assertThat(foundRating).contains(expectedEntity);
    }

    @Test
    void givenRatingsWhenGetMovieRatingExpectCorrectValue() {
        // given
        disableForeignKeyChecks();
        long movieId = 20L;
        int firstRating = 5;
        int secondRating = 3;
        repository.save(createRatingBuilder().userId(1L).movieId(movieId).rating(firstRating).build());
        repository.save(createRatingBuilder().userId(2L).movieId(movieId).rating(secondRating).build());

        // when
        Optional<AverageRating> averageRating = repository.getAverageRating(movieId);

        // then
        AverageRating expectedAverage = AverageRating.of(BigDecimal.valueOf((firstRating + secondRating) / 2), 2);
        assertThat(averageRating).contains(expectedAverage);
    }

    private static RatingEntity.RatingEntityBuilder createRatingBuilder() {
        return RatingEntity.builder()
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .userId(1L)
            .movieId(2L)
            .rating(3);
    }
}
