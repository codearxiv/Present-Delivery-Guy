package com.basic2DPhys;

import com.basic2DObj.EmptyObj;

public class PositionalLink {

	float x;
	float y;
	EmptyObj obj;

	public float length;
	public Float compressibility;
	public Float stretchibility;

	public Float compressionLimit;
	public Float stretchLimit;

	public float elasticity;
	
	//----------------------------------------------------------------------------	
	public PositionalLink(float x, float y, float l, Float c, Float s, Float cl, Float sl, float e, EmptyObj o1)
	{
		length = l;
		compressibility = c;
		stretchibility = s;
		
		elasticity = e;
	
		compressionLimit = cl;
		stretchLimit = sl;
		
		obj = o1;	
	}
	//----------------------------------------------------------------------------	
	public PositionalLink(float x, float y, Float c, Float s, Float clFactor, Float slFactor, float e, EmptyObj o1)
	{
		length = (float)dist(o1);
		compressibility = c;
		stretchibility = s;
		
		elasticity = e;
	
		if(clFactor!=null){ compressionLimit = clFactor*length; }
		else{ compressionLimit = null; }
		
		if(slFactor!=null){ stretchLimit = slFactor*length; }
		else{ stretchLimit = null; }
		
		obj = o1;
		
	}
	
	//----------------------------------------------------------------------------	
	public void setObj(EmptyObj o1)
	{		
		obj = o1;
	}
	//----------------------------------------------------------------------------	
	public void stretchToObjDist()
	{
		length = (float)dist(obj);
	}
	//----------------------------------------------------------------------------
	public double dist(EmptyObj o1)
	{
		return Math.sqrt((o1.posX-x)*(o1.posX-x) + (o1.posY-y)*(o1.posY-y));
		
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, float mobility)
	{
		float diffX = obj.posX-x;
		float diffY = obj.posY-y;
		
		float dist = (float)Math.sqrt(diffX*diffX + diffY*diffY);
		
		boolean compressionLimitReached = false;
		boolean stretchLimitReached = false;	
		
		if( dist > 1e-7 ){
				
			if( dist<length ){
					
				if( compressibility!=null ){
				
					float incl = elapsedTime*(length-dist)/(compressibility*dist*dist);
						
					obj.velX += mobility*incl*diffX;
					obj.velY += mobility*incl*diffY;
								

				}
				
				if( compressionLimit!=null && (length-dist > compressionLimit) ){ compressionLimitReached = true; }
				
			}
			else if( dist>length ){
				
				if( stretchibility!=null ){
					
					float decl = elapsedTime*(dist-length)/(stretchibility*length*dist);
	
					obj.velX -= mobility*decl*diffX;
					obj.velY -= mobility*decl*diffY;
				
				}
				
				if( stretchLimit!=null && (dist-length > stretchLimit) ){ stretchLimitReached = true; }
					
				
			}
								
			
		}	
		
		
		if( compressionLimitReached || stretchLimitReached ){
			
			float inverseM = 1f/mobility;
								
			float centroidPosX = inverseM*mobility*x;					
			float centroidPosY = inverseM*mobility*y;

			float c;

			if( compressionLimitReached ){ c = inverseM*(length-compressionLimit)/dist; }
			else{ c = inverseM*(length+stretchLimit)/dist; }
		
			float incrementPosX = c*diffX; 
			float incrementPosY = c*diffY;
	
			obj.posX = centroidPosX + mobility*incrementPosX;				
			obj.posY = centroidPosY + mobility*incrementPosY;
			
			
			float invDistSq = 1f/(dist*dist);
			float projCoeffient1 = invDistSq*(obj.velX*diffX + obj.velY*diffY);
			float projCoeffient2 = invDistSq*(x*diffX + y*diffY);
			float projX1 = projCoeffient1*diffX; 
			float projX2 = projCoeffient2*diffX; 
			float projY1 = projCoeffient1*diffY; 
			float projY2 = projCoeffient2*diffY; 
			
			float centroidVelX =  inverseM*mobility*projX2;
			float centroidVelY =  inverseM*mobility*projY2;
			float incrementVelX = inverseM*elasticity*(projX2-projX1);
			float incrementVelY = inverseM*elasticity*(projY2-projY1);
			
			obj.velX = obj.velX - projX1 + (centroidVelX + mobility*incrementVelX);			
			obj.velY = obj.velY - projY1 + (centroidVelY + mobility*incrementVelY);			
			
		}
	
		
	}
	//----------------------------------------------------------------------------
	public void nextActionY(float elapsedTime, float mobility)
	{
		float diffY = obj.posY-y;
		
		float distY = Math.abs(diffY);
		
		if( distY > 1e-7 ){
				
			if( distY<length ){
				
				if( compressibility!=null ){
					float incl = elapsedTime*(length-distY)/(compressibility*diffY);
					
					obj.velY += mobility*incl;			
				}
				
				if( compressionLimit!=null && (length-distY > compressionLimit) ){
					
					float inverseM = 1f/mobility;
					float centroidPos = inverseM*mobility*y;
					float incrementPos = inverseM*Math.signum(diffY)*(length-compressionLimit);
										
					obj.posY = centroidPos + mobility*incrementPos;
						
					
					float centroidVel =  inverseM*mobility*y;
					float incrementVel = inverseM*elasticity*(y-obj.velY);
					
					obj.velY = centroidVel + mobility*incrementVel;			
					
				}
				
			}
			else if( distY>length ){
				
				if( stretchibility!=null ){
					float decl = elapsedTime*(distY-length)*Math.signum(diffY)/(stretchibility*length);
				
					obj.velY -= mobility*decl;					
	
				}
				
				if( stretchLimit!=null && (distY-length > stretchLimit) ){
					
					float inverseM = 1f/mobility;
					float centroidPos = inverseM*mobility*y;
					float incrementPos = inverseM*Math.signum(diffY)*(length+stretchLimit);
											
					obj.posY = centroidPos + mobility*incrementPos;
					
					
					
					float centroidVel =  inverseM*mobility*y;
					float incrementVel = inverseM*elasticity*(y-obj.velY);
					
					obj.velY = centroidVel + mobility*incrementVel;			
				
					
					
				}
		
			}
		
		}
	}
	//----------------------------------------------------------------------------
	public void nextActionX(float elapsedTime, float mobility)
	{
		float diffX = obj.posX-x;
		
		float distX = Math.abs(diffX);
		
		if( distX > 1e-7 ){
				
			if( distX<length ){
				
				if( compressibility!=null ){
					float incl = elapsedTime*(length-distX)/(compressibility*diffX);
					
					obj.velX += mobility*incl;			
				}
				
				if( compressionLimit!=null && (length-distX > compressionLimit) ){
					
					float inverseM = 1f/mobility;
					float centroidPos = inverseM*mobility*x;
					float incrementPos = inverseM*Math.signum(diffX)*(length-compressionLimit);
										
					obj.posX = centroidPos + mobility*incrementPos;
					
					
					float centroidVel =  inverseM*mobility*x;
					float incrementVel = inverseM*elasticity*(x-obj.velX);
					
					obj.velX = centroidVel + mobility*incrementVel;			
					
				}
				
			}
			else if( distX>length ){
				
				if( stretchibility!=null ){
					float decl = elapsedTime*(distX-length)*Math.signum(diffX)/(stretchibility*length);
				
					obj.velX -= mobility*decl;					
				}
				
				if( stretchLimit!=null && (distX-length > stretchLimit) ){
					
					float inverseM = 1f/mobility;
					float centroidPos = inverseM*mobility*x;
					float incrementPos = inverseM*Math.signum(diffX)*(length+stretchLimit);
											
					obj.posX = centroidPos + mobility*incrementPos;
									
					
					float centroidVel =  inverseM*mobility*x;
					float incrementVel = inverseM*elasticity*(x-obj.velX);
					
					obj.velX = centroidVel + mobility*incrementVel;			
				
				}
		
			}
		
		}
	}
	//----------------------------------------------------------------------------

	
	
	
}
