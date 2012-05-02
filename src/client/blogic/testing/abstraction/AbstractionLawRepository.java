package client.blogic.testing.abstraction;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import compserver.abstraction.AbstractionLaw;


/**
 * The instance of this class (which is a singleton) contains a number of
 * instances of AbstractionLaw.
 */
public class AbstractionLawRepository implements AbstractRepository<AbstractionLaw>{
    
    private static AbstractionLawRepository abstractionLawRepository;
    private AbstractRepository<AbstractionLaw> repository;

    /**
     * Creates intances of AbstractionLawRepository.
     */
	private AbstractionLawRepository(){
		repository = new ConcreteRepository<AbstractionLaw>();
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static AbstractionLawRepository getInstance(){	
		if(abstractionLawRepository==null)
			abstractionLawRepository = new AbstractionLawRepository();
		return abstractionLawRepository;
	}

    /**
     * Creates an iterator in order to be possible the traversal along this
     * structure.
     * @return
     */
	public AbstractIterator<AbstractionLaw> createIterator(){
		return repository.createIterator();
	}

    /**
     * Add a new instance of AbstractionLaw to this object.
     * @param absLaw
     */
	public void addElement(AbstractionLaw absLaw){
		repository.addElement(absLaw);
	}
}