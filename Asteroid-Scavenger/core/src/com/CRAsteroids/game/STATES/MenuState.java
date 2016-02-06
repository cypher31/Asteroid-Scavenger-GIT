package com.CRAsteroids.game.STATES;

import java.util.ArrayList;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.CRAsteroids.game.Objects.Asteroid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuState extends GameState{
	
	private String TAG = System.class.getName();
	
	//variables
	private SpriteBatch sb;
	private ShapeRenderer sr;
	public static Viewport menuViewport;
	//Stage
	private Stage stage;
	//Font
	//private BitmapFont titleFont;
	private BitmapFont optionStyle;
	private LabelStyle titleStyle;
	//Buttons
	private TextButton playButton;
	private TextButton highScoreButton;
	private TextButton quitButton;
	private TextButtonStyle textButtonStyle;
	
	//Text
	private final String title = "Asteroid Scavenger";
	private final String play = "Play";
	private final String highScore = "Highscore";
	private final String quit = "Quit";
	
	private float currentScreenWidth;
	private float currentScreenHeight;
	
	private ArrayList<Asteroid> asteroids;
	
	protected MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init(){
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		currentScreenWidth = Gdx.graphics.getWidth();
		currentScreenHeight = Gdx.graphics.getHeight();
		
		menuViewport = new StretchViewport(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
		stage = new Stage(menuViewport);
		Gdx.input.setInputProcessor(stage);

		//come back and make if statement depending on screen size
//			titleStyle = CRAsteroidsGame.smallStyle;
//			titleStyle = CRAsteroidsGame.mediumStyle;
			titleStyle = CRAsteroidsGame.largeStyle;
		
		Label titleName = new Label(title, titleStyle);

		//buttons
		//come back and make if statement depending on screen size
//		optionStyle = CRAsteroidsGame.smallStyle.font;
		optionStyle = CRAsteroidsGame.mediumStyle.font;
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = optionStyle;
		
		playButton = new TextButton(play, textButtonStyle);
		highScoreButton = new TextButton(highScore, textButtonStyle);
		quitButton = new TextButton(quit, textButtonStyle);
		
		//add actors
		Table titleTable = new Table();
		Table optionsTable = new Table();
		
		titleTable.setFillParent(true);
		optionsTable.setFillParent(true);
		
		stage.addActor(titleTable);
		stage.addActor(optionsTable);
		
		//title 
		titleTable.align(Align.top).padTop(currentScreenHeight * 0.1f);
		titleTable.add(titleName).top();

		//buttons
		optionsTable.add(playButton).align(Align.center).row();
		optionsTable.add(highScoreButton).align(Align.center).row();
		optionsTable.add(quitButton).align(Align.center).row();
		
		playButton.setTouchable(Touchable.enabled);
		highScoreButton.setTouchable(Touchable.enabled);
		quitButton.setTouchable(Touchable.enabled);
		
		//Debug
//		titleTable.setDebug(true);
//		optionsTable.setDebug(true);
//		playButton.setDebug(true);
//		highScoreButton.setDebug(true);
//		quitButton.setDebug(true);

		//create asteroids
		asteroids = new ArrayList<Asteroid>();
		for(int i = 0; i < 6; i++){
			asteroids.add(new Asteroid(MathUtils.random(CRAsteroidsGame.WIDTH), 
					MathUtils.random(CRAsteroidsGame.HEIGHT), Asteroid.LARGE));
		}
		
		
	}

	@Override
	public void update(float dt) {
		
		handleInput();
		
		for(int i = 0; i < asteroids.size(); i++){
			asteroids.get(i).update(dt);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void draw() {
		
		sb.setProjectionMatrix(CRAsteroidsGame.cam.combined);
		sr.setProjectionMatrix(CRAsteroidsGame.cam.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//draw asteroids
		for(int i = 0; i < asteroids.size(); i++){
			asteroids.get(i).draw(sr);
		}
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void handleInput() {
		
		//play button click
		playButton.addListener(new ChangeListener(){
		@Override
		public void changed(ChangeEvent event, Actor actor){
			gsm.setState(GameStateManager.ChooseShipState);
		}
		});
		
		//play button touch 
		playButton.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			gsm.setState(GameStateManager.ChooseShipState);
			return true;
		}
		
		});
		
		//highscore button click
		highScoreButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				gsm.setState(GameStateManager.HIGHSCORE);
			}
			});
		
		//highscore button touch 
		highScoreButton.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			gsm.setState(GameStateManager.HIGHSCORE);
			return true;
		}
		
		});
		
		//quitbutton click
		quitButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				Gdx.app.exit();
			}
			});
		
		//quit button touch 
		quitButton.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			Gdx.app.exit();
			return true;
		}
		
		});
		
	}
	
	@Override
	public void dispose() {
		sr.dispose();
		sb.dispose();
		stage.dispose();
//		titleFont.dispose();
//		font.dispose();
	}


}
