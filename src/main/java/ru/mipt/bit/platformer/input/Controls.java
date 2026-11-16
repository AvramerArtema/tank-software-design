package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.logic.PlayerAction;

import java.util.List;

/**
 * Источник действий игрока. Игра спрашивает, что игрок запросил сейчас, и не знает,
 * чем эти действия созданы: клавиатурой, джойстиком или тестом.
 */
public interface Controls {

    /**
     * Действия, запрошенные игроком в текущий момент. Список пуст, если игрок ничего не нажимал.
     */
    List<PlayerAction> activeActions();
}
