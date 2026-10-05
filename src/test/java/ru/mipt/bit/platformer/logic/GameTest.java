package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.graphics.EntityGraphics;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TreeObstacle;
import ru.mipt.bit.platformer.tank.TankActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GameTest {

    private static final float SPEED = 0.4f;

    private Tank tank;
    private TreeObstacle treeObstacle;
    private EntityGraphics tankGraphics;
    private EntityGraphics treeGraphics;
    private FakeControls controls;

    @BeforeEach
    void setUp() {
        tank = new Tank(new Coordinates(1, 1), SPEED);
        treeObstacle = new TreeObstacle(new Coordinates(1, 3));
        tankGraphics = mock(EntityGraphics.class);
        treeGraphics = mock(EntityGraphics.class);
        controls = new FakeControls();
    }

    private Game newGame() {
        MovementRules movementRules = new MovementRules(treeObstacle);
        TankController controller = new TankController(tank, movementRules, new TankActions(tank));
        return new Game(controller, controls,
                new Renderable(tank, tankGraphics),
                new Renderable(treeObstacle, treeGraphics));
    }

    @Test
    void updateAppliesPlayerActionToTank() {
        controls.pressMove(Direction.UP);

        newGame().update(0.016f);

        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void tankNeedsAnotherFrameToArrive() {
        controls.pressMove(Direction.RIGHT);
        Game game = newGame();

        game.update(0.016f);
        assertEquals(new Coordinates(1, 1), tank.getCoordinates(), "в кадре с началом хода танк ещё в старой клетке");

        game.update(SPEED);
        assertEquals(new Coordinates(2, 1), tank.getCoordinates(), "через время переезда танк в новой клетке");
    }

    @Test
    void updateSyncsGraphicsWithTheModel() {
        controls.pressMove(Direction.RIGHT);

        newGame().update(SPEED);

        verify(tankGraphics).updatePosition(tank);
        verify(treeGraphics).updatePosition(treeObstacle);
    }

    @Test
    void tankCannotEnterTheTreeObstacle() {
        tank = new Tank(new Coordinates(1, 2), SPEED);
        controls.pressMove(Direction.UP);

        newGame().update(0.016f);

        assertEquals(new Coordinates(1, 2), tank.getCoordinates());
        assertEquals(new Coordinates(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void renderDrawsTankWithItsRotationAndTreeUnrotated() {
        newGame().render(mock(Batch.class));

        verify(tankGraphics).render(any(Batch.class), eq(Direction.RIGHT.getRotation()));
        verify(treeGraphics).render(any(Batch.class), eq(0f));
    }

    @Test
    void addedRenderableIsUpdatedAndRenderedToo() {
        EntityGraphics extraGraphics = mock(EntityGraphics.class);
        TreeObstacle extraEntity = new TreeObstacle(new Coordinates(5, 5));
        Game game = newGame();

        game.add(new Renderable(extraEntity, extraGraphics));
        game.update(SPEED);
        game.render(mock(Batch.class));

        verify(extraGraphics).updatePosition(extraEntity);
        verify(extraGraphics).render(any(Batch.class), eq(0f));
    }
}
