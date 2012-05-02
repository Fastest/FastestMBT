package common.z.czt.visitors;

import common.z.SpecUtils;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.Pred2Visitor;
import net.sourceforge.czt.z.visitor.NegPredVisitor;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.visitor.Expr2Visitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.NegExprVisitor;

/**
 * Instances of this class allow the application of the rules "Morgan Laws" and "double 
 * negation" to predicates. This class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class NegPredDistributor
        implements NegPredVisitor<Pred>, Pred2Visitor<Pred>,
        PredVisitor<Pred>, ExprPredVisitor<Pred>, NegExprVisitor<Pred>,
        Expr2Visitor<Pred>, ExprVisitor<Pred>{

    public Pred visitNegPred(NegPred negPred) {
        Pred pred = negPred.getPred();

        ZFactory zFactory = new ZFactoryImpl();
        if (pred instanceof AndPred) {
            AndPred andPred = (AndPred) pred;

            NegPred negPred1 = zFactory.createNegPred();
            negPred1.setPred(andPred.getLeftPred());

            NegPred negPred2 = zFactory.createNegPred();
            negPred2.setPred(andPred.getRightPred());

            OrPred orPred = zFactory.createOrPred();
            orPred.setLeftPred(negPred1.accept(this));
            orPred.setRightPred(negPred2.accept(this));

            return orPred;
        } else if (pred instanceof OrPred) {
            OrPred orPred = (OrPred) pred;

            NegPred negPred1 = zFactory.createNegPred();
            negPred1.setPred(orPred.getLeftPred());

            NegPred negPred2 = zFactory.createNegPred();
            negPred2.setPred(orPred.getRightPred());

            AndPred andPred = zFactory.createAndPred();
            andPred.setLeftPred(negPred1.accept(this));
            andPred.setRightPred(negPred2.accept(this));
            andPred.setAnd(And.NL);

            return andPred;
        } else if (pred instanceof NegPred) {
            return ((NegPred) pred).getPred().accept(this);
        }else if(pred instanceof ExprPred){
            Expr expr = ((ExprPred) pred).getExpr();
            NegExpr negExpr = zFactory.createNegExpr(expr);
            return negExpr.accept(this);            
        }
        else {
            negPred.setPred(pred.accept(this));
            return negPred;
        }
    }

    public Pred visitPred2(Pred2 pred2) {
        pred2.setLeftPred(pred2.getLeftPred().accept(this));
        pred2.setRightPred(pred2.getRightPred().accept(this));
        return pred2;
    }


    public Pred visitPred(Pred pred) {
        return pred;
    }


    public Pred visitExprPred(ExprPred exprPred) {
        Pred auxPred = exprPred.getExpr().accept(this);
        if(auxPred instanceof ExprPred){
            ExprPred auxExprPred = (ExprPred) auxPred;
            exprPred.setExpr(auxExprPred.getExpr());
        }else{
            System.out.println("Error: instance of ExprPred expected in "
                    + "the method visitExprPred of ImpliesPredRemover.");
        }       
        return exprPred;   
    } 


    public Pred visitNegExpr(NegExpr negExpr){

        ZFactory zFactory = new ZFactoryImpl();
        Pred newPred = newPred = zFactory.createExprPred(negExpr);
        Expr subExpr = negExpr.getExpr();
        if(subExpr instanceof NegExpr){
            Expr subsubExpr = ((NegExpr) subExpr).getExpr();
            newPred = subsubExpr.accept(this);
        }
        else if(subExpr instanceof AndExpr){
            AndExpr andExpr = (AndExpr) subExpr;
            NegExpr negExpr1 = zFactory.createNegExpr(andExpr.getLeftExpr());
            Pred newLeftPred = negExpr1.accept(this);
            NegExpr negExpr2 = zFactory.createNegExpr(andExpr.getRightExpr());
            Pred newRightPred = negExpr2.accept(this);
            if(newLeftPred instanceof ExprPred &&
                newRightPred instanceof ExprPred){
                    Expr newLeftExpr = ((ExprPred) newLeftPred).getExpr();
                    Expr newRightExpr = ((ExprPred) newRightPred).getExpr();
                    OrExpr orExpr = zFactory.createOrExpr();
                    orExpr.setLeftExpr(newLeftExpr);
                    orExpr.setRightExpr(newRightExpr);      
                    newPred = zFactory.createExprPred(orExpr);
                    
            }else{
                System.out.println("Error: instances of ExprPred expected in "
                    + "the method visitNegExpr of NegPredDistributor.");
            }
        }
        else if(subExpr instanceof OrExpr){
            OrExpr orExpr = (OrExpr) subExpr;
            NegExpr negExpr1 = zFactory.createNegExpr(orExpr.getLeftExpr());
            Pred newLeftPred = negExpr1.accept(this);
            NegExpr negExpr2 = zFactory.createNegExpr(orExpr.getRightExpr());
            Pred newRightPred = negExpr2.accept(this);
            if(newLeftPred instanceof ExprPred &&
                newRightPred instanceof ExprPred){
                    Expr newLeftExpr = ((ExprPred) newLeftPred).getExpr();
                    Expr newRightExpr = ((ExprPred) newRightPred).getExpr();
                    AndExpr andExpr = zFactory.createAndExpr();
                    andExpr.setLeftExpr(newLeftExpr);
                    andExpr.setRightExpr(newRightExpr);      
                    newPred = zFactory.createExprPred(andExpr);                    
            }else{
                System.out.println("Error: instances of ExprPred expected in "
                    + "the method visitNegExpr of NegPredDistributor.");
            }                    
        }
        return newPred;
    }


    public Pred visitExpr2(Expr2 expr2){
        Pred newLeftPred = expr2.getLeftExpr().accept(this);
        Pred newRightPred = expr2.getRightExpr().accept(this);
        if(newLeftPred instanceof ExprPred &&
                newRightPred instanceof ExprPred){
                Expr newLeftExpr = ((ExprPred) newLeftPred).getExpr();
                Expr newRightExpr = ((ExprPred) newRightPred).getExpr();
                expr2.setLeftExpr(newLeftExpr);
                expr2.setRightExpr(newRightExpr);
        }else{
            System.out.println("Error: instances of ExprPred expected in "
                    + "the method visitExpr2 of NegPredDistributor.");
        }
        return (new ZFactoryImpl()).createExprPred(expr2);
    }


    public Pred visitExpr(Expr expr){
        return (new ZFactoryImpl()).createExprPred(expr);
    }

}