package com.CRAsteroids.game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Credits extends SpaceObject{
	
	private float acceleration = 2.0f;
	
	public long score;
	private long creditWorth;
	
	public Credits(float x, float y){
		
		this.x = x;
		this.y = y;
		
		speed = MathUtils.random(25, 75);
		
		shapex = new float[4];
		shapey = new float[4];
	
		radians = MathUtils.random(2 * 3.1415f);
		dx = MathUtils.cos(radians) * speed;
		dy = MathUtils.sin(radians) * speed;
		
		creditWorth = MathUtils.random(50, 150);
		
	}
	
	private void setShape(){
		
		shapex[0] = x + MathUtils.cos(radians) * 4;
		shapey[0] = y + MathUtils.sin(radians) * 4;
		
		shapex[1] = x + MathUtils.cos(radians + 3.1415f / 2) * 4;
		shapey[1] = y + MathUtils.sin(radians + 3.1415f / 2) * 4;
		
		shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 4;
		shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 4;
		
		shapex[3] = x + MathUtils.cos(radians + 3.1415f + 3.1415f / 2) * 4;
		shapey[3] = y + MathUtils.sin(radians + 3.1415f + 3.1415f / 2) * 4;
	}
	
	public void setPosition(float x, float y){
		super.setPosition(x, y);
		setShape();
	}
	
	public long creditWorth(){
		return creditWorth;
	}
	
	public void update(float dt){
		
		dx = MathUtils.cos(radians) * speed;
		dy = MathUtils.sin(radians) * speed;
		
		//set position
		x += dx * dt;
		y += dy * dt;
		
//		if (TimeUtils.timeSinceNanos(startTime) > 2000000000) {
//			playerLocation();
//			startTime = TimeUtils.nanoTime();
//			}
		
		
		//setShape
		setShape();
	}
	
	public void draw(ShapeRenderer sr){
		sr.setColor(Color.YELLOW);
		sr.begin(ShapeType.Line);
		
		for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
			sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
		}
		
		sr.end();
	}
		

}
