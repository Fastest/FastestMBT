package compserver.abstraction.capture;

/**
 * Stores the captured value of a pointer variable in the UUT
 */
public class CapturedVarPointer implements CapturedVar{

	public CapturedVarPointer(){
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
	public void setValue(CapturedVar value){
		this.value = value;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public CapturedVar getValue(){
		return value;
	}

	private String varName;
	private CapturedVar value;
}