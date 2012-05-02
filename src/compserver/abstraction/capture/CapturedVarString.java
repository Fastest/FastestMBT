package compserver.abstraction.capture;

/**
 * Stores the captured value of a string
 variable in the UUT
 */
public class CapturedVarString implements CapturedVar{

	public CapturedVarString(){
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
	public void setValue(String value){
		this.value = value;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public String getValue(){
		return value;
	}

	private String varName;
	private String value;
}