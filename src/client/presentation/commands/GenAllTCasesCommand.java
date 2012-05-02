package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import common.repository.ConcreteRepository;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.events.TCaseRequested;
import client.blogic.testing.ttree.visitors.TClassLeavesFinder;
import common.repository.AbstractIterator;
import common.repository.AbstractRepository;
import common.z.TClass;

/**
 * Instances of this class allow the generation of test cases for the operations for which 
 * there is a test tree generated.
 * @author Pablo Rodriguez Monetti
 */
public class GenAllTCasesCommand implements Command {

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

            /*
             * The following piece of commented code implements the checking
             * that all axiomatic definitions have a value assigned to them
            Map<String,Expr> axDefsRequired = controller.getAxDefsRequired();
            Map<RefExpr, Expr> axDefsValues = controller.getAxDefsValues();

            Set<Map.Entry<String, Expr>> requiredAxDefsSet = axDefsRequired.entrySet();
            Iterator<Map.Entry<String, Expr>> requiredAxDefsIt = requiredAxDefsSet.iterator();
            List<String> varsWithoutValue = new ArrayList<String>();
            while(requiredAxDefsIt.hasNext()){
            Map.Entry<String,Expr> requiredAxDefEntry = requiredAxDefsIt.next();
            String varName = requiredAxDefEntry.getKey();

            Set<Map.Entry<RefExpr, Expr>> axDefsValuesSet = axDefsValues.entrySet();
            Iterator<Map.Entry<RefExpr, Expr>> axDefsValuesIt = axDefsValuesSet.iterator();
            boolean theVarHasAValue = false;
            while(axDefsValuesIt.hasNext() && !theVarHasAValue){
            Map.Entry<RefExpr,Expr> axDefValueEntry = axDefsValuesIt.next();
            RefExpr axDefRefExpr = axDefValueEntry.getKey();
            if(varName.equals(axDefRefExpr.getName().toString())){
            theVarHasAValue = true;
            }

            }
            if(!theVarHasAValue)
            varsWithoutValue.add(varName);

            }

            if(!varsWithoutValue.isEmpty()){
            output.print("The following variables, defined in axiomatic" +
            " definitions, \nmust have a value assigned to them" +
            " before initiating the test \ncase generation: ");
            output.print(varsWithoutValue.get(0));
            for(int i=1; i< varsWithoutValue.size(); i++){
            output.print(", " + varsWithoutValue.get(i));
            }
            output.println(".");
            return;
            }
             */
            EventAdmin eventAdmin = EventAdmin.getInstance();
            Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
            Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            int maxEval = controller.getMaxEval();

            boolean someEventAnnounced = false;
            while (iterator.hasNext()) {
                Map.Entry<String, TClassNode> mapEntry = iterator.next();
                String opName = mapEntry.getKey();
                TClassNode opTTreeRoot = mapEntry.getValue();


                // Extracts all the TCLassNodes that are leaves of the tClassNode test tree
                // except for those leaves that are descendants of pruned test classes.
                AbstractRepository<TClass> tClassLeaves =
                        opTTreeRoot.acceptVisitor(new TClassLeavesFinder());
                AbstractIterator<TClass> tClassIt =
                        tClassLeaves.createIterator();
                while (tClassIt.hasNext()) {
                    someEventAnnounced = true;
                    TClass tClass = tClassIt.next();
                    TCaseRequested tCaseRequested =
                            new TCaseRequested(opName, tClass, maxEval);
                    eventAdmin.announceEvent(tCaseRequested);
                }


            }


            if (someEventAnnounced) {
                synchronized (clientTextUI) {
                    clientTextUI.wait();
                }
            }

            // Quitamos las seleccion de las operaciones elegidas
            controller.setOpsToTestRep(new ConcreteRepository<String>());
            controller.setOpTTreeStrategyMap(new HashMap<String, TTreeStrategy>());
            controller.setTacticMap(new HashMap<String, List<Tactic>>());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
