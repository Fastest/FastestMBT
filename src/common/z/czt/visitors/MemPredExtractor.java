package common.z.czt.visitors;

import java.util.*;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.visitor.MemPredVisitor;
import net.sourceforge.czt.z.ast.ZExprList;
import common.z.UtilSymbols;
import common.z.SpecUtils;


public class MemPredExtractor	
	implements  TermVisitor<List<Term>>, MemPredVisitor<List<Term>>{

	private String operator;
	private String symbol;
	//private List<Term> termList;

	public MemPredExtractor(String operator){
		this.operator = operator;
		if(operator.equals("\\geq"))
			symbol = UtilSymbols.gecSymbol();
		else if(operator.equals("\\leq"))
			symbol = UtilSymbols.leqSymbol();
		else if(operator.equals("\\neq"))
			symbol = UtilSymbols.neqSymbol();
		else if(operator.equals("<"))
			symbol = UtilSymbols.lessThanSymbol();
		else if(operator.equals(">"))
			symbol = UtilSymbols.greaterThanSymbol();
		//termList = new ArrayList<Term>();
	}

	public List<Term> visitTerm(Term term){
	Object[] array = term.getChildren();
	Boolean result = new Boolean(false);
	Term auxTerm = null;
	List<Term> termList = new ArrayList<Term>();
	for (int i = 0; i < array.length; i++) {
	final Object object = array[i];
	if (object instanceof Term){
		auxTerm = (Term) object;
		//finalTerm = auxTerm.accept(this);
		boolean changed = termList.addAll(auxTerm.accept(this));
	}
	}
	return termList;
	}

	public List<Term> visitMemPred(MemPred memPred){
		//System.out.println("Visitando la ApplExpr");
		List<Term> termList = new ArrayList<Term>();
		Expr leftExpr = memPred.getLeftExpr();
		Expr rightExpr = memPred.getRightExpr();
		boolean isEquality = false;
		if(rightExpr instanceof SetExpr){
			SetExpr auxSet = (SetExpr) rightExpr;
			ZExprList zList = auxSet.getZExprList();
			if(zList.size()==1)
				isEquality = true;
		}
		if(isEquality){
			if(operator.equals("="))
				termList.add(memPred);
			return termList;
		}
		else{
			//System.out.println("Es un operador");
			if(rightExpr instanceof RefExpr){
				RefExpr refAux = (RefExpr) rightExpr;
				String refExprStr = refAux.getZName().toString();
				if(refExprStr.equals(symbol)){
					termList.add(memPred);
					return termList;
				}
			}
			return termList;
		}
	}
}