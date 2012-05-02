package compserver.prunning.operators;

/**
 * Instances of this class 
 */
public class SpecialLine{

	public SpecialLine()
	{}

	public SpecialLine(String operator, String line, String theoremName)
	{
		this.operator = operator;
		this.line = line;
		this.theoremName = theoremName;
	}
    /**
     * Sets the operator included in the line
     * @param name
     */
	public void setOperator(String operator){
        	this.operator = operator;
    	}

    /**
     * Gets the operator
     */
	public String getOperator(){
        	return operator;
    }

    /**
     * Sets the predicate
     * @param type
     */
	public void setLine(String line){
        	this.line = line;
    	}

    /**
     * Gets the predicate
     */
	public String getLine(){
        	return line;
	}

    /**
     * Sets the theorem's name
     * @param type
     */
	public void setTheoremName(String theoremName){
        	this.theoremName = theoremName;
    	}

    /**
     * Gets the theorem's name
     */
	public String getTheoremName(){
        	return theoremName;
	}
	private String operator;
	private String line;
	private String theoremName;
}