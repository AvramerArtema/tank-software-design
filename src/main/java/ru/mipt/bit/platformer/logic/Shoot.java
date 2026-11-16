package ru.mipt.bit.platformer.logic;

/**
 * Действие «выстрелить». Параметров у него нет, поэтому достаточно одного экземпляра.
 */
public final class Shoot extends PlayerAction {

    public static final Shoot INSTANCE = new Shoot();

    private Shoot() {
    }

    @Override
    public boolean canBeHandledBy(ActionHandler handler) {
        return handler.handlesShooting();
    }

    @Override
    public void execute(ActionHandler handler) {
        handler.shoot();
    }
}
