package ru.mipt.bit.platformer.model;

public class Bullet extends Entity{

    private final int damage = 25;

    public Bullet(Coordinates start,Direction direction){
        super(start, direction, 0.1f);
        startMovement(direction);
    }
    public int getDamage(){
        return damage;
    }
}
