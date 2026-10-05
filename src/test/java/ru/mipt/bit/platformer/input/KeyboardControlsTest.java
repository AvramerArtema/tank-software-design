package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.logic.Movement;
import ru.mipt.bit.platformer.logic.PlayerAction;
import ru.mipt.bit.platformer.logic.Shoot;
import ru.mipt.bit.platformer.model.Direction;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Таблица связей «кнопка — действие»: единственное место, которое знает коды кнопок.
 */
class KeyboardControlsTest {

    private Input input;
    private KeyboardControls controls;

    @BeforeEach
    void setUp() {
        input = mock(Input.class);
        controls = new KeyboardControls() {
            @Override
            protected Input input() {
                return input;
            }
        };
    }

    private void press(int... keys) {
        for (int key : keys) {
            when(input.isKeyPressed(key)).thenReturn(true);
        }
    }

    private Direction moveDirectionOf(int key) {
        PlayerAction action = controls.actionOf(key);

        assertTrue(action instanceof Movement, "кнопка " + key + " должна запрашивать движение");
        return ((Movement) action).getDirection();
    }

    @Test
    void arrowKeysAreBoundToMovement() {
        assertEquals(Direction.UP, moveDirectionOf(UP));
        assertEquals(Direction.DOWN, moveDirectionOf(DOWN));
        assertEquals(Direction.LEFT, moveDirectionOf(LEFT));
        assertEquals(Direction.RIGHT, moveDirectionOf(RIGHT));
    }

    @Test
    void wasdKeysAreBoundToTheSameMovement() {
        assertEquals(Direction.UP, moveDirectionOf(W));
        assertEquals(Direction.DOWN, moveDirectionOf(S));
        assertEquals(Direction.LEFT, moveDirectionOf(A));
        assertEquals(Direction.RIGHT, moveDirectionOf(D));
    }

    @Test
    void spaceIsBoundToShooting() {
        assertSame(Shoot.INSTANCE, controls.actionOf(SPACE));
    }

    @Test
    void noKeysMeansNoActions() {
        assertTrue(controls.activeActions().isEmpty());
    }

    @Test
    void pressedKeyProducesItsAction() {
        press(UP);

        List<PlayerAction> actions = controls.activeActions();

        assertEquals(1, actions.size());
        assertTrue(actions.get(0) instanceof Movement);
        assertEquals(Direction.UP, ((Movement) actions.get(0)).getDirection());
    }

    @Test
    void severalKeysProduceSeveralActions() {
        press(SPACE, RIGHT);

        List<PlayerAction> actions = controls.activeActions();

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Shoot.INSTANCE));
    }

    @Test
    void movementIsReportedBeforeShootingWhenBothKeysAreHeld() {
        press(SPACE, UP);

        List<PlayerAction> actions = controls.activeActions();

        assertTrue(actions.get(0) instanceof Movement,
                "сначала танк должен повернуться, и только потом стрелять");
    }
}
