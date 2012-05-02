package common.z.czt.visitors;

import java.util.*;
import java.math.*;
import java.io.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.QntPredVisitor;
import net.sourceforge.czt.z.visitor.SetCompExprVisitor;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import net.sourceforge.czt.animation.eval.TextUI;

import common.z.SpecUtils;
import common.z.czt.UniqueZLive;

import common.z.UtilSymbols;



/**
 * An instance of this class allow the replacement of alphanumeric constants with numeric
 * constants, defining an injective function between them. This class is based on the Visitor
 * design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class StringToNumReplacer	
	implements  TermVisitor<Term>, RefExprVisitor<Term>, QntPredVisitor<Term>,
SetCompExprVisitor<Term>{
       
    
	private Map<String, NumExpr> refNumExprMap ;
    private int nextInt;
    private List<String> additionalReservedWords;

	public StringToNumReplacer(){
		refNumExprMap = new HashMap<String, NumExpr>();
        additionalReservedWords = new ArrayList<String>();
		nextInt = 0;
	}

	public void setNextInt(int nextInt){
		this.nextInt = nextInt;
	}

	public int getNextInt(){
		return nextInt;
	}

	public void setRefNumExprMap(Map<String, NumExpr> refNumExprMap){
		this.refNumExprMap = refNumExprMap;
	}

	public Map<String, NumExpr> getRefNumExprMap(){
		return refNumExprMap;
	}



	public Term visitTerm(Term term){
		TypeAnn s = term.getAnn(TypeAnn.class);
		if(s!=null)
		{
			Type type = s.getType();
			boolean b;
			if(type instanceof PowerType)
				b = term.getAnns().remove(s);

		}
		//boolean b = term.getAnns().remove(s);
		return VisitorUtils.visitTerm(this, term, false);
	}

	public Term visitQntPred(QntPred qntPred){
		ZSchText zSchText = qntPred.getZSchText();
		ZDeclList zList = zSchText.getZDeclList();
		for(int i=0;i<zList.size();i++){
			Decl auxDecl = zList.get(i);
			if(auxDecl instanceof VarDecl){
				VarDecl varDecl = (VarDecl) auxDecl;
				ZNameList zNameList = varDecl.getZNameList();
				for(int j=0;j<zNameList.size();j++){
				//System.out.println("Variable: "+ SpecUtils.termToLatex(zNameList.get(j)));
				String varName = SpecUtils.termToLatex(zNameList.get(j));
				additionalReservedWords.add(varName);
				}
			}
		}
		return VisitorUtils.visitTerm(this, qntPred, false);
	}


	public Term visitRefExpr(RefExpr refExpr){
		RefExpr origRefExpr = (RefExpr) refExpr.accept(new CZTCloner());
	ZExprList zList = refExpr.getZExprList();

		String refExprStr = refExpr.getZName().toString();
       // If refExpr corresponds to a reserved Z word, we return refExpr.
       boolean isReserved = false;
	ZFactory zFactory = new ZFactoryImpl();
       for(int i=0; i< UtilSymbols.getNumOfSymbols();i++){
           if(refExprStr.equals(UtilSymbols.getSymbol(i)))
               isReserved = true;       
       }
           //System.out.print("Variable: "+refExprStr);       
       if(isReserved){
	//MICODIGO
	ZExprList zExprList = refExpr.getZExprList();
	for(int i=0;i<zExprList.size();i++)
	{
		Expr expr = zExprList.get(i);
		//System.out.println(SpecUtils.termToLatex(expr)+" es hijo de "+SpecUtils.termToLatex(refExpr));
		zExprList.set(i, (Expr) expr.accept(new RefExprToArithmos()));
	}
        return refExpr;               
	//MICODIGO

       }
            //System.out.println("  -> NO");        
        
        // If this term is contained in a set comprehension with a declared variable whose
        // name is equal to this refExpr, we return refExpr            
        for(int i=0; i<additionalReservedWords.size(); i++){
            if(refExprStr.equals(additionalReservedWords.get(i))){
                return refExpr;
            }
        }
   
        // If refExpr corresponds to a String contained in the refNumExprMap, we return
        // a clone of the instance of NumExpr associated to it.
        NumExpr numExpr = refNumExprMap.get(refExprStr);
		if(numExpr != null){
			return (NumExpr) numExpr.accept(new CZTCloner());
		}

        // Otherwise, we create a new instance of NumExpr, numExpr, with the value of nextInt
        // (which is incremented afterwards) and we add to the map refNumExprMap the pair
        // (refExpr, numExpr)
		ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(nextInt++));
		numExpr = zFactory.createNumExpr(zNumeral);	

		refNumExprMap.put(refExprStr,numExpr);

		return numExpr;
	}
    
    
    public Term visitSetCompExpr(SetCompExpr setCompExpr){
        // We save the list of additional reserved words
        List<String> originalList = new ArrayList<String>();
        for(int i=0; i<additionalReservedWords.size(); i++){
            originalList.add(additionalReservedWords.get(i));
        }
       
        // We add the variables declared in the SetCompExpr to the list of additional 
        // reserved words, in order not to replace their ocurrences.
        ZSchText zSchText = setCompExpr.getZSchText();
		DeclList declList = zSchText.getDeclList();
		if(declList instanceof ZDeclList){
			ZDeclList zDeclList = (ZDeclList)declList;
			for(int j=0; j< zDeclList.size(); j++){
				Decl decl = zDeclList.get(j);
				if(decl instanceof VarDecl){
                    VarDecl varDecl = (VarDecl) decl;
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl)	{
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        for(int i=0; i < zNameListImpl.size(); i++){
                            String varName = zNameListImpl.get(i).toString();
                            additionalReservedWords.add(varName);
                        }                          
                    }                  
                }
			}
		}

        
        TextUI textUI = new TextUI(UniqueZLive.getInstance(), new PrintWriter(System.out, true));

        // We replace the alphanumeric constants of the SetCompExpr recursively, without
        // replacing the variables declared in the SetCompExpr
        setCompExpr.setSchText((ZSchText) zSchText.accept(this)) ;
        Expr expr = setCompExpr.getExpr();
        setCompExpr.setExpr((Expr) expr.accept(this));

        // We restore the list of additional reserved words
        additionalReservedWords = originalList;
    
        return setCompExpr;
        
    }
	/*public Term visitSetExpr(SetExpr setExpr){
		ZFactory zFactory = new ZFactoryImpl();
		ZExprList zExprList = setExpr.getZExprList();
		for(int i=0;i<zExprList.size();i++)
		{
			Expr expr = zExprList.get(i);
			if(expr instanceof RefExpr)
			{
				zExprList.set(i, (Expr)expr.accept(this));
			}
			else
				zExprList.set(i, (Expr)expr.accept(this));
		}
		return setExpr;
	}*/

} 
