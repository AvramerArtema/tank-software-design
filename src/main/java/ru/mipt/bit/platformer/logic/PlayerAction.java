package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;

public abstract class PlayerAction {

    public abstract boolean canBeHandledBy(ActionHandler handler);

    public abstract void execute(ActionHandler handler);

    // Сигнатура должна совпадать с той, что в Movement
    public boolean canBeExecuted(Tank tank, MovementRules rules) {
        return true;
    }
}