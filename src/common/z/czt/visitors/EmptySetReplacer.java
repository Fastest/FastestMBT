package common.z.czt.visitors;

import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZFactory;
import common.z.UtilSymbols;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.ast.ZExprList;
import common.z.SpecUtils;


public class EmptySetReplacer	
	implements  TermVisitor<Term>, RefExprVisitor<Term> {

	public Term visitTerm(Term term){
		return VisitorUtils.visitTerm(this, term, false);
	}

	public Term visitRefExpr(RefExpr refExpr){
		String strRefExpr = refExpr.getZName().toString();
		if(strRefExpr.equals(UtilSymbols.emptySetSymbol())){
		ZFactory zFactory = new ZFactoryImpl();
		ZExprList zList = zFactory.createZExprList();
		SetExpr newEmptySet =zFactory.createSetExpr(zList);
		return newEmptySet;
		}
		return refExpr;
	}
}