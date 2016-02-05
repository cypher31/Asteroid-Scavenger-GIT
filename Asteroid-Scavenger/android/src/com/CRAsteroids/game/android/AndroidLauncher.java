package com.CRAsteroids.game.android;

import android.os.Bundle;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

        initialize(new CRAsteroidsGame(), cfg);
    }
}