/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import common.z.OpSchemeImpl;
import common.z.SpecUtils;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.visitor.AxParaVisitor;

/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class ParenthesisRemover implements AxParaVisitor<Void>,
        TermVisitor<Void> {

    public Void visitAxPara(AxPara axPara) {
        if (OpSchemeImpl.isOpScheme(axPara)) {
            //Eliminamos parentesis extras
            Pred pred = SpecUtils.getAxParaPred(axPara);
            if(pred != null){
                pred = (Pred) pred.accept(new DeleteParenAnn());
                SpecUtils.setAxParaPred(axPara, pred);
            }

        }
        return null;
    }

    public Void visitTerm(Term term) {
        VisitorUtils.visitTerm(this, term, false);
        return null;
    }
}
