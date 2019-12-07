package com.basic2DObj;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.basic2DGL.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;


public class EmptyObj{

	

	public boolean active;
	
	public float posX, posY;
	
	public float velX, velY;
	//public float accX, accY;
	
	public int id;

	
	
	
	//----------------------------------------------------------------------------
	public EmptyObj() 
	{
		velX = 0.0f;
		velY = 0.0f;
		
		//accX = 0.0f;
		//accY = 0.0f;	
	}	
	//----------------------------------------------------------------------------
	public EmptyObj(float x, float y, float vx, float vy, boolean active)
	{
		posX = x;
		posY = y;	
				
		velX = vx;
		velY = vy;
		
		//accX = ax;
		//accY = ay;
		
		
		
		this.active = active;
	}
	//----------------------------------------------------------------------------
	public void setId(int i) 
	{
		id = i;
	}
	
	//----------------------------------------------------------------------------
	public void setActive() 
	{
		active = true;
	}
	//----------------------------------------------------------------------------
	public void setInactive() 
	{
		active = false;
	}
	//----------------------------------------------------------------------------
	public boolean isActive() 
	{
		return active;
	}
	//----------------------------------------------------------------------------
	public void setPos(float x, float y)
	{
		posX = x;
		posY = y;	
	}
	//----------------------------------------------------------------------------
	public void incrementPos(float x, float y)
	{
		posX += x;
		posY += y;	
	}
	//---------------------------------------------------------------------------- 
	public void setVelocity(float vx, float vy)
	{
		velX = vx;
		velY = vy;
	}
	//----------------------------------------------------------------------------
	public void incrementVelocity(float vx, float vy)
	{
		velX += vx;
		velY += vy;	
	}
	//---------------------------------------------------------------------------- 
	/*
	public void setAcceleration(float ax, float ay)
	{
		accX = ax;
		accY = ay;
	}
	//----------------------------------------------------------------------------
	public void incrementAcceleration(float ax, float ay)
	{
		accX += ax;
		accY += ay;	
	}
	//---------------------------------------------------------------------------- 
	public void nextVelocity(float elapsedTime)
	{
		velX += elapsedTime*accX;
		velY += elapsedTime*accY;
	}
	//---------------------------------------------------------------------------- 
	public void nextVelocityX(float elapsedTime)
	{
		velX += elapsedTime*accX;
	}
	//---------------------------------------------------------------------------- 
	public void nextVelocityY(float elapsedTime)
	{
		velY += elapsedTime*accY;
	}
	*/
	//---------------------------------------------------------------------------- 
	public void nextPos(float elapsedTime)
	{
		posX += elapsedTime*velX;
		posY += elapsedTime*velY;	
	}
	//---------------------------------------------------------------------------- 
	public void nextPosX(float elapsedTime)
	{
		posX += elapsedTime*velX;
	}
	//---------------------------------------------------------------------------- 
	public void nextPosY(float elapsedTime)
	{
		posY += elapsedTime*velY;
	}
	//----------------------------------------------------------------------------

}
