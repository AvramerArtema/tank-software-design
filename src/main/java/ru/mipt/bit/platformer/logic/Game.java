package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.input.Controls;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

/**
 * APPLICATION: Use-case (Сценарий игры).
 * Управляет игровым циклом, но зависит только от абстракций (Портов).
 */
public class Game {
    private final List<Controller> controllers;
    private final Controls controls;
    private final GameWorld world;
    private final LevelRenderer renderer; // <--- ТЕПЕРЬ ЭТО ПОРТ (ИНТЕРФЕЙС)

    public Game(List<Controller> controllers, Controls controls, GameWorld world, LevelRenderer renderer) {
        this.controllers = controllers;
        this.controls = controls;
        this.world = world;
        this.renderer = renderer;
    }

    public void update(float deltaTime) {
        for (Controller controller : controllers) {
            controller.update(controls);
        }

        List<Entity> entities = world.getEntities();
        for (Entity entity : List.copyOf(entities)) {
            if (entity instanceof Bullet) {
                Bullet bullet = (Bullet) entity;
                bullet.updateMovement(deltaTime, bullet.getMovementSpeed());

                if (checkBulletCollisions(bullet)) {
                    continue;
                }

                if (!bullet.isMoving()) {
                    Coordinates nextPos = bullet.getNextCoordinates();
                    if (nextPos.getX() < 0 || nextPos.getX() >= 10 || nextPos.getY() < 0 || nextPos.getY() >= 8) {
                        world.removeEntity(bullet);
                        continue;
                    }
                    bullet.startMovement(bullet.getDirection());
                }

            } else if (entity instanceof Tank) {
                Tank tank = (Tank) entity;
                tank.tick(deltaTime);
                tank.updateMovement(deltaTime, tank.getMovementSpeed());
                if (tank.isDead()) {
                    world.removeEntity(tank);
                }
            }
        }

        // Обновляем графику через Порт
        for (Renderable r : renderer.getRenderables()) {
            r.update(deltaTime);
        }
    }

    private boolean checkBulletCollisions(Bullet bullet) {
        for (Entity target : world.getEntities()) {
            if (target == bullet) continue;

            if (target.getCoordinates().equals(bullet.getCoordinates()) ||
                    target.getDestinationCoordinates().equals(bullet.getCoordinates())) {

                if (target instanceof Tank) {
                    Tank tank = (Tank) target;
                    tank.takeDamage(bullet.getDamage());
                }
                world.removeEntity(bullet);
                return true;
            }
        }
        return false;
    }

    // Отрисовка тоже через Порт
    public void render(Batch batch) {
        renderer.render(batch); // <--- Делегируем отрисовку адаптеру
    }
}