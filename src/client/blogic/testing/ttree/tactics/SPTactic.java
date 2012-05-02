package client.blogic.testing.ttree.tactics;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.animation.eval.TextUI;
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
import net.sourceforge.czt.session.CommandException;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.SpecUtils;
import common.z.TClass;
import common.z.TClassImpl;
import common.z.czt.UniqueZLive;
import common.repository.AbstractIterator;
import common.z.czt.visitors.ParamExtractor;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.CZTReplacer;
import common.z.czt.visitors.ContainsTermInPredVerifier;
import java.net.URL;

/**
 * Implementation of the tactic Standard Partitions (SP).
 * @author Pablo Rodriguez Monetti
 */
public class SPTactic extends AbstractTactic {


    private SPTacticInfo spTacticInfo;

    /**
     * Creates instances of SPTactic.
     */
    public SPTactic() {
        URL url = SPTactic.class.getResource("SPTactic.class");
        String urlStr = url.toString();
        String currentDir = urlStr.substring(9, urlStr.indexOf("fastest.jar"));
        (new StdPartitionLoader(currentDir + "lib/conf/stdpartition.spf")).loadStdPartitions();
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* SP (Standard Partitions). This tactic uses a predeﬁned\n";
        description += "  partition of some mathematical operator (see \n";
        description += "  \"Standard domains for Z operators\" at page 165 of \n";
        description += "  Stocks’ Ph.D. thesis).\n";
        description += "  Usage: addtactic <op_name> SP <operator> <expression>\n";
        description += "  where op_name is the name of a selected operation,\n";
        description += "  operator is the Latex string of a Z operator, and\n ";
        description += " expression is a Z expression written in Latex. It is\n";
        description += "  assumed that operator appears in the expression and\n";
        description += "  this in turn appears in the predicate of the selected\n";
        description += "  operation.\n";
    }



    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param tClass
     * @return
     */
    public List<TClass> applyTactic(TClass tClass) {
        AxPara opAxPara = (AxPara) originalOp.getMyAxPara().
                accept(new CZTCloner());
        List<TClass> tClassList = new ArrayList<TClass>();
        // We replace real parameters by formal parameters in each predicate
        // and generate a new schema for each one.
        List<AxPara> axParaList = new ArrayList<AxPara>();
        List<Pred> predList = spTacticInfo.getStdPartition().getPredList();

        AxPara tClassAxPara = tClass.getMyAxPara();
        CZTCloner cloneVisitor = new CZTCloner();
        CZTReplacer replaceVisitor = new CZTReplacer();
        List<String> formalParamList = spTacticInfo.getStdPartition().
                getFormalParamList();

        String opName = SpecUtils.getAxParaName(opAxPara);
        Integer tClassInteger = tClassNumbersMap.get(opName);
        if (tClassInteger == null) {
            tClassInteger = new Integer(1);
            tClassNumbersMap.put(opName, tClassInteger);
        }


        for (int i = 0; i < predList.size(); i++) {
            Pred pred = predList.get(i);
            Pred newPred = (Pred) pred.accept(cloneVisitor);
            for (int j = 0; j < formalParamList.size(); j++) {
                String originalString = formalParamList.get(j);

                ZFactory zFactory = new ZFactoryImpl();
                ZName zName = zFactory.createZName(originalString,
                        zFactory.createZStrokeList(), "word");
                ZExprList zExprList = zFactory.createZExprList();
                RefExpr originalRefExpr = zFactory.createRefExpr(zName,
                        zExprList, false, false);

                Term finalTerm = spTacticInfo.getRealParamList().get(j);
                // We replace the instance of RefExpr called originalString by
                // finalTerm

                replaceVisitor.setOrigTerm(originalRefExpr);
                replaceVisitor.setNewTerm(finalTerm);

                newPred = (Pred) newPred.accept(replaceVisitor);

            }
            int tClassNumber = tClassNumbersMap.get(opName).intValue();
            String tClassName = opName + "_SP_"
                    + tClassNumber;
            tClassNumbersMap.put(opName, tClassNumber + 1);
            AxPara newAxPara = (AxPara) tClassAxPara.accept(cloneVisitor);

            Pred newAxParaPred = SpecUtils.getAxParaPred(newAxPara);
            SpecUtils.setAxParaPred(newAxPara, SpecUtils.andPreds(newAxParaPred,
                    newPred));


            // The test class is added to the list of test classes
            TClass auxTClass = new TClassImpl((AxPara) tClass.getMyAxPara().accept(new CZTCloner()), tClassName);
            AxPara auxTClassAxPara = auxTClass.getMyAxPara();

            ZFactory zFactory = new ZFactoryImpl();
            ZName zName = zFactory.createZName(tClass.getSchName(),
                    zFactory.createZStrokeList(), "IncludedTClass");
            RefExpr refExpr = zFactory.createRefExpr(zName,
                    zFactory.createZExprList(), false, false);
            ZDeclList zDeclList = zFactory.createZDeclList();
            zDeclList.add(0, zFactory.createInclDecl(refExpr));
            SpecUtils.setAxParaName(auxTClassAxPara, tClassName);
            SpecUtils.setAxParaListOfDecl(auxTClassAxPara, zDeclList);
            SpecUtils.setAxParaPred(auxTClassAxPara, newPred);
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
        String parts[] = args.split(" ", 2);
        if (parts.length > 2 || parts.length < 2) {
            System.out.println("Invalid parameters.  Try 'help'." + parts.length);
            return false;
        }
        ZLive zLive = UniqueZLive.getInstance();
        TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
        String operatorStr = parts[0];
        String termStr = parts[1];
        spTacticInfo = new SPTacticInfo();
        try {
            AbstractIterator<StdPartition> stdPartitionIt =
                    StdPartitionsControl.getInstance().createIterator();

            // We look for the operator's standard partition
            boolean operatorFound = false;
            StdPartition stdPartition = null;

            while (stdPartitionIt.hasNext() & !operatorFound) {
                stdPartition = stdPartitionIt.next();
                if (stdPartition.getOperator().equals(operatorStr)) {
                    operatorFound = true;
                }
            }

            if ((stdPartition == null) || !operatorFound) {
                System.out.println("There is not a standard partition loaded "
                        + "for the specified operator (" + operatorStr + ").");
                return false;
            }

            spTacticInfo.setStdPartition(stdPartition);

            Term parsedTerm = ParseUtils.parsePred(new StringSource(termStr),
                    zLive.getCurrentSection(), zLive.getSectionManager());

            if (parsedTerm instanceof ExprPred) {
                parsedTerm = ((ExprPred) parsedTerm).getExpr();
            }

            // We check if the input expression is contained in the predicate.
            AxPara opAxPara = (AxPara) originalOp.getMyAxPara();
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
                System.out.println("The specified term (" + termStr + ") is "
                        + "not correct.");
                return false;
            }
            List<String> formalParamList = stdPartition.getFormalParamList();

            if (realParamList != null) {
                if (formalParamList.size() != realParamList.size()) {
                    System.out.println("The loaded standard partition is invalid. "
                            + "Please, check it.");
                    return false;
                }
            }
            spTacticInfo.setRealParamList(realParamList);

            return true;
        } catch (CommandException e) {
            System.out.println("The specified term (" + termStr + ") is "
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
     * @throws java.lang.IllegalArgumentException if tacticInfo is not an instance of
     * TacticInfo.
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalArgumentException {

        if (tacticInfo instanceof SPTacticInfo) {
            spTacticInfo = (SPTacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return spTacticInfo;
    }

}
