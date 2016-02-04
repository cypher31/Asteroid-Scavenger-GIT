package com.CRAsteroids.game.STATES;

import java.util.ArrayList;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.CRAsteroids.game.GameKeys;
import com.CRAsteroids.game.Save;
import com.CRAsteroids.game.Objects.Asteroid;
import com.CRAsteroids.game.Objects.Bullet;
import com.CRAsteroids.game.Objects.Credits;
import com.CRAsteroids.game.Objects.FlyingSaucer;
import com.CRAsteroids.game.Objects.Particle;
import com.CRAsteroids.game.Objects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PlayState extends GameState implements InputProcessor{
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private OrthographicCamera cam;
	private Vector3 screenCoords;
	public static String Quad;
	public static int quadInterval = 5000;
	
	private FitViewport playViewPort;
	private Stage playerHud;
	private LabelStyle scoreStyle;
	private Label hudScore;
	private Label lives;
	private Label creditsHud;
	private String playerScore;
	private String playerCredits;
	private String playerLives;
	private Table scoreTable;
	private Table livesTable;
	private Table creditsTable;
	
	private BitmapFont font;
	private Player hudPlayer;
	
	public Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Bullet> enemyBullets;
	private ArrayList<Credits> credits;
	
	private FlyingSaucer flyingSaucer;
	private float fsTimer;
	private float fsTime;
	
	private ArrayList<Particle> particles;
	
	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	private Vector2 lastTouch = new Vector2();
	
	private float maxDelay;
	private float minDelay;
	private float currentDelay;
	private float bgTimer;
	private boolean playLowPulse;

	protected PlayState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
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
		
		level = 1;
		spawnAsteroids();
		
		hudPlayer = new Player(null);
		
		fsTimer = 0;
		fsTime = 15;
		enemyBullets = new ArrayList<Bullet>();
		
		Gdx.input.setInputProcessor(this);
		
		//Set up bg music
		maxDelay = 1;
		minDelay = .25f;
		currentDelay = maxDelay;
		bgTimer = maxDelay;
		playLowPulse = true;
		
		//*HUD stage
		playViewPort = new FitViewport(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
		playerHud = new Stage(playViewPort);
		
		scoreStyle = CRAsteroidsGame.smallStyle;
		
		hudScore = new Label("Score: 0", scoreStyle);
		lives = new Label("Extra Lives: " + player.getLives(), scoreStyle);
		creditsHud = new Label("Credits: " + player.getPlayerCredit(), scoreStyle);
		
		//add actors
		scoreTable = new Table();
		livesTable = new Table();
		creditsTable = new Table();
		
		scoreTable.setFillParent(true);
		livesTable.setFillParent(true);
		creditsTable.setFillParent(true);
		
		playerHud.addActor(scoreTable);
		playerHud.addActor(livesTable);
		playerHud.addActor(creditsTable);
		
		scoreTable.align(Align.top);
		scoreTable.add(hudScore).padTop(Gdx.graphics.getHeight() * .025f).row();
		scoreTable.add(creditsHud).row();
		scoreTable.add(lives);
		
		playerHud.setDebugAll(true);
		scoreTable.setDebug(true);
		livesTable.setDebug(true);
		
//				sb.setColor(1, 1, 1, 1);
//				sb.begin();
//				String playerScore = Long.toString(player.getScore());
//				TextBounds playerScoreBounds = font.getBounds(playerScore);
//				font.draw(
//						sb, 
//						playerScore, 
//						player.getx() - playerScoreBounds.width / 2,
//						player.gety() + 450);
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
		
		int numToSpawn = 4 + level - 1;
		totalAsteroids = numToSpawn * 7;
		numAsteroidsLeft = totalAsteroids;
		currentDelay = maxDelay;
		
		for(int i = 0; i < numToSpawn; i++){
			float x = MathUtils.random(CRAsteroidsGame.WIDTH);
			float y = MathUtils.random(CRAsteroidsGame.HEIGHT);
			
			float dx = x - player.getx();
			float dy = y - player.gety();
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			
			while (dist < 100){
				x = MathUtils.random(CRAsteroidsGame.WIDTH);
				y = MathUtils.random(CRAsteroidsGame.HEIGHT);
				dx = x - player.getx();
				dy = y - player.gety();
				dist = (float) Math.sqrt(dx * dx + dy * dy);
			}
			
			asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
		}
	}
	
	@Override
	public void update(float dt) {
		handleInput();
		
		Quadrants();
		
		cam = new OrthographicCamera(CRAsteroidsGame.WIDTH, CRAsteroidsGame.HEIGHT);
		cam.position.set(player.getx(), player.gety(), 0);
		cam.update();
		
		//next level
		if(asteroids.size() == 0){
			level++;
			spawnAsteroids();
		}
		
		
		//update credits
		for(int i = 0; i < credits.size(); i++)
		credits.get(i).update(dt);
		
		//update player
		player.update(dt);
		if(player.isDead()){
			if(player.getLives() == 0) {
//				Jukebox.stopAll();
				Save.gd.setTentativeScore(player.getScore());
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
					player.incrementScore(a.getScore());
	//				Jukebox.play("explode");
					if(a.getType() == 2){
						System.out.println(a.getType());
						credits.add(new Credits(a.getx(), a.gety()));
					}
					if(a.getType() == 1){
						System.out.println(a.getType());
						credits.add(new Credits(a.getx(), a.gety()));
						credits.add(new Credits(a.getx(), a.gety()));
					}
					if(a.getType() == 0){
						System.out.println(a.getType());
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
					System.out.println(player.getPlayerCredit());
				}
				
			}
		}
		
		//player-flying saucer collision
		if(flyingSaucer != null){
			if(player.intersects(flyingSaucer)){
				player.hit();
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
					player.incrementScore(flyingSaucer.getScore());
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
			asteroids.get(i).draw(sr);
		}
		
		//draw particles
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).draw(sr);
		}
		
		//draw hud
		if(player!= null){
		playerScore = Long.toString(player.getScore());
		hudScore.setText("Score: " + playerScore);
		playerCredits = Long.toString(player.getPlayerCredit());
		creditsHud.setText("Credits: " + playerCredits);
		playerLives = Long.toString(player.getLives());
		lives.setText("Extra Lives: " + playerLives);
		}
		
//		//draw lives
//		for(int i = 0; i < player.getLives(); i++){
//			hudPlayer.setPosition(
//					(player.getx() - (player.playerWidth + player.playerWidth / 2)) + (player.playerWidth + player.playerWidth / 2) * i, 
//					player.gety() + 400);
//			hudPlayer.draw(sr);
//		}
	}
	
	public void Quadrants(){
		if(player.getx() < 5000 && player.gety() < 5000){
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
			player.hyperDrive = true;
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
		
		lastTouch.set(screenX, screenY);
		
		if(Gdx.input.isTouched(0)){
			player.setUp(true);
		}
		
		if(Gdx.input.isTouched(1)){
			player.shoot();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		player.setUp(false);
		player.setLeft(false);
		player.setRight(false);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		Vector2 newTouch = new Vector2(screenX, screenY);
		// delta will now hold the difference between the last and the current touch positions
	    // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
	    Vector2 delta = newTouch.cpy().sub(lastTouch);
	    lastTouch = newTouch;
	    
	    if(delta.x > 0){
	    	player.setRight(true);
	    	player.setLeft(false);
	    } 
	    
	    if(delta.x < 0){
	    	player.setLeft(true);
	    	player.setRight(false);
	    }
	    
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
		// TODO Auto-generated method stub
		
	}

}
