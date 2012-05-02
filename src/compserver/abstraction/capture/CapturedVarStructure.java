package compserver.abstraction.capture;

import java.util.*;

/**
 * Stores the captured value of a structure variable in the UUT
 */
public class CapturedVarStructure implements CapturedVar{

	public CapturedVarStructure(){
	}
    /**
     * Sets the name of the variable
     * @param varName
     */
	public void setVarName(String varName){
		this.varName = varName;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public String getVarName(){
		return varName;
	}
    /**
     * Sets the value of the variable
     * @param varName
     */
	public void setValue(List<CapturedField> fields){
		this.value = fields;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public List<CapturedField> getValue(){
		return value;
	}

	private String varName;
	private List<CapturedField> value;
}