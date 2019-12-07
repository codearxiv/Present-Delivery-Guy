package com.basic2DPhys;

import com.basic2DObj.EmptyObj;

public class DirectionalFriction {

	
	EmptyObj obj;
	
	float dirX;
	float dirY;
	
	//----------------------------------------------------------------------------
	public DirectionalFriction(float x, float y, float invMagnitude, EmptyObj o)
	{
		dirX = (float) (invMagnitude*x/(Math.sqrt(x*x+y*y)));
		dirY = (float) (invMagnitude*y/(Math.sqrt(x*x+y*y)));
		
		obj = o;
	}	
	//----------------------------------------------------------------------------
	public DirectionalFriction(float dx, float dy, EmptyObj o)
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
	public void setAcceleration(float invMagnitude)
	{
		dirX = (float) (invMagnitude*dirX/(Math.sqrt(dirX*dirX+dirY*dirY)));
		dirY = (float) (invMagnitude*dirY/(Math.sqrt(dirX*dirX+dirY*dirY)));	
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime)
	{		
		if( Math.signum(obj.velX) != Math.signum(dirX) ){
			obj.velX *= Math.pow(Math.abs(dirX),elapsedTime);
		}
		
		if( Math.signum(obj.velY) != Math.signum(dirY) ){
			obj.velY *= Math.pow(Math.abs(dirY),elapsedTime);
		}

	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, EmptyObj o)
	{								
		if( Math.signum(o.velX) != Math.signum(dirX) ){
			o.velX *= Math.pow(Math.abs(dirX),elapsedTime);
		}
		
		if( Math.signum(o.velY) != Math.signum(dirY) ){
			o.velY *= Math.pow(Math.abs(dirY),elapsedTime);
		}	
		
	}		
	//----------------------------------------------------------------------------
	
	
}
