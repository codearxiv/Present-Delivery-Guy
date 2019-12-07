package com.Utils;



public class FSItr<E>
{
	private Node<E> header; 
	private Node<E> current;    
	
	//----------------------------------------------------------------------------	
	public FSItr(Node<E> h, Node<E> c)
	{
		current = c;
		header = h;	
	}
	//----------------------------------------------------------------------------
	public boolean atHeader()
	{
		return current == header;
	}

	//----------------------------------------------------------------------------	
	public boolean hasNext()
	{
		return current.next != header;
	}
	//----------------------------------------------------------------------------
	public boolean hasPrev()
	{
		return current.prev != header;
	}

	//----------------------------------------------------------------------------	
	public void advance()
	{
		current = current.next;
	}
	//----------------------------------------------------------------------------	
	public void retreat()
	{
		current = current.prev;
	}
	
	//----------------------------------------------------------------------------	
	public void insert(E x)
	{
		Node<E> newNode = new Node<E>(x,current.next,current);

		(current.next).prev = newNode;
		current.next = newNode;
						
		
	}
	//----------------------------------------------------------------------------	
	public void insertBehind(E x)
	{
		Node<E> newNode = new Node<E>(x,current,current.prev);

		(current.prev).next = newNode;
		current.prev = newNode;					
		
	}
	
	//----------------------------------------------------------------------------	
	public void remove()
	{
		
		if( current!=header ){
	      
			(current.prev).next = current.next;
			(current.next).prev = current.prev;
			current = current.next;	

		}
	}
	//----------------------------------------------------------------------------	
	public Node<E> getHeader()
	{
		return header;
	}
	//----------------------------------------------------------------------------	
	public Node<E> getNode()
	{
		return current;
	}
	//----------------------------------------------------------------------------	
	public E get()
	{
		return current.element;
	}
	//----------------------------------------------------------------------------	
	public E getPrev()
	{
		return current.prev.element;
	}
	//----------------------------------------------------------------------------	
	public E getNext()
	{
		return current.prev.element;
	}
	//----------------------------------------------------------------------------

	
}

