package com.CRAsteroids.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HighScoreState extends GameState{
	
	private SpriteBatch sb;
	
	PlayState playState;
	
	private BitmapFont font;
	
	private long[] highScores;
	private String[] names;

	protected HighScoreState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
		sb = new SpriteBatch();
		font = CRAsteroidsGame.fontLarge;
		
		Save.load();
		highScores = Save.gd.getHighScores();
		names = Save.gd.getNames();
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void draw() {
		
		sb.setProjectionMatrix(CRAsteroidsGame.cam.combined);
		
		sb.begin();
		
		String s;
		float w;
		
		s = "High Scores";
		w = font.getBounds(s).width;
		font.draw(sb, s, (CRAsteroidsGame.WIDTH - w) / 2, 950);
		
		for(int i = 0; i < highScores.length; i++){
			s = String.format(
						"%2d. %7s %s",
						i + 1,
						highScores[i],
						names[i]
					);
			w = font.getBounds(s).width;
			font.draw(sb, s, (CRAsteroidsGame.WIDTH - w) / 2, 800 - (font.getBounds(s).height + 20) * i);
		}
		
		sb.end();
	}

	@Override
	public void dispose() {
		sb.dispose();
//		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			gsm.setState(GameStateManager.MENU);
		}
		
	}

}
