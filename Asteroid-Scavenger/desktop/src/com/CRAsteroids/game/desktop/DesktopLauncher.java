package com.CRAsteroids.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.CRAsteroids.game.CRAsteroidsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Asteroid Scavenger v0.1.0";
		cfg.height = 900;
		cfg.width = 1600;
		cfg.resizable = false;
		new LwjglApplication(new CRAsteroidsGame(), cfg);
	}
}
