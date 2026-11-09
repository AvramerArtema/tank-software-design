package ru.mipt.bit.platformer.model;
import java.util.Random;

public class Tank extends Entity {
    private static final Direction DEFAULT_DIRECTION = Direction.RIGHT;
    private static final float DEFAULT_SPEED = 0.4f;

    private int health; // Здоровье
    private float shootCooldown = 0f;

    public Tank(Coordinates coordinates) {
        this(coordinates, DEFAULT_SPEED);
    }

    public Tank(Coordinates coordinates, float movementSpeed) {
        super(coordinates, DEFAULT_DIRECTION, movementSpeed);
        this.health = 80 + new Random().nextInt(21);
    }

    public void move(Direction direction) {
        startMovement(direction);
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public void tick(float deltaTime) {
        if (shootCooldown > 0) {
            shootCooldown -= deltaTime;
        }
    }

    public boolean canShoot() {
        return shootCooldown <= 0;
    }

    public void resetShootCooldown() {
        this.shootCooldown = 0.5f; // 0.5 секунды между выстрелами
    }
}