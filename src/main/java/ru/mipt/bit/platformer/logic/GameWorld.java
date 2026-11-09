package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.model.Entity;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final List<Entity> entities = new ArrayList<>();
    private final List<LevelObserver> observers = new ArrayList<>();

    public void addObserver(LevelObserver observer) {
        this.observers.add(observer);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        for (LevelObserver observer : observers) {
            observer.onEntityAdded(entity);
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        for (LevelObserver observer : observers) {
            observer.onEntityRemoved(entity);
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}

