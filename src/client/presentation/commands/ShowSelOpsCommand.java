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
 * An instance of this class allow the presentation of the names of the operations selected
 * to be tested, together with the list of tactics to be applied to each one.
 * @author Pablo Rodriguez Monetti
 */
public class ShowSelOpsCommand implements Command{ 

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
	public void run(ClientTextUI clientTextUI, String args){
		PrintWriter output = clientTextUI.getOutput();

		if (!args.equals("")){
    		output.println("Invalid parameters.  Try 'help'.");

		}
		else{
			Controller controller = clientTextUI.getMyController();

			if(controller.getOriginalSpec()==null){
				output.println("There is not any loaded specification.");
					
			}
			else{
				AbstractRepository<String> opsToTestRep = controller.getOpsToTestRep();
				Map<String,TTreeStrategy> opTTreeStrategyMap = controller.getOpTTreeStrategyMap();
				Map<String,List<Tactic>> opTacticMap = controller. getTacticMap();
				AbstractIterator<String> opsIt = opsToTestRep.createIterator();
				while (opsIt.hasNext()){
					String opName = opsIt.next();
					TTreeStrategy tTreeStrategy = opTTreeStrategyMap.get(opName);
					output.println("* Operation: "+ opName);
					List<Tactic> tacticList = opTacticMap.get(opName);
					if(tacticList == null)
						output.println("  There is no tactic loaded for the '" + opName + "' operation.");
					else{
						output.println("  Tactic Repository:");
						for(int i=0; i<tacticList.size(); i++)
							output.println("   - " + tacticList.get(i).getTacticInfo().getTacticName());
					}	
				}
			}
		}
	}

}
 
 
