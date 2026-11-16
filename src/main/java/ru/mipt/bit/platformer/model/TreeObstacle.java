package ru.mipt.bit.platformer.model;

/**
 * Дерево — неподвижное препятствие на уровне.
 */
public class TreeObstacle extends Entity implements Obstacle {

    private static final Direction DEFAULT_DIRECTION = Direction.RIGHT;
    private static final float NO_SPEED = 0f;

    public TreeObstacle(Coordinates coordinates) {
        super(coordinates, DEFAULT_DIRECTION, NO_SPEED);
    }
}
