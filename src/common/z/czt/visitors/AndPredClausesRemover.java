package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.visitor.*;
import net.sourceforge.czt.z.impl.*;


import common.z.*;

/**
 * Given a conjunction P, an instance of this class allow the removal of those clauses from P
 * that are equal to the predicate passed as argument in the constructor of this class.
 * AndPredClausesRemover is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class AndPredClausesRemover
        implements AndPredVisitor<Pred>,
        PredVisitor<Pred>, ExprPredVisitor<Pred>, AndExprVisitor<Pred>,
        ExprVisitor<Pred>{

    private Pred myPred;

    public AndPredClausesRemover(Pred pred) {
        myPred = pred;
    }

    public Pred visitAndPred(AndPred andPred) {
        Pred leftPred = andPred.getLeftPred();
        Pred rightPred = andPred.getRightPred();
        Pred pred1 = leftPred.accept(this);
        Pred pred2 = rightPred.accept(this);

        if (pred1 == null) {
            return pred2;
        } else if (pred2 == null) {
            return pred1;
        } else {
            AndPred andPred2 = (new ZFactoryImpl()).createAndPred();
            andPred2.setLeftPred(pred1);
            andPred2.setRightPred(pred2);
            andPred2.setAnd(And.NL);
            return andPred2;
        }
    }

    public Pred visitPred(Pred pred) {
        boolean areEqual = SpecUtils.areEqualTerms(myPred, pred);
        if (areEqual) {
            return null;
        } else {
            return pred;
        }
    }

    public Pred visitExprPred(ExprPred exprPred){
        return exprPred.getExpr().accept(this);
    }

    public Pred visitAndExpr(AndExpr andExpr){
        Pred leftPred = andExpr.getLeftExpr().accept(this);
        Pred rightPred = andExpr.getRightExpr().accept(this);
        Pred newPred = null;
        if(leftPred instanceof ExprPred && rightPred instanceof ExprPred){
            if(leftPred == null)
                newPred = rightPred;
            else if(rightPred == null)
                newPred = leftPred;
            else{
                ZFactory zFactory = new ZFactoryImpl();
                AndExpr newAndExpr = zFactory.createAndExpr();
                newAndExpr.setLeftExpr(((ExprPred) leftPred).getExpr());
                newAndExpr.setRightExpr(((ExprPred) rightPred).getExpr());
                newPred = zFactory.createExprPred(newAndExpr);
            }
        }
        else{
            System.out.println("Error: instance of ExprPred expected in "
                    + "the method visitAndExpr of AndPredClausesRemover.");

        }
        return newPred;
    }


    public Pred visitExpr(Expr expr) {
        if(!(myPred instanceof ExprPred))
            return null;

        Expr myExpr = ((ExprPred)myPred).getExpr();

        if (SpecUtils.areEqualTerms(myExpr, expr))
            return null;

        return (new ZFactoryImpl()).createExprPred();
    }
}
