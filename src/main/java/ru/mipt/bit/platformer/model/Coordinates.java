package ru.mipt.bit.platformer.model;

import java.util.Objects;

/**
 * Позиция сущности на сетке уровня. Значимый тип: две координаты равны, если равны их компоненты.
 */
public final class Coordinates {

    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(Coordinates other) {
        this(other.x, other.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates translatedBy(int deltaX, int deltaY) {
        return new Coordinates(x + deltaX, y + deltaY);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Coordinates)) {
            return false;
        }
        Coordinates that = (Coordinates) other;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
