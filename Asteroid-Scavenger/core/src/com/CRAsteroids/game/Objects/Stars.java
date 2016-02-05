package com.CRAsteroids.game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Stars extends SpaceObject{
	
	private float[] shapex1;
	private float[] shapex2;
	private float[] shapey1;
	private float[] shapey2;
	
	public int xLength;
	public int yLength;
	
	public Stars(float x, float y){
		
		this.x = x;
		this.y = y;
		
		shapex1 = new float [2];
		shapey1 = new float [2];
		
		shapex2 = new float [2];
		shapey2 = new float [2];
		
		xLength = MathUtils.random(4, 8);
		yLength = xLength;
	}
	
	private void setShape(){
		
		shapex1[0] = x;
		shapey1[0] = y;
		
		shapex1[1] = x + xLength;
		shapey1[1] = y;
		
		shapex2[0] = x + xLength / 2;
		shapey2[0] = y - yLength / 2;
		
		shapex2[1] = x + xLength / 2;
		shapey2[1] = y + yLength / 2;
	}
	
	public void setPosition(float x, float y){
		super.setPosition(x, y);
		setShape();
	}
	
	public void update(float dt){
		
		setShape();
		
	}
	
	public void draw(ShapeRenderer sr){
		sr.setColor(Color.WHITE);
		sr.begin(ShapeType.Line);
		
		for(int i = 0, j = shapex1.length - 1; i < shapex1.length; j = i ++){
			sr.line(shapex1[i], shapey1[i], shapex1[j], shapey1[j]);
		}
		
		for(int i = 0, j = shapex2.length -1; i < shapex2.length; j = i ++){
			sr.line(shapex2[i], shapey2[i], shapex2[j], shapey2[j]);
		}
		
		sr.end();
	}

}
