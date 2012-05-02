package client.presentation.commands;

import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import common.repository.ConcreteRepository;
import java.io.PrintWriter;

/**
 * An instance of this class removes the selection of all the schemas marked as
 * an schema predicates.
 * @author Pablo Rodriguez Monetti
 */
public class UnSelAllPredsCommand implements Command {

    /**
     * Runs this command.
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {

        PrintWriter output = clientTextUI.getOutput();
        if (!args.equals("")) {
            output.println("Invalid parameters.  Try 'help'.");
        } else {
            Controller controller = clientTextUI.getMyController();
            controller.setSchemaPredicatesRep(new ConcreteRepository<String>());

        }
    }
}
