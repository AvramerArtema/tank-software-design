package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Единственное место, где текстура из файла превращается в графическое представление сущности,
 * и единственный владелец загруженных текстур — native-ресурсов, которые нужно освобождать.
 * Благодаря этому лаунчер не знает ни про Texture, ни про TextureRegion.
 */
public class GraphicsFactory implements Disposable {

    private final TiledMapTileLayer tileLayer;
    private final Interpolation interpolation;
    private final List<Texture> textures = new ArrayList<>();

    public GraphicsFactory(TiledMapTileLayer tileLayer, Interpolation interpolation) {
        this.tileLayer = tileLayer;
        this.interpolation = interpolation;
    }

    public EntityGraphics create(String texturePath) {
        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
        Texture texture = new Texture(texturePath);
        textures.add(texture);
        return new EntityGraphics(new TextureRegion(texture), tileLayer, interpolation);
    }

    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }
}
