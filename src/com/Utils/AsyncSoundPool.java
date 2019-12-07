package com.Utils;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.basic2DPhys.BoundingBox;

import android.media.AudioManager;
import android.media.SoundPool;

public class AsyncSoundPool extends SoundPool
{

	private int[] streamID;
	private int currentIndex = 0;

	public final Lock updateLock = new ReentrantLock();
	 
	
	public AsyncSoundPool(int maxStreams, int streamType, int srcQuality) 
	{
		super(maxStreams,streamType,srcQuality);
		
		streamID = new int[maxStreams];
	
	}

	
	

	
	public void playSound(final int soundID, 
			final float leftVolume, final float rightVolume, final int priority, final int loop, final float rate)
	{
		
		Thread soundThread = new Thread(){

			@Override
			public void run()
			{
				int id = play(soundID,leftVolume,rightVolume,priority,loop,rate);
				update(id);	
			}
		};
		
		
		soundThread.start();
	}
	
	
	
	
	public void stopAll()
	{
		for(int i=0;i<streamID.length;i++){ stop(streamID[i]); }
		
	}
	
	
	
	
	private void update(int id)
	{
		updateLock.lock();
				
		streamID[currentIndex] = id; 
		
		currentIndex++;
		
		if( currentIndex == streamID.length ){ currentIndex=0; }
		
		updateLock.unlock();

		
	}
	
		
	
}








