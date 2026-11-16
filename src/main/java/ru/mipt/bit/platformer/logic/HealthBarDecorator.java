package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.model.Tank;

public class HealthBarDecorator implements Renderable {
    private final Renderable wrappee; // Оборачиваемый объект (танк)
    private final Tank tank;
    private final GraphicsManager graphicsManager;

    // 1x1 белая текстура для рисования прямоугольников
    private static Texture whitePixel;

    public HealthBarDecorator(Renderable wrappee, Tank tank, GraphicsManager graphicsManager) {
        this.wrappee = wrappee;
        this.tank = tank;
        this.graphicsManager = graphicsManager;

        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    @Override
    public void update(float deltaTime) {
        wrappee.update(deltaTime);
    }

    @Override
    public void render(Batch batch) {
        // 1. Рисуем сам танк
        wrappee.render(batch);

        // 2. Если полоски включены - рисуем поверх
        if (graphicsManager.isHealthBarsEnabled()) {
            float hpRatio = Math.max(0, tank.getHealth() / 100f); // 0.0 to 1.0

            // Размеры и позиция (рисуем над танком)
            float width = 100f;
            float height = 10f;
            // Центрируем над танком (тайл 128x128, танк рисуется с 0,0)
            float x = tank.getCoordinates().getX() * 128 + 14;
            float y = tank.getCoordinates().getY() * 128 + 110;

            // Фон (красный)
            batch.setColor(Color.RED);
            batch.draw(whitePixel, x, y, width, height);

            // Заполнение (зеленый)
            batch.setColor(Color.GREEN);
            batch.draw(whitePixel, x, y, width * hpRatio, height);

            // Сбрасываем цвет обратно в белый
            batch.setColor(Color.WHITE);
        }
    }
}