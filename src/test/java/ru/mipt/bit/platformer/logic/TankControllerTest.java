package ru.mipt.bit.platformer.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TreeObstacle;
import ru.mipt.bit.platformer.tank.TankActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankControllerTest {

    private static final float SPEED = 0.4f;
    private static final Coordinates TANK_START = new Coordinates(1, 1);

    private Tank tank;
    private TankController controller;
    private FakeControls controls;

    @BeforeEach
    void setUp() {
        tank = new Tank(TANK_START);
        MovementRules movementRules = new MovementRules(new TreeObstacle(new Coordinates(1, 3)));
        controller = new TankController(tank, movementRules, new TankActions(tank));
        controls = new FakeControls();
    }

    private void rebuildWithTreeAt(Coordinates treePosition) {
        MovementRules movementRules = new MovementRules(new TreeObstacle(treePosition));
        controller = new TankController(tank, movementRules, new TankActions(tank));
    }

    @Test
    void tankMovesInRequestedDirection() {
        controls.pressMove(Direction.UP);

        controller.update(controls);

        assertTrue(tank.isMoving());
        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void tankStaysIdleWithoutRequestedActions() {
        controller.update(controls);

        assertFalse(tank.isMoving());
        assertEquals(TANK_START, tank.getCoordinates());
    }

    @Test
    void moveIntoObstacleIsRejected() {
        rebuildWithTreeAt(new Coordinates(1, 2));
        controls.pressMove(Direction.UP);

        controller.update(controls);

        assertFalse(tank.isMoving(), "в дерево врезаться нельзя");
        assertEquals(TANK_START, tank.getCoordinates());
    }

    @Test
    void canMoveIsFalseWhenTheNeighbourCellIsTakenByTree() {
        rebuildWithTreeAt(new Coordinates(2, 1));

        assertFalse(controller.canMove(Direction.RIGHT));
        assertTrue(controller.canMove(Direction.UP), "остальные направления свободны");
    }

    @Test
    void canMoveIsFalseWhileTankIsMoving() {
        controls.pressMove(Direction.UP);
        controller.update(controls);

        assertFalse(controller.canMove(Direction.RIGHT));
    }

    @Test
    void secondMoveDuringTravelDoesNotChangeDestination() {
        controls.pressMove(Direction.UP);
        controller.update(controls);
        tank.updateMovement(SPEED / 2f, SPEED);

        controls.releaseAll();
        controls.pressMove(Direction.LEFT);
        controller.update(controls);

        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates(), "цель переезда не меняется");
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void blockedMoveDoesNotTurnTheTank() {
        rebuildWithTreeAt(new Coordinates(1, 2));
        controls.pressMove(Direction.UP);

        controller.update(controls);

        assertEquals(Direction.RIGHT, tank.getDirection(), "разворот происходит только вместе с начатым ходом");
    }

    @Test
    void tankKeepsMovingWhileKeyIsHeld() {
        controls.pressMove(Direction.RIGHT);

        controller.update(controls);
        tank.updateMovement(SPEED, SPEED);
        controller.update(controls);

        assertEquals(new Coordinates(3, 1), tank.getDestinationCoordinates());
    }

    @Test
    void shootActionDoesNotBreakMovement() {
        controls.pressShoot();

        controller.update(controls);

        assertFalse(tank.isMoving());
        assertEquals(TANK_START, tank.getCoordinates());
    }

    @Test
    void controllerExposesItsTank() {
        assertEquals(tank, controller.getTank());
    }
}
