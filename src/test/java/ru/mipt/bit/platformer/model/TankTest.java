package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        tank.move(Direction.UP); // Убрали assertTrue

        assertTrue(tank.isMoving());
        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test // <--- Вернули потерянную аннотацию
    void moveIsRefusedWhileTankIsAlreadyMoving() {
        tank.move(Direction.UP);
        tank.updateMovement(SPEED / 2f, SPEED);

        tank.move(Direction.LEFT); // Убрали assertFalse

        assertEquals(Direction.UP, tank.getDirection(), "направление не меняется на ходу");
        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void moveTakesEffectAfterPreviousOneIsFinished() {
        tank.move(Direction.RIGHT);
        tank.updateMovement(SPEED, SPEED);

        tank.move(Direction.RIGHT); // Убрали assertTrue
        tank.updateMovement(SPEED, SPEED);

        assertEquals(new Coordinates(3, 1), tank.getCoordinates());
    }
}