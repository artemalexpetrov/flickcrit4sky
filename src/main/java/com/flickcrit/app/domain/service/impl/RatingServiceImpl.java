package com.flickcrit.app.domain.service.impl;

import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.domain.model.rating.AverageRating;
import com.flickcrit.app.domain.model.rating.Rating;
import com.flickcrit.app.domain.model.user.UserId;
import com.flickcrit.app.domain.repository.RatingRepository;
import com.flickcrit.app.domain.service.RatingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class RatingServiceImpl implements RatingService {

    private final RatingRepository repository;

    @Override
    public AverageRating getMovieRating(MovieId movieId) {
        return repository
            .getMovieRating(movieId)
            .orElse(AverageRating.zero());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rating> findRating(@NonNull UserId userId, @NonNull MovieId movieId) {
        return repository.findRating(userId, movieId);
    }

    @Override
    @Transactional
    public Rating saveRating(@NonNull Rating rating) {
        Optional<Rating> rating1 = findRating(rating.getUserId(), rating.getMovieId());
        Rating ratingToSave = rating1.orElse(rating);
        ratingToSave.updateRating(rating.getValue());
        return repository.save(ratingToSave);
    }

    @Override
    @Transactional
    public void deleteRating(@NonNull Rating rating) {
        repository.delete(rating);
    }
}
