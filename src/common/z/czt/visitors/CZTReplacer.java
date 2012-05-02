package common.z.czt.visitors;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;

import common.z.SpecUtils;


/** 
 * An instance of this class allow the replacement of terms within the context of a third 
 * term. This class is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class CZTReplacer	
	implements  TermVisitor<Term>{

	private Term origTerm;
	private Term newTerm;

	public void setOrigTerm(Term origTerm){
		this.origTerm = origTerm;
	}

	public void setNewTerm(Term newTerm){
		this.newTerm = newTerm;
	}

	public Term getOrigTerm(){
		return origTerm;
	}

	public Term getNewTerm(){
		return newTerm;
	}


	public Term visitTerm(Term term){
			
		if(SpecUtils.areEqualTerms(term,origTerm)){
			return newTerm.accept(new CZTCloner());
		}
		return VisitorUtils.visitTerm(this, term, false);
	}

}

