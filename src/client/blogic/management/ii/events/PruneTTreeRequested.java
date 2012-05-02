package client.blogic.management.ii.events;

import common.z.TClass;
import common.repository.AbstractRepository;

/**
 * Represents the implicit invocation event that must be announced when the user orders the
 * prunning of the test tree.
 */
public class PruneTTreeRequested extends Event_{

	private String opName;
	private AbstractRepository<TClass> leaves;


    /**
     * Creates instances of PruneTTreeRequested.
     * @param leaves A repository with the leaves of the testing tree
     */
	public PruneTTreeRequested(AbstractRepository<TClass> leaves){
		this.leaves = leaves;
		super.setEventName("pruneTTreeRequested");
	}

    /**
     * Sets the repository with the leaves of the testing tree
     * @param leaves A repository with the leaves of the testing tree
     */
	public void setLeaves(AbstractRepository<TClass> leaves){
		this.leaves = leaves;
	}
    /**
     * Gets the repository with the leaves of the testing tree
     * @return
     */
	public AbstractRepository<TClass> getLeaves(){
		return leaves;
	}	
}