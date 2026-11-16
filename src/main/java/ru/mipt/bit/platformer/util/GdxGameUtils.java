package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.mipt.bit.platformer.model.Coordinates;

import java.util.NoSuchElementException;

public final class GdxGameUtils {

    private GdxGameUtils() {
    }

    public static MapRenderer createSingleLayerMapRenderer(TiledMap tiledMap, Batch batch) {
        TiledMapTileLayer tileLayer = getSingleLayer(tiledMap);
        float viewWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        float viewHeight = tileLayer.getHeight() * tileLayer.getTileHeight();

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        mapRenderer.getViewBounds().set(0f, 0f, viewWidth, viewHeight);

        return mapRenderer;
    }

    public static <L extends MapLayer> L getSingleLayer(Map map) {
        MapLayers layers = map.getLayers();
        switch (layers.size()) {
            case 0:
                throw new NoSuchElementException("Map has no layers");
            case 1:
                @SuppressWarnings("unchecked")
                L layer = (L) layers.iterator().next();
                return layer;
            default:
                throw new IllegalArgumentException("Map has more than one layer");
        }
    }

    public static Rectangle moveRectangleAtTileCenter(TiledMapTileLayer tileLayer, Rectangle rectangle, Coordinates tileCoordinates) {
        return rectangle.setCenter(calculateTileCenter(tileLayer, tileCoordinates));
    }

    /**
     * Ставит прямоугольник в точку между центрами двух клеток: при progress = 0 он в центре
     * клетки from, при progress = 1 — в центре клетки to.
     */
    public static Rectangle moveRectangleBetweenTileCenters(TiledMapTileLayer tileLayer, Rectangle rectangle,
                                                           Coordinates from, Coordinates to, float progress,
                                                           Interpolation interpolation) {
        moveRectangleAtTileCenter(tileLayer, rectangle, from);
        float fromX = rectangle.x;
        float fromY = rectangle.y;

        moveRectangleAtTileCenter(tileLayer, rectangle, to);
        float toX = rectangle.x;
        float toY = rectangle.y;

        return rectangle
                .setX(interpolation.apply(fromX, toX, progress))
                .setY(interpolation.apply(fromY, toY, progress));
    }

    public static void drawTextureRegionUnscaled(Batch batch, TextureRegion region, Rectangle rectangle, float rotation) {
        int regionWidth = region.getRegionWidth();
        int regionHeight = region.getRegionHeight();
        float regionOriginX = regionWidth / 2f;
        float regionOriginY = regionHeight / 2f;
        batch.draw(region, rectangle.x, rectangle.y, regionOriginX, regionOriginY, regionWidth, regionHeight, 1f, 1f, rotation);
    }

    private static Vector2 calculateTileCenter(TiledMapTileLayer tileLayer, Coordinates tileCoordinates) {
        int tileWidth = tileLayer.getTileWidth();
        int tileHeight = tileLayer.getTileHeight();
        int tileBottomLeftCornerX = tileCoordinates.getX() * tileWidth;
        int tileBottomLeftCornerY = tileCoordinates.getY() * tileHeight;

        return new Rectangle()
                .setX(tileBottomLeftCornerX)
                .setY(tileBottomLeftCornerY)
                .setWidth(tileWidth)
                .setHeight(tileHeight)
                .getCenter(new Vector2());
    }
}
