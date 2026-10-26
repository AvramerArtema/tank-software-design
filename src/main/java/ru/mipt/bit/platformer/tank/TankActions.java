package ru.mipt.bit.platformer.tank;

import ru.mipt.bit.platformer.logic.ActionHandler;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

/**
 * Механики танка, доступные игроку. Класс живёт рядом с танком и знает только про него,
 * поэтому новое действие танка описывается здесь отдельным методом, без правок игрового цикла.
 */
public class TankActions implements ActionHandler {

    private final Tank tank;

    public TankActions(Tank tank) {
        this.tank = tank;
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
        // TODO: стрельба
    }
}
