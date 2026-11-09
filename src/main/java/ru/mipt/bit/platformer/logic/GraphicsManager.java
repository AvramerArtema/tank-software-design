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
    private final Tank playerTank;
    private final Map<Entity, Renderable> renderables = new HashMap<>();

    private boolean healthBarsEnabled = false; // <--- НОВЫЙ ФЛАГ

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

        // ПАТТЕРН ДЕКОРАТОР: Если это танк, оборачиваем его в декоратор
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

    public Iterable<Renderable> getRenderables() {
        return renderables.values();
    }
}