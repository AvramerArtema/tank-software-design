package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Entity;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleBetweenTileCenters;

/**
 * Графическое представление сущности: спрайт и ограничивающий прямоугольник.
 * Связывает модельные координаты с пиксельными, поэтому сама модель о графике не знает.
 */
public class EntityGraphics {

    private final TextureRegion region;
    private final Rectangle rectangle;
    private final TiledMapTileLayer tileLayer;
    private final Interpolation interpolation;

    public EntityGraphics(TextureRegion region, TiledMapTileLayer tileLayer, Interpolation interpolation) {
        this.region = region;
        this.tileLayer = tileLayer;
        this.interpolation = interpolation;
        this.rectangle = new Rectangle()
                .setWidth(region.getRegionWidth())
                .setHeight(region.getRegionHeight());
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Ставит прямоугольник в соответствие с текущим положением сущности на сетке: центр текущей
     * клетки, центр клетки назначения и плавный переход между ними по прогрессу переезда.
     */
    public void updatePosition(Entity entity) {
        Coordinates from = entity.getCoordinates();
        Coordinates to = entity.getDestinationCoordinates();
        moveRectangleBetweenTileCenters(tileLayer, rectangle, from, to, entity.getMovementProgress(), interpolation);
    }

    public void render(Batch batch, float rotation) {
        drawTextureRegionUnscaled(batch, region, rectangle, rotation);
    }
}
