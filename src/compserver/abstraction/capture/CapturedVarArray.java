package compserver.abstraction.capture;

import java.util.*;

/**
 * Stores the captured value of an array variable in the UUT
 */
public class CapturedVarArray implements CapturedVar{

	public CapturedVarArray(){
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
	public void setValue(List<CapturedVar> value){
		this.value = value;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public List<CapturedVar> getValue(){
		return value;
	}

	private String varName;
	private List<CapturedVar> value;
}