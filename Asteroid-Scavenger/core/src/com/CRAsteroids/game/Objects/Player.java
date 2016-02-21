package com.CRAsteroids.game.Objects;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.CRAsteroids.game.STATES.ChooseShipState;
import com.CRAsteroids.game.STATES.PlayState;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Player extends SpaceObject{
	
	private final int MAX_BULLETS = 4;
	private ArrayList<Bullet> bullets;
	
	private long startTime;
	
	private float[] flamex;
	private float[] flamey;
	
	private boolean left;
	private boolean right;
	private boolean up;
	
	private float maxSpeed;
	private float acceleration;
	private float deceleration;
	private float acceleratingTimer;
	
	//Ship properties
	public int playerWidth;
	public int playerHealth;
	public int playerShield;
	public Weapons currentWeapon;
	private int fighterWidth;
	private int fighterHeight;
	
	public int freighterWidth;
	public int freighterHeight;
	public int freighterCenterWidth;
	public int freighterTailLength;
	
	private boolean hit;
	private boolean dead;
	public boolean hyperDrive = false;
	
	private float hitTimer;
	private float hitTime;
	private Line2D.Float[] hitLines;
	private Point2D.Float[] hitLinesVector;
	
	public long score;
	public long playerCredit;
	public int mineLevel;
	public int fightLevel;
	public long playerMineXP;
	public long playerFightXP;
	public long xpToLevelMine;
	public long xpToLevelFight;
	private int extraLives;
	private long requiredScore;
	public int getx;
	public int gety;
	
	//Ship types
	public  boolean freighterShip;
	public  boolean fighterShip;
	
	public Player(ArrayList<Bullet> bullets){
		
		startTime = TimeUtils.nanoTime();
		
		playerHealth = 100;
		playerShield = 100;
		
		xpToLevelMine = 100;
		xpToLevelFight = 100;
		
		mineLevel = 1;
		fightLevel = 1;
		playerMineXP = 0;
		playerFightXP = 0;
		
		this.bullets = bullets;
		
		x = CRAsteroidsGame.WIDTH / 2;
		y = CRAsteroidsGame.HEIGHT / 2;
		
		maxSpeed = 300;
		acceleration = 200;
		deceleration = 10;
		
		fighterShip = ChooseShipState.fighterShip;
		freighterShip = ChooseShipState.freighterShip;
		
		if(fighterShip == true){
			shapex = new float[6];
			shapey = new float[6];
		}
		if(freighterShip == true){
			 shapex = new float[10];
			 shapey = new float[10];
		}
		
		flamex = new float[3];
		flamey = new float[3];
		
		radians = 3.1415f / 2;
		rotationSpeed = 3;
		
		//fighter dimensions
		fighterWidth = 16;
		fighterHeight = 8;
		
		//freighter dimensions
		freighterHeight = 16;
		freighterWidth = 12;
		freighterCenterWidth = 4;
		freighterTailLength = 8;
		
		//Ship dimensions
		if(fighterShip == true){
			playerWidth = fighterWidth;
		}else if(freighterShip == true){
			playerWidth = freighterWidth;
		}else{
		}
		
		hit = false;
		hitTimer = 0;
		hitTime = 2;
		
		score = 0;
		playerCredit = 0;
		extraLives = 3;
		requiredScore = 10000;
	}
	
	public enum Weapons{
		BOMB, SPREADBOMB, LASER, TRILASER
	}
	
	private void setShape(){
		if(fighterShip == true){
		//Tip of ship
		shapex[0] = x + MathUtils.cos(radians) * fighterHeight;
		shapey[0] = y + MathUtils.sin(radians) * fighterHeight;
		//Right wing mid
		shapex[1] = x + MathUtils.cos(radians - 2 * 3.1415f / 4) * fighterWidth / 6;
		shapey[1] = y + MathUtils.sin(radians - 2 * 3.1415f / 4) * fighterWidth / 6;
		//Right wing
		shapex[2] = x + MathUtils.cos(radians - 5 * 3.1415f / 6) * fighterWidth;
		shapey[2] = y + MathUtils.sin(radians - 5 * 3.1415f / 6) * fighterWidth;
		//Center
		shapex[3] = x + MathUtils.cos(radians + 3.1415f) * fighterHeight / 1.5f;
		shapey[3] = y + MathUtils.sin(radians + 3.1415f) * fighterHeight / 1.5f;
		//Left wing 
		shapex[4] = x + MathUtils.cos(radians + 5 * 3.1415f / 6) * fighterWidth;
		shapey[4] = y + MathUtils.sin(radians + 5 * 3.1415f / 6) * fighterWidth;
		//Left wing mid
		shapex[5] = x + MathUtils.cos(radians + 2 * 3.1415f / 4) * fighterWidth / 6;
		shapey[5] = y + MathUtils.sin(radians + 2 * 3.1415f / 4) * fighterWidth / 6;
		}
		
		if(freighterShip == true){
		//Front center
		shapex[0] = x + MathUtils.cos(radians) * freighterHeight;
		shapey[0] = y + MathUtils.sin(radians) * freighterHeight;
		//Right front
		shapex[1] = x + MathUtils.cos(radians - 3.1415f / 8) * freighterWidth;
		shapey[1] = y + MathUtils.sin(radians - 3.1415f / 8) * freighterWidth;
		//Right Front broad
		shapex[2] = x + MathUtils.cos(radians + 7 * 3.1415f / 4) * freighterCenterWidth;
		shapey[2] = y + MathUtils.sin(radians + 7 * 3.1415f / 4) * freighterCenterWidth;
		//Right back broad
		shapex[3] = x + MathUtils.cos(radians + 5 * 3.1415f / 4) * freighterCenterWidth;
		shapey[3] = y + MathUtils.sin(radians + 5 * 3.1415f / 4) * freighterCenterWidth;
		//Right back tip
		shapex[4] = x + MathUtils.cos(radians + 9 * 3.1415f / 8) * freighterWidth;
		shapey[4] = y + MathUtils.sin(radians + 9 * 3.1415f / 8) * freighterWidth;
		//Back center
		shapex[5] = x + MathUtils.cos(radians + 3.1415f) * freighterTailLength;
		shapey[5] = y + MathUtils.sin(radians + 3.1415f) * freighterTailLength;
		//Left back
		shapex[6] = x + MathUtils.cos(radians - 9 * 3.1415f / 8) * freighterWidth;
		shapey[6] = y + MathUtils.sin(radians - 9 * 3.1415f / 8) * freighterWidth;
		//Left back broad
		shapex[7] = x + MathUtils.cos(radians - 5 * 3.1415f / 4) * freighterCenterWidth;
		shapey[7] = y + MathUtils.sin(radians - 5 * 3.1415f / 4) * freighterCenterWidth;
		//Left front broad
		shapex[8] = x + MathUtils.cos(radians - 7 * 3.1415f / 4) * freighterCenterWidth;
		shapey[8] = y + MathUtils.sin(radians - 7 * 3.1415f / 4) * freighterCenterWidth;
		//Left front
		shapex[9] = x + MathUtils.cos(radians + 3.1415f / 8) * freighterWidth;
		shapey[9] = y + MathUtils.sin(radians + 3.1415f / 8) * freighterWidth;
		}
	}
	
	private void setFlame(){
		if(freighterShip == true){
			flamex[0] = shapex[5];
			flamey[0] = shapey[5];
		}
		
		if(fighterShip == true){
			flamex[0] = shapex[3];
			flamey[0] = shapey[3];	
		}
		
		flamex[1] = x + MathUtils.cos(radians - 3.1415f) * (6 + acceleratingTimer * 150);
		flamey[1] = y + MathUtils.sin(radians - 3.1415f) * (6 + acceleratingTimer * 150);
		
		flamex[2] = flamex[1];
		flamey[2] = flamey[1];
	}
	
	public void setLeft(boolean b){
		left = b;
	}
	
	public void setRight(boolean b){
		right = b;
	}
	
	public void setUp(boolean b){
		if(b && !up && !hit){
//			Jukebox.loop("thruster");
		}
		else if(!b){
//			Jukebox.stop("thruster");
		}
		up = b;
	}
	
	public void setPosition(float x, float y){
		super.setPosition(x, y);
		setShape();
	}
	
	public boolean isHit(){
		return hit;
		
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void reset(){
		x = CRAsteroidsGame.WIDTH / 2;
		y = CRAsteroidsGame.HEIGHT / 2;
		setShape();
		playerShield = 100;
		playerHealth = 100;
		hit = dead = false;
	}
	
	public long getScore(){
		return score;
	}
	
	public long getPlayerCredit(){
		return playerCredit;
	}
	
	public long getPlayerMineXP(){
		return playerMineXP;
	}
	
	public int getMineLevel(){
		return mineLevel;
	}
	
	public long getPlayerFightXP(){
		return playerFightXP;
	}
	
	public int getFightLevel(){
		return fightLevel;
	}
	
	public void levelUp(){
		if(playerMineXP > xpToLevelMine){
			incrementMineLevel();
			xpToLevelMine = (xpToLevelMine * mineLevel) + xpToLevelMine * 2;
		}
		
		if(playerFightXP > xpToLevelFight){
			incrementMineLevel();
			xpToLevelFight = (xpToLevelFight * fightLevel) + xpToLevelFight * 2;
		}
	}
	
	public int getPlayerHealth(){
		return playerHealth;
	}
	
	public int getPlayerShield(){
		return playerShield;
	}
	
	public int getLives(){
		return extraLives;
	}
	
	public void loseLife(){
		extraLives--;
	}
	
	public void incrementScore(long l){
		score += l;
	}
	
	public void incrementMineXP(long l){
		playerMineXP += l;
	}
	
	public void incrementFightXP(long l){
		playerFightXP += l;
	}
	
	public void incrementMineLevel(){
		mineLevel += 1;
	}
	
	public void incrementFightLevel(){
		fightLevel += 1;
	}
	
	public void incrementCredits(long l){
		playerCredit += l;
	}
	
	public void shoot(){
		if(bullets.size() >= MAX_BULLETS) return;
			if(fighterShip == true){
				if(currentWeapon == Weapons.BOMB){
					if(bullets.size() == MAX_BULLETS) return;
					bullets.add(new Bullet(x, y, radians));
			//		Jukebox.play("shoot");
				}
				
				if(currentWeapon == Weapons.SPREADBOMB){
					bullets.add(new Bullet(x, y, radians));
					bullets.add(new Bullet(x, y, radians + 3.1415f / 16));
					bullets.add(new Bullet(x, y, radians - 3.1415f / 16));
			//		Jukebox.play("shoot");
				}
				
				if(currentWeapon == Weapons.LASER){
					bullets.add(new Bullet(x, y , radians));
			//		Jukebox.play("shoot");
				}
				
				if(currentWeapon == Weapons.TRILASER){
					bullets.add(new Bullet(x, y, radians));
					bullets.add(new Bullet(x + 12 * MathUtils.sin(radians), y - 12 * MathUtils.cos(radians), radians));
					bullets.add(new Bullet(x - 12 * MathUtils.sin(radians), y + 12 * MathUtils.cos(radians), radians));
			//		Jukebox.play("shoot");
				}
				
		}
			if(freighterShip == true){
				bullets.add(new Bullet(shapex[2], shapey[2], radians - 3.1415f / 2));
				bullets.add(new Bullet(shapex[8], shapey[8], radians + 3.1415f / 2));
		//		Jukebox.play("shoot");
		}
	}
	
	public void hit(){
		
		if(playerHealth == 0){
			if(hit) return;
			
			hit = true;
			dx = dy = 0;
			left = right = up = false;
	//		Jukebox.stop("thruster");
			
			if(fighterShip == true)
				hitLines = new Line2D.Float[4];
			if(freighterShip == true)
				hitLines = new Line2D.Float[10];
			
			for(int i = 0, j = hitLines.length - 1; i < hitLines.length; j = i++){
				hitLines[i] = new Line2D.Float(shapex[i], shapey[i], shapex[j], shapey[j]);
			}
			
			if(fighterShip == true){
				hitLinesVector = new Point2D.Float[4];
				
				hitLinesVector[0] = new Point2D.Float(MathUtils.cos(radians + 1.5f),
						MathUtils.sin(radians + 1.5f));
				
				hitLinesVector[1] = new Point2D.Float(MathUtils.cos(radians - 1.5f),
						MathUtils.sin(radians - 1.5f));
				
				hitLinesVector[2] = new Point2D.Float(MathUtils.cos(radians - 2.8f),
						MathUtils.sin(radians - 2.8f));
				
				hitLinesVector[3] = new Point2D.Float(MathUtils.cos(radians + 2.8f),
						MathUtils.sin(radians + 2.8f));
			
			}
			
			if(freighterShip == true){
				hitLinesVector = new Point2D.Float[10];
				
				hitLinesVector[0] = new Point2D.Float(MathUtils.cos(radians + MathUtils.random(-3, 3)),
						MathUtils.sin(radians + MathUtils.random(-3, 3)));
				
				hitLinesVector[1] = new Point2D.Float(MathUtils.cos(radians - MathUtils.random(-3, 3)),
						MathUtils.sin(radians - MathUtils.random(-3, 3)));
				
				hitLinesVector[2] = new Point2D.Float(MathUtils.cos(radians - MathUtils.random(-3, 3)),
						MathUtils.sin(radians - MathUtils.random(-3, 3)));
				
				hitLinesVector[3] = new Point2D.Float(MathUtils.cos(radians + MathUtils.random(-3, 3)),
						MathUtils.sin(radians + MathUtils.random(-3, 3)));
				
				hitLinesVector[4] = new Point2D.Float(MathUtils.cos(radians + MathUtils.random(-3, 3)),
						MathUtils.sin(radians + MathUtils.random(-3, 3)));
				
				hitLinesVector[5] = new Point2D.Float(MathUtils.cos(radians - MathUtils.random(-3, 3)),
						MathUtils.sin(radians - MathUtils.random(-3, 3)));
				
				hitLinesVector[6] = new Point2D.Float(MathUtils.cos(radians - MathUtils.random(-3, 3)),
						MathUtils.sin(radians - MathUtils.random(-3, 3)));
				
				hitLinesVector[7] = new Point2D.Float(MathUtils.cos(radians + MathUtils.random(-3, 3)),
						MathUtils.sin(radians + MathUtils.random(-3, 3)));
				
				hitLinesVector[8] = new Point2D.Float(MathUtils.cos(radians - MathUtils.random(-3, 3)),
						MathUtils.sin(radians - MathUtils.random(-3, 3)));
				
				hitLinesVector[9] = new Point2D.Float(MathUtils.cos(radians + MathUtils.random(-3, 3)),
						MathUtils.sin(radians + MathUtils.random(-3, 3)));
				
			}
		}
		
	}
	
	public void update(float dt){
		if(hit){
			hitTimer += dt;
			if(hitTimer > hitTime){
				dead = true;
				hitTimer = 0;
			}
			for(int i = 0; i < hitLines.length; i++){
				hitLines[i].setLine(
							hitLines[i].x1 + hitLinesVector[i].x * 10 * dt,
							hitLines[i].y1 + hitLinesVector[i].y * 10 * dt,
							hitLines[i].x2 + hitLinesVector[i].x * 10 * dt,
							hitLines[i].y2 + hitLinesVector[i].y * 10 * dt
						);
			}
			return;
		}
		
		levelUp();
		
		//turning
		if(left){
			radians += rotationSpeed * dt;
		}else if(right){
			radians -= rotationSpeed * dt;
		}
		
		//accelerating
		if(up == true && hyperDrive == true){
			dx += MathUtils.cos(radians) * acceleration * 200 * dt;
			dy += MathUtils.sin(radians) * acceleration * 200 * dt;
			acceleratingTimer += dt;
			rotationSpeed = 1;
			if(acceleratingTimer > .3f){
				acceleratingTimer = 0;
			}
		}else if(up == true && hyperDrive == false){
			dx += MathUtils.cos(radians) * acceleration * dt;
			dy += MathUtils.sin(radians) * acceleration * dt;
			acceleratingTimer += dt;
			if(acceleratingTimer > .1f){
				acceleratingTimer = 0;
		}else{
			rotationSpeed = 3;
		}
	}
		
		//decelerating
		float vec = (float)Math.sqrt(dx * dx + dy * dy);
		if(vec > 0){
			dx -= (dx / vec) * deceleration * dt;
			dy -= (dy / vec) * deceleration * dt;
		}
		if(vec > maxSpeed){
			dx = (dx / vec) * maxSpeed;
			dy = (dy / vec) * maxSpeed;
		}
		
		//set position
		x += dx * dt;
		y += dy * dt;
		
		if (TimeUtils.timeSinceNanos(startTime) > 2000000000) {
			playerLocation();
			startTime = TimeUtils.nanoTime();
		}
		
		
		//setShape
		setShape();
		
		//set flame
		if(up){
			setFlame();
		}
		
		//screen wrap
//		wrap();
	}
	
	public void playerLocation(){
		getx = MathUtils.round(getx());
		gety = MathUtils.round(gety());
		
		if(PlayState.Quad == "1"){
			getx = getx;
			gety = gety;
		}else if(PlayState.Quad == "2"){
			getx = getx - PlayState.quadInterval;
			gety = gety;
		}else if(PlayState.Quad == "3"){
			getx = getx - PlayState.quadInterval;
			gety = gety - PlayState.quadInterval;
		}else if(PlayState.Quad == "4"){
			getx = getx;
			gety = gety - PlayState.quadInterval;
		}else if(PlayState.Quad == "5"){
			getx = getx;
			gety = gety - (PlayState.quadInterval * 2);
		}else if(PlayState.Quad == "6"){
			getx = getx - PlayState.quadInterval;
			gety = gety - (PlayState.quadInterval * 2);
		}else if(PlayState.Quad == "7"){
			getx = getx - PlayState.quadInterval;
			gety = gety - (PlayState.quadInterval * 3);
		}else if(PlayState.Quad == "8"){
			getx = getx;
			gety = gety - (PlayState.quadInterval * 3);
		}
	}
	
	public void draw(ShapeRenderer sr){
		
		if(PlayState.isFlashing == true){
			sr.setColor(Color.PURPLE);
		}else{
			sr.setColor(Color.WHITE);
		}
		
		sr.begin(ShapeType.Line);
		
		//check if hit
		if(hit){
			for(int i = 0; i < hitLines.length; i++){
				sr.line(
						hitLines[i].x1,
						hitLines[i].y1,
						hitLines[i].x2,
						hitLines[i].y2
						);
			}
			sr.end();
			return;
		}
		
		//draw ship
		if(fighterShip == true){
			for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
				sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
			}
		}
		
		if(freighterShip == true){
			for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
				sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
			}
		}
		
		//draw flames
		if(up){
			for(int i = 0, j = flamex.length - 1; i < flamex.length; i++){
				sr.line(flamex[i], flamey[i], flamex[j], flamey[j]);
			}
		}
		
		sr.end();
	}
	
}
