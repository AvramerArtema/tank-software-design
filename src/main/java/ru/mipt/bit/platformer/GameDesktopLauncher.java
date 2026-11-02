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
import ru.mipt.bit.platformer.graphics.GraphicsFactory;
import ru.mipt.bit.platformer.input.Controls;
import ru.mipt.bit.platformer.input.KeyboardControls;
import ru.mipt.bit.platformer.logic.*;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Level;
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

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private GraphicsFactory graphicsFactory;
    private Game game;

    @Override
    public void create() {
        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        graphicsFactory = new GraphicsFactory(groundLayer, Interpolation.smooth);

        // Провайдер уровня
        LevelProvider levelProvider = new RandomLevelProvider(10, 8, 5);
        Level levelData = levelProvider.provide();

        // 1. Создаем Логический мир
        GameWorld world = new GameWorld();

        // 2. Создаем танк игрока (СНАЧАЛА создаем его, чтобы передать в GraphicsManager)
        Tank playerTank = new Tank(levelData.getTankStart(), LevelConfig.TANK_SPEED);

        // 3. Создаем Наблюдателя (Графику) и передаем ему танк игрока
        GraphicsManager graphicsManager = new GraphicsManager(graphicsFactory, playerTank);

        // Связываем их
        world.addObserver(graphicsManager);

        List<Tank> allTanks = new ArrayList<>();
        List<Controller> controllers = new ArrayList<>();
        List<Obstacle> obstacles = new ArrayList<>();

        // 4. Создаем деревья
        for (Coordinates treePos : levelData.getTrees()) {
            TreeObstacle tree = new TreeObstacle(treePos);
            obstacles.add(tree);
            world.addEntity(tree);
        }

        // 5. Создаем правила движения
        MovementRules movementRules = new MovementRules(10, 8, obstacles);

        // 6. Добавляем танк игрока в мир
        allTanks.add(playerTank);
        world.addEntity(playerTank);

        // Передаем world в TankActions, чтобы танк мог стрелять пулями
        TankController playerController = new TankController(
                playerTank, movementRules, new TankActions(playerTank, world)
        );
        controllers.add(playerController);

        // 7. Создаем танки ботов
        for (Coordinates botPos : levelData.getBotStarts()) {
            Tank botTank = new Tank(botPos, LevelConfig.TANK_SPEED);
            allTanks.add(botTank);
            world.addEntity(botTank);

            AIController botController = new AIController(
                    botTank, movementRules, new TankActions(botTank, world), new RandomAIStrategy()
            );
            controllers.add(botController);
        }

        // 8. Отдаем список всех танков в правила
        movementRules.setTanks(allTanks);

        // 9. Создаем игру
        Controls controls = new KeyboardControls();
        game = new Game(controllers, controls, world, graphicsManager);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        // Обновляем логику и графику
        game.update(deltaTime);

        levelRenderer.render();

        batch.begin();
        game.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}

    @Override
    public void dispose() {
        graphicsFactory.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}