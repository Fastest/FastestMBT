package compserver.abstraction.capture;

import java.util.*;

/**
 * Stores the captured value of a list variable in the UUT
 */
public class CapturedVarList implements CapturedVar{

	public CapturedVarList(){
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
	public void setValue(List<List<CapturedField>> elements){
		this.value = elements;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public List<List<CapturedField>> getValue(){
		return value;
	}

	private String varName;
	private List<List<CapturedField>> value;
}