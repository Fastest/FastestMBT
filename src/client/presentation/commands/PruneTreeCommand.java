package client.presentation.commands;

import java.io.*;

import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import compserver.prunning.PruneUtils;
import common.z.TClass;
import common.repository.AbstractRepository;
import client.blogic.management.ii.events.PruneTTreeRequested;
import client.blogic.management.ii.EventAdmin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;

/**
 * An instance of this class allows the prune of the tree.
 */
public class PruneTreeCommand implements Command {

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
             *             /*
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
            boolean someEventAnnounced = false;
            PruneUtils.prePrune(controller);
            AbstractRepository<TClass> leaves = PruneUtils.obtainTClasses(controller);

            if (leaves != null) {
                PruneTTreeRequested pruneTTreeRequested = new PruneTTreeRequested(leaves);
                EventAdmin eventAdmin = EventAdmin.getInstance();
                eventAdmin.announceEvent(pruneTTreeRequested);
                someEventAnnounced = true;
            } else {
                output.println("Pruning can be executed after a testing"
                        + " tree has been generated.");
                return;

            }
            if (someEventAnnounced) {
                synchronized (clientTextUI) {
                    clientTextUI.wait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
