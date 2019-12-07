package com.basic2DPhys;

import com.basic2DObj.EmptyObj;

public class BoundingBoxOffset extends BoundingBox{

	
	public float offsetX;
	public float offsetY;	
	
	
	//----------------------------------------------------------------------------
	public BoundingBoxOffset(EmptyObj o)
	{
		owner = o;
	}
	//----------------------------------------------------------------------------	
	public BoundingBoxOffset(float x, float y, float w, float h, EmptyObj o)
	{
		offsetX = x;
		offsetY = y;	
		height = h;
		width = w;		
		owner = o;

	}
	//----------------------------------------------------------------------------
	@Override
	public float getPosX(){
		return owner.posX+offsetX;
	}
	//----------------------------------------------------------------------------
	@Override
	public float getPosY(){
		return owner.posY+offsetY;
	}
	//----------------------------------------------------------------------------
	
	
}
