package compserver.abstraction.capture;

import java.util.*;

/**
 * Stores the captured value of a database variable in the UUT
 */
public class CapturedVarDB implements CapturedVar{

	public CapturedVarDB(){
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
	public void setValue(Map<String,List<String>> columns){
		this.value = columns;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public Map<String,List<String>> getValue(){
		return value;
	}

	private String varName;
	private Map<String,List<String>> value;
}