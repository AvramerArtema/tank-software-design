package ru.mipt.bit.platformer.model;

public class HeavyTankState implements TankState {
    @Override
    public float getSpeedMultiplier() { return 3f; } // В 3 раза медленнее

    @Override
    public boolean canShoot() { return false; } // Не может стрелять

    @Override
    public float getShootCooldown() { return 999f; } // Заглушка
}