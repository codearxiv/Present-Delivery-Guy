package com.FlappySanta;

import com.basic2DGL.Sprite;
import com.basic2DObj.SimpleObj;
import com.basic2DPhys.BoundingBox;

public class ReindeerObj extends GameObj{


	
	float angle; 
	boolean pullUp; 
	
	//----------------------------------------------------------------------------
	public ReindeerObj(float x, float y, float vx, float vy, float w, float h, float health, int id, boolean active) 
	{
		super(x,y,vx,vy,w,h,health,id,active);
				
		angle = 0f; 
		pullUp = false; 

		
	}
	
	//----------------------------------------------------------------------------
	
		
}
