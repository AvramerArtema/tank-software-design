package ru.mipt.bit.platformer.model;

/**
 * Направление движения и поворота сущности. Единственное место, где задано соответствие
 * между направлением, смещением на сетке и углом поворота спрайта.
 */
public enum Direction {

    UP(0, 1, 90f),
    DOWN(0, -1, -90f),
    LEFT(-1, 0, -180f),
    RIGHT(1, 0, 0f);

    private final int deltaX;
    private final int deltaY;
    private final float rotation;

    Direction(int deltaX, int deltaY, float rotation) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.rotation = rotation;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public float getRotation() {
        return rotation;
    }

    /**
     * Соседняя клетка в этом направлении.
     */
    public Coordinates shift(Coordinates from) {
        return from.translatedBy(deltaX, deltaY);
    }
}
