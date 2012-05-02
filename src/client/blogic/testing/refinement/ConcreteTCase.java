package client.blogic.testing.refinement;

import java.util.*;
import common.z.AbstractTCase;

/**
 * This module stores all the information related to a concrete test case
 * @author Hache
 */

public class ConcreteTCase {
    private String preamble;
    private String epilogue;
    private List<TCaseAssignment> tCaseAssignments = new ArrayList<TCaseAssignment>();
    private String pathUUT;
    private String concreteTCaseName;
    private AbstractTCase abstractTCase;
    private String absLawName;
    private String opName;
    private String targetLanguaje;
    private String initDecls;

    public void setPreamble(String preable){
        this.preamble = preable;
    }
    public void setInitDeclarations(String initDecls){
        this.initDecls = initDecls;
    }
    
    public void setEpilogue(String epilogue){
        this.epilogue = epilogue;
    }
    public void addTCaseAssignment(TCaseAssignment tCaseAssignment){
        tCaseAssignments.add(tCaseAssignment);
    }

    public void addTCaseAssignment(List<TCaseAssignment> tCaseAssignments){
        this.tCaseAssignments.addAll(tCaseAssignments);
    }
    
    public String getPreamble(){
        return preamble;
    }

    public String getInitDeclarations(){
        return initDecls;
    }

    public String getEpilogue(){
        return epilogue;
    }

    public List<TCaseAssignment> getAssigments(){
        return tCaseAssignments;
    }
    public void setPathUUT(String pathUUT){
	this.pathUUT = pathUUT;
    }
    public String getPathUUT(){
	return pathUUT;
    }
    public void setConcreteTCaseName(String concreteTCaseName){
	this.concreteTCaseName = concreteTCaseName;
    }
    public String getConcreteTCaseName(){
	return concreteTCaseName;
    }
    public void setAbstractTCase(AbstractTCase abstractTCase){
	this.abstractTCase = abstractTCase;
    }
    public AbstractTCase getAbstractTCase(){
	return abstractTCase;
    }
    public void setAbsLawName(String absLawName){
	this.absLawName = absLawName;
    }
    public String getAbsLawName(){
	return absLawName;
    }
    public void setOpName(String opName){
	this.opName = opName;
    }
    public String getOpName(){
	return opName;
    }
    public void setLanguaje(String targetLanguaje){
	this.targetLanguaje = targetLanguaje;
    }
    public String getLanguaje(){
	return targetLanguaje;
    }
    @Override
    public String toString(){
	return Utils.printCTC(concreteTCaseName, this);
    }

}