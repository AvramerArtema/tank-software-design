package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.logic.Movement;
import ru.mipt.bit.platformer.logic.PlayerAction;
import ru.mipt.bit.platformer.logic.Shoot;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

/**
 * Единственное место, где кнопка связывается с действием. Добавить новую механику —
 * значит дописать одну строку в таблицу: игровой цикл и контроллер не меняются.
 *
 * Клавиатура берётся лениво, чтобы таблицу связей можно было проверять unit-тестами
 * без запуска libGDX.
 */
public class KeyboardControls implements Controls {

    /** Порядок связей задаёт порядок, в котором действия попадают в {@link #activeActions()}. */
    private final Map<Integer, PlayerAction> bindings = new LinkedHashMap<>();

    private Input input;

    public KeyboardControls() {
        bindDirection(UP, Direction.UP);
        bindDirection(W, Direction.UP);
        bindDirection(DOWN, Direction.DOWN);
        bindDirection(S, Direction.DOWN);
        bindDirection(LEFT, Direction.LEFT);
        bindDirection(A, Direction.LEFT);
        bindDirection(RIGHT, Direction.RIGHT);
        bindDirection(D, Direction.RIGHT);
        // выстрел заведён последним: сначала танк должен повернуться, и только потом стрелять
        bindings.put(SPACE, Shoot.INSTANCE);
    }

    /**
     * Действие, которое запрошено этой кнопкой.
     */
    public PlayerAction actionOf(int key) {
        return bindings.get(key);
    }

    @Override
    public List<PlayerAction> activeActions() {
        List<PlayerAction> actions = new ArrayList<>();
        for (Map.Entry<Integer, PlayerAction> binding : bindings.entrySet()) {
            if (input().isKeyPressed(binding.getKey())) {
                actions.add(binding.getValue());
            }
        }
        return actions;
    }

    /**
     * Клавиатура текущего приложения.
     */
    protected Input input() {
        if (input == null) {
            input = Gdx.input;
        }
        return input;
    }

    private void bindDirection(int key, Direction direction) {
        bindings.put(key, new Movement(direction));
    }
}
