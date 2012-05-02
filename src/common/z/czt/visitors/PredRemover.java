package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.AndPredVisitor;
import net.sourceforge.czt.z.visitor.OrPredVisitor;
import common.repository.AbstractRepository;

/**
 * Given a predicate P in Conjunctive Normal Form (CNF), an instance of this class allow the 
 * removal of those literals of P that contains any of the variables listed in a repository of
 * variable names. This class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class PredRemover
        implements PredVisitor<Pred>, OrPredVisitor<Pred>, AndPredVisitor<Pred> {

    private AbstractRepository<String> varNamesRep;

    public PredRemover(AbstractRepository<String> varNamesRep) {
        this.varNamesRep = varNamesRep;
    }

    public Pred visitOrPred(OrPred orPred) {
        Pred leftPred = orPred.getLeftPred().accept(this);
        Pred rightPred = orPred.getRightPred().accept(this);
        if (leftPred == null) {
            return rightPred;
        } else if (rightPred == null) {
            return leftPred;
        } else {
            orPred.setLeftPred(leftPred);
            orPred.setRightPred(rightPred);
            return orPred;
        }
    }

    public Pred visitAndPred(AndPred andPred) {
        Pred leftPred = andPred.getLeftPred().accept(this);
        Pred rightPred = andPred.getRightPred().accept(this);
        if (leftPred == null) {
            return rightPred;
        } else if (rightPred == null) {
            return leftPred;
        } else {
            andPred.setLeftPred(leftPred);
            andPred.setRightPred(rightPred);
            return andPred;
        }
    }

    public Pred visitPred(Pred pred) {
        WordsFinder wordsFinderVisitor = new WordsFinder(varNamesRep);
        if (pred.accept(wordsFinderVisitor)) {
            return null;
        } else {
            return pred;
        }
    }
}
