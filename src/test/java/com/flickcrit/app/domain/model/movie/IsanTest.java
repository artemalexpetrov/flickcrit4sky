package com.flickcrit.app.domain.model.movie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IsanTest {

    @Test
    void testGenerateCreatesNonNullIsan() {
        // Act
        Isan generatedIsan = Isan.generate();

        // Assert
        assertNotNull(generatedIsan, "Generated ISAN object should not be null");
        assertNotNull(generatedIsan.value(), "The value of the generated ISAN should not be null");
    }

    @Test
    void testGenerateCreatesUniqueIsanValues() {
        // Act
        Isan isan1 = Isan.generate();
        Isan isan2 = Isan.generate();

        // Assert
        assertNotEquals(isan1.value(), isan2.value(), "Each generated ISAN should have a unique value");
    }
}