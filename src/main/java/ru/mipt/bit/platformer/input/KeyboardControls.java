package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.logic.GraphicsManager;
import ru.mipt.bit.platformer.logic.Movement;
import ru.mipt.bit.platformer.logic.PlayerAction;
import ru.mipt.bit.platformer.logic.Shoot;
import ru.mipt.bit.platformer.logic.ToggleHealthBarsAction;
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

public class KeyboardControls implements Controls {

    private final Map<Integer, PlayerAction> bindings = new LinkedHashMap<>();
    private Input input;

    // Теперь конструктор принимает GraphicsManager
    public KeyboardControls(GraphicsManager graphicsManager) {
        bindDirection(UP, Direction.UP);
        bindDirection(W, Direction.UP);
        bindDirection(DOWN, Direction.DOWN);
        bindDirection(S, Direction.DOWN);
        bindDirection(LEFT, Direction.LEFT);
        bindDirection(A, Direction.LEFT);
        bindDirection(RIGHT, Direction.RIGHT);
        bindDirection(D, Direction.RIGHT);

        bindings.put(SPACE, Shoot.INSTANCE);

        // ПРИВЯЗКА ALT (Левый и правый)
        ToggleHealthBarsAction toggleAction = new ToggleHealthBarsAction(graphicsManager);
        bindings.put(Input.Keys.ALT_LEFT, toggleAction);
        bindings.put(Input.Keys.ALT_RIGHT, toggleAction);
    }

    public PlayerAction actionOf(int key) {
        return bindings.get(key);
    }

    @Override
    public List<PlayerAction> activeActions() {
        List<PlayerAction> actions = new ArrayList<>();
        for (Map.Entry<Integer, PlayerAction> binding : bindings.entrySet()) {
            PlayerAction action = binding.getValue();
            int key = binding.getKey();

            // Если это переключатель (Toggle), используем isKeyJustPressed (только в момент нажатия)
            if (action instanceof ToggleHealthBarsAction) {
                if (input().isKeyJustPressed(key)) {
                    actions.add(action);
                }
            }
            // Для остальных действий (движение, стрельба) используем isKeyPressed (можно зажать)
            else {
                if (input().isKeyPressed(key)) {
                    actions.add(action);
                }
            }
        }
        return actions;
    }

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