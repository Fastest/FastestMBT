/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;

import net.sourceforge.czt.z.ast.SetExpr;
import java.util.*;


/**
 * Instances of this class contains relevant information about the related tactic, 
 * the SSE (Sub Set of Set Extension) tactic, such as the tactic name and other 
 * parameters of the expression to be analized.
 *
 */
public class SSETacticInfo extends TacticInfo{

	private String var;
	private SetExpr setExpr; 

	/**
	* Creates instances of SSETacticInfo.
	*/
	public SSETacticInfo()
	{
		super.setTacticName("SSE");
	}	

	/**
	* Sets the name of the variable
	* @param var
	*/
	public void setVar(String var){
		this.var = var;
	}
	/**
	* Gets the name of the variable
	*/
	public String getVar(){
		return var;
	}
	/**
	* Sets the set extension
	* @param setExpr
	*/
	public void setSet(SetExpr setExpr){ 
		this.setExpr = setExpr;
	}
	/**
	* Gets the set extension
	*/
	public SetExpr getSet(){
		return setExpr;
	}
}