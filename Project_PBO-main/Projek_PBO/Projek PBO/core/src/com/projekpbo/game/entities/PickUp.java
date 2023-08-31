package com.projekpbo.game.entities;

public interface PickUp {
    void pickedUp(Player player);
    void doEffect();
    void reverseEffect();
    boolean durationExpired();
}