package com.basic2DGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;



public abstract class Shape{

	protected FloatBuffer vertexBuffer = null;
	protected FloatBuffer colorBuffer = null;
	protected FloatBuffer textureBuffer = null;
	
	protected int numVertices = 0;


	protected int mode;

	//----------------------------------------------------------------------------
	protected Shape(){ }
	//----------------------------------------------------------------------------
	public Shape(int mode, int vertexCapacity, boolean color, boolean texture)
	{
		resizeClientBuffers(vertexCapacity,color,texture);
		this.mode = mode;
	}

	//----------------------------------------------------------------------------
	public Shape(GL11 gl, int mode, float[] vertices, float[] colors, float[] textures, Float scale)
	{
	
		mapToClientBuffers(vertices,colors,textures,scale);
		
		this.mode = mode;

	}
	//----------------------------------------------------------------------------
	public int numVertices()
	{
		return numVertices;
	}
	//----------------------------------------------------------------------------
	public void setVertices(float[] vertices, int offset)
	{
		int limit = Math.min(2*numVertices-offset, vertices.length);

		vertexBuffer.position(offset);
		vertexBuffer.put(vertices, 0, limit);
		vertexBuffer.position(0);

	}
	//----------------------------------------------------------------------------
	public void setVertex(float[] vertex, int offset)
	{
		vertexBuffer.position(offset);
		vertexBuffer.put(vertex, 0, 2);
		vertexBuffer.position(0);
	}	
	//----------------------------------------------------------------------------
	public void setColors(float[] colors, int offset)
	{
		int limit = Math.min(4*numVertices-offset, colors.length);

		colorBuffer.position(offset);
		colorBuffer.put(colors, 0, limit);
		colorBuffer.position(0);

	}
	//----------------------------------------------------------------------------
	public void setColor(float[] color, int offset)
	{

		colorBuffer.position(offset);
		colorBuffer.put(color, 0, 4);
		colorBuffer.position(0);

	}
	//----------------------------------------------------------------------------
	public void setAlpha(float alpha)
	{

		for(int i=0, numColorCoords=4*numVertices; i<numColorCoords; i+=4){

			colorBuffer.put(i+3,alpha);
		}

		
	}
	//----------------------------------------------------------------------------
	public void setTextures(float[] textures, int offset)
	{
		int limit = Math.min(2*numVertices-offset, textures.length);

		textureBuffer.position(offset);
		textureBuffer.put(textures, 0, limit);
		textureBuffer.position(0);

	}
	//----------------------------------------------------------------------------
	public void mapToClientBuffers(float[] vertices, float[] colors, float[] textures, Float scale)
	{

		float[] newVertices;

		if( scale==null ){ newVertices = vertices; }
		else{
			newVertices = new float[vertices.length];
			for(int i=0; i<vertices.length; i++){ newVertices[i] = scale*vertices[i]; }
		}
		numVertices = newVertices.length/2;


		ByteBuffer bbVertex = ByteBuffer.allocateDirect(newVertices.length<<2);
		bbVertex.order(ByteOrder.nativeOrder());
		vertexBuffer = bbVertex.asFloatBuffer();
		vertexBuffer.put(newVertices);
		vertexBuffer.position(0);


		if(colors!=null){
			ByteBuffer bbColor = ByteBuffer.allocateDirect(colors.length<<2);
			bbColor.order(ByteOrder.nativeOrder());
			colorBuffer = bbColor.asFloatBuffer();
			colorBuffer.put(colors);
			colorBuffer.position(0);
			
		}

		if(textures!=null){
			
			ByteBuffer bbTexture = ByteBuffer.allocateDirect(textures.length<<2);
			bbTexture.order(ByteOrder.nativeOrder());
			textureBuffer = bbTexture.asFloatBuffer();
			textureBuffer.put(textures);
			textureBuffer.position(0);
		}		
		
		
	}
	//----------------------------------------------------------------------------
	public void resizeClientBuffers(int vertexCapacity, boolean color, boolean texture)
	{

		FloatBuffer oldVertexBuffer = vertexBuffer;
		FloatBuffer oldColorBuffer = colorBuffer;
		FloatBuffer oldTextureBuffer = textureBuffer;

		ByteBuffer bbVertex = ByteBuffer.allocateDirect(8*vertexCapacity);
		bbVertex.order(ByteOrder.nativeOrder());
		vertexBuffer = bbVertex.asFloatBuffer();
		vertexBuffer.limit(2*numVertices);

		if(color){
			ByteBuffer bbColor = ByteBuffer.allocateDirect(16*vertexCapacity);
			bbColor.order(ByteOrder.nativeOrder());
			colorBuffer = bbColor.asFloatBuffer();
			colorBuffer.limit(4*numVertices);
		}
		
		if(texture){
	
			ByteBuffer bbTexture = ByteBuffer.allocateDirect(8*vertexCapacity);
			bbTexture.order(ByteOrder.nativeOrder());
			textureBuffer = bbTexture.asFloatBuffer();
			textureBuffer.limit(2*numVertices);
		}
		
		if(numVertices>0){

			if(oldVertexBuffer!=null){
				oldVertexBuffer.position(0);
				vertexBuffer.put(oldVertexBuffer);
				vertexBuffer.position(0);
			}

			if(oldColorBuffer!=null && color){
				oldColorBuffer.position(0);
				colorBuffer.put(oldColorBuffer);
				colorBuffer.position(0);
			}
			
			if(oldTextureBuffer!=null && texture){
				oldTextureBuffer.position(0);
				textureBuffer.put(oldTextureBuffer);
				textureBuffer.position(0);
			}

		}

	}
	
	//----------------------------------------------------------------------------
	public static void enableRendering(GL11 gl, boolean color, boolean texture)
	{

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		if(color){ gl.glEnableClientState(GL10.GL_COLOR_ARRAY); }
		if(texture){ gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); }

	}
	//----------------------------------------------------------------------------
	public static void disableRendering(GL11 gl)
	{

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	}

	//----------------------------------------------------------------------------
}
