package client.blogic.management.ii.events;

import client.blogic.testing.refinement.ConcreteTCase;
import common.z.TClass;

/**
 * Represents the implicit invocation event that must be announced when the 
 * refined JAVA test case is generated.
 * 
 * @author  Pablo D. Coca
 *
 * @since   v2.0
 *
 * @version 2.0
 */
public class TCaseRefinedJAVA extends Event_{
    
	private String opName;
	private TClass tClass;
    private ConcreteTCase concreteTCase;

    public TCaseRefinedJAVA(String opName, TClass tClass, ConcreteTCase concreteTCase){
        
    	this.opName = opName;
		this.tClass = tClass;
        this.concreteTCase = concreteTCase;
		super.setEventName("tCaseRefinedJAVA");
		
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
     * @return the operation name
     */
	public String getOpName(){
		return opName;
	}

    /**
     * Sets the test class associated to this object.
     * @param tClass
     */
	public void setTClass(TClass tClass){
		this.tClass = tClass;
	}

    /**
     * Gets the test class associated to this object.
     * @return the test class
     */
	public TClass getTClass(){
		return tClass;
	}

    /**
     * Sets the abstract test case associated to this object.
     * @param abstractTCase
     */
	public void setConcreteTCase(ConcreteTCase concreteTCase){
		this.concreteTCase = concreteTCase;
	}

    /**
     * Gets the abstract test case associated to this object.
     * @return the abstract test case
     */
	public ConcreteTCase getConcreteTCase(){
		return concreteTCase;
	}
}