package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.AndPredVisitor;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.AndExprVisitor;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;

/**
 * Instances of this class allow the extraction of the predicates that form a conjunction. The
 * class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class AndPredClausesExtractor
        implements AndPredVisitor<AbstractRepository<Pred>>,
        PredVisitor<AbstractRepository<Pred>>,
        ExprPredVisitor<AbstractRepository<Pred>>,
        AndExprVisitor<AbstractRepository<Pred>>,
        ExprVisitor<AbstractRepository<Pred>>{

    public AbstractRepository<Pred> visitAndPred(AndPred andPred) {
        AbstractRepository<Pred> predRep1 = andPred.getLeftPred().accept(this);
        AbstractRepository<Pred> predRep2 = andPred.getRightPred().accept(this);
        AbstractIterator<Pred> predIt = predRep2.createIterator();
        while (predIt.hasNext()) {
            Pred pred = predIt.next();
            predRep1.addElement(pred);
        }
        return predRep1;
    }

    public AbstractRepository<Pred> visitPred(Pred pred) {
        AbstractRepository<Pred> predRep = new ConcreteRepository<Pred>();
        predRep.addElement(pred);
        return predRep;
    }

    public AbstractRepository<Pred> visitExprPred(ExprPred exprPred) {
        return (exprPred.getExpr()).accept(this);
    }

    public AbstractRepository<Pred> visitAndExpr(AndExpr andExpr) {
        AbstractRepository<Pred> predRep1 = andExpr.getLeftExpr().accept(this);
        AbstractRepository<Pred> predRep2 = andExpr.getRightExpr().accept(this);
        AbstractIterator<Pred> predIt = predRep2.createIterator();
        while (predIt.hasNext()) {
            predRep1.addElement(predIt.next());
        }
        return predRep1;
   }


    public AbstractRepository<Pred> visitExpr(Expr expr) {
        AbstractRepository<Pred> predRep = new ConcreteRepository<Pred>();
        predRep.addElement((new ZFactoryImpl()).createExprPred(expr));
        return predRep;
    }


}
