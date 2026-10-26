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
import ru.mipt.bit.platformer.logic.Controller;
import ru.mipt.bit.platformer.logic.AIController;
import ru.mipt.bit.platformer.logic.RandomAIStrategy;
import ru.mipt.bit.platformer.logic.Controller;

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
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        graphicsFactory = new GraphicsFactory(groundLayer, Interpolation.smooth);

        // Провайдер уровня
        LevelProvider levelProvider = new RandomLevelProvider(10, 8, 5);
        Level levelData = levelProvider.provide();

        // Списки для игры
        List<Tank> allTanks = new ArrayList<>();
        List<Renderable> renderables = new ArrayList<>();
        List<Controller> controllers = new ArrayList<>();
        List<Obstacle> obstacles = new ArrayList<>();

        // 1. Создаем деревья
        for (Coordinates treePos : levelData.getTrees()) {
            TreeObstacle tree = new TreeObstacle(treePos);
            obstacles.add(tree);
            EntityGraphics treeG = graphicsFactory.create(LevelConfig.TREE_TEXTURE);
            renderables.add(new EntityRenderable(tree, treeG));
        }

        // 2. Создаем правила движения
        MovementRules movementRules = new MovementRules(10, 8, obstacles);

        // 3. Создаем танк игрока
        Tank playerTank = new Tank(levelData.getTankStart(), LevelConfig.TANK_SPEED);
        allTanks.add(playerTank);
        EntityGraphics playerG = graphicsFactory.create(LevelConfig.TANK_TEXTURE);
        renderables.add(new EntityRenderable(playerTank, playerG));

        TankController playerController = new TankController(playerTank, movementRules, new TankActions(playerTank));
        controllers.add(playerController);

        // 4. Создаем танки ботов
        for (Coordinates botPos : levelData.getBotStarts()) {
            Tank botTank = new Tank(botPos, LevelConfig.TANK_SPEED);
            allTanks.add(botTank);

            // (Предполагается, что у тебя есть текстура для врага, если нет — используй ту же)
            EntityGraphics botG = graphicsFactory.create(LevelConfig.TANK_TEXTURE);
            renderables.add(new EntityRenderable(botTank, botG));

            // Создаем AI контроллер
            AIController botController = new AIController(
                    botTank, movementRules, new TankActions(botTank), new RandomAIStrategy()
            );
            controllers.add(botController);
        }

        // 5. Отдаем список всех танков в правила для коллизий
        movementRules.setTanks(allTanks);

        // 6. Создаем игру с МАССИВОМ контроллеров
        Controls controls = new KeyboardControls();
        game = new Game(controllers, controls, renderables);
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