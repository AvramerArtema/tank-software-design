package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * PORT: Абстракция для отрисовки уровня. Application-слой зависит от этого интерфейса.
 */
public interface LevelRenderer {
    Iterable<Renderable> getRenderables();
    void render(Batch batch);
}