package common.z.czt.visitors;

import common.z.UtilSymbols;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.visitor.ZNameVisitor;

import net.sourceforge.czt.z.ast.ZName;


/**
 * An instance of this class allow the search for alphanumeric constants in a
 * expression. Those constants are specified through the repository that is
 * passed as argument to the constructor of this class. WordsFinder is based
 * on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class OutputVarsFinder
        implements TermVisitor<Boolean>,
        ZNameVisitor<Boolean> {


    public Boolean visitTerm(Term term) {
        Object[] array = term.getChildren();
        Boolean result = false;
        for (int i = 0; i < array.length && !result.booleanValue(); i++) {
            final Object object = array[i];
            if (object instanceof Term) {
                result = ((Term) object).accept(this);
            }
        }
        return result;
    }


    public Boolean visitZName(ZName zName) {
        String zNameWord = zName.toString();
        return zNameWord.endsWith("!") ||
                zNameWord.endsWith(UtilSymbols.primeSymbol());
    }

}
