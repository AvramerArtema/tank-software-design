package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.EntityGraphics;
import ru.mipt.bit.platformer.graphics.GraphicsFactory;
import ru.mipt.bit.platformer.input.Controls;
import ru.mipt.bit.platformer.input.KeyboardControls;
import ru.mipt.bit.platformer.logic.Game;
import ru.mipt.bit.platformer.logic.Renderable;
import ru.mipt.bit.platformer.logic.TankController;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TreeObstacle;
import ru.mipt.bit.platformer.tank.TankActions;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float TANK_SPEED = 0.4f;
    private static final Coordinates TANK_START = new Coordinates(1, 1);
    private static final Coordinates TREE_OBSTACLE_POSITION = new Coordinates(1, 3);

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private GraphicsFactory graphicsFactory;

    private Game game;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);

        // собрать графику сущностей
        graphicsFactory = new GraphicsFactory(groundLayer, Interpolation.smooth);
        EntityGraphics tankGraphics = graphicsFactory.create("images/tank_blue.png");
        EntityGraphics treeObstacleGraphics = graphicsFactory.create("images/greenTree.png");

        // собрать модель уровня
        Tank tank = new Tank(TANK_START, TANK_SPEED);
        TreeObstacle treeObstacle = new TreeObstacle(TREE_OBSTACLE_POSITION);
        MovementRules movementRules = new MovementRules(treeObstacle);

        // подключить механики танка к действиям игрока
        TankController tankController = new TankController(tank, movementRules, new TankActions(tank));
        Controls controls = new KeyboardControls();

        // всё, что игровой цикл будет обновлять и рисовать, — список пар «сущность + графика»
        game = new Game(tankController, controls,
                new Renderable(tank, tankGraphics),
                new Renderable(treeObstacle, treeObstacleGraphics));
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();
        game.update(deltaTime);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        game.render(batch);

        // submit all drawing requests
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        graphicsFactory.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
