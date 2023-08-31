package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.*;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

import com.projekpbo.game.entities.*;

public class GameScreen implements Screen {
    //Fundamental vars
    MainGame game;
    private OrthographicCamera camera;
    public long score = 0;
    private Texture background;
    private Music gameScreenMusic;


    //Player vars
    private Player player;
    private Texture playerSpriteSh;
    private Animation playerDefault;


    //Projectile vars
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private int projectileWidth = 16;
    private int projectileHeight = 16;
    private Texture projectileSprite;


    //Obstacle / Pickup vars
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private long lastObstacleSpawn;
    private double obsFrequency = 1.5;
    private int obstacleWidth = 64;
    private int obstacleHeight = 64;

    private Animation bulletSpeedPickupSprite;
    private Animation bulletRatePickupSprite;
    private Animation ballonSprite;
    private Animation obstacleAnim;
    private Texture obstacleSpriteSheet;


    //Wall vars
    int wallFreq = 1; //frequency of wall spawning attempts
    int wallChance = 5; //int ranging from 0 to 100. describes the chance (%) of spawning a wall
    long lastWallSpawn = 0;
    private Texture wallSprite;


    //Sounds
    private Sound speedPickupSound;
    private String speedPickupSoundPath = "pickupSound1.mp3";
    public Sound ratePickupSound;
    private String ratePickupSoundPath = "pickupSound2.mp3";
    private Sound birdHitSound;
    private String birdHitSoundPath = "birdHitSound.mp3";
    private Sound balloonHitSound;
    private String balloonHitSoundPath = "balloonHitSound.mp3";
    private Sound breakWallSound;
    private String breakWallSoundPath = "wallBreakSound.mp3";

    //Path strings
    private String playerSpriteShPath = "playerNew.png";
    private String obstacleSpriteSheetPath = "enemy-bird-Sheet.png";
    private String projectileSpritePath = "heartAttack.png";
    private String backgroundPath = "background.png";
    private String wallSpritePath = "tembok.png";
    private String ballonSpritePath = "whiteBallon-animated.png";
    private String bulletSpeedPickupSpritePath = "BulletSpeedPickup.png";
    private String bulletRatePickupSpritePath = "BulletRatePickup.png";
    private String gameScreenMusicPath = "GSmusic.mp3";

    GameScreen(final MainGame game) {
        this.game = game;
        obstacleSpriteSheet = new Texture(obstacleSpriteSheetPath);
        obstacleAnim = new Animation(obstacleSpriteSheet, 72, 72);
        background = new Texture(backgroundPath);
        playerSpriteSh = new Texture(playerSpriteShPath);
        playerDefault = new Animation(playerSpriteSh, 72, 72);
        projectileSprite = new Texture(projectileSpritePath);
        wallSprite = new Texture(wallSpritePath);
        ballonSprite = new Animation(new Texture(ballonSpritePath),100,100);
        bulletRatePickupSprite = new Animation(new Texture(bulletRatePickupSpritePath),64,64);
        bulletSpeedPickupSprite = new Animation(new Texture(bulletSpeedPickupSpritePath),64,64);

        speedPickupSound = Gdx.audio.newSound(Gdx.files.internal(speedPickupSoundPath));
        ratePickupSound = Gdx.audio.newSound(Gdx.files.internal(ratePickupSoundPath));
        breakWallSound = Gdx.audio.newSound(Gdx.files.internal(breakWallSoundPath));
        birdHitSound = Gdx.audio.newSound(Gdx.files.internal(birdHitSoundPath));
        balloonHitSound = Gdx.audio.newSound(Gdx.files.internal(balloonHitSoundPath));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);

        player = new Player();
    }


    @Override
    public void show() {
        gameScreenMusic = Gdx.audio.newMusic(Gdx.files.internal(gameScreenMusicPath));
        gameScreenMusic.setLooping(true);
        gameScreenMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f,1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(background, 0, 0, windowWidth, windowHeight);
        game.batch.draw(playerDefault.animate(), player.x, player.y);

        //Draw obstacles / pickups
        for(Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext();) {
            Obstacle obstacle = iter.next();

            if(obstacle instanceof BirdObstacle) {
                game.batch.draw(obstacleAnim.animate(), obstacle.x, obstacle.y);
            }
            else if(obstacle instanceof BulletSpeedPickup) {
                game.batch.draw(bulletSpeedPickupSprite.animate(), obstacle.x, obstacle.y, obstacleWidth, obstacleHeight);
            }
            else if(obstacle instanceof BulletRatePickup) {
                game.batch.draw(bulletRatePickupSprite.animate(), obstacle.x, obstacle.y, obstacleWidth, obstacleHeight);
            } else if(obstacle instanceof Wall) {
                game.batch.draw(wallSprite, obstacle.x, obstacle.y, Wall.wallWidth, ((Wall)obstacle).wallHeight);
            }
            else { //Default obstacle image
                game.batch.setColor(obstacle.color);
                game.batch.draw(ballonSprite.animate(), obstacle.x, obstacle.y - 32);
                game.batch.setColor(Color.WHITE);
            }
        }

        //Draw projectiles
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();) {
            Projectile projectile = iter.next();
            game.batch.draw(projectileSprite, projectile.x, projectile.y, projectile.width, projectile.height);
        }

        //Draw score text
        game.impactFont.setColor(Color.GRAY);
        game.impactFont.draw(game.batch, "SCORE: " + score, 10, windowHeight-10);
        game.impactFont.setColor(Color.WHITE);

        game.batch.end();

        //Move all projectiles
        moveProjectile(delta);

        //Move the player (contains input handling)
        player.movePlayer(delta, camera, projectiles);

        //Generate obstacles and walls
        generateObstacle(obsFrequency);
        generateWall(wallFreq, wallChance);

        //Collision Detection
        for(Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
            Obstacle obstacle = iter.next();

            obstacle.moveObstacle(delta);
            if(obstacle.x + obstacle.width < 0) {
                iter.remove();
            }

            if(obstacle.overlaps(player)) {
                if(obstacle instanceof PickUp) {
                    for(int i = 0; i < player.pickUps.size(); i++) {
                        //If the player already has the pickup
                        if(obstacle.getClass() == player.pickUps.get(i).getClass()) {
                            player.pickUps.get(i).reverseEffect();
                            player.pickUps.remove(i);
                            i--;
                        }
                    }

                    //Add the pickup to the player's pickup list
                    player.pickUps.add((PickUp) obstacle);

                    //Initialize Pickup with current time as well as the Player object it affects
                    ((PickUp) obstacle).pickedUp(player);

                    //Play sounds depending on the type of pickup
                     if (obstacle instanceof BulletSpeedPickup) {
                         speedPickupSound.play();
                     } else if (obstacle instanceof BulletRatePickup) {
                         ratePickupSound.play();
                     }

                    iter.remove(); //Remove pickup from the list of obstacles on screen (pickup will not be checked for collisions or drawn next frame)
                } else { //Stop all sounds and go to the GameOver screen
                    gameScreenMusic.stop();
                    speedPickupSound.stop();
                    ratePickupSound.stop();
                    breakWallSound.stop();
                    birdHitSound.stop();
                    balloonHitSound.stop();
                    game.setScreen(new GameOver(this.game, score));
                }
            }
        }
        score += 1; //Base score increment per frame
    }


    void moveProjectile(float delta) { //Moves projectiles and detects collision between projectiles and obstacles and walls
        for(int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).moveProjectile(delta);
            boolean outOfBounds = (
                    projectiles.get(i).x + projectileWidth < 0 ||
                    projectiles.get(i).x > windowWidth ||
                    projectiles.get(i).y + projectileHeight < 0 ||
                    projectiles.get(i).y > windowHeight
            );

            if(outOfBounds) {
                projectiles.remove(i);
                i--;
                continue;
            }

            for(int j = 0; j < obstacles.size(); j++) { //Check for collisions between projectile and obstacles
                if(projectiles.get(i).overlaps(obstacles.get(j)) && !(obstacles.get(j) instanceof PickUp)) {
                    score += 100;
                    if(obstacles.get(j) instanceof BirdObstacle) {
                        score += 50;
                    }

                    if(obstacles.get(j) instanceof Wall) {
                        if(((Wall)(obstacles.get(j))).takeDamage()) {
                            obstacles.remove(j);
                            breakWallSound.play();
                            score += 400;
                        }
                    } else {
                        if (obstacles.get(j) instanceof BirdObstacle) {
                            birdHitSound.play();
                        } else {
                            balloonHitSound.play();
                        }
                        obstacles.remove(j);
                    }
                    projectiles.remove(i);
                    i--;
                    break;
                }
            }
        }
    }
    void generateWall(double freq, int chance) {
        if(TimeUtils.millis() - lastWallSpawn > 1000/freq) {
            int height = (int)MathUtils.random(Player.tmp.height * 3, windowHeight/2);
            if(MathUtils.random(0, 100) <= chance) {
                Wall wall = new Wall(Wall.wallWidth, height);
                wall.x = windowWidth;
                wall.y = MathUtils.random(0,2) == 0 ? 0 : windowHeight - height;
                obstacles.add(wall);
            }
            lastWallSpawn = TimeUtils.millis();
        }
    }

    void generateObstacle(double freq) {
        if(TimeUtils.millis() - lastObstacleSpawn > 1000/freq) {
            int amount = MathUtils.random(0, 3); //amount of obstacles to generate
            int obsGap = MathUtils.random(5, 20); //random gap between slots
            ArrayList<Integer> slots = new ArrayList<>();

            for(int i = 0; i <  windowHeight / (obstacleHeight + obsGap); i++) {
                slots.add(i);
            }

            for (int i = 0; i < amount; i++) {
                if(slots.size() == 0) continue;
                Obstacle obstacle = new Obstacle();

                if(MathUtils.random(0,10) > 8) {
                    obstacle = new BirdObstacle();
                } else {
                    if(MathUtils.random(0,100) >= 90) {
                        if(MathUtils.random(0, 100) >= 49) {
                            obstacle = new BulletRatePickup();
                        } else {
                            obstacle = new BulletSpeedPickup();
                        }
                    }
                }
                obstacle.width = obstacleWidth;
                obstacle.height = obstacleHeight;

                obstacle.x = windowWidth;
                int chosenSlot = MathUtils.random(0, slots.size()-1);
                obstacle.y = slots.get(chosenSlot) * (obstacleHeight + obsGap);

                slots.remove(chosenSlot);

                obstacles.add(obstacle);
            }

            lastObstacleSpawn = TimeUtils.millis();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        speedPickupSound.dispose();
        ratePickupSound.dispose();
        birdHitSound.dispose();
        balloonHitSound.dispose();
        breakWallSound.dispose();
        gameScreenMusic.dispose();
    }
}
