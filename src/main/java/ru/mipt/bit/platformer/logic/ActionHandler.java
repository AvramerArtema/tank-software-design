package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.Direction;

/**
 * Умеет выполнять действия игрока над одним объектом. Каждая механика реализует этот интерфейс
 * у себя: обработка действий танка живёт рядом с танком, а не в игровом цикле.
 *
 * Каждый метод отвечает на вопрос «поддерживаю ли я такое действие» и выполняет его.
 */
public interface ActionHandler {

    boolean handlesMovement();

    void move(Direction direction);

    boolean handlesShooting();

    void shoot();
}
