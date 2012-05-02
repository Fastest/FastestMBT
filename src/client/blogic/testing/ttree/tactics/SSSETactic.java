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
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ExprList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZExprListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
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
import common.z.czt.visitors.ContainsTermVerifier;
import common.z.czt.visitors.ParamExtractor;
import common.z.UtilSymbols;

/**
 * Implementation of the tactic Sub Set of Set Extension (SSSE).
 * @author Pablo Rodriguez Monetti
 */
public class SSSETactic extends AbstractTactic {

    private SSSETacticInfo ssseTacticInfo;

    /**
     * Creates new instances of SSSETactic.
     */
    public SSSETactic() {
        ssseTacticInfo = new SSSETacticInfo();
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* SSSE (Sub Set of Set Extension). This tactic generates \n";
        description += " as many test classes as elements a set extension has. \n";
        description += " In other words, if in your model there is some predicate \n";
        description += " x \\subseteq \\{x1,x2,x3\\},then by applying this tactic to this expresion \n";
        description += " you will get three test classes:\n";
        description += " one in which x equals x1, one in which x equals x2 and the\n";
        description += " third in which x equals x3.\n";
        description += "  Usage: addtactic <op_name> SSSE <expression>\n";
        description += "  where op_name is the name of a selected operation\n";
        description += "  and expression is a Z expression written in Latex.\n ";
        description += "  It is assumed that  \\subseteq operator appears in the expression\n";
        description += "  and this in turn appears in the predicate\n";
        description += "  of the selected operation.\n";
    }  // VER - Agregar descripcion

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

        ExprList listExpr = ssseTacticInfo.getSet().getExprList();
        if (listExpr instanceof ZExprListImpl) {
            ZExprListImpl zlistExpr = (ZExprListImpl) listExpr;
            Expr leftExpr = ssseTacticInfo.getLeftExpr();


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
                memPredExprList.add(0, leftExpr);
                memPredExprList.add(1, supersetExpr);
                MemPred memPred = zFactory.createMemPred(memPredExprList, true);

                int tClassNumber = tClassNumbersMap.get(opName).intValue();
                String tClassName = SpecUtils.getAxParaName(opAxPara) + "_SSSE_"
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

        ZLive zLive = UniqueZLive.getInstance();
        TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));

        try {
            Term parsedTerm = ParseUtils.parsePred(new StringSource(args),
                    zLive.getCurrentSection(), zLive.getSectionManager());

            if (parsedTerm instanceof ExprPred) {
                parsedTerm = ((ExprPred) parsedTerm).getExpr();
            }

            // We check if the input expression contain the \subset operator
            if (!(parsedTerm instanceof MemPred)) {
                System.out.println("The specified term (" + args + ") does "
                        + "not contain the \\subset operator.");
                return false;
            }

            MemPred memPred = (MemPred) parsedTerm;
            if (!memPred.getMixfix()) {
                System.out.println("The specified term (" + args + ") does "
                        + "not contain the \\subset operator.");
                return false;
            }
            Expr rightExpr = memPred.getRightExpr();
            if (!(rightExpr instanceof RefExpr)) {
                System.out.println("The specified term (" + args + ") does "
                        + "not contain the \\subset operator.");
                return false;
            }

            RefExpr refExpr = (RefExpr) rightExpr;
            String str = refExpr.getName().toString();
            String subsetStr = UtilSymbols.getSymbol(25);
            if (!str.equals(subsetStr)) {
                System.out.println("The specified term (" + args + ") does "
                        + "not contain the \\subset operator.");
                return false;
            }

            // We check if the input expression is contained in the predicate.
            AxPara opAxPara = (AxPara) originalOp.getMyAxPara();
            opAxPara = (AxPara) originalOp.getMyAxPara();
            if (!opAxPara.accept(new ContainsTermVerifier(parsedTerm)).booleanValue()) {
                System.out.println("The specified term (" + args + ") is "
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


            Term leftTerm = realParamList.get(0);
            Term setTerm = realParamList.get(1);

            if (!(leftTerm instanceof Expr)) {
                System.out.println("The first argument is not an expression");
                return false;
            }
            common.z.czt.CztPrinter.printExpr((Expr) leftTerm, 1);

            ssseTacticInfo.setLeftExpr((Expr) leftTerm);

            // We check if the last parameter is a set extension
            if (!(setTerm instanceof SetExpr)) {
                System.out.println("The second argument is not a set extension");
                return false;
            }

            SetExpr setExpr = (SetExpr) setTerm;
            ssseTacticInfo.setSet(setExpr);


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

        if (tacticInfo instanceof SSSETacticInfo) {
            ssseTacticInfo = (SSSETacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return ssseTacticInfo;
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
