package common.repository;

/**
 * Abstract collection of elements.
 * @author Pablo Rodriguez Monetti
 * @param <E>
 */
public interface AbstractRepository<E>{
    /**
     * Adds an element to the repository.
     * @param x the element to be added
     */
	public void addElement(E x);
    
    /**
     * Creates an iterator over this repository.
     * @return an iterator over this repository.
     */
	public AbstractIterator<E> createIterator();

} 
