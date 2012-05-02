/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.management.ii.events;


import compserver.tcasegen.strategies.TCaseStrategy;
/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class TCaseStrategySelected extends Event_{
    private String tClassName;
    private TCaseStrategy tCaseStrategy;
    
    public TCaseStrategySelected(String tClassName, TCaseStrategy tCaseStrategy){
        this.tClassName = tClassName;
        this.tCaseStrategy = tCaseStrategy;
		super.setEventName("tCaseStrategySelected");        
    }
    
    
    /**
     * Sets the test class associated to this object.
     * @param tClass
     */
 	public void setTClassName(String tClassName){
		this.tClassName = tClassName;
	}

    
    
    /**
     * Gets the test class associated to this object.
     * @return
     */
	public String getTClassName(){
		return tClassName;
	}    
    
    
    
    
    /**
     * Sets the strategy of test cases generation associated to this object.
     * @param tCaseStrategy
     */
	public void setTCaseStrategy(TCaseStrategy tCaseStrategy){
		this.tCaseStrategy = tCaseStrategy;
	}

    
    
    /**
     * Gets the strategy of test cases generation associated to this object.
     * @return
     */
	public TCaseStrategy getTCaseStrategy(){
		return tCaseStrategy;
	}
}
