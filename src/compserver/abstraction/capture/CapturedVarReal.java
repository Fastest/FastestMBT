package compserver.abstraction.capture;

/**
 * Stores the captured value of a rel variable in the UUT
 */
public class CapturedVarReal implements CapturedVar{

	public CapturedVarReal(){
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
	public void setValue(double value){
		this.value = value;
	}
    /**
     * Gets the name of the variable
     * @return varName
     */
	public double getValue(){
		return value;
	}

	private String varName;
	private double value;
}