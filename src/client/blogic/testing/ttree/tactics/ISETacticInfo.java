/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.SetExpr;
import java.util.*;


/**
 * Instances of this class contains relevant information about the related tactic, 
 * the ISE (In set extension) tactic, such as the tactic name and other 
 * parameters of the expression to be analized.
 *
 */
public class ISETacticInfo extends TacticInfo{

	private Expr leftExpr;
	private SetExpr setExpr; 

	/**
	* Creates instances of ISETacticInfo.
	*/
	public ISETacticInfo()
	{
		super.setTacticName("ISE");
	}	

	/**
	* Sets the name of the left expression
	* @param leftExpr
	*/
	public void setLeftExpr(Expr leftExpr){
		this.leftExpr = leftExpr;
	}
	/**
	* Gets the name of the left expression
	*/
	public Expr getLeftExpr(){
            return leftExpr;
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