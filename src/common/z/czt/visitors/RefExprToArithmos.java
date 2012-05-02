package common.z.czt.visitors;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;

import common.z.UtilSymbols;
/**
 * An instance of this class allow the replacement of any RefExpr for a reference to 
 * arithmos. This class is based on the Visitor
 * design pattern.
 */
public class RefExprToArithmos	
	implements  TermVisitor<Term>, RefExprVisitor<Term>{

	public Term visitTerm(Term term){
		TypeAnn s = term.getAnn(TypeAnn.class);
		if(s!=null)
		{
			Type type = s.getType();
			boolean b;
			if(type instanceof PowerType)
				b = term.getAnns().remove(s);
		}
		return VisitorUtils.visitTerm(this, term, false);
	}

	public Term visitRefExpr(RefExpr refExpr){
		ZFactory zFactory = new ZFactoryImpl();
		ZName zName = zFactory.createZName(UtilSymbols.getSymbol(3), zFactory.createZStrokeList(),"");
		RefExpr rExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(),false,false);
		return rExpr;
	}

} 
