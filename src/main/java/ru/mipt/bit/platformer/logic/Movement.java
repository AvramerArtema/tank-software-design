package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.Direction;

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
}
