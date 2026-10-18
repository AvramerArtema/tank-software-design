package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.model.Coordinates;

public class LevelConfig {
    public static final float TANK_SPEED = 0.4f;
    public static final Coordinates TANK_START = new Coordinates(1, 1);

    public static final String TANK_TEXTURE = "images/tank_blue.png";
    public static final String TREE_TEXTURE = "images/greenTree.png";

    public static final Coordinates[] TREES = {
            new Coordinates(1, 3),
            new Coordinates(3, 5) // Добавили второе дерево
    };
}