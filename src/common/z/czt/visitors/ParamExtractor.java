/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.z.czt.visitors;
    
import java.util.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.ApplExpr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.MemPredVisitor;
import net.sourceforge.czt.z.visitor.ApplExprVisitor;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;


/**
 * An instance of this class allow the extraction of the parameters from a term. For example,
 * the extraction of parameters from "a=b" results in the list [a,b]. This class is based on
 * the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class ParamExtractor implements PredVisitor<List<Term>>,
                ExprVisitor<List<Term>>, MemPredVisitor<List<Term>>, 
                ApplExprVisitor<List<Term>>, ExprPredVisitor<List<Term>>{
    
    
    public List<Term> visitPred(Pred pred){
        return null;
    }
    
    public List<Term> visitExpr(Expr expr){
        return null;
    }
    
    public List<Term> visitMemPred(MemPred memPred){
        List<Term> termList = null;
        if(memPred.getMixfix()){
            // El operador es un >, >=, <, <=, !=, c, etc
            Expr leftExpr = memPred.getLeftExpr();
            if(leftExpr instanceof TupleExpr){
                TupleExpr tupleExpr = (TupleExpr) leftExpr;
                ZExprList zExprList = tupleExpr.getZExprList();
                termList = new ArrayList<Term>();
                for(int i=0; i<zExprList.size(); i++)
                    termList.add(zExprList.get(i));
            }
            else if(leftExpr instanceof RefExpr){
                RefExpr refExpr = (RefExpr) leftExpr;
                Expr rightExpr = memPred.getRightExpr();
                if(rightExpr instanceof SetExpr){
                    SetExpr setExpr = (SetExpr) rightExpr;
                    ZExprList zExprList = setExpr.getZExprList();
                    if(zExprList.size() == 1){
                        // El operador es el =
                        termList = new ArrayList<Term>();
                        termList.add(refExpr);
                        termList.add(zExprList.get(0));
                    }
                    else
                        return null;
                }
                else
                    return null;              
            }
            else
                return null;
        }
        else{
            // El operador es un \in
            termList = new ArrayList<Term>();
            termList.add(memPred.getLeftExpr());
            termList.add(memPred.getRightExpr());
            
        }
        return termList;
    }
    
    public List<Term> visitApplExpr(ApplExpr applExpr){
        if(!applExpr.getMixfix())
	{
            return null;
	}
        List<Term> termList = new ArrayList<Term>();
        Expr rightExpr = applExpr.getRightExpr();
        if(rightExpr instanceof TupleExpr){
            // Operadores n-arios. n > 1
            TupleExpr tupleExpr = (TupleExpr) rightExpr;
            ZExprList zExprList = tupleExpr.getZExprList();
                
            for(int i=0; i<zExprList.size(); i++)
                termList.add(zExprList.get(i));
            
            return termList;        
        }
        else if(rightExpr instanceof RefExpr) {
	    // Operadores unarios
	    RefExpr refExpr = (RefExpr) rightExpr;
	    termList.add(refExpr);
            return termList;
        }
	return null;
    }
    
    public List<Term> visitExprPred(ExprPred exprPred){
       return exprPred.getExpr().accept(this);
    }

}
