package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.blogic.testing.ttree.TClassNode;
import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import client.blogic.testing.ttree.visitors.TTreeFromPruner;

/**
 * An instance of this class allows the unpruning of test classes, taken from the test trees,
 * that have been previously pruned.
 * @author Pablo Rodriguez Monetti
 */
public class UnPruneCommand implements Command{ 
    
    
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */    
    @Override
    public void run(ClientTextUI clientTextUI, String args){
		PrintWriter output = clientTextUI.getOutput();
		String tTreeStrategyName = "";
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
            Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
            Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            Boolean result = new Boolean(false);
            TTreeFromPruner tTreePruner = new TTreeFromPruner(tClassName, false);
            while(iterator.hasNext() && !result.booleanValue()){
                Map.Entry<String, TClassNode> mapEntry = iterator.next();
                TClassNode opTTreeRoot = mapEntry.getValue();  
                result = opTTreeRoot.acceptVisitor(tTreePruner);                    
            }          

            if (!result.booleanValue())
                  output.println("'" + tClassName + "' is not the name of any " +
                      "test class of the generated test trees."); 

            
        }
        catch(Exception e){
            e.printStackTrace();
        }
           
    }    
    

}
