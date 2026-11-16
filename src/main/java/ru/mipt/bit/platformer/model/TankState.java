package ru.mipt.bit.platformer.model;
/**
 * Состояние танка определяет его характеристики (скорость, возможность стрелять).
 */
public interface TankState {
    // Возвращает множитель скорости (1f - норма, 2f - в 2 раза медленнее)
    float getSpeedMultiplier();

    // Может ли танк стрелять в этом состоянии
    boolean canShoot();

    // Время перезарядки в этом состоянии
    float getShootCooldown();
}