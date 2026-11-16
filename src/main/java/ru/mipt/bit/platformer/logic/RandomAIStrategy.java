package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.Direction;
import java.util.Random;

public class RandomAIStrategy implements AIStrategy {
    private final Random random = new Random();

    @Override
    public Direction chooseDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }
}