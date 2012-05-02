package common.z.czt.visitors;

import java.util.*;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.ApplExpr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.visitor.ApplExprVisitor;
import net.sourceforge.czt.z.ast.ZExprList;
import common.z.UtilSymbols;
import common.z.SpecUtils;

public class FuncApplExtractor	
	implements  TermVisitor<List<Term>>, ApplExprVisitor<List<Term>>{

	private String operator;
	private String symbol;

	public FuncApplExtractor(String operator){
		this.operator = operator;
		if(operator.equals("\\cup"))
			symbol = UtilSymbols.unionSymbol();
		else if(operator.equals("\\cap"))
			symbol = UtilSymbols.intersectionSymbol();
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

	public List<Term> visitApplExpr(ApplExpr applExpr){
		//System.out.println("Visitando la ApplExpr");
		List<Term> termList = new ArrayList<Term>();
		Expr leftExpr = applExpr.getLeftExpr();
		Expr rightExpr = null;
		if(leftExpr instanceof RefExpr){
			RefExpr refExpr = (RefExpr) leftExpr;
			//System.out.println("Es una RefExpr");
			String refExprStr = refExpr.getZName().toString();
			if(refExprStr.equals(symbol)){
				termList.add(applExpr);
				return termList;
				//System.out.println("Es una interseccion!!!");
				//rightExpr = applExpr.getRightExpr();
				//if(rightExpr instanceof TupleExpr){
				//	TupleExpr tupleExpr = (TupleExpr) rightExpr;
					//System.out.println("Es una tupla");
				//	return tupleExpr;
					//ZExprList zList = tupleExpr.getZExprList();
					//for(int i=0;i<zList.size();i++){
					//	System.out.println(SpecUtils.termToLatex(zList.get(i)));
					//	Expr aux = zList.get(i);
					//} 
					
				//}
			}
		}
		return termList;
	}
}