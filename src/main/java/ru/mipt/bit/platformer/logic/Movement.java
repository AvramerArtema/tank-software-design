package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;

/**
 * Действие «начать движение в заданном направлении». Направление — параметр именно этого
 * действия, поэтому хранится здесь, а не там, где заведены кнопки.
 */
public final class Movement extends PlayerAction {

    private final Direction direction;

    public Movement(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean canBeHandledBy(ActionHandler handler) {
        return handler.handlesMovement();
    }

    @Override
    public void execute(ActionHandler handler) {
        handler.move(direction);
    }

    @Override
    public boolean canBeExecuted(Tank tank, MovementRules rules) {
        if (tank.isMoving()) return false;

        // Вычисляем, куда хотим поехать
        Coordinates target = direction.shift(tank.getCoordinates());

        // Спрашиваем у правил, можно ли туда
        return rules.canEnter(target);
    }
}