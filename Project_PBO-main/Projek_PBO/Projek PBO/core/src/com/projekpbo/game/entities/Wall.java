package com.projekpbo.game.entities;

public class Wall extends Obstacle {
    public static int wallWidth = 128;
    public int wallHeight;
    private int health = 10;

    public Wall(int wallWidth, int wallHeight) {
        super();
        this.width = wallWidth;
        this.height = wallHeight;
        this.wallHeight = wallHeight;
    }

    public boolean takeDamage() {
        health--;
        return health <= 0;
    }
}
