package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.MathUtils;

/**
 * Сущность на уровне: занимает клетку сетки, смотрит в одном из направлений и умеет
 * плавно переезжать из текущей клетки в соседнюю. Логика перемещения общая для всех сущностей,
 * поэтому живёт здесь, а не в конкретных классах танка и препятствия.
 */
public abstract class Entity {

    private Coordinates coordinates;
    private Coordinates destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction;
    private float movementSpeed;

    /**
     * @param movementSpeed сколько времени занимает переезд в соседнюю клетку
     */
    protected Entity(Coordinates coordinates, Direction direction, float movementSpeed) {
        this.coordinates = new Coordinates(coordinates);
        this.destinationCoordinates = new Coordinates(coordinates);
        this.direction = direction;
        this.movementSpeed = movementSpeed;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Coordinates getDestinationCoordinates() {
        return destinationCoordinates;
    }

    /**
     * Клетка, в которую сущность переехала бы, если бы продолжила движение в текущем направлении.
     */
    public Coordinates getNextCoordinates() {
        return direction.shift(coordinates);
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Доля пройденного пути до клетки назначения: 1, если сущность стоит на месте.
     */
    public float getMovementProgress() {
        return movementProgress;
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    /**
     * Начать движение в заданном направлении. Сущность не может начать новый переезд,
     * пока не завершила предыдущий, — иначе она потеряла бы текущую позицию;
     * в этом случае направление остаётся прежним.
     */
    public void startMovement(Direction direction) {
        if (isMoving()) {
            return;
        }
        this.direction = direction;
        destinationCoordinates = direction.shift(coordinates);
        movementProgress = 0f;
    }

    /**
     * Продвинуть переезд на прошедшее время. Как только путь пройден, сущность
     * фиксируется в клетке назначения.
     */
    public void updateMovement(float deltaTime, float speed) {
        if (isMoving()) {
            movementProgress = MathUtils.clamp(movementProgress + deltaTime / speed, 0f, 1f);
        }
        if (!isMoving()) {
            coordinates = new Coordinates(destinationCoordinates);
        }
    }
}
