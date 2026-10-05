package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.input.Controls;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Arrays;
import java.util.List;

/**
 * Превращает запрошенные игроком действия в действия над танком. Контроллер не знает,
 * какие механики существуют: он находит обработчик, который поддерживает действие.
 *
 * Проверка «можно ли туда ходить» — это правило хода, а не ввод и не механика танка,
 * поэтому вынесена в {@link MovementRules}.
 */
public class TankController {

    private final Tank tank;
    private final MovementRules movementRules;
    private final List<ActionHandler> handlers;

    public TankController(Tank tank, MovementRules movementRules, ActionHandler... handlers) {
        this(tank, movementRules, Arrays.asList(handlers));
    }

    public TankController(Tank tank, MovementRules movementRules, List<ActionHandler> handlers) {
        this.tank = tank;
        this.movementRules = movementRules;
        this.handlers = handlers;
    }

    public Tank getTank() {
        return tank;
    }

    /**
     * Выполнить действия, запрошенные игроком в этом кадре.
     */
    public void update(Controls controls) {
        for (PlayerAction action : controls.activeActions()) {
            if (canBeExecuted(action)) {
                execute(action);
            }
        }
    }

    /**
     * Ход разрешён, если танк не занят переездом и целевая клетка свободна.
     */
    public boolean canMove(Direction direction) {
        return !tank.isMoving() && movementRules.canEnter(direction.shift(tank.getCoordinates()));
    }

    private void execute(PlayerAction action) {
        for (ActionHandler handler : handlers) {
            if (action.canBeHandledBy(handler)) {
                action.execute(handler);
            }
        }
    }

    private boolean canBeExecuted(PlayerAction action) {
        return !(action instanceof Movement) || canMove(((Movement) action).getDirection());
    }
}
