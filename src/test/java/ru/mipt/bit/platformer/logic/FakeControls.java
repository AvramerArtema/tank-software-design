package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.input.Controls;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Подставной источник действий игрока: вместо клавиатуры набор запрошенных действий
 * задаётся прямо из теста.
 */
public class FakeControls implements Controls {

    private final List<PlayerAction> actions = new ArrayList<>();

    public void press(PlayerAction action) {
        actions.add(action);
    }

    public void pressMove(Direction direction) {
        press(new Movement(direction));
    }

    public void pressShoot() {
        press(Shoot.INSTANCE);
    }

    public void releaseAll() {
        actions.clear();
    }

    @Override
    public List<PlayerAction> activeActions() {
        return new ArrayList<>(actions);
    }
}
