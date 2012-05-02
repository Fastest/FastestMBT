package client.presentation.commands;

import client.blogic.management.Controller;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import client.blogic.testing.ttree.tactics.Tactic;
import client.presentation.ClientTextUI;
import common.repository.AbstractIterator;
import common.repository.AbstractRepository;
import common.z.SpecUtils;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZSect;

/**
 * An instance of this class allow the selection of an schema as a predicate.
 * @author Pablo Rodriguez Monetti
 */
public class SelPredCommand implements Command {

    /**
     * Runs this command.
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {

        PrintWriter output = clientTextUI.getOutput();
        try {
            if (args == null || "".equals(args)) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            final String parts[] = args.split(" ");
            if (parts.length > 1) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            String schName = parts[0];

            Controller controller = clientTextUI.getMyController();
            Spec spec = controller.getUnfoldedSpec();

            // We check if the specified name correspond to the name of a
            // schema from the loaded specification
            boolean schemaFound = false;
            for (Sect sect : spec.getSect()) {
                if (sect instanceof ZSect) {
                    ParaList paraList = ((ZSect) sect).getParaList();
                    if (paraList instanceof ZParaList) {
                        ZParaList zParaList = (ZParaList) paraList;
                        for (int i = 0; i < zParaList.size(); i++) {
                            Para para = zParaList.get(i);
                            if (para instanceof AxPara) {
                                if ((SpecUtils.getAxParaName((AxPara) para).
                                        equals(schName))) {
                                    schemaFound = true;
                                }
                            }
                        }
                    }
                }
            }

            if (!schemaFound) {
                output.println("'" + schName + "' is not the name of a schema from the loaded specification.");
                return;
            }



            //We check if schema was already selected. If not, we select it.
            AbstractRepository<String> schemaPredsRep =
                    controller.getSchemaPredicatesRep();
            AbstractIterator<String> it = schemaPredsRep.createIterator();
            boolean hasFound = false;
            while (it.hasNext() && !hasFound) {
                if (it.next().equals(schName)) {
                    hasFound = true;
                }
            }
            if (!hasFound) {
                schemaPredsRep.addElement(schName);
            }

            // Now we check if the schema was previously selected as an
            // operation to be tested. If so, we remove it from the list of
            // selected operations
            AbstractRepository<String> opsToTestRep = controller.getOpsToTestRep();
            it = opsToTestRep.createIterator();
            hasFound = false;
            while (it.hasNext() && !hasFound) {
                if (it.next().equals(schName)) {
                    hasFound = true;
                    it.remove();
                }
            }
            if (hasFound) {
                Map<String, TTreeStrategy> opTTreeStrategyMap = controller.getOpTTreeStrategyMap();
                if (opTTreeStrategyMap.containsKey(schName)) {
                    opTTreeStrategyMap.remove(schName);
                }

                Map<String, List<Tactic>> opTacticMap = controller.getTacticMap();
                if (opTacticMap.containsKey(schName)) {
                    opTacticMap.remove(schName);
                }
            }
        } catch (Exception e) {
            output.println(e.getMessage());
            output.println("Error while printing schema predicates.");
        }

    }
}
