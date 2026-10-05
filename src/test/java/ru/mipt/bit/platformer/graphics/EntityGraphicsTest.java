package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Проверка того, что графическое представление переводит координаты модели в пиксельные:
 * тайлы 128x128, линейная интерполяция.
 */
class EntityGraphicsTest {

    private static final int TILE_SIZE = 128;

    private TiledMapTileLayer tileLayer;
    private Batch batch;
    private EntityGraphics graphics;

    @BeforeEach
    void setUp() {
        tileLayer = mock(TiledMapTileLayer.class);
        when(tileLayer.getTileWidth()).thenReturn(TILE_SIZE);
        when(tileLayer.getTileHeight()).thenReturn(TILE_SIZE);
        batch = mock(Batch.class);

        TextureRegion region = mock(TextureRegion.class);
        when(region.getRegionWidth()).thenReturn(TILE_SIZE);
        when(region.getRegionHeight()).thenReturn(TILE_SIZE);

        graphics = new EntityGraphics(region, tileLayer, Interpolation.linear);
    }

    @Test
    void boundingRectangleMatchesSpriteSize() {
        Rectangle rectangle = graphics.getRectangle();

        assertEquals(TILE_SIZE, rectangle.width);
        assertEquals(TILE_SIZE, rectangle.height);
    }

    @Test
    void idleEntityIsCenteredOnItsTile() {
        Tank tank = new Tank(new Coordinates(2, 3));

        graphics.updatePosition(tank);

        Rectangle rectangle = graphics.getRectangle();
        assertEquals(2 * TILE_SIZE, rectangle.x);
        assertEquals(3 * TILE_SIZE, rectangle.y);
    }

    @Test
    void halfFinishedMoveIsHalfwayBetweenTiles() {
        Tank tank = new Tank(new Coordinates(0, 0));
        tank.startMovement(Direction.RIGHT);
        tank.updateMovement(0.2f, 0.4f);

        graphics.updatePosition(tank);

        assertEquals(TILE_SIZE / 2f, graphics.getRectangle().x, 1e-4f);
        assertEquals(0f, graphics.getRectangle().y, 1e-4f);
    }

    @Test
    void finishedMoveIsCenteredOnDestinationTile() {
        Tank tank = new Tank(new Coordinates(0, 0));
        tank.startMovement(Direction.UP);
        tank.updateMovement(0.4f, 0.4f);

        graphics.updatePosition(tank);

        assertEquals(0f, graphics.getRectangle().x, 1e-4f);
        assertEquals(TILE_SIZE, graphics.getRectangle().y, 1e-4f);
    }

    @Test
    void renderDrawsSpriteWithGivenRotation() {
        Tank tank = new Tank(new Coordinates(1, 1));
        graphics.updatePosition(tank);

        graphics.render(batch, tank.getDirection().getRotation());

        verify(batch).draw(graphics.getRectangle().x, graphics.getRectangle().y,
                TILE_SIZE / 2f, TILE_SIZE / 2f, TILE_SIZE, TILE_SIZE, 1f, 1f, Direction.RIGHT.getRotation());
    }
}
