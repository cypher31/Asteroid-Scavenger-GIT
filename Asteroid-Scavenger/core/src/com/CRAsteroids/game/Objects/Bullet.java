package com.CRAsteroids.game.Objects;

import com.CRAsteroids.game.Objects.Player.Weapons;
import com.CRAsteroids.game.STATES.PlayState;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends SpaceObject{

	private float lifeTime;
	private float lifeTimer;
	public int bulletDamage;
	public float enemyXPosition;
	public float enemyYPosition;
	
	private boolean remove;
	
	public Bullet(float x, float y, float radians){
		
		if(PlayState.player.currentWeapon == Weapons.BOMB){
			this.x = x;
			this.y = y;
			this.radians = radians;
			
			bulletDamage = 7;
			
			float speed = 250;
			dx = MathUtils.cos(radians) * speed;
			dy = MathUtils.sin(radians) * speed;
			
			width = height = 2;
			
			lifeTimer = 0;
			lifeTime = 1;
		}
		
		if(PlayState.player.currentWeapon == Weapons.SPREADBOMB){
			this.x = x;
			this.y = y;
			this.radians = radians;
			
			bulletDamage = 4;
			
			float speed = 250;
			dx = MathUtils.cos(radians) * speed;
			dy = MathUtils.sin(radians) * speed;
			
			width = height = 2;
			
			lifeTimer = 0;
			lifeTime = 1;
		}
		
		if(PlayState.player.currentWeapon == Weapons.LASER){
			this.x = x;
			this.y = y;
			this.radians = radians;
			
			bulletDamage = 4;
			
			float speed = 750;
			dx = MathUtils.cos(radians) * speed;
			dy = MathUtils.sin(radians) * speed;
			
			width = 20;
			height = 2;
			
			lifeTimer = 0;
			lifeTime = 1;
		}
		
		if(PlayState.player.currentWeapon == Weapons.TRILASER){
			this.x = x;
			this.y = y;
			this.radians = radians;
			
			bulletDamage = 2;
			
			float speed = 500;
			dx = MathUtils.cos(radians) * speed;
			dy = MathUtils.sin(radians) * speed;
			
			width = 15;
			height = 1;
			
			lifeTimer = 0;
			lifeTime = 1;
		}
		
	}
	
	public boolean shouldRemove(){
		return remove;
	}
	
	public void update(float dt){
		
		x += dx * dt;
		y += dy * dt;
		
		lifeTimer += dt;
		if(lifeTimer > lifeTime){
			remove = true;
		}
	}
	
	public void draw(ShapeRenderer sr){
		//player weapons
		if(PlayState.player.currentWeapon == Weapons.BOMB || PlayState.player.currentWeapon == Weapons.SPREADBOMB){
			sr.setColor(0.3f, 1.0f, 0.3f, 1.0f);
			sr.begin(ShapeType.Filled);
			sr.circle(x, y, width / 2);
			sr.end();
		}
		
		if(PlayState.player.currentWeapon == Weapons.LASER || PlayState.player.currentWeapon == Weapons.TRILASER){
			sr.setColor(0.3f, 1.0f, 0.3f, 1.0f);
			sr.begin(ShapeType.Line);
			sr.rect(x, y, 0, 0, width, height, 1, 1, MathUtils.radiansToDegrees * radians + 180);
			sr.end();
		}
		
	}
}
