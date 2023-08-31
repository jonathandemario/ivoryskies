package com.projekpbo.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import static com.projekpbo.game.MainGame.windowWidth;
import static com.projekpbo.game.MainGame.windowHeight;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Ivory Skies");
		config.setWindowedMode(windowWidth, windowHeight);
		config.setResizable(false);
		config.setWindowIcon("dumbo72px1.png");
		new Lwjgl3Application(new MainGame(), config);
	}
}
