package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Правило, куда можно ходить: проверка границ, препятствий и других танков.
 */
public class MovementRules {

    private final int width;
    private final int height;
    private final Collection<Obstacle> obstacles;
    private Collection<Tank> tanks;

    public MovementRules(int width, int height, Collection<Obstacle> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = Collections.unmodifiableCollection(new ArrayList<>(obstacles));
        this.tanks = new ArrayList<>();
    }

    // Конструктор для обратной совместимости (тесты из ДЗ 3)
    public MovementRules(Obstacle... obstacles) {
        this(10, 8, Arrays.asList(obstacles));
    }

    public void setTanks(Collection<Tank> tanks) {
        this.tanks = tanks;
    }

    public boolean isBlocked(Coordinates coordinates) {
        // 1. Проверка границ
        if (coordinates.getX() < 0 || coordinates.getX() >= width ||
                coordinates.getY() < 0 || coordinates.getY() >= height) {
            return true;
        }

        // 2. Проверка препятствий (деревьев)
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(coordinates)) {
                return true;
            }
        }

        // 3. Проверка танков (заняты текущая и целевая клетки)
        for (Tank tank : tanks) {
            if (tank.getCoordinates().equals(coordinates) ||
                    tank.getDestinationCoordinates().equals(coordinates)) {
                return true;
            }
        }

        return false;
    }

    public boolean canEnter(Coordinates coordinates) {
        return !isBlocked(coordinates);
    }
}