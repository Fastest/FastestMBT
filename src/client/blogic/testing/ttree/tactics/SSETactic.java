package client.blogic.testing.ttree.tactics;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ExprList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZStrokeList;
import net.sourceforge.czt.z.impl.ZExprListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.session.CommandException;

import common.z.SpecUtils;
import common.z.TClass;
import common.z.TClassImpl;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.UniqueZLive;
import common.z.czt.visitors.ContainsTermInPredVerifier;
import common.z.czt.visitors.ParamExtractor;

/**
 * Implementation of the tactic Subset of Set Extension (SSE).
 * @author Pablo Rodriguez Monetti
 */
public class SSETactic extends AbstractTactic {


    private SSETacticInfo sseTacticInfo;

    /**
     * Creates new instances of SSETactic.
     */
    public SSETactic() {
        sseTacticInfo = new SSETacticInfo();
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* SSE (Subset of Set Extension). It is identical to PSSE tactic \n";
        description += "  but it applies to predicates of the form  \n";
        description += "  expr \\subseteq \\{expr(1),...,expr(n)\\} in which case\\}\n";
        description += "  it generate 2^n by considering also \\{expr(1),...,expr(n)\\}.\n";
        description += "  Usage: addtactic <op_name> SSE <expression>\n";
        description += "  where op_name is the name of a selected operation\n";
        description += "  and expression is a Z expression written in Latex.\n";
        description += "  It is assumed that  \\subseteq operator appears in the expression\n";
        description += "  and this in turn appears in the predicate\n";
        description += "  of the selected operation.\n";
    }

    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param tClass
     * @return
     */
    public List<TClass> applyTactic(TClass tClass) {
        List<TClass> tClassList = new ArrayList<TClass>();
        ZFactory zFactory = new ZFactoryImpl();
        AxPara opAxPara = (AxPara) originalOp.getMyAxPara().accept(new CZTCloner());
        AxPara tClassAxPara = (AxPara) tClass.getMyAxPara().accept(new CZTCloner());
        String opName = SpecUtils.getAxParaName(opAxPara);

        Integer tClassInteger = tClassNumbersMap.get(opName);
        if (tClassInteger == null) {
            tClassInteger = new Integer(1);
            tClassNumbersMap.put(opName, tClassInteger);
        }

        ExprList listExpr = sseTacticInfo.getSet().getExprList();
        if (listExpr instanceof ZExprListImpl) {
            ZExprListImpl zlistExpr = (ZExprListImpl) listExpr;
            String varNameStr = sseTacticInfo.getVar();
            ZExprList zExprList = zFactory.createZExprList();
            ZName varZName = null;
            ZStrokeList zStrokeList = zFactory.createZStrokeList();
            if (varNameStr.endsWith("?")) {
                zStrokeList.add(zFactory.createInStroke());
                // We remove the ? character from the variable name
                varNameStr = varNameStr.substring(0, varNameStr.length() - 1);
                varZName = zFactory.createZName(varNameStr, zStrokeList, "neg");
            } else {
                varZName = zFactory.createZName(varNameStr, zFactory.createZStrokeList(), "neg");
            }

            RefExpr varRefExpr = zFactory.createRefExpr(varZName, zExprList, false, false);

            int numNewClasses = (int) Math.pow(2, zlistExpr.size());

            for (int j = 0; j < numNewClasses; j++) {

                //We create a ZExprList with a set from the powerset of zlistExpr
                ZExprList setZExprList = zFactory.createZExprList();
                String indexSet = intToBinary(j, zlistExpr.size());
                for (int i = 0; i < zlistExpr.size(); i++) {
                    if (indexSet.charAt(i) == '1') {
                        setZExprList.add(zlistExpr.get(i));
                    }
                }

                //We create the SetExpr that contains the right expr
                SetExpr setExpr = zFactory.createSetExpr(setZExprList);
                ZExprList supersetZExprList = zFactory.createZExprList();
                supersetZExprList.add(setExpr);
                SetExpr supersetExpr = zFactory.createSetExpr(supersetZExprList);

                //We create the MemPred that will be added to the new test class
                ZExprList memPredExprList = zFactory.createZExprList();
                memPredExprList.add(0, varRefExpr);
                memPredExprList.add(1, supersetExpr);
                MemPred memPred = zFactory.createMemPred(memPredExprList, true);

                int tClassNumber = tClassNumbersMap.get(opName).intValue();
                String tClassName = SpecUtils.getAxParaName(opAxPara) + "_SSE_"
                        + tClassNumber;
                tClassNumbersMap.put(opName, tClassNumber + 1);

                Pred newAxParaPred = SpecUtils.getAxParaPred(tClassAxPara);
                SpecUtils.setAxParaPred(tClassAxPara, SpecUtils.andPreds(newAxParaPred, memPred));


                // The test class is added to the list of test classes
                TClass auxTClass = new TClassImpl((AxPara) tClass.getMyAxPara().accept(new CZTCloner()), tClassName);
                AxPara auxTClassAxPara = auxTClass.getMyAxPara();

                ZName zName = zFactory.createZName(tClass.getSchName(),
                        zFactory.createZStrokeList(), "IncludedTClass");
                RefExpr refExpr = zFactory.createRefExpr(zName,
                        zFactory.createZExprList(), false, false);

                ZDeclList zDeclList = zFactory.createZDeclList();
                zDeclList.add(0, zFactory.createInclDecl(refExpr));
                SpecUtils.setAxParaName(auxTClassAxPara, tClassName);
                SpecUtils.setAxParaListOfDecl(auxTClassAxPara, zDeclList);
                SpecUtils.setAxParaPred(auxTClassAxPara, memPred);
                tClassList.add(auxTClass);
            }
        }
        return tClassList;
    }



    /**
     * Parses the parameters of this tactic.
     * @param args
     * @return
     */
    public boolean parseArgs(String args) {
        String parts[] = args.split(" ", 3);
        if (parts.length != 3) {
            System.out.println("Invalid parameters.  Try 'help'." + parts.length);
            return false;
        }
        String varName = parts[0];
        String operator = parts[1];
        String set = parts[2];
        if (varName.endsWith("!")) {
            System.out.println("The specified variable can not be an output variable.");
            return false;
        }

        if (!operator.equals("\\subseteq")) {
            System.out.println("This tactic cannot be applied to the operator " + operator);
            return false;
        }


        AxPara opAxPara = (AxPara) originalOp.getMyAxPara();

        // We check if the specified variable is declared in the scheme declaration
        String typeName = "";
        DeclList declList = SpecUtils.getAxParaListOfDecl(opAxPara);
        boolean found = false;
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;


            for (int j = 0; j < zDeclList.size() && typeName.equals(""); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        for (int k = 0; k < zNameListImpl.size() && typeName.equals(""); k++) {
                            String auxVarName = zNameListImpl.get(k).toString();
                            if (auxVarName.equals(varName)) {
                                found = true;
                            }
                        }
                    }

                }
            }
        } else {
            System.out.println("There is some problem with the specified "
                    + "operation");
        }

        if (!found) {
            System.out.println("The specified variable is not declared in "
                    + "the specified operation's schema.");
            return false;
        }

        ZLive zLive = UniqueZLive.getInstance();
        TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));

        try {
            Term parsedTerm = ParseUtils.parsePred(new StringSource(args),
                    zLive.getCurrentSection(), zLive.getSectionManager());

            if (parsedTerm instanceof ExprPred) {
                parsedTerm = ((ExprPred) parsedTerm).getExpr();
            }

            // We check if the input expression is contained in the predicate.
            opAxPara = (AxPara) originalOp.getMyAxPara();
            if (!opAxPara.accept(new ContainsTermInPredVerifier(parsedTerm, spec, controller)).booleanValue()) {
                System.out.println("The specified term (" + SpecUtils.termToLatex(parsedTerm) + ") is "
                        + "not contained in the predicate of the operation's "
                        + "schema.");

                return false;
            }

            // We get expression's real parameters
            List<Term> realParamList = null;

            if (parsedTerm instanceof Pred) {
                realParamList = ((Pred) parsedTerm).accept(new ParamExtractor());
            } else {
                realParamList = ((Expr) parsedTerm).accept(new ParamExtractor());
            }

            if (realParamList == null) {
                System.out.println("The specified term (" + args + ") is "
                        + "not correct.");
                return false;
            }

            // We check if the last parameter is a set extension
            Expr parsedExpr = ParseUtils.parseExpr(new StringSource(set),
                    zLive.getCurrentSection(), zLive.getSectionManager());
            if (parsedExpr instanceof SetExpr) {
                SetExpr parsedSet = (SetExpr) parsedExpr;
                sseTacticInfo.setSet(parsedSet);
            } else {
                System.out.println("The last parameter (" + set + ") is not a set extension");
            }

            sseTacticInfo.setVar(varName);
            return true;
        } catch (CommandException e) {
            System.out.println("The specified term (" + args + ") is "
                    + "not correct.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalArgumentException
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalArgumentException {

        if (tacticInfo instanceof SSETacticInfo) {
            sseTacticInfo = (SSETacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return sseTacticInfo;
    }



    /**
     * Gets a string that represent the binary representation of a integer using ndigits 
     * @param description
     */
    private static String intToBinary(int binary, int ndigits) {

        String temp = Integer.toBinaryString(binary);
        int foundDigits = temp.length();
        String returner = temp;
        for (int i = foundDigits; i < ndigits; i++) {
            returner = "0" + returner;
        }
        return returner;
    }
}
