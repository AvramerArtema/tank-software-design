package ru.mipt.bit.platformer.model;

public class MediumTankState implements TankState {
    @Override
    public float getSpeedMultiplier() { return 2f; } // В 2 раза медленнее

    @Override
    public boolean canShoot() { return true; }

    @Override
    public float getShootCooldown() { return 0.5f; }
}