package com.projekpbo.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import static com.projekpbo.game.MainGame.windowHeight;
import static com.projekpbo.game.MainGame.windowWidth;

public class Player extends Rectangle {
    private double acceleration = 1000; //acceleration downwards
    private double velocity = 0; //initial velocity
    private double addedVelocity = 42; //velocity added per frame of user input
    private int horizontalSpeed = 200;
    private static int playerWidth = 64;
    private static int playerHeight = 64;
    public double projectileFreq = 1;
    public double projectileSpeed = 300;
    private long lastProjectileTime;
    public ArrayList<PickUp> pickUps;
    private Sound shootSound;

    private String shootSoundPath = "shootSound.mp3";

    public Player() {
        super();
        this.width = playerWidth;
        this.height = playerHeight;
        this.x = (int)(windowWidth*0.3) - (playerWidth/2);
        this.y = windowHeight/2 - playerHeight/2;
        pickUps = new ArrayList<>();
        shootSound = Gdx.audio.newSound(Gdx.files.internal(shootSoundPath));
    }

    void launchProjectile(Vector2 direction, ArrayList<Projectile> projectileList) {
        Projectile bullet = new Projectile(this.x + 64, this.y + 20, direction, projectileSpeed);
        projectileList.add(bullet);
        shootSound.play();
    }

    public void movePlayer(float delta, OrthographicCamera camera, ArrayList<Projectile> projectileList) {
        for(int i = 0; i < pickUps.size(); i++) {
            if(pickUps.get(i).durationExpired()) {
                pickUps.remove(i);
                i--;
            } else {
                pickUps.get(i).doEffect();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            velocity -= addedVelocity;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.x -= horizontalSpeed * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.x += horizontalSpeed * delta;
        }
        if(Gdx.input.isTouched() && TimeUtils.millis() - lastProjectileTime > 1000/projectileFreq) { //Fire projectiles towards the mouse
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            launchProjectile(new Vector2(touchPos.x - (this.x + 64), touchPos.y - (this.y + 20)), projectileList);
            lastProjectileTime = TimeUtils.millis();
        }

        //Physics for vertical movement
        velocity += acceleration * delta;
        this.y -= velocity * delta;

        //Movement Bounds
        if(this.y < 0) {
            this.y = 0;
            velocity = 0;
        }
        if(this.y > windowHeight-playerHeight) {
            this.y = windowHeight-playerHeight;
            velocity = 0;
        }

        if(this.x > windowWidth - playerWidth) {
            this.x = windowWidth - playerWidth;
        }
        if(this.x < 0) {
            this.x = 0;
        }
    }
}
