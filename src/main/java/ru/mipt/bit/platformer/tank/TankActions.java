package ru.mipt.bit.platformer.tank;

import ru.mipt.bit.platformer.logic.ActionHandler;
import ru.mipt.bit.platformer.logic.GameWorld;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Bullet;

/**
 * Механики танка, доступные игроку. Класс живёт рядом с танком и знает только про него,
 * поэтому новое действие танка описывается здесь отдельным методом, без правок игрового цикла.
 */
public class TankActions implements ActionHandler {

    private final Tank tank;
    private final GameWorld gameWorld;
    private float shootCooldown = 0f; // Таймер перезарядки
    private static final float SHOOT_DELAY = 0.5f; // Полсекунды между выстрелами

    public TankActions(Tank tank, GameWorld gameWorld) {
        this.tank = tank;
        this.gameWorld = gameWorld;
    }

    @Override
    public boolean handlesMovement() {
        return true;
    }

    @Override
    public void move(Direction direction) {
        tank.move(direction);
    }

    /**
     * Выстрел пока не реализован: механика появится здесь, а ввод и игровой цикл
     * останутся без изменений.
     */
    @Override
    public boolean handlesShooting() {
        return true;
    }

    @Override
    public void shoot() {
        if (!tank.canShoot()) return; // Если идет перезарядка - выходим

        Coordinates bulletStart = tank.getNextCoordinates();
        Bullet bullet = new Bullet(bulletStart, tank.getDirection());
        gameWorld.addEntity(bullet);

        tank.resetShootCooldown(); // Запускаем перезарядку
    }
}

