package client.presentation.commands;

import java.io.*;
import java.util.*;


import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;


import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.tactics.MTSTactic;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import common.z.SpecUtils;
import common.z.OpScheme;
import common.z.OpSchemeImpl;
import client.blogic.testing.ttree.visitors.TTreeNodeFinder;
import client.blogic.testing.ttree.*;
import common.z.TClass;
import common.fastest.FastestUtils;

import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import common.z.czt.visitors.CZTCloner;

/**
 * An instance of this class makes possible the addition of a new tactic (together
 * with its parameters, if neccesary) to the tactic list associated with an operation 
 * or a test class to be tested.
 * @author Pablo Rodriguez Monetti
 */
public class AddTacticCommand implements Command {

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {

        PrintWriter output = clientTextUI.getOutput();
        // unitToTestName is the name of an operation or the name of a previously
        // generated test class
        String unitToTestName = "";
        String tacticName = "";
        try {
            // We make a little check of the parameters
            if (args == null || "".equals(args)) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            final String parts[] = args.split(" ", 3);
            if (parts.length != 2 && parts.length != 3) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            // We obtain the parameters
            unitToTestName = parts[0];
            tacticName = parts[1];
            Controller controller = clientTextUI.getMyController();

            // We check if unitToTestName is the name of a selected operation,
            // the name of an operation with a previously generated test tree or
            // the name of a test class previously generated
            boolean isOp = FastestUtils.isLoadedOperation(controller, unitToTestName);
            String opName = null;
            if (isOp) {
                // unitToTestName is an operation
                // Now we check if this operation either have a testing tree
                // associated or have been selected to be tested
                opName = unitToTestName;
                Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
                TClassNode tClassRoot = opTTreeMap.get(unitToTestName);
                if (tClassRoot == null) {
                    // It does not have a testing tree associated,
                    // so it must have been selected
                    boolean isSelOp = FastestUtils.isSelectedOperation(
                            controller, unitToTestName);
                    // If not, we finish returning an error message
                    if (!isSelOp) {
                        output.println("'" + unitToTestName + "' is not the name of "
                                + "a selected operation.");
                        return;
                    }
                }
            } else {
                // unitToTestName is not an operation
                // Now we check if unitToTestName is the name of a test class
                // contained/ in a test tree

                opName = FastestUtils.getOpAssociated(controller, unitToTestName);
                // If unitToTest is not a test class, we finish returning
                // an error message
                if (opName == null) {
                    output.println("'" + unitToTestName + "' is not the name of a "
                            + "loaded operation nor a test class.");
                    return;
                }


            }




            // If the user try to apply DNF tactic we return because this
            // tactic is applied by default
            if (tacticName.equals("DNF")) {
                return;
            }

            // We create the Tactic object using Java Reflection
            Class tacticClass = Class.forName("client.blogic.testing.ttree."
                    + "tactics." + parts[1] + "Tactic");
            Object object = tacticClass.newInstance();


            if (object instanceof Tactic) {

                Spec spec = controller.getOriginalSpec();

                AxPara axPara = null;
                for (Sect sect : spec.getSect()) {
                    if (sect instanceof ZSect) {
                        ParaList paraList = ((ZSect) sect).getParaList();
                        if (paraList instanceof ZParaList) {
                            axPara = SpecUtils.axParaSearch(opName,
                                    (ZParaList) paraList);
                        }
                    }
                }
                if (axPara == null) {
                    output.println("'" + opName + "' is not the name of "
                            + "a loaded operation.");
                    return;
                }

                OpScheme opScheme = new OpSchemeImpl(axPara);

                Tactic tactic = (Tactic) object;
                tactic.setOriginalOp(opScheme);
                tactic.setController(controller);
                tactic.setSectionManager(controller.getTypeCheckerManager());
                tactic.setSpec(spec);

                if (tactic.parseArgs(parts[2])) {
                    Map<String, List<Tactic>> opTacticMap = controller.
                            getTacticMap();
                    if (opTacticMap.get(unitToTestName) == null) {
                        opTacticMap.put(unitToTestName, new ArrayList<Tactic>());
                    }
                    opTacticMap.get(unitToTestName).add(tactic);
                } else {
                    output.println("Error at parsing arguments.");
                }

            }
        } catch (ClassNotFoundException e) {
            output.println("'" + tacticName
                    + "' is not the name of a class which extends Tactic.");
        } catch (InstantiationException e) {
            output.println("Error: The class " + tacticName + " could not be"
                    + " instantiated.");
        } catch (IllegalAccessException e) {
            output.println("Error: " + e);
            e.printStackTrace(output);
        }
    }
}