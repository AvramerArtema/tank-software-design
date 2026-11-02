package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.input.Controls;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;

public class AIController implements Controller {
    private final Tank tank;
    private final MovementRules rules;
    private final ActionHandler handler;
    private final AIStrategy strategy;

    public AIController(Tank tank, MovementRules rules, ActionHandler handler, AIStrategy strategy) {
        this.tank = tank;
        this.rules = rules;
        this.handler = handler;
        this.strategy = strategy;
    }

    @Override
    public void update(Controls controls) {
        if (tank.isMoving()) return;

        // 20% шанс выстрелить вместо движения
        if (Math.random() < 0.1) {
            Shoot shootAction = Shoot.INSTANCE;

            if (shootAction.canBeExecuted(tank, rules)) {
                shootAction.execute(handler);
                return; // В этом кадре только стреляем
            }
        }

        // Иначе двигаемся как раньше
        Direction direction = strategy.chooseDirection();
        Movement action = new Movement(direction);
        if (action.canBeExecuted(tank, rules)) {
            action.execute(handler);
        }
    }
}