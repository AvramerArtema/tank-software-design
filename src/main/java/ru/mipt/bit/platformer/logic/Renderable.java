package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Renderable {
    void update(float deltaTime);
    void render(Batch batch);
}