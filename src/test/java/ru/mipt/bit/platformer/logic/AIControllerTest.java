package ru.mipt.bit.platformer.logic;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.tank.TankActions;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AIControllerTest {

    @Test
    public void testAIControllerMovesTank() {
        Tank tank = new Tank(new Coordinates(1, 1), 1.0f);
        MovementRules rules = new MovementRules(10, 10, Collections.emptyList());

        // Создаем стратегию, которая всегда возвращает RIGHT
        AIStrategy alwaysRight = () -> Direction.RIGHT;

        AIController controller = new AIController(tank, rules, new TankActions(tank), alwaysRight);

        // Игрок ничего не нажимает (передаем null, так как AI игнорирует controls)
        controller.update(null);

        // Проверяем, что танк начал движение направо
        assertTrue(tank.isMoving());
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertEquals(new Coordinates(2, 1), tank.getDestinationCoordinates());
    }
}