package com.basic2DPhys;

public class CollisionDetector 
{
	
	
	public static boolean pointInBox(float x, float y, BoundingBox bb)
	{
		return (x < bb.owner.posX+bb.width) && (x > bb.owner.posX-bb.width) &&
			(y < bb.owner.posY+bb.height) && (y > bb.owner.posY-bb.height);
		
	}
	
	public static boolean boxIntersectBox(BoundingBox bb1, BoundingBox bb2)
	{
		return Math.abs(bb1.getPosY()-bb2.getPosY()) <= bb1.height+bb2.height && 
				Math.abs(bb1.getPosX()-bb2.getPosX()) <= bb1.width+bb2.width;
			
			
			
	}
	
	
	public static boolean pointInTriangle()
	{
		
		
	}

}
