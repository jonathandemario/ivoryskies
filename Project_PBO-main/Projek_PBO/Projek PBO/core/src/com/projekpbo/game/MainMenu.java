package com.projekpbo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

public class MainMenu implements Screen {

    MainGame game;
    OrthographicCamera camera;
    private Texture background;
    private Texture startHover;
    private Texture startButton;
    private Music menuMusic;

    private String backgroundPath = "background menu.png";
    private String startHoverPath = "playButton2.png";
    private String startButtonPath = "playButton1.png";
    private String menuMusicPath = "MMmusicNew.mp3";

    MainMenu(final MainGame game) {
        this.game = game;
        background = new Texture(backgroundPath);
        startHover = new Texture(startHoverPath);
        startButton = new Texture(startButtonPath);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);

    }

    @Override
    public void show() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal(menuMusicPath));
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, windowWidth, windowHeight);
        game.batch.draw(startButton, (windowWidth / 2) - (startButton.getWidth() / 2), (windowHeight / 2) - (startButton.getHeight() / 2) - 100);
        game.batch.end();

        if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 457 && Gdx.input.getY() <= 532) {
            game.batch.begin();
            game.batch.draw(startHover, (windowWidth / 2) - (startButton.getWidth() / 2), (windowHeight / 2) - (startButton.getHeight() / 2) - 100);
            game.batch.end();
        }

        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() >= 286 && Gdx.input.getX() <= 509 && Gdx.input.getY() >= 457 && Gdx.input.getY() <= 532) {
                menuMusic.stop();
                game.setScreen(new GameScreen(this.game));
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
        menuMusic.dispose();
    }
}
