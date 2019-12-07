package com.FlappySanta;



import com.basic2DObj.*;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;


public class MainActivity extends Activity {

	GameGLSurfaceView glView;

	//----------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		glView = new GameGLSurfaceView(this, this);
		setContentView(glView);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

	}
	//----------------------------------------------------------------------------
	@Override
	protected void onResume() {

		super.onResume();
		
		glView.onResume();	
		
	}
	//----------------------------------------------------------------------------
	@Override
	public void onRestart()
	{
		super.onRestart();
	}
	//----------------------------------------------------------------------------
	@Override
	protected void onPause() {

		super.onPause();
		
		glView.onPause();
	
	}
	//----------------------------------------------------------------------------	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		glView.onDestroy();

	}
	//----------------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}
	//----------------------------------------------------------------------------




	//----------------------------------------------------------------------------
	void toast(String message, int duration)
	{
		Toast results = Toast.makeText(MainActivity.this, message, duration);

		//center the Toast in the screen
		results.setGravity(Gravity.CENTER, results.getXOffset()/2, results.getYOffset()/2);
		results.show();

	}
	//----------------------------------------------------------------------------

	//----------------------------------------------------------------------------
	//----------------------------------------------------------------------------

}

