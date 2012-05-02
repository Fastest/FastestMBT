package compserver.abstraction.capture;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;


/**
 * The instance of this class (which is a singleton) contains a number of
 * instances of CapturedVar.
 */
public class CapturedVarsControl implements AbstractRepository<CapturedVar>{
    
    private static CapturedVarsControl capturedVarsControl;
    private AbstractRepository<CapturedVar> repository;
    

    /**
     * Creates intances of CapturedVarsControl.
     */
	private CapturedVarsControl(){
		repository = new ConcreteRepository<CapturedVar>();
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static CapturedVarsControl getInstance(){	
		if(capturedVarsControl==null)
			capturedVarsControl = new CapturedVarsControl();
		return capturedVarsControl;
	}


    /**
     * Creates an iterator in order to be possible the traversal along this
     * structure.
     * @return
     */
	public AbstractIterator<CapturedVar> createIterator(){
		return repository.createIterator();
	}

    
    
    /**
     * Add a new instance of CapturedVar to this object.
     * @param theorem
     */
	public void addElement(CapturedVar capturedVar){
		repository.addElement(capturedVar);
	}

}