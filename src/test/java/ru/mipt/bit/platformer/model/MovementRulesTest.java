package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class MovementRulesTest {

    @Test
    public void testOutOfBounds() {
        // Поле 10 на 8
        MovementRules rules = new MovementRules(10, 8, Collections.emptyList());

        assertFalse(rules.canEnter(new Coordinates(10, 5))); // X за правой границей
        assertFalse(rules.canEnter(new Coordinates(5, 8)));  // Y за верхней границей
        assertFalse(rules.canEnter(new Coordinates(-1, 5))); // X за левой границей
        assertTrue(rules.canEnter(new Coordinates(9, 7)));   // Внутри
    }

    @Test
    public void testTankCollision() {
        MovementRules rules = new MovementRules(10, 8, Collections.emptyList());
        Tank tank1 = new Tank(new Coordinates(2, 2), 1.0f);
        Tank tank2 = new Tank(new Coordinates(3, 3), 1.0f);

        rules.setTanks(Arrays.asList(tank1, tank2));

        // Танк 2 стоит на (3,3), туда нельзя
        assertFalse(rules.canEnter(new Coordinates(3, 3)));

        // Танк 2 начал движение в (4,3). Теперь заняты и (3,3) и (4,3)
        tank2.startMovement(Direction.RIGHT);
        assertFalse(rules.canEnter(new Coordinates(3, 3))); // текущая клетка
        assertFalse(rules.canEnter(new Coordinates(4, 3))); // целевая клетка
    }
}