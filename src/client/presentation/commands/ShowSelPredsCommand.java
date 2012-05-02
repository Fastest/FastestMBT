package client.presentation.commands;

import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import common.repository.AbstractIterator;
import common.repository.AbstractRepository;
import java.io.PrintWriter;

/**
 * An instance of this class allow the presentation of the names of the
 * schemas selected as schema predicates.
 * @author Pablo Rodriguez Monetti
 */
public class ShowSelPredsCommand implements Command {

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

            if (controller.getOriginalSpec() == null) {
                output.println("There is not any loaded specification.");

            } else {
                AbstractRepository<String> schemaPredsRep =
                        controller.getSchemaPredicatesRep();
                AbstractIterator<String> schemaPredsIt = schemaPredsRep.createIterator();
                while (schemaPredsIt.hasNext()) {
                    String schemaPred = schemaPredsIt.next();
                    output.println(schemaPred);
                }
            }
        }
    }
}
