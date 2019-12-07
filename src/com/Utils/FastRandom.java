package com.Utils;

public class FastRandom {

	
	private long val;
	public static final float INV_MAX_LONG = 1.0f/Long.MAX_VALUE;  
	
	public void setSeed(long seed)
	{
		val = seed;
	}
	
	
	public long nextLong()
	{
		val ^= (val << 21);
		val ^= (val >>> 35);
		val ^= (val << 4);
		
		return val;
	}

	public long nextPositiveLong()
	{
		val ^= (val << 21);
		val ^= (val >>> 35);
		val ^= (val << 4);
		
		return Math.abs(val);
	}

	
	public long nextLong(int n)
	{
		return nextPositiveLong();
	}
	
	public int nextInt(int n)
	{
		return (int)(nextPositiveLong()%n);
	}
	
	public float nextFloat()
	{
		return ((float)nextPositiveLong())*INV_MAX_LONG;
	}	


	
}
