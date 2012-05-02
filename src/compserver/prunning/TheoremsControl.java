package compserver.prunning;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;


/**
 * The instance of this class (which is a singleton) contains a number of
 * instances of Theorem.
 */
public class TheoremsControl implements AbstractRepository<Theorem>{
    
    private static TheoremsControl theoremsControl;
    private AbstractRepository<Theorem> repository;
    private int maxCard;
    
    
    
    /**
     * Creates intances of TheoremsControl.
     */
	private TheoremsControl(){
		repository = new ConcreteRepository<Theorem>();
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static TheoremsControl getInstance(){	
		if(theoremsControl==null)
			theoremsControl = new TheoremsControl();
		return theoremsControl;
	}

    
    
    /**
     * Creates an iterator in order to be possible the traversal along this
     * structure.
     * @return
     */
	public AbstractIterator<Theorem> createIterator(){
		return repository.createIterator();
	}

    
    
    /**
     * Add a new instance of Theorem to this object.
     * @param theorem
     */
	public void addElement(Theorem theorem){
		repository.addElement(theorem);
	}

	public void setMaxCardinality(int maxCard){
		this.maxCard = maxCard;
	}

	public int getMaxCardinality(){
		return maxCard;
	}

}