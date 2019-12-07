package com.basic2DGL;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;




public class ShapeVertexOrder extends Shape
{

	protected int[] handles = null;


	//----------------------------------------------------------------------------
	protected ShapeVertexOrder(){}
	
	//----------------------------------------------------------------------------
	public ShapeVertexOrder(GL11 gl, int mode)
	{

		this.mode = mode;

		handles = new int[3];
		gl.glGenBuffers(3,handles,0);

	}
	//----------------------------------------------------------------------------
	public ShapeVertexOrder(GL11 gl, int mode, int vertexCapacity, boolean color, boolean texture)
	{
		resizeClientBuffers(vertexCapacity,color,texture);
		
		this.mode = mode;

		handles = new int[3];
		gl.glGenBuffers(3,handles,0);

	}
	//----------------------------------------------------------------------------
	public ShapeVertexOrder(GL11 gl, int mode, float[] vertices, float[] colors, float[] textures, Float scale)
	{
		super(gl,mode,vertices,colors,textures,scale);

		handles = new int[3];
		gl.glGenBuffers(3,handles,0);


	}
	//----------------------------------------------------------------------------
	public void mapToBuffer(GL11 gl, boolean vertex, boolean color, boolean texture)
	{

		if(vertex){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[0]);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, 8*numVertices, vertexBuffer, GL11.GL_STATIC_DRAW);
		}
		
		if(color){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[1]);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, 16*numVertices, colorBuffer, GL11.GL_STATIC_DRAW);
			
		}
		
		if(texture){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[2]);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, 8*numVertices, textureBuffer, GL11.GL_STATIC_DRAW);
		}
		
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

	}

	//----------------------------------------------------------------------------
	public void bindToBuffer(GL11 gl, boolean color, boolean texture)
	{


		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[0]);
		gl.glVertexPointer( 2, GL11.GL_FLOAT, 0, 0);

		if(color){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[1]);
			gl.glColorPointer( 4, GL11.GL_FLOAT, 0, 0);
		}
		if(texture){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[2]);
			gl.glTexCoordPointer( 2, GL11.GL_FLOAT, 0, 0);			
		}

	}
	//----------------------------------------------------------------------------
	public void unbindFromBuffer(GL11 gl)
	{		
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	}
	//----------------------------------------------------------------------------
	public void deleteFromBuffer(GL11 gl)
	{
		gl.glDeleteBuffers(3,handles,0);

	}
	//----------------------------------------------------------------------------
	public final void draw(GL11 gl)
	{
		gl.glDrawArrays( mode, 0, numVertices);

	}
	//----------------------------------------------------------------------------
	public void drawClientSide(GL11 gl, boolean color, boolean texture)
	{
		
		gl.glVertexPointer( 2, GL11.GL_FLOAT, 0, vertexBuffer);
				
		if(color){
			gl.glColorPointer( 4, GL11.GL_FLOAT, 0, colorBuffer);
		}
		if(texture){
			gl.glTexCoordPointer( 2, GL11.GL_FLOAT, 0, textureBuffer);
		}
		
		gl.glDrawArrays( mode, 0, numVertices);


	}
	//----------------------------------------------------------------------------
	public void put(Shape shape, float posX, float posY, boolean color, boolean texture)
	{
		int oldNumVertices = numVertices;
		numVertices += shape.numVertices;

		if( 2*numVertices > vertexBuffer.capacity() ){
			resizeClientBuffers(2*numVertices,color,texture);
		}


		vertexBuffer.limit(2*numVertices);
		vertexBuffer.position(2*oldNumVertices);
		shape.vertexBuffer.position(0);
		for(int i=0, limit=2*shape.numVertices; i<limit; i+=2){
			vertexBuffer.put( shape.vertexBuffer.get(i) + posX );
			vertexBuffer.put( shape.vertexBuffer.get(i+1) + posY );

		}
		vertexBuffer.position(0);
		shape.vertexBuffer.position(0);

		if(color){
			colorBuffer.limit(4*numVertices);
			colorBuffer.position(4*oldNumVertices);
			shape.colorBuffer.position(0);
			colorBuffer.put(shape.colorBuffer);
			colorBuffer.position(0);
			shape.colorBuffer.position(0);
		}

		if(texture){			
			textureBuffer.limit(2*numVertices);
			textureBuffer.position(2*oldNumVertices);
			shape.textureBuffer.position(0);
			textureBuffer.put(shape.textureBuffer);
			textureBuffer.position(0);
			shape.textureBuffer.position(0);
		}
	}

	//----------------------------------------------------------------------------
}
