package ru.mipt.bit.platformer.model;

/**
 * Препятствие, которое занимает клетку уровня и не даёт сущностям войти в неё.
 */
public interface Obstacle {

    Coordinates getCoordinates();
}
