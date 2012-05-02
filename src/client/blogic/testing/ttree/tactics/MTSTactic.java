package client.blogic.testing.ttree.tactics;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.SchExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;

import common.z.SpecUtils;
import common.z.TClass;
import common.z.TClassImpl;
import common.z.czt.UniqueZLive;
import common.z.czt.visitors.CZTCloner;
import common.z.UtilSymbols;

/**
 * Implementation of the tactic Mandatory Test Set (MTS).
 * @author Pablo Rodriguez Monetti
 */
public class MTSTactic  extends AbstractTactic {


    private MTSTacticInfo mTSTacticInfo;
    
    /**
     * Creates new instances of MTSTactic.
     */    
    public MTSTactic() {
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* MTS (Mandatory Test Set). This tactic allows the user\n";
        description += "  to partition the input domain giving a finite set of values\n";
        description += "  for a variable or expression. If the expression is e and the\n";
        description += "  set is {v_1,v_2,...,v_n} n+1 classes will be generated: the\n";
        description += "  first n has a predicate of the form e = v_i and the\n";
        description += "  predicate of the last one is e \\notin {v_1,v_2,...,v_n}.\n";
        description += "  Usage: addtactic Op MTS \"<expr>\" <set>.\n";

    }

    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param tClass
     * @return
     */
    public List<TClass> applyTactic(TClass tClass) {

        ZFactory zFactory = new ZFactoryImpl();

        AxPara opAxPara = (AxPara) originalOp.getMyAxPara().
                accept(new CZTCloner());
        List<TClass> tClassList = new ArrayList<TClass>();


        List<AxPara> axParaList = new ArrayList<AxPara>();


        AxPara tClassAxPara = tClass.getMyAxPara();
        CZTCloner cloneVisitor = new CZTCloner();


        String opName = SpecUtils.getAxParaName(opAxPara);
        Integer tClassInteger = tClassNumbersMap.get(opName);
        if (tClassInteger == null) {
            tClassInteger = new Integer(1);
            tClassNumbersMap.put(opName, tClassInteger);
        }

        Expr expr = mTSTacticInfo.getExpr();
        SetExpr setExpr = mTSTacticInfo.getSetExpr();

        ZExprList zExprList = setExpr.getZExprList();

        List<Pred> predList = new ArrayList<Pred>();


        for (int j = 0; j < zExprList.size(); j++) {

            Expr auxExpr = zExprList.get(j);
            //We create the SetExpr that contains the right expr
            ZExprList setZExprList = zFactory.createZExprList();
            setZExprList.add(auxExpr);
            SetExpr auxSetExpr = zFactory.createSetExpr(setZExprList);

            //We create the MemPred that will be added to the new test class
            ZExprList memPredExprList = zFactory.createZExprList();
            memPredExprList.add(0, expr);
            memPredExprList.add(1, auxSetExpr);
            MemPred memPred = zFactory.createMemPred(memPredExprList, true);

            predList.add(memPred);
        }


        ZExprList innerZExprList = zFactory.createZExprList();
        innerZExprList.add(expr);
        innerZExprList.add(setExpr);
        TupleExpr tupleExpr = zFactory.createTupleExpr(innerZExprList);
        ZName greaterThanSymbolZName = zFactory.createZName(UtilSymbols.getSymbol(20),
                zFactory.createZStrokeList(), "notin");
        RefExpr greaterThanSymbolRefExpr = zFactory.createRefExpr(greaterThanSymbolZName,
                zFactory.createZExprList(), false, false);
        ZExprList outerZExprList = zFactory.createZExprList();
        outerZExprList.add(tupleExpr);
        outerZExprList.add(greaterThanSymbolRefExpr);
        MemPred memPred = zFactory.createMemPred(outerZExprList, Boolean.TRUE);

        predList.add(memPred);


        for (int j = 0; j < predList.size(); j++) {

            Pred pred = predList.get(j);

            int tClassNumber = tClassNumbersMap.get(opName).intValue();
            String tClassName = SpecUtils.getAxParaName(opAxPara) + "_MTS_"
                    + tClassNumber;
            tClassNumbersMap.put(opName, tClassNumber + 1);
            AxPara newAxPara = (AxPara) tClassAxPara.accept(cloneVisitor);

            Pred newAxParaPred = SpecUtils.getAxParaPred(newAxPara);
            SpecUtils.setAxParaPred(newAxPara, SpecUtils.andPreds(newAxParaPred,
                    pred));

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
            SpecUtils.setAxParaPred(auxTClassAxPara, pred);

            tClassList.add(auxTClass);
        }
        return tClassList;
    }

    /**
     * Parses the parameters of this tactic.
     * @param args
     * @return
     */
    public boolean parseArgs(String args) {

        args = args.trim();
        int exprStartingIndex = args.indexOf("\"") + 1;
        int exprEndingIndex = args.lastIndexOf("\"");

        if (exprStartingIndex != 1) {
            System.out.println("The expression must be enclosed between quotes.");
            return false;
        }

        if (exprEndingIndex <= 1) {
            System.out.println("The expression must be enclosed between quotes.");
            return false;
        }

        int setStartingIndex = exprEndingIndex + 1;

        String exprStr = args.substring(exprStartingIndex, exprEndingIndex);
        String setStr = args.substring(setStartingIndex).trim();

        ZLive zLive = UniqueZLive.getInstance();

        String exprBeingParsing = exprStr;
        try {


            Term exprParsed = ParseUtils.parsePred(new StringSource(exprStr),
                    zLive.getCurrentSection(), zLive.getSectionManager());

            if (exprParsed instanceof ExprPred) {
                exprParsed = ((ExprPred) exprParsed).getExpr();
            }

            Expr expr = (Expr) exprParsed;

            exprBeingParsing = setStr;

            Term setParsed = ParseUtils.parsePred(new StringSource(setStr),
                    zLive.getCurrentSection(), zLive.getSectionManager());
            if (setParsed instanceof ExprPred) {
                setParsed = ((ExprPred) setParsed).getExpr();
            }

            SetExpr setExpr = (SetExpr) setParsed;

            ZExprList zExprList = setExpr.getZExprList();

            if (zExprList.size() == 0) {
                System.out.println("The specified set must not be empty.");
                return false;
            }


            // We check if the expression has the type of the elements of the set
            AxPara opAxPara = (AxPara) originalOp.getMyAxPara();
            AxPara auxAxPara = (AxPara) opAxPara.accept(new CZTCloner());
            Pred originalOpPred = SpecUtils.getAxParaPred(auxAxPara);
            ZFactory zFactory = new ZFactoryImpl();
            ZExprList innerZExprList = zFactory.createZExprList();
            innerZExprList.add(expr);
            innerZExprList.add(setExpr);
            TupleExpr tupleExpr = zFactory.createTupleExpr(innerZExprList);
            ZName greaterThanSymbolZName = zFactory.createZName(UtilSymbols.getSymbol(20),
                    zFactory.createZStrokeList(), "notin");
            RefExpr greaterThanSymbolRefExpr = zFactory.createRefExpr(greaterThanSymbolZName,
                    zFactory.createZExprList(), false, false);
            ZExprList outerZExprList = zFactory.createZExprList();
            outerZExprList.add(tupleExpr);
            outerZExprList.add(greaterThanSymbolRefExpr);
            Pred memPred = zFactory.createMemPred(outerZExprList, Boolean.TRUE);

            Pred newPred = SpecUtils.andPreds(originalOpPred, memPred);
            SpecUtils.setAxParaPred(auxAxPara, newPred);
            SpecUtils.setAxParaName(auxAxPara, SpecUtils.getAxParaName(auxAxPara) + "aux");


            String sectionName = "";
            for (Sect sect : spec.getSect()) {
                if (sect instanceof ZSect) {
                    ZSect zSect = (ZSect) sect;
                    sectionName = zSect.getName();
                }
            }

            SchExpr schExpr = SpecUtils.getAxParaSchExpr(auxAxPara);
            List<? extends ErrorAnn> errors =
                    TypeCheckUtils.typecheck(schExpr, manager, false, sectionName);


            if (errors.size() > 0) {
                System.out.println("The type of the arguments is not correct");
                System.out.println(errors);
                return false;
            }


            mTSTacticInfo = new MTSTacticInfo();
            mTSTacticInfo.setExpr((Expr) exprParsed);
            mTSTacticInfo.setSetExpr(setExpr);

            return true;
        } catch (CommandException e) {
            System.out.println("The argument '" + exprBeingParsing
                    + "' is not syntactically correct.");
            return false;
        } catch (IOException e) {
            System.out.println("There was an IO error at parsing arguments .");
            return false;
        }
    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalArgumentException if tacticInfo is not an instance of
     * ETTacticInfo.
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalArgumentException {

        if (tacticInfo instanceof MTSTacticInfo) {
            mTSTacticInfo = (MTSTacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return mTSTacticInfo;
    }


}
