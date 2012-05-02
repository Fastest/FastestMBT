package common.repository;


/**
 * An Iterator over a repository of elements.
 * @author Pablo Rodriguez Monetti
 * @param <E>
 */
public interface AbstractIterator<E>{
	
    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration. 
     */
    public E next();
    
    /**
     * Returns true if the iteration has more elements.
     * @return true if the iterator has more elements.
     */
	public boolean hasNext();
    
    /**
     * Removes from the underlying repository the last element returned by the iterator.
     * This method can be called only once per call to next. The behavior of an iterator is 
     * unspecified if the underlying repository is modified while the iteration is in
     * progress in any way other than by calling this method. 
     */
	public void remove();
}