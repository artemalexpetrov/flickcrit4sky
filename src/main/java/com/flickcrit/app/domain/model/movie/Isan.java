package com.flickcrit.app.domain.model.movie;

import lombok.NonNull;

import java.util.UUID;

/**
 * Represents a fake International Standard Audiovisual Number (ISAN)
 * for uniquely identifying audiovisual works.
 *
 * @param value the UUID representing the ISAN
 */
public record Isan(UUID value) {

    public Isan(@NonNull UUID value) {
        this.value = value;
    }

    public static Isan of(UUID isan) {
        return new Isan(isan);
    }

    public static Isan generate() {
        return new Isan(UUID.randomUUID());
    }
}
