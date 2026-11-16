package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void shiftMovesCoordinatesToNeighbourTile() {
        Coordinates origin = new Coordinates(5, 5);

        assertEquals(new Coordinates(5, 6), Direction.UP.shift(origin));
        assertEquals(new Coordinates(5, 4), Direction.DOWN.shift(origin));
        assertEquals(new Coordinates(4, 5), Direction.LEFT.shift(origin));
        assertEquals(new Coordinates(6, 5), Direction.RIGHT.shift(origin));
    }

    @Test
    void shiftDoesNotChangeSourceCoordinates() {
        Coordinates origin = new Coordinates(5, 5);

        Direction.UP.shift(origin);

        assertEquals(new Coordinates(5, 5), origin);
    }

    @Test
    void rotationMatchesSpriteOrientation() {
        assertEquals(90f, Direction.UP.getRotation());
        assertEquals(-90f, Direction.DOWN.getRotation());
        assertEquals(-180f, Direction.LEFT.getRotation());
        assertEquals(0f, Direction.RIGHT.getRotation());
    }

    @Test
    void deltasAreUnitVectors() {
        for (Direction direction : Direction.values()) {
            assertEquals(1, Math.abs(direction.getDeltaX()) + Math.abs(direction.getDeltaY()),
                    "направление " + direction + " должно смещать ровно на одну клетку по одной оси");
        }
    }
}
