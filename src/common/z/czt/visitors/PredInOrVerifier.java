package common.z.czt.visitors;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.OrPredVisitor;

import common.z.*;

/**
 * An instance of this class allow the comprobation that a predicate P is a disjunction such
 * that the predicate passed as argument to the constructor of this class is equal to one
 * of its operands. PredInOrVerifier is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class PredInOrVerifier	
	implements  OrPredVisitor<Boolean>, PredVisitor<Boolean>{

	private Pred myPred;

	public PredInOrVerifier(Pred pred){
		myPred = pred;
	}

	public Boolean visitOrPred(OrPred orPred){
		Pred leftPred = orPred.getLeftPred();
		Pred rightPred = orPred.getRightPred();
		boolean leftResult = false;
		boolean rightResult = false;
		if(leftPred instanceof OrPred)
			leftResult = ((OrPred)leftPred).accept(this);
		else
			leftResult = SpecUtils.areEqualTerms(myPred, leftPred);

		if(leftResult)
			return true;

		if(rightPred instanceof OrPred)
			rightResult = ((OrPred)rightPred).accept(this);
		else
			rightResult = SpecUtils.areEqualTerms(myPred, rightPred);

		return rightResult;

	}

	public Boolean visitPred(Pred pred){
		return false;
	}
} 
 
