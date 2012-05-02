/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;


import java.util.*;

import net.sourceforge.czt.z.ast.Pred;



/**
 * Instances of this class represents standard partitions of Z operators.
 * @author Pablo Rodriguez Monetti
 */
public class StdPartition {

    List<Pred> predList;
    List<String> formalParamList;
    String definition;
    String operator;
    
    
        
    /**
     * Sets the list of predicates that conforms this standard partition.
     * @param predList
     */
    public void setPredList(List<Pred> predList){
        this.predList = predList;
    }
        
    
    
    /**
     * Gets the list of predicates that conforms this standard partition.
     * @return
     */
    public List<Pred> getPredList(){
        return predList;     
    }
    

    
    /**
     * Sets the list of formal parameters of this standard partition.
     * @param formalParamList
     */
    public void setFormalParamList(List<String> formalParamList){
        this.formalParamList = formalParamList;
    }
    
    
    
    /**
     * Gets the list of formal parameters of this standard partition.
     * @return
     */
    public List<String> getFormalParamList(){
        return formalParamList;     
    }
    
    
    
    /**
     * Sets the definition of this standard partition.
     * @param definition
     */
    public void setDefinition(String definition){
        this.definition = definition;
    }
    
    
    
    
    /**
     * Gets the definition of this standard partition.
     * @return
     */
    public String getDefinition(){
        return definition;
    }
    
        
    /**
     * Sets the operator of this standard partition.
     * @param operator
     */
    public void setOperator(String operator){
        this.operator = operator;
    }
    
    
    
    /**
     * Gets the operator of this standard partition.
     * @return
     */
    public String getOperator(){
        return operator;
    }

}
