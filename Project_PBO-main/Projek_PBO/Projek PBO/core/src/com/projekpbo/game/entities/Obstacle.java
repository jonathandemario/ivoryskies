package com.projekpbo.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends Rectangle {
    protected int obstacleSpeed;
    public Color color;


    public Obstacle(int obstacleSpeed) {
        super();
        this.obstacleSpeed = obstacleSpeed;
        this.color = new Color(MathUtils.random(0,255) / 255f, MathUtils.random(0,255) / 255f, MathUtils.random(0,255) / 255f, 1);
    }

    public Obstacle() {
        this(200);
    }

    public void moveObstacle(float delta) {
        this.x -= delta * obstacleSpeed;
    }
}
