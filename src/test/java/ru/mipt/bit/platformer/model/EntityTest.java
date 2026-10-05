package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private static final float SPEED = 0.4f;
    private static final float FRAME = 0.016f;

    private Entity entity;

    @BeforeEach
    void setUp() {
        entity = new Tank(new Coordinates(1, 1));
    }

    @Test
    void entityStartsIdleAtItsCell() {
        assertFalse(entity.isMoving());
        assertEquals(1f, entity.getMovementProgress());
        assertEquals(new Coordinates(1, 1), entity.getCoordinates());
        assertEquals(entity.getCoordinates(), entity.getDestinationCoordinates());
    }

    @Test
    void startMovementAimsAtNeighbourTileAndResetsProgress() {
        entity.startMovement(Direction.UP);

        assertTrue(entity.isMoving());
        assertEquals(0f, entity.getMovementProgress());
        assertEquals(new Coordinates(1, 2), entity.getDestinationCoordinates());
        assertEquals(Direction.UP, entity.getDirection());
    }

    @Test
    void startMovementIsRefusedWhileEntityIsMoving() {
        entity.startMovement(Direction.UP);
        entity.updateMovement(0.1f, SPEED);
        float progressBefore = entity.getMovementProgress();

        entity.startMovement(Direction.LEFT);

        assertEquals(Direction.UP, entity.getDirection(), "направление не меняется на ходу");
        assertEquals(new Coordinates(1, 2), entity.getDestinationCoordinates(), "цель переезда не меняется");
        assertEquals(progressBefore, entity.getMovementProgress(), "прогресс переезда не сбрасывается");
    }

    @Test
    void startMovementWorksAgainAfterPreviousOneIsFinished() {
        entity.startMovement(Direction.UP);
        entity.updateMovement(SPEED, SPEED);

        entity.startMovement(Direction.LEFT);

        assertTrue(entity.isMoving());
        assertEquals(Direction.LEFT, entity.getDirection());
        assertEquals(new Coordinates(0, 1), entity.getDestinationCoordinates());
    }

    @Test
    void entityReachesDestinationAfterEnoughTime() {
        entity.startMovement(Direction.RIGHT);

        entity.updateMovement(SPEED, SPEED);

        assertFalse(entity.isMoving());
        assertEquals(1f, entity.getMovementProgress());
        assertEquals(new Coordinates(2, 1), entity.getCoordinates());
    }

    @Test
    void coordinatesStayUntilMovementIsFinished() {
        entity.startMovement(Direction.RIGHT);

        entity.updateMovement(SPEED / 2f, SPEED);

        assertTrue(entity.isMoving());
        assertEquals(new Coordinates(1, 1), entity.getCoordinates(), "пока танк едет, он ещё в старой клетке");
        assertEquals(new Coordinates(2, 1), entity.getDestinationCoordinates());
    }

    @Test
    void nextCoordinatesPointToTheNeighbourCell() {
        assertEquals(new Coordinates(1, 2), entity.getNextCoordinates());

        entity.startMovement(Direction.LEFT);

        assertEquals(new Coordinates(0, 1), entity.getNextCoordinates());
    }

    @Test
    void movementProgressNeverExceedsOne() {
        entity.startMovement(Direction.UP);

        entity.updateMovement(SPEED * 10f, SPEED);
        entity.updateMovement(SPEED * 10f, SPEED);

        assertEquals(1f, entity.getMovementProgress());
        assertEquals(new Coordinates(1, 2), entity.getCoordinates());
    }

    @Test
    void entityCanMoveAgainAfterFinishingPreviousMove() {
        entity.startMovement(Direction.UP);
        entity.updateMovement(SPEED, SPEED);

        entity.startMovement(Direction.UP);
        entity.updateMovement(SPEED, SPEED);

        assertEquals(new Coordinates(1, 3), entity.getCoordinates());
    }

    @Test
    void progressAdvancesProportionallyToTime() {
        entity.startMovement(Direction.DOWN);

        entity.updateMovement(FRAME, SPEED);
        float afterOneFrame = entity.getMovementProgress();

        entity.updateMovement(FRAME, SPEED);
        float afterTwoFrames = entity.getMovementProgress();

        assertEquals(FRAME / SPEED, afterOneFrame, 1e-6f);
        assertEquals(2f * FRAME / SPEED, afterTwoFrames, 1e-6f);
    }
}
