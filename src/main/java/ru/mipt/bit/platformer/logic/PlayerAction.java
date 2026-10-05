package ru.mipt.bit.platformer.logic;

/**
 * Действие, которое игрок может запросить у игры. Действие само умеет себя выполнить,
 * поэтому игровому циклу не нужно разбирать действия по типам.
 *
 * Чтобы добавить новую механику, нужно три вещи:
 * 1. класс действия в этом пакете (если понадобится — с его параметрами);
 * 2. кнопку для него в {@code KeyboardControls};
 * 3. метод-обработчик в {@link ActionHandler} и его реализацию в механике того объекта,
 *    к которому действие относится (например, в {@code TankActions}).
 *
 * Игровой цикл и {@link TankController} при этом не меняются.
 */
public abstract class PlayerAction {

    /**
     * Поддерживает ли этот обработчик такое действие.
     */
    public abstract boolean canBeHandledBy(ActionHandler handler);

    /**
     * Выполнить действие с помощью обработчика, который его поддерживает.
     */
    public abstract void execute(ActionHandler handler);
}
