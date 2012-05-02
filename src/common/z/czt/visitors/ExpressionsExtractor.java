package common.z.czt.visitors;

import net.sourceforge.czt.base.ast.*;
import net.sourceforge.czt.base.visitor.*;
import net.sourceforge.czt.z.ast.Ann;
import net.sourceforge.czt.z.ast.ParenAnn;
import java.util.*;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.ApplExprVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.NumExprVisitor;
import net.sourceforge.czt.z.visitor.SetExprVisitor;
import net.sourceforge.czt.z.visitor.MemPredVisitor;
import net.sourceforge.czt.z.visitor.SetCompExprVisitor;
import net.sourceforge.czt.z.ast.ApplExpr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.SetCompExpr;
import net.sourceforge.czt.z.ast.ZExprList;
import common.z.UtilSymbols;
import net.sourceforge.czt.z.ast.Ann;
import net.sourceforge.czt.z.ast.TypeAnn;
import net.sourceforge.czt.z.ast.Type;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.ZNameList;

/**
 * A visitor that obtains all the Expr from an AST.
 */

public class ExpressionsExtractor
  implements TermVisitor<Map<Expr,Type>>, ApplExprVisitor<Map<Expr,Type>>, RefExprVisitor<Map<Expr,Type>>, NumExprVisitor<Map<Expr,Type>>, SetExprVisitor<Map<Expr,Type>>, MemPredVisitor<Map<Expr,Type>>, SetCompExprVisitor<Map<Expr,Type>>
{

	public ExpressionsExtractor(){
		boundedNames = new ArrayList<Name>();
	}


	public Map<Expr,Type> visitTerm(Term term)
	{
		Map<Expr,Type> mapList = new HashMap<Expr,Type>();
		try{
		if(term != null){
		Object[] array = term.getChildren();
		Boolean result = new Boolean(false);
		Term auxTerm = null;
		if(array!=null){
		for (int i = 0; i < array.length; i++) {
		final Object object = array[i];
		if (object instanceof Term && object != null){
			auxTerm = (Term) object;
			if(auxTerm!=null){
			Map<Expr,Type> auxMap = auxTerm.accept(this);
			if(auxMap!=null)
				mapList.putAll(auxMap);
			}
		}
		}
		}
		}
		}
		catch(Exception e){
			System.out.println("Exception: \n"+e.toString());
		}
		return mapList;
	}

	public Map<Expr,Type> visitApplExpr(ApplExpr applExpr)
	{
		Map<Expr,Type> mapList = new HashMap<Expr,Type>();
		TypeAnn typeAnn = applExpr.getAnn(TypeAnn.class);
		Type type = typeAnn.getType();

		boolean contains = false;
		for(int i=0;i<boundedNames.size();i++)
			if(applExpr.accept(new ContainsTermVerifier(boundedNames.get(i))))
				contains = true;
		if(!contains)
			mapList.put(applExpr,type);
		//else
		//	System.out.println("Contenia:\n"+SpecUtils.termToLatex(applExpr));

		Expr rightExpr = applExpr.getRightExpr();
		mapList.putAll(rightExpr.accept(this));
		Expr leftExpr = applExpr.getLeftExpr();
		mapList.putAll(leftExpr.accept(this));
		return mapList;
	}
	public Map<Expr,Type> visitRefExpr(RefExpr refExpr)
	{
		Map<Expr,Type> mapList = new HashMap<Expr,Type>();
		try{
		String refExprName = refExpr.getZName().getWord().toString();
		if(!refExpr.getMixfix() && !refExpr.getExplicit()){
			boolean isReserved = false;
			for(int i=0; i< UtilSymbols.getNumOfSymbols() && !isReserved;i++)
				if(refExprName.equals(UtilSymbols.getSymbol(i)))
						isReserved = true;
			if(!isReserved){

		boolean contains = false;
		for(int i=0;i<boundedNames.size();i++)
			if(refExpr.accept(new ContainsTermVerifier(boundedNames.get(i))))
				contains = true;
		//if(contains)
		//	System.out.println("Contenia:\n"+SpecUtils.termToLatex(refExpr));



			TypeAnn typeAnn = refExpr.getAnn(TypeAnn.class);
			if(typeAnn!=null){
			Type type = typeAnn.getType();
			if(!contains)
				mapList.put(refExpr,type);
			}
			else
				System.out.println("Sin info de tipos: "+SpecUtils.termToLatex(refExpr));
			}
		}
		}
		catch(Exception e){
			System.out.println("EXCEPTION!!!");
		}
		return mapList;
	}

	public Map<Expr,Type> visitNumExpr(NumExpr numExpr){
		Map<Expr,Type> mapList = new HashMap<Expr,Type>();
		TypeAnn typeAnn = numExpr.getAnn(TypeAnn.class);
		Type type = typeAnn.getType();
		Type t = mapList.put(numExpr,type);
		return mapList;
	}

	public Map<Expr,Type> visitSetExpr(SetExpr setExpr){
		Map<Expr,Type> mapList = new HashMap<Expr,Type>();
		TypeAnn typeAnn = setExpr.getAnn(TypeAnn.class);
		Type type = typeAnn.getType();
		ZExprList zExprList = setExpr.getZExprList();
		if(zExprList.size()>0){
		Type t = mapList.put(setExpr,type);
		for(int i=0;i<zExprList.size();i++){
			Expr auxExpr = zExprList.get(i);
			mapList.putAll(auxExpr.accept(this));
		}
		}
		return mapList;
	}

	public Map<Expr,Type> visitMemPred(MemPred memPred){
		Map<Expr,Type> mapList = new HashMap<Expr,Type>();
		Expr rightExpr = memPred.getRightExpr();
		Expr leftExpr = memPred.getLeftExpr();
		if(rightExpr instanceof SetExpr){
		SetExpr auxSetExpr = (SetExpr) rightExpr;
		ZExprList zExprList = auxSetExpr.getZExprList();
		for(int i=0;i<zExprList.size();i++){
			Expr auxExpr = zExprList.get(i);
			mapList.putAll(auxExpr.accept(this));
		}
		mapList.putAll(leftExpr.accept(this));
		}
		else{
			mapList.putAll(leftExpr.accept(this));
			mapList.putAll(rightExpr.accept(this));
		}
		return mapList;
	}

	public Map<Expr,Type> visitSetCompExpr(SetCompExpr setCompExpr){
		Map<Expr,Type> mapList = new HashMap<Expr,Type>();
		TypeAnn typeAnn = setCompExpr.getAnn(TypeAnn.class);
		Type type = typeAnn.getType();
		Type t = mapList.put(setCompExpr,type);

		ZSchText zSchText = setCompExpr.getZSchText();
		ZDeclList zList = zSchText.getZDeclList();
		for(int i=0;i<zList.size();i++){
			Decl auxDecl = zList.get(i);
			if(auxDecl instanceof VarDecl){
				VarDecl varDecl = (VarDecl) auxDecl;
				ZNameList zNameList = varDecl.getZNameList();
				for(int j=0;j<zNameList.size();j++){
				//System.out.println("Variable: "+ SpecUtils.termToLatex(zNameList.get(j)));
				//String varName = SpecUtils.termToLatex(zNameList.get(j));
				//boundedNames.add(varName);
				boundedNames.add(zNameList.get(j));
				}
			}
		}
		mapList.putAll(setCompExpr.getExpr().accept(this));
		mapList.putAll(setCompExpr.getZSchText().accept(this));
		return mapList;
	}
	private List<Name> boundedNames;
}