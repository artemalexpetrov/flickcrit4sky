package com.flickcrit.app.infrastructure.api.mapper;


import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.infrastructure.api.model.rating.AverageRatingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AverageRatingToAverageRatingDtoMapperTest {

    private AverageRatingToAverageRatingDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AverageRatingToAverageRatingDtoMapper.class);
    }

    @Test
    void givenAverageRatingWhenConvertToDtoExpectDtoWithSameValues() {
        // given
        AverageRating rating = new AverageRating(4.5, 100);

        // when
        AverageRatingDto result = mapper.convert(rating);

        // then
        assertNotNull(result);
        assertThat(result.value()).isEqualTo(rating.value());
        assertThat(result.ratesCount()).isEqualTo(rating.ratesCount());
    }
}