package com.FlappySanta;



import com.Utils.AsyncSoundPool;
import com.Utils.SoundTimer;
import com.Utils.TimedCounter;
import com.Utils.Timer;
import com.basic2DGL.Sprite;
import com.basic2DObj.SimpleObj;

public class GameObj extends SimpleObj{

	
	
	public float health;
	
	public SoundTimer soundTimer = new SoundTimer();

	
	
	
	
	//----------------------------------------------------------------------------
	public GameObj() 
	{
		super();
	}
	//----------------------------------------------------------------------------
	public GameObj(float x, float y, float vx, float vy, float w, float h, float health, int id, boolean active) 
	{
		super(x,y,vx,vy,w,h,id,active);
		
		this.health = health;
		
	}
	//----------------------------------------------------------------------------
	public void setHealth(float h)
	{
		health = h;
		
	}
	//----------------------------------------------------------------------------	
	public void incrememntHealth(float i)
	{
		health += i;
	}
	//----------------------------------------------------------------------------
	public float getHealth()
	{
		return health;
	}
	//----------------------------------------------------------------------------
	public void playSound(AsyncSoundPool soundPool, int soundID, float vL, float vR, int loop, float rate, float interruptTime)
	{
		soundTimer.playSound(soundPool,soundID,vL,vR,loop,rate,interruptTime);
	}
	//----------------------------------------------------------------------------
}
