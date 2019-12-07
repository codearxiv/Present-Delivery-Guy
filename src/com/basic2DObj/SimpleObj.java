package com.basic2DObj;

import javax.microedition.khronos.opengles.GL11;

import com.basic2DGL.Sprite;
import com.basic2DObj.EmptyObj;
import com.basic2DObj.SpriteData;
import com.basic2DPhys.BoundingBox;

public class SimpleObj extends DrawableObj{

	public BoundingBox bounds;
	
	
	//----------------------------------------------------------------------------
	public SimpleObj() 
	{
		super();
		
		//bounds = new BoundingBox(0.0f,0.0f,1.0f,1.0f,this);
		bounds = new BoundingBox(1.0f,1.0f,this);
		
		
		textureIndex = 0;
	}
	//----------------------------------------------------------------------------
	public SimpleObj(float x, float y, float vx, float vy, float w, float h, int id, boolean active) 
	{
		super(x,y,vx,vy,active);
		
		//bounds = new BoundingBox(0.0f,0.0f,w,h,this);
		bounds = new BoundingBox(w,h,this);
		
		this.id = id;

	}	
	
	//----------------------------------------------------------------------------
	public void setDim(float w, float h) 
	{
		bounds.width = w;
		bounds.height = h;	
	}

	//----------------------------------------------------------------------------
	
	
	
}
