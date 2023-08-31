package com.projekpbo.game.entities;

import com.badlogic.gdx.utils.TimeUtils;

public class BulletSpeedPickup extends Obstacle implements PickUp {
    long effectDuration = 10000;
    long startTime;
    double prevSpeed;
    Player player;

    @Override
    public void pickedUp(Player player) {
        startTime = TimeUtils.millis();
        prevSpeed = player.projectileSpeed;
        this.player = player;
    }

    @Override
    public void doEffect() {
        player.projectileSpeed = 600;
    }

    @Override
    public void reverseEffect() {
        player.projectileSpeed = prevSpeed;
    }

    @Override
    public boolean durationExpired() {
        if (TimeUtils.millis() - startTime > effectDuration) {
            reverseEffect();
            return true;
        }
        return false;
    }

}
