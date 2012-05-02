package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.visitor.PredVisitor;


import java.util.ArrayList;
import java.util.List;
import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.AndPred;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.AndExprVisitor;
import net.sourceforge.czt.z.visitor.AndPredVisitor;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;

/**
 * Instances of this class allow the extraction of the predicates that form a disjunction. The
 * class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class AndPredClausesExtractor2
        implements AndPredVisitor<List<Pred>>,
        PredVisitor<List<Pred>>,
        ExprPredVisitor<List<Pred>>,
        AndExprVisitor<List<Pred>>,
        ExprVisitor<List<Pred>>{

    public List<Pred> visitAndPred(AndPred andPred) {
        //System.out.println(SpecUtils.termToLatex(orPred));
        Pred leftPred = andPred.getLeftPred();
        Pred rightPred = andPred.getRightPred();

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


    public List<Pred> visitAndExpr(AndExpr andExpr) {
        List<Pred> predList1 = andExpr.getLeftExpr().accept(this);
        List<Pred> predList2 = andExpr.getRightExpr().accept(this);
        predList1.addAll(predList2);
        return predList1;
    }


    public List<Pred> visitExpr(Expr expr) {
        List<Pred> predList = new ArrayList<Pred>();
        predList.add((new ZFactoryImpl()).createExprPred(expr));
        return predList;
    }
}
