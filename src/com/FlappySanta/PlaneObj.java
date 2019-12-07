package com.FlappySanta;

import com.Utils.AccumulationCounter;
import com.Utils.TimedCounter;
import com.basic2DGL.Sprite;

public class PlaneObj extends GameObj{


	
	float angle; 
	AccumulationCounter counter;
	
	//----------------------------------------------------------------------------
	public PlaneObj(float x, float y, float vx, float vy, float w, float h, float health, int id, boolean active) 
	{
		super(x,y,vx,vy,w,h,health,id,active);
		
		
		angle = 0f; 
	
		counter = new AccumulationCounter();

	}
	
	//----------------------------------------------------------------------------
	
}
