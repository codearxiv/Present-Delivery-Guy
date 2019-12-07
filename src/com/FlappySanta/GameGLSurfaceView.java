package com.FlappySanta;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.WindowManager;

import com.Utils.AccumulationCounter;
import com.Utils.AsyncSoundPool;
import com.Utils.FSItr;
import com.Utils.FSList;
import com.Utils.FastRandom;
import com.Utils.SoundTimer;
import com.Utils.TimedCounter;
import com.Utils.Timer;
import com.basic2DGL.GLRenderer;
import com.basic2DGL.Line;
import com.basic2DGL.Rectangle;
import com.basic2DGL.Shape;
import com.basic2DGL.Sprite;
import com.basic2DGL.Triangle;
import com.basic2DObj.EmptyObj;
import com.basic2DObj.SimpleObj;
import com.basic2DObj.Text2D;
import com.basic2DPhys.BoundingBox;
import com.basic2DPhys.CollisionDetector;
import com.basic2DPhys.CollisionHandler;
import com.basic2DPhys.DirectionalForce;
import com.basic2DPhys.DirectionalFriction;
import com.basic2DPhys.Friction;
import com.basic2DPhys.ObjForce;
import com.basic2DPhys.ObjLink;
import com.basic2DPhys.PositionalForce;
import com.basic2DPhys.PositionalLink;
import com.basic2DPhys.SpacePartition;

//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
public class GameGLSurfaceView extends GLSurfaceView
{

	final ARGLRenderer renderer;
	Context context;
	
	SharedPreferences savedSettings;
	
	GameLoop gameLoop;
	
	
	MediaPlayer backgroundMusic = null;
	Integer currentMusicResID = null;
	float currentMusicVolume;
	int currentMusicPosition;
	boolean currentMusicLooping;
	
	
	AsyncSoundPool soundPool;	
	int smallExplosionSoundID;
	int explosionSoundID;
	int jingleBellsSoundID;
	int darkJingleBellsSoundID;
	int bellsSoundID;
	int fireSoundID;
	int whooshSoundID;
	int deepWhooshSoundID;
	int punchSoundID;
	int crashSoundID;
	int planeSoundID;
	int scoreSoundID;
	int screamSoundID;
	int itemSoundID;
	SoundTimer punchSoundTimer = new SoundTimer();
	
	
	FastRandom rand = new FastRandom();

	
	enum GameMode  { FIRST_GAME_BEGIN, GAME_BEGIN, GAME_IN_PROGRESS, GAME_OVER, GAME_END, GAME_WIN };
	GameMode gameMode;
	


	
	
	int score = 49;
	int highScore = 0;
	Timer scoreTimer = new Timer();
	Timer scoreScreenTimer = new Timer();
	boolean displayScore = false;
	

	
	long prevTouchEventTime = -1;	
	long prevPresentReleaseTime = -1;	
	
	float planeGenHeight = 0.35f;
	GameObj lastPlane = null;
	PlaneObj[] plane = new PlaneObj[3];
	
	
	float treeGenFrequency = 0.2f;
	SimpleObj lastTree = null;
	SimpleObj[] tree = new SimpleObj[10];
	
	
	
	AccumulationCounter reindeerAnimationCounter = new AccumulationCounter();
	AccumulationCounter reindeerPullUpCounter = new AccumulationCounter();
	ReindeerObj[] reindeer = new ReindeerObj[4]; 
	SleighObj sleigh;
	
	SimpleObj[] present = new SimpleObj[100];
	final int maxReleasePresents = 6;
	int currentReleasePresent = 0;
	int maxPresents = maxReleasePresents;
	int currentPresent = maxReleasePresents;
	Timer presentShowerTimer = new Timer();
	float presentShowerTime;
	boolean presentShower =  false;
	
	
	
		
	SimpleObj lastHouse = null;
	HouseObj[] onscreenHouse = new HouseObj[6];
	HouseObj[] house = new HouseObj[15];
	ChimneyObj[] chimney = new ChimneyObj[6];
	Text2D[] chimneyNumber = new Text2D[6];
	SimpleObj[] snowman = new SimpleObj[6];
	int numChimneys = 44;
	
	FSList<ExplosionObj> explosions = new FSList<ExplosionObj>();
	FSList<FireObj> fire = new FSList<FireObj>();
	
	SimpleObj ground;

	float groundTexturePosX = 0f;
	float nearHillsTexturePosX = 0f;
	float farHillsTexturePosX = 0f;

	
	
	SimpleObj button;

	
	
	Text2D text1, text2;
	Text2D smallText1, smallText2, smallText3;
	Text2D scoreText;
	Text2D smallScoreText;
	
	
	ObjLink[] reinsY = new ObjLink[4];
	ObjLink[] strutsX = new ObjLink[4];
	ObjLink[] reins = new ObjLink[4];

	DirectionalForce gravity;
	DirectionalForce airResistance;
	DirectionalForce airResistanceSleigh;
	PositionalLink[] textStrut = new PositionalLink[2];
	ObjLink strut;
	
	
	SpacePartition space;
	

	//----------------------------------------------------------------------------
	@SuppressWarnings("static-access")
	public GameGLSurfaceView(MainActivity mainActivity, Context context)
	{
		super(context);
		
		this.context = context;
		
		renderer = new ARGLRenderer();
		setRenderer(renderer);

		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		
		
		savedSettings = context.getSharedPreferences("saved_settings",context.MODE_PRIVATE);
		highScore = savedSettings.getInt("highScore",0);
		
		
		
		setBackgroundMusic(R.raw.jinglebells,0.45f,0,true);

		
		
		soundPool = new AsyncSoundPool(10,AudioManager.STREAM_MUSIC,0);		
		explosionSoundID = soundPool.load(context,R.raw.explosion,1);
		smallExplosionSoundID = soundPool.load(context,R.raw.smallexplosion,1);		
		fireSoundID = soundPool.load(context,R.raw.fire,1);	
		whooshSoundID = soundPool.load(context,R.raw.whoosh,1);
		deepWhooshSoundID = soundPool.load(context,R.raw.whoosh4,1);
		punchSoundID = soundPool.load(context,R.raw.punch,1);
		//crashSoundID = soundPool.load(context,R.raw.crash,1);
		planeSoundID = soundPool.load(context,R.raw.plane2,1);
		scoreSoundID = soundPool.load(context,R.raw.score2,1);
		screamSoundID = soundPool.load(context,R.raw.scream2,1);
		itemSoundID = soundPool.load(context,R.raw.item,1);
		
		
		
		
		float[] charWidthFactors = new float[68];
		for(int i=0;i<charWidthFactors.length;i++){ charWidthFactors[i] = 1f; }
		
		charWidthFactors[ Text2D.charToIndex('1') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('i') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('I') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('j') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('J') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('l') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('L') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('f') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('t') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('r') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex(',') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('.') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('!') ] = 0.7f;
		charWidthFactors[ Text2D.charToIndex('m') ] = 1.3f;
		charWidthFactors[ Text2D.charToIndex('M') ] = 1.3f;
		
		text1 = new Text2D(1.3685f,0.366f,0.1f,null,charWidthFactors);
		text2 = new Text2D(1.3685f,0.766f,0.1f,null,charWidthFactors);
		text2.setText("Get Ready");

				
		smallText1 = new Text2D(1.3685f,0.666f,0.05f,null,charWidthFactors);
		smallText1.setText("Get a present in all 50 chimneys.");
		smallText2 = new Text2D(1.3685f,0.866f,0.05f,null,charWidthFactors);
		smallText2.setText("TAP TO FLY");
		smallText3 = new Text2D(1.3685f,1.066f,0.05f,null,charWidthFactors);
		
		smallScoreText = new Text2D(0f,0f,0.05f,null,null);

		scoreText = new Text2D(1.3685f,0.766f,0.2f,null,charWidthFactors);	
		scoreText.initCharWidthArray();
		scoreText.setCharWidthFactor('1',0.5f);
		
		
		
		
		for(int i=0;i<plane.length;i++){			
			plane[i] = new PlaneObj(0f,0f,-2f,0f,0.16f,0.075f,2f,6,false);
			plane[i].counter = new AccumulationCounter();
			plane[i].angle = 0f;
		}

		for(int i=0;i<tree.length;i++){			
			tree[i] = new SimpleObj(0f,0f,-0.4f,0f,0.135f,0.15f,8,false);
		}
		
		for(int i=0;i<reindeer.length;i++){			
			reindeer[i] = new ReindeerObj(0.66f+i*0.11f,0.5f,0f,0f,0.07f,0.08f,1f,1,true);
			reindeer[i].angle = 0f;
			reindeer[i].pullUp = false;
		}
		
		
		sleigh = new SleighObj(0.5f,0.5f,0f,0f,0.1f,0.075f,1f,2,true);
		sleigh.angle = 0f;
		sleigh.pullUp = false;
		
		
		house[0] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[1] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[2] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[3] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[4] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[5] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[6] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[7] = new HouseObj(0f,0f,-1f,0f,0.2f,0.1f,3,false);
		house[8] = new HouseObj(0f,0f,-1f,0f,0.2f,0.2f,3,false);
		house[9] = new HouseObj(0f,0f,-1f,0f,0.2f,0.2f,3,false);
		house[10] = new HouseObj(0f,0f,-1f,0f,0.2f,0.2f,3,false);
		house[11] = new HouseObj(0f,0f,-1f,0f,0.2f,0.2f,3,false);
		house[12] = new HouseObj(0f,0f,-1f,0f,0.2f,0.2f,3,false);
		house[13] = new HouseObj(0f,0f,-1f,0f,0.2f,0.2f,3,false);
		house[14] = new HouseObj(0f,0f,-1f,0f,0.2f,0.2f,3,false);
	

		for(int i=0;i<onscreenHouse.length;i++){ 
			onscreenHouse[i] = house[i]; 
			house[i].setActive();
			house[i].posX = -1f;
			chimney[i] = new ChimneyObj(0f,0f,-1f,0f,0.045f,0.035f,1f,4,false);
			chimney[i].chimneyServiced = false;
			chimney[i].chimneyNumber = 0;
			snowman[i] = new SimpleObj(0f,0f,-1f,0f,0.03f,0.07f,7,false);	
			chimneyNumber[i] = new Text2D(0f,0f,0.05f,null,null);
			chimneyNumber[i].setVelocity(-1f,0f);
		}

	
		
		for(int i=0;i<maxReleasePresents;i++){
			present[i] = new SimpleObj(0f,0f,0f,0f,0.04f,0.04f,5,false);
		}
		for(int i=maxReleasePresents;i<present.length;i++){
			present[i] = new SimpleObj(0f,0f,0f,0f,0.04f,0.04f,10,false);
		}
		
		ground = new SimpleObj(1.3685f,1.493f,0f,0f,1.37f,0.05f,0,true);
		
		
		
		
		space = new SpacePartition(0,10.0f,1.5f,0.75f,0);
		space.insert(ground);	
		/*
		space.insert(reindeer[0]);
		space.insert(reindeer[1]);
		space.insert(reindeer[2]);
		space.insert(reindeer[3]);
		space.insert(sleigh);
		 */
			
		
		
		button = new SimpleObj(0.19f,1.32f,0f,0f,0.24f,0.24f,-1,true);
			
		
		
			
		strutsX[0] = new ObjLink(0.3f,0.3f,0.1f,0.5f,0.1f,sleigh,reindeer[0]);
		strutsX[1] = new ObjLink(0.3f,0.3f,0.1f,0.5f,0.1f,reindeer[0],reindeer[1]);
		strutsX[2] = new ObjLink(0.3f,0.3f,0.1f,0.5f,0.1f,reindeer[1],reindeer[2]);
		strutsX[3] = new ObjLink(0.3f,0.3f,0.1f,0.5f,0.1f,reindeer[2],reindeer[3]);

		reinsY[0] = new ObjLink(0.01f,null,null,null,0.01f,0.1f,sleigh,reindeer[0]);
		reinsY[1] = new ObjLink(0.0055f,null,null,null,0.0055f,0.1f,reindeer[0],reindeer[1]);
		reinsY[2] = new ObjLink(0.0055f,null,null,null,0.0055f,0.1f,reindeer[1],reindeer[2]);
		reinsY[3] = new ObjLink(0.0055f,null,null,null,0.0055f,0.1f,reindeer[2],reindeer[3]);

	
		reins[0] = new ObjLink(0.1f,null,0.3f,null,0.15f,0.1f,sleigh,reindeer[0]);
		reins[1] = new ObjLink(0.05f,null,0.3f,null,0.1f,0.1f,reindeer[0],reindeer[1]);
		reins[2] = new ObjLink(0.05f,null,0.3f,null,0.1f,0.1f,reindeer[1],reindeer[2]);
		reins[3] = new ObjLink(0.05f,null,0.3f,null,0.1f,0.1f,reindeer[2],reindeer[3]);
		
		
		textStrut[0] = new PositionalLink(1.3685f,0f,0.666f,0.03f,0.02f,0.01f,1.5f,0.05f,smallText1);
		textStrut[1] = new PositionalLink(1.3685f,0f,0.866f,0.03f,0.02f,0.01f,1.5f,0.05f,smallText2);
		
		
		gravity = new DirectionalForce(0f,1f,5.0f,null);		
		airResistance = new DirectionalForce(-1f,0f,3.5f,null);
		airResistanceSleigh = new DirectionalForce(-1f,0f,5f,null);
				
		
		
		
		gameMode = GameMode.FIRST_GAME_BEGIN;
		//gameMode = GameMode.GAME_IN_PROGRESS;

		rand.setSeed(System.nanoTime());
	}


	//----------------------------------------------------------------------------
	@Override
	public void onResume() 
	{

		super.onResume();
			
		if( currentMusicResID!=null ){
			setBackgroundMusic(currentMusicResID,currentMusicVolume,currentMusicPosition,currentMusicLooping);
		}		
		
		soundPool.autoResume();
		
		gameLoop = new GameLoop();
		gameLoop.setRunning(true); 
		gameLoop.start();
		

	}
	//----------------------------------------------------------------------------
	@Override
	public void onPause() {

		super.onPause();

		soundPool.autoPause();
		
		if(backgroundMusic!=null){ 
			currentMusicPosition = backgroundMusic.getCurrentPosition();
			backgroundMusic.release(); 
			backgroundMusic = null;
				
		}
		
		saveHighScore();
		
		stopGameLoop();


	}
	//----------------------------------------------------------------------------

	public void onDestroy() {

		if(backgroundMusic!=null){ 
			backgroundMusic.release(); 
			backgroundMusic = null;				
		}
		
		soundPool.release();
		soundPool = null;

	}
	//----------------------------------------------------------------------------
	public void stopGameLoop(){
		
		if(gameLoop!=null){ 	
			
			gameLoop.setRunning(false);

			boolean retry = true;
			  
			while (retry){
				try{
					gameLoop.join();
					retry = false;
				} 
				catch (InterruptedException e){} 
			} 
			
		}
		
	}
	
	//----------------------------------------------------------------------------
	public void saveHighScore()
	{
		if(score>highScore){
			highScore = score;
			SharedPreferences.Editor preferencesEditor = savedSettings.edit();
			preferencesEditor.putInt("highScore",highScore);
			preferencesEditor.apply();
		}
	}
	//----------------------------------------------------------------------------
	public void setBackgroundMusic(int resID, float volume, final int musicPosition, boolean loop)
	{
		
		if(backgroundMusic!=null){ backgroundMusic.release(); }

		currentMusicResID = resID;
		currentMusicVolume = volume;
		currentMusicLooping = loop;
		
		backgroundMusic = new MediaPlayer();
		try {
			backgroundMusic.setDataSource(context,Uri.parse("android.resource://com.FlappySanta/"+resID));
			backgroundMusic.setAudioStreamType(AudioManager.STREAM_MUSIC);
			backgroundMusic.setVolume(volume,volume);
			backgroundMusic.setLooping(loop);
		} catch (IllegalArgumentException e) {
		
			e.printStackTrace();
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		//backgroundMusic = MediaPlayer.create(context,resID);
		
		
		
		//backgroundMusic.
		backgroundMusic.setOnPreparedListener(
				
			new MediaPlayer.OnPreparedListener(){
				
				@Override
				public void onPrepared(MediaPlayer player){ 
					try{
						backgroundMusic.start();
						backgroundMusic.seekTo(musicPosition); 
					} catch (IllegalStateException e) {}
				}
			}
		);
				
		backgroundMusic.prepareAsync();
		
	}
	
	//----------------------------------------------------------------------------
	public void initGameBegin()
	{
		
		stopGameLoop();
		
		for(int i=0;i<plane.length;i++){			
			plane[i].setVelocity(-1.7f,0f);
			plane[i].setHealth(2f);
			plane[i].setInactive();
			plane[i].angle = 0f;
			space.remove(plane[i]);
		}
		lastPlane = null;
		planeGenHeight = 0.35f;
		
		for(int i=0;i<tree.length;i++){			
			tree[i].setInactive();
		}
		lastTree = null;
		treeGenFrequency = 0.15f;
		
		for(int i=0;i<reindeer.length;i++){			
			reindeer[i].setPos(0.66f+i*0.11f,0.5f);
			reindeer[i].setVelocity(0f,0f);
			reindeer[i].setHealth(1f);
			reindeer[i].setActive();
			reindeer[i].setTextureIndex(0);
			reindeer[i].angle = 0f;
			reindeer[i].pullUp = false;
		}
		
		sleigh.setPos(0.5f,0.5f);
		sleigh.setVelocity(0f,0f);
		sleigh.setHealth(1f);
		sleigh.setActive();		
		sleigh.setTextureIndex(0);
		sleigh.angle = 0f;
		sleigh.pullUp = false;
		
		for(int i=onscreenHouse.length;i<house.length;i++){
			house[i].setInactive();
			space.remove(house[i]);
		}	
		
		
		for(int i=0;i<onscreenHouse.length;i++){ 
			onscreenHouse[i] = house[i];
			house[i].setActive();
			house[i].posX = -1f;
			chimney[i].setInactive();
			chimney[i].chimneyServiced = false;
			chimney[i].chimneyNumber = 0;
			chimney[i].setHealth(1f);
			space.remove(chimney[i]);

		}
		numChimneys = 0;
		lastHouse = null;

			
			
		for(int i=0;i<maxPresents;i++){
			present[i].setInactive(); 
			space.remove(present[i]);
		}
		currentReleasePresent = 0;
		currentPresent = maxReleasePresents;
		maxPresents = maxReleasePresents;	
		presentShower =  false;

		
		
		explosions.clear();
		fire.clear();
		
		space = new SpacePartition(0,10.0f,1.5f,0.75f,0);
		space.insert(ground);	
		

		
		score = 0;
		
		
		

		soundPool.stopAll(); 
		
		setBackgroundMusic(R.raw.bells,0.25f,0,true);
		
		
		gameMode = GameMode.GAME_BEGIN;		
		

		gameLoop = new GameLoop();
		gameLoop.setRunning(true); 
		gameLoop.start();
		
		
		
	}



	//----------------------------------------------------------------------------	
	public void initGameOver()
	{
		
	
							
		smallText1.setPos(1.3685f,1.666f);
		smallText2.setPos(1.3685f,1.866f);
	
		
		smallText1.setText("Score: "+String.valueOf(score));				
		smallText2.setText("High Score: "+String.valueOf(highScore));
		
		
		text1.setSpacing(0f);
		text1.setText("GAME OVER");
		
		
		setBackgroundMusic(R.raw.thehalloweenchristmas,0.15f,0,true);
	
		
		saveHighScore();
		
		scoreScreenTimer.start();
		
		
		gameMode = GameMode.GAME_OVER;
	}
	
	//----------------------------------------------------------------------------	
	public void initGameEnd()
	{
		

							
		smallText1.setPos(1.3685f,1.666f);
		smallText2.setPos(1.3685f,1.866f);
	
		
		smallText1.setText("Score: "+String.valueOf(score));				
		smallText2.setText("High Score: "+String.valueOf(highScore));
		
		
		smallText3.setSpacing(0f);
		
		int scoreLevel = (int) Math.floor(0.1*score);			
		
		switch(scoreLevel){
			case 0:
				smallText3.setText(String.valueOf(50-score)+" Presents Undelivered, A Destitute Christmas!");		
				break;
			case 1:
				smallText3.setText(String.valueOf(50-score)+" Presents Undelivered, Most Were Left Giftless!");		
				break;
			case 2:
				smallText3.setText(String.valueOf(50-score)+" Presents Undelivered, Halfway There.");		
				break;
			case 3:
				smallText3.setText(String.valueOf(50-score)+" Presents Undelivered, Too Close To Give Up!");		
				break;
			case 4:
				smallText3.setText(String.valueOf(50-score)+" Presents Undelivered, An Almost Perfect Christmas.");		
				break;					
		}

		text1.setSpacing(0f);
		text1.setText("Delivery Run Complete");	

		
		treeGenFrequency = 0.5f;

		setBackgroundMusic(R.raw.jinglebells,0.45f,0,true);		
			
		saveHighScore();
		
		scoreScreenTimer.start();
		
		gameMode = GameMode.GAME_END;
	}
	//----------------------------------------------------------------------------	
	public void initGameWin()
	{
		

		
			
		smallText1.setPos(1.3685f,1.666f);
		smallText2.setPos(1.3685f,1.866f);
	
		
		smallText1.setText("Score: "+String.valueOf(score));				
		smallText2.setText("High Score: "+String.valueOf(highScore));
				
		smallText3.setSpacing(0f);	
		smallText3.setText("You Are One Of The Few Capable Of Saving Christmas!");		
	
		
		text1.setSpacing(0f);
		text1.setText("CONGRATULATIONS!");	

		
		
		maxPresents = present.length;
		currentReleasePresent = 0;		
		currentPresent = maxReleasePresents;
		presentShower =  true;
		presentShowerTime = Float.POSITIVE_INFINITY;

		
		treeGenFrequency = 0.75f;
		
		setBackgroundMusic(R.raw.jinglebells,0.45f,0,true);
		
		
		
		gameMode = GameMode.GAME_WIN;

		
	}
	//----------------------------------------------------------------------------
	public void releasePresent()
	{
		
		//for(int k=0;k<10;k++){
		if( currentReleasePresent >= maxReleasePresents ){ currentReleasePresent = 0; }
		
		present[currentReleasePresent].setActive();
		present[currentReleasePresent].setPos(sleigh.posX,sleigh.posY);
		present[currentReleasePresent].setVelocity(1.2f,sleigh.velY);
		present[currentReleasePresent].setTextureIndex(rand.nextInt(4));
		
		space.insert(present[currentReleasePresent]);
		
		currentReleasePresent++;	
		
		soundPool.playSound(whooshSoundID,0.8f,0.6f,1,0,1f);
		
 
		
		//}		
		
	}
	//----------------------------------------------------------------------------
	public void reindeerFly(float effort)
	{
		reindeerPullUpCounter.startTS(0,0,5,17,45);
		
		sleigh.pullUp = true;
		
		for(int i=0; i<reindeer.length; i++){
			if( reindeer[i].health == 1f && reindeer[i].posY>-0.055f ){ 
				reindeer[i].setVelocity(0f,-(effort+i*0.07f));
				reindeer[i].pullUp = true;
			}
		}

		/*
		if( (eventTime - prevTouchEventTime)<200 && prevTouchEventTime != -1 ){
			
			for(int i=0; i<reindeer.length; i++){			
				if( reindeer[i].health == 1f && reindeer[i].posY>0f ){ 
					reindeer[i].incrementVelocity(0f,-(0.1f+i*0.05f)); 	
				}
			}
		}
		*/
	}
	//----------------------------------------------------------------------------
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		switch(gameMode){
			case FIRST_GAME_BEGIN:			
			case GAME_BEGIN:
				switch(e.getAction()){
					case MotionEvent.ACTION_DOWN:
						
						reindeerAnimationCounter.stopTS();
					
						if(backgroundMusic!=null){ 
							backgroundMusic.release(); 
							backgroundMusic = null;
							currentMusicResID = null;
						}

						
						gameMode = GameMode.GAME_IN_PROGRESS;
						break;	
				}
				break;
			case GAME_WIN:
			case GAME_END:				
			case GAME_OVER:
				
				switch(e.getAction()){
					case MotionEvent.ACTION_DOWN:
			
						if( scoreScreenTimer.elapsedTimeSec()>0.5f ){						
							
							if( CollisionDetector.pointInBox(
								renderer.pixelXToPosX(e.getX()),renderer.pixelYToPosY(e.getY()),button.bounds) ){					
								
									initGameBegin();							
							}
						}

						
						break;
				
					}
				break;
			
			case GAME_IN_PROGRESS:
			
				switch(e.getAction()){
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_2_DOWN:
					
					long eventTime = e.getEventTime();
					boolean releaseButtonPressed = false;
					boolean emptyScreenPressed = false;  
											
					for(int j=0;j<e.getPointerCount();j++){
						
						
						if( !releaseButtonPressed && CollisionDetector.pointInBox(
								renderer.pixelXToPosX(e.getX(j)),renderer.pixelYToPosY(e.getY(j)),button.bounds) ){
							
							if( (eventTime - prevPresentReleaseTime)>275 || prevPresentReleaseTime==-1 ){
								
								releasePresent();							
								prevPresentReleaseTime = eventTime;
							}
							
							
							releaseButtonPressed = true;
							
						}
						else if(!emptyScreenPressed){
							
							reindeerFly(2f);
							
							
							soundPool.playSound(deepWhooshSoundID,1f,0.75f,1,0,1f);
							//prevTouchEventTime = eventTime;
							emptyScreenPressed = true;
						}
					}
					
					break;
			
			
				} 	
			
				break;
		
		}

			
		return true;
	}
	//----------------------------------------------------------------------------



	
	//----------------------------------------------------------------------------
	//----------------------------------------------------------------------------	
	//long timeSleeping = 0L;
	//Timer sleepTimer = new Timer();
	//Timer runTimer = new Timer();

	
	private class GameLoop extends Thread
	{
		Timer updateTimer = new Timer();
		
		

		boolean isRunning = true;

		
		FSList<BoundingBox> collisions = new FSList<BoundingBox>();
		

		//----------------------------------------------------------------------------
		public GameLoop()
		{			
			
		}

		//----------------------------------------------------------------------------
		public void setRunning(boolean running)
		{
			isRunning = running;
		}
		//----------------------------------------------------------------------------
		public void render()
		{
			requestRender();
		}
		//----------------------------------------------------------------------------
		
		@Override
		public void run()
		{
			Timer.TIME_CAP = 20000000L;
			//runTimer.start();
			//timeSleeping = 0L;
			
			long elapsedTime;
	
					
			updateTimer.start();		
			
			
			
			while(isRunning){
					
			
				
				elapsedTime = updateTimer.elapsedTime();			
				
			
				
				if( elapsedTime>4000000L ){				
					
					updateTimer.restart();	
		
					nextGameIteration(elapsedTime);
					render();
					
				}
				
				else if( elapsedTime<1000000L ){ 
					
					//sleepTimer.start();
					try { sleep(0,(999999-(int)elapsedTime)); }	
					catch (InterruptedException e) {}
					//timeSleeping+=sleepTimer.elapsedTime();
				} 
				else{
					
					yield();
				}
				
			

			}
			

		}
		
		//----------------------------------------------------------------------------
		public void nextGameIteration(long elapsedTime)
		{	
			
			
			float elapsedTimeSec = (float)(1e-9*elapsedTime);
			
			float invEnergyLoss = (float)Math.pow(0.02,elapsedTimeSec);
				
		
			switch(gameMode){
				case FIRST_GAME_BEGIN:
				case GAME_BEGIN:
					
					nextActionDeerRun(elapsedTimeSec);
					nextActionOther(elapsedTimeSec);
					
					break;
				case GAME_WIN:																						
				case GAME_END:	
					
					if( sleigh.posY>1.225f ){ reindeerFly(2f*rand.nextFloat()); }					
					if( smallText3.getSpacing()<0.05f ){ smallText3.incrementSpacing(elapsedTimeSec); }
					else if( smallText3.getSpacing()>0.05f ){ smallText3.setSpacing(0.05f); }
					
				case GAME_OVER:
					
					nextActionScoreScreen(elapsedTimeSec,invEnergyLoss);
								
				case GAME_IN_PROGRESS:
					nextActionOther(elapsedTimeSec);
					nextActionBuildings(elapsedTimeSec);						
					nextActionReindeer(elapsedTimeSec,invEnergyLoss);
					nextActionSleigh(elapsedTimeSec,invEnergyLoss);
					nextActionPresents(elapsedTimeSec);
					nextActionTrees(elapsedTimeSec);
					//nextActionPlanes(elapsedTimeSec);
					nextActionExplosions(elapsedTimeSec); 
					nextActionFire(elapsedTimeSec); 
					
					break;
					
			}
	
			

		}
		//----------------------------------------------------------------------------
		public void nextActionDeerRun(float elapsedTimeSec)
		{
			if( reindeerAnimationCounter.finishedTS() ){ reindeerAnimationCounter.startTS(0,0,5,-1,15); }
			else{ 
				int textureIndex = reindeerAnimationCounter.nextValueAlternatingTS(elapsedTimeSec);
				for(int i=0;i<reindeer.length;i++){ reindeer[i].setTextureIndex(textureIndex); }
			}
		}
		//----------------------------------------------------------------------------	
		public void nextRandomPresent(float elapsedTimeSec)
		{
			
			if( rand.nextFloat() < 16f*elapsedTimeSec ){
				
				
				present[currentPresent].setPos(2.7f*rand.nextFloat()+0.5f,-0.2f);
				present[currentPresent].setActive();
				present[currentPresent].setTextureIndex(rand.nextInt(4));
				
				space.insert(present[currentPresent]);
									
				
				
				
				currentPresent++;
				
				if( currentPresent==maxPresents ){ currentPresent = maxReleasePresents; }
							
			}
			
		}
		//----------------------------------------------------------------------------		
		public void nextActionScoreScreen(float elapsedTimeSec, float invEnergyLoss)
		{
			if(text1.getSpacing() < 0.1f){ text1.incrementSpacing(elapsedTimeSec); }
			else if(text1.getSpacing() > 0.1f){ text1.setSpacing(0.1f); }
			/*	
			if( smallText[0].posY > 0.666f ){ smallText[0].posY -= 7*elapsedTimeSec; }
			else{  smallText[0].posY = 0.666f; }
			
			if( smallText[1].posY > 0.866f ){ smallText[1].posY -= 7*elapsedTimeSec; }
			else{  smallText[1].posY = 0.866f; }
			 */
			
			textStrut[0].nextActionY(elapsedTimeSec,1f);
			textStrut[1].nextActionY(elapsedTimeSec,1f);
			
			Friction.nextAction(smallText1,invEnergyLoss);
			Friction.nextAction(smallText2,invEnergyLoss);
			
			smallText1.nextPos(elapsedTimeSec);
			smallText2.nextPos(elapsedTimeSec);
		}
		//----------------------------------------------------------------------------
		public void nextActionPlanes(float elapsedTimeSec)
		{
			
			
			for(int i=0;i<plane.length;i++){
				
				
				
				if( plane[i].isActive() ){
				
					//if( plane[i].health != 0f ){

						plane[i].textureIndex = plane[i].counter.nextValueAlternatingTS(elapsedTimeSec); 
						
						space.checkVacancy(plane[i],collisions);
						
						plane[i].nextPos(elapsedTimeSec);

											
						if(!collisions.isEmpty()){
							processCollisionsPlane(i,collisions);
							collisions.clear();
						}
					
						if( plane[i].posX < -0.5f || plane[i].posY > 2f){ 
							plane[i].setInactive(); 
							space.remove(plane[i]);
							
						}

						
					//}
					/*
					else{
						plane[i].setInactive(); 
						space.remove(plane[i]);
						
					}
					*/				
				
				}
				else{
					if( rand.nextFloat()<=0.14f*elapsedTimeSec ){
						
			
						if( rand.nextFloat()<=0.3f ){
							if( lastPlane!=null &&  !(lastPlane.isActive() && lastPlane.posX>1f) ){
									if( planeGenHeight == 0.35f ){ planeGenHeight = 0.625f; }
									else{ planeGenHeight = 0.35f; }
							}
						}
						
						
						plane[i].setPos(3.5f,planeGenHeight-0.085f*rand.nextFloat());		
						plane[i].setVelocity(-1.7f,0f);
						plane[i].setHealth(2f);
						plane[i].angle = 0f;
						
						
						if( lastPlane!=null && lastPlane.isActive() && lastPlane.posX > 3.1f ){
							plane[i].posX = lastPlane.posX+0.5f;
						}
						
						plane[i].counter.startTS(0,0,6,-1,30);
						space.insert(plane[i]);					
						lastPlane = plane[i];
						plane[i].setActive();
						
						
						plane[i].playSound(soundPool,planeSoundID,0.31f,0.31f,0,0.5f,1.5f);
						
					}
					
				}

			
			}
				
			
			
			
		}
		//----------------------------------------------------------------------------
		public void nextActionTrees(float elapsedTimeSec)
		{
			
			
			for(int i=0;i<tree.length;i++){
				
				
				
				if( tree[i].isActive() ){
				
					tree[i].nextPos(elapsedTimeSec);											
				
					if( tree[i].posX < -0.5f ){ 
						tree[i].setInactive(); 				
					}
											
				}
				else{
					if( rand.nextFloat()<treeGenFrequency*elapsedTimeSec ){
						
						tree[i].setPos(3.5f,1.45f-tree[i].bounds.height);		
						
						if( lastTree!=null && lastTree.isActive() ){
							if( lastTree.posX > 3.4f ){
								tree[i].posX = lastTree.posX+0.1f;
							}							
						}
						
						
						lastTree = tree[i];
						tree[i].setActive();											
					}
					
				}

			
			}
				
			
			
			
		}
		//----------------------------------------------------------------------------
		public void nextActionBuildings(float elapsedTimeSec)
		{
			
			
			for(int i=0;i<onscreenHouse.length;i++){
				

				if( (onscreenHouse[i].posX < -0.5f) ){
						
					onscreenHouse[i].setInactive();
					space.remove(onscreenHouse[i]);
					
					
					if( chimney[i].isActive() ){
						chimney[i].setInactive();
						space.remove(chimney[i]);
						
						if( chimney[i].chimneyNumber==50 && chimney[i].id==4 ){
							if( gameMode==GameMode.GAME_IN_PROGRESS ){
								if( score < 50 ){ initGameEnd(); }
								else{ initGameWin(); }
							}
							else{ treeGenFrequency = 0.5f; }
						}
					}
			
					
					if( numChimneys<50 ){ 
					
						int k = rand.nextInt(house.length);
						while( house[k].isActive() ){
							k++;
							if(k>=house.length){k=0;}
						}
						
						
						if( lastHouse!=null ){ 
							house[k].setPos(lastHouse.posX+0.75f,1.47f-house[k].bounds.height);											
						}
						else{
							house[k].setPos(4f,1.45f-house[k].bounds.height);
						}
						
						onscreenHouse[i] = house[k];
						onscreenHouse[i].setActive();
						lastHouse = onscreenHouse[i];
									
						space.insert(onscreenHouse[i]);				
						
						onscreenHouse[i].textureIndex = rand.nextInt(4);
						
						float randFloat = rand.nextFloat();
						
						if( randFloat>=0.4f && i!=0 && !(chimney[i-1].isActive()) ){
						
							chimney[i].setActive();	
							chimney[i].setId(4);
							chimney[i].setTextureIndex(0);
							chimney[i].setPos(onscreenHouse[i].posX-0.03f,onscreenHouse[i].posY-onscreenHouse[i].bounds.height-chimney[i].bounds.height);	
							chimney[i].setHealth(1f);
							space.insert(chimney[i]);
			
														
							numChimneys++;
							chimney[i].chimneyNumber = numChimneys;			
							chimney[i].chimneyServiced = false;
							
							chimneyNumber[i].setText(String.valueOf(numChimneys));
							chimneyNumber[i].setPos(onscreenHouse[i].posX,onscreenHouse[i].posY+onscreenHouse[i].bounds.height-0.1f);
						

							snowman[i].setPos(onscreenHouse[i].posX-0.3f,1.39f);	
						}
						else if( randFloat<=0.15f ){
							
							chimney[i].setActive();	
							chimney[i].setId(9);
							chimney[i].setTextureIndex(2);
							chimney[i].setPos(onscreenHouse[i].posX-0.03f,onscreenHouse[i].posY-onscreenHouse[i].bounds.height-chimney[i].bounds.height);	
							chimney[i].setHealth(1f);
							space.insert(chimney[i]);
							
						}
						
						
	
					}
				
					
					
					
				}
	
				
				if( onscreenHouse[i].isActive() ){
					onscreenHouse[i].nextPos(elapsedTimeSec);
				}

			
				if( chimney[i].isActive() ){		
					chimney[i].nextPos(elapsedTimeSec);
					snowman[i].nextPos(elapsedTimeSec);
					chimneyNumber[i].nextPos(elapsedTimeSec);
				}
				
				

			
			}
				
		}
		//----------------------------------------------------------------------------
		public void nextActionReindeer(float elapsedTimeSec, float invEnergyLoss)
		{
		
			
			int textureIndex;
			if( !reindeerPullUpCounter.finishedTS() ){ 
				textureIndex = reindeerPullUpCounter.nextValueAlternatingTS(elapsedTimeSec); 
			}
			else{
				textureIndex = 5;
			}
			
			
			boolean deadInTandem = true;
			
			for(int i=reindeer.length-1;i>=0;i--){
				
				
				if( reindeer[i].isActive() ){
					
					if( reindeer[i].health==1f ){
						
						reindeer[i].setTextureIndex(textureIndex); 
						
						if(reindeer[i].pullUp){
							reindeer[i].angle += elapsedTimeSec*180f;
											
							if(reindeer[i].angle>35f){ 
								reindeer[i].angle = 35f;
								reindeer[i].pullUp = false; 
							}
						}
						else{ reindeer[i].angle = (reindeer[i].angle+40f)*invEnergyLoss - 40f; }
					}
					else{ reindeer[i].angle = (reindeer[i].angle+50f)*invEnergyLoss - 50f; }
					
					gravity.nextAction(elapsedTimeSec,reindeer[i]);		
					
					if( reindeer[i].health == 1f || !deadInTandem ){ 
						strutsX[i].nextActionX(elapsedTimeSec,1f,1f);
						if(i!=0){ reinsY[i].nextActionY(elapsedTimeSec,0.3f,0.3f); }
						else{ reinsY[i].nextActionY(elapsedTimeSec,0.15f,0.3f); }
						deadInTandem = false;

					}
					else{  
						if( (i!=0 && reindeer[i-1].health==1f) || (i==0 && sleigh.health==1f) ){ 
							reins[i].nextAction(elapsedTimeSec,0f,0.3f); 
						}
						else{ reins[i].nextAction(elapsedTimeSec,0.3f,0.3f); }
											
						airResistance.nextAction(elapsedTimeSec,reindeer[i]);
						Friction.nextAction(reindeer[i],invEnergyLoss);
					} 
					
					
					space.checkVacancy(reindeer[i],collisions);
					
					
					if(!collisions.isEmpty()){
						processCollisionsReindeer(i,collisions);
						collisions.clear();
					}
			
					reindeer[i].nextPos(elapsedTimeSec);
					
					if( reindeer[i].posX < -1f || reindeer[i].posY > 2f){ reindeer[i].setInactive(); }

				}
			}

			
		
		}		
		//----------------------------------------------------------------------------
		public void nextActionSleigh(float elapsedTimeSec, float invEnergyLoss)
		{
				
			
			if( sleigh.isActive() ){
				
				if( sleigh.health == 1f ){
					
					if(sleigh.pullUp){
						sleigh.angle += elapsedTimeSec*180f;
						
						if(sleigh.angle>7f){ 
							sleigh.angle = 7f;
							sleigh.pullUp = false; 
						}
					}
					else{ sleigh.angle = (sleigh.angle+15f)*invEnergyLoss - 15f; }

				}
				else{ sleigh.angle = (sleigh.angle+20f)*invEnergyLoss - 20f; }
				
				
				gravity.nextAction(elapsedTimeSec,sleigh);
				space.checkVacancy(sleigh,collisions);
			
				

				if(!collisions.isEmpty()){
					processCollisionsSleigh(collisions);
					collisions.clear();
				}
				
				sleigh.nextPos(elapsedTimeSec);
				
				if( sleigh.posX < -1f || sleigh.posY > 2f){ sleigh.setInactive(); }
			
				
				
				if( (sleigh.health!=1 || !sleigh.isActive()) ){ 
									
					if( gameMode == GameMode.GAME_IN_PROGRESS ){ initGameOver(); }
									
				}
			}
				
			
			
		}
		//----------------------------------------------------------------------------
		public void nextActionPresents(float elapsedTimeSec)
		{
			if( presentShower ){
				float time = presentShowerTimer.elapsedTimeSec();				
				if( time < presentShowerTime ){				
					if(time < presentShowerTime-3f){ nextRandomPresent(elapsedTimeSec); }
				}
				else{
					presentShower = false;
					maxPresents = maxReleasePresents;
				}
			}
	
			
			for(int i=0;i<maxPresents;i++){
			
				if( present[i].isActive() ){
					
					gravity.nextAction(elapsedTimeSec,present[i]);
					//if(present[i].velX>-1f){ airResistance.nextAction(elapsedTimeSec,present[i]); }
					space.checkVacancy(present[i],collisions);
				
					

					if(!collisions.isEmpty()){
						processCollisionsPresent(i,collisions);
						collisions.clear();
					}
					
					present[i].nextPos(elapsedTimeSec);
					
					if( present[i].posX < -0.2f || present[i].posY > 2f){ 
						present[i].setInactive(); 
						space.remove(present[i]); 
					}
										
					
				}
			}				
			
			
		}
		//----------------------------------------------------------------------------
		public void nextActionExplosions(float elapsedTimeSec)
		{
			FSItr<ExplosionObj> itr = explosions.firstItr();
			
			while( !itr.atHeader() ){
			
				ExplosionObj explosion = itr.get();
				if( !explosion.animCounter.finishedTS() ){
					explosion.setTextureIndex( explosion.animCounter.nextValueTS(elapsedTimeSec) );
					explosion.nextPos(elapsedTimeSec);
					itr.advance();
				}
				else{
					itr.remove();
				}
			
			}
		
		}
		//----------------------------------------------------------------------------
		public void nextActionFire(float elapsedTimeSec)
		{
			FSItr<FireObj> itr = fire.firstItr();
			
			while( !itr.atHeader() ){
			
				FireObj currentFire = itr.get();
				
				currentFire.objLock.lock();
				if( currentFire.nextTextureIndices(elapsedTimeSec) && currentFire.owner.isActive() ){
					currentFire.nextFlamePos(elapsedTimeSec);
					itr.advance();
				}
				else{
					itr.remove();
				}
				currentFire.objLock.unlock();

				
			}
		
		}		
		//----------------------------------------------------------------------------
		public void nextActionOther(float elapsedTimeSec)
		{
			farHillsTexturePosX += 0.0082f*elapsedTimeSec;	
			nearHillsTexturePosX += 0.021f*elapsedTimeSec;
			groundTexturePosX += 2.75f*elapsedTimeSec;

			if( farHillsTexturePosX>10f ){ farHillsTexturePosX -= 10; }
			if( nearHillsTexturePosX>10f ){ nearHillsTexturePosX -= 10; }
			if( groundTexturePosX>75f ){ groundTexturePosX -= 75; }

		
		}
		//----------------------------------------------------------------------------
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//----------------------------------------------------------------------------
		public void processCollisionsPlane(int index, FSList<BoundingBox> collisions)
		{
			FSItr<BoundingBox> itr = collisions.firstItr();
			
			BoundingBox currentbb;
			
			while( !itr.atHeader() ){
				
				currentbb = itr.get();
				
				switch(currentbb.owner.id){
					case 4: 
					case 9:	
						if( ((ChimneyObj)currentbb.owner).chimneyServiced ){
							((ChimneyObj)currentbb.owner).setHealth(0f); 
							explodeObject(currentbb.owner,currentbb.owner.velX,currentbb.owner.velY,0);
						}
						space.remove((SimpleObj)currentbb.owner);
					case 0:
					case 3:
						plane[index].setInactive(); 
						space.remove(plane[index]);
						plane[index].setHealth(0f);
						plane[index].setVelocity(-1f,0f);
						explodeObject(plane[index],currentbb.owner.velX,-0.3f,2);
						break;	
					default: break;
				}
				
				itr.advance();
			}
		
			
		}
		//----------------------------------------------------------------------------
		public void processCollisionsReindeer(int index, FSList<BoundingBox> collisions)
		{
			FSItr<BoundingBox> itr = collisions.firstItr();
			
			BoundingBox currentbb;
			
			while( !itr.atHeader() ){
				
				currentbb = itr.get();
				
				switch(currentbb.owner.id){
					case 4:	
						((ChimneyObj)currentbb.owner).setHealth(0f); 
						space.remove((SimpleObj)currentbb.owner);
						explodeObject(currentbb.owner,currentbb.owner.velX,currentbb.owner.velY,0);
						break;
					case 9:
						((ChimneyObj)currentbb.owner).setHealth(0f); 
						space.remove((SimpleObj)currentbb.owner);
						
						if( reindeer[index].health==1f ){
							presentShower = true;
							maxPresents = present.length;	
							presentShowerTime = 9.5f;	
							presentShowerTimer.start();
							soundPool.playSound(itemSoundID,1f,1f,1,0,0.7f);
							setBackgroundMusic(R.raw.bells,0.4f,0,false);
						}
						break;
					case 6:
						
						if( ((PlaneObj)currentbb.owner).health == 2f ){
							((PlaneObj)currentbb.owner).setHealth(1f); 
							((PlaneObj)currentbb.owner).angle = 50f;						
							explodeObject(currentbb.owner,currentbb.owner.velX,-0.2f,1);
							currentbb.owner.setVelocity(-1.4f,1f);
							combustObject(currentbb.owner,0f,0f,0.5f,-0.5f,2f,0.7f,0.05f,0);
						}
						
						break;
					default: break;	
						
				}
				
				switch(currentbb.owner.id){						
					case 0:
						if(reindeer[index].velX>-1f){ reindeer[index].incrementVelocity(-0.15f,0f); }
					case 3:
					case 4:
					case 6:
						CollisionHandler.nextAction(reindeer[index].bounds,currentbb,0.3f,1f,0f); 				
						reindeer[index].angle = 0f;
						
						if( reindeer[index].health==1f ){
							reindeer[index].setHealth(0f);			
							reindeer[index].setTextureIndex(6);
							reindeer[index].pullUp = false;
						
							punchSoundTimer.playSound(soundPool,punchSoundID,0.4f,0.25f,0,1.3f,0.1f);
							//soundPool.stop(punchSoundID);
							//reindeer[index].playSound(soundPool,punchSoundID,0.2f,0.2f,0,1.3f,1.5f);
						}
						break;
					case 10:

						CollisionHandler.nextAction(reindeer[index].bounds,currentbb,0.3f,0f,1f);
						break;
						
					default: break;
				}
				
				
		
				
				itr.advance();
			}
		
			
		}
		//----------------------------------------------------------------------------
		public void processCollisionsSleigh(FSList<BoundingBox> collisions)
		{
			FSItr<BoundingBox> itr = collisions.firstItr();
			
			BoundingBox currentbb;
			
			while( !itr.atHeader() ){
				
				currentbb = itr.get();
				
				switch(currentbb.owner.id){
					case 4:

						sleigh.setTextureIndex(2);
						
						if(sleigh.health>0f){		
							combustObject(sleigh,0f,0f,0f,-0.2f,1.5f,0.7f,0.05f,0);	
							sleigh.playSound(soundPool,screamSoundID,0.7f,0.5f,0,1f,0f);	
						}

						((ChimneyObj)currentbb.owner).setHealth(0f); 
						space.remove((SimpleObj)currentbb.owner);
						explodeObject(currentbb.owner,currentbb.owner.velX,currentbb.owner.velY,0);
						
						break;
					case 9:
						((ChimneyObj)currentbb.owner).setHealth(0f); 
						space.remove((SimpleObj)currentbb.owner);
						presentShower = true;
						presentShowerTimer.start();
						maxPresents = present.length;
						presentShowerTime = 9.5f;
						soundPool.playSound(itemSoundID,1f,1f,1,0,0.7f);
						setBackgroundMusic(R.raw.bells,0.4f,0,false);		
						break;
					case 6:
						if( ((PlaneObj)currentbb.owner).health == 2f ){
							((PlaneObj)currentbb.owner).setHealth(1f); 
							((PlaneObj)currentbb.owner).angle = 50f;						
							explodeObject(currentbb.owner,currentbb.owner.velX,-0.2f,1);
							currentbb.owner.setVelocity(-1.4f,1f);
							combustObject(currentbb.owner,0f,0f,0.5f,-0.5f,2f,0.7f,0.05f,0);
						}
						
						sleigh.setTextureIndex(2);
						
						if(sleigh.health>0f){
							//explodeObject(sleigh,sleigh.velX,-0.4f,2);				
							combustObject(sleigh,0f,0f,0f,-0.2f,1f,0.7f,0.05f,0);
							sleigh.playSound(soundPool,screamSoundID,0.75f,0.5f,0,1f,0f);	
						}
			
						break;
						
					case 0:
					case 3:
						if( sleigh.health>0f ){
							if( rand.nextFloat()<=0.38f ){
								explodeObject(sleigh,sleigh.velX,-0.4f,2);				
								combustObject(sleigh,0f,0f,0f,-0.2f,1.5f,0.7f,0.05f,0);				
								sleigh.setTextureIndex(2);
								
								sleigh.playSound(soundPool,screamSoundID,0.75f,0.5f,0,1f,0f);	
							}
							else{ sleigh.setTextureIndex(1); }
							
							
						}				
						break;
					default: break;

				}
				
				
				
						
				switch(currentbb.owner.id){
					case 0:
						if(sleigh.velX>-1f){ sleigh.incrementVelocity(-0.15f,0f); }						
					case 3:
					case 4:
					case 6:
						CollisionHandler.nextAction(sleigh.bounds,currentbb,0.3f,1f,0f);					
						sleigh.pullUp = false;
						sleigh.angle = 0f;
						sleigh.setHealth(0f);
						break;
					case 10:
						CollisionHandler.nextAction(sleigh.bounds,currentbb,0.3f,0f,1f);
						break;

					default: break;
				}
				
				itr.advance();
			}
		
			
		}
		//----------------------------------------------------------------------------
		public void processCollisionsPresent(int index, FSList<BoundingBox> collisions)
		{
			FSItr<BoundingBox> itr = collisions.firstItr();
			
			BoundingBox currentbb;
			
			while( !itr.atHeader() ){
				
				currentbb = itr.get();
				
				
				
				switch(currentbb.owner.id){
					case 4:
						float diffX = present[index].posX - currentbb.owner.posX;
						float diffY = present[index].posY - currentbb.owner.posY;
						float totalWidth = present[index].bounds.width + currentbb.width;
						float totalHeight = present[index].bounds.height + currentbb.height;
						

						if( diffY<0f && (Math.abs(diffX)/totalWidth)<(Math.abs(diffY)/totalHeight) ){ 
								
							if( !((ChimneyObj)currentbb.owner).chimneyServiced ){
								score++; 
								displayScore = true;
								scoreTimer.start();					
								present[index].setInactive();
								space.remove(present[index]);								
								((ChimneyObj)currentbb.owner).chimneyServiced = true;
								((ChimneyObj)currentbb.owner).setTextureIndex(1);
								soundPool.playSound(scoreSoundID,1f,1f,1,0,1f);
								
								if( score==50 ){ initGameWin(); }
							}
						}
						else{
							CollisionHandler.nextAction(present[index].bounds,currentbb,0.3f,1f,0f);
						}
						
						break;
					case 0:
						//if(present[index].velX>-1f){ present[index].incrementVelocity(-0.15f,0f); }	
					case 3:
					case 9:
						CollisionHandler.nextAction(present[index].bounds,currentbb,0.3f,1f,0f);
						break;
					case 5:
					case 10:
						if( present[index].id==currentbb.owner.id ){ 
							CollisionHandler.nextAction(present[index].bounds,currentbb,0.3f,1f,1f);
						}
						break;
					case 6:
						if( ((PlaneObj)currentbb.owner).health == 2f ){
							
							if(present[index].id==5){ ((PlaneObj)currentbb.owner).setHealth(1f); }
							else{ ((PlaneObj)currentbb.owner).health-=0.5f; } 
							
							((PlaneObj)currentbb.owner).angle = 45f;
							explodeObject(currentbb.owner,currentbb.owner.velX,-0.2f,1);
							combustObject(currentbb.owner,0f,0f,0.5f,-0.5f,2f,0.7f,0.05f,0);
							currentbb.owner.setVelocity(-1.3f,1f);
						}
						else if( ((PlaneObj)currentbb.owner).health == 0f ){
						
							currentbb.owner.setInactive(); 
							space.remove((PlaneObj)currentbb.owner);
							((PlaneObj)currentbb.owner).setHealth(0f);
							currentbb.owner.setVelocity(-1f,0f);
							explodeObject(currentbb.owner,currentbb.owner.velX,-0.3f,2);
							
						}

						
					case 1:
					case 2:
						if(present[index].id==10){			
							CollisionHandler.nextAction(present[index].bounds,currentbb,1f,1f,0f);
						}
						break;
					/*
					case 1:
					case 2:
						switch(gameMode){				
							case GAME_WIN:
								CollisionHandler.nextAction(present[index].bounds,currentbb,0.3f,1f,0f);
							default: break;
						}
						break;
					*/	
					default: break;
				}
				
				itr.advance();
			}
		
			
		}
		//----------------------------------------------------------------------------
		public void explodeObject(EmptyObj obj, float vx, float vy, int size)
		{
			ExplosionObj explosion = new ExplosionObj(obj.posX,obj.posY,vx,vy,size,true);
			explosion.animCounter = new AccumulationCounter();
			explosion.soundTimer = new SoundTimer();
			explosion.animCounter.startTS(0,0,16,16,20);
			
			switch(size){
				case 0: explosion.playSound(soundPool,smallExplosionSoundID,0.55f,0.55f,0,1f,0); break;
				case 1: explosion.playSound(soundPool,explosionSoundID,0.45f,0.45f,0,1.5f,0); break;
				case 2: explosion.playSound(soundPool,explosionSoundID,0.55f,0.55f,0,1.3f,0); break;
			}
			
			explosions.enqueue(explosion);			
			
		}
		//----------------------------------------------------------------------------
		public void combustObject(EmptyObj obj, float x, float y, float vx, float vy, 
				float burnDuration, float flameDuration, float flameGenTime, int size)
		{
			FireObj newFire = new FireObj(x,y,vx,vy,burnDuration,flameDuration,flameGenTime,obj,null,size);
			switch(size){
				case 0: newFire.playSound(soundPool,fireSoundID,0.1f,0.1f,0,1.3f,0); break;
				case 1: newFire.playSound(soundPool,fireSoundID,0.15f,0.15f,0,1.3f,0); break;
				case 2: newFire.playSound(soundPool,fireSoundID,0.2f,0.2f,0,1.3f,0); break;
			}	
			
			newFire.start();
			fire.enqueue(newFire);			
			
		}
		//----------------------------------------------------------------------------
		
	};//end Runnable

	//----------------------------------------------------------------------------
	//----------------------------------------------------------------------------
	
	
	
	
	
	
	

	//----------------------------------------------------------------------------		
	//----------------------------------------------------------------------------
	public class ARGLRenderer extends GLRenderer
	{
		
		
		
		Sprite planeSprite;		
		Sprite treeSprite;	
		Sprite reindeerSprite;
		Sprite sleighSprite;
		Sprite smallHouseSprite;
		Sprite bigHouseSprite;
		Sprite chimneySprite;
		Sprite snowmanSprite;
		Sprite presentSprite;
		Sprite groundSprite;
		Sprite nearHillsSprite;	
		Sprite farHillsSprite;	
		Sprite skySprite;	
		
		
		Sprite[] explosionSprite = new Sprite[3];
		Sprite smallExplosionSprite;
		
		Sprite smallCharSprite;
		Sprite charSprite;
		Sprite scoreCharSprite;
		Sprite smallScoreCharSprite;
		Sprite buttonSprite;
		
		
		Line reinLine;
		
		float[] charColors = {
			0.5f, 0.5f, 0.5f, 1.0f,
			0.4f, 0.4f, 0.4f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f,
			1f, 1f, 1f, 1.0f
		};

		float[] gameObjColors = {
			0.85f, 0.85f, 1f, 1.0f,
			0.35f, 0.35f, 1f, 1.0f,
			0.65f, 0.65f, 1f, 1.0f,
			1f, 1f, 1f, 1.0f
		};

		
		
		int[] pr = new int[4];
		int[] rd = new int[7];
		int[] ss = new int[3];
		int sk;
		int[] h1 = new int[4];
		int[] h2 = new int[4];
		int[] c = new int[3];
		int g;
		int hf, hb;
		int[] ex = new int[17];
		int[] p = new int[7];
		int t;
		
		int[] ch = new int[68];
		//int[] chs = new int[10];
		int[] chp = new int[10];
		int[] rb = new int[4];
		int sn;

		
		//Timer renderTimer = new Timer();
		Timer FPSTimer = new Timer();
		float FPS=0f;
		int frames=0;

		//----------------------------------------------------------------------------
		ARGLRenderer() 
		{
			super();			
		}
		//----------------------------------------------------------------------------
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			super.onSurfaceCreated(gl, config);
			
			FPSTimer.start();
			//renderTimer.start();

			//gl.glEnable(gl.GL_CULL_FACE);         // do not calculate inside of polys
			//gl.glFrontFace(gl.GL_CW);            // counter clock-wise polygons are out

			gl.glLineWidth(3);
			//gl.glColor4f(0.63671875f, 0.76953125f, 0.22265625f, 0f);
		
	
			p[0] = loadTexture(gl,context,R.drawable.plane0);  
			p[1] = loadTexture(gl,context,R.drawable.plane1);  
			p[2] = loadTexture(gl,context,R.drawable.plane2);  
			p[3] = loadTexture(gl,context,R.drawable.plane3);  
			p[4] = loadTexture(gl,context,R.drawable.plane4);  
			p[5] = loadTexture(gl,context,R.drawable.plane5);  
			p[6] = loadTexture(gl,context,R.drawable.plane6);  
			

			t = loadTexture(gl,context,R.drawable.tree2);
			
			//rd[0] = loadTexture(gl,context,R.drawable.deer0);  
			rd[0] = loadTexture(gl,context,R.drawable.deer1); 
			rd[1] = loadTexture(gl,context,R.drawable.deer2); 
			rd[2] = loadTexture(gl,context,R.drawable.deer4); 
			rd[3] = loadTexture(gl,context,R.drawable.deer5); 
			//rd[5] = loadTexture(gl,context,R.drawable.deer6); 
			rd[4] = loadTexture(gl,context,R.drawable.deer7); 
			rd[5] = loadTexture(gl,context,R.drawable.deer8); 
			rd[6] = loadTexture(gl,context,R.drawable.deerdead);
			
			ss[0] = loadTexture(gl,context,R.drawable.sleigh);
			ss[1] = loadTexture(gl,context,R.drawable.sleighdead);
			ss[2] = loadTexture(gl,context,R.drawable.sleighdeadburnt);
			
			h1[0] = loadTexture(gl,context,R.drawable.house1);
			h1[1] = loadTexture(gl,context,R.drawable.house1b);
			h1[2] = loadTexture(gl,context,R.drawable.house3);
			h1[3] = loadTexture(gl,context,R.drawable.house3b);
			h2[0] = loadTexture(gl,context,R.drawable.house2);
			h2[1] = loadTexture(gl,context,R.drawable.house2b);
			h2[2] = loadTexture(gl,context,R.drawable.house4);
			h2[3] = loadTexture(gl,context,R.drawable.house4b);
			
			
			pr[0] = loadTexture(gl,context,R.drawable.present0);
			pr[1] = loadTexture(gl,context,R.drawable.present1);
			pr[2] = loadTexture(gl,context,R.drawable.present2);
			pr[3] = loadTexture(gl,context,R.drawable.present3);
			
			c[0] = loadTexture(gl,context,R.drawable.chimney);
			c[1] = loadTexture(gl,context,R.drawable.chimneylit);
			c[2] = loadTexture(gl,context,R.drawable.present4);
			
			
			sn = loadTexture(gl,context,R.drawable.snowman);

			
			hf = loadTexture(gl,context,R.drawable.hills1);			
			hb = loadTexture(gl,context,R.drawable.hills2);
			sk = loadTexture(gl,context,R.drawable.sky2);
			g = loadTexture(gl,context,R.drawable.ground2);
	
			ex[0] = loadTexture(gl,context,R.drawable.explosion0);
			ex[1] = loadTexture(gl,context,R.drawable.explosion1);
			ex[2] = loadTexture(gl,context,R.drawable.explosion2);
			ex[3] = loadTexture(gl,context,R.drawable.explosion3);
			ex[4] = loadTexture(gl,context,R.drawable.explosion4);
			ex[5] = loadTexture(gl,context,R.drawable.explosion5);
			ex[6] = loadTexture(gl,context,R.drawable.explosion6);
			ex[7] = loadTexture(gl,context,R.drawable.explosion7);
			ex[8] = loadTexture(gl,context,R.drawable.explosion8);
			ex[9] = loadTexture(gl,context,R.drawable.explosion9);
			ex[10] = loadTexture(gl,context,R.drawable.explosion10);
			ex[11] = loadTexture(gl,context,R.drawable.explosion11);
			ex[12] = loadTexture(gl,context,R.drawable.explosion12);
			ex[13] = loadTexture(gl,context,R.drawable.explosion13);
			ex[14] = loadTexture(gl,context,R.drawable.explosion14);
			ex[15] = loadTexture(gl,context,R.drawable.explosion15);
			ex[16] = loadTexture(gl,context,R.drawable.explosion16);

			
			
			chp[0] = loadTexture(gl,context,R.drawable.f0);
			chp[1] = loadTexture(gl,context,R.drawable.f1);
			chp[2] = loadTexture(gl,context,R.drawable.f2);
			chp[3] = loadTexture(gl,context,R.drawable.f3);
			chp[4] = loadTexture(gl,context,R.drawable.f4);
			chp[5] = loadTexture(gl,context,R.drawable.f5);
			chp[6] = loadTexture(gl,context,R.drawable.f6);
			chp[7] = loadTexture(gl,context,R.drawable.f7);
			chp[8] = loadTexture(gl,context,R.drawable.f8);
			chp[9] = loadTexture(gl,context,R.drawable.f9);
			
			/*
			chs[0] = loadTexture(gl,context,R.drawable.zero);
			chs[1] = loadTexture(gl,context,R.drawable.one);
			chs[2] = loadTexture(gl,context,R.drawable.two);
			chs[3] = loadTexture(gl,context,R.drawable.three);
			chs[4] = loadTexture(gl,context,R.drawable.four);
			chs[5] = loadTexture(gl,context,R.drawable.five);
			chs[6] = loadTexture(gl,context,R.drawable.six);
			chs[7] = loadTexture(gl,context,R.drawable.seven);
			chs[8] = loadTexture(gl,context,R.drawable.eight);
			chs[9] = loadTexture(gl,context,R.drawable.nine);
			*/
			
			
			ch[0] = loadTexture(gl,context,R.drawable.zero);
			ch[1] = loadTexture(gl,context,R.drawable.one);
			ch[2] = loadTexture(gl,context,R.drawable.two);
			ch[3] = loadTexture(gl,context,R.drawable.three);
			ch[4] = loadTexture(gl,context,R.drawable.four);
			ch[5] = loadTexture(gl,context,R.drawable.five);
			ch[6] = loadTexture(gl,context,R.drawable.six);
			ch[7] = loadTexture(gl,context,R.drawable.seven);
			ch[8] = loadTexture(gl,context,R.drawable.eight);
			ch[9] = loadTexture(gl,context,R.drawable.nine);
			ch[10] = loadTexture(gl,context,R.drawable.aa);
			ch[11] = loadTexture(gl,context,R.drawable.bb);
			ch[12] = loadTexture(gl,context,R.drawable.cc);
			ch[13] = loadTexture(gl,context,R.drawable.dd);
			ch[14] = loadTexture(gl,context,R.drawable.ee);
			ch[15] = loadTexture(gl,context,R.drawable.ff);
			ch[16] = loadTexture(gl,context,R.drawable.gg);
			ch[17] = loadTexture(gl,context,R.drawable.hh);
			ch[18] = loadTexture(gl,context,R.drawable.ii);
			ch[19] = loadTexture(gl,context,R.drawable.jj);
			ch[20] = loadTexture(gl,context,R.drawable.kk);
			ch[21] = loadTexture(gl,context,R.drawable.ll);
			ch[22] = loadTexture(gl,context,R.drawable.mm);
			ch[23] = loadTexture(gl,context,R.drawable.nn);
			ch[24] = loadTexture(gl,context,R.drawable.oo);
			ch[25] = loadTexture(gl,context,R.drawable.pp);
			ch[26] = loadTexture(gl,context,R.drawable.qq);
			ch[27] = loadTexture(gl,context,R.drawable.rr);
			ch[28] = loadTexture(gl,context,R.drawable.ss);
			ch[29] = loadTexture(gl,context,R.drawable.tt);
			ch[30] = loadTexture(gl,context,R.drawable.uu);
			ch[31] = loadTexture(gl,context,R.drawable.vv);
			ch[32] = loadTexture(gl,context,R.drawable.ww);
			ch[33] = loadTexture(gl,context,R.drawable.xx);
			ch[34] = loadTexture(gl,context,R.drawable.yy);
			ch[35] = loadTexture(gl,context,R.drawable.zz);
			ch[36] = loadTexture(gl,context,R.drawable.a);
			ch[37] = loadTexture(gl,context,R.drawable.b);
			ch[38] = loadTexture(gl,context,R.drawable.c);
			ch[39] = loadTexture(gl,context,R.drawable.d);
			ch[40] = loadTexture(gl,context,R.drawable.e);
			ch[41] = loadTexture(gl,context,R.drawable.f);
			ch[42] = loadTexture(gl,context,R.drawable.g);
			ch[43] = loadTexture(gl,context,R.drawable.h);
			ch[44] = loadTexture(gl,context,R.drawable.i);
			ch[45] = loadTexture(gl,context,R.drawable.j);
			ch[46] = loadTexture(gl,context,R.drawable.k);
			ch[47] = loadTexture(gl,context,R.drawable.l);
			ch[48] = loadTexture(gl,context,R.drawable.m);
			ch[49] = loadTexture(gl,context,R.drawable.n);
			ch[50] = loadTexture(gl,context,R.drawable.o);
			ch[51] = loadTexture(gl,context,R.drawable.p);
			ch[52] = loadTexture(gl,context,R.drawable.q);
			ch[53] = loadTexture(gl,context,R.drawable.r);
			ch[54] = loadTexture(gl,context,R.drawable.s);
			ch[55] = loadTexture(gl,context,R.drawable.t);
			ch[56] = loadTexture(gl,context,R.drawable.u);
			ch[57] = loadTexture(gl,context,R.drawable.v);
			ch[58] = loadTexture(gl,context,R.drawable.w);
			ch[59] = loadTexture(gl,context,R.drawable.x);
			ch[60] = loadTexture(gl,context,R.drawable.y);
			ch[61] = loadTexture(gl,context,R.drawable.z);
			ch[62] = loadTexture(gl,context,R.drawable.blank);
			ch[63] = loadTexture(gl,context,R.drawable.period);
			ch[64] = loadTexture(gl,context,R.drawable.comma);
			ch[65] = loadTexture(gl,context,R.drawable.shriek);			
			ch[67] = loadTexture(gl,context,R.drawable.colon);
	
			rb[0] = loadTexture(gl,context,R.drawable.releasebuttonready);
			rb[1] = loadTexture(gl,context,R.drawable.releasebuttonwaiting);
			rb[2] = loadTexture(gl,context,R.drawable.restartbutton);
			rb[3] = loadTexture(gl,context,R.drawable.restartbuttonwaiting);
			
			
			planeSprite = new Sprite((GL11)gl,0.16f,0.075f,1f,1f,p,gameObjColors);
			
			treeSprite = new Sprite((GL11)gl,0.135f,0.15f,1f,1f,t,gameObjColors);
			
			reindeerSprite = new Sprite((GL11)gl,0.06f,0.075f,1f,1f,rd,gameObjColors);
			
			sleighSprite = new Sprite((GL11)gl,0.1f,0.075f,1f,1f,ss,gameObjColors);
			
			
			smallHouseSprite = new Sprite((GL11)gl,0.2f,0.1f,1f,1f,h1,gameObjColors);
			bigHouseSprite = new Sprite((GL11)gl,0.2f,0.2f,1f,1f,h2,gameObjColors);
			
			
			chimneySprite = new Sprite((GL11)gl,0.045f,0.035f,1f,1f,c,gameObjColors);
			
			snowmanSprite = new Sprite((GL11)gl,0.03f,0.07f,1f,1f,sn,gameObjColors);
			
			presentSprite = new Sprite((GL11)gl,0.04f,0.04f,1f,1f,pr,gameObjColors);
			
			nearHillsSprite = new Sprite((GL11)gl,4.105f,0.2f,1f,1f,hf,null);
			farHillsSprite = new Sprite((GL11)gl,2.737f,0.2f,1f,1f,hb,null);
			skySprite = new Sprite((GL11)gl,1.3685f,0.766f,1f,1f,sk,null);
			groundSprite = new Sprite((GL11)gl,1.3685f,0.05f,7.5f,1f,g,gameObjColors);
			
			explosionSprite[0] = new Sprite((GL11)gl,0.07f,0.07f,1f,1f,ex,gameObjColors);
			explosionSprite[1] = new Sprite((GL11)gl,0.125f,0.125f,1f,1f,ex,gameObjColors);
			explosionSprite[2] = new Sprite((GL11)gl,0.2f,0.2f,1f,1f,ex,gameObjColors);
			

			
			charSprite = new Sprite((GL11)gl,0.1f,0.13f,1f,1f,ch,charColors);
			smallCharSprite = new Sprite((GL11)gl,0.045f,0.055f,1f,1f,ch,charColors);
			scoreCharSprite = new Sprite((GL11)gl,0.1f,0.17f,1f,1f,chp,charColors);
			smallScoreCharSprite = new Sprite((GL11)gl,0.045f,0.055f,1f,1f,ch,charColors);

			
			buttonSprite = new Sprite((GL11)gl,0.15f,0.17f,1f,1f,rb,null);
			
		
			//text.setText(String.valueOf(1234567890));
			//text.setText("1234567890");
			//text.setText(String.valueOf(pixelsX) + " " + String.valueOf(pixelsY));
			//text.setText(String.valueOf(renderer.getWidth()) + " " + String.valueOf(renderer.getHeight()));

			
			
			
			reinLine = new Line((GL11)gl,0f,0f,0f,1f);

			//counter.start(0,0,null,null,5);
			
			
			
			
			for(int i=0;i<=7;i++){ house[i].setSprite(smallHouseSprite); }
			for(int i=8;i<15;i++){ house[i].setSprite(bigHouseSprite); }	
			
			
			
			
			Shape.enableRendering((GL11)gl,true,true);

		}

		//----------------------------------------------------------------------------
		@Override
		public void onDrawFrame(GL10 gl)
		{
			super.onDrawFrame(gl);
		
			gl.glPushMatrix();
			
			
			
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glPushMatrix();
			gl.glTranslatef(1.3685f,0.766f,0);
			skySprite.drawClientSide((GL11)gl,0,false,true);
			gl.glTranslatef(0f,0.476f,0);
			farHillsSprite.setTexturePosX(farHillsTexturePosX);
			farHillsSprite.drawClientSide((GL11)gl,0,false,true);
			nearHillsSprite.setTexturePosX(nearHillsTexturePosX);
			nearHillsSprite.drawClientSide((GL11)gl,0,false,true);
			gl.glPopMatrix();			
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			
	
			
			for(int i=0;i<tree.length;i++){
				if( tree[i].isActive() ){			
					tree[i].drawClientSide((GL11)gl,treeSprite,true,true);
				}
			}
		
			
			for(int i=0;i<onscreenHouse.length;i++){ 
				if( onscreenHouse[i].isActive() ){ 
					
					onscreenHouse[i].drawClientSide((GL11)gl,true,true); 
					
					if( chimney[i].isActive() ){ 
						if( chimney[i].health==1f ){ 
							chimney[i].drawClientSide((GL11)gl,chimneySprite,true,true);	
						}
						if( chimney[i].id==4 ){ 
							chimneyNumber[i].draw((GL11)gl,smallScoreCharSprite,true);
							snowman[i].drawClientSide((GL11)gl,snowmanSprite,true,true);	
						} 	
					}
				
				
				} 	
			}

		
			
			for(int i=0;i<plane.length;i++){
				if( plane[i].isActive() ){			
					plane[i].drawClientSide((GL11)gl,planeSprite,plane[i].angle,true,true);
				}
			}
			
	
			
			gl.glDisable(gl.GL_TEXTURE_2D);
			reinLine.setVertices(sleigh.posX,sleigh.posY,reindeer[0].posX,reindeer[0].posY);
			reinLine.drawClientSide((GL11)gl,true,false);
			reinLine.setVertices(reindeer[0].posX,reindeer[0].posY,reindeer[1].posX,reindeer[1].posY);
			reinLine.drawClientSide((GL11)gl,true,false);
			reinLine.setVertices(reindeer[1].posX,reindeer[1].posY,reindeer[2].posX,reindeer[2].posY);
			reinLine.drawClientSide((GL11)gl,true,false);
			reinLine.setVertices(reindeer[2].posX,reindeer[2].posY,reindeer[3].posX,reindeer[3].posY);
			reinLine.drawClientSide((GL11)gl,true,false);	
			gl.glEnable(gl.GL_TEXTURE_2D);
			
			
			
			
			for(int i=0;i<reindeer.length;i++){
				if( reindeer[i].isActive() ){			
					reindeer[i].drawClientSide((GL11)gl,reindeerSprite,reindeer[i].angle,true,true);
				}
			}
			
			if( sleigh.isActive() ){	
				sleigh.drawClientSide((GL11)gl,sleighSprite,sleigh.angle,true,true);
			}
			

		
			
			for(int i=0;i<maxPresents;i++){ 
				if( present[i].isActive() ){ 
					present[i].drawClientSide((GL11)gl,presentSprite,true,true);	 
				} 	
			}
			
			


			
			groundSprite.setTexturePosX(groundTexturePosX);
			ground.drawClientSide((GL11)gl,groundSprite,true,true);
			
			
			
			
			for(FSItr<FireObj> itr = fire.firstItr(); !itr.atHeader(); itr.advance()){
				
				FireObj currentFire = itr.get();
				currentFire.objLock.lock();
				currentFire.objLock.unlock();
				currentFire.drawFire((GL11)gl,explosionSprite[currentFire.id]);
			}
			

			for(FSItr<ExplosionObj> itr = explosions.firstItr(); !itr.atHeader(); itr.advance()){
			
				ExplosionObj explosion = itr.get();				
				explosion.drawClientSide((GL11)gl,explosionSprite[explosion.id],true,true);

			}
				
			//if( !counter.finished() ){ text.setText(String.valueOf(counter.nextValue())); }
			//if(!reindeerPullUpCounter.finished()){ text.setText(String.valueOf(reindeerPullUpCounter.nextValueAlternating())); }
			//text.setText(String.valueOf(counter.nextValue()));
			//text.draw((GL11)gl,true);
			
			
		
			gl.glPopMatrix();
			
		
			switch(gameMode){
				case FIRST_GAME_BEGIN:
					smallText1.draw((GL11)gl,smallCharSprite,true);
					smallText2.draw((GL11)gl,smallCharSprite,true);
					
					gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
					button.drawClientSide((GL11)gl,buttonSprite,0,false,true);
					gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			
					break;

				case GAME_BEGIN:
					text2.draw((GL11)gl,charSprite,true);
					
					gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
					button.drawClientSide((GL11)gl,buttonSprite,0,false,true);
					gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
					
					break; 

				case GAME_IN_PROGRESS: 
				
					if( displayScore ){
						if( scoreTimer.elapsedTimeSec()<1f ){
							scoreText.setText(String.valueOf(score));
							scoreText.draw((GL11)gl,scoreCharSprite,true);
						}
						else{ displayScore = false; }
					}
					
					gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
					button.drawClientSide((GL11)gl,buttonSprite,0,false,true);
					gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

					break;
				case GAME_WIN:
				case GAME_END:
			
					smallText3.draw((GL11)gl,smallCharSprite,true);
								
				case GAME_OVER: 
				
					text1.draw((GL11)gl,charSprite,true);
					
					smallText1.draw((GL11)gl,smallCharSprite,true);
					smallText2.draw((GL11)gl,smallCharSprite,true);
				
					
					gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			
					if(scoreScreenTimer.elapsedTimeSec()>0.5f){
						button.drawClientSide((GL11)gl,buttonSprite,2,false,true); 	
					}
					else{
						button.drawClientSide((GL11)gl,buttonSprite,3,false,true); 
					}
			
					gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
					
					break;
			
			}
			/*
			float elapsedTime = FPSTimer.elapsedTimeSec();
			if(elapsedTime<1f){frames++;}
			else{ FPS=frames/elapsedTime; frames=0; FPSTimer.restart(); }
			//smallText3.setText(String.valueOf(FPS));
			//String stats1 = String.valueOf(runCount)+" "+String.valueOf(sleepCount)+" "+String.valueOf(yieldCount);
			String stats2 = String.valueOf((float)(timeSleeping)/runTimer.elapsedTime());		
			smallText3.setText(stats2);
			smallText3.draw((GL11)gl,smallCharSprite,true);
			*/

		
		}
		





	}
	//----------------------------------------------------------------------------

}