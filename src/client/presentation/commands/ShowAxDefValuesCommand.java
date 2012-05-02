package client.presentation.commands;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.Spec;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import common.z.SpecUtils;

/**
 * An instance of this class allow the presentation of values assigned to the
 * variables defined in aximatic definitions.
 * @author Pablo Rodriguez Monetti
 */
public class ShowAxDefValuesCommand implements Command {

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {


        PrintWriter stdout = clientTextUI.getOutput();
        try {
            final String argv[] = args.split(" ");
            int argc = argv.length;

            PrintWriter printer = stdout;
            for (int index = 0, argCount = 1; index < argc; index += argCount) {
                if (argv[index].equals("-o")) {
                    String fileName = argv[index + 1];
                    printer = new PrintWriter(new FileWriter(fileName));
                    argCount = 2;
                } else if (!argv[index].equals("")) {
                    stdout.println("Invalid parameters.  Try 'help'.");
                    return;
                }
            }

            Controller controller = clientTextUI.getMyController();
            Spec spec = controller.getOriginalSpec();

            if (spec == null) {
                stdout.println("There is not any loaded specification.");
                return;
            }

            Map<RefExpr, Expr> axDefValues = controller.getAxDefsValues();
            Set<Map.Entry<RefExpr, Expr>> axDefValuesSet = axDefValues.entrySet();
            Map<String, List<String>> basicAxDefs = controller.getBasicAxDefs();
            Set<Map.Entry<String,List<String>>> basicAxDefsSet = basicAxDefs.entrySet();
            
            if (axDefValuesSet.isEmpty() && basicAxDefsSet.isEmpty()) {
                stdout.println("There is not any variable with an assigned value.");
                return;
            }
            printer.println("Variable\tValue");
            printer.println("--------\t-----");
            Iterator<Map.Entry<RefExpr, Expr>> axDefValuesSetIt =
                    axDefValuesSet.iterator();
            while (axDefValuesSetIt.hasNext()) {
                Map.Entry<RefExpr, Expr> entry = axDefValuesSetIt.next();
                RefExpr refExpr = entry.getKey();
                Expr expr = entry.getValue();
                printer.print(refExpr.getZName().toString());
                printer.print("\t\t");
                printer.println(SpecUtils.termToLatex(expr));
            }
            Iterator<Map.Entry<String, List<String>>> basicAxDefsSetIt =
                    basicAxDefsSet.iterator();
            while (basicAxDefsSetIt.hasNext()) {
                Map.Entry<String, List<String>> entry = basicAxDefsSetIt.next();
                List<String> constants = entry.getValue();
                for(int i=0; i< constants.size(); i++){
                    String constant = constants.get(i);
                    printer.println(constant + "\t\t"+ constant);
                }
            }

            printer.flush();
        } catch (IOException e) {
            System.out.println("IOException while printing results.");
        }

    }
}

