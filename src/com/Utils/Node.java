package com.Utils;


public class Node<E>
{

	public E element;	
	public Node<E> next;		
	public Node<E> prev;		


	public Node()
	{
		element = null;
		next = this;
		prev = this;

	}
	
	public Node(E e, Node<E> n, Node<E> p)
	{
		element = e;
		next = n;
		prev = p;

	}
	
	
	public void remove()
	{
		
		prev.next = next;
		next.prev = prev;
		
	}
	
	
}

