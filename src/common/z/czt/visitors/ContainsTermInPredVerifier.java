package common.z.czt.visitors;

import client.blogic.management.Controller;
import common.z.SpecUtils;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.DecorExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Expr2;
import net.sourceforge.czt.z.ast.InclDecl;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SchExpr;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.z.visitor.Expr2Visitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.SchExprVisitor;

/**
 * Verifies if a given term is contained in the predicate of a given schema. 
 * The verification considers inclusion of schemas that are not selected as 
 * operations nor as predicates.
 * 
 * The verification is performed recursively and is based on the Visitor design 
 * pattern.
 * 
 * @author Pablo Rodriguez Monetti
 */
public class ContainsTermInPredVerifier implements
        SchExprVisitor<Boolean>,
        Expr2Visitor<Boolean>,
        RefExprVisitor<Boolean>,
        AxParaVisitor<Boolean> {
    // The term to find
    private Term termToFind;
    // A reference to the list of paragraphs of the loaded specification
    private ZParaList zParaList;
    // A reference to the current instance of Controller, which is used to
    // determine if a given schema reference correspond or not to a schema 
    // selected as operation or as predicate.     
    private Controller controller;

    public ContainsTermInPredVerifier(Term termToFind, Spec spec,
            Controller controller) {
        this.termToFind = termToFind;
        for (Sect sect : spec.getSect()) {
            if (sect instanceof ZSect) {
                ParaList paraList = ((ZSect) sect).getParaList();
                if (paraList instanceof ZParaList) {
                    zParaList = (ZParaList) paraList;
                }
            }
        }
        this.controller = controller;
    }

    public Boolean visitSchExpr(SchExpr schExpr) {
        ZSchText zSchText = schExpr.getZSchText();
        Pred pred = zSchText.getPred();

        Boolean termInPred = pred.accept(new ContainsTermVerifier(termToFind)).
                booleanValue();
        if (termInPred.booleanValue()) {
            return termInPred;
        }

        DeclList declList = zSchText.getDeclList();

        Boolean termInInclSchema = false;
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int i = 0; i < zDeclList.size() && !termInInclSchema; i++) {
                Decl decl = zDeclList.get(i);
                if (decl instanceof InclDecl) {
                    InclDecl inclDecl = (InclDecl) decl;
                    Expr inclExpr = inclDecl.getExpr();
                    String inclName = "";
                    if (inclExpr instanceof RefExpr) {
                        inclName = ((RefExpr) inclExpr).getName().toString();
                        int firstCharCode = (int) inclName.charAt(0);
                        if (firstCharCode == 916 || firstCharCode == 926) {
                            // The schema is a \Delta or a \Xi
                            inclName = inclName.substring(1);
                        }
                    } else if (inclExpr instanceof DecorExpr) {
                        inclName = ((RefExpr) ((DecorExpr) inclExpr).getExpr()).
                                getName().toString();
                    }

                    boolean isSelOp = controller.isSelectedOperation(inclName);
                    boolean isSelPred = controller.isSelectedPredicate(inclName);

                    // The verification will be performed only if the included 
                    //schema is not selected as operation or as predicate                    
                    if (!isSelOp && !isSelPred) {
                        AxPara inclAxPara = SpecUtils.axParaSearch(inclName,
                                zParaList);
                        termInInclSchema = inclAxPara.accept(this);
                    }

                }
            }
        }
        return termInInclSchema;
    }

    public Boolean visitExpr2(Expr2 expr2) {
        Expr leftExpr = expr2.getLeftExpr();
        Expr rightExpr = expr2.getRightExpr();
        Boolean termInLeftExpr = leftExpr.accept(this);
        Boolean termInRightExpr = rightExpr.accept(this);
        return termInLeftExpr.booleanValue()
                || termInRightExpr.booleanValue();
    }

    public Boolean visitRefExpr(RefExpr refExpr) {
        String refExprName = refExpr.getZName().getWord().toString();

        int firstCharCode = (int) refExprName.charAt(0);
        if (firstCharCode == 916 || firstCharCode == 926) {
            // The schema is a Delta or a Xi
            refExprName = refExprName.substring(1);
        }

        boolean isSelOp = controller.isSelectedOperation(refExprName);
        boolean isSelPred = controller.isSelectedPredicate(refExprName);

        // The verification will be performed only if the referred schema is not
        // selected as operation or as predicate
        Boolean termInPred = false;
        if (!isSelOp && !isSelPred) {
            AxPara refAxPara = SpecUtils.axParaSearch(refExprName, zParaList);
            termInPred = refAxPara.accept(this);
        }
        return termInPred;
    }

    public Boolean visitAxPara(AxPara axPara) {
        Boolean termInAxPara = null;
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();
        if (declList instanceof ZDeclList) {
            for (int j = 0; j < ((ZDeclList) declList).size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    Expr axParaExpr = constDecl.getExpr();
                    termInAxPara = axParaExpr.accept(this);
                }
            }
        }
        return termInAxPara;
    }
}
