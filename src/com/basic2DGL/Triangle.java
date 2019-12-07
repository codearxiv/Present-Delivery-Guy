
package com.basic2DGL;

import javax.microedition.khronos.opengles.GL11;




public class Triangle extends ShapeVertexOrder
{

	static float[] vertices = {
		// in counterclockwise order:
		0.0f,  0.622008459f,
		-0.5f, -0.311004243f,
		0.5f, -0.311004243f
	};

	static float[] colors = {
		0.63671875f, 0.16953125f, 0.22265625f, 1.0f,
		0.63671875f, 0.76953125f, 0.22265625f, 1.0f,
		0.63671875f, 0.76953125f, 0.22265625f, 1.0f
	};

	static float[] textures = {
		0.5f, 1.0f, 
		0.0f, 0.0f, 
		1.0f, 0.0f
	};
	//----------------------------------------------------------------------------
	public Triangle(GL11 gl)
	{
		super(gl,GL11.GL_TRIANGLES,vertices,colors,textures,null);
	}
	//----------------------------------------------------------------------------
	public Triangle(GL11 gl, Float scale)
	{
		super(gl,GL11.GL_TRIANGLES,vertices,colors,textures,scale);

	}

	//----------------------------------------------------------------------------

}
