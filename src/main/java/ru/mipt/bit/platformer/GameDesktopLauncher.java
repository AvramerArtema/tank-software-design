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
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TreeObstacle;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final Coordinates TANK_START = new Coordinates(1, 1);
    private static final Coordinates TREE_OBSTACLE_POSITION = new Coordinates(1, 3);

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private GraphicsFactory graphicsFactory;

    private Tank tank;
    private EntityGraphics tankGraphics;

    private TreeObstacle treeObstacle;
    private EntityGraphics treeObstacleGraphics;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        graphicsFactory = new GraphicsFactory(groundLayer, Interpolation.smooth);

        tank = new Tank(TANK_START);
        tankGraphics = graphicsFactory.create("images/tank_blue.png");

        treeObstacle = new TreeObstacle(TREE_OBSTACLE_POSITION);
        treeObstacleGraphics = graphicsFactory.create("images/greenTree.png");
        treeObstacleGraphics.updatePosition(treeObstacle);
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        // продвинуть уже начатый переезд танка
        tank.updateMovement(deltaTime, MOVEMENT_SPEED);

        // единственное место, где нажатия кнопок превращаются в намерения танка;
        // вектор направления, поворот спрайта и запрет движения во время переезда
        // уже описаны в модели
        if (isPressed(UP, W) && canMove(Direction.UP)) {
            tank.startMovement(Direction.UP);
        }
        if (isPressed(DOWN, S) && canMove(Direction.DOWN)) {
            tank.startMovement(Direction.DOWN);
        }
        if (isPressed(LEFT, A) && canMove(Direction.LEFT)) {
            tank.startMovement(Direction.LEFT);
        }
        if (isPressed(RIGHT, D) && canMove(Direction.RIGHT)) {
            tank.startMovement(Direction.RIGHT);
        }

        // рассчитать экранные координаты танка с учётом прогресса переезда
        tankGraphics.updatePosition(tank);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        // render player
        tankGraphics.render(batch, tank.getDirection().getRotation());

        // render tree obstacle
        treeObstacleGraphics.render(batch, treeObstacle.getDirection().getRotation());

        // submit all drawing requests
        batch.end();
    }

    private static boolean isPressed(int firstKey, int secondKey) {
        return Gdx.input.isKeyPressed(firstKey) || Gdx.input.isKeyPressed(secondKey);
    }

    private boolean canMove(Direction direction) {
        return !tank.isMoving() && !direction.shift(tank.getCoordinates()).equals(treeObstacle.getCoordinates());
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
