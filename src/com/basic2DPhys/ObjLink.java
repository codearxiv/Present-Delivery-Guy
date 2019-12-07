package com.basic2DPhys;


import com.basic2DObj.EmptyObj;

public class ObjLink {
	
	EmptyObj obj1, obj2;

	public float length;
	public Float compressibility;
	public Float stretchibility;

	public Float compressionLimit;
	public Float stretchLimit;

	public float elasticity;
	
	//----------------------------------------------------------------------------	
	public ObjLink(float l, Float c, Float s, Float cl, Float sl, float e, EmptyObj o1, EmptyObj o2)
	{
		length = l;
		compressibility = c;
		stretchibility = s;
		
		elasticity = e;
	
		compressionLimit = cl;
		stretchLimit = sl;
		
		obj1 = o1;
		obj2 = o2;
		
	}
	//----------------------------------------------------------------------------	
	public ObjLink(Float c, Float s, Float clFactor, Float slFactor, float e, EmptyObj o1, EmptyObj o2)
	{
		length = (float)dist(o1,o2);
		compressibility = c;
		stretchibility = s;
		
		elasticity = e;
	
		if(clFactor!=null){ compressionLimit = clFactor*length; }
		else{ compressionLimit = null; }
		
		if(slFactor!=null){ stretchLimit = slFactor*length; }
		else{ stretchLimit = null; }
		
		obj1 = o1;
		obj2 = o2;
		
	}
	
	//----------------------------------------------------------------------------	
	public void setObj(EmptyObj o1, EmptyObj o2)
	{
		
		obj1 = o1;
		obj2 = o2;
	}
	//----------------------------------------------------------------------------	
	public void stretchToObjDist()
	{
		length = (float)dist(obj1,obj2);
	}
	//----------------------------------------------------------------------------
	public double dist(EmptyObj o1, EmptyObj o2)
	{
		return Math.sqrt((o1.posX-o2.posX)*(o1.posX-o2.posX) + (o1.posY-o2.posY)*(o1.posY-o2.posY));
		
	}
	//----------------------------------------------------------------------------
	public void nextAction(float elapsedTime, float mobility1, float mobility2)
	{
		float diffX = obj1.posX-obj2.posX;
		float diffY = obj1.posY-obj2.posY;
		
		float dist = (float)Math.sqrt(diffX*diffX + diffY*diffY);
		
		boolean compressionLimitReached = false;
		boolean stretchLimitReached = false;	
		
		if( dist > 1e-7 ){
				
			if( dist<length ){
					
				if( compressibility!=null ){
				
					float incl = elapsedTime*(length-dist)/(compressibility*dist*dist);
						
					obj1.velX += mobility1*incl*diffX;
					obj1.velY += mobility1*incl*diffY;
								
					obj2.velX -= mobility2*incl*diffX;
					obj2.velY -= mobility2*incl*diffY;

				}
				
				if( compressionLimit!=null && (length-dist > compressionLimit) ){ compressionLimitReached = true; }
				
			}
			else if( dist>length ){
				
				if( stretchibility!=null ){
					
					float decl = elapsedTime*(dist-length)/(stretchibility*length*dist);
	
					obj1.velX -= mobility1*decl*diffX;
					obj1.velY -= mobility1*decl*diffY;
				
					obj2.velX += mobility2*decl*diffX;
					obj2.velY += mobility2*decl*diffY;

				}
				
				if( stretchLimit!=null && (dist-length > stretchLimit) ){ stretchLimitReached = true; }
					
				
			}
								
			
		}	
		
		
		if( compressionLimitReached || stretchLimitReached ){
			
			float inverseM = 1f/(mobility1+mobility2);
								
			float centroidPosX = inverseM*(mobility2*obj1.posX + mobility1*obj2.posX);					
			float centroidPosY = inverseM*(mobility2*obj1.posY + mobility1*obj2.posY);

			float c;

			if( compressionLimitReached ){ c = inverseM*(length-compressionLimit)/dist; }
			else{ c = inverseM*(length+stretchLimit)/dist; }
		
			float incrementPosX = c*diffX; 
			float incrementPosY = c*diffY;
	
			obj1.posX = centroidPosX + mobility1*incrementPosX;
			obj2.posX = centroidPosX - mobility2*incrementPosX;
					
			obj1.posY = centroidPosY + mobility1*incrementPosY;
			obj2.posY = centroidPosY - mobility2*incrementPosY;
			
			
			float invDistSq = 1f/(dist*dist);
			float projCoeffient1 = invDistSq*(obj1.velX*diffX + obj1.velY*diffY);
			float projCoeffient2 = invDistSq*(obj2.velX*diffX + obj2.velY*diffY);
			float projX1 = projCoeffient1*diffX; 
			float projX2 = projCoeffient2*diffX; 
			float projY1 = projCoeffient1*diffY; 
			float projY2 = projCoeffient2*diffY; 
			
			float centroidVelX =  inverseM*(mobility2*projX1 + mobility1*projX2);
			float centroidVelY =  inverseM*(mobility2*projY1 + mobility1*projY2);
			float incrementVelX = inverseM*elasticity*(projX2-projX1);
			float incrementVelY = inverseM*elasticity*(projY2-projY1);
			
			obj1.velX = obj1.velX - projX1 + (centroidVelX + mobility1*incrementVelX);			
			obj2.velX = obj2.velX - projX2 + (centroidVelX - mobility2*incrementVelX);
			
			obj1.velY = obj1.velY - projY1 + (centroidVelY + mobility1*incrementVelY);
			obj2.velY = obj2.velY - projY2 + (centroidVelY - mobility2*incrementVelY);
			
			
		}
	
		
	}
	//----------------------------------------------------------------------------
	public void nextActionY(float elapsedTime, float mobility1, float mobility2)
	{
		float diffY = obj1.posY-obj2.posY;
		
		float distY = Math.abs(diffY);
		
		if( distY > 1e-7 ){
				
			if( distY<length ){
				
				if( compressibility!=null ){
					float incl = elapsedTime*(length-distY)/(compressibility*diffY);
					
					obj1.velY += mobility1*incl;			
					obj2.velY -= mobility2*incl;
				}
				
				if( compressionLimit!=null && (length-distY > compressionLimit) ){
					
					float inverseM = 1f/(mobility1+mobility2);
					float centroidPos = inverseM*(mobility2*obj1.posY + mobility1*obj2.posY);
					float incrementPos = inverseM*Math.signum(diffY)*(length-compressionLimit);
										
					obj1.posY = centroidPos + mobility1*incrementPos;
					obj2.posY = centroidPos - mobility2*incrementPos;
					
					
					float centroidVel =  inverseM*(mobility2*obj1.velY + mobility1*obj2.velY);
					float incrementVel = inverseM*elasticity*(obj2.velY-obj1.velY);
					
					obj1.velY = centroidVel + mobility1*incrementVel;			
					obj2.velY = centroidVel - mobility2*incrementVel;
					
				}
				
			}
			else if( distY>length ){
				
				if( stretchibility!=null ){
					float decl = elapsedTime*(distY-length)*Math.signum(diffY)/(stretchibility*length);
				
					obj1.velY -= mobility1*decl;					
					obj2.velY += mobility2*decl;		
				}
				
				if( stretchLimit!=null && (distY-length > stretchLimit) ){
					
					float inverseM = 1f/(mobility1+mobility2);
					float centroidPos = inverseM*(mobility2*obj1.posY + mobility1*obj2.posY);
					float incrementPos = inverseM*Math.signum(diffY)*(length+stretchLimit);
											
					obj1.posY = centroidPos + mobility1*incrementPos;
					obj2.posY = centroidPos - mobility2*incrementPos;
					
					
					float centroidVel =  inverseM*(mobility2*obj1.velY + mobility1*obj2.velY);
					float incrementVel = inverseM*elasticity*(obj2.velY-obj1.velY);
					
					obj1.velY = centroidVel + mobility1*incrementVel;			
					obj2.velY = centroidVel - mobility2*incrementVel;
					
					
				}
		
			}
		
		}
	}
	//----------------------------------------------------------------------------
	public void nextActionX(float elapsedTime, float mobility1, float mobility2)
	{
		float diffX = obj1.posX-obj2.posX;
		
		float distX = Math.abs(diffX);
		
		if( distX > 1e-7 ){
				
			if( distX<length ){
				
				if( compressibility!=null ){
					float incl = elapsedTime*(length-distX)/(compressibility*diffX);
					
					obj1.velX += mobility1*incl;			
					obj2.velX -= mobility2*incl;
				}
				
				if( compressionLimit!=null && (length-distX > compressionLimit) ){
					
					float inverseM = 1f/(mobility1+mobility2);
					float centroidPos = inverseM*(mobility2*obj1.posX + mobility1*obj2.posX);
					float incrementPos = inverseM*Math.signum(diffX)*(length-compressionLimit);
										
					obj1.posX = centroidPos + mobility1*incrementPos;
					obj2.posX = centroidPos - mobility2*incrementPos;
					
					
					float centroidVel =  inverseM*(mobility2*obj1.velX + mobility1*obj2.velX);
					float incrementVel = inverseM*elasticity*(obj2.velX-obj1.velX);
					
					obj1.velX = centroidVel + mobility1*incrementVel;			
					obj2.velX = centroidVel - mobility2*incrementVel;
					
				}
				
			}
			else if( distX>length ){
				
				if( stretchibility!=null ){
					float decl = elapsedTime*(distX-length)*Math.signum(diffX)/(stretchibility*length);
				
					obj1.velX -= mobility1*decl;					
					obj2.velX += mobility2*decl;		
				}
				
				if( stretchLimit!=null && (distX-length > stretchLimit) ){
					
					float inverseM = 1f/(mobility1+mobility2);
					float centroidPos = inverseM*(mobility2*obj1.posX + mobility1*obj2.posX);
					float incrementPos = inverseM*Math.signum(diffX)*(length+stretchLimit);
											
					obj1.posX = centroidPos + mobility1*incrementPos;
					obj2.posX = centroidPos - mobility2*incrementPos;
					
					
					float centroidVel =  inverseM*(mobility2*obj1.velX + mobility1*obj2.velX);
					float incrementVel = inverseM*elasticity*(obj2.velX-obj1.velX);
					
					obj1.velX = centroidVel + mobility1*incrementVel;			
					obj2.velX = centroidVel - mobility2*incrementVel;
					
					
				}
		
			}
		
		}
	}
	//----------------------------------------------------------------------------

}

