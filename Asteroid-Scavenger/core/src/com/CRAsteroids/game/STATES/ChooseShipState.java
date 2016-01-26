package com.CRAsteroids.game.STATES;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ChooseShipState extends GameState{
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private Viewport chooseShipViewport;
	
	private Stage stage;
	
	private ButtonStyle shipChoice;
	private Button fighterShip;
	private Button freighterShip;
	
	private float currentScreenWidth;
	private float currentScreenHeight;

	protected ChooseShipState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		currentScreenWidth = Gdx.graphics.getWidth();
		currentScreenHeight = Gdx.graphics.getHeight();
		
		chooseShipViewport = new FitViewport(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
		stage = new Stage(chooseShipViewport);
		Gdx.input.setInputProcessor(stage);
		
		//create buttons
		shipChoice = new ButtonStyle();
		
		fighterShip = new Button(shipChoice);
		freighterShip = new Button(shipChoice);
		
		//add actors
		Table buttonTable = new Table();
		
		buttonTable.setFillParent(true);
		
		stage.addActor(buttonTable);
	}

	@Override
	public void update(float dt) {
		
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void handleInput() {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

}
