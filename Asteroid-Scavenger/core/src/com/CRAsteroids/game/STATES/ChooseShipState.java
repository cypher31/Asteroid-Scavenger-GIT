package com.CRAsteroids.game.STATES;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.CRAsteroids.game.Objects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ChooseShipState extends GameState{
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private Viewport chooseShipViewport;
	
	private Stage stage;
	
	private TextButtonStyle shipChoice;
	private TextButton fighterButton;
	private TextButton freighterButton;
	
	private float currentScreenWidth;
	private float currentScreenHeight;
	
	public static boolean fighterShip;
	public static boolean freighterShip;
	
	public Player fighterHud;
	public Player freighterHud;
	
	protected ChooseShipState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		fighterHud = new Player(null);
		freighterHud = new Player(null);
		
		currentScreenWidth = Gdx.graphics.getWidth();
		currentScreenHeight = Gdx.graphics.getHeight();
		
		chooseShipViewport = new FitViewport(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
		stage = new Stage(chooseShipViewport);
		Gdx.input.setInputProcessor(stage);
		
		BitmapFont optionStyle = CRAsteroidsGame.mediumStyle.font;
		
		//create buttons
		shipChoice = new TextButtonStyle();
		shipChoice.font = optionStyle;
		
		fighterButton = new TextButton(" ",shipChoice);
		freighterButton = new TextButton(" ",shipChoice);
		
		//add actors
		Table buttonTable = new Table();
		
		buttonTable.setFillParent(true);
		
		stage.addActor(buttonTable);
		
		buttonTable.add(fighterButton).align(Align.center).padRight(100);
		fighterButton.getLabel().setFontScale(4, 4);
		
		buttonTable.add(freighterButton).align(Align.center).padLeft(100);
		freighterButton.getLabel().setFontScale(4, 4);
		
		buttonTable.setDebug(true);
		fighterButton.setDebug(true);
	}

	@Override
	public void update(float dt) {
		handleInput();
		
	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(CRAsteroidsGame.cam.combined);
		sr.setProjectionMatrix(CRAsteroidsGame.cam.combined);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

//		fighterHud.setPosition(Gdx.graphics.getWidth() / 2 - 100 - fighterButton.getWidth() / 2, Gdx.graphics.getHeight() / 2);
//		fighterHud.draw(sr);
//	
//		freighterHud.setPosition(Gdx.graphics.getWidth() / 2 + 100 + fighterButton.getWidth() / 2, Gdx.graphics.getHeight() / 2);
//		freighterHud.draw(sr);
		
	}

	@Override
	public void handleInput() {
		//play button click
		fighterButton.addListener(new ChangeListener(){
		@Override
		public void changed(ChangeEvent event, Actor actor){
			System.out.println("fighter chosen");
			gsm.setState(GameStateManager.PLAY);
			fighterShip = true;
			freighterShip = false;
		}
		});
		
		//play button touch 
		fighterButton.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			System.out.println("fighter chosen");
			gsm.setState(GameStateManager.PLAY);
			fighterShip = true;
			freighterShip = false;
			return true;
		}
		
		});
		
		//highscore button click
		freighterButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				System.out.println("freighter chosen");
				gsm.setState(GameStateManager.PLAY);
				freighterShip = true;
				fighterShip = false;
			}
			});
		
		//highscore button touch 
		freighterButton.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			System.out.println("freighter chosen");
			gsm.setState(GameStateManager.PLAY);
			freighterShip = true;
			fighterShip = false;
			return true;
		}
		});
	}

	@Override
	public void dispose() {
		sr.dispose();
		sb.dispose();
		stage.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}
}
