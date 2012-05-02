package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.And;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.AndPredVisitor;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.SpecUtils;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;

/**
 * An instance of this class allow the application of the rules of conjunction 
 * idempotence  (p1 /\ p1 = p1) and conjunction absortion  (p1 /\ (p1 \/ p2) =
 * p1). This class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class AndPredSimplifier
        implements AndPredVisitor<Pred>,
        PredVisitor<Pred> {

    public Pred visitAndPred(AndPred andPred) {
        Pred leftPred = andPred.getLeftPred();
        Pred rightPred = andPred.getRightPred();

        if (!(leftPred instanceof AndPred) && !(rightPred instanceof AndPred)) {
            if (SpecUtils.areEqualTerms(leftPred, rightPred)) {
                return leftPred;
            } else if (rightPred.accept(new PredInOrVerifier(leftPred)).booleanValue()) {
                return leftPred;
            } else if (leftPred.accept(new PredInOrVerifier(rightPred)).booleanValue()) {
                return rightPred;
            } else {
                return andPred;
            }
        } else if (!(leftPred instanceof AndPred) && rightPred instanceof AndPred) {
            boolean found = false;
            if (leftPred instanceof OrPred) {
                AbstractRepository<Pred> predRep = rightPred.accept(new AndPredClausesExtractor());
                AbstractIterator<Pred> predIt = predRep.createIterator();
                found = leftPred.accept(new PredInOrVerifier(rightPred));
                while (predIt.hasNext() && !found) {
                    found = leftPred.accept(new PredInOrVerifier(predIt.next())).booleanValue();
                }
            }

            if (found) {
                return rightPred.accept(this);
            }

            rightPred = rightPred.accept(new AndPredClausesRemover(leftPred));
            if (rightPred != null) {
                rightPred = rightPred.accept(new OrPredRemover(leftPred));
            }

            if (rightPred != null) {
                rightPred = rightPred.accept(this);
            }

            if (rightPred == null) {
                return leftPred;
            }

            AndPred andPred2 = (new ZFactoryImpl()).createAndPred();
            andPred2.setLeftPred(leftPred);
            andPred2.setRightPred(rightPred);
            andPred2.setAnd(And.NL);
            return andPred2;

        } else if (leftPred instanceof AndPred && !(rightPred instanceof AndPred)) {

            boolean found = false;
            if (rightPred instanceof OrPred) {
                AbstractRepository<Pred> predRep = leftPred.accept(new AndPredClausesExtractor());
                AbstractIterator<Pred> predIt = predRep.createIterator();
                while (predIt.hasNext() && !found) {
                    found = rightPred.accept(new PredInOrVerifier(predIt.next())).booleanValue();
                }
                if (found) {
                    return leftPred.accept(this);
                }
            }

            leftPred = leftPred.accept(new AndPredClausesRemover(rightPred));

            if (leftPred != null) {
                leftPred = leftPred.accept(new OrPredRemover(rightPred));
            }

            if (leftPred != null) {
                leftPred = leftPred.accept(this);
            }

            if (leftPred == null) {
                return rightPred;
            }

            AndPred andPred2 = (new ZFactoryImpl()).createAndPred();
            andPred2.setLeftPred(leftPred);
            andPred2.setRightPred(rightPred);
            andPred2.setAnd(And.NL);
            return andPred2;
        } else {

            Pred leftPredClone = (Pred) leftPred.accept(new CZTCloner());

            AbstractRepository<Pred> rightPredRep = rightPred.accept(new AndPredClausesExtractor());
            AbstractIterator<Pred> rightPredIt = rightPredRep.createIterator();

            while (rightPredIt.hasNext() && leftPred != null) {
                leftPred = leftPred.accept(new OrPredRemover(rightPredIt.next()));
            }

            if (leftPred != null) {
                leftPred = leftPred.accept(this);
            }
            AbstractRepository<Pred> leftPredRep = leftPredClone.accept(new AndPredClausesExtractor());
            AbstractIterator<Pred> leftPredIt = leftPredRep.createIterator();
            while (leftPredIt.hasNext() && rightPred != null) {
                Pred clauseToRemove = leftPredIt.next();
                rightPred = rightPred.accept(new AndPredClausesRemover(clauseToRemove));
                if (rightPred != null) {
                    rightPred = rightPred.accept(new OrPredRemover(clauseToRemove));
                }
            }

            if (rightPred != null) {
                rightPred = rightPred.accept(this);
            }

            if (leftPred == null) {
                return rightPred;
            }

            if (rightPred == null) {
                return leftPred;
            }

            AndPred andPred2 = (new ZFactoryImpl()).createAndPred();
            andPred2.setLeftPred(leftPred);
            andPred2.setRightPred(rightPred);
            andPred2.setAnd(And.NL);
            return andPred2;
        }
    }

    public Pred visitPred(Pred pred) {
        //   System.out.println("pred es " + SpecUtils.termToLatex(pred));
        return pred;
    }
}
