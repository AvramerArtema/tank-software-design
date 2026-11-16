package ru.mipt.bit.platformer.model;

public class LightTankState implements TankState {
    @Override
    public float getSpeedMultiplier() { return 1f; } // Нормальная скорость

    @Override
    public boolean canShoot() { return true; }

    @Override
    public float getShootCooldown() { return 0.5f; }
}