package com.basic2DPhys;

import com.basic2DObj.EmptyObj;

public class DirectionalForce {

	EmptyObj obj;
	
	float dirX, dirY;
	
	
	//----------------------------------------------------------------------------
	public DirectionalForce(float x, float y, float acceleration, EmptyObj o)
	{
		dirX = (float) (acceleration*x/(Math.sqrt(x*x+y*y)));
		dirY = (float) (acceleration*y/(Math.sqrt(x*x+y*y)));
		
		obj = o;
		
	}
	//----------------------------------------------------------------------------
	public DirectionalForce(float dx, float dy, EmptyObj o)
	{
		dirX = dx;
		dirY = dy;
		
		obj = o;
		
	}
	//----------------------------------------------------------------------------	
	public void setObj(EmptyObj o)
	{
		obj = o;
	}
	//----------------------------------------------------------------------------
	public void setVector(float dx, float dy)
	{
		dirX = dx;
		dirY = dy;	
	}
	//----------------------------------------------------------------------------
	public void setAcceleration(float acceleration)
	{
		dirX = (float) (acceleration*dirX/(Math.sqrt(dirX*dirX+dirY*dirY)));
		dirY = (float) (acceleration*dirY/(Math.sqrt(dirX*dirX+dirY*dirY)));	
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime)
	{								
		obj.velX += elapsedTime*dirX;
		obj.velY += elapsedTime*dirY;
	
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, EmptyObj o)
	{								
		o.velX += elapsedTime*dirX;
		o.velY += elapsedTime*dirY;
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, float mobility)
	{								
		obj.velX += elapsedTime*mobility*dirX;
		obj.velY += elapsedTime*mobility*dirY;
	
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, EmptyObj o, float mobility)
	{								
		o.velX += elapsedTime*mobility*dirX;
		o.velY += elapsedTime*mobility*dirY;
	}
	//----------------------------------------------------------------------------
}
