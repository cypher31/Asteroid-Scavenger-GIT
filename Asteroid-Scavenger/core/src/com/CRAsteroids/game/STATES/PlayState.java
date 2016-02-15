package com.CRAsteroids.game.STATES;

import java.util.ArrayList;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.CRAsteroids.game.GameKeys;
import com.CRAsteroids.game.Objects.Asteroid;
import com.CRAsteroids.game.Objects.Bullet;
import com.CRAsteroids.game.Objects.Credits;
import com.CRAsteroids.game.Objects.FlyingSaucer;
import com.CRAsteroids.game.Objects.Particle;
import com.CRAsteroids.game.Objects.Player;
import com.CRAsteroids.game.Objects.Stars;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PlayState<Radar> extends GameState implements InputProcessor{
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private long startTime;
	private long shootTime;
	public long hitTime;
	
	public static boolean isFlashing;
	
	private OrthographicCamera cam;
	private Vector3 screenCoords;
	public static String Quad;
	public static int quadInterval = 5000;
	
	//Hud
	private FitViewport playViewPort;
	public static Stage playerHud;
	private LabelStyle scoreStyle;
	private LabelStyle buttonStyle;
	private Label hudMineXP;
	private Label hudFightXP;
	private Label creditsHud;
	private Label location;
	private Label hudHealth;
	private Label hudShield;
	private String playerMineXP;
	private String playerFightXP;
	private String playerCredits;
	private String playerLocationX;
	private String playerLocationY;
	private String playerHealth;
	private String playerShield;
	private Table scoreTable;
	private Table healthTable;
	private Table locationTable;
	private Table buttonTableLeft;
	private Table buttonTableRight;
	private TextButtonStyle tbs;
	private TextButton turnLeftAndroid;
	private TextButton turnRightAndroid;
	private TextButton shootAndroid;
	private TextButton thrustOnAndroid;
	private TextButton thrustOffAndroid;
	
	private BitmapFont font;
	
	public Player player;
	private Actor radar;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Bullet> enemyBullets;
	private ArrayList<Credits> credits;
	private ArrayList<Stars> stars;
	
	private FlyingSaucer flyingSaucer;
	private float fsTimer;
	private float fsTime;
	
	private ArrayList<Particle> particles;
	
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	private Vector2 lastTouch = new Vector2();
	
	private float maxDelay;
	private float minDelay;
	private float currentDelay;
	private float bgTimer;
	private boolean playLowPulse;
	private boolean isDown;

	protected PlayState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
		shootTime = TimeUtils.nanoTime();
		hitTime = TimeUtils.nanoTime();
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		cam = new OrthographicCamera(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
		cam.position.set(CRAsteroidsGame.WIDTH / 2, CRAsteroidsGame.HEIGHT / 2, 0);
		cam.update();
		
		font = CRAsteroidsGame.fontSmall;
		
		bullets = new ArrayList<Bullet>();
		
		player = new Player(bullets);
		
		
		asteroids = new ArrayList<Asteroid>();
		
		particles = new ArrayList<Particle>();
		
		credits = new ArrayList<Credits>();
		
		stars = new ArrayList<Stars>();
		
		createStars();
		
		spawnAsteroids();
		
		fsTimer = 0;
		fsTime = 15;
		enemyBullets = new ArrayList<Bullet>();
		
		if(Gdx.app.getType() == ApplicationType.Android){
			Gdx.input.setInputProcessor(playerHud);
		}else{
			Gdx.input.setInputProcessor(this);
		}
		
		//Set up bg music
		maxDelay = 1;
		minDelay = .25f;
		currentDelay = maxDelay;
		bgTimer = maxDelay;
		playLowPulse = true;
		
		//*HUD stage
		playViewPort = new FitViewport(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
		playerHud = new Stage(playViewPort);
		
		
		class Radar extends Actor {

		    @Override
		    public void draw (Batch batch, float parentAlpha) {
				batch.end();
		    	
				ShapeRenderer shapeRenderer = new ShapeRenderer();
				
				shapeRenderer.setProjectionMatrix(playerHud.getCamera().combined);
				
				shapeRenderer.begin(ShapeType.Filled);
				
				int radius = 100;
				
				shapeRenderer.circle(playViewPort.getWorldWidth() - radius * 1.5f, playViewPort.getWorldHeight() - radius * 1.5f, radius);
				
				shapeRenderer.end();
				
				batch.begin();
		    }
		}
		
		scoreStyle = CRAsteroidsGame.hudStyle;
		buttonStyle = CRAsteroidsGame.mediumStyle;
		
		hudMineXP = new Label("Mining XP: 0", scoreStyle);
		hudFightXP = new Label("Fighting XP: 0", scoreStyle);
		creditsHud = new Label("Credits: " + player.getPlayerCredit(), scoreStyle);
		location = new Label("Location: " + "Sector(" + Quad + ")" + player.getx +  " , " + player.gety, scoreStyle);
		hudHealth = new Label("Armor: " + player.getPlayerHealth(), scoreStyle);
		hudShield = new Label("Shields: " + player.getPlayerShield(), scoreStyle);
		
		//Buttons
		tbs = new TextButtonStyle();
		tbs.font = buttonStyle.font;
		
		//add actors
		scoreTable = new Table();
		healthTable = new Table();
		locationTable = new Table();
		//android
		buttonTableLeft = new Table();
		buttonTableRight = new Table();
		turnLeftAndroid = new TextButton("LEFT ", tbs);
		turnRightAndroid = new TextButton("RIGHT", tbs);
		shootAndroid = new TextButton("Shoot", tbs);
		thrustOnAndroid = new TextButton("Thrust On", tbs);
		thrustOffAndroid = new TextButton("Thrust Off", tbs);
		
		scoreTable.setFillParent(true);
		healthTable.setFillParent(true);
		locationTable.setFillParent(true);
		buttonTableLeft.setFillParent(true);
		buttonTableRight.setFillParent(true);
		
		radar = new Radar();
		
		playerHud.addActor(scoreTable);
		playerHud.addActor(healthTable);
		playerHud.addActor(locationTable);
		playerHud.addActor(buttonTableLeft);
		playerHud.addActor(buttonTableRight);
		playerHud.addActor(radar);
		
		scoreTable.align(Align.top);
		scoreTable.add(hudMineXP).padTop(Gdx.graphics.getHeight() * .025f).row();
		scoreTable.add(hudFightXP).row();
		scoreTable.add(creditsHud).row();
		
		healthTable.align(Align.topLeft).defaults().width(150).padTop(Gdx.graphics.getHeight() * .025f);
		healthTable.add(hudShield).getActor().setAlignment(Align.right);
		healthTable.row();
		healthTable.add(hudHealth).getActor().setAlignment(Align.right);
		
		locationTable.align(Align.bottomRight);
		locationTable.add(location).padRight(Gdx.graphics.getWidth() * .025f);
		
		if(Gdx.app.getType() == ApplicationType.Android){
			buttonTableLeft.align(Align.bottomLeft).padBottom(Gdx.graphics.getHeight() * 0.1f);
			buttonTableLeft.add(turnLeftAndroid).padLeft(Gdx.graphics.getWidth() * .05f);
			buttonTableLeft.defaults().height(100);
			buttonTableLeft.add(turnRightAndroid).padLeft(Gdx.graphics.getWidth() * .025f);
			buttonTableLeft.row().align(Align.center).colspan(2).padLeft(Gdx.graphics.getWidth() * .05f).padTop(Gdx.graphics.getHeight() * .05f);
			buttonTableLeft.add(thrustOnAndroid);
			buttonTableLeft.row().align(Align.center).colspan(2).padLeft(Gdx.graphics.getWidth() * .05f).padTop(Gdx.graphics.getHeight() * .05f);
			buttonTableLeft.add(thrustOffAndroid);
			
			buttonTableRight.align(Align.bottomRight).padBottom(Gdx.graphics.getHeight() * 0.3f);
			buttonTableRight.defaults().height(100);
			buttonTableRight.add(shootAndroid).padRight(Gdx.graphics.getWidth() * .1f);
			
			buttonTableLeft.setDebug(true);
			buttonTableRight.setDebug(true);
			turnLeftAndroid.setDebug(true);
			turnRightAndroid.setDebug(true);
		}
		
		
//		playerHud.setDebugAll(true);
//		scoreTable.setDebug(true);
//		livesTable.setDebug(true);
//		locationTable.setDebug(true);
		
		
	}
	

	private void createParticles(float x, float y){
		for(int i = 0; i < 6; i++){
			particles.add(new Particle(x, y));
		}
	}
	
	private void splitAsteroid(Asteroid a){
		createParticles(a.getx(), a.gety());
		numAsteroidsLeft--;
		currentDelay = ((maxDelay - minDelay) * numAsteroidsLeft / totalAsteroids)
				+ minDelay;
		if(a.getType() == Asteroid.LARGE){
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
		}
		if(a.getType() == Asteroid.MEDIUM){
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
		}
	}
	
	private void spawnAsteroids(){
		asteroids.clear();
		
		int numToSpawn = 1000;
		totalAsteroids = numToSpawn * 7;
		numAsteroidsLeft = totalAsteroids;
		currentDelay = maxDelay;
		
		for(int i = 0; i < numToSpawn; i++){
			float x = MathUtils.random(0, 10000);
			float y = MathUtils.random(0, 20000);
			
//			float dx = x - player.getx();
//			float dy = y - player.gety();
//			float dist = (float) Math.sqrt(dx * dx + dy * dy);
//			
//			while (dist < 100){
//				x = MathUtils.random(CRAsteroidsGame.WIDTH);
//				y = MathUtils.random(CRAsteroidsGame.HEIGHT);
//				dx = x - player.getx();
//				dy = y - player.gety();
//				dist = (float) Math.sqrt(dx * dx + dy * dy);
//			}
			
			asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
		}
	}
	
	@Override
	public void update(float dt) {
		
		if(Gdx.app.getType() == ApplicationType.Android)
			handleInput();
			
		
		if(isDown == true && TimeUtils.timeSinceNanos(shootTime) > 150000000 && !player.isHit()){
			player.shoot();
			shootTime = TimeUtils.nanoTime();
		}
		
		Quadrants();
		
		if(Gdx.app.getType() == ApplicationType.Android){
			cam = new OrthographicCamera(CRAsteroidsGame.WIDTH / 2, CRAsteroidsGame.HEIGHT / 2);
			cam.position.set(player.getx(), player.gety(), 0);
			cam.update();
		}else{
			cam = new OrthographicCamera(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
			cam.position.set(player.getx(), player.gety(), 0);
			cam.update();
		}
		
		//update radar
		radar.setPosition(player.getx(), player.gety());
		
		//update credits
		for(int i = 0; i < credits.size(); i++)
		credits.get(i).update(dt);
		
		//update stars
		for(int i = 0; i < stars.size(); i++)
		stars.get(i).update(dt);
		
		//update player
		player.update(dt);
		if(player.isDead()){
			if(player.getLives() == 0) {
//				Jukebox.stopAll();
				gsm.setState(GameStateManager.GAMEOVER);
				return;
			}
			player.reset();
			player.loseLife();
			flyingSaucer = null;
//			Jukebox.stop("smallsaucer");
//			Jukebox.stop("largesaucer");
			return;
		}
		
		//update player bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update(dt);
			if(bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}
		
		//update flying saucer
		if(flyingSaucer == null){
			fsTimer += dt;
			if(fsTimer > fsTime){
				fsTimer = 0;
				int type = MathUtils.random() < 0.5 ? FlyingSaucer.SMALL : FlyingSaucer.LARGE;
				int direction = MathUtils.random() < 0.5 ? FlyingSaucer.RIGHT: FlyingSaucer.LEFT;
				flyingSaucer = new FlyingSaucer(
						type,
						direction,
						player,
						enemyBullets
						);
			}
		}
		//if already a suacer
		else{
			flyingSaucer.update(dt);
			if(flyingSaucer.shouldRemove()){
				flyingSaucer = null;
//				Jukebox.stop("smallsaucer");
//				Jukebox.stop("largesaucer");
			}
		}
		
		//update fs bullets
		for(int i = 0; i < enemyBullets.size(); i++){
			enemyBullets.get(i).update(dt);
			if(enemyBullets.get(i).shouldRemove()){
				enemyBullets.remove(i);
				i++;
			}
		}
		
		//update asteroids
		for(int i = 0; i < asteroids.size(); i++){
			asteroids.get(i).update(dt);
			if(asteroids.get(i).shouldRemove()){
				asteroids.remove(i);
				i--;
			}
		}
		
		//update particles
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).update(dt);
			if(particles.get(i).shouldRemove()){
				particles.remove(i);
				i--;
			}
		}
		
		//check collisions
		checkCollisions();
		
		//hyperDrive
		hyperDrive();
		
		if(Gdx.app.getType() == ApplicationType.Android)
			Gdx.input.setInputProcessor(playerHud);
		
//		//play bg music
//		bgTimer += dt;
//		if(!player.isHit() && bgTimer >= currentDelay){
//			if(playLowPulse){
//				Jukebox.play("pulselow");
//			}
//			else{
//				Jukebox.play("pulsehigh");
//			}
//			playLowPulse = !playLowPulse;
//			bgTimer = 0;
//		}
	}
	
	private void checkCollisions(){
		//player-asteroid collision
		if(!player.isHit()){
			for(int i = 0; i < asteroids.size(); i++){
				Asteroid a = asteroids.get(i);
				if(a.intersects(player)){
					player.hit();
					int crashDamage = 15;
					if(player.playerShield > 0 && !player.isHit() && TimeUtils.timeSinceNanos(hitTime) > 2000000000){
						player.playerShield -= crashDamage * 2;
						hitTime = System.nanoTime();
					} else if(player.playerShield == 0 && !player.isHit() && TimeUtils.timeSinceNanos(hitTime) > 2000000000){
						player.playerHealth -= crashDamage;
						hitTime = System.nanoTime();
					}
					asteroids.remove(i);
					i--;
					splitAsteroid(a);
//					Jukebox.play("explode");
					break;
				}
			}
		}
		
		//bullet-asteroid collision
		for(int i = 0; i < bullets.size(); i++){
			Bullet b = bullets.get(i);
			for(int j = 0; j < asteroids.size(); j++){
				Asteroid a = asteroids.get(j);
				if(a.contains(b.getx(), b.gety())){
					bullets.remove(i);
					i--;
					asteroids.remove(j);
					j--;
					splitAsteroid(a);
					player.incrementMineXP(a.getxp());
	//				Jukebox.play("explode");
					if(a.getType() == 2){
						credits.add(new Credits(a.getx(), a.gety()));
					}
					if(a.getType() == 1){
						credits.add(new Credits(a.getx(), a.gety()));
						credits.add(new Credits(a.getx(), a.gety()));
					}
					if(a.getType() == 0){
						credits.add(new Credits(a.getx(), a.gety()));
						credits.add(new Credits(a.getx(), a.gety()));
						credits.add(new Credits(a.getx(), a.gety()));
					}
					break;
				}
			}
		}
		
		//player-credit collision
		if(!player.isHit()){
			for(int i = 0; i < credits.size(); i ++){
				Credits c = credits.get(i);
				if(c.intersects(player)){
					credits.remove(i);
					i--;
					player.incrementCredits(c.creditWorth());
				}
				
			}
		}
		
		//player-flying saucer collision
		if(flyingSaucer != null){
			if(player.intersects(flyingSaucer)){
				player.hit();
				int crashDamage = 15;
				if(player.playerShield > 0 && !player.isHit() && TimeUtils.timeSinceNanos(hitTime) > 200000000){
					player.playerShield -= crashDamage * 2;
					hitTime = System.nanoTime();
				} else if (player.playerShield == 0 && !player.isHit() && TimeUtils.timeSinceNanos(hitTime) > 200000000){
					player.playerHealth -= crashDamage;
					hitTime = System.nanoTime();
				}
				createParticles(player.getx(), player.gety());
				createParticles(flyingSaucer.getx(), flyingSaucer.gety());
				flyingSaucer = null;
//				Jukebox.stop("smallsaucer");
//				Jukebox.stop("largesaucer");
//				Jukebox.play("explode");
			}
		}
		
		//bullet-flying saucer collision
		if(flyingSaucer != null){
			for(int i = 0; i < bullets.size(); i++){
				Bullet b = bullets.get(i);
				if(flyingSaucer.contains(b.getx(), b.gety())){
					bullets.remove(i);
					i--;
					createParticles(flyingSaucer.getx(), flyingSaucer.gety());
					player.incrementFightXP(flyingSaucer.getxp());
					flyingSaucer = null;
//					Jukebox.stop("smallsaucer");
//					Jukebox.stop("largesaucer");
//					Jukebox.play("explode")
					break;
				}
			}
		}
		
		//player-enemy bullets collision
		if(!player.isHit()){
			for(int i = 0; i < enemyBullets.size(); i++){
				Bullet b = enemyBullets.get(i);
				if(player.contains(b.getx(), b.gety())){
					player.hit();
					enemyBullets.remove(i);
					i--;
					int bulletDamage = 5;
					if(player.playerShield > 0 && !player.isHit() && TimeUtils.timeSinceNanos(hitTime) > 200000000){
						player.playerShield -= bulletDamage * 2;
						hitTime = System.nanoTime();
					} else if(player.playerShield == 0 && !player.isHit() && TimeUtils.timeSinceNanos(hitTime) > 200000000){
						player.playerHealth -= bulletDamage;
						hitTime = System.nanoTime();
					}
//					Jukebox.play("explode");
					break;
				}
			}
		}
		
		//flying saucer-asteroid collision
		if(flyingSaucer != null){
			for(int i = 0; i < asteroids.size(); i++){
				Asteroid a = asteroids.get(i);
				if(a.intersects(flyingSaucer)){
					asteroids.remove(i);
					i--;
					splitAsteroid(a);
					createParticles(a.getx(), a.gety());
					createParticles(flyingSaucer.getx(), flyingSaucer.gety());
					flyingSaucer = null;
//					Jukebox.stop("smallsaucer");
//					Jukebox.stop("largesaucer");
//					Jukebox.play("explode");
					break;
				}
			}
		}
		
		//asteroid enemy bullet collision
		for(int i = 0; i < enemyBullets.size(); i++){
			Bullet b = enemyBullets.get(i);
			for(int j = 0; j < asteroids.size(); j++){
				Asteroid a = asteroids.get(j);
				if(a.contains(b.getx(), b.gety())){
					asteroids.remove(j);
					j--;
					splitAsteroid(a);
					enemyBullets.remove(i);
					i--;
					createParticles(a.getx(), a.gety());
//					Jukebox.play("explode");
					break;
				}
			}
		}
		
	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(cam.combined);
		sr.setProjectionMatrix(cam.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//draw stage
		playerHud.act(Gdx.graphics.getDeltaTime());
		playerHud.draw();
		
		//draw credits
		for(int i = 0; i < credits.size(); i++)
		credits.get(i).draw(sr);
		
		//draw stars
		for(int i = 0; i < stars.size(); i++){
			if(cam.frustum.pointInFrustum(stars.get(i).getx(), stars.get(i).gety(), 0)){
				stars.get(i).draw(sr);
			}
		}
		
		
		//draw player
		player.draw(sr);

		//draw bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(sr);
		}
		
		//draw flying saucer
		if(flyingSaucer != null){
			flyingSaucer.draw(sr);
		}
		
		//draw fs bullets
		for(int i = 0; i < enemyBullets.size(); i++){
			enemyBullets.get(i).draw(sr);
		}
		
		//draw asteroids
		
		for(int i = 0; i < asteroids.size(); i++){
			if(cam.frustum.pointInFrustum(asteroids.get(i).getx(), asteroids.get(i).gety(), 0)){
				asteroids.get(i).draw(sr);
			}
		}
		
		//draw particles
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).draw(sr);
		}
		
		//draw hud
		if(player!= null){
		playerMineXP = Long.toString(player.getPlayerMineXP());
		hudMineXP.setText("Mining XP: " + playerMineXP);
		
		playerFightXP = Long.toString(player.getPlayerFightXP());
		hudFightXP.setText("Fighting XP: " + playerFightXP);
		
		if(player.getPlayerHealth() < 0){
			player.playerHealth = 0;
		}
		playerHealth = Integer.toString(player.getPlayerHealth());
		hudHealth.setText("Armor: " + playerHealth);
		
		if(player.getPlayerShield() < 0){
			player.playerShield = 0;
		}
		playerShield = Integer.toString(player.getPlayerShield());
		
		hudShield.setText("Shield: " + playerShield);
		
		playerCredits = Long.toString(player.getPlayerCredit());
		creditsHud.setText("Credits: " + playerCredits);
		
		playerLocationX = Integer.toString(player.getx);
		playerLocationY = Integer.toString(player.gety);
		location.setText("Location: " + "Sector(" + Quad + ")" + playerLocationX +  " , " + playerLocationY);
		}
	}
	
	public void Quadrants(){
		if(player.getx() < quadInterval && player.gety() < quadInterval){
			Quad = "1";
		}else if(player.getx() > quadInterval * 1 && player.gety() < quadInterval * 1){
			Quad = "2";
		}else if(player.getx() > quadInterval * 1 && player.gety() > quadInterval * 1 && player.gety() < quadInterval * 2){
			Quad = "3";
		}else if(player.getx() < quadInterval * 1 && player.gety() > quadInterval * 1 && player.gety() < quadInterval * 2){
			Quad = "4";
		}else if(player.getx() < quadInterval * 1 && player.gety() > quadInterval * 2 && player.gety() < quadInterval * 3){
			Quad = "5";
		}else if(player.getx() > quadInterval * 1 && player.gety() > quadInterval * 2 && player.gety() < quadInterval * 3){
			Quad = "6";
		}else if(player.getx() > quadInterval * 1 && player.gety() > quadInterval * 3 && player.gety() < quadInterval * 4){
			Quad = "7";
		}else if(player.getx() < quadInterval * 1 && player.gety() > quadInterval * 3 && player.gety() < quadInterval * 4){
			Quad = "8";
		}else{
			Quad = "The Unknown";
		}
	}
	
	private void createStars(){
		int numberOfStars = 10000;
		
		for(int i = 0; i < numberOfStars; i++){
			stars.add(new Stars(MathUtils.random(0, 10000), MathUtils.random(0, 20000)));
		}
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
//		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	public void hyperDrive(){
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && TimeUtils.timeSinceNanos(startTime) > 3000000000L){
			player.hyperDrive = true;
			startTime = TimeUtils.nanoTime();
			isFlashing = false;
		}
	}


	public boolean keyDown(int k) {
		if(k == Keys.UP) {
			if(!player.isHit())
				player.setUp(true);
		}
		if(k == Keys.LEFT) {
			if(!player.isHit())
				player.setLeft(true);
		}
		if(k == Keys.DOWN) {
			GameKeys.setKey(GameKeys.DOWN, true);
		}
		if(k == Keys.RIGHT) {
			if(!player.isHit())
				player.setRight(true);
		}
		if(k == Keys.ENTER) {
			GameKeys.setKey(GameKeys.ENTER, true);
		}
		if(k == Keys.ESCAPE) {
			GameKeys.setKey(GameKeys.ESCAPE, true);
		}
		if(k == Keys.SPACE) {
			if(!player.isHit())
				player.shoot();
		}
		if(k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
			startTime = TimeUtils.nanoTime();
			isFlashing = true;
		}
		return true;
	}
	
	public boolean keyUp(int k) {
		if(k == Keys.UP) {
			player.setUp(false);
		}
		if(k == Keys.LEFT) {
			player.setLeft(false);
		}
		if(k == Keys.DOWN) {
			GameKeys.setKey(GameKeys.DOWN, false);
		}
		if(k == Keys.RIGHT) {
			player.setRight(false);
		}
		if(k == Keys.ENTER) {
			GameKeys.setKey(GameKeys.ENTER, false);
		}
		if(k == Keys.ESCAPE) {
			GameKeys.setKey(GameKeys.ESCAPE, false);
		}
		if(k == Keys.SPACE) {
		}
		if(k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
			player.hyperDrive = false;
			isFlashing = false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//MultiTouch
//		lastTouch.set(screenX, screenY);
//		
//		if(Gdx.input.isTouched(0)){
//			player.setUp(true);
//		}
//		
//		if(Gdx.input.isTouched(1)){
//			player.shoot();
//		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		
//		player.setUp(false);
//		player.setLeft(false);
//		player.setRight(false);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
//		Vector2 newTouch = new Vector2(screenX, screenY);
//		// delta will now hold the difference between the last and the current touch positions
//	    // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
//	    Vector2 delta = newTouch.cpy().sub(lastTouch);
//	    lastTouch = newTouch;
//	    
//	    if(delta.x > 0){
//	    	player.setRight(true);
//	    	player.setLeft(false);
//	    } 
//	    
//	    if(delta.x < 0){
//	    	player.setLeft(true);
//	    	player.setRight(false);
//	    }
	    
	    return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleInput() {
		
		turnLeftAndroid.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			player.setLeft(true);
			player.setRight(false);
			return true;
		}
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button){
			player.setLeft(false);
		}
		
		});
		
		//play button touch 
		turnRightAndroid.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			player.setRight(true);
			return true;
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button){
			player.setRight(false);
		}
		
		});
		
		//play button touch 
		shootAndroid.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			isDown = true;
			return true;
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button){
			isDown = false;
		}
		
		});
		
		//play button touch 
		thrustOnAndroid.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			player.setUp(true);;
			return true;
		}		
		});
		
		//play button touch 
		thrustOffAndroid.addListener(new InputListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			player.setUp(false);;
			return true;
		}		
		});
	}
	

}
