/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import client.blogic.management.Controller;
import common.fastest.FastestUtils;
import common.z.SpecUtils;
import java.util.List;
import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.DecorExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Expr2;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.IffExpr;
import net.sourceforge.czt.z.ast.ImpliesExpr;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.ast.PreExpr;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Pred2;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.DecorExprVisitor;
import net.sourceforge.czt.z.visitor.Expr2Visitor;
import net.sourceforge.czt.z.visitor.ExprPredVisitor;
import net.sourceforge.czt.z.visitor.Pred2Visitor;
import net.sourceforge.czt.z.visitor.PredVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;

/**
 * This class was made to traverse a predicate replacing each schema reference
 * that is used as a predicate, providing that the referred schema is not
 * selected as operation nor as predicate. This class is used in the calculus
 * of the Valid Input Space (VIS) of operations to be tested, so the replacements
 * of schema references with predicates try to avoid the inclusion of atomic 
 * predicates with output or primed variables, like in the case of a reference
 * of the form \Delta A or \Xi A, cases in which the appearing predicates would
 * not include the "output parts".
 * @author Pablo Rodriguez Monetti
 */
public class PredSchemasUnfolder implements PredVisitor<Pred>,
        Pred2Visitor<Pred>, ExprPredVisitor<Pred>, Expr2Visitor<Pred>,
        RefExprVisitor<Pred>, DecorExprVisitor<Pred> {

    private ZParaList zParaList;
    private Controller controller;

    public PredSchemasUnfolder(ZParaList zParaList, Controller controller) {
        this.zParaList = zParaList;
        this.controller = controller;
    }

    public Pred visitPred2(Pred2 pred2) {
        Pred newLeftPred = pred2.getLeftPred().accept(this);
        Pred newRightPred = pred2.getRightPred().accept(this);

        if (newLeftPred == null) {
            return newRightPred;
        } else if (newRightPred == null) {
            return newLeftPred;
        } else {
            pred2.setLeftPred(newLeftPred);
            pred2.setRightPred(newRightPred);
            return pred2;
        }
    }

    public Pred visitExprPred(ExprPred exprPred) {
        return exprPred.getExpr().accept(this);
    }

    public Pred visitExpr2(Expr2 expr2) {
        Pred newLeftPred = expr2.getLeftExpr().accept(this);
        Pred newRightPred = expr2.getRightExpr().accept(this);

        if (newLeftPred == null) {
            return newRightPred;
        } else if (newRightPred == null) {
            return newLeftPred;
        } else {
            ZFactory zFactory = new ZFactoryImpl();
            if (newLeftPred instanceof ExprPred
                    && newRightPred instanceof ExprPred) {
                Expr newLeftExpr = ((ExprPred) newLeftPred).getExpr();
                Expr newRightExpr = ((ExprPred) newRightPred).getExpr();
                expr2.setLeftExpr(newLeftExpr);
                expr2.setRightExpr(newRightExpr);
                return zFactory.createExprPred(expr2);
            } else {
                Pred2 newPred = null;
                if (expr2 instanceof OrExpr) {
                    newPred = zFactory.createOrPred();
                } else if (expr2 instanceof AndExpr) {
                    newPred = zFactory.createAndPred();
                } else if (expr2 instanceof ImpliesExpr) {
                    newPred = zFactory.createImpliesPred();
                } else if (expr2 instanceof IffExpr) {
                    newPred = zFactory.createIffPred();
                }

                newPred.setLeftPred(newLeftPred);
                newPred.setRightPred(newRightPred);

                return newPred;
            }

        }
    }

    public Pred visitRefExpr(RefExpr refExpr) {
        String refExprName = refExpr.getZName().getWord().toString();

        int firstCharCode = (int) refExprName.charAt(0);
        boolean deltaOrXi = false;
        if (firstCharCode == 916 || firstCharCode == 926) {
            // The schema is a Delta or a Xi
            refExprName = refExprName.substring(1);
            deltaOrXi = true;
        }

        boolean isSelOp = controller.isSelectedOperation(refExprName);
        boolean isSelPred = controller.isSelectedPredicate(refExprName);
        boolean isOp = FastestUtils.isLoadedOperation(controller, refExprName);

        Pred newPred = null;
        ZFactory zFactory = new ZFactoryImpl();

        if (isSelOp || (isSelPred && isOp)) {
            // The schema reference A is replaced with
            // the reference 'pre A' because it is an operation
            // selected as operation or as predicate and,
            // consequently it would contains output
            // variables
            PreExpr preExpr = zFactory.createPreExpr(refExpr);
            newPred = zFactory.createExprPred(preExpr);
        } else if (isSelPred && !isOp) {
            // The schema reference A is selected as a
            // a predicate so it will not be replaced by its
            // predicate
            // If the reference is not decorated it remains
            // unchanged; while if it is decorated with a
            // Delta or Xi then the Delta or Xi is removed
            RefExpr newRefExpr = refExpr;
            if (deltaOrXi) {
                ZName zName = zFactory.createZName(refExprName,
                        zFactory.createZStrokeList(), refExprName);
                ZExprList zExprList = zFactory.createZExprList();
                newRefExpr = zFactory.createRefExpr(zName,
                        zExprList, false, false);
            }
            newPred = zFactory.createExprPred(newRefExpr);
        } else { /* if(!isSelOp && !isSelPred)   */
            // The schema reference A is replaced with
            // its predicate (without output vars) because
            // it is not an schema selected as operation
            // or predicate
            List<Pred> dnfPredList =
                    (SpecUtils.axParaSearch(refExprName, zParaList)).accept(new DNFPredExtractor(zParaList, controller));
            newPred = SpecUtils.createAndPred(dnfPredList);
        }

        return newPred;
    }


    public Pred visitDecorExpr(DecorExpr refExpr) {
        // The schema reference is primed so it returns
        // the predicate true
        return null;
    }

    public Pred visitPred(Pred pred) {
        return pred;
    }
}
