package com.flickcrit.app.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.UUID;

@Entity
@Getter
@Builder
@Table(name = "movie")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false)
    private UUID isan;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Year year;

}
