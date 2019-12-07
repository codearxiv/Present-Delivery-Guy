package com.basic2DPhys;

import com.Utils.FSItr;
import com.basic2DObj.EmptyObj;

public class BoundingBox{
	

	public float height;
	public float width;	
	public EmptyObj owner;
	public FSItr<BoundingBox> location = null;

	//public boolean active = true;
	
	
	//----------------------------------------------------------------------------
	public BoundingBox()
	{
	}
	//----------------------------------------------------------------------------
	public BoundingBox(EmptyObj o)
	{
		owner = o;
	}
	//----------------------------------------------------------------------------
	public BoundingBox(float w, float h, EmptyObj o)
	{
		height = h;
		width = w;		
		owner = o;

	}
	//----------------------------------------------------------------------------
	/*
	public BoundingBox(float x, float y, float w, float h, EmptyObj o)
	{
		offsetX = x;
		offsetY = y;	
		height = h;
		width = w;		
		owner = o;

	}

	//----------------------------------------------------------------------------
	
	public void enable()
	{
		active = true;
	}
	//----------------------------------------------------------------------------
	public void disable()
	{
		active = false;
	}
	//----------------------------------------------------------------------------
	public static int collisionType(BoundingBox bb1,BoundingBox bb2)
	{
		float diffY = bb1.getPosY()-bb2.getPosY();
		float diffX = bb1.getPosX()-bb2.getPosX();
		
		float totalHeight = bb1.height+bb2.height;
		float totalWidth = bb1.width+bb2.width;
		
		
		if(diffY > 0.0f){
			
		
			
		}		
		
	}
*/
	//----------------------------------------------------------------------------
	public float getPosX(){
		return owner.posX;
	}
	//----------------------------------------------------------------------------
	public float getPosY(){
		return owner.posY;
	}
	//----------------------------------------------------------------------------

}
