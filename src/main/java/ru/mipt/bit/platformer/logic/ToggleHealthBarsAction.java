package ru.mipt.bit.platformer.logic;

/**
 * Команда для переключения отображения здоровья.
 * Она не привязана к конкретному танку, поэтому работает глобально.
 */
public class ToggleHealthBarsAction extends PlayerAction {
    private final GraphicsManager graphicsManager;

    public ToggleHealthBarsAction(GraphicsManager graphicsManager) {
        this.graphicsManager = graphicsManager;
    }

    @Override
    public boolean canBeHandledBy(ActionHandler handler) {
        return false; // Это не действие танка, а глобальное действие
    }

    @Override
    public void execute(ActionHandler handler) {
        // Ничего не делаем, логика выполняется в TankController
    }

    public void toggle() {
        graphicsManager.toggleHealthBars();
    }
}