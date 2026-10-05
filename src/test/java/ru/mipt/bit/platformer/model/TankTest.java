package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Механики танка: движение доступно игроку, а логика переезда между клетками общая
 * и проверяется в {@link EntityTest}.
 */
class TankTest {

    private static final float SPEED = 0.4f;

    private Tank tank;

    @BeforeEach
    void setUp() {
        tank = new Tank(new Coordinates(1, 1));
    }

    @Test
    void tankStartsFacingRight() {
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertFalse(tank.isMoving());
    }

    @Test
    void moveStartsTravellingToTheNeighbourCell() {
        assertTrue(tank.move(Direction.UP));

        assertTrue(tank.isMoving());
        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveIsRefusedWhileTankIsAlreadyMoving() {
        tank.move(Direction.UP);
        tank.updateMovement(SPEED / 2f, SPEED);

        assertFalse(tank.move(Direction.LEFT));

        assertEquals(Direction.UP, tank.getDirection(), "направление не меняется на ходу");
        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void moveTakesEffectAfterPreviousOneIsFinished() {
        tank.move(Direction.RIGHT);
        tank.updateMovement(SPEED, SPEED);

        assertTrue(tank.move(Direction.RIGHT));
        tank.updateMovement(SPEED, SPEED);

        assertEquals(new Coordinates(3, 1), tank.getCoordinates());
    }
}
