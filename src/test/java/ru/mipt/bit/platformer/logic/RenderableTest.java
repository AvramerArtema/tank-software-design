package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.graphics.EntityGraphics;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
/**
 * Игровой цикл работает с парой «сущность + графика» и не знает, что это за сущность.
 */
class RenderableTest {

    private static final float SPEED = 0.4f;

    private Tank tank;
    private EntityGraphics graphics;
    private Renderable renderable;

    @BeforeEach
    void setUp() {
        tank = new Tank(new Coordinates(1, 1), SPEED);
        graphics = mock(EntityGraphics.class);
        renderable = new Renderable(tank, graphics);
    }

    @Test
    void exposesItsEntityAndGraphics() {
        assertEquals(tank, renderable.getEntity());
        assertEquals(graphics, renderable.getGraphics());
    }

    @Test
    void updateAdvancesEntityMovementWithItsOwnSpeed() {
        tank.move(Direction.RIGHT);

        renderable.update(SPEED);

        assertEquals(new Coordinates(2, 1), tank.getCoordinates(), "сущность должна переехать сама");
    }

    @Test
    void updatePlacesGraphicsAtTheEntityPosition() {
        renderable.update(SPEED);

        verify(graphics).updatePosition(tank);
    }

    @Test
    void renderUsesTheEntityRotation() {
        tank.move(Direction.UP);

        renderable.render(mock(Batch.class));

        verify(graphics).render(any(Batch.class), eq(90f));
    }
}
