package com.FlappySanta;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.opengles.GL11;

import com.Utils.AccumulationCounter;
import com.Utils.AsyncSoundPool;
import com.Utils.FSItr;
import com.Utils.FSList;
import com.Utils.SoundTimer;
import com.Utils.TimedCounter;
import com.Utils.Timer;
import com.basic2DGL.Sprite;
import com.basic2DObj.EmptyObj;

public class FireObj extends EmptyObj{

	
	Sprite sprite;
	
	public EmptyObj owner;
	
	//Timer timer = new Timer();
	float totalElapsedTime = 0;
	
	public float burnDuration;	
	public float flameGenTime;
	public float flameAnimationFPS;
	
	
	float firstFlameTime = 0f;
	
	FSList<ExplosionObj> flames = new FSList<ExplosionObj>();
	
	public final Lock objLock = new ReentrantLock();

	
	public SoundTimer soundTimer = new SoundTimer();

	
	
	//----------------------------------------------------------------------------
	public FireObj(float x, float y, float vx, float vy, 
			float burnDuration, float flameDuration, float flameGenTime, EmptyObj owner, Sprite s, int size)
	{
		super(x,y,vx,vy,true);
		
		sprite = s;
		
		id = size;
		
		this.burnDuration = burnDuration;
		
		flameAnimationFPS = 17f/flameDuration;
		
		this.flameGenTime = flameGenTime;
	
		
		this.owner = owner;
	}
	//----------------------------------------------------------------------------
	/*
	public void setSprite(Sprite s) 
	{
		sprite = s;
		
		
		for(FSItr<Flame> itr = flames.firstItr(); !itr.atHeader(); itr.advance()){
			
			Flame flame = itr.get();
			if( flame.sprite == null ){ flame.setSprite(sprite); } 

		}
		

	}
	*/
	//---------------------------------------------------------------------------- 
	public void start()
	{
		firstFlameTime = 0f;
		//timer.start();
		totalElapsedTime = 0f;
	}	
	//---------------------------------------------------------------------------- 
	public void restart()
	{	
		flames.clear();
		start();
	}	
	//---------------------------------------------------------------------------- 
	public boolean finishedBurning()
	{
		//return timer.elapsedTimeSec()>burnDuration;
		return totalElapsedTime>burnDuration;
	}		
	//---------------------------------------------------------------------------- 
	public boolean finishedFlaming()
	{
		boolean flameOut = true;
		
		
		for(FSItr<ExplosionObj> itr = flames.firstItr(); !itr.atHeader(); itr.advance()){

			ExplosionObj flame = itr.get();				

			if( !flame.animCounter.finished() ){ flameOut = false; break; }
		
		}
		
		return flameOut;
	}		
	//---------------------------------------------------------------------------- 
	public void nextFlamePos(float elapsedTime)
	{

	
		for(FSItr<ExplosionObj> itr = flames.firstItr(); !itr.atHeader(); itr.advance()){
			
			ExplosionObj flame = itr.get();
			flame.nextPos(elapsedTime);
			
		}
		
		
	}	
	//----------------------------------------------------------------------------	
	public boolean nextTextureIndices(float elapsedTime)
	{


		totalElapsedTime += elapsedTime;//(float) timer.elapsedTimeSec();
		
		boolean flameOut = true;
		
		
		
		FSItr<ExplosionObj> itr = flames.firstItr();
		
		for(float t = firstFlameTime; t < totalElapsedTime; t+=flameGenTime){

			

			if( itr.atHeader() ){
				
				if( t<burnDuration ){
					flameOut = false;
					ExplosionObj flame;
					
					if(owner==null){ flame = new ExplosionObj(posX,posY,velX,velY,id,true); }
					else{ 
						flame = new ExplosionObj(owner.posX+posX,owner.posY+posY,owner.velX+velX,owner.velY+velY,id,true); 
					}
						
					flame.animCounter = new AccumulationCounter();
					flame.animCounter.start(0,0,16,16,flameAnimationFPS);
					flames.enqueue(flame);
					
				
				}
				else{
					break;
				}
			}
			else{
				ExplosionObj flame = itr.get();				
				
				if( !flame.animCounter.finished() ){
			
					flameOut = false;
					flame.setTextureIndex(flame.animCounter.nextValue(elapsedTime));
					itr.advance();
				}
				else{
					firstFlameTime += flameGenTime;
					itr.remove();
				}	
			}
			
		
		
		}
		
		return !flameOut || ( totalElapsedTime < burnDuration );

	}
	//----------------------------------------------------------------------------
	public void drawFire(GL11 gl, Sprite sprite)
	{
		
		
		for(FSItr<ExplosionObj> itr = flames.firstItr(); !itr.atHeader(); itr.advance()){
			
			ExplosionObj flame = itr.get();
			
			flame.drawClientSide((GL11)gl,sprite,true,true);

		}
		

	}
	//----------------------------------------------------------------------------
	public void playSound(AsyncSoundPool soundPool, int soundID, float vL, float vR, int loop, float rate, float interruptTime)
	{
		soundTimer.playSound(soundPool,soundID,vL,vR,loop,rate,interruptTime);
	}
	//----------------------------------------------------------------------------

}
