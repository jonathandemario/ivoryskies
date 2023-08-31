package com.projekpbo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {
	SpriteBatch batch;
	BitmapFont font;
	BitmapFont impactFont;

	public static int windowHeight = 800;
	public static int windowWidth = 800;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		impactFont = new BitmapFont(Gdx.files.internal("impactFont.fnt"));
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
