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
        // Если танк уже едет, не мешаем ему
        if (tank.isMoving()) return;

        // Стратегия выбирает направление
        Direction direction = strategy.chooseDirection();

        // Создаем команду (паттерн Команда)
        Movement action = new Movement(direction);

        // Проверяем, можно ли выполнить, и выполняем
        if (action.canBeExecuted(tank, rules)) {
            action.execute(handler);
        }
    }
}