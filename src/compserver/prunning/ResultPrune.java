package compserver.prunning;

import java.util.*;

/**
 * Instances of this class abstracts the result of try to prune a test class
 */
public class ResultPrune{

	public ResultPrune(String tClassName, String theoremName, List<String> params, boolean result)
	{
		this.tClassName = tClassName;
		this.theoremName = theoremName;
		this.params = params;
		this.result = result;
	}

    /**
     * Sets the name of the TClass
     * @param tClassName
     */
	public void setTClassName(String tClassName){
        	this.tClassName = tClassName;
    	}

    /**
     * Gets the name of the TClass
     */
	public String getTClassName(){
        	return tClassName;
    }

    /**
     * Sets the name of the Theorem
     * @param theoremName
     */
	public void setTheoremName(String theoremName){
        	this.theoremName = theoremName;
    	}

    /**
     * Gets the name of the Theorem
     */
	public String getTheoremName(){
        	return theoremName;
    }

    /**
     * Sets the list of the parameters of the theorem
     * @param params
     */
	public void setParams(List<String> params){
        	this.params = params;
    	}

    /**
     * Gets the list of the parameters of the theorem
     */
	public List<String> getParams(){
        	return params;
    }

    /**
     * Sets the result of the prunning
     * @param params
     */
	public void setResult(boolean result){
        	this.result = result;
    	}

    /**
     * Gets the result of the prunning
     */
	public boolean getResult(){
        	return result;
    }

	private String tClassName;
	private String theoremName;
	private List<String> params;
	private boolean result;
}