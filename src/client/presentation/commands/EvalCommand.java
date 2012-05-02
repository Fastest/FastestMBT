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
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;

import client.presentation.ClientTextUI;
import common.z.czt.UniqueZLive;
import common.z.SpecUtils;

/**
 * An instance of this class allow the evaluation of Z terms.
 * @author Pablo Rodriguez Monetti
 */
public class EvalCommand implements Command {

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {
        PrintWriter output = clientTextUI.getOutput();
        try {
            
            if(args.equals("")){
                output.println("Invalid parameters. Try 'help'.");
		return;
            }
            
            ZLive zLive = UniqueZLive.getInstance();
            Term parsedTerm = ParseUtils.parsePred(
                    new StringSource(args),
                    zLive.getCurrentSection(),
                    zLive.getSectionManager());

            Term result = null;
            List<? extends ErrorAnn> errors =
                    TypeCheckUtils.typecheck(parsedTerm, zLive.getSectionManager(),
                    false, zLive.getCurrentSection());

            if (errors.size() > 0) {
                output.println(errors.toString());
                return;
            }

            if (parsedTerm instanceof Pred) {
                Pred parsedPred = (Pred) parsedTerm;
                result = zLive.evalPred(parsedPred);
            } else if (parsedTerm instanceof Expr) {
                Expr parsedExpr = (Expr) parsedTerm;
                result = zLive.evalExpr(parsedExpr);
            }

            output.println(SpecUtils.termToLatex(result));

        }
        catch (Exception e) {
            output.println(e.getMessage());
        }

    }
}
