package com.CRAsteroids.game.Objects;

import com.CRAsteroids.game.CRAsteroidsGame;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Credits extends SpaceObject{
	
	private float acceleration = 2.0f;
	
	public long score;
	
	public Credits(float x, float y){
		
		this.x = x;
		this.y = y;
		
		shapex = new float[4];
		shapey = new float[4];
	
		radians = 3.1415f / 2;
		
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
	
	public long getScore(){
		return score;
	}
	
	public void update(float dt){
		
		dx += MathUtils.cos(MathUtils.random()) * acceleration * dt;
		dy += MathUtils.sin(MathUtils.random()) * acceleration * dt;
		
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
		sr.setColor(1, 1, 1, 1);
		sr.begin(ShapeType.Line);
		
		for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
			sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
		}
		
		sr.end();
	}
		

}
