package ru.mipt.bit.platformer.tank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Обработчик механик танка: проверяем, что действия игрока доходят до самой модели танка.
 */
class TankActionsTest {

    private Tank tank;
    private TankActions actions;

    @BeforeEach
    void setUp() {
        tank = new Tank(new Coordinates(1, 1));
        actions = new TankActions(tank);
    }

    @Test
    void movementIsSupported() {
        assertTrue(actions.handlesMovement());
    }

    @Test
    void moveActionReachesTheTank() {
        actions.move(Direction.UP);

        assertTrue(tank.isMoving());
        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void shootingIsSupported() {
        assertTrue(actions.handlesShooting());
    }

    @Test
    void shootActionIsAccepted() {
        actions.shoot();

        // стрельба ещё не реализована, но вызов не должен ломать состояние танка
        assertEquals(new Coordinates(1, 1), tank.getCoordinates());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }
}
