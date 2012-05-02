package client.blogic.management.ii.events;

import net.sourceforge.czt.session.SectionManager;

/**
 * Represents the implicit invocation event that must be announced when the SectionManager
 * that have information of types of the Spec is loaded
 */
public class ManagerLoaded extends Event_{

	private SectionManager manager;

    
    /**
     * Creates instances of ManagerLoaded.
     * @param manager The SectionManager
     */
	public ManagerLoaded(SectionManager manager){
		this.manager = manager;
		super.setEventName("managerLoaded");
	}

    /**
     * Sets the Section Manager associated to this object.
     * @param tClass
     */
 	public void setManager(SectionManager manager){
		this.manager = manager;
	}

    
    
    /**
     * Gets the Section Manager associated to this object.
     * @return
     */
	public SectionManager getManager(){
		return manager;
	}
} 
