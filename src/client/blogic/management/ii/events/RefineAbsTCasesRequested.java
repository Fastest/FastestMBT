package client.blogic.management.ii.events;

import java.io.*;
import java.util.Collection;
import common.z.AbstractTCase;
import client.blogic.testing.refinement.*;

/**
 * Represents the implicit invocation event that must be announced when
 * a set of abstract test cases need to be refined.
 * @author Hache
 */
public class RefineAbsTCasesRequested extends Event_{
    
    private Collection<AbstractTCase> absTCasesColl;
    private String opName;
    private String targetLanguaje;
    private String pathUUT;
    private String refLawName;
    /**
     * Creates instances of RefineAbsTCasesRequested.
     */
    public RefineAbsTCasesRequested(String opName, Collection<AbstractTCase> set, String pathUUT, String targetLanguaje, String refLawName){
        this.absTCasesColl = set;
        this.opName = opName;
	this.targetLanguaje = targetLanguaje;
	this.pathUUT = pathUUT;
	this.refLawName = refLawName;
        super.setEventName("refineAbsTCasesRequested");
    }

    public String getOpName(){
        return opName;
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

    public Collection<AbstractTCase> getAbsTCasesColl(){
        return absTCasesColl;
    }

}
