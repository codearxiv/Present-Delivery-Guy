package com.FlappySanta;

import com.basic2DGL.Sprite;
import com.basic2DObj.SimpleObj;
import com.basic2DPhys.BoundingBox;

public class ChimneyObj extends GameObj{
	
	
	public int chimneyNumber;
	public boolean chimneyServiced;

	
	public ChimneyObj(float x, float y, float vx, float vy, float w, float h, float health, int id, boolean active) 
	{
		super(x,y,vx,vy,w,h,health,id,active);
		
	}	
	
}
