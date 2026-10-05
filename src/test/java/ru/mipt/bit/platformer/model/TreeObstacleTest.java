package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TreeObstacleTest {

    @Test
    void treeStaysWhereItWasPlaced() {
        TreeObstacle tree = new TreeObstacle(new Coordinates(1, 3));

        assertFalse(tree.isMoving());
        assertEquals(new Coordinates(1, 3), tree.getCoordinates());
    }

    @Test
    void treeIsAnObstacleWithCoordinates() {
        Obstacle tree = new TreeObstacle(new Coordinates(1, 3));

        assertEquals(new Coordinates(1, 3), tree.getCoordinates());
    }

    @Test
    void treeKeepsItsCellWhileTimePasses() {
        TreeObstacle tree = new TreeObstacle(new Coordinates(1, 3));

        tree.updateMovement(1f, 0.4f);

        assertEquals(new Coordinates(1, 3), tree.getCoordinates());
        assertFalse(tree.isMoving());
    }
}
