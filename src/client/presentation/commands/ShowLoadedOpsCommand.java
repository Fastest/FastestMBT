package client.presentation.commands;

import java.io.*;


import net.sourceforge.czt.z.ast.Spec;

import client.presentation.ClientTextUI;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import client.blogic.management.Controller;



/**
 * An instance of this class allow the presentation of the list of operations loaded with
 * the specification of the system to be tested. Those operations are the ones that can be 
 * tested.
 * @author Pablo Rodriguez Monetti
 */
public class ShowLoadedOpsCommand implements Command{ 

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
	public void run(ClientTextUI clientTextUI, String args){
		PrintWriter output = clientTextUI.getOutput();

		if (!args.equals("")){
    		output.println("Invalid parameters.  Try 'help'.");
		}
		else{
			Controller controller = clientTextUI.getMyController();
			Spec spec = controller.getUnfoldedSpec();
			
			if (spec == null)
				output.println("There is not any specification loaded.");
			else{
				AbstractRepository<String> opNamesRep = controller.getLoadedOpsRep();
				AbstractIterator<String> it = opNamesRep.createIterator();
				while(it.hasNext())
					output.println("* "+ it.next());
			}
		}	
	}

}
