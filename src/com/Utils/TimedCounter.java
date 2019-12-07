package com.Utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimedCounter {
	


 	int firstValue;
 	int startValue;
	int lastValue;
	
	int numTicks;
	int tickCount = -1;
	long nanoSecPerTick;
	
	Timer timer = new Timer();

	
	
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
		this.startValue = startValue;
		this.lastValue = lastValue;
		this.numTicks = numTicks;
		
		
		
		nanoSecPerTick = Math.round(1e9/ticksPerSec);
		
		
		timer.start();
		//if( headStartSec == 0f ){ startTime = System.nanoTime(); }
		//else{ startTime = System.nanoTime() - (long)(1e9*headStartSec); }
		

			
		tickCount = 0;		
		

	}
	//----------------------------------------------------------------------------
	
	public void setTicksPerSec(float ticksPerSec)
	{
		nanoSecPerTick = Math.round(1e9/ticksPerSec);	
	}
	
	//----------------------------------------------------------------------------
	public int nextValueTS()
	{
		counterActionLock.lock();
		
		int nextValue = nextValue();
		
		counterActionLock.unlock();
		
		
		return nextValue; 
	}
	//----------------------------------------------------------------------------
	public int nextValue()
	{
		
		tickCount = (int) (timer.elapsedTime()/nanoSecPerTick);

		return startValue + tickCount;

	}
	//----------------------------------------------------------------------------	
	
	public int nextValueCyclicTS()
	{
		counterActionLock.lock();
		
		int nextValue = nextValueCyclic(); 
		
		counterActionLock.unlock();
		
		
		return nextValue;
	
		
	}
	//----------------------------------------------------------------------------	
	
	public int nextValueCyclic()
	{
		

		tickCount = (int) (timer.elapsedTime()/nanoSecPerTick);

		int interval = lastValue-firstValue+1;
		int quotient = tickCount/interval;	
		int newValue = startValue + tickCount - interval*( quotient );
		
		
		if( newValue <= lastValue ){ return newValue; }
		else{ return newValue - interval; }
		
		
	}
	//----------------------------------------------------------------------------	
	
	public int nextValueAlternatingTS()
	{
		counterActionLock.lock();
		
		int nextValue = nextValueAlternating(); 
		
		counterActionLock.unlock();
		
		
		return nextValue;
		
	}
	//----------------------------------------------------------------------------	
	
	public int nextValueAlternating()
	{
		

		tickCount = (int) (timer.elapsedTime()/nanoSecPerTick);


		int interval = lastValue-firstValue;
		int quotient = tickCount/interval;
		int remainder = tickCount - interval*( quotient );		
		int newValue = startValue + remainder;

		
		
		if((quotient & 1) == 0){
			if( newValue <= lastValue ){ return newValue; }
			else{ return 2*lastValue - newValue; }
		}
		else{			
			if( newValue <= lastValue ){ return lastValue-newValue+firstValue;}
			else{ return newValue - interval; }
		}		
		
		
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
