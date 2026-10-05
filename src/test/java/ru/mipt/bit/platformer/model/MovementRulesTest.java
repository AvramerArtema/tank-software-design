package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementRulesTest {

    private static final Coordinates TREE = new Coordinates(1, 3);

    @Test
    void emptyLevelHasNoObstacles() {
        MovementRules rules = new MovementRules();

        assertFalse(rules.isBlocked(new Coordinates(0, 0)));
        assertTrue(rules.canEnter(new Coordinates(0, 0)));
    }

    @Test
    void cellWithObstacleIsBlocked() {
        MovementRules rules = new MovementRules(new TreeObstacle(TREE));

        assertTrue(rules.isBlocked(TREE));
        assertFalse(rules.canEnter(TREE));
    }

    @Test
    void neighbouringCellStaysFree() {
        MovementRules rules = new MovementRules(new TreeObstacle(TREE));

        assertFalse(rules.isBlocked(new Coordinates(1, 2)));
        assertTrue(rules.canEnter(new Coordinates(2, 3)));
    }

    @Test
    void obstaclesCanBeGivenAsACollection() {
        TreeObstacle tree = new TreeObstacle(TREE);
        MovementRules rules = new MovementRules(Collections.singletonList(tree));

        assertTrue(rules.isBlocked(TREE));
        assertTrue(rules.getObstacles().contains(tree));
    }

    @Test
    void severalObstaclesAreAllBlocked() {
        MovementRules rules = new MovementRules(
                new TreeObstacle(TREE),
                new TreeObstacle(new Coordinates(4, 5)));

        assertTrue(rules.isBlocked(TREE));
        assertTrue(rules.isBlocked(new Coordinates(4, 5)));
        assertFalse(rules.isBlocked(new Coordinates(0, 0)));
    }
}
