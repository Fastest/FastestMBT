package common.z.czt.visitors;

import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.ast.ImpliesPred;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.Pred2Visitor;
import net.sourceforge.czt.z.visitor.ImpliesPredVisitor;
import net.sourceforge.czt.z.visitor.IffPredVisitor;
import net.sourceforge.czt.z.visitor.NegPredVisitor;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.Expr2Visitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.IffExprVisitor;
import net.sourceforge.czt.z.visitor.ImpliesExprVisitor;

/**
 * An instance of this class allow the transformation of predicates like a => b into 
 * predicates of the form ¬a \/ b. This class is based on the Visitor desing pattern.
 * @author Pablo Rodriguez Monetti
 */
public class ImpliesPredRemover
        implements IffPredVisitor<Pred>, ImpliesPredVisitor<Pred>,
        Pred2Visitor<Pred>, NegPredVisitor<Pred>, PredVisitor<Pred>,
        ExprPredVisitor<Pred>, Expr2Visitor<Pred>, ImpliesExprVisitor<Pred>,
        IffExprVisitor<Pred>, ExprVisitor<Pred> {

    public Pred visitIffPred(IffPred iffPred) {
        Pred leftPred = iffPred.getLeftPred().accept(this);
        Pred rightPred = iffPred.getRightPred().accept(this);

        ZFactory zFactory = new ZFactoryImpl();
        NegPred negPred1 = zFactory.createNegPred(leftPred);
        NegPred negPred2 = zFactory.createNegPred(rightPred);
        
        AndPred andPred1 = zFactory.createAndPred();
        andPred1.setLeftPred(negPred1);
        andPred1.setRightPred(negPred2);
        andPred1.setAnd(And.NL);

        AndPred andPred2 = zFactory.createAndPred();
        andPred2.setLeftPred((Pred) leftPred.accept(new CZTCloner()));
        andPred2.setRightPred((Pred) rightPred.accept(new CZTCloner()));
        andPred2.setAnd(And.NL);

        OrPred orPred = zFactory.createOrPred();
        orPred.setLeftPred(andPred1);
        orPred.setRightPred(andPred2);
        return orPred;


    }

    public Pred visitImpliesPred(ImpliesPred impliesPred) {
        Pred leftPred = impliesPred.getLeftPred().accept(this);
        Pred rightPred = impliesPred.getRightPred().accept(this);

        NegPred negPred = (new ZFactoryImpl()).createNegPred();
        negPred.setPred(leftPred);

        OrPred orPred = (new ZFactoryImpl()).createOrPred();
        orPred.setLeftPred(negPred);
        orPred.setRightPred(rightPred);

        return orPred;
    }

    public Pred visitPred2(Pred2 pred2) {
        pred2.setLeftPred(pred2.getLeftPred().accept(this));
        pred2.setRightPred(pred2.getRightPred().accept(this));
        return pred2;
    }

    public Pred visitNegPred(NegPred negPred) {
        negPred.setPred(negPred.getPred().accept(this));
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

    public Pred visitImpliesExpr(ImpliesExpr impliesExpr) {
        Pred newLeftPred = impliesExpr.getLeftExpr().accept(this);
        Pred newRightPred = impliesExpr.getRightExpr().accept(this);
        ZFactory zFactory = new ZFactoryImpl();
        ExprPred exprPred = zFactory.createExprPred(impliesExpr);
        if (newLeftPred instanceof ExprPred
                && newRightPred instanceof ExprPred) {
            Expr newLeftExpr = ((ExprPred) newLeftPred).getExpr();
            Expr newRightExpr = ((ExprPred) newRightPred).getExpr();

            NegExpr negExpr = zFactory.createNegExpr(newLeftExpr);
            OrExpr orExpr = zFactory.createOrExpr();
            orExpr.setLeftExpr(negExpr);
            orExpr.setRightExpr(newRightExpr);
            exprPred = zFactory.createExprPred(orExpr);

        } else {
            System.out.println("Error: instances of ExprPred expected in "
                    + "the method visitImpliesExpr of ImpliesPredRemover.");
        }
        return exprPred;

    }

    public Pred visitIffExpr(IffExpr iffExpr) {
        Pred newLeftPred = iffExpr.getLeftExpr().accept(this);
        Pred newRightPred = iffExpr.getRightExpr().accept(this);
        ZFactory zFactory = new ZFactoryImpl();
        ExprPred exprPred = zFactory.createExprPred(iffExpr);
        if (newLeftPred instanceof ExprPred
                && newRightPred instanceof ExprPred) {
            Expr newLeftExpr = ((ExprPred) newLeftPred).getExpr();
            Expr newRightExpr = ((ExprPred) newRightPred).getExpr();

            NegExpr negExpr1 = zFactory.createNegExpr(newLeftExpr);
            OrExpr orExpr1 = zFactory.createOrExpr();
            orExpr1.setLeftExpr(negExpr1);
            orExpr1.setRightExpr(newRightExpr);

            NegExpr negExpr2 = zFactory.createNegExpr(newRightExpr);
            OrExpr orExpr2 = zFactory.createOrExpr();
            orExpr2.setLeftExpr(negExpr2);
            orExpr2.setRightExpr(newLeftExpr);

            AndExpr andExpr = zFactory.createAndExpr();
            andExpr.setLeftExpr(orExpr1);
            andExpr.setRightExpr(orExpr2);
            exprPred = zFactory.createExprPred(andExpr);

        } else {
            System.out.println("Error: instances of ExprPred expected in "
                    + "the method visitIffExpr of ImpliesPredRemover.");
        }
        return exprPred;
    }

    public Pred visitExpr2(Expr2 expr2) {

        Pred newLeftPred = expr2.getLeftExpr().accept(this);
        Pred newRightPred = expr2.getRightExpr().accept(this);

        if (newLeftPred instanceof ExprPred
                && newRightPred instanceof ExprPred) {
            Expr newLeftExpr = ((ExprPred) newLeftPred).getExpr();
            Expr newRightExpr = ((ExprPred) newRightPred).getExpr();
            expr2.setLeftExpr(newLeftExpr);
            expr2.setRightExpr(newRightExpr);
        } else {
            System.out.println("Error: instances of ExprPred expected in "
                    + "the method visitExpr2 of ImpliesPredRemover.");
        }
        return (new ZFactoryImpl()).createExprPred(expr2);
    }

    public Pred visitExpr(Expr expr) {
        return (new ZFactoryImpl()).createExprPred(expr);
    }
}
