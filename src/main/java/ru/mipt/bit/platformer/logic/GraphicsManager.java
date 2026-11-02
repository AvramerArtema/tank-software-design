package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.graphics.EntityGraphics;
import ru.mipt.bit.platformer.graphics.GraphicsFactory;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TreeObstacle;

import java.util.HashMap;
import java.util.Map;

public class GraphicsManager implements LevelObserver {
    private final GraphicsFactory factory;
    private final Tank playerTank; // танк игрока
    private final Map<Entity, Renderable> renderables = new HashMap<>();

    // Передаем танк игрока в конструктор
    public GraphicsManager(GraphicsFactory factory, Tank playerTank) {
        this.factory = factory;
        this.playerTank = playerTank;
    }

    @Override
    public void onEntityAdded(Entity entity) {
        String texture = "images/tank_blue.png"; // Дефолт для ботов

        if (entity instanceof TreeObstacle) {
            texture = "images/greenTree.png";
        } else if (entity instanceof Bullet) {
            texture = "images/bullet.png"; // новая пуля
        } else if (entity == playerTank) {
            texture = "images/mytank.png"; // танк игрока
        }

        EntityGraphics graphics = factory.create(texture);
        renderables.put(entity, new EntityRenderable(entity, graphics));
    }

    @Override
    public void onEntityRemoved(Entity entity) {
        renderables.remove(entity);
    }

    public Iterable<Renderable> getRenderables() {
        return renderables.values();
    }
}