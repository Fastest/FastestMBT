package client.blogic.testing.ttree.tactics;

import java.util.*;

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
 * Implementation of the In Set Extension (ISE).
 * @author Pablo Rodriguez Monetti
 */
public class ISETactic extends AbstractTactic {


    private ISETacticInfo iseTacticInfo;

    /**
     * Creates new instances of ISETactic.
     */
    public ISETactic() {
        iseTacticInfo = new ISETacticInfo();
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* ISE (In Set Extension). This tactic generates as many test\n";
        description += "  classes as elements a set extension has. In other words, if in \n";
        description += "  your model there is some predicate expr \\in \\{x1,x2,x3\\},\n";
        description += "  then by applying this tactic to this expresion \n";
        description += "  you will get three test classes:\n";
        description += "  one in which expr equals x1, one in which expr equals x2 and the\n";
        description += "  third in which expr equals x3.\n";
        description += "  Usage: addtactic <op_name> ISE <expression>\n";
        description += "  where op_name is the name of a selected operation\n";
        description += "  and expression is a Z expression written in Latex.\n";
        description += "  It is assumed that  \\in operator appears in the expression\n";
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

        ExprList listExpr = iseTacticInfo.getSet().getExprList();
        if (listExpr instanceof ZExprListImpl) {
            ZExprListImpl zlistExpr = (ZExprListImpl) listExpr;
            Expr leftExpr = iseTacticInfo.getLeftExpr();

            for (int j = 0; j < zlistExpr.size(); j++) {
                Expr valueExpr = zlistExpr.get(j);

                //We create the SetExpr that contains the right expr
                ZExprList setZExprList = zFactory.createZExprList();
                setZExprList.add(0, valueExpr);
                SetExpr setExpr = zFactory.createSetExpr(setZExprList);

                //We create the MemPred that will be added to the new test class
                ZExprList memPredExprList = zFactory.createZExprList();
                memPredExprList.add(0, leftExpr);
                memPredExprList.add(1, setExpr);
                MemPred memPred = zFactory.createMemPred(memPredExprList, true);

                int tClassNumber = tClassNumbersMap.get(opName).intValue();
                String tClassName = SpecUtils.getAxParaName(opAxPara) + "_ISE_"
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


        AxPara opAxPara = (AxPara) originalOp.getMyAxPara();

        ZLive zLive = UniqueZLive.getInstance();

        try {
            Term parsedTerm = ParseUtils.parsePred(new StringSource(args),
                    zLive.getCurrentSection(), zLive.getSectionManager());

            if (parsedTerm instanceof ExprPred) {
                parsedTerm = ((ExprPred) parsedTerm).getExpr();
            }


            // We check if the input expression is a MemPred
            if (!(parsedTerm instanceof MemPred)) {
                System.out.println("The specified term (" + args + ") is "
                        + "not a member predicate.");
                return false;
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


            Term leftTerm = realParamList.get(0);
            Term setTerm = realParamList.get(1);

            if (!(leftTerm instanceof Expr)) {
                System.out.println("The first argument is not an expression");
                return false;
            }

            iseTacticInfo.setLeftExpr((Expr) leftTerm);

            // We check if the last parameter is a set extension
            if (!(setTerm instanceof SetExpr)) {
                System.out.println("The second argument is not a set extension");
                return false;
            }

            SetExpr setExpr = (SetExpr) setTerm;
            iseTacticInfo.setSet(setExpr);


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

        if (tacticInfo instanceof ISETacticInfo) {
            iseTacticInfo = (ISETacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return iseTacticInfo;
    }


}
