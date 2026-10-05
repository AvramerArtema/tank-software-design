package ru.mipt.bit.platformer.model;

/**
 * Дерево — неподвижное препятствие на уровне.
 */
public class TreeObstacle extends Entity {

    private static final Direction DEFAULT_DIRECTION = Direction.RIGHT;

    public TreeObstacle(Coordinates coordinates) {
        super(coordinates, DEFAULT_DIRECTION);
    }
}
