package client.presentation.commands;

import java.io.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;

/**
 *
 * @author Hache
 */
public class ShowSelRefLawCommand implements Command{

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

            if(controller.getSelRefLaw() == null){
                output.println("There is no refinement law selected.");
            }
            else{
                output.println("* " + controller.getSelRefLaw());
            }
        }
    }
}
