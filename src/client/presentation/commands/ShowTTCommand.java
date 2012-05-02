package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.testing.ttree.visitors.TTreeVisitor;
import client.blogic.testing.ttree.visitors.TTreeTextUIPrinter;
import client.blogic.testing.ttree.visitors.TTreeTextUIPrunePrinter;
/**
 * An instance of this class allow the presentation of the test trees previously generated.
 * @author Pablo Rodriguez Monetti
 */
public class ShowTTCommand implements Command{ 
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
	public void run(ClientTextUI clientTextUI, String args){
		PrintWriter output = clientTextUI.getOutput();
        try{
    		final String argv[] = args.split(" ");
            int argc = argv.length;
            
            String opName = "";
            PrintWriter printer = output;
            boolean showPrunedTestClasses = false;
            for(int index = 0, argCount=1; index < argc; index += argCount ){
                if(argv[index].equals("-p")){
                    opName = argv[index+1];
                    argCount = 2;
                }
                else if(argv[index].equals("-o")){
                    String fileName = argv[index+1];
                    printer = new PrintWriter(new FileWriter(fileName));
                    clientTextUI.setOutput(printer);
                    argCount = 2;
                }
                else if(argv[index].equals("-x")){
                    showPrunedTestClasses = true;
                    argCount = 2;
                }
                else if (!argv[index].equals("")){
                    output.println("Invalid parameters.  Try 'help'.");
                    return;              
                }    
            }

               
    		Controller controller = clientTextUI.getMyController();
            Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();

            TTreeVisitor<Void> tTreeVisitor = null;
            if(showPrunedTestClasses)
                 tTreeVisitor = new TTreeTextUIPrunePrinter(printer);
            else
                 tTreeVisitor = new TTreeTextUIPrinter(printer);
            
            
            
            if(opName.equals("")){
                Collection<TClassNode> opTTreeCollection = opTTreeMap.values();
                Iterator<TClassNode> tTreeIt = opTTreeCollection.iterator();
	

                while(tTreeIt.hasNext()){
                    printer.println();
                    TClassNode opTTreeRoot = tTreeIt.next();
                    opTTreeRoot.acceptVisitor(tTreeVisitor);
                }
                printer.println();
            }
            else{
                TClassNode opTTreeRoot = opTTreeMap.get(opName);

                if(opTTreeRoot == null){
                    output.println("'" + opName + "' is not the name of any operation for which there is a " +
                        " test tree generated."); 
                }
                else{
                    printer.println();
                    opTTreeRoot.acceptVisitor(tTreeVisitor);
                    printer.println();
                }             
            }
		}
        catch(Exception e){
           e.printStackTrace(); 
        }
        finally{
            clientTextUI.setOutput(output);
        }
	}


}
 
