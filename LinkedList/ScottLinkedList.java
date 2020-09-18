//https://www.youtube.com/watch?v=pBrz9HmjFOs

public class ScottLinkedList<E>
{
  private Node<E> head, temp, next;
  private int nIndex;

  public E set(int index, E e)
  {
    next = head;
    nIndex = 0;
    while (index != nIndex )
    {
      next = next.next();
      nIndex++;
    }
    next.setData(e);
    return e;
  }
  public void addFirst (E e)
  {
    temp = head;
    head = new Node<E>(e, temp);
  }
  public E getFirst()
  {
    return head.getData();
  }
  public E removeFirst()
  {
    head = head.next();
    return head.getData();
  }
  public void addLast(E e)
  {
    next = head;
    while (next.next() != null)
      next = next.next();
    next.setPointer(new Node<E>(e, null));
  }
  public E get(int index)
  {
    next = head;
    nIndex = 0;
    while (index != nIndex)
    {
      next = next.next();
      nIndex++;
    }
    return next.getData();
  }
  public int size()
  {
    next = head;
    nIndex = 0;
    while (next != null)
    {
      nIndex++;
      next = next.next();
    }
    return nIndex;
  }
  public boolean add(E e)
  {
   if (head == null)
   	head = new Node<E>(e, null);
   	else
   	{
    next = head;
    while (next.next() != null)
      next = next .next();
    next.setPointer(new Node<E>(e, null));
   }
   return true;
  }
  public void clear()
  {
    head = null;
  }
  public E getLast()
  {
    next = head;
    while (next.next() != null)
      next = next.next();
    return next.getData();
  }
  public E remove()
  {
    next = head;
    while (next.next().next() != null)
      next = next.next();
    next.setPointer(null);
    return next.getData();
  }
  public boolean contains(Object o)
  {
    next = head;
    while (next.next() != null)
    {
     if (next.getData() == o)
       return true;
      next = next.next();
    }
    return false;
  }
  public int indexOf(Object o)
  {
    next = head;
    nIndex = 0;
    while (next.next() != null)
    {
     if (next.getData() == o)
       return nIndex;
      next = next.next();
      nIndex++;
    }
    return -1;
  }
  public void add(int index, E element)
  {
    next = head;
    nIndex = 0;
    while  ((index-1 != nIndex) && (index > 1))
    {
      next = next.next();
      nIndex++;
    }
    if (index == 0)
   	   head = new Node<E>(element, next);
   	else if (index == 1)
   		head.setPointer(next = new Node<E>(element, head.next()));
   	else if (index < 0)
		index = 0;
   	 else
       next.setPointer(new Node<E>(element, next.next()));
  }
  public E remove(int index)
  {
	 next = head;
	 nIndex = 0;
	 while ((index-1 != nIndex) && (index > 1))
	 {
		next = next.next();
		nIndex++;
	 }
	if (index == 0)
		head = head.next();
	else if (index == 1)
		head.setPointer(next.next().next());
	else if (index < 0)
		index = 0;
	else
		next.setPointer(next.next().next());
	return next.getData();
  }
}

class Node<E>
{
  private Node<E> pointTo;
  private E element;

  public Node(E element, Node<E> pointTo)
  {
    this.pointTo = pointTo;
    this.element = element;
  }

  public E getData()
  {
    return element;
  }
  public Node<E> next()
  {
    return pointTo;
  }
  public void setPointer(Node<E> e)
  {
    pointTo = e;
  }
  public void setData(E e)
  {
    element = e;
  }
}
