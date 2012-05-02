package compserver.tcasegen.eval;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.animation.eval.EvalException;
import net.sourceforge.czt.animation.eval.UndefException;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;

import common.z.SpecUtils;
import common.z.TClass;
import common.z.czt.visitors.CZTReplacer;
import common.z.czt.visitors.StringToNumReplacer;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.UniqueZLive;

import net.sourceforge.czt.session.*;

/**
 * An instance of this class allows the evaluation of schemas that correspond to test classes.
 * @author Pablo Rodriguez Monetti
 */
public class SchemeEvaluator {

    private ZLive zLive = UniqueZLive.getInstance();

    /**
     * Evaluates the predicate of the specified test class replacing each variable with the
     * value indicated in the specified Map.
     * @param tClass
     * @param varExprMap
     * @return
     */
    public EvaluationResp evalSchemeSat(TClass tClass, Map<RefExpr, Expr> varExprMap) {

        EvaluationResp evaluationResp = new EvaluationResp(false, null);
        String log = "";
        log += "\n\n\n***********************COMPROBACION***********************\n";
        log += "La clase: " + tClass.getSchName() + "\n";
        TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));

        // We clone the predicate of the test class
        CZTCloner cloneVisitor = new CZTCloner();
        Pred pred = SpecUtils.getAxParaPred(tClass.getMyAxPara());

        if (pred != null) {
            Pred newPred = (Pred) pred.accept(cloneVisitor);

            /*
            For each expression assigned to some variable declared within the test class, we 
            replace the alphanumeric constants (unless they are Z reserved words) contained in 
            that expression, with numeric constants. Then, we replace each variable in the cloned
            predicate with the corresponding, modified, expression. It is important to verify 
            that the visitor, instance of StringToNumReplacer, is created before the first 
            iteration occurs, because it references a map that contains every correspondence 
            between alphanumeric and numeric constants, and it is modified in each iteration. 
            The map is neccesary in order to replace two ocurrences of the same alphanumeric 
            constant, with the same numeric constant.
             */

            Set<Map.Entry<RefExpr, Expr>> set = varExprMap.entrySet();
            Iterator<Map.Entry<RefExpr, Expr>> iterator = set.iterator();

            StringToNumReplacer stringToNumReplacer = new StringToNumReplacer();

            CZTReplacer replaceVisitor = new CZTReplacer();
            while (iterator.hasNext()) {
                Map.Entry<RefExpr, Expr> mapEntry = iterator.next();
                RefExpr refExpr = mapEntry.getKey();
                Expr expr = mapEntry.getValue();

                log += textUI.printTerm(refExpr, Markup.LATEX);
                //CztPrinter.printRefExpr(refExpr,0, true);
                log += " is ";
                log += textUI.printTerm(expr, Markup.LATEX) + "\n";

                Expr cloneExpr = (Expr) expr.accept(cloneVisitor);
                //	System.out.println(textUI.printTerm(cloneExpr, Markup.LATEX) + "\n");
                cloneExpr = (Expr) cloneExpr.accept(stringToNumReplacer);
                //log += textUI.printTerm(cloneExpr, Markup.LATEX) + "\n";
                //	System.out.println(textUI.printTerm(cloneExpr, Markup.LATEX) + "\n");

                replaceVisitor.setOrigTerm(refExpr);
                replaceVisitor.setNewTerm(cloneExpr);
                newPred = (Pred) newPred.accept(replaceVisitor);
            }

            /**
             * We replace with numeric constants those alphanumeric constants from the predicate
             * which have not been replaced in the previous loop and which do not correpond with 
             * reserved Z words.
             */
            newPred = (Pred) newPred.accept(stringToNumReplacer);


            Term result = null;
            log += "*****PREDICATE*****\n";
            log += textUI.printTerm(newPred, Markup.LATEX) + "\n";
            //CztPrinter.printPred(newPred,0);
            log += "*******************\n";

            try {
                List<? extends ErrorAnn> errors =
                        TypeCheckUtils.typecheck(newPred, zLive.getSectionManager(),
                        false, zLive.getCurrentSection());

                if (errors.size() > 0) {
                    log += "The predicate did not pass the typechecking stage:\n";
                    log += errors.toString();
                    log += "\n***********************************************************";
                    evaluationResp.setLog(log);
                    return evaluationResp;
                } else {
                    //Calendar cal = Calendar.getInstance();
                    //long inicio = cal.getTimeInMillis();
                    result = zLive.evalPred(newPred);
                    //cal = Calendar.getInstance();
                    //long fin = cal.getTimeInMillis(); 
                    //System.out.println("La regular tardo: "+(fin-inicio));
                }

                log += "The result of the evaluation is: ";
                String strResult = textUI.printTerm(result, Markup.LATEX);
                log += strResult + "\n***********************************************************";

                evaluationResp.setLog(log);
                evaluationResp.setResult(new Boolean(strResult));
                /*
                if(new Boolean(strResult))
                System.out.println(SpecUtils.termToLatex(newPred));
                 */
                return evaluationResp;
            } catch (UndefException ex) {

                log += "Undefined!  " + ex.toString() + "\n";
                ex.printStackTrace();
                if (ex.getTerm() != null) {
                    log += "    term = ";
                    log += textUI.printTerm(ex.getTerm(), Markup.LATEX) + "\n";
                }
                log += "There is an undefined expression in the predicate!\n***********************************************************";

                evaluationResp.setLog(log);

                return evaluationResp;
            } catch (EvalException ex) {
                log += "\nError: evaluation too difficult/large: "
                        + ex.getMessage() + "\n";
                if (ex.getTerm() != null) {
                    log += "    term = ";
                    log += textUI.printTerm(ex.getTerm(), Markup.LATEX) + "\n";
                }
                log += "The evaluation result is null!!\n";

                evaluationResp.setLog(log);
                return evaluationResp;
            } catch (Exception e) {
                log += e.toString();
                return evaluationResp;
            }
        } else {
            evaluationResp.setLog("");
            evaluationResp.setResult(true);
            return evaluationResp;
        }
    }

    /**
     * Evaluates the predicate of the specified test class replacing each variable with the
     * value indicated in the specified Map.
     * @param tClass
     * @param varExprMap
     * @return
     */
    public EvaluationResp evalAtomicPredSat(Pred atomicPred, Map<RefExpr, Expr> varExprMap) {

        EvaluationResp evaluationResp = new EvaluationResp(false, null);
        String log = "";
        log += "\n\n\n***********************COMPROBACION***********************\n";
        log += "La clase: " + "\n";
        TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));

        // We clone the atomic predicate
        CZTCloner cloneVisitor = new CZTCloner();
        Pred newPred = (Pred) atomicPred.accept(cloneVisitor);

        //System.out.println("Va a evaluar:\n"+SpecUtils.termToLatex(newPred));
        /*
        For each expression assigned to some variable declared within the test class, we 
        replace the alphanumeric constants (unless they are Z reserved words) contained in 
        that expression, with numeric constants. Then, we replace each variable in the cloned
        predicate with the corresponding, modified, expression. It is important to verify 
        that the visitor, instance of StringToNumReplacer, is created before the first 
        iteration occurs, because it references a map that contains every correspondence 
        between alphanumeric and numeric constants, and it is modified in each iteration. 
        The map is neccesary in order to replace two ocurrences of the same alphanumeric 
        constant, with the same numeric constant.
         */

        Set<Map.Entry<RefExpr, Expr>> set = varExprMap.entrySet();
        Iterator<Map.Entry<RefExpr, Expr>> iterator = set.iterator();

        StringToNumReplacer stringToNumReplacer = new StringToNumReplacer();

        CZTReplacer replaceVisitor = new CZTReplacer();
        while (iterator.hasNext()) {
            Map.Entry<RefExpr, Expr> mapEntry = iterator.next();
            RefExpr refExpr = mapEntry.getKey();
            Expr expr = mapEntry.getValue();

            log += textUI.printTerm(refExpr, Markup.LATEX);
            //CztPrinter.printRefExpr(refExpr,0, true);
            //System.out.println(SpecUtils.termToLatex(refExpr));
            log += " is ";
            log += textUI.printTerm(expr, Markup.LATEX) + "\n";
            //System.out.println(SpecUtils.termToLatex(expr));

            Expr cloneExpr = (Expr) expr.accept(cloneVisitor);
            //	System.out.println(textUI.printTerm(cloneExpr, Markup.LATEX) + "\n");
            cloneExpr = (Expr) cloneExpr.accept(stringToNumReplacer);
            //log += textUI.printTerm(cloneExpr, Markup.LATEX) + "\n";
            //	System.out.println(textUI.printTerm(cloneExpr, Markup.LATEX) + "\n");

            replaceVisitor.setOrigTerm(refExpr);
            replaceVisitor.setNewTerm(cloneExpr);
            newPred = (Pred) newPred.accept(replaceVisitor);
        }

        /**
         * We replace with numeric constants those alphanumeric constants from the predicate
         * which have not been replaced in the previous loop and which do not correpond with 
         * reserved Z words.
         */
        newPred = (Pred) newPred.accept(stringToNumReplacer);


        Term result = null;
        log += "*****PREDICATE*****\n";
        log += textUI.printTerm(newPred, Markup.LATEX) + "\n";
        //CztPrinter.printPred(newPred,0);
        log += "*******************\n";

        try {
            List<? extends ErrorAnn> errors =
                    TypeCheckUtils.typecheck(newPred, zLive.getSectionManager(), false, zLive.getCurrentSection());

            if (errors.size() > 0) {
                log += "The predicate did not pass the typechecking stage:\n";
                log += errors.toString();
                log += "\n***********************************************************";
                evaluationResp.setLog(log);
                return evaluationResp;
            } else {
                //Calendar cal = Calendar.getInstance();
                //long inicio = cal.getTimeInMillis(); 
                result = zLive.evalPred(newPred);
                //cal = Calendar.getInstance();
                //long fin = cal.getTimeInMillis(); 
                //System.out.println("La regular tardo: "+(fin-inicio));




            }



            log += "The result of the evaluation is: ";
            String strResult = textUI.printTerm(result, Markup.LATEX);
            log += strResult + "\n***********************************************************";

            evaluationResp.setLog(log);
            evaluationResp.setResult(new Boolean(strResult));
            /*
            if(new Boolean(strResult))
            System.out.println(SpecUtils.termToLatex(newPred));
             */
            return evaluationResp;
        } catch (UndefException ex) {
            log += "Undefined!  " + ex.toString() + "\n";
            ex.printStackTrace();
            if (ex.getTerm() != null) {
                log += "    term = ";
                log += textUI.printTerm(ex.getTerm(), Markup.LATEX) + "\n";
            }
            log += "There is an undefined expression in the predicate!\n***********************************************************";

            evaluationResp.setLog(log);
            return evaluationResp;
        } catch (EvalException ex) {
            log += "\nError: evaluation too difficult/large: "
                    + ex.getMessage() + "\n";
            if (ex.getTerm() != null) {
                log += "    term = ";
                log += textUI.printTerm(ex.getTerm(), Markup.LATEX) + "\n";
            }
            log += "The evaluation result is null!!\n";

            evaluationResp.setLog(log);
            return evaluationResp;
        } catch (Exception e) {
            log += e.toString();
            return evaluationResp;
        }
    }
}
