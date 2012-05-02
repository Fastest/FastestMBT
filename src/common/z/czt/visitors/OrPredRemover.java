package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.And;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.AndPredVisitor;
import net.sourceforge.czt.z.visitor.OrPredVisitor;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

/**
 * Given a conjunction P, an instance of this class allow the removal of those clauses from P
 * that are a disjunction such one of its operands is equal to the predicate passed as 
 * argument to the constructor of this class. AndPredClausesRemover is based on the Visitor 
 * design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class OrPredRemover
        implements AndPredVisitor<Pred>,
        OrPredVisitor<Pred>, PredVisitor<Pred> {

    private Pred myPred;

    public OrPredRemover(Pred pred) {
        myPred = pred;
    }

    public Pred visitOrPred(OrPred orPred) {
        Boolean result = orPred.accept(new PredInOrVerifier(myPred));
        if (result.booleanValue()) {
            return null;
        } else {
            return orPred;
        }
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
        return pred;
    }
}
