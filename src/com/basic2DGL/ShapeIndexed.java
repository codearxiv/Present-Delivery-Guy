package com.basic2DGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;



public class ShapeIndexed extends Shape
{
	
	
	protected int[] handles = null;

	
	protected ShortBuffer indexBuffer = null;
	protected int numIndices = 0;

	//----------------------------------------------------------------------------
	protected ShapeIndexed(){}
	
	//----------------------------------------------------------------------------
	public ShapeIndexed(GL11 gl, int mode)
	{

		this.mode = mode;

		handles = new int[4];
		gl.glGenBuffers(4,handles,0);
		
	}
	//----------------------------------------------------------------------------
	public ShapeIndexed(GL11 gl, int mode, int vertexCapacity, int indexCapacity, boolean color, boolean texture)
	{

		resizeClientBuffers(vertexCapacity,color,texture);
		resizeClientIndexBuffer(indexCapacity);

		this.mode = mode;

		handles = new int[4];
		gl.glGenBuffers(4,handles,0);
		
	}
	//----------------------------------------------------------------------------
	public ShapeIndexed(GL11 gl, int mode, float[] vertices, float[] colors, float[] textures, short[] indices, Float scale)
	{

		super(gl,mode,vertices,colors,textures,scale);

		mapToClientIndexBuffer(indices);

		handles = new int[4];
		gl.glGenBuffers(4,handles,0);


	}
	//----------------------------------------------------------------------------
	public void mapToBuffer(GL11 gl,boolean vertex, boolean color, boolean texture, boolean index)
	{
		if(vertex){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[0]);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, 8*numVertices,vertexBuffer, GL11.GL_STATIC_DRAW);
		}
		if(color){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[1]);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, 16*numVertices,colorBuffer, GL11.GL_STATIC_DRAW);
		}

		if(texture){
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, handles[2]);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, 8*numVertices, textureBuffer, GL11.GL_STATIC_DRAW);
		}
		if(index){
			gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, handles[3]);
			gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, 2*numIndices, indexBuffer, GL11.GL_STATIC_DRAW);
		}
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
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
		
		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, handles[3]);


	}
	//----------------------------------------------------------------------------
	public void unbindFromBuffer(GL11 gl)
	{
		
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);


	}
	//----------------------------------------------------------------------------
	public void deleteFromBuffer(GL11 gl)
	{
		gl.glDeleteBuffers(4,handles,0);
		
	}
	//----------------------------------------------------------------------------
	public final void draw(GL11 gl)
	{
		gl.glDrawElements(mode, numIndices, GL11.GL_UNSIGNED_SHORT, 0);
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
		
		gl.glDrawElements(mode, numIndices, GL11.GL_UNSIGNED_SHORT, indexBuffer);
		

	}
	//----------------------------------------------------------------------------
	public void put(ShapeIndexed shape, float posX, float posY, float posZ, boolean color, boolean texture)
	{

		int oldNumVertices = numVertices;
		numVertices += shape.numVertices;

		if( 2*numVertices > vertexBuffer.capacity() ){
			resizeClientBuffers(2*numVertices,color,texture);
		}

		int oldNumIndices = numIndices;
		numIndices += shape.numIndices;

		if( numIndices > indexBuffer.capacity() ){
			resizeClientIndexBuffer(2*numIndices);
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

		indexBuffer.limit(numIndices);
		indexBuffer.position(oldNumIndices);
		shape.indexBuffer.position(0);
		for(int i=0; i<shape.numIndices; i++){
			indexBuffer.put( (short)(shape.indexBuffer.get(i) + oldNumVertices) );
		}
		indexBuffer.position(0);
		shape.indexBuffer.position(0);


	}
	//----------------------------------------------------------------------------
	public void mapToClientIndexBuffer(short[] indices)
	{

		ByteBuffer bbIndice = ByteBuffer.allocateDirect(2*indices.length);
		bbIndice.order(ByteOrder.nativeOrder());
		indexBuffer = bbIndice.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);

		numIndices = indices.length;

	}
	//----------------------------------------------------------------------------
	public void resizeClientIndexBuffer(int indexCapacity)
	{

		ShortBuffer oldIndexBuffer = indexBuffer;

		ByteBuffer bbIndex = ByteBuffer.allocateDirect(2*indexCapacity);
		bbIndex.order(ByteOrder.nativeOrder());
		indexBuffer = bbIndex.asShortBuffer();
		indexBuffer.limit(numIndices);

		if(numIndices>0){

			if(oldIndexBuffer!=null){
				oldIndexBuffer.position(0);
				indexBuffer.put(oldIndexBuffer);
				indexBuffer.position(0);
			}

		}

	}
	//----------------------------------------------------------------------------

	public static ShapeIndexed asIndexed(GL11 gl, Shape shape, boolean color, boolean texture)
	{

		ShapeIndexed shapeIndexed = new ShapeIndexed(gl,shape.mode,shape.numVertices,shape.numVertices,color,texture);

		shape.vertexBuffer.position(0);
		shapeIndexed.vertexBuffer.put(shape.vertexBuffer);
		shape.vertexBuffer.position(0);
		shapeIndexed.vertexBuffer.position(0);

		if(color){
			shape.colorBuffer.position(0);
			shapeIndexed.colorBuffer.put(shape.colorBuffer);
			shape.colorBuffer.position(0);
			shapeIndexed.colorBuffer.position(0);
		}
		
		if(texture){
			shape.textureBuffer.position(0);
			shapeIndexed.textureBuffer.put(shape.textureBuffer);
			shape.textureBuffer.position(0);
			shapeIndexed.textureBuffer.position(0);
		}		
		
		

		for(int i=0; i<shape.numVertices; i++){
			shapeIndexed.indexBuffer.put((short)i);
		}
		shapeIndexed.indexBuffer.position(0);


		shapeIndexed.numVertices = shape.numVertices;
		shapeIndexed.numIndices = shape.numVertices;
		shapeIndexed.mode = shape.mode;

		return shapeIndexed;

	}
	//----------------------------------------------------------------------------

}
