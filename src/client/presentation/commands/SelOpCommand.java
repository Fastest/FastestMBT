package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.tactics.DNFTactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import net.sourceforge.czt.z.ast.*;
import common.z.OpScheme;
import common.z.OpSchemeImpl;
import common.z.SpecUtils;

/**
 * An instance of this class allow the selection of an operation to be tested, together with
 * the strategy of tactic application, which is needed to generate a test tree associated
 * to the operation.
 * @author Pablo Rodriguez Monetti
 */
public class SelOpCommand implements Command {

    private final String defaultTTreeStrategy = "IterativeTTreeStrategy";

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {

        PrintWriter output = clientTextUI.getOutput();
        String tTreeStrategyName = "";
        try {
            if (args == null || "".equals(args)) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            final String parts[] = args.split(" ");
            if (parts.length > 1) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            String opName = parts[0];

            tTreeStrategyName = defaultTTreeStrategy;

            Controller controller = clientTextUI.getMyController();

            //We check if the name of the operation to be selected is contained
            //in the repository of loaded operations names
            AbstractRepository<String> loadedOpsRep = controller.getLoadedOpsRep();
            AbstractIterator<String> it = loadedOpsRep.createIterator();
            boolean hasFound = false;
            while (it.hasNext() && !hasFound) {
                if (it.next().equals(opName)) {
                    hasFound = true;
                }
            }

            if (!hasFound) {
                output.println("'" + opName + "' is not the name of a loaded operation.");
                return;
            }

            //We check if the operation to be selected has been already selected
            AbstractRepository<String> opsToTestRep = controller.getOpsToTestRep();
            AbstractIterator<String> opsToTestIt = opsToTestRep.createIterator();
            hasFound = false;
            while (opsToTestIt.hasNext() && !hasFound) {
                if (opsToTestIt.next().equals(opName)) {
                    hasFound = true;
                }
            }
            if (!hasFound) {
                opsToTestRep.addElement(opName);
            }

            //If the second parameter is an appropiate TTreeStrategy class we set it as the operation's tactic strategy  in
            // the controller's opTTreeStrategyMap
            Class tTreeStrategyClass = Class.forName("client.blogic.testing.ttree.strategies." + tTreeStrategyName);
            Object tTreeStrategy = tTreeStrategyClass.newInstance();
            Map<String, TTreeStrategy> opTTreeStrategyMap = controller.getOpTTreeStrategyMap();
            // We analyze if the operation was selected in another step of testing
            boolean wasSelected = false;
            if (opTTreeStrategyMap.get(opName) != null) {
                wasSelected = true;
            }

            if (tTreeStrategy instanceof TTreeStrategy) {
                opTTreeStrategyMap.put(opName, (TTreeStrategy) tTreeStrategy);
            }

            // If this is the first time that the operation is selected, we add
            // by default the DNF tactic
            if (!wasSelected) {
                Map<String, List<Tactic>> opTacticMap = controller.getTacticMap();

                DNFTactic dnfTactic = new DNFTactic();
                // We obtain the AxPara related to this opName
                Spec spec = controller.getUnfoldedSpec();
                AxPara axPara = null;

                for (Sect sect : spec.getSect()) {
                    if (sect instanceof ZSect) {
                        ParaList paraList = ((ZSect) sect).getParaList();
                        if (paraList instanceof ZParaList) {
                            ZParaList zParaListUnFold = (ZParaList) paraList;
                            axPara = SpecUtils.axParaSearch(opName, (ZParaList) paraList);
                        }
                    }
                }

                OpScheme opScheme = new OpSchemeImpl(axPara);
                dnfTactic.setOriginalOp(opScheme);
                dnfTactic.setController(controller);
                opTacticMap.put(opName, new ArrayList<Tactic>());
                opTacticMap.get(opName).add(dnfTactic);
            }


            // We check if the operation to be selected was previously selected
            // as a predicate. If so, we remove it from the repository of schemas
            // selected as predicates.
            AbstractRepository<String> schemaPredicatesRep =
                    controller.getSchemaPredicatesRep();
            AbstractIterator<String> schemaPredicatesIt =
                    schemaPredicatesRep.createIterator();
            hasFound = false;
            while (schemaPredicatesIt.hasNext() && !hasFound) {
                if (schemaPredicatesIt.next().equals(opName)) {
                    hasFound = true;
                    schemaPredicatesIt.remove();
                }
            }



        } catch (ClassNotFoundException e) {
            output.println("'" + tTreeStrategyName
                    + "' is not the name of a class which extends TTreeStrategy.");
        } catch (InstantiationException e) {
            output.println("Error: The class " + tTreeStrategyName + " could not be instantiated.");
        } catch (IllegalAccessException e) {
            output.println("Error: " + e);
            e.printStackTrace(output);
        }
    }
}
