package client.blogic.management.ii.events;

import client.blogic.testing.ttree.TClassNode;



/**
 * Represents the implicit invocation event that must be announced when the 
 * test tree associated to some operation is generated.
 * @author Pablo Rodriguez Monetti
 */
public class TTreeGenerated extends Event_{

	private String opName;
	private TClassNode tTree;

    
    
    /**
     * Creates instances of TTreeGenerated.
     * @param opName
     * @param tTree
     */
	public TTreeGenerated(String opName, TClassNode tTree){
		this.opName = opName;
		this.tTree = tTree;
		super.setEventName("tTreeGenerated");
	}

    
    
    /**
     * Sets the name of the operation associated to this object.
     * @param opName
     */
	public void setOpName(String opName){
		this.opName = opName;
	}

    
    
    /**
     * Gets the name of the operation associated to this object.
     * @return
     */
	public String getOpName(){
		return opName;
	}

    
    
    /**
     * Sets the test tree associated to this object. This must be the generated
     * test trree for the operation associated to this object.
     * @param tClassNode
     */
	public void setTTree(TClassNode tClassNode){
		tTree = tClassNode;
	}

    
    
    /**
     * Gets the test tree associated to this object. This must be the generated
     * test trree for the operation associated to this object.
     * @return
     */
	public TClassNode getTTree(){
		return tTree;
	}	
}
