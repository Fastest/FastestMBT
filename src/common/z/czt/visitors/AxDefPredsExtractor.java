/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.visitor.AxParaVisitor;

/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class AxDefPredsExtractor implements AxParaVisitor<Void>,
        TermVisitor<Void> {

    private Map<String, List<Pred>> axDefsRequiredPreds;
    private Map<Pred, List<String>> axDefsPredVars;
    private List<RefExpr> noBasicAxDefVars;

    public AxDefPredsExtractor(Map<String, List<Pred>> axDefsRequiredPreds,
            Map<Pred, List<String>> axDefsPredVars,
            List<RefExpr> noBasicAxDefVars) {
        this.axDefsRequiredPreds = axDefsRequiredPreds;
        this.axDefsPredVars = axDefsPredVars;
        this.noBasicAxDefVars = noBasicAxDefVars;
    }

    public Void visitAxPara(AxPara axPara) {

        if (axPara.getBox() == Box.AxBox) {

            ZSchText zSchText = axPara.getZSchText();
            Pred axDefPred = zSchText.getPred();

            // We add the pred p of this axdef to the predicate
            // list of each not basic variable that is
            // contained in p
            List<String> predVarList = new ArrayList<String>();
            ContainsTermVerifier containsTermVerif = new ContainsTermVerifier();
            if (axDefPred != null) {
                for (int k = 0; k < noBasicAxDefVars.size(); k++) {
                    RefExpr varNameRefExpr = noBasicAxDefVars.get(k);
                    containsTermVerif.setTerm(varNameRefExpr);
                    if (axDefPred.accept(containsTermVerif)) {
                        String varName = varNameRefExpr.getZName().toString();
                        List<Pred> predList = axDefsRequiredPreds.get(varName);
                        if (predList == null) {
                            predList = new ArrayList<Pred>();
                        }
                        predList.add(axDefPred);
                        axDefsRequiredPreds.put(varName, predList);
                        predVarList.add(varName);
                    }
                }
                // and we associate to p the list
                // of not basic variables contained in p
                axDefsPredVars.put(axDefPred, predVarList);
            }
        }
        return null;
    }

    public Void visitTerm(Term term) {
        VisitorUtils.visitTerm(this, term, false);
        return null;
    }
}
