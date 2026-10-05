package ru.mipt.bit.platformer.model;

/**
 * Танк игрока. Здесь описаны механики, которые принадлежат именно танку и доступны игроку:
 * сейчас это движение, дальше — стрельба. Скорость танк носит с собой, поэтому она
 * не обязана быть общей для всех сущностей уровня.
 */
public class Tank extends Entity {

    private static final Direction DEFAULT_DIRECTION = Direction.RIGHT;
    private static final float DEFAULT_SPEED = 0.4f;

    public Tank(Coordinates coordinates) {
        this(coordinates, DEFAULT_SPEED);
    }

    /**
     * @param movementSpeed сколько времени занимает переезд в соседнюю клетку
     */
    public Tank(Coordinates coordinates, float movementSpeed) {
        super(coordinates, DEFAULT_DIRECTION, movementSpeed);
    }

    /**
     * Начать движение в заданном направлении, если танк сейчас не занят переездом.
     */
    public void move(Direction direction) {
        startMovement(direction);
    }
}
