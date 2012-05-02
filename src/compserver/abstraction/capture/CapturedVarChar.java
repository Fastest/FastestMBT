package compserver.abstraction.capture;

/**
 * Stores the captured value of a char variable in the UUT
 */
public class CapturedVarChar implements CapturedVar{

	public CapturedVarChar(){
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
	public void setValue(char value){
		this.value = value;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public char getValue(){
		return value;
	}

	private String varName;
	private char value;
}