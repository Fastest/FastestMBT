package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import common.repository.ConcreteRepository;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;

/**
 * An instance of this class allow the unselection of all operations selected to be tested.
 * @author Pablo Rodriguez Monetti
 */
public class UnSelAllOpsCommand implements Command{ 
    
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
			controller.setOpsToTestRep(new ConcreteRepository<String>());
			controller.setOpTTreeStrategyMap(new HashMap<String,TTreeStrategy>());
			controller.setTacticMap(new HashMap<String,List<Tactic>>());
		}
	}
} 
