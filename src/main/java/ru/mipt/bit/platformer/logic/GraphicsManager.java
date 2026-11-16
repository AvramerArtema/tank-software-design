package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.graphics.EntityGraphics;
import ru.mipt.bit.platformer.graphics.GraphicsFactory;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TreeObstacle;

import java.util.HashMap;
import java.util.Map;

/**
 * INFRASTRUCTURE: Адаптер (Adapter).
 * Реализует порт LevelRenderer (для отрисовки) и LevelObserver (для получения событий).
 */
public class GraphicsManager implements LevelObserver, LevelRenderer {
    private final GraphicsFactory factory;
    private final Tank playerTank;
    private final Map<Entity, Renderable> renderables = new HashMap<>();

    private boolean healthBarsEnabled = false;

    public GraphicsManager(GraphicsFactory factory, Tank playerTank) {
        this.factory = factory;
        this.playerTank = playerTank;
    }

    public void toggleHealthBars() {
        this.healthBarsEnabled = !this.healthBarsEnabled;
    }

    public boolean isHealthBarsEnabled() {
        return healthBarsEnabled;
    }

    @Override
    public void onEntityAdded(Entity entity) {
        String texture = "images/tank_blue.png";

        if (entity instanceof TreeObstacle) {
            texture = "images/greenTree.png";
        } else if (entity instanceof Bullet) {
            texture = "images/bullet.png";
        } else if (entity == playerTank) {
            texture = "images/mytank.png";
        }

        EntityGraphics graphics = factory.create(texture);
        Renderable baseRenderable = new EntityRenderable(entity, graphics);

        if (entity instanceof Tank) {
            Tank tank = (Tank) entity;
            renderables.put(entity, new HealthBarDecorator(baseRenderable, tank, this));
        } else {
            renderables.put(entity, baseRenderable);
        }
    }

    @Override
    public void onEntityRemoved(Entity entity) {
        renderables.remove(entity);
    }

    @Override
    public Iterable<Renderable> getRenderables() {
        return renderables.values();
    }

    // НОВЫЙ МЕТОД ИЗ ИНТЕРФЕЙСА: Теперь GraphicsManager сам умеет рисовать всё, что у него есть
    @Override
    public void render(Batch batch) {
        for (Renderable r : renderables.values()) {
            r.render(batch);
        }
    }
}