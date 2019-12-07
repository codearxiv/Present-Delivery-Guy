package com.basic2DObj;

import javax.microedition.khronos.opengles.GL11;

import com.basic2DGL.Sprite;
import com.basic2DPhys.BoundingBox;


public class Text2D extends DrawableObj{

	String text = null;
	
	private float[] charWidthFactors = null;
	
	private float spacing;
	
	private float textCenterX = 0f;
	
	
	//----------------------------------------------------------------------------
	public Text2D(float x, float y, float sp, String txt, float[] factors) 
	{
		super(x,y,0f,0f,true);
		
		spacing = sp;
						
		charWidthFactors = factors;
		
		text = txt;

		computeTextCenterX();
		
	}
	//----------------------------------------------------------------------------
	public void setText(String txt)
	{
		text = txt;
	
		computeTextCenterX();
	}
	//----------------------------------------------------------------------------
	public void setSpacing(float sp)
	{
		spacing = sp;
		
		computeTextCenterX();
	}
	//----------------------------------------------------------------------------
	public void incrementSpacing(float inc)
	{
		setSpacing(spacing + inc);
		
	}
	//----------------------------------------------------------------------------
	public float getSpacing()
	{
		return spacing; 
	}
	//----------------------------------------------------------------------------
	private void computeTextCenterX()
	{
		
		if( text!=null ){
			int numGaps = text.length()-1;
			
			if( charWidthFactors == null ){ 
				textCenterX = 0.5f*spacing*numGaps; 
			}
			else{ 
				
				float widthFactorLength = 0f;
				
				for(int i=0;i<numGaps;i++){
					widthFactorLength += charWidthFactors[ charToIndex(text.charAt(i)) ];
				}
				textCenterX = 0.5f*spacing*widthFactorLength;
			}
			
			
		}
	}
	//----------------------------------------------------------------------------
	public void initCharWidthArray()
	{
		charWidthFactors = new float[68];
		
		for(int i=0;i<charWidthFactors.length;i++){ charWidthFactors[i] = 1f; }
	}
	//----------------------------------------------------------------------------
	public void setCharWidthArray(float[] factors)
	{
		charWidthFactors = factors;		
	}
	//----------------------------------------------------------------------------
	public void setCharWidthFactor(char c, float width)
	{
		if( charWidthFactors != null ){ charWidthFactors[ charToIndex(c) ] = width;  }		
	}
	//----------------------------------------------------------------------------
	private float widthFactorAt(int i)
	{
		return charWidthFactors[charToIndex(text.charAt(i))];
	}
	//----------------------------------------------------------------------------

	public void draw(GL11 gl, Sprite sprite, boolean center)
	{
		if(text!=null){		
			
			gl.glPushMatrix();

			
			if(center){ gl.glTranslatef(posX-textCenterX,posY,0); }
			else{ gl.glTranslatef(posX,posY,0); }	
			
			int numGaps = text.length()-1;
			
			if( charWidthFactors == null ){
			
				for(int i=0;i<numGaps;i++){						
					sprite.drawClientSide(gl,charToIndex(text.charAt(i)),true,true);
					gl.glTranslatef(spacing,0,0);					
				}
						
			}
			else{
				
				for(int i=0;i<numGaps;i++){
					sprite.drawClientSide(gl,charToIndex(text.charAt(i)),true,true);

					float shiftX = 0.5f*(widthFactorAt(i)+widthFactorAt(i+1))*spacing;
					gl.glTranslatef(shiftX,0,0);	
				}
				
			}
			
			if( text.length()>=1 ){
				sprite.drawClientSide(gl,charToIndex(text.charAt(numGaps)),true,true);			
			}
			
	
			gl.glPopMatrix();
		}
	}
	//----------------------------------------------------------------------------
	public static int charToIndex(char c)
	{
 
		switch(c){
			case '0': return 0;
			case '1': return 1;
			case '2': return 2;
			case '3': return 3;
			case '4': return 4;
			case '5': return 5;
			case '6': return 6;
			case '7': return 7;
			case '8': return 8;
			case '9': return 9;
			case 'a': return 10;
			case 'b': return 11;
			case 'c': return 12;
			case 'd': return 13;
			case 'e': return 14;
			case 'f': return 15;
			case 'g': return 16;
			case 'h': return 17;
			case 'i': return 18;
			case 'j': return 19;
			case 'k': return 20;
			case 'l': return 21;
			case 'm': return 22;
			case 'n': return 23;
			case 'o': return 24;
			case 'p': return 25;
			case 'q': return 26;
			case 'r': return 27;
			case 's': return 28;
			case 't': return 29;
			case 'u': return 30;
			case 'v': return 31;
			case 'w': return 32;
			case 'x': return 33;
			case 'y': return 34;
			case 'z': return 35;
			case 'A': return 36;
			case 'B': return 37;
			case 'C': return 38;
			case 'D': return 39;
			case 'E': return 40;
			case 'F': return 41;
			case 'G': return 42;
			case 'H': return 43;
			case 'I': return 44;
			case 'J': return 45;
			case 'K': return 46;
			case 'L': return 47;
			case 'M': return 48;
			case 'N': return 49;
			case 'O': return 50;
			case 'P': return 51;
			case 'Q': return 52;
			case 'R': return 53;
			case 'S': return 54;
			case 'T': return 55;
			case 'U': return 56;
			case 'V': return 57;
			case 'W': return 58;
			case 'X': return 59;
			case 'Y': return 60;
			case 'Z': return 61;
			case ' ': return 62;
			case '.': return 63;
			case ',': return 64;
			case '!': return 65;
			case '?': return 66;
			case ':': return 67;
			default: return 62;
		}
		
	}
	//----------------------------------------------------------------------------
}
