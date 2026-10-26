package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomLevelProvider implements LevelProvider {
    private final int width;
    private final int height;
    private final int treeCount;
    private final Random random = new Random();

    public RandomLevelProvider(int width, int height, int treeCount) {
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
    }

    @Override
    public Level provide() {
        Set<Coordinates> occupied = new HashSet<>();

        Coordinates tankStart = new Coordinates(random.nextInt(width), random.nextInt(height));
        occupied.add(tankStart);

        List<Coordinates> trees = new ArrayList<>();
        while (trees.size() < treeCount) {
            Coordinates treePos = new Coordinates(random.nextInt(width), random.nextInt(height));
            if (!occupied.contains(treePos)) {
                occupied.add(treePos);
                trees.add(treePos);
            }
        }

        // Генерируем 3 ботов
        List<Coordinates> bots = new ArrayList<>();
        int botCount = 3;
        while (bots.size() < botCount) {
            Coordinates botPos = new Coordinates(random.nextInt(width), random.nextInt(height));
            if (!occupied.contains(botPos)) {
                occupied.add(botPos);
                bots.add(botPos);
            }
        }

        return new Level(tankStart, bots, trees);
    }
}