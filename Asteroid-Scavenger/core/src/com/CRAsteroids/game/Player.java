package com.CRAsteroids.game;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Player extends SpaceObject{
	
	private final int MAX_BULLETS = 4;
	private ArrayList<Bullet> bullets;
	
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
	private int fighterWidth;
	private int fighterHeight;
	
	public int freighterWidth;
	public int freighterHeight;
	public int freighterCenterWidth;
	public int freighterTailLength;
	
	private boolean hit;
	private boolean dead;
	
	private float hitTimer;
	private float hitTime;
	private Line2D.Float[] hitLines;
	private Point2D.Float[] hitLinesVector;
	
	public long score;
	private int extraLives;
	private long requiredScore;
	
	//Ship types
	public boolean fighterShip;
	public boolean freighterShip;
	
	public Player(ArrayList<Bullet> bullets){
		
		this.bullets = bullets;
		
		x = CRAsteroidsGame.WIDTH / 2;
		y = CRAsteroidsGame.HEIGHT / 2;
		
		maxSpeed = 300;
		acceleration = 200;
		deceleration = 10;
		
		freighterShip = true;
		
		if(fighterShip == true){
			shapex = new float[4];
			shapey = new float[4];
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
		fighterWidth = 8;
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
			System.out.println("no width");
		}
		
		hit = false;
		hitTimer = 0;
		hitTime = 2;
		
		score = 0;
		extraLives = 3;
		requiredScore = 10000;
	}
	
	private void setShape(){
		if(fighterShip == true){
		//Tip of ship
		shapex[0] = x + MathUtils.cos(radians) * fighterWidth;
		shapey[0] = y + MathUtils.sin(radians) * fighterWidth;
		//Right wing
		shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * fighterWidth;
		shapey[1] = y + MathUtils.sin(radians - 4 * 3.1415f / 5) * fighterWidth;
		//Center
		shapex[2] = x + MathUtils.cos(radians + 3.1415f) * fighterHeight / 1.5f;
		shapey[2] = y + MathUtils.sin(radians + 3.1415f) * fighterHeight / 1.5f;
		//Left wing
		shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f /5) * fighterWidth;
		shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * fighterWidth;
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
			flamex[0] = shapex[2];
			flamey[0] = shapey[2];	
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
		hit = dead = false;
	}
	
	public long getScore(){
		return score;
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
	
	public void shoot(){
		if(bullets.size() == MAX_BULLETS) return;
		if(fighterShip == true){
		bullets.add(new Bullet(x, y, radians));
//		Jukebox.play("shoot");
		}
		
		System.out.println(getScore());
		
		if(bullets.size() == MAX_BULLETS && freighterShip == true) return;
		if(freighterShip == true){
		bullets.add(new Bullet(shapex[2], shapey[2], radians - 3.1415f / 2));
		bullets.add(new Bullet(shapex[8], shapey[8], radians + 3.1415f / 2));
//		Jukebox.play("shoot");
		}
	}
	
	public void hit(){
		
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
	
	public void update(float dt){
		
//		for(int i = 0; i < shapex.length; i++){
//			System.out.printf("%s, %s\n", shapex[i], shapey[i]);
//		}
		
		//check if hit
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
		
		//check extra lives
		if(score >= requiredScore){
			extraLives++;
			requiredScore += 10000;
//			Jukebox.play("extralife");
		}
		
		//turning
		if(left){
			radians += rotationSpeed * dt;
		}else if(right){
			radians -= rotationSpeed * dt;
		}
		
		//accelerating
		if(up){
			dx += MathUtils.cos(radians) * acceleration * dt;
			dy += MathUtils.sin(radians) * acceleration * dt;
			acceleratingTimer += dt;
			if(acceleratingTimer > .1f){
				acceleratingTimer = 0;
			}
		}else{
			acceleratingTimer = 0;
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
		
		//setShape
		setShape();
		
		//set flame
		if(up){
			setFlame();
		}
		
		//screen wrap
		wrap();
	}
	
	public void draw(ShapeRenderer sr){
		
		sr.setColor(1, 1, 1, 1);
		
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
		for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
			sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
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
