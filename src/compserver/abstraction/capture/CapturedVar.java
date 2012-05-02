package compserver.abstraction.capture;

/**
 * Interface that represents the captured value of a variable in the UUT
 */
public interface CapturedVar{
    /**
     * Sets the name of the variable
     * @param varName
     */
	public void setVarName(String varName);
    /**
     * Gets the name of the variable
     * @return id
     */
	public String getVarName();
}