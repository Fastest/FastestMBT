package compserver.management;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import net.sourceforge.czt.z.ast.Spec;

/**
 * The instance of this class (which is a singleton) contains all the specifications that were
 * sent to the server
 */
public class SpecsReceived implements AbstractRepository<Spec>{
    
    private static SpecsReceived specsReceived;
    private AbstractRepository<Spec> repository;
    
    
    
    /**
     * Creates intances of SpecsReceived.
     */
	private SpecsReceived(){
		repository = new ConcreteRepository<Spec>();
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static SpecsReceived getInstance(){	
		if(specsReceived==null)
			specsReceived = new SpecsReceived();
		return specsReceived;
	}

    
    
    /**
     * Creates an iterator in order to be possible the traversal along this
     * structure.
     * @return
     */
	public AbstractIterator<Spec> createIterator(){
		return repository.createIterator();
	}

    
    
    /**
     * Add a new instance of Spec to this object.
     * @param spec
     */
	public void addElement(Spec spec){
		repository.addElement(spec);
	}

	public synchronized boolean existsHash(int hashSpec)
	{
		return true;
	}
}