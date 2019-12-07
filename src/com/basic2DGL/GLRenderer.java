
package com.basic2DGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;


public class GLRenderer implements GLSurfaceView.Renderer
{

	int width;
	int height;
	
	//----------------------------------------------------------------------------
	protected GLRenderer()
	{
		super();

	}
	//----------------------------------------------------------------------------
	public int getWidth()
	{
		return width;
	}
	//----------------------------------------------------------------------------
	public int getHeight()
	{
		return height;
	}
	//----------------------------------------------------------------------------
	//max visible posX = 2.737
	public float pixelXToPosX(float x)
	{
		return 0.001525f*x*(1794f/width);
	}
	//----------------------------------------------------------------------------
	//max visible posY = 1.532
	public float pixelYToPosY(float y)
	{
		return 0.001525f*y*(1005f/height);
	}
	//----------------------------------------------------------------------------
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{

		gl.glClearColor(0.2f, 0.2f, 0.5f, 1.0f);


		//gl.glShadeModel(gl.GL_SMOOTH);        // use smooth shading
		//gl.glShadeModel(gl.GL_FLAT);
		//gl.glEnable(GL10.GL_DEPTH_TEST);        // hidden surface removal
		gl.glEnable(gl.GL_BLEND);					// enable blending
		gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);


		//gl.glHint(gl.GL_LINE_SMOOTH_HINT, gl.GL_FASTEST);
		//gl.glHint(gl.GL_POLYGON_SMOOTH_HINT, gl.GL_FASTEST);
		//gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_FASTEST);

		/*
		gl.glEnable(gl.GL_LIGHTING);				// enable lighting
		float[] lightPosition = { 0.0f, 0.0f, 0.0f, 1.0f };
		float[] diffuseLight = { 0.9f, 0.9f, 0.9f, 1.0f };
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, diffuseLight, 0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, diffuseLight, 0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightPosition, 0);
		gl.glEnable(gl.GL_LIGHT0);
		*/
		gl.glEnable(gl.GL_COLOR_MATERIAL);
		//float[] material = { 0.6f, 0.6f, 0.7f, 1.0f };
		//gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, material, 0);
		 

		//float[] lightModel = { 0.6f, 0.6f, 0.7f, 1.0f };
		//gl.glLightModelfv(gl.GL_LIGHT_MODEL_AMBIENT, lightModel, 0);
		
		gl.glEnable(gl.GL_TEXTURE_2D);


	}
	//----------------------------------------------------------------------------
	@Override
	public void onDrawFrame(GL10 gl)
	{

		//gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		gl.glClear(GL11.GL_COLOR_BUFFER_BIT);


		gl.glLoadIdentity();

	}
	//----------------------------------------------------------------------------
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{

		this.width = width;
		this.height = height;
		
		gl.glViewport(0,0,width,height);

		//float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		//gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);
		//GLU.gluPerspective(gl,54.0f, (float)width/(float)height, 0.01f, 10000.0f);
		//GLU.gluOrtho2D(gl, 0,width,height,0);
	    //gl.glOrthox(0,100*width,100*height,0,-1,1);
		gl.glOrthox(0,100*1794,100*1005,0,-1,1);


	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	//----------------------------------------------------------------------------
	public static int loadTexture(GL10 gl, final Context context, final int resourceId)
	{
		final int[] textureHandle = new int[1];
		
		gl.glGenTextures(1, textureHandle, 0);
		
		if (textureHandle[0] != 0)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;	// No pre-scaling

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
						
			// Bind to the texture in OpenGL
			gl.glBindTexture(gl.GL_TEXTURE_2D, textureHandle[0]);
			
			// Set filtering
			gl.glTexParameterx(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);
			gl.glTexParameterx(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);
			
			
			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(gl.GL_TEXTURE_2D, 0, bitmap, 0);
			
			
			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();						
		}
		
		if (textureHandle[0] == 0)
		{
			throw new RuntimeException("Error loading texture.");
		}
		
		return textureHandle[0];
	}
	
	//----------------------------------------------------------------------------
}