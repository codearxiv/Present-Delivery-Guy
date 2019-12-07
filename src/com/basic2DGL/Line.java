package com.basic2DGL;

import javax.microedition.khronos.opengles.GL11;




public class Line extends ShapeVertexOrder
{


	//----------------------------------------------------------------------------
	public Line(GL11 gl,float r, float g, float b, float a)
	{
		super(gl,GL11.GL_LINES);
		
		numVertices = 2;
		
		resizeClientBuffers(2,true,false);
		
		setColor(r,g,b,a);
		

	}
	//----------------------------------------------------------------------------
	public void setVertices(float x1, float y1, float x2, float y2)
	{
		vertexBuffer.position(0);
		vertexBuffer.put(x1);
		vertexBuffer.put(y1);
		vertexBuffer.put(x2);
		vertexBuffer.put(y2);
		vertexBuffer.position(0);
		
	}
	//----------------------------------------------------------------------------
	public void setColor(float r, float g, float b, float a)
	{
		colorBuffer.position(0);
		colorBuffer.put(r);
		colorBuffer.put(g);
		colorBuffer.put(b);
		colorBuffer.put(a);
		colorBuffer.put(r);
		colorBuffer.put(g);
		colorBuffer.put(b);
		colorBuffer.put(a);
		colorBuffer.position(0);
		
	}
	//----------------------------------------------------------------------------

}