package com.basic2DPhys;



import java.util.BitSet;

import com.Utils.FSItr;
import com.Utils.FSList;
import com.basic2DObj.CompoundObj;
import com.basic2DObj.SimpleObj;


public class SpacePartition {

	private float posX;
	private float posY;	
	private float dim;
	int boundedBits;
	
	private SpacePartition[] nodes = null;
	
	public FSList<BoundingBox> members = new FSList<BoundingBox>();
	
	
	//----------------------------------------------------------------------------
	public SpacePartition(int depth, float dim, float x, float y, int boundedBits)
	{
		
		if(depth>0){
			
			this.boundedBits = boundedBits;
			
			nodes = new SpacePartition[4];
			
			float halfDim = 0.5f*dim;
			
			nodes[0] = new SpacePartition(depth-1,halfDim,x+halfDim,y+halfDim,boundedBits|12);
			nodes[1] = new SpacePartition(depth-1,halfDim,x-halfDim,y+halfDim,boundedBits|9);
			nodes[2] = new SpacePartition(depth-1,halfDim,x-halfDim,y-halfDim,boundedBits|3);
			nodes[3] = new SpacePartition(depth-1,halfDim,x+halfDim,y-halfDim,boundedBits|6);
		}
		

		
	}
	//----------------------------------------------------------------------------
	public void insert(BoundingBox bb)
	{
		insert(bb,bb.getPosX(),bb.getPosY());
		
	}
	//----------------------------------------------------------------------------	
	
	public void insert(BoundingBox bb, float x, float y)
	{
		
		if( nodes!=null ){
			
			if( y-bb.height >= posY ){
				
				if( x-bb.width >= posX ){
									
					nodes[0].insert(bb,x,y); 
					return;
					
				}
				else if( x+bb.width < posX ){
	
					nodes[1].insert(bb,x,y); 
					return;
				}
				
			}
			else if( y+bb.height < posY ){
				
				if( x-bb.width >= posX ){
					
					nodes[3].insert(bb,x,y); 
					return;
				
				}
				else if( x+bb.width < posX ){
					
					nodes[2].insert(bb,x,y); 
					return;
				}			
			}
		}

			
		members.push(bb);
		bb.location = members.firstItr();
	
					
	}

	//----------------------------------------------------------------------------	
	public void insert(SimpleObj gObj)
	{			
		insert(gObj.bounds);	
	}

	//----------------------------------------------------------------------------	
	public void insert(SimpleObj gObj, float x, float y)
	{			
		insert(gObj.bounds,x,y);
	}	
	//----------------------------------------------------------------------------
	public static void remove(SimpleObj gObj)
	{
		if( gObj.bounds.location!=null ){
			gObj.bounds.location.remove();
			gObj.bounds.location = null;
		}
	}
	//----------------------------------------------------------------------------	
	public void insert(CompoundObj gObj, float x, float y)
	{			
		for(int i=0; i<gObj.bounds.length; i++){
			insert(gObj.bounds[i],x,y);
		}
	
	}
	//----------------------------------------------------------------------------	
	public void insert(CompoundObj gObj)
	{			
		for(int i=0; i<gObj.bounds.length; i++){
			insert(gObj.bounds[i]);
		}
	
	}
	//----------------------------------------------------------------------------
	public  void remove(CompoundObj gObj)
	{
		for(int i=0;i<gObj.bounds.length;i++){
			if( gObj.bounds[i].location!=null ){
				gObj.bounds[i].location.remove();
				gObj.bounds[i].location = null;
			}
		}
	}
	//----------------------------------------------------------------------------
	public void remove(CompoundObj gObj, int i)
	{
		if( gObj.bounds[i].location!=null ){
			gObj.bounds[i].location.remove();
			gObj.bounds[i].location = null;
		}
	}

	//----------------------------------------------------------------------------
	//----------------------------------------------------------------------------	
	public void checkVacancy(BoundingBox bb, FSList<BoundingBox> collisions)
	{		
		checkVacancy(bb,bb.getPosX(),bb.getPosY(),collisions);		
	}
	
	//----------------------------------------------------------------------------
	public void checkVacancy(BoundingBox bb, float x, float y, FSList<BoundingBox> collisions)
	{

		FSItr<BoundingBox> itr = members.firstItr();
	
		BoundingBox currentbb;
		
		while( !itr.atHeader() ){
			
			currentbb = itr.get();
			
			if( bb != currentbb ){
				if( Math.abs(y-currentbb.getPosY()) <= bb.height+currentbb.height && 
					Math.abs(x-currentbb.getPosX()) <= bb.width+currentbb.width ){
				
					collisions.enqueue(currentbb);
				}
			}
			itr.advance();
		}
	
		
		
		if( nodes!=null ){
			
			if( y+bb.height >= posY ){
				
				if( x+bb.width >= posX ){ 
					nodes[0].checkVacancy(bb,x,y,collisions); 
				}
				
				if( x-bb.width <= posX ){
					nodes[1].checkVacancy(bb,x,y,collisions); 
				}
				
			}
			
			if( y-bb.height <= posY ){
				
				if( x+bb.width >= posX ){ 
					nodes[3].checkVacancy(bb,x,y,collisions); 
				}
				
				if( x-bb.width <= posX ){
					nodes[2].checkVacancy(bb,x,y,collisions); 
				}
			
			}
			
		}
		
		
		
	}
	//----------------------------------------------------------------------------	
	/*
	
	
	public void checkVacancy(GameObject gObj, FSList<BoundingBox> collisions)
	{			
		for(int i=0; i<gObj.bounds.length; i++){
			checkVacancy(gObj.bounds[i],collisions);
		}
	
	}	
	//----------------------------------------------------------------------------	
	public void checkVacancy(GameObject gObj, float x, float y, FSList<BoundingBox> collisions)
	{			
		for(int i=0; i<gObj.bounds.length; i++){
			checkVacancy(gObj.bounds[i],x+gObj.bounds[i].offsetX,y+gObj.bounds[i].offsetY,collisions);
		}
	
	}
	*/	
	//----------------------------------------------------------------------------	
	public void checkVacancy(SimpleObj gObj, FSList<BoundingBox> collisions)
	{			
		checkVacancy(gObj.bounds,collisions);
	}	
	//----------------------------------------------------------------------------	
	public void checkVacancy(SimpleObj gObj, float x, float y, FSList<BoundingBox> collisions)
	{			
		//checkVacancy(gObj.bounds,x+gObj.bounds.offsetX,y+gObj.bounds.offsetY,collisions);
		checkVacancy(gObj.bounds,x,y,collisions);
	}
	//----------------------------------------------------------------------------
	public static boolean inBounds(BoundingBox bb, SpacePartition location)
	{
		return inBounds(bb,bb.getPosX(),bb.getPosY(),location);
	}
	//----------------------------------------------------------------------------
	public static boolean inBounds(BoundingBox bb, float x, float y, SpacePartition location)
	{	
		
		return  ( ( (location.boundedBits&2)==0 ) || ( y+bb.height <= (location.posY+location.dim) ) ) && 
				( ( (location.boundedBits&8)==0 ) || ( y-bb.height >= (location.posY-location.dim) ) ) && 
				( ( (location.boundedBits&1)==0 ) || ( x+bb.width <= (location.posX+location.dim) ) ) && 
				( ( (location.boundedBits&4)==0 ) || ( x-bb.width >= (location.posX-location.dim) ) );			
	}
	//----------------------------------------------------------------------------
	
}
