/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;

import java.util.*;

import net.sourceforge.czt.base.ast.Term;



/**
 * Instances of this class contains relevant information about the related tactic, 
 * the SP (Standard Partition) tactic, such as the tactic name and other 
 * necessary parameters: the standard partition related to this tactic and the
 * list of real parameters of the expression to be analized.
 * @author Pablo Rodriguez Monetti
 */
public class SPTacticInfo extends TacticInfo{
    
    private StdPartition stdPartition;
    List<Term> realParamList;
    
    
    
    /**
     * Creates instances of SPTacticInfo.
     */
    public SPTacticInfo(){
		super.setTacticName("SP");
	}
    
    
    
    /**
     * Sets the standard partition associated to this object.
     * @param stdPartition
     */
    public void setStdPartition(StdPartition stdPartition){
        this.stdPartition = stdPartition;
    }
    
    
    
    /**
     * Gets the standard partition associated to this object.
     * @return
     */
    public StdPartition getStdPartition(){
        return stdPartition;
    }
    
    
    
    /**
     * Sets the list of real parameters associated to this object.
     * @param realParamList
     */
    public void setRealParamList(List<Term> realParamList){
        this.realParamList = realParamList;
    } 
    
    
    
    /**
     * Gets the list of real parameters associated to this object.
     * @return
     */
    public List<Term> getRealParamList(){
        return realParamList;
    }
}
