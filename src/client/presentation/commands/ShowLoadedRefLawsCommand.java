package client.presentation.commands;

import java.io.*;

import client.presentation.ClientTextUI;
import java.util.Set;
import java.util.Iterator;
import client.blogic.testing.refinement.RefLawRepository;
import client.blogic.management.Controller;

/**
 *
 * @author Hache
 */
public class ShowLoadedRefLawsCommand implements Command{
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

            RefLawRepository refLawRepository = controller.getRefLawRepository();

            if (refLawRepository.size() == 0){
                output.println("There is not any refinement law in the repository.");
            }
            else{
                Set<String> refLawNames = refLawRepository.getRefLawNames();
                Iterator<String> it = refLawNames.iterator();
                while(it.hasNext())
                    output.println("* "+ it.next());
            }
        }
    }
}
