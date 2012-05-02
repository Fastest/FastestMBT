package client.blogic.management.ii.events;

import java.io.*;
import common.z.AbstractTCase;

/**
 * @author Hache
 */
public class TCaseRefineRequested extends Event_{
    private String opName;
    private AbstractTCase abstractTCase;
    private String targetLanguaje;
    private String pathUUT;
    private String refLawName;

    public TCaseRefineRequested(String opName, AbstractTCase abstractTCase, String pathUUT, String targetLanguaje, String refLawName){
		this.opName = opName;
		this.abstractTCase = abstractTCase;
		this.targetLanguaje = targetLanguaje;
		this.pathUUT = pathUUT;
		this.refLawName = refLawName;
		super.setEventName("tCaseRefineRequested");
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


	public void setAbstractTCase(AbstractTCase abstractTCase){
		this.abstractTCase = abstractTCase;
	}

	public AbstractTCase getAbstractTCase(){
		return abstractTCase;
	}

	public String getTargetLanguaje(){
		return targetLanguaje;
	}
	
	public String getPathUUT(){
		return pathUUT;
	}
	
	public String getRefLawName(){
		return refLawName;
	}

}
