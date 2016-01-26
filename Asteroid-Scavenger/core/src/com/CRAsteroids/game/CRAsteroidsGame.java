package com.CRAsteroids.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class CRAsteroidsGame implements ApplicationListener {
	
	//width and height of screen
	public static int WIDTH;
	public static int HEIGHT;
	
	Player player;
	
	//Camera perpendicular to game plane
	public static OrthographicCamera cam;
	public static StretchViewport viewport;
	
	public static int smallFontSize;
	public static int mediumFontSize;
	public static int largeFontSize;
	
	public static BitmapFont fontSmall;
	public static BitmapFont fontMedium;
	public static BitmapFont fontLarge;
	
	public static LabelStyle smallStyle;
	public static LabelStyle mediumStyle;
	public static LabelStyle largeStyle;
	
	private GameStateManager gsm;
	private PlayState playState;

	@Override
	public void create() {
		
		//width of screen
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		//create camera
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		//set camera position
		cam.position.set(WIDTH / 2, HEIGHT / 2, 0);
		//updates camera
		cam.update();
		
		//sets input processor to this
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		fontSize();
		
		//Generate font
		SmartFontGenerator fontGen = new SmartFontGenerator();
		FileHandle exoFile = Gdx.files.internal("fonts/Hyperspace Bold.ttf");
		fontSmall = fontGen.createFont(exoFile, "exo-small", 12);
		fontMedium = fontGen.createFont(exoFile, "exo-medium", 20);
		fontLarge = fontGen.createFont(exoFile, "exo-large", 45);

		smallStyle = new Label.LabelStyle();
		smallStyle.font = fontSmall;
		mediumStyle = new Label.LabelStyle();
		mediumStyle.font = fontMedium;
		largeStyle = new Label.LabelStyle();
		largeStyle.font = fontLarge;
		
//		Jukebox.load("sounds/explode.ogg", "explode");
//		Jukebox.load("sounds/extralife.ogg", "extralife");
//		Jukebox.load("sounds/largesaucer.ogg", "largesaucer");
//		Jukebox.load("sounds/pulsehigh.ogg", "pulsehigh");
//		Jukebox.load("sounds/pulselow.ogg", "pulselow");
//		Jukebox.load("sounds/saucershoot.ogg", "saucershoot");
//		Jukebox.load("sounds/shoot.ogg", "shoot");
//		Jukebox.load("sounds/smallsaucer.ogg", "smallsaucer");
//		Jukebox.load("sounds/thruster.ogg", "thruster");
		
		gsm = new GameStateManager();
	}

	@Override
	public void resize(int width, int height) {
		gsm.resize(width, height);
	}

	@Override
	public void render() {
		//clear screen to black
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//width of screen
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
				
		//create camera
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.position.set(WIDTH / 2, HEIGHT / 2, 0);
		
		//updates camera
		cam.update();
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		
		GameKeys.update();
	}
	
	public void fontSize(){
		smallFontSize = (int) (Gdx.graphics.getWidth() * .01f);
		mediumFontSize = (int) (Gdx.graphics.getWidth() * .025f);
		largeFontSize = (int) (Gdx.graphics.getWidth() * .030f);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
