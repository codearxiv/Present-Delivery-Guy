package com.Utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccumulationCounter {




 	int firstValue;
	int lastValue;
	int currentValue;
	boolean increasing = true;
	
	
	int numTicks;
	int tickCount = -1;
	float secPerTick;
	
	
	float accumulator = 0;

	
	
	public final Lock counterActionLock = new ReentrantLock();
 
	

	
	//----------------------------------------------------------------------------
	public void startTS(int firstValue, int startValue, int lastValue, int numTicks, float ticksPerSec)
	{

		counterActionLock.lock();
		
		start(firstValue,startValue,lastValue,numTicks,ticksPerSec);
		
		counterActionLock.unlock();
	}
	//----------------------------------------------------------------------------
	public void start(int firstValue, int startValue, int lastValue, int numTicks, float ticksPerSec)
	{
		
		this.firstValue = firstValue;
		this.lastValue = lastValue;
		this.numTicks = numTicks;
		
		currentValue = startValue;
		
		secPerTick = 1f/ticksPerSec;	

		accumulator = 0;	
		tickCount = 0;	
		
		increasing = true;
		

	}
	//----------------------------------------------------------------------------
	
	public void setTicksPerSec(float ticksPerSec)
	{
		secPerTick = 1f/ticksPerSec;	
	}
	
	//----------------------------------------------------------------------------
	public int nextValueTS(float elapsedTime)
	{
		counterActionLock.lock();
		
		int nextValue = nextValue(elapsedTime);
		
		counterActionLock.unlock();
		
		
		return nextValue; 
	}
	//----------------------------------------------------------------------------
	public int nextValue(float elapsedTime)
	{
		
		accumulator += elapsedTime;
		
		if( accumulator>=secPerTick ){ 
			accumulator -= secPerTick;
			tickCount++;
			currentValue++;
		}
		
		return currentValue;

	}
	//----------------------------------------------------------------------------	
	
	public int nextValueCyclicTS(float elapsedTime)
	{
		counterActionLock.lock();
		
		int nextValue = nextValueCyclic(elapsedTime); 
		
		counterActionLock.unlock();
		
		
		return nextValue;
	
		
	}
	//----------------------------------------------------------------------------	
	
	public int nextValueCyclic(float elapsedTime)
	{
		accumulator += elapsedTime;
		
		if( accumulator>=secPerTick ){ 
			accumulator -= secPerTick; 
			tickCount++;
			currentValue++;
			if( currentValue>lastValue ){ currentValue = firstValue; }
		}

		
		return currentValue;
		
	}
	//----------------------------------------------------------------------------	
	
	public int nextValueAlternatingTS(float elapsedTime)
	{
		counterActionLock.lock();
		
		int nextValue = nextValueAlternating(elapsedTime); 
		
		counterActionLock.unlock();
		
		
		return nextValue;
		
	}
	//----------------------------------------------------------------------------	
	
	public int nextValueAlternating(float elapsedTime)
	{
		
		accumulator += elapsedTime;
		
		if( accumulator>=secPerTick ){ 
			accumulator -= secPerTick; 
			tickCount++;
			
			if(increasing){ 
				if( currentValue<lastValue ){ currentValue++; } 
				else{ increasing = false; currentValue--; }
			}
			else{ 
				if( currentValue>firstValue ){ currentValue--; } 
				else{ increasing = true; currentValue++; }
			}			
			
		}
		
		return currentValue;
				
	}
	//----------------------------------------------------------------------------
	
	public void stopTS()
	{
		counterActionLock.lock();
		
		tickCount = -1;
		
		counterActionLock.unlock();
	}
	
	//----------------------------------------------------------------------------
	
	public void stop()
	{
		tickCount = -1;		
	}
	//----------------------------------------------------------------------------
	public boolean finishedTS()
	{
		counterActionLock.lock();				
		
		boolean finished = finished();
				
		counterActionLock.unlock();	

		
		return finished;  
	
		
	}
	//----------------------------------------------------------------------------
	public boolean finished()
	{
		
		boolean finished;
		
		if( tickCount == -1 ){ return true; }
		else if( numTicks == -1 ){ return false; }
		else{ return tickCount>=numTicks; }
		
		
		
	}
	//----------------------------------------------------------------------------

	
}


