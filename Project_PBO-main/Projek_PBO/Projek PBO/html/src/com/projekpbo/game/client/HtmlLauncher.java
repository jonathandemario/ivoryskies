package com.projekpbo.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.projekpbo.game.MainGame;

import static com.projekpbo.game.MainGame.windowHeight;
import static com.projekpbo.game.MainGame.windowWidth;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
//                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                return new GwtApplicationConfiguration(windowWidth, windowHeight);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MainGame();
        }
}