package ru.mipt.bit.platformer.model;

import java.util.Random;

public class Tank extends Entity {
    private static final Direction DEFAULT_DIRECTION = Direction.RIGHT;
    private static final float DEFAULT_SPEED = 0.4f;

    private int health;
    private float shootCooldown = 0f;

    // НОВОЕ: Поле для хранения текущего состояния
    private TankState state;

    public Tank(Coordinates coordinates) {
        this(coordinates, DEFAULT_SPEED);
    }

    public Tank(Coordinates coordinates, float movementSpeed) {
        super(coordinates, DEFAULT_DIRECTION, movementSpeed);
        this.health = 80 + new Random().nextInt(21);
        updateState(); // <--- Инициализируем состояние при создании
    }

    public void move(Direction direction) {
        startMovement(direction);
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        updateState(); // <--- НОВОЕ: При получении урона проверяем, не изменилось ли состояние
    }

    // НОВЫЙ ПРИВАТНЫЙ МЕТОД: Пересчитывает состояние на основе здоровья
    private void updateState() {
        float healthRatio = health / 100f;
        if (healthRatio > 0.7f) {
            state = new LightTankState();
        } else if (healthRatio >= 0.15f) {
            state = new MediumTankState();
        } else {
            state = new HeavyTankState();
        }
    }

    // ПЕРЕОПРЕДЕЛЯЕМ СКОРОСТЬ: Теперь она зависит от состояния (делим на множитель)
    @Override
    public float getMovementSpeed() {
        // Так как в Entity формула deltaTime / speed, то умножение скорости
        // делает танк медленнее (что нам и нужно для Medium и Heavy)
        return super.getMovementSpeed() * state.getSpeedMultiplier();
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

    // ОБНОВЛЕНО: Проверяем и кулдаун, и возможность стрелять из состояния
    public boolean canShoot() {
        return state.canShoot() && shootCooldown <= 0;
    }

    // ОБНОВЛЕНО: Берем время перезарядки из состояния
    public void resetShootCooldown() {
        this.shootCooldown = state.getShootCooldown();
    }
}