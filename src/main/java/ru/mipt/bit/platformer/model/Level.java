package ru.mipt.bit.platformer.model;

import java.util.List;

public class Level {
    private final Coordinates tankStart;
    private final List<Coordinates> botStarts; // Новое поле
    private final List<Coordinates> trees;

    public Level(Coordinates tankStart, List<Coordinates> botStarts, List<Coordinates> trees) {
        this.tankStart = tankStart;
        this.botStarts = botStarts;
        this.trees = trees;
    }

    public Coordinates getTankStart() { return tankStart; }
    public List<Coordinates> getBotStarts() { return botStarts; }
    public List<Coordinates> getTrees() { return trees; }
}