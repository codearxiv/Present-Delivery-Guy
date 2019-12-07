package com.basic2DObj;

import javax.microedition.khronos.opengles.GL11;

import com.basic2DGL.Sprite;

public class DrawableObj extends EmptyObj{


	//public Sprite sprite = null;
	public int textureIndex = 0;
	

	
	
	
	//----------------------------------------------------------------------------
	public DrawableObj() 
	{
	}	
	//----------------------------------------------------------------------------
	public DrawableObj(float x, float y, float vx, float vy, boolean active)
	{
		
		super(x,y,vx,vy,active);
		
		//sprite = s;
		
	}

	//----------------------------------------------------------------------------
	/*
	public void setSprite(Sprite s) 
	{
		sprite = s;
	}
	//----------------------------------------------------------------------------
	
	public void setSprite(Sprite s, int i) 
	{
		sprites[i] = s;
	}
	//----------------------------------------------------------------------------
	public void setTextureIndex(int j, int i) 
	{
		spriteData[i].textureIndex = j;
	}
	//----------------------------------------------------------------------------
	public int getTextureIndex(int i) 
	{
		return spriteData[i].textureIndex;
	}

	//----------------------------------------------------------------------------
	public void draw(GL11 gl)
	{		
		gl.glPushMatrix();
		gl.glTranslatef(posX,posY,0);
		
		for(int i=0; i<sprites.length; i++){
			
			gl.glPushMatrix();
			gl.glTranslatef(spriteData[i].offsetX,spriteData[i].offsetY,0);

			sprites[i].rectangle.bindToBuffer(gl,true,true);
			sprites[i].draw(gl,spriteData[i].textureIndex);
			
			gl.glPopMatrix();
		}		

		gl.glPopMatrix();
		
	}
	//----------------------------------------------------------------------------
	
	public void drawClientSide(GL11 gl, boolean color, boolean texture)
	{
		gl.glPushMatrix();
		gl.glTranslatef(posX,posY,0);
		
		for(int i=0; i<sprites.length; i++){
			
			gl.glPushMatrix();
			gl.glTranslatef(spriteData[i].offsetX,spriteData[i].offsetY,0);
	
			sprites[i].drawClientSide(gl,spriteData[i].textureIndex,color,texture);
			
			gl.glPopMatrix();
		}		

		gl.glPopMatrix();

	}
	*/
	//----------------------------------------------------------------------------
	public void setTextureIndex(int i) 
	{
		textureIndex = i;
	}
	//----------------------------------------------------------------------------
	public int getTextureIndex() 
	{
		return textureIndex;
	}
	//----------------------------------------------------------------------------
	public final void draw(GL11 gl, Sprite sprite)
	{		
		gl.glPushMatrix();
		gl.glTranslatef(posX,posY,0);
				
		sprite.draw(gl,textureIndex);
			
		gl.glPopMatrix();
		
	}
	//----------------------------------------------------------------------------
	public void drawClientSide(GL11 gl, Sprite sprite, int textureIndex, boolean color, boolean texture)
	{
		gl.glPushMatrix();
		gl.glTranslatef(posX,posY,0);
					
		sprite.drawClientSide(gl,textureIndex,color,texture);	

		gl.glPopMatrix();

	}
	//----------------------------------------------------------------------------
	public void drawClientSide(GL11 gl, Sprite sprite, boolean color, boolean texture)
	{
		gl.glPushMatrix();
		gl.glTranslatef(posX,posY,0);
					
		sprite.drawClientSide(gl,textureIndex,color,texture);	

		gl.glPopMatrix();

	}
	//----------------------------------------------------------------------------
	public void drawClientSide(GL11 gl, Sprite sprite, float angle, int textureIndex, boolean color, boolean texture)
	{
		gl.glPushMatrix();
		gl.glTranslatef(posX,posY,0);
		gl.glRotatef(-angle, 0, 0, 1);			
		sprite.drawClientSide(gl,textureIndex,color,texture);	

		gl.glPopMatrix();

	}
	//----------------------------------------------------------------------------
	public void drawClientSide(GL11 gl, Sprite sprite, float angle, boolean color, boolean texture)
	{
		gl.glPushMatrix();
		gl.glTranslatef(posX,posY,0);
		gl.glRotatef(-angle, 0, 0, 1);			
		sprite.drawClientSide(gl,textureIndex,color,texture);	

		gl.glPopMatrix();

	}
	//---------------------------------------------------------------------------- 
	
	
	
	
	
	
}
