package client.blogic.management.ii.events;

import java.util.*;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import client.blogic.testing.ttree.TClassNode;



/**
 * Represents the implicit invocation event that must be announced when all the
 * information needed to test an operation is loaded. This information consists
 * of the operation name, the strategy of tactics application and the list of
 * tactics to be applied. Eventually it could be consisted of a test class from
 * which generate more test classes, the strategy and the list of tactics.
 * @author Pablo Rodriguez Monetti
 */
public class TTreeRequested extends Event_{

	private String unitToTest;
	private List<Tactic> tacticList;
	private TTreeStrategy tTreeStrategy;
	private TClassNode tClassNode;

    
    
    /**
     * Creates instances of TTreeRequested.
     * @param opName
     * @param tacticList
     * @param tTreeStrategy
     */
	public TTreeRequested(String opName, List<Tactic> tacticList, TTreeStrategy tTreeStrategy, TClassNode tClassNode){
		this.unitToTest = opName;
		this.tacticList = tacticList;
		this.tTreeStrategy = tTreeStrategy;
		this.tClassNode = tClassNode;
		super.setEventName("tTreeRequested");
	}

    
    
    /**
     * Sets the operation or test class name associated to this object.
     * @param unitToTest
     */
	public void setUnitToTest(String unitToTest){
		this.unitToTest = unitToTest;
	}

    
    
    /**
     * Gets the operation name associated to this object.
     * @return
     */
	public String getUnitToTest(){
		return unitToTest;
	}

    
    /**
     * Sets the list of tactics associated to this object.
     * @param tacticList
     */
	public void setTacticList(List<Tactic> tacticList){
		this.tacticList = tacticList;
	}

    
    
    /**
     * Gets the list of tactics associated to this object.
     * @return
     */
	public List<Tactic> getTacticList(){
		return tacticList;
	}

    
    
    /**
     * Sets the strategy of tactics application that will be used to 
     * generate the test tree for the operation associated to this object.
     * @param tTreeStrategy
     */
	public void setTTreeStrategy(TTreeStrategy tTreeStrategy){
		this.tTreeStrategy = tTreeStrategy;
	}

    
    
    /**
     * Gets the strategy of tactics application that will be used to 
     * generate the test tree for the operation associated to this object.
     * @return
     */
	public TTreeStrategy getTTreeStrategy(){
		return tTreeStrategy;
	}

    /**
     * Sets the TClassNode that will be the root of the new subtree
     * @param tClass
     */
	public void setTClassNode(TClassNode tClassNode){
		this.tClassNode = tClassNode;
	}

    /**
     * Gets the TClassNode that will be the root of the new subtree
     */
	public TClassNode getTClassNode(){
		return tClassNode;
	}
	
}