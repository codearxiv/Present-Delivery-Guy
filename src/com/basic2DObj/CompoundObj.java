package com.basic2DObj;

import com.basic2DGL.Sprite;
import com.basic2DPhys.BoundingBox;
import com.basic2DPhys.BoundingBoxOffset;

public class CompoundObj extends DrawableObj{

	
	public BoundingBoxOffset[] bounds;

	//----------------------------------------------------------------------------
	public CompoundObj(float x, float y, float vx, float vy, int numBounds,  int id, boolean active) 
	{
		super(x,y,vx,vy,active);
		
		bounds = new BoundingBoxOffset[numBounds];
		
		for(int i=0;i<bounds.length;i++){ bounds[i] = new BoundingBoxOffset(this); }
		
		this.id = id;

	}		
	//----------------------------------------------------------------------------
	public void setDim(float w, float h, int i) 
	{
		bounds[i].width = w;
		bounds[i].height = h;
		
	}
	//----------------------------------------------------------------------------
	public void setOffset(float x, float y, int i) 
	{
		bounds[i].offsetX = x;
		bounds[i].offsetY = y;
		
	}
	//----------------------------------------------------------------------------
}
