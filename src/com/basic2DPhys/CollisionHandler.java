package com.basic2DPhys;



public class CollisionHandler 
{
/*
	public static void nextAction(BoundingBox bb1, BoundingBox bb2, float elasticity, float mobility1, float mobility2)
	{
		float x1 = bb1.getPosX();
		float x2 = bb2.getPosX();
		float y1 = bb1.getPosY();
		float y2 = bb2.getPosY();
		
		
		float diffX = x1 - x2;
		float diffY = y1 - y2;
		
		float inverseM = 1f/(mobility1+mobility2);

		if( Math.abs(diffX)/(bb1.width+bb2.width) > Math.abs(diffY)/(bb1.height+bb2.height) ){

			float centroidPos =  inverseM*(mobility2*x1 + mobility1*x2);
			float incrementPos = inverseM*Math.signum(diffX)*(bb1.width+bb2.width+10e-4f);
			
			if( !(bb1 instanceof BoundingBoxOffset) ){  bb1.owner.posX = centroidPos + mobility1*incrementPos;}
			else{ bb1.owner.posX = centroidPos + mobility1*incrementPos - ((BoundingBoxOffset)bb1).offsetX; }	
			
			if( !(bb2 instanceof BoundingBoxOffset) ){ bb2.owner.posX = centroidPos - mobility2*incrementPos; }
			else{ bb2.owner.posX = centroidPos - mobility2*incrementPos - ((BoundingBoxOffset)bb2).offsetX; }
			
			
					
			float centroidVel =  inverseM*(mobility2*bb1.owner.velX + mobility1*bb2.owner.velX);
			float incrementVel = inverseM*elasticity*(bb2.owner.velX-bb1.owner.velX);
			
			bb1.owner.velX = centroidVel + mobility1*incrementVel;			
			bb2.owner.velX = centroidVel - mobility2*incrementVel;		
		
		}
		else{
			
			float centroidPos =  inverseM*(mobility2*y1 + mobility1*y2);
			float incrementPos = inverseM*Math.signum(diffY)*(bb1.height+bb2.height+10e-4f);
			
			if( !(bb1 instanceof BoundingBoxOffset) ){  bb1.owner.posY = centroidPos + mobility1*incrementPos;}
			else{ bb1.owner.posY = centroidPos + mobility1*incrementPos - ((BoundingBoxOffset)bb1).offsetY; }
			
			if( !(bb2 instanceof BoundingBoxOffset) ){ bb2.owner.posY = centroidPos - mobility2*incrementPos; }
			else{ bb2.owner.posY = centroidPos - mobility2*incrementPos - ((BoundingBoxOffset)bb2).offsetY; }
			
			
			
			float centroidVel =  inverseM*(mobility2*bb1.owner.velY + mobility1*bb2.owner.velY);
			float incrementVel = inverseM*elasticity*(bb2.owner.velY-bb1.owner.velY);
			
			bb1.owner.velY = centroidVel + mobility1*incrementVel;			
			bb2.owner.velY = centroidVel - mobility2*incrementVel;
			

		}	
	
	}

*/
	public static void nextAction(BoundingBox bb1, BoundingBox bb2, float elasticity, float mobility1, float mobility2)
	{
		float x1 = bb1.getPosX();
		float x2 = bb2.getPosX();
		float y1 = bb1.getPosY();
		float y2 = bb2.getPosY();
		
		
		float diffX = x1 - x2;
		float diffY = y1 - y2;
		
		float inverseM = 1f/(mobility1+mobility2);

		if( Math.abs(diffX)/(bb1.width+bb2.width) > Math.abs(diffY)/(bb1.height+bb2.height) ){

			float centroidPos =  inverseM*(mobility2*x1 + mobility1*x2);
			float incrementPos = inverseM*Math.signum(diffX)*(bb1.width+bb2.width+10e-4f);
			
			bb1.owner.posX = centroidPos + mobility1*incrementPos;	
			bb2.owner.posX = centroidPos - mobility2*incrementPos; 
		
					
			float centroidVel =  inverseM*(mobility2*bb1.owner.velX + mobility1*bb2.owner.velX);
			float incrementVel = inverseM*elasticity*(bb2.owner.velX-bb1.owner.velX);
			
			bb1.owner.velX = centroidVel + mobility1*incrementVel;			
			bb2.owner.velX = centroidVel - mobility2*incrementVel;		
		
		}
		else{
			
			float centroidPos =  inverseM*(mobility2*y1 + mobility1*y2);
			float incrementPos = inverseM*Math.signum(diffY)*(bb1.height+bb2.height+10e-4f);
			
			bb1.owner.posY = centroidPos + mobility1*incrementPos;
			bb2.owner.posY = centroidPos - mobility2*incrementPos; 
			
			float centroidVel =  inverseM*(mobility2*bb1.owner.velY + mobility1*bb2.owner.velY);
			float incrementVel = inverseM*elasticity*(bb2.owner.velY-bb1.owner.velY);
			
			bb1.owner.velY = centroidVel + mobility1*incrementVel;			
			bb2.owner.velY = centroidVel - mobility2*incrementVel;
			

		}	
	
	}
	

	
	
}
