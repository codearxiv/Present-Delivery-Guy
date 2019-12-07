package com.FlappySanta;



import com.Utils.AccumulationCounter;
import com.Utils.AsyncSoundPool;
import com.Utils.SoundTimer;
import com.Utils.TimedCounter;
import com.basic2DGL.Sprite;
import com.basic2DObj.DrawableObj;

public class ExplosionObj extends DrawableObj {


	public SoundTimer soundTimer = null;
	
	public AccumulationCounter animCounter = null;
	

	//----------------------------------------------------------------------------
	public ExplosionObj(float x, float y, float vx, float vy, int size, boolean active) 
	{
		super(x,y,vx,vy,active);
		
		id = size;

	}	
	//----------------------------------------------------------------------------
	public void playSound(AsyncSoundPool soundPool, int soundID, float vL, float vR, int loop, float rate, float interruptTime)
	{
		if(soundTimer!=null){ soundTimer.playSound(soundPool,soundID,vL,vR,loop,rate,interruptTime); }
	}
	//----------------------------------------------------------------------------
}
