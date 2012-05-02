package compserver.tcasegen.strategies;

import java.util.*;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;

/**
 * This module stores a set of variables of the specific test class
 */
public class VariablesSet{

	public VariablesSet(){
	}
	public VariablesSet(List<RefExpr> vars){
		this.vars = vars;
	}
	public void setVariables(List<RefExpr> vars){
		this.vars = vars;
	}
	public List<RefExpr> getVariables(){
		return vars;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for(int i=0;i<vars.size();i++){
			RefExpr refExpr = vars.get(i);
			hashCode += refExpr.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof VariablesSet){
			VariablesSet auxVarsSet = (VariablesSet) obj;
			List<RefExpr> otherVars = auxVarsSet.getVariables();
			// We must check that the list of variables of the two instances have the same elements
			if(otherVars.size()!=vars.size())
				return false;

			boolean match = true;
			for(int i=0;i<vars.size() && match;i++){
				RefExpr var = vars.get(i);
				boolean found = false;
				for(int j=0;j<otherVars.size() && !found;j++){
					RefExpr otherVar = otherVars.get(j);
					found = otherVar.equals(var);
				}
				if(!found)
					match = false;
			}
			return match;
		}
		else
			return false;
	}

	private List<RefExpr> vars;
}
