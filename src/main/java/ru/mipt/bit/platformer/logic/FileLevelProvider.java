package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Level;

import java.util.ArrayList;
import java.util.List;

public class FileLevelProvider implements LevelProvider {
    private final String filePath;
    private final int mapHeight; // Нужна для инверсии оси Y

    public FileLevelProvider(String filePath, int mapHeight) {
        this.filePath = filePath;
        this.mapHeight = mapHeight;
    }

    @Override
    public Level provide() {
        FileHandle file = Gdx.files.internal(filePath);
        String[] lines = file.readString().split("\n");

        Coordinates tankStart = null;
        List<Coordinates> trees = new ArrayList<>();

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y].trim();
            if (line.isEmpty()) continue;

            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                // Инвертируем Y, чтобы 0 был внизу, как в libGDX
                int gameY = (mapHeight - 1) - y;

                if (c == 'X') {
                    tankStart = new Coordinates(x, gameY);
                } else if (c == 'T') {
                    trees.add(new Coordinates(x, gameY));
                }
            }
        }

        if (tankStart == null) {
            throw new IllegalStateException("На уровне не найден танк (символ 'X')");
        }

        return new Level(tankStart, trees);
    }
}