package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.input.Controls;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Controller> controllers; // Список контроллеров (игрок + боты)
    private final Controls controls;
    private final List<Renderable> renderables;

    public Game(List<Controller> controllers, Controls controls, List<Renderable> renderables) {
        this.controllers = controllers;
        this.controls = controls;
        this.renderables = new ArrayList<>(renderables);
    }

    public void update(float deltaTime) {
        // Обновляем всех: и игрока, и ботов
        for (Controller controller : controllers) {
            controller.update(controls);
        }
        for (Renderable renderable : renderables) {
            renderable.update(deltaTime);
        }
    }

    // ВОТ ЭТОТ МЕТОД БЫЛ ПОТЕРЯН
    public void render(Batch batch) {
        for (Renderable renderable : renderables) {
            renderable.render(batch);
        }
    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
    }
}