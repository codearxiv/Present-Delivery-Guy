package com.Utils;





public class FSList<E>
{

	private Node<E> header;

	
	//----------------------------------------------------------------------------	
	public FSList()
	{
		header = new Node<E>();
	}

	//----------------------------------------------------------------------------	
	public boolean isEmpty()
	{
		return header.next == header;

	}
	//----------------------------------------------------------------------------	
	public void clear()
	{				
		header.next = header;
		header.prev = header;	
	}
	
	//----------------------------------------------------------------------------
	public void push(E x)
	{
		Node<E> newNode = new Node<E>(x,header.next,header);

		(header.next).prev = newNode;
		header.next = newNode;
		
	}

	//----------------------------------------------------------------------------	
	public void pop()
	{
		((header.next).next).prev = header; 
		header.next = (header.next).next;
		
	}
	
	
	//----------------------------------------------------------------------------	
	public void enqueue(E x)
	{
		Node<E> newNode = new Node<E>(x,header,header.prev);

		(header.prev).next = newNode;
		header.prev = newNode;
		
	}
	
	//----------------------------------------------------------------------------
	public void dequeue()
	{
		((header.prev).prev).next = header; 
		header.prev = (header.prev).prev;
		
	}

	
	//----------------------------------------------------------------------------	
	public Node<E> firstNode() 
	{
		return header.next;
	}
	
	//----------------------------------------------------------------------------	
	public Node<E> lastNode() 
	{
		return header.prev;
	}

	//----------------------------------------------------------------------------	
	public FSItr<E> firstItr() 
	{
		return new FSItr<E>(header,header.next);
	}

	//----------------------------------------------------------------------------	
	public FSItr<E> lastItr() 
	{
		return new FSItr<E>(header,header.prev);
	}	
	//----------------------------------------------------------------------------	
	public FSItr<E> headerItr() 
	{
		return new FSItr<E>(header,header);
	}
	//----------------------------------------------------------------------------



	
	
}


