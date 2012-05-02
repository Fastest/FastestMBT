package compserver.prunning;

import java.util.*;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.FreePara;


/**
 * Instances of this class stores all the information that the module of pruning must know about the
 * specification to prune a test class
 */
public class SpecInfo{

	public SpecInfo(){

	}
	/**
	* Sets the map with the information of the axiomatic definitions
	* @param axDefsValues
	*/
	public void setAxDefsValues(Map<RefExpr,Expr> axDefsValues){
		this.axDefsValues = axDefsValues;
	}
	/**
	* Gets the map with the information of the axiomatic definitions
	*/
	public Map<RefExpr,Expr> getAxDefsValues(){
		return axDefsValues;
	}
	/**
	* Sets the list with the free type paragraphs of the specification
	* @param freeParaList
	*/
	public void setFreeParaList(List<FreePara> freeParaList){
		this.freeParaList = freeParaList;
	}

	/**
	* Gets the list with the free type paragraphs of the specification
	*/
	public List<FreePara> getFreeParaList(){
		return freeParaList;
	}

	private Map<RefExpr,Expr> axDefsValues;
	private List<FreePara> freeParaList;
}