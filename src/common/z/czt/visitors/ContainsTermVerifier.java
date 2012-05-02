
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import common.z.SpecUtils;

/**
 * An instance of this class allows the verification of term containing another 
 * term.
 * 
 * The verification is performed recursively and is based on the Visitor design 
 * pattern.
 * 
 * @author Pablo Rodriguez Monetti
 */
public class ContainsTermVerifier
        implements TermVisitor<Boolean> {

    // The term to find
    private Term theTerm;

    public ContainsTermVerifier() {
    }

    public ContainsTermVerifier(Term theTerm) {
        this.theTerm = theTerm;
    }

    public Boolean visitTerm(Term term) {
        Object[] array = term.getChildren();
        
        if (SpecUtils.areEqualTerms(term, theTerm)) {
            return true;
        }

        for (int i = 0; i < array.length; i++) {
            final Object object = array[i];
            if (object instanceof Term) {
                Term auxTerm = (Term) object;
                boolean equals = SpecUtils.areEqualTerms(auxTerm, theTerm);
                boolean contains = auxTerm.accept(this);
                if(equals || contains)
                    return true;
            }
        }
        return false;
    }

    public void setTerm(Term term) {
        this.theTerm = term;
    }
}
