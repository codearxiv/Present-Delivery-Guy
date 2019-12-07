package com.Utils;



public class SoundTimer extends Timer{

	
	
	float playingInterruptTime = 0f;
	
	//----------------------------------------------------------------------------
	
	public SoundTimer(){}
	
	//----------------------------------------------------------------------------
	public void playSound(AsyncSoundPool soundPool, int soundID, float vL, float vR, int loop, float rate, float interruptTime)
	{
			
		if( elapsedTimeSec()>playingInterruptTime ){
			playingInterruptTime = interruptTime;
			start();
			
			soundPool.playSound(soundID,vL,vR,1,loop,rate);
			
		}
		
				
	}
	//----------------------------------------------------------------------------
}
