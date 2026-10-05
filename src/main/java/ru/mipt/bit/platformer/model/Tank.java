package ru.mipt.bit.platformer.model;

/**
 * Танк игрока. Отдельный класс нужен, чтобы поведение, специфичное для танка
 * (например, стрельба), не смешивалось с общим перемещением сущностей.
 */
public class Tank extends Entity {

    private static final Direction DEFAULT_DIRECTION = Direction.RIGHT;

    public Tank(Coordinates coordinates) {
        super(coordinates, DEFAULT_DIRECTION);
    }
}
