package client.blogic.management.ii.events;

import common.z.AbstractTCase;
import common.z.TClass;

/**
 * Represents the implicit invocation event that must be announced when the user orders the
 * generation of JAVA concrete test case.
 *
 * @author  Pablo D. Coca
 *
 * @since   v2.0
 *
 * @version 2.0
 */
public class TCaseRefineRequestedJAVA extends Event_{
    
	private String opName;
    private TClass tClass;
    private AbstractTCase abstractTCase;

    public TCaseRefineRequestedJAVA(String opName, TClass tClass, AbstractTCase abstractTCase){
		
    	this.opName = opName;
		this.tClass = tClass;
		this.abstractTCase = abstractTCase;
		super.setEventName("tCaseRefineRequestedJAVA");
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
	public void setAbstractTCase(AbstractTCase abstractTCase){
		this.abstractTCase = abstractTCase;
	}

    /**
     * Gets the abstract test case associated to this object.
     * @return the abstract test case
     */
	public AbstractTCase getAbstractTCase(){
		return abstractTCase;
	}

}
