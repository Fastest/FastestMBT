/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.z.czt.visitors;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import common.z.SpecUtils;



/**
 * An instance of this class allow the comprobation of term containing. This class is based
 * on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class TermExtractor
    implements TermVisitor<Boolean>{
    
    private Term theTerm;
    private Term foundTerm;	
    
    public TermExtractor(Term theTerm){
        this.theTerm = theTerm;
    }

    
	public Boolean visitTerm(Term term){
        
		Object[] array = term.getChildren();
		Boolean result = new Boolean(false);
        Term auxTerm = null;
		for (int i = 0; i < array.length && !result.booleanValue(); i++) {
    		final Object object = array[i];
            if (object instanceof Term){
                auxTerm = (Term) object;  

                boolean b1 = SpecUtils.areEqualTerms(auxTerm, theTerm);
		if(b1)
			foundTerm = auxTerm;
                boolean b2 = auxTerm.accept(this);                            
                
                result = new Boolean(b1 ||b2 );
            }
		}
		return result;
    }

	public Term getTerm()
	{
		return foundTerm;
	}

}