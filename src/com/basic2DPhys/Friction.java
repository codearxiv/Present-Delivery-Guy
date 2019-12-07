package com.basic2DPhys;

import com.basic2DObj.EmptyObj;

public class Friction {

	EmptyObj obj;
	
	float invMagnitude;
	
	//----------------------------------------------------------------------------
	public Friction(float m, EmptyObj o)
	{
		invMagnitude = m;
		obj = o;
	}
	//----------------------------------------------------------------------------	
	public void setObj(EmptyObj o)
	{
		obj = o;
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime)
	{								
		double invEnergyLoss = Math.pow(invMagnitude,elapsedTime);
		
		obj.velX *= invEnergyLoss;
		obj.velY *= invEnergyLoss;
	
	}	
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, EmptyObj o)
	{								
		double invEnergyLoss = Math.pow(invMagnitude,elapsedTime);
		
		o.velX *= invEnergyLoss;
		o.velY *= invEnergyLoss;
	}
	//----------------------------------------------------------------------------
	public static void nextAction(EmptyObj o, float invEnergyLoss)
	{								
		o.velX *= invEnergyLoss;
		o.velY *= invEnergyLoss;
	}	
	//----------------------------------------------------------------------------
	
	
}
