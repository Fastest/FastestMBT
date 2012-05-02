package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;

/**
 * An instance of this class removes the selection of an operation to be tested.
 * @author Pablo Rodriguez Monetti
 */
public class UnSelOpCommand implements Command{ 

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
	public void run(ClientTextUI clientTextUI, String args){
		
			PrintWriter output = clientTextUI.getOutput();
			if (args == null || "".equals(args)){
	    		output.println("Invalid parameters.  Try 'help'.");
				return;	
			}
			
			final String parts[] = args.split(" ");
			if (parts.length !=1){
				output.println("Invalid parameters.  Try 'help'.");
				return;
			}
			
			String opName = parts[0];
	
			Controller controller = clientTextUI.getMyController();
			
			//We check if the name of the operation to be unselected is contained in the repository of loaded operations names
			AbstractRepository<String> loadedOpsRep = controller.getLoadedOpsRep();
			AbstractIterator<String> it = loadedOpsRep.createIterator();
			boolean hasFound = false; 
			while (it.hasNext() && !hasFound)
				if(it.next().equals(opName))
					hasFound = true;			
			if(!hasFound){
				output.println("'" + opName + "' is not the name of a loaded operation."); 
				return;
			}
	
			//We check if the operation to be unselected has been selected. If so, we remove it from the repository.
			AbstractRepository<String> opsToTestRep = controller.getOpsToTestRep();
			it = opsToTestRep.createIterator();
			hasFound = false; 
			while (it.hasNext() && !hasFound)
				if(it.next().equals(opName)){
					hasFound = true;
					it.remove();
				}
			if(!hasFound)
				output.println("'"+ opName + "' is not the name of a testing list's operation.");
				Map<String,TTreeStrategy> opTTreeStrategyMap = controller.getOpTTreeStrategyMap();
			if(opTTreeStrategyMap.containsKey(opName))
				opTTreeStrategyMap.remove(opName);
			
			Map<String,List<Tactic>> opTacticMap = controller.getTacticMap();
			if(opTacticMap.containsKey(opName))
				opTacticMap.remove(opName);
	
	}
}
 
 
