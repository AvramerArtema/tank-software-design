package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.graphics.EntityGraphics;
import ru.mipt.bit.platformer.model.Entity;

/**
 * Сущность вместе с её графическим представлением: то, что игровой цикл обновляет
 * и отрисовывает, не зная, танк это, дерево или будущий снаряд.
 */
public class Renderable {

    private final Entity entity;
    private final EntityGraphics graphics;

    public Renderable(Entity entity, EntityGraphics graphics) {
        this.entity = entity;
        this.graphics = graphics;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityGraphics getGraphics() {
        return graphics;
    }

    public void update(float deltaTime) {
        entity.updateMovement(deltaTime, entity.getMovementSpeed());
        graphics.updatePosition(entity);
    }

    public void render(Batch batch) {
        graphics.render(batch, entity.getDirection().getRotation());
    }
}
