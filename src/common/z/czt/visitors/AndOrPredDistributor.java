package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.And;
import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.Pred2;
import net.sourceforge.czt.z.ast.NegPred;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Expr2;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.NegExpr;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.AndPredVisitor;
import net.sourceforge.czt.z.visitor.Pred2Visitor;
import net.sourceforge.czt.z.visitor.NegPredVisitor;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.AndExprVisitor;
import net.sourceforge.czt.z.visitor.Expr2Visitor;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.NegExprVisitor;

/**
 * An instance of this class allow the application of the distributive property of a 
 * conjunction over a disjunction. The class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class AndOrPredDistributor
        implements AndPredVisitor<Pred>, Pred2Visitor<Pred>,
        NegPredVisitor<Pred>, PredVisitor<Pred>, ExprPredVisitor<Pred>,
        AndExprVisitor<Pred>, Expr2Visitor<Pred>, NegExprVisitor<Pred>,
        ExprVisitor<Pred> {

    public Pred visitAndPred(AndPred andPred) {
        Pred leftPred = andPred.getLeftPred();
        Pred rightPred = andPred.getRightPred();
        Pred visitedLeftPred = null;
        Pred visitedRightPred = null;

        if (leftPred instanceof OrPred) {
            OrPred orPred = (OrPred) leftPred;
            Pred leftLeftPred = orPred.getLeftPred();
            Pred rightLeftPred = orPred.getRightPred();

            AndPred andPred1 = (new ZFactoryImpl()).createAndPred();
            andPred1.setLeftPred(leftLeftPred);
            andPred1.setRightPred(rightPred);
            andPred1.setAnd(And.NL);

            AndPred andPred2 = (new ZFactoryImpl()).createAndPred();
            andPred2.setLeftPred(rightLeftPred);
            andPred2.setRightPred((Pred) rightPred.accept(new CZTCloner()));
            andPred2.setAnd(And.NL);

            orPred.setLeftPred(andPred1.accept(this));
            orPred.setRightPred(andPred2.accept(this));
            return orPred;
        }  else if (rightPred instanceof OrPred) {
            OrPred orPred = (OrPred) rightPred;
            Pred leftRightPred = orPred.getLeftPred();
            Pred rightRightPred = orPred.getRightPred();

            AndPred andPred1 = (new ZFactoryImpl()).createAndPred();
            andPred1.setLeftPred(leftPred);
            andPred1.setRightPred(leftRightPred);
            andPred1.setAnd(And.NL);

            AndPred andPred2 = (new ZFactoryImpl()).createAndPred();
            andPred2.setLeftPred((Pred) leftPred.accept(new CZTCloner()));
            andPred2.setRightPred(rightRightPred);
            andPred2.setAnd(And.NL);

            orPred.setLeftPred(andPred1.accept(this));
            orPred.setRightPred(andPred2.accept(this));
            return orPred;
        } else if ((visitedLeftPred = leftPred.accept(this)) instanceof OrPred) {
            andPred.setLeftPred(visitedLeftPred);
            andPred.setRightPred(rightPred);
            andPred.setAnd(And.NL);
            return andPred.accept(this);
        } else if ((visitedRightPred = rightPred.accept(this)) instanceof OrPred) {
            andPred.setLeftPred(leftPred);
            andPred.setRightPred(visitedRightPred);
            andPred.setAnd(And.NL);
            return andPred.accept(this);
        } else {
            andPred.setLeftPred(visitedLeftPred);
            andPred.setRightPred(visitedRightPred);
            return andPred;
        }
    }

    public Pred visitPred2(Pred2 pred2) {
        pred2.setLeftPred(pred2.getLeftPred().accept(this));
        pred2.setRightPred(pred2.getRightPred().accept(this));
        return pred2;
    }

    public Pred visitNegPred(NegPred negPred) {
        Pred pred = negPred.getPred();
        negPred.setPred(pred.accept(this));
        return negPred;
    }

    public Pred visitPred(Pred pred) {
        return pred;
    }

    public Pred visitExprPred(ExprPred exprPred) {
        Pred auxPred = exprPred.getExpr().accept(this);
        if (auxPred instanceof ExprPred) {
            ExprPred auxExprPred = (ExprPred) auxPred;
            exprPred.setExpr(auxExprPred.getExpr());
        } else {
            System.out.println("Error: instance of ExprPred expected in "
                    + "the method visitExprPred of ImpliesPredRemover.");
        }
        return exprPred;
    }

    public Pred visitAndExpr(AndExpr andExpr) {
        Expr leftExpr = andExpr.getLeftExpr();
        Expr rightExpr = andExpr.getRightExpr();
        ZFactory zFactory = new ZFactoryImpl();
        ExprPred exprPred = zFactory.createExprPred(andExpr);
        if (leftExpr instanceof OrExpr) {
            OrExpr orExpr = (OrExpr) leftExpr;
            Expr leftleftExpr = orExpr.getLeftExpr();
            Expr rightLeftExpr = orExpr.getRightExpr();

            AndExpr andExpr1 = zFactory.createAndExpr();
            andExpr1.setLeftExpr(leftleftExpr);
            andExpr1.setRightExpr(rightExpr);

            AndExpr andExpr2 = zFactory.createAndExpr();
            andExpr2.setLeftExpr(rightLeftExpr);
            andExpr2.setRightExpr((Expr) rightExpr.accept(new CZTCloner()));

            Pred pred1 = andExpr1.accept(this);
            Pred pred2 = andExpr2.accept(this);
            if (pred1 instanceof ExprPred
                    && pred2 instanceof ExprPred) {
                Expr newLeftExpr = ((ExprPred) pred1).getExpr();
                Expr newRightExpr = ((ExprPred) pred2).getExpr();
                orExpr.setLeftExpr(newLeftExpr);
                orExpr.setRightExpr(newRightExpr);
                exprPred = zFactory.createExprPred(orExpr);

            } else {
                System.out.println("Error: instances of ExprPred expected in "
                        + "the method visitAndExpr of AndOrPredDistributor.");
            }
        } else if (rightExpr instanceof OrExpr) {
            OrExpr orExpr = (OrExpr) rightExpr;
            Expr leftRightExpr = orExpr.getLeftExpr();
            Expr rightRightExpr = orExpr.getRightExpr();


            AndExpr andExpr1 = zFactory.createAndExpr();
            andExpr1.setLeftExpr(leftExpr);
            andExpr1.setRightExpr(leftRightExpr);

            AndExpr andExpr2 = zFactory.createAndExpr();
            andExpr2.setLeftExpr((Expr) leftExpr.accept(new CZTCloner()));
            andExpr2.setRightExpr(rightRightExpr);

            Pred pred1 = andExpr1.accept(this);
            Pred pred2 = andExpr2.accept(this);
            if (pred1 instanceof ExprPred
                    && pred2 instanceof ExprPred) {
                Expr newLeftExpr = ((ExprPred) pred1).getExpr();
                Expr newRightExpr = ((ExprPred) pred2).getExpr();
                orExpr.setLeftExpr(newLeftExpr);
                orExpr.setRightExpr(newRightExpr);
                exprPred = zFactory.createExprPred(orExpr);

            } else {
                System.out.println("Error: instances of ExprPred expected in "
                        + "the method visitAndExpr of AndOrPredDistributor.");
            }

        } else {
            Pred visitedLeftPred = leftExpr.accept(this);
            if (visitedLeftPred instanceof ExprPred) {
                Expr visitedLeftExpr = ((ExprPred) visitedLeftPred).getExpr();
                if (visitedLeftExpr instanceof OrExpr) {
                    andExpr.setLeftExpr(visitedLeftExpr);
                    andExpr.setRightExpr(rightExpr);
                    Pred newPred = andExpr.accept(this);
                    if (newPred instanceof ExprPred) {
                        exprPred = (ExprPred) newPred;
                    } else {
                        System.out.println("Error: instance of ExprPred expected in "
                                + "the method visitAndExpr of AndOrPredDistributor.");
                    }
                } else {
                    Pred visitedRightPred = rightExpr.accept(this);
                    if (visitedRightPred instanceof ExprPred) {
                        Expr visitedRightExpr = ((ExprPred) visitedRightPred).getExpr();
                        if (visitedRightExpr instanceof OrExpr) {
                            andExpr.setLeftExpr(leftExpr);
                            andExpr.setRightExpr(visitedRightExpr);
                            Pred newPred = andExpr.accept(this);
                            if (newPred instanceof ExprPred) {
                                exprPred = (ExprPred) newPred;
                            } else {
                                System.out.println("Error: instance of ExprPred expected in "
                                        + "the method visitAndExpr of AndOrPredDistributor.");
                            }
                        }
                        else{
                            andExpr.setLeftExpr(visitedLeftExpr);
                            andExpr.setRightExpr(visitedRightExpr);
                            exprPred = zFactory.createExprPred(andExpr);
                        }
                    }
                }
            } else {
                System.out.println("Error: instance of ExprPred expected in "
                        + "the method visitAndExpr of AndOrPredDistributor.");
            }
        }
        return exprPred;
    }


    public Pred visitNegExpr(NegExpr negExpr){
        ZFactory zFactory = new ZFactoryImpl();
        Pred newSubPred = negExpr.getExpr().accept(this);
        if(newSubPred instanceof ExprPred){
            Expr newSubExpr = ((ExprPred) newSubPred).getExpr();
            negExpr.setExpr(newSubExpr);
        }
        else{
            System.out.println("Error: instance of ExprPred expected in "
            + "the method visitNegPred of AndOrPredDistributor.");
        }
        Pred newExprPred = zFactory.createExprPred(negExpr);
        return newExprPred;
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
                    + "the method visitExpr2 of AndOrPredDistributor.");
        }
        return (new ZFactoryImpl()).createExprPred(expr2);
    }


    public Pred visitExpr(Expr expr){
        return (new ZFactoryImpl()).createExprPred(expr);
    }



}
