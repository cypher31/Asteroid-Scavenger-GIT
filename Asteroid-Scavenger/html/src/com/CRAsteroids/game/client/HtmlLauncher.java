package com.CRAsteroids.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.CRAsteroids.game.CRAsteroidsGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1000, 1000);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new CRAsteroidsGame();
        }
}