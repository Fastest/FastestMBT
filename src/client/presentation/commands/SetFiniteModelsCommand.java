/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.commands;


import java.io.*;
import java.util.*;


import client.presentation.ClientTextUI;
import client.blogic.management.ii.events.TCaseStrategySelected;
import compserver.tcasegen.strategies.TCaseStrategy;
import client.presentation.tcasestrategyparsers.TCaseStrategyParser;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.management.ii.EventAdmin;
import client.blogic.testing.ttree.visitors.TClassLeavesFinder;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import common.z.TClass;

/**
 * An instance of this class allows the setting of the strategy of generation of test cases
 * that will be used together with the specified test class. 
 * @author Pablo Rodriguez Monetti
 */
public class SetFiniteModelsCommand implements Command{ 

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
	public void run(ClientTextUI clientTextUI, String args){
        PrintWriter stdout = clientTextUI.getOutput();
        try{         
            if (args == null || "".equals(args)){
	    		stdout.println("Invalid parameters.  Try 'help'.");
				return;	
			}
			
			final String parts[] = args.split(" ",2);
            int argc = parts.length;  
			if (parts.length != 2){
				stdout.println("Invalid parameters.  Try 'help'.");
				return;
			}
            
            String tClassName = "";
            String strategyName = "AtomicPred";
            //String strategyName = "Iterative";
            
            if(!parts[0].equals("")){
                tClassName = parts[0];
            }


            // We check whether the test class exists
            Controller controller = clientTextUI.getMyController();
            EventAdmin eventAdmin = EventAdmin.getInstance();
            Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
            Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            TClass foundTClass = null;
            while(iterator.hasNext()  && foundTClass == null){
                Map.Entry<String, TClassNode> mapEntry = iterator.next();
                TClassNode opTTreeRoot = mapEntry.getValue();
				AbstractRepository<TClass> tClassLeaves = opTTreeRoot.acceptVisitor(new TClassLeavesFinder());
				AbstractIterator<TClass> tClassIt = tClassLeaves.createIterator();
				while(tClassIt.hasNext() && foundTClass == null){
                    TClass tClass = tClassIt.next();
                    String auxTClassName = tClass.getSchName();
                    if(auxTClassName.equals(tClassName))
                        foundTClass = tClass;
				}
            }
            if(foundTClass == null){
                stdout.println("Error at parsing arguments. The specified test " +
                        "class could not be found.");
                return;
            }


            
            
            // We create the TCaseStrategy object using Java Reflection
			Class tCaseStrategyClass = Class.forName("compserver.tcasegen.strategies." + strategyName + "TCaseStrategy");
            Class tCaseStrategyParserClass = Class.forName("client.presentation.tcasestrategyparsers." + strategyName + "TCaseStrategyParser");
			Object strategyObject = tCaseStrategyClass.newInstance();
            Object strategyParserObject = tCaseStrategyParserClass.newInstance();

            
            
			if (strategyObject instanceof TCaseStrategy && 
                    strategyParserObject instanceof TCaseStrategyParser){
                TCaseStrategy tCaseStrategy = (TCaseStrategy) strategyObject;
                TCaseStrategyParser tCaseStrategyParser = (TCaseStrategyParser) strategyParserObject;
                
                tCaseStrategyParser.setClientTextUI(clientTextUI);
                if(tCaseStrategyParser.parse(foundTClass, parts[1], tCaseStrategy)){
                    TCaseStrategySelected tCaseStrategySelected = new TCaseStrategySelected(tClassName, tCaseStrategy);
                    eventAdmin.announceEvent(tCaseStrategySelected);
                }
                else{
                    stdout.println("Error at parsing arguments.");                   
                }
			}
            else{
                stdout.println("Unexpected error.");       
            }
                

        
        
        }
        catch(ClassNotFoundException e){
            stdout.println("Invalid strategy.");
            e.printStackTrace();
        }
        catch(IllegalAccessException e){
            stdout.println("Access error.");
            e.printStackTrace();            
        }
        catch(InstantiationException e){
            stdout.println("Invalid strategy.");
            e.printStackTrace();            
        }
    }

}
