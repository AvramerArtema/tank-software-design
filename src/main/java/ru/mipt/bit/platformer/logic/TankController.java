package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.input.Controls;
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
public class TankController implements Controller{

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
            if (action.canBeExecuted(tank, movementRules)) {
                execute(action);
            }
        }
    }


    private void execute(PlayerAction action) {
        for (ActionHandler handler : handlers) {
            if (action.canBeHandledBy(handler)) {
                action.execute(handler);
            }
        }
    }

}
