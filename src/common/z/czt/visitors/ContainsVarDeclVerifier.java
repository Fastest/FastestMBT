package common.z.czt.visitors;

import client.blogic.management.Controller;
import common.z.SpecUtils;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Expr2;
import net.sourceforge.czt.z.ast.InclDecl;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SchExpr;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.z.visitor.Expr2Visitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.SchExprVisitor;

/**
 * Verifies if a variable given by its name is contained in the declaration part
 * of a given schema, considering included schemas and selection of schemas as
 * operations as well as selection of schemas as predicates. Each method of this
 * visitor returns an string with the name of the variable type, null if the 
 * variable is not declared in the expression associated to the method and the 
 * empty string if the type of the variable does not verifies one of the
 * neccessary condition to be a free type: not being represented by a RefExpr.
 * @author Pablo Rodriguez Monetti
 */
public class ContainsVarDeclVerifier implements
        SchExprVisitor<String>,
        Expr2Visitor<String>,
        RefExprVisitor<String>,
        AxParaVisitor<String> {

    private String varNameToFind;
    private ZParaList zParaList;
    private Controller controller;

    public ContainsVarDeclVerifier(String varNameToFind, Spec spec,
            Controller controller) {
        this.varNameToFind = varNameToFind;
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

    public String visitSchExpr(SchExpr schExpr) {
        ZSchText zSchText = schExpr.getZSchText();

        DeclList declList = zSchText.getDeclList();

        String typeName = null;
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int i = 0; i < zDeclList.size() && typeName == null; i++) {
                Decl decl = zDeclList.get(i);
                if (decl instanceof InclDecl) {
                    InclDecl inclDecl = (InclDecl) decl;
                    Expr inclExpr = inclDecl.getExpr();
                    String inclName = "";
                    if (inclExpr instanceof RefExpr) {
                        inclName = ((RefExpr) inclExpr).getName().toString();
                        int firstCharCode = (int) inclName.charAt(0);
                        
                        if (firstCharCode == 916) {
                            //The schema to be expanded is a \Delta
                            inclName = inclName.substring(1);
                        } else if (firstCharCode == 926) {
                            //The schema to be expanded is a \Xi
                            inclName = inclName.substring(1);
                        }

                        boolean isSelOp = controller.isSelectedOperation(inclName);
                        boolean isSelPred = controller.isSelectedPredicate(inclName);

                        if (!isSelOp && !isSelPred) {
                            AxPara inclAxPara = SpecUtils.axParaSearch(inclName,
                                    zParaList);
                            typeName = inclAxPara.accept(this);
                        }
                    }
                } else if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        for (int k = 0; k < zNameListImpl.size() && typeName == null; k++) {
                            String auxVarName = zNameListImpl.get(k).toString();
                            if (auxVarName.equals(varNameToFind)) {
                                Expr expr = varDecl.getExpr();
                                if (expr instanceof RefExpr) {
                                    typeName = ((RefExpr) expr).getName().toString();
                                } else {
                                    typeName = "";
                                }
                            }
                        }
                    }

                }
            }
        }
        return typeName;
    }

    public String visitExpr2(Expr2 expr2) {
        Expr leftExpr = expr2.getLeftExpr();
        Expr rightExpr = expr2.getRightExpr();
        String typeName1 = leftExpr.accept(this);
        String typeName2 = rightExpr.accept(this);

        if (typeName1 == null) {
            return typeName2;
        } else {
            return typeName1;
        }

    }

    public String visitRefExpr(RefExpr refExpr) {
        String refExprName = refExpr.getZName().getWord().toString();

        int firstCharCode = (int) refExprName.charAt(0);
        if (firstCharCode == 916) {
            //The schema to be expanded is a \Delta
            refExprName = refExprName.substring(1);
        } else if (firstCharCode == 926) {
            //The schema to be expanded is a \Xi
            refExprName = refExprName.substring(1);
        }

        boolean isSelOp = controller.isSelectedOperation(refExprName);
        boolean isSelPred = controller.isSelectedPredicate(refExprName);

        String typeName = null;
        if (!isSelOp && !isSelPred) {
            AxPara refAxPara = SpecUtils.axParaSearch(refExprName, zParaList);
            typeName = refAxPara.accept(this);
        }
        return typeName;
    }

    public String visitAxPara(AxPara axPara) {
        String typeName = null;
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();
        if (declList instanceof ZDeclList) {
            for (int j = 0; j < ((ZDeclList) declList).size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    Expr axParaExpr = constDecl.getExpr();
                    typeName = axParaExpr.accept(this);
                }
            }
        }
        return typeName;
    }
}
