package common.repository;

import java.util.*;
import java.lang.Object.*;
import java.lang.Throwable.*;
import java.lang.Exception.*;


/**
 * Concrete collection of elements.
 * @author Pablo Rodriguez Monetti
 * @param <E>
 */
public class ConcreteRepository<E> implements AbstractRepository<E>{

	public class ConcreteIterator implements AbstractIterator<E>{
	
		private int index;
		private ArrayList<E> arrList;
		private boolean nextRecentlyCalled;

		public ConcreteIterator(ArrayList<E> arrL){
			index = -1;
			nextRecentlyCalled = false;
			arrList = arrL;
		}


		public boolean hasNext(){
			return (index + 1 < arrList.size());
		}

        
		public E next() throws NoSuchElementException{
			if (!hasNext())
				throw new NoSuchElementException();
			nextRecentlyCalled = true;
			return arrList.get(++index);
		}
		
        
		public void remove() throws IllegalStateException{
			if(!nextRecentlyCalled)
				throw new IllegalStateException();
			nextRecentlyCalled = false;
			arrList.remove(index);
			index--; // CAMBIO
		}
	}

	private ArrayList<E> arrayList;

	public ConcreteRepository(){
		arrayList = new ArrayList<E>();
	}

    
    /**
     * Adds an element to the repository.
     * @param x
     */
	public void addElement(E x){
		arrayList.add(x);
	}

    
    /**
     * Creates an iterator over this repository.
     * @return an iterator over this repository.
     */
	public AbstractIterator<E> createIterator(){
		return (new ConcreteIterator(arrayList));
	}

}
