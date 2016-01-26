package com.CRAsteroids.game;

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
		
		newHighScore = Save.gd.isHighScore(Save.gd.getTentativeScore());
		if(newHighScore){
			newName = new char[]{
					'A', 'A', 'A'
			};
			currentChar = 0;
		}
		
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
		
		sb.setProjectionMatrix(CRAsteroidsGame.cam.combined);
		
		sb.setColor(1, 1, 1, 1);
		sb.begin();
		
		String s;
		float w;
		
		s = "Game Over";
		w = gameOverFont.getBounds(s).width;
		gameOverFont.draw(sb, s, 
				(Gdx.graphics.getWidth() / 2 - w / 2)
				, Gdx.graphics.getHeight() * .95f);
		
		if(!newHighScore){
			sb.end();
			return;
		}
		
		s = "New High Score:" + Save.gd.getTentativeScore();
		w = newNameFont.getBounds(s).width;
		newNameFont.draw(
				sb, 
				s, 
				Gdx.graphics.getWidth() / 2 - w / 2,
				Gdx.graphics.getHeight() * .50f);
		
		for (int i = 0; i < newName.length; i++){
			s = Character.toString(newName[i]);
			newNameFont.draw(
				sb,  
				s, 
				Gdx.graphics.getWidth() / 2 - 15 * 2 + 15 * i,
				Gdx.graphics.getHeight() * .30f);
		}
		
//		(Gdx.graphics.getWidth() / 2 - (player.playerWidth + player.playerWidth / 2)) 
//		+ (player.playerWidth + player.playerWidth / 2) * i, 
//		Gdx.graphics.getHeight() * .90f);
//hudPlayer.draw(sr);
		
		sb.end();
		
		sr.begin(ShapeType.Line);
		sr.line(Gdx.graphics.getWidth() / 2 - 15 * 2 + 15 * currentChar, 
				Gdx.graphics.getHeight() * .30f - 18, 
				Gdx.graphics.getWidth() / 2 + 10 - 15 * 2 + 15 * currentChar, 
				Gdx.graphics.getHeight() * .30f - 18);
		sr.end();
	}

	@Override
	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
			if(newHighScore){
				Save.gd.addHighScore(Save.gd.getTentativeScore(), new String(newName));
				Save.save();
			}
			gsm.setState(GameStateManager.MENU);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.UP)){
			if(newName[currentChar] == ' '){
				newName[currentChar] = 'Z';
			}
			else {
				newName[currentChar]--;
				if(newName[currentChar] < 'A'){
					newName[currentChar] = ' ';
				}
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
			if(newName[currentChar] == ' '){
				newName[currentChar] = 'A';
			}
			else {
				newName[currentChar]++;
				if(newName[currentChar] > 'Z'){
					newName[currentChar] = ' ';
				}
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT)){
			if(currentChar < newName.length - 1){
				currentChar++;
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)){
			if(currentChar > 0){
				currentChar--;
			}
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
