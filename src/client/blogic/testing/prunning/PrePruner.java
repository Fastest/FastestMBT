/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.blogic.testing.prunning;

import common.z.TClass;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.NegPred;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.NegExpr;
import net.sourceforge.czt.z.ast.RefExpr;
import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import common.z.czt.visitors.AndPredClausesExtractor;

/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class PrePruner {

    public static boolean isTClassEmpty(TClass tClass) {
        // We check whether the explicit predicate (without schema unfolding)
        // of the specified test class is a conjunction of predicates in which
        // there is an explicit contradiction (ie. a predicate p and the
        // predicate ~p in the same conjunction) or not.
        Pred pred = SpecUtils.getAxParaPred(tClass.getMyAxPara());

        if (!(pred instanceof AndPred)) {
            return false;
        }

        AbstractRepository<Pred> predRep =
                pred.accept(new AndPredClausesExtractor());
        AbstractIterator<Pred> predRepIterator1 = predRep.createIterator();
        boolean contradiction = false;
        while (predRepIterator1.hasNext() && !contradiction) {
            Pred auxPred1 = predRepIterator1.next();
            if (auxPred1 instanceof NegPred) {
                Pred predToFind = ((NegPred) auxPred1).getPred();

                AbstractIterator<Pred> predRepIterator2 =
                        predRep.createIterator();

                while (predRepIterator2.hasNext() && !contradiction) {
                    Pred auxPred2 = predRepIterator2.next();

                    if (SpecUtils.areEqualTerms(auxPred2, predToFind)) {
                        contradiction = true;
                    }
                }
            }
        }
        return contradiction;
    }
    public static boolean hasSchemaContradictions(TClass tClass){
        // We check whether the explicit predicate (without schema unfolding)
        // of the specified test class is a conjunction of predicates in which
        // there is an explicit contradiction (ie. a schema predicate P and the
        // schema predicate ~P in the same conjunction) or not.
        Pred pred = SpecUtils.getAxParaPred(tClass.getMyAxPara());
        if(!(pred instanceof AndPred))
            return false;
        AbstractRepository<Pred> predRep =
            pred.accept(new AndPredClausesExtractor());
        AbstractIterator<Pred> predRepIterator1 = predRep.createIterator();
        boolean contradiction = false;
        while(predRepIterator1.hasNext() && !contradiction){
            Pred auxPred1 = predRepIterator1.next();
            if (auxPred1 instanceof ExprPred){
		ExprPred exprPred1 = (ExprPred) auxPred1;
		if(exprPred1.getExpr() instanceof NegExpr){
			NegExpr negExpr1 = (NegExpr) exprPred1.getExpr();
                	if(negExpr1.getExpr() instanceof RefExpr){
				RefExpr refExpr1 = (RefExpr) negExpr1.getExpr();
				String nameToFind = refExpr1.getZName().getWord();
				AbstractIterator<Pred> predRepIterator2 =
					predRep.createIterator();
		
				while(predRepIterator2.hasNext() && !contradiction){
				Pred auxPred2 = predRepIterator2.next();
				if(auxPred2 instanceof ExprPred){
					ExprPred exprPred2 = (ExprPred) auxPred2;
					if(exprPred2.getExpr() instanceof RefExpr){
						RefExpr refExpr2 = (RefExpr) exprPred2.getExpr();
						String name = refExpr2.getZName().getWord();
						if(!name.equals("") && name.equals(nameToFind))
							contradiction = true;
					}	
				}
				}
			}
            	}
	    }
        }
        return contradiction;
    }

}
