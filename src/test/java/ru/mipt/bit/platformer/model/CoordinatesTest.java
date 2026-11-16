package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {

    @Test
    void coordinatesWithSameValuesAreEqual() {
        assertEquals(new Coordinates(1, 2), new Coordinates(1, 2));
        assertEquals(new Coordinates(1, 2).hashCode(), new Coordinates(1, 2).hashCode());
    }

    @Test
    void coordinatesWithDifferentValuesAreNotEqual() {
        assertNotEquals(new Coordinates(1, 2), new Coordinates(2, 1));
        assertNotEquals(new Coordinates(1, 2), null);
        assertNotEquals(new Coordinates(1, 2), "1,2");
    }

    @Test
    void copyConstructorCopiesValues() {
        Coordinates original = new Coordinates(3, 4);
        Coordinates copy = new Coordinates(original);

        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void translatedByMovesOnlyAlongGivenAxis() {
        Coordinates coordinates = new Coordinates(2, 2);

        assertEquals(new Coordinates(2, 3), coordinates.translatedBy(0, 1));
        assertEquals(new Coordinates(1, 2), coordinates.translatedBy(-1, 0));
        assertEquals(new Coordinates(2, 2), coordinates);
    }

    @Test
    void gettersReturnConstructedValues() {
        Coordinates coordinates = new Coordinates(-3, 7);

        assertEquals(-3, coordinates.getX());
        assertEquals(7, coordinates.getY());
    }
}
