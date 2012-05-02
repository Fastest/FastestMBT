package common.z.czt.visitors;

import java.util.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.base.visitor.VisitorUtils;


/**
 * An instance of this class allow the deep cloning of terms. This class is based on the 
 * Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class CZTCloner	
	implements TermVisitor<Term>
{

	private void visitAnns(Term term, Term result){
    	List anns = result.getAnns();
    	for (Object o : term.getAnns()) {
    		if (o instanceof Term) {
        		anns.add(((Term) o).accept(this));
    		}
    		else {
        		anns.add(o);
    		}
    	}
  	}


	public Term visitTerm(Term term){
    	Term result = VisitorUtils.visitTerm(this, term, false);
    	//visitAnns(term, result);
    	return result;
	}



}