package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.input.Controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Кадр игры: применить действия игрока, продвинуть состояние сущностей и отрисовать их.
 * Игровой цикл не знает ни одной конкретной механики и ни одного конкретного типа сущности —
 * всё, что он делает, он делает со списком {@link Renderable}.
 */
public class Game {

    private final TankController tankController;
    private final Controls controls;
    private final List<Renderable> renderables;

    public Game(TankController tankController, Controls controls, Renderable... renderables) {
        this(tankController, controls, Arrays.asList(renderables));
    }

    public Game(TankController tankController, Controls controls, List<Renderable> renderables) {
        this.tankController = tankController;
        this.controls = controls;
        this.renderables = new ArrayList<>(renderables);
    }

    /**
     * Обновить состояние игры за прошедшее время.
     */
    public void update(float deltaTime) {
        tankController.update(controls);
        for (Renderable renderable : renderables) {
            renderable.update(deltaTime);
        }
    }

    /**
     * Отрисовать сущности. Карта уровня рисуется отдельно, поэтому здесь только они.
     */
    public void render(Batch batch) {
        for (Renderable renderable : renderables) {
            renderable.render(batch);
        }
    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
    }
}
