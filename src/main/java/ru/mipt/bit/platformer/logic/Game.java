package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.input.Controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    private final Controller controller; // <-- Теперь интерфейс (DIP)
    private final Controls controls;
    private final List<Renderable> renderables; // <-- Теперь интерфейс

    public Game(Controller controller, Controls controls, Renderable... renderables) {
        this(controller, controls, Arrays.asList(renderables));
    }

    public Game(Controller controller, Controls controls, List<Renderable> renderables) {
        this.controller = controller;
        this.controls = controls;
        this.renderables = new ArrayList<>(renderables);
    }

    public void update(float deltaTime) {
        controller.update(controls);
        for (Renderable renderable : renderables) {
            renderable.update(deltaTime);
        }
    }

    public void render(Batch batch) {
        for (Renderable renderable : renderables) {
            renderable.render(batch);
        }
    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
    }
}