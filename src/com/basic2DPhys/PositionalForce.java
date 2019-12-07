package com.basic2DPhys;

import com.basic2DObj.EmptyObj;

public class PositionalForce {

	EmptyObj obj;
	
	float posX, posY;
	float acceleration;
	int taper;	
	
	//----------------------------------------------------------------------------
	public PositionalForce(float x, float y, float a, int t, EmptyObj o)
	{
		posX = x;
		posY = y;
		acceleration = a;
		taper = t;
		
		obj = o;
		
	}
	//----------------------------------------------------------------------------	
	public void setObj(EmptyObj o)
	{
		
		obj = o;
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
	public void nextAction(float elapsedTime, float mobility)
	{
		float diffX = obj.posX-posX;
		float diffY = obj.posY-posY;
		
		float distSq = diffX*diffX + diffY*diffY;
		float declination = 0f;

		switch(taper){		
			case 0: declination = elapsedTime*acceleration/(float)Math.sqrt(distSq); break;
			case 1: declination = elapsedTime*acceleration/distSq; break;
			case 2: declination = elapsedTime*acceleration/(distSq*(float)Math.sqrt(distSq)); break;
			case 3: declination = elapsedTime*acceleration/(distSq*distSq); break;
			default: declination = elapsedTime*acceleration/(distSq*distSq*distSq); break;		
		}		
		//declination = elapsedTime*acceleration/(float)Math.pow(diffX*diffX + diffY*diffY,0.5f*(taper+1));
		
		
		obj.velX += mobility*declination*diffX;
		obj.velY += mobility*declination*diffY;
									
		
	}

	//----------------------------------------------------------------------------
	
	
}
