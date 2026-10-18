package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.input.Controls;

public interface Controller {
    void update(Controls controls);
}