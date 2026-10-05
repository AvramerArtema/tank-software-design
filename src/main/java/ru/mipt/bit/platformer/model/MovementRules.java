package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Правило, куда можно ходить: клетка занята препятствием или нет. Живёт отдельно от
 * сущностей и от ввода, поэтому проверяется unit-тестами напрямую.
 */
public class MovementRules {

    private final Collection<Obstacle> obstacles;

    public MovementRules(Obstacle... obstacles) {
        this(Arrays.asList(obstacles));
    }

    public MovementRules(Collection<Obstacle> obstacles) {
        this.obstacles = Collections.unmodifiableCollection(new ArrayList<>(obstacles));
    }

    public boolean isBlocked(Coordinates coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(coordinates)) {
                return true;
            }
        }
        return false;
    }

    public boolean canEnter(Coordinates coordinates) {
        return !isBlocked(coordinates);
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(new ArrayList<>(obstacles));
    }
}
