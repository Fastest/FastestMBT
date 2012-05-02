package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.OrPredVisitor;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.OrExprVisitor;

/**
 * Instances of this class allow the extraction of the predicates that form a disjunction. The
 * class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class OrPredClausesExtractor
        implements OrPredVisitor<List<Pred>>,
        PredVisitor<List<Pred>>,
        ExprPredVisitor<List<Pred>>,
        OrExprVisitor<List<Pred>>,
        ExprVisitor<List<Pred>>{

    public List<Pred> visitOrPred(OrPred orPred) {
        //System.out.println(SpecUtils.termToLatex(orPred));
        Pred leftPred = orPred.getLeftPred();
        Pred rightPred = orPred.getRightPred();

        //AbstractRepository<Pred> predRep1 = leftPred.accept(this);
        //AbstractRepository<Pred> predRep2 = rightPred.accept(this);
        List<Pred> predList1 = leftPred.accept(this);
        List<Pred> predList2 = rightPred.accept(this);

       predList1.addAll(predList2);
        return predList1;

    }

    public List<Pred> visitPred(Pred pred) {
        List<Pred> predList = new ArrayList<Pred>();
        predList.add(pred);
        return predList;
    }


    public List<Pred> visitExprPred(ExprPred exprPred) {
        Expr expr = exprPred.getExpr();
        return expr.accept(this);
    }


    public List<Pred> visitOrExpr(OrExpr orExpr) {
        List<Pred> predList1 = orExpr.getLeftExpr().accept(this);
        List<Pred> predList2 = orExpr.getRightExpr().accept(this);
        predList1.addAll(predList2);
        return predList1;
    }


    public List<Pred> visitExpr(Expr expr) {
        List<Pred> predList = new ArrayList<Pred>();
        predList.add((new ZFactoryImpl()).createExprPred(expr));
        return predList;
    }
}
