package compserver.abstraction.capture;

/**
 * Stores the captured value of an integer variable in the UUT
 */
public class CapturedVarInt implements CapturedVar{

	public CapturedVarInt(){
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
	public void setValue(int value){
		this.value = value;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public int getValue(){
		return value;
	}

	private String varName;
	private int value;
}