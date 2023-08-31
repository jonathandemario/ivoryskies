package com.projekpbo.game.entities;

import com.badlogic.gdx.utils.TimeUtils;

public class BulletRatePickup extends Obstacle implements PickUp {
    long effectDuration = 10000;
    long startTime;
    double prevFreq;
    Player player;

    @Override
    public void pickedUp(Player player) {
        this.player = player;
        this.startTime = TimeUtils.millis();
        prevFreq = player.projectileFreq;
    }

    @Override
    public void doEffect() {
        player.projectileFreq = 10;
    }

    @Override
    public void reverseEffect() {
        player.projectileFreq = prevFreq;

    }

    @Override
    public boolean durationExpired() {
        if (TimeUtils.millis() - startTime > effectDuration) {
            reverseEffect();
            System.out.println("Duration Expired!");
            return true;
        }
        return false;
    }
}
