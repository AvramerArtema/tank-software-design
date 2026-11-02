package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.input.Controls;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Entity;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

public class Game {
    private final List<Controller> controllers;
    private final Controls controls;
    private final GameWorld world;
    private final GraphicsManager graphicsManager;

    public Game(List<Controller> controllers, Controls controls, GameWorld world, GraphicsManager graphicsManager) {
        this.controllers = controllers;
        this.controls = controls;
        this.world = world;
        this.graphicsManager = graphicsManager;
    }

    public void update(float deltaTime) {
        // 1. Выполняем команды (Игрок и AI)
        for (Controller controller : controllers) {
            controller.update(controls);
        }

        // 2. Тик для всех объектов (движение, коллизии)
        List<Entity> entities = world.getEntities();
        for (Entity entity : List.copyOf(entities)) {
            if (entity instanceof Bullet) {
                Bullet bullet = (Bullet) entity;

                // Двигаем пулю
                bullet.updateMovement(deltaTime, bullet.getMovementSpeed());

                // Проверяем столкновения
                if (checkBulletCollisions(bullet)) {
                    continue; // Если попала - удалена, идем дальше
                }

                // Если пуля долетела до клетки и НЕ попала - летим дальше
                if (!bullet.isMoving()) {
                    Coordinates nextPos = bullet.getNextCoordinates();
                    // Проверяем, не вылетит ли за границы (поле 10x8)
                    if (nextPos.getX() < 0 || nextPos.getX() >= 10 || nextPos.getY() < 0 || nextPos.getY() >= 8) {
                        world.removeEntity(bullet);
                        continue;
                    }
                    // Запускаем следующий шаг
                    bullet.startMovement(bullet.getDirection());
                }

            } else if (entity instanceof Tank) {
                Tank tank = (Tank) entity;
                tank.tick(deltaTime); // Обновляем перезарядку
                tank.updateMovement(deltaTime, tank.getMovementSpeed());
                if (tank.isDead()) {
                    world.removeEntity(tank);
                }
            }
        }

        // 3. Обновляем графику
        for (Renderable r : graphicsManager.getRenderables()) {
            r.update(deltaTime);
        }
    }

    private boolean checkBulletCollisions(Bullet bullet) {
        for (Entity target : world.getEntities()) {
            if (target == bullet) continue;

            // Проверяем и текущую клетку цели, и клетку назначения (если танк едет)
            if (target.getCoordinates().equals(bullet.getCoordinates()) ||
                    target.getDestinationCoordinates().equals(bullet.getCoordinates())) {

                if (target instanceof Tank) {
                    Tank tank = (Tank) target;
                    tank.takeDamage(bullet.getDamage());
                }
                world.removeEntity(bullet);
                return true; // Пуля уничтожена
            }
        }
        return false; // Пуля летит дальше
    }

    public void render(Batch batch) {
        for (Renderable r : graphicsManager.getRenderables()) {
            r.render(batch);
        }
    }
}