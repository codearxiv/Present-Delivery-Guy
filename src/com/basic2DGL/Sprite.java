package com.basic2DGL;

import javax.microedition.khronos.opengles.GL11;


public class Sprite {

	
	public Rectangle rectangle;	
	public int[] textureIDs;
	
	
	
	//----------------------------------------------------------------------------
	public Sprite(Rectangle r, int[] t)
	{			
		rectangle = r;
		textureIDs = t;	
	}
	//----------------------------------------------------------------------------
	public Sprite(Rectangle r, int t)
	{
			
		rectangle = r;
		textureIDs = new int[1];
		textureIDs[0] = t;
	
	}
	//----------------------------------------------------------------------------
	public Sprite(GL11 gl, float w, float h, float tileX, float tileY, int[] t, float[] colors)
	{			
		rectangle = new Rectangle(gl,w,h,tileX,tileY,colors);
		textureIDs = t;
	}
	//----------------------------------------------------------------------------
	public Sprite(GL11 gl, float w, float h, float tileX, float tileY, int t, float colors[])
	{			
		rectangle = new Rectangle(gl,w,h,tileX,tileY,colors);
		textureIDs = new int[1];
		textureIDs[0] = t;
	}
	//----------------------------------------------------------------------------
	public void bindToBuffer(GL11 gl, boolean color, boolean texture)
	{
		rectangle.bindToBuffer(gl,color,texture);
	
	}
	//----------------------------------------------------------------------------
	public void mapToBuffer(GL11 gl, boolean color, boolean texture)
	{
		rectangle.mapToBuffer(gl,true,color,texture,true);
	
	}
	//----------------------------------------------------------------------------
	public void draw(GL11 gl, Integer textureIndex)
	{		
			
		if( textureIndex!=null ){
			gl.glBindTexture(gl.GL_TEXTURE_2D, textureIDs[textureIndex]);
		}
		rectangle.draw(gl);
		
	
	}
	//----------------------------------------------------------------------------
	public void drawClientSide(GL11 gl, Integer textureIndex, boolean color, boolean texture)
	{
		
		if( textureIndex!=null ){
			gl.glBindTexture(gl.GL_TEXTURE_2D, textureIDs[textureIndex]);
		}
		rectangle.drawClientSide(gl,color,texture);
			
	}
	//----------------------------------------------------------------------------
	public void shiftTexturePosX(float x)
	{
		rectangle.shiftTexturePosX(x);	
	}	
	//----------------------------------------------------------------------------
	public void shiftTexturePosY(float y)
	{
		rectangle.shiftTexturePosY(y);	
	}	
	//----------------------------------------------------------------------------
	public void setTexturePosX(float x)
	{
		rectangle.setTexturePosX(x);	
	}	
	//----------------------------------------------------------------------------
	public void setTexturePosY(float y)
	{
		rectangle.setTexturePosY(y);	
	}	
	//----------------------------------------------------------------------------

}
