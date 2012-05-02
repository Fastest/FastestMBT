/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import common.z.SpecUtils;
import common.z.UtilSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.visitor.AxParaVisitor;

/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class AxDefsClassifier implements AxParaVisitor<Void>,
        TermVisitor<Void> {

    private List<String> basicTypeNames;
    private List<String> freeTypeNames;
    private Map<String, List<String>> basicAxDefs;
    private Map<RefExpr, Expr> axDefsValues;
    private Map<String, Expr> axDefsRequired;
    private List<RefExpr> noBasicAxDefVars;

    public AxDefsClassifier(List<String> basicTypeNames,
            List<String> freeTypeNames,
            Map<String, List<String>> basicAxDefs,
            Map<RefExpr, Expr> axDefsValues,
            Map<String, Expr> axDefsRequired,
            List<RefExpr> noBasicAxDefVars) {

        this.basicTypeNames = basicTypeNames;
        this.freeTypeNames = freeTypeNames;
        this.basicAxDefs = basicAxDefs;
        this.axDefsValues = axDefsValues;
        this.axDefsRequired = axDefsRequired;
        this.noBasicAxDefVars = noBasicAxDefVars;
    }

    public Void visitAxPara(AxPara axPara) {
        if (axPara.getBox() == Box.AxBox) {
            // Obtenemos el Map<RefExpr,Expr> m con pares
            // (var, val) del pred de la def axiomatica
            ZSchText zSchText = axPara.getZSchText();
            Pred axDefPred = zSchText.getPred();
            Map<String, Expr> assignedValues =
                    SpecUtils.getAssignedValues(axDefPred);
            DeclList declList = zSchText.getDeclList();
            if (declList instanceof ZDeclList) {
                ZFactory zFactory = new ZFactoryImpl();
                ZDeclList zDeclList = (ZDeclList) declList;
                String natStr = UtilSymbols.naturalSymbol();
                String intStr = UtilSymbols.integerSymbol();
                for (int j = 0; j < zDeclList.size(); j++) {
                    // Para cada declaracion d
                    Decl decl = zDeclList.get(j);
                    if (decl instanceof VarDecl) {
                        VarDecl varDecl = (VarDecl) decl;
                        Expr typeExpr = varDecl.getExpr();
                        String strCategory = "other";
                        RefExpr typeRefExpr = null;
                        String typeStr = null;

                        if (typeExpr instanceof RefExpr) {
                            typeRefExpr = (RefExpr) typeExpr;
                            typeStr = typeRefExpr.getName().toString();
                            if (basicTypeNames.contains(typeStr)) {
                                strCategory = "basic";
                            } else if (freeTypeNames.contains(typeStr)
                                    || typeStr.equals(natStr)
                                    || typeStr.equals(intStr)) {
                                strCategory = "freeNatOrNum";
                            }
                        }

                        NameList nameList = varDecl.getName();


                        if (nameList instanceof ZNameListImpl) {
                            ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                            for (int k = 0; k < zNameListImpl.size(); k++) {
                                Name name = zNameListImpl.get(k);
                                ZExprList auxZExprList = zFactory.createZExprList();
                                RefExpr varRefExpr = zFactory.createRefExpr(name, auxZExprList,
                                        Boolean.TRUE, Boolean.TRUE);
                                String varName = varRefExpr.getName().toString();

                                Expr varValue = assignedValues.get(varName);
                                if (strCategory.equals("basic")) {
                                    // If the variable type is basic
                                    //  we add its name to the list of
                                    // constants associated to the basic type
                                    List<String> constantsList = basicAxDefs.get(typeStr);
                                    if (constantsList == null) {
                                        constantsList = new ArrayList<String>();
                                    }
                                    constantsList.add(varRefExpr.getName().toString());
                                    basicAxDefs.put(typeStr, constantsList);

                                } else {

                                    noBasicAxDefVars.add(varRefExpr);

                                    if (strCategory.equals("freeNatOrNum")
                                            && varValue != null) {
                                        // If the variable type is free or \nat or
                                        // \num and it has a value assigned within
                                        // the predicate we add its name to the
                                        // map of variable values
                                        axDefsValues.put(varRefExpr, varValue);

                                    } else if (strCategory.equals("freeNatOrNum")
                                            || strCategory.equals("other")) {
                                        // If the variable type is free or \nat or
                                        // \num and it has not a value assigned within
                                        // the predicate we add its name to the
                                        // map of variables which need a value
                                        axDefsRequired.put(varName, typeExpr);
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
        return null;
    }



    public Void visitTerm(Term term) {
        VisitorUtils.visitTerm(this, term, false);
        return null;
    }


}
