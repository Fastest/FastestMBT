/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.commands;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.session.CommandException;
import net.sourceforge.czt.session.Key;
import net.sourceforge.czt.session.Source;
import net.sourceforge.czt.session.FileSource;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.NarrPara;
import net.sourceforge.czt.z.ast.LatexMarkupPara;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.SectTypeEnvAnn;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.session.Markup;

import common.z.czt.visitors.StringToNumReplacer;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.CZTReplacer;
import client.presentation.ClientTextUI;
import common.z.czt.UniqueZLive;
import client.blogic.management.Controller;
import common.z.SpecUtils;
import net.sourceforge.czt.parser.util.CztError;
import net.sourceforge.czt.parser.util.CztErrorList;

/**
 * An instance of this class allow the assignation of values to variables
 * defined in axiomatic definitions.
 * @author Pablo Rodriguez Monetti
 */
public class SetAxDefCommand implements Command {

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {
        PrintWriter output = clientTextUI.getOutput();
        String fileName = "tempAxDef.tex";
        File tempFile = new File(fileName);
        try {
            if (args == null || "".equals(args)) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            final String parts[] = args.split(" ", 2);
            if (parts.length > 2) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            String varName = parts[0];

            String restStr = parts[1];

            if (!(restStr.startsWith("\""))) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }


            final String subParts[] = restStr.split("\"");

            String valueStr = "";
            String declStr = "";
            boolean isThereDecls = false;
            if (subParts.length == 2) {
                valueStr = subParts[1];
            } else if (subParts.length == 4) {
                declStr = subParts[1];
                valueStr = subParts[3];
                isThereDecls = true;
            } else {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            Controller controller = clientTextUI.getMyController();
            List<String> blockedSetAxDefs = controller.getBlockedAxDefs();
            if (blockedSetAxDefs.contains(varName)) {
                output.println("The variable " + varName + " cannot be modified "
                        + "because there is a previously pruned test class that "
                        + "contains the variable.");
                return;
            }

            ZLive zLive = UniqueZLive.getInstance();
            TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
            Term parsedTerm = ParseUtils.parsePred(
                    new StringSource(valueStr),
                    zLive.getCurrentSection(),
                    zLive.getSectionManager());


            Map<String, Expr> axDefsRequired = controller.getAxDefsRequired();
            Expr typeExpr = axDefsRequired.get(varName);

            if (typeExpr == null) {
                output.println("The specified variable is not a variable "
                        + "defined in an axiomatic definition of the loaded"
                        + " specification or the variable cannot be modified.");
                return;
            }

            Expr parsedExpr = null;
            if (parsedTerm instanceof ExprPred) {
                parsedExpr = ((ExprPred) parsedTerm).getExpr();
            } else if (parsedTerm instanceof Expr) {
                parsedExpr = (Expr) parsedTerm;
            } else {
                output.println("The specified value is not correct.");
                return;
            }


            // We verify that the type of the specified value is correct
            PrintWriter printer = new PrintWriter(new FileWriter(tempFile));


            Spec spec = controller.getOriginalSpec();

            for (Sect sect : spec.getSect()) {
                if (sect instanceof ZSect) {
                    ZSect zSect = (ZSect) sect;
                    ParaList paraList = zSect.getParaList();
                    if (paraList instanceof ZParaList) {
                        ZParaList zParaList = (ZParaList) paraList;
                        for (int i = 0; i < zParaList.size(); i++) {
                            Para para = zParaList.get(i);
                            if (!(para instanceof NarrPara
                                    || para instanceof LatexMarkupPara)) {
                                printer.println("\n" + SpecUtils.termToLatex(para));
                            }
                        }
                    }
                }
            }


            printer.println("\\begin{axdef}");
            String varDeclStr = varName + "aux :"
                    + SpecUtils.termToLatex(typeExpr);
            printer.print(varDeclStr);
            if (!(declStr.equals(("")))) {
                printer.println("\\\\");
            } else {
                printer.println();
            }
            printer.println(declStr);
            printer.println("\\where");
            String equalityStr = varName + "aux = " + valueStr;
            printer.println(equalityStr);
            printer.println("\\end{axdef}");
            printer.flush();


            Source source = new FileSource(fileName);
            SectionManager manager = new SectionManager();
            manager.put(new Key(fileName, Source.class), source);

            spec = (Spec) manager.get(new Key(fileName, Spec.class));

            // To typecheck the value we typecheck the following artificious
            // specification. At the same time, we extract the object represen-
            // tation of the declaration part, if any (ie. if isThereDecls
            // is true).
            ZDeclList constantsDecl = null;
            for (Sect sect : spec.getSect()) {
                if (sect instanceof ZSect) {
                    ZSect zSect = (ZSect) sect;
                    // We typecheck the specification here
                    String sectionName = zSect.getName();
                    SectTypeEnvAnn envAnn =
                            (SectTypeEnvAnn) manager.get(new Key(sectionName,
                            SectTypeEnvAnn.class));
                    if (isThereDecls) {
                        ParaList paraList = zSect.getParaList();
                        if (paraList instanceof ZParaList) {
                            ZParaList zParaList = (ZParaList) paraList;
                            Para para = zParaList.get(zParaList.size() - 2);
                            if (para instanceof AxPara) {
                                AxPara axPara = (AxPara) para;
                                if (axPara.getBox() == Box.AxBox) {
                                    constantsDecl = axPara.getZSchText().getZDeclList();
                                }
                            }
                        }
                    }
                }
            }

            if (isThereDecls) {
                if (constantsDecl == null) {
                    System.out.println("This message could have not been printed!");
                }

                constantsDecl.remove(0);
                Map<String, ZDeclList> auxiliarDecls = controller.getAuxiliarDecls();
                auxiliarDecls.put(varName, constantsDecl);
            }



            // We verify if the value satisfies the axdef predicate, considering
            // the others variables defined in axiomatic definitions
            ZFactory zFactory = new ZFactoryImpl();
            ZExprList zExprList = zFactory.createZExprList();
            ZName zName = zFactory.createZName(varName,
                    zFactory.createZStrokeList(), "varName");
            RefExpr varNameRefExpr = zFactory.createRefExpr(zName, zExprList,
                    false, false);
            Map<String, List<Pred>> axDefsRequiredPreds =
                    controller.getAxDefsRequiredPreds();
            Map<Pred, List<String>> axDefsPredVars = controller.getAxDefsPredVars();
            Map<RefExpr, Expr> axDefsValues = controller.getAxDefsValues();
            Set<Map.Entry<RefExpr, Expr>> axDefsValuesSet = axDefsValues.entrySet();
            CZTReplacer replaceVisitor = new CZTReplacer();
            List<Pred> predList = axDefsRequiredPreds.get(varName);

            if (predList != null) {
                for (int i = 0; i < predList.size(); i++) {
                    // For each predicate p in which the variable of name varName
                    // appears ...
                    Pred p = predList.get(i);
                    List<String> varList = axDefsPredVars.get(p);
                    Pred pClone = (Pred) p.accept(new CZTCloner());
                    boolean pCloneIsConstant = true;
                    for (int j = 0; j < varList.size() && pCloneIsConstant; j++) {
                        // we check if every variable of p has a value, ie. if
                        // p is a constant predicate
                        String jVarName = varList.get(j);
                        if (!jVarName.equals(varName)) {
                            Iterator<Map.Entry<RefExpr, Expr>> axDefsValuesIt =
                                    axDefsValuesSet.iterator();
                            boolean foundValue = false;
                            while (axDefsValuesIt.hasNext() && !foundValue) {
                                Map.Entry<RefExpr, Expr> mapEntry =
                                        axDefsValuesIt.next();
                                RefExpr refExpr = mapEntry.getKey();
                                String itVarName = refExpr.getZName().toString();
                                if (itVarName.equals(jVarName)) {
                                    Expr expr = mapEntry.getValue();
                                    replaceVisitor.setOrigTerm(refExpr);
                                    replaceVisitor.setNewTerm(expr);
                                    /*
                                    System.out.println("******" + l);
                                    System.out.println("Variable " + refExpr.getName().toString());
                                    System.out.println("Valor " + SpecUtils.termToLatex(expr) + "\n");

                                    l++;
                                     */
                                    pClone = (Pred) pClone.accept(replaceVisitor);
                                    foundValue = true;
                                }
                            }
                            if (foundValue == false) {
                                pCloneIsConstant = false;
                            }
                        }
                    }
                    replaceVisitor.setOrigTerm(varNameRefExpr);
                    replaceVisitor.setNewTerm(parsedExpr);
                    pClone = (Pred) pClone.accept(replaceVisitor);

                    // If pClone is constant here, we evaluate it
                    if (pCloneIsConstant) {

                        pClone = (Pred) pClone.accept(new StringToNumReplacer());

                        Term result = null;
                        //output.println("a ver... "+ SpecUtils.termToLatex(pClone));
                        output.flush();
                        List<? extends ErrorAnn> errors =
                                TypeCheckUtils.typecheck(pClone, zLive.getSectionManager(),
                                false, zLive.getCurrentSection());

                        if (errors.size() > 0) {
                            output.println("The specified value does not satisfy the predicate:");
                            output.println(" " + SpecUtils.termToLatex(p));
                            output.println(" whose evaluation was not successfully completed.");
                            output.println(errors.toString());
                        } else {
                            //CztPrinter.printPred(newPred,0);
                            result = zLive.evalPred(pClone);
                        }

                        String strResult = textUI.printTerm(result, Markup.LATEX);
                        if (strResult.equals("false")) {
                            output.println("The specified value does not satisfy the predicate:");
                            output.println(SpecUtils.termToLatex(p));
                            return;
                        }
                    }
                }

            }
            // We add the (var,val) pair to the axDefsValues map
            axDefsValues.put(varNameRefExpr, parsedExpr);
            tempFile.delete();

        } catch (CommandException e) {
            tempFile.delete();
            Throwable cause = e.getCause();
            if (cause instanceof CztErrorList) {
                output.println("There are type errors.");
                List<CztError> errors = new ArrayList<CztError>();
                errors.addAll(((CztErrorList) cause).getErrors());
                Collections.sort(errors);
                for (Object o : errors) {
                    output.println(o.toString());
                }
            }
            output.println("There are type errors.");
            output.println(e.getCause().getMessage());
        } catch (Exception e) {
            tempFile.delete();
            e.printStackTrace();
            output.println("The specified value is not correct.");
        }
    }
}
