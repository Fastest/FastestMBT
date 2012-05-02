package client.presentation.commands;

import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import common.repository.AbstractIterator;
import common.repository.AbstractRepository;
import java.io.PrintWriter;

/**
 * An instance of this class removes the selection of an schema to be marked as
 * an schema predicate.
 * @author Pablo Rodriguez Monetti
 */
public class UnSelPredCommand implements Command {

    /**
     * Runs this command.
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {

        PrintWriter output = clientTextUI.getOutput();
        if (args == null || "".equals(args)) {
            output.println("Invalid parameters.  Try 'help'.");
            return;
        }

        final String parts[] = args.split(" ");
        if (parts.length != 1) {
            output.println("Invalid parameters.  Try 'help'.");
            return;
        }

        String opName = parts[0];

        Controller controller = clientTextUI.getMyController();

        //We check if the schema to be unselected has been selected. If so,
        // we remove it from the repository of schemas selected as predicates
        AbstractRepository<String> schemaPredicatesRep =
                controller.getSchemaPredicatesRep();
        AbstractIterator<String> it = schemaPredicatesRep.createIterator();
        boolean hasFound = false;
        while (it.hasNext() && !hasFound) {
            if (it.next().equals(opName)) {
                hasFound = true;
                it.remove();
            }
        }
        if (!hasFound) {
            output.println("'" + opName + "' is not the name of a schema "
                    + "selected as predicate");
        }

    }
}
