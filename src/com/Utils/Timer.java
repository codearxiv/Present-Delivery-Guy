package com.Utils;

public class Timer {

	long startTime = 0;

	public static long TIME_CAP = Long.MAX_VALUE;
	
	public Timer()
	{}
	
	
	public void start()
	{
		startTime = System.nanoTime();

	}
	
	public void restart()
	{
		startTime = System.nanoTime();

	}
	
	public long elapsedTime()
	{
		return System.nanoTime()-startTime; 
		
	}

	
	public float elapsedTimeSec()
	{
		return (float)1e-9f*elapsedTime();
	}

	
	
	
	
	
	public long elapsedTimeCapped()
	{
		if( System.nanoTime()-startTime <= TIME_CAP ){ return System.nanoTime()-startTime; }
		else{ return TIME_CAP; } 
				
	}

	public float elapsedTimeSecCapped()
	{
		return (float)1e-9f*elapsedTimeCapped();
	}
	
}
