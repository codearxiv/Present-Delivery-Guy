package com.basic2DPhys;

import com.basic2DObj.EmptyObj;

public class ObjForce {


	EmptyObj obj1, obj2;

	float acceleration;
	int taper;
	
	//----------------------------------------------------------------------------	
	public ObjForce(float a, int t, EmptyObj o1, EmptyObj o2)
	{
		acceleration = a;
		taper = t;
		
		obj1 = o1;
		obj2 = o2;
		
	}
	//----------------------------------------------------------------------------	
	public void setObj(EmptyObj o1, EmptyObj o2){
		
		obj1 = o1;
		obj2 = o2;
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, float mobilityObj1, float mobilityObj2)
	{
		float diffX = obj1.posX-obj2.posX;
		float diffY = obj1.posY-obj2.posY;
		
		float distSq = diffX*diffX + diffY*diffY;
		float declination = 0.0f;
				
		switch(taper){		
			case 0: declination = elapsedTime*acceleration/(float)Math.sqrt(distSq); break;
			case 1: declination = elapsedTime*acceleration/distSq; break;
			case 2: declination = elapsedTime*acceleration/(distSq*(float)Math.sqrt(distSq)); break;
			case 3: declination = elapsedTime*acceleration/(distSq*distSq); break;
			default: declination = elapsedTime*acceleration/(distSq*distSq*distSq); break;		
		}						
				
									
		obj1.velX += mobilityObj1*declination*diffX;
		obj1.velY += mobilityObj1*declination*diffY;
					
		obj2.velX -= mobilityObj2*declination*diffX;
		obj2.velY -= mobilityObj2*declination*diffY;			
		
	}
	//----------------------------------------------------------------------------
	public void nextActionX(float elapsedTime, float mobilityObj1, float mobilityObj2)
	{
		float diffX = Math.abs(obj1.posX-obj2.posX);
		
		float declination = 0.0f;
				
		switch(taper){		
			case 0: declination = elapsedTime*acceleration; break;
			case 1: declination = elapsedTime*acceleration/diffX; break;
			case 2: declination = elapsedTime*acceleration/(diffX*diffX); break;
			case 3: declination = elapsedTime*acceleration/(diffX*diffX*diffX); break;
			default: declination = elapsedTime*acceleration/(diffX*diffX*diffX*diffX); break;		
		}						
				
									
		obj1.velX += mobilityObj1*declination;					
		obj2.velX -= mobilityObj2*declination;
		
	}
	//----------------------------------------------------------------------------
	public void nextActionY(float elapsedTime, float mobilityObj1, float mobilityObj2)
	{
		float diffY = Math.abs(obj1.posY-obj2.posY);
		
		float declination = 0.0f;
				
		switch(taper){		
			case 0: declination = elapsedTime*acceleration; break;
			case 1: declination = elapsedTime*acceleration/diffY; break;
			case 2: declination = elapsedTime*acceleration/(diffY*diffY); break;
			case 3: declination = elapsedTime*acceleration/(diffY*diffY*diffY); break;
			default: declination = elapsedTime*acceleration/(diffY*diffY*diffY*diffY); break;		
		}						
				
									
		obj1.velY += mobilityObj1*declination;					
		obj2.velY -= mobilityObj2*declination;
		
	}

	//---------------------------------------------------------------------------- 	
	
	
}
