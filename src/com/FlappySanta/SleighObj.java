package com.FlappySanta;

import com.basic2DGL.Sprite;

public class SleighObj extends GameObj{


	
	float angle; 
	boolean pullUp; 
	
	//----------------------------------------------------------------------------
	public SleighObj(float x, float y, float vx, float vy, float w, float h, float health, int id, boolean active) 
	{
		super(x,y,vx,vy,w,h,health,id,active);
		
	
		angle = 0f; 
		pullUp = false; 

		
	}
	
	//----------------------------------------------------------------------------
	
		
}
	

