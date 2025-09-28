package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.infrastructure.persistence.model.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaMovieRepository extends JpaRepository<MovieEntity, Long> {


}
