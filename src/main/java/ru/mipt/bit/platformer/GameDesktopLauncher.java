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
import ru.mipt.bit.platformer.logic.EntityRenderable;
import ru.mipt.bit.platformer.logic.Game;
import ru.mipt.bit.platformer.logic.Renderable;
import ru.mipt.bit.platformer.logic.TankController;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.MovementRules;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TreeObstacle;
import ru.mipt.bit.platformer.tank.TankActions;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import ru.mipt.bit.platformer.logic.FileLevelProvider;
import ru.mipt.bit.platformer.logic.RandomLevelProvider;
import ru.mipt.bit.platformer.logic.LevelProvider;
import ru.mipt.bit.platformer.model.Level;

public class GameDesktopLauncher implements ApplicationListener {

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

        graphicsFactory = new GraphicsFactory(groundLayer, Interpolation.smooth);

        // ВЫБИРАЕМ СТРАТЕГИЮ (Способ: из файла)
         LevelProvider levelProvider = new FileLevelProvider("level.txt", 8);

        // ВАРИАНТ: рандом, комментим строку выше
        // LevelProvider levelProvider = new RandomLevelProvider(10, 8, 5);

        // Получаем данные уровня от провайдера
        Level levelData = levelProvider.provide();

        // 1. Создаем танк из полученных данных
        Tank tank = new Tank(levelData.getTankStart(), LevelConfig.TANK_SPEED);
        EntityGraphics tankGraphics = graphicsFactory.create(LevelConfig.TANK_TEXTURE);

        List<Renderable> renderables = new ArrayList<>();
        renderables.add(new EntityRenderable(tank, tankGraphics));

        // 2. Создаем деревья из полученных данных
        List<Obstacle> obstacles = new ArrayList<>();
        for (Coordinates treePos : levelData.getTrees()) {
            TreeObstacle tree = new TreeObstacle(treePos);
            obstacles.add(tree);

            EntityGraphics treeGraphics = graphicsFactory.create(LevelConfig.TREE_TEXTURE);
            renderables.add(new EntityRenderable(tree, treeGraphics));
        }

        // 3. Передаем список препятствий
        MovementRules movementRules = new MovementRules(obstacles);

        // 4. Подключаем механики
        TankController tankController = new TankController(tank, movementRules, new TankActions(tank));
        Controls controls = new KeyboardControls();

        // 5. Создаем игру
        game = new Game(tankController, controls, renderables);
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