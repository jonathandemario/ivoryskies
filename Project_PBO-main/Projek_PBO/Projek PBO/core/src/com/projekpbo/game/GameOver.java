package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

public class GameOver implements Screen {

    MainGame game;
    OrthographicCamera camera;
    long score;
    private Texture background;
    private Texture restart;
    private Texture restartHover;
    private Texture exit;
    private Texture exitHover;
    private Sound gameOverSound;

    private String backgroundPath = "gameOver back.png";
    private String restartPath = "restartButton1.png";
    private String restartHoverPath = "restartButton2.png";
    private String exitPath = "exitButton1.png";
    private String exitHoverPath = "exitButton2.png";
    private String gameOverSoundPath = "GOsound.mp3";

    GameOver(final MainGame game, long score) {
        this.game = game;
        this.score = score;
        background = new Texture(backgroundPath);
        restart = new Texture(restartPath);
        restartHover = new Texture(restartHoverPath);
        exit = new Texture(exitPath);
        exitHover = new Texture(exitHoverPath);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);
    }

    @Override
    public void show() {
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal(gameOverSoundPath));
        gameOverSound.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, windowWidth, windowHeight);
        game.impactFont.setColor(new Color(0xffffffff));

        long finalScore = score;
        int n = 0;
        while (finalScore > 1) {
            finalScore /= 10;
            n++;
        }
        if((int)(score / Math.pow(10, n)) == 1) {
            n++;
        }
        game.impactFont.draw(game.batch, "YOUR SCORE: " + score, (windowWidth / 2) - (85 + (n * 75f / 10)), windowHeight / 2 + 100);

        game.batch.draw(restart, (windowWidth / 2) - (restart.getWidth() / 2), (windowHeight / 2) - (restart.getHeight() / 2) - 70);
        game.batch.draw(exit, (windowWidth / 2) - (exit.getWidth() / 2), (windowHeight / 2) - (exit.getHeight() / 2) - 200);
        game.batch.end();

        if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 454 && Gdx.input.getY() <= 483) {
            game.batch.begin();
            game.batch.draw(restartHover, (windowWidth / 2) - (restart.getWidth() / 2), (windowHeight / 2) - (restart.getHeight() / 2) - 70);
            game.batch.end();
        }
        if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 562 && Gdx.input.getY() <= 633) {
            game.batch.begin();
            game.batch.draw(exitHover, (windowWidth / 2) - (exit.getWidth() / 2), (windowHeight / 2) - (exit.getHeight() / 2) - 200);
            game.batch.end();
        }

        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 454 && Gdx.input.getY() <= 483) {
                gameOverSound.stop();
                game.setScreen(new GameScreen(this.game));
            }
            if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 562 && Gdx.input.getY() <= 633) {
                gameOverSound.stop();
                game.setScreen(new MainMenu(this.game));
            }
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
        gameOverSound.dispose();
    }
}
