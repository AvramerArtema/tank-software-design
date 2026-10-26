package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.Direction;

public interface AIStrategy {
    Direction chooseDirection();
}