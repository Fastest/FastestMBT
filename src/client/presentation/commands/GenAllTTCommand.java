package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.events.TTreeRequested;
import client.blogic.management.ii.events.UnfoldedSpecLoaded;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.testing.ttree.visitors.TClassNodeLeavesFinder;
import common.fastest.FastestUtils;
import net.sourceforge.czt.z.ast.Spec;

/**
 * Instances of this class allow the generation of test trees for the operations
 * selected to be tested.
 * @author Pablo Rodriguez Monetti
 */
public class GenAllTTCommand implements Command {

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {
        PrintWriter output = clientTextUI.getOutput();
        try {

            if (!args.equals("")) {
                output.println("Invalid parameters. Try 'help'.");
                return;
            }

            Controller controller = clientTextUI.getMyController();

            // At first, an unfolded version of the loaded specification is
            // generated

            Spec spec = controller.getOriginalSpec();
            AbstractRepository<String> opNames = controller.getOpsToTestRep();
            AbstractRepository<String> schPredNames = controller.getSchemaPredicatesRep();
            EventAdmin eventAdmin = EventAdmin.getInstance();

/*            Spec unfoldedSpec = (Spec) spec.accept
                    (new SchemeUnfolder(opNames,schPredNames));

            Spec unfoldedSpec = spec;

            
            eventAdmin.announceEvent(new UnfoldedSpecLoaded(unfoldedSpec));
            controller.setUnfoldedSpec(unfoldedSpec);
*/
            boolean someEventAnnounced = false;

            Map<String, TTreeStrategy> opTTreeStrategyMap =
                    controller.getOpTTreeStrategyMap();

            // We iterate over the repository that contains operations and test
            // classes with their associated tactics list
            Map<String, List<Tactic>> tacticMap = controller.getTacticMap();
            
            Set<Map.Entry<String, List<Tactic>>> tacticSet = tacticMap.entrySet();
            Iterator<Map.Entry<String, List<Tactic>>> tacticIterator =
                    tacticSet.iterator();
            while (tacticIterator.hasNext()) {
                Map.Entry<String, List<Tactic>> tacticEntry = tacticIterator.next();
                String opNameOrTClassName = tacticEntry.getKey();
                List<Tactic> tacticList = tacticEntry.getValue();
                boolean isOp = FastestUtils.isLoadedOperation(controller,
                        opNameOrTClassName);

                // We obtain the strategy for generate the tree
                TTreeStrategy tTreeStrategy = null;
                if (isOp) {
                    tTreeStrategy = opTTreeStrategyMap.get(opNameOrTClassName);
                } else {
                    tTreeStrategy = opTTreeStrategyMap.get(
                            FastestUtils.getOpAssociated(controller,
                            opNameOrTClassName));
                }

                if (tacticList == null) {
                    output.println("The test tree for  '" + opNameOrTClassName
                            + "' can not be generated."
                            + "Please add some tactic that can be applied to that "
                            + "operation in order to generate "
                            + " its test tree.");
                } else {
                    AbstractRepository<String> opsToTestRep =
                            controller.getOpsToTestRep();
                    AbstractRepository<String> classToTestRep =
                            controller.getClassToTestRep();
                    // Now we analyze if opName is the name of an operation or the
                    // name of a test class and proceed accordingly
                    if (isOp) {
                        Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
                        TClassNode tClassRoot = opTTreeMap.get(opNameOrTClassName);
                        if (tClassRoot == null) {
                            // We must generate the test tree from the beginning
                            someEventAnnounced = true;
                            opsToTestRep.addElement(opNameOrTClassName);
                            TTreeRequested tTreeRequested = new TTreeRequested(opNameOrTClassName,
                                    tacticList, tTreeStrategy, null);
                            eventAdmin.announceEvent(tTreeRequested);
                        } else {
                            // We must generate the test tree from
                            // every leaf of the operation's test tree
                            AbstractIterator<String> iter = opsToTestRep.createIterator();
                            // We must remove the opName from opsToTestRep 
                            // because we will just use the leaves corresponding
                            // to this opName
                            while (iter.hasNext()) {
                                if (iter.next().equals(opNameOrTClassName)) {
                                    iter.remove();
                                }
                            }
                            // We request the creation of subtrees from
                            // every leaf of the operation's test tree
                            AbstractRepository<TClassNode> leaves = tClassRoot.
                                    acceptVisitor(new TClassNodeLeavesFinder());
                            AbstractIterator<TClassNode> leavesIt = leaves.createIterator();
                            boolean someChild = false;
                            while (leavesIt.hasNext()) {
                                someChild = true;
                                TClassNode auxTClass = leavesIt.next();
                                String nodeName = auxTClass.getValue().getSchName();
                                classToTestRep.addElement(nodeName);
                                someEventAnnounced = true;
                                TTreeRequested tTreeRequested = new TTreeRequested(
                                        nodeName, tacticList, tTreeStrategy, auxTClass);
                                eventAdmin.announceEvent(tTreeRequested);
                            }
                            
                            if (!someChild) {
                                // The ttree has only one node
                                classToTestRep.addElement(opNameOrTClassName);
                                someEventAnnounced = true;
                                TTreeRequested tTreeRequested = new TTreeRequested(
                                        opNameOrTClassName, tacticList,
                                        tTreeStrategy, tClassRoot);
                                eventAdmin.announceEvent(tTreeRequested);
                            }
                        }
                    } else {
                        // The test tree will be generated from the specified
                        // test class
                        TClassNode tClassNodeAux = (TClassNode) 
                                FastestUtils.getTTreeNode(controller,
                                opNameOrTClassName);
                        AbstractRepository<TClassNode> leaves = tClassNodeAux.
                                acceptVisitor(new TClassNodeLeavesFinder());
                        AbstractIterator<TClassNode> it = leaves.createIterator();
                        boolean someChild = false;
                        while (it.hasNext()) {
                            someChild = true;
                            TClassNode auxTClass = it.next();
                            String nodeName = auxTClass.getValue().getSchName();
                            classToTestRep.addElement(nodeName);
                            someEventAnnounced = true;
                            TTreeRequested tTreeRequested = new TTreeRequested(nodeName,
                                    tacticList, tTreeStrategy, auxTClass);
                            eventAdmin.announceEvent(tTreeRequested);
                        }
                        // No have childs
                        if (!someChild) {
                            classToTestRep.addElement(opNameOrTClassName);
                            someEventAnnounced = true;
                            TTreeRequested tTreeRequested = new TTreeRequested(
                                    opNameOrTClassName, tacticList,
                                    tTreeStrategy, tClassNodeAux);
                            eventAdmin.announceEvent(tTreeRequested);
                        }
                    }
                }

            }
            if (someEventAnnounced) {
                synchronized (clientTextUI) {
                    clientTextUI.wait();
                }
            }

            // Quitamos la seleccion de las operaciones elegidas
            controller.setOpsToTestRep(new ConcreteRepository<String>());
            // Por ahora comento esta linea porque es la que me permite saber si una operacion
            // ya fue seleccionada o no
            //controller.setOpTTreeStrategyMap(new HashMap<String,TTreeStrategy>());
            controller.setTacticMap(new HashMap<String, List<Tactic>>());
        } catch (InterruptedException e) {
            System.out.println("ooooooooooooooooo");
        } catch (IllegalMonitorStateException e) {
            System.out.println(e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
