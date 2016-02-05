package com.CRAsteroids.game.STATES;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.CRAsteroids.game.Save;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameOverState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private boolean newHighScore;
	private char[] newName;
	private int currentChar;
	
	private BitmapFont gameOverFont;
	private BitmapFont newNameFont;

	protected GameOverState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
//		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace bold.ttf"));
//		FreeTypeFontParameter genPar = new FreeTypeFontParameter();
//		
//		genPar.size = 32;
//		
//		BitmapFont gameOverFont = gen.generateFont(genPar);
//		BitmapFont font = gen.generateFont(genPar);
//		gen.dispose();
		
		gameOverFont = CRAsteroidsGame.mediumStyle.font;
		newNameFont = CRAsteroidsGame.mediumStyle.font;
		
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.UP)){
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT)){
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)){
		}
	}

	@Override
	public void dispose() {
		sb.dispose();
		sr.dispose();
//		gameOverFont.dispose();
//		newNameFont.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
