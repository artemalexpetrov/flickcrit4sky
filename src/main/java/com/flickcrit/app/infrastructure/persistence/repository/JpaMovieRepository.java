package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
interface JpaMovieRepository extends JpaRepository<MovieEntity, Long> {

    /**
     * Retrieves a list of MovieEntity objects whose IDs are contained within the specified collection.
     *
     * @param ids a collection of Long values representing the IDs of the movies to retrieve; must not be null.
     * @return a list of MovieEntity objects corresponding to the provided IDs.
     */
    List<MovieEntity> findByIdIn(Collection<Long> ids);

}
