package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import client.blogic.testing.refinement.ConcreteTCase;

/**
 *
 * @author Hache
 */
public class ShowAllTCRCommand implements Command{
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args){
        PrintWriter stdout = clientTextUI.getOutput();
        Controller controller = clientTextUI.getMyController();
        Map<String, ConcreteTCase> atcCtcMap = controller.getAbsTCaseConcrTCaseMap();
        Set<String> namesSet = atcCtcMap.keySet();
        Iterator<String> setIter = namesSet.iterator();
        String ctcName;
        PrintWriter printer;

        final String argv[] = args.split(" ");
        final int argc = argv.length;

        if (args.equals("")){
            printer = stdout;
            while (setIter.hasNext()){
                ctcName = setIter.next();
                System.out.println("----------------------------------------");
                System.out.println("*" + ctcName);
                System.out.println("----------------------------------------");
                printer.println(atcCtcMap.get(ctcName).toString());
            }
        }
        else if((argc == 1) && (argv[0].equals("-o"))){
            while (setIter.hasNext()){
                ctcName = setIter.next();
                try{
                    printer = new PrintWriter(new FileWriter(ctcName + ".c"));
                    printer.println(atcCtcMap.get(ctcName).toString());
                    printer.flush();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else{
            stdout.println("Invalid parameters.  Try 'help'.");
        }
    }
}
