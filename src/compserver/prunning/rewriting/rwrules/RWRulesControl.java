package compserver.prunning.rewriting.rwrules;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;

/**
 * The instance of this class (which is a singleton) contains a number of
 * instances of objetcs that implements the interface RWRule.
 */
public class RWRulesControl implements AbstractRepository<RWRule>{
    
    private static RWRulesControl rwRulesControl;
    private static AbstractRepository<RWRule> repository;
    
    
    
    /**
     * Creates intances of RWRulesControl and load the rules
     */
	private RWRulesControl(){
		repository = new ConcreteRepository<RWRule>();
		RWRule rwEquality = new RWEquality();
		RWRule rwGreaterThanEqual = new RWGreaterThanEqual();
		RWRule rwGreaterThan = new RWGreaterThan();
		RWRule rwIntersection = new RWIntersection();
		RWRule rwLessThanEqual = new RWLessThanEqual();
		RWRule rwLessThan = new	RWLessThan();
		RWRule rwNotEqual = new RWNotEqual();
		RWRule rwUnion = new RWUnion();
		repository.addElement(rwEquality);
		repository.addElement(rwGreaterThanEqual);
		repository.addElement(rwGreaterThan);
		repository.addElement(rwIntersection);
		repository.addElement(rwLessThanEqual);
		repository.addElement(rwLessThan);
		repository.addElement(rwNotEqual);
		repository.addElement(rwUnion);
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static RWRulesControl getInstance(){	
		if(rwRulesControl==null)
			rwRulesControl = new RWRulesControl();
		else if(isEmpty()){
			rwRulesControl = new RWRulesControl();
		}
		return rwRulesControl;
	}

    
    
    /**
     * Creates an iterator in order to be possible the traversal along this
     * structure.
     * @return
     */
	public AbstractIterator<RWRule> createIterator(){
		return repository.createIterator();
	}

	private static boolean isEmpty(){
		boolean empty = true;
		AbstractIterator<RWRule> it = repository.createIterator();
		if(it.hasNext())
			empty = false;
		return empty;
	}
    
    
    /**
     * Add a new instance of a rewrite rule.
     * @param rwRule
     */
	public void addElement(RWRule rwRule){
		repository.addElement(rwRule);
	}
}