/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.DecorExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Expr2;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Pred2;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.AndPredVisitor;
import net.sourceforge.czt.z.visitor.OrPredVisitor;
import net.sourceforge.czt.z.visitor.PredVisitor;

/**
 * An instance of this class takes as input a predicate in Disjunctive Normal
 * Form and removes the atomic predicates that contains output and primed 
 * variables.
 * @author Pablo Rodriguez Monetti
 */
public class OutputAtomicPredRemover
        implements PredVisitor<Pred>, OrPredVisitor<Pred>, AndPredVisitor<Pred> {

    public Pred visitOrPred(OrPred orPred) {
        return auxVisitAndOrPred(orPred);
    }

    public Pred visitAndPred(AndPred andPred) {
        return auxVisitAndOrPred(andPred);
    }

    public Pred visitPred(Pred pred) {
        if (pred.accept(new OutputVarsFinder())) {
            return null;
        } else {
            return pred;
        }
    }

    private Pred auxVisitAndOrPred(Pred2 pred2) {
        Pred leftPred = pred2.getLeftPred().accept(this);
        Pred rightPred = pred2.getRightPred().accept(this);
        if (leftPred == null) {
            return rightPred;
        } else if (rightPred == null) {
            return leftPred;
        } else {
            pred2.setLeftPred(leftPred);
            pred2.setRightPred(rightPred);
            return pred2;
        }
    }

    public Pred visitExprPred(ExprPred exprPred) {
        Pred auxPred = exprPred.getExpr().accept(this);

        if (auxPred == null) {
            return null;
        }

        if (auxPred instanceof ExprPred) {
            ExprPred auxExprPred = (ExprPred) auxPred;
            exprPred.setExpr(auxExprPred.getExpr());
        } else {
            System.out.println("Error: instance of ExprPred expected in "
                    + "the method visitExprPred of OutputAtomicPredRemover.");
        }
        return exprPred;

    }

    public Pred visitAndExpr(AndExpr andExpr) {
        return auxVisitAndOrExpr(andExpr);
    }

    public Pred visitOrExpr(OrExpr orExpr) {
        return auxVisitAndOrExpr(orExpr);
    }

    private Pred auxVisitAndOrExpr(Expr2 expr2) {
        Pred newLeftPred = expr2.getLeftExpr().accept(this);
        Pred newRightPred = expr2.getRightExpr().accept(this);
        ZFactory zFactory = new ZFactoryImpl();
        ExprPred exprPred = null;
        if (newLeftPred == null) {
            if (newRightPred instanceof ExprPred) {
                exprPred = zFactory.createExprPred(((ExprPred) newRightPred).getExpr());
            } else {
                System.out.println("Error: instance of ExprPred expected in "
                        + "the method auxVisitAndOrExpr of OutputAtomicPredRemover.");
            }
        } else if (newRightPred == null) {
            if (newLeftPred instanceof ExprPred) {
                exprPred = zFactory.createExprPred(((ExprPred) newLeftPred).getExpr());
            } else {
                System.out.println("Error: instance of ExprPred expected in "
                        + "the method auxVisitAndOrExpr of OutputAtomicPredRemover.");
            }
        } else {
            if (newLeftPred instanceof ExprPred
                    && newRightPred instanceof ExprPred) {
                expr2.setLeftExpr(((ExprPred) newLeftPred).getExpr());
                expr2.setRightExpr(((ExprPred) newRightPred).getExpr());
                exprPred = zFactory.createExprPred(expr2);
            } else {
                System.out.println("Error: instances of ExprPred expected in "
                        + "the method auxVisitAndOrExpr of OutputAtomicPredRemover.");
            }
        }
        return exprPred;
    }

    public Pred visitDecorExpr(DecorExpr decorExpr) {
        // null is returned because the DecorExpr represents a primed schema
        return null;
    }

    public Pred visitExpr(Expr expr) {
        return (new ZFactoryImpl()).createExprPred(expr);
    }
}
