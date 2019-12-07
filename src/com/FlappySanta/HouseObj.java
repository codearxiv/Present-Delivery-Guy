package com.FlappySanta;

import javax.microedition.khronos.opengles.GL11;

import com.basic2DGL.Sprite;
import com.basic2DObj.SimpleObj;

public class HouseObj extends SimpleObj{
	
	
	Sprite sprite = null;
	
	
	
	public HouseObj(float x, float y, float vx, float vy, float w, float h, int id, boolean active) 
	{
		super(x,y,vx,vy,w,h,id,active);
		
	}
	
	public void setSprite(Sprite s)
	{
		sprite = s;
	}
	
	
	public void drawClientSide(GL11 gl, boolean color, boolean texture)
	{
		drawClientSide(gl,sprite,textureIndex,color,texture);	
	}

}
