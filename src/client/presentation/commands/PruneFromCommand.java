package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import compserver.prunning.TreePruner;


/**
 * An instance of this class allows the pruning of test classes taken from the 
 * test trees previously generated. The command needs the name of the test class
 * that will be pruned.
 * @author Pablo Rodriguez Monetti
 */
public class PruneFromCommand implements Command{
    
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args){
		PrintWriter output = clientTextUI.getOutput();
		try{
			if (args == null || "".equals(args)){
	    		output.println("Invalid parameters.  Try 'help'.");
				return;
			}
		
			final String parts[] = args.split(" ");
			if (parts.length != 1){
				output.println("Invalid parameters.  Try 'help'.");
				return;
			}
		
			String tClassName = parts[0];      
            		Controller controller = clientTextUI.getMyController();
			boolean result = (new TreePruner(controller)).pruneFrom(tClassName);

			if (!result)
				output.println("'" + tClassName + "' is not the name of any " + "test class of the generated test trees."); 
        }
        catch(Exception e){
            e.printStackTrace();
        }
           
    }    
    

}
