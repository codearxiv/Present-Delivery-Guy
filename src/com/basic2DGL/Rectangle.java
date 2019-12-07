package com.basic2DGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL11;




public class Rectangle extends ShapeIndexed
{

/*
	static float[] vertices = {
		// in counterclockwise order:
		1.0f, 1.0f,
		-1.0f, 1.0f,
		-1.0f, -1.0f,
		1.0f, -1.0f
	};
*/
/*
	static float[] colors = {
		0.163671875f, 0.16953125f, 0.22265625f, 1.0f,
		0.63671875f, 0.176953125f, 0.22265625f, 1.0f,
		0.63671875f, 0.76953125f, 0.122265625f, 1.0f,
		0.63671875f, 0.76953125f, 0.22265625f, 1.0f
	};
*/
	static short [] indices = {0,1,2, 0,2,3};

	
	//----------------------------------------------------------------------------
	public Rectangle(GL11 gl, float width, float height, float tileX, float tileY, float[] colors)
	{	
	
		super(gl,GL11.GL_TRIANGLES);

		float[] vertices = new float[8];		
		vertices[0] = width;
		vertices[1] = height;
		vertices[2] = -width;
		vertices[3] = height;
		vertices[4] = -width;
		vertices[5] = -height;
		vertices[6] = width;
		vertices[7] = -height;

		
		float[] textures = {
			tileX, tileY,
			0.0f, tileY, 
			0.0f, 0.0f, 
			tileX, 0.0f
		};
		
		if( colors!=null){ mapToClientBuffers(vertices,colors,textures,null); }
		else{ mapToClientBuffers(vertices,null,textures,null); }
			
		mapToClientIndexBuffer(indices);
				
		
	}

	//----------------------------------------------------------------------------
	public void shiftTexturePosX(float x)
	{
			float limitX = 2*Math.abs( textureBuffer.get(0)-textureBuffer.get(2) );
			
			if( textureBuffer.get(0)+x > limitX ){ x -= limitX;	}
			else if( textureBuffer.get(0)+x < -limitX ){ x += limitX; } 
			
			 textureBuffer.put(0,textureBuffer.get(0)+x);
			 textureBuffer.put(2,textureBuffer.get(2)+x);
			 textureBuffer.put(4,textureBuffer.get(4)+x);
			 textureBuffer.put(6,textureBuffer.get(6)+x);
			
	}
			 
	//----------------------------------------------------------------------------
	public void shiftTexturePosY(float y)
	{
						
			float limitY = 2*Math.abs( textureBuffer.get(1)-textureBuffer.get(5) );
			
			if( textureBuffer.get(1)+y > limitY ){ y -= limitY;	}
			else if( textureBuffer.get(1)+y < -limitY ){ y += limitY; } 
			
			 textureBuffer.put(1,textureBuffer.get(1)+y);
			 textureBuffer.put(3,textureBuffer.get(3)+y);
			 textureBuffer.put(5,textureBuffer.get(5)+y);
			 textureBuffer.put(7,textureBuffer.get(7)+y);
		 
			 
	}
	//----------------------------------------------------------------------------
	public void setTexturePosX(float x)
	{
			float tileX = Math.abs( textureBuffer.get(0)-textureBuffer.get(2) );
					
			 textureBuffer.put(0,tileX+x);
			 textureBuffer.put(2,x);
			 textureBuffer.put(4,x);
			 textureBuffer.put(6,tileX+x);		
			
	}
	//----------------------------------------------------------------------------
	public void setTexturePosY(float y)
	{
			float tileY = Math.abs( textureBuffer.get(1)-textureBuffer.get(5) );
					
			 textureBuffer.put(1,tileY+y);
			 textureBuffer.put(3,tileY+y);
			 textureBuffer.put(5,y);
			 textureBuffer.put(7,y);			
	}			 
	//----------------------------------------------------------------------------

}
