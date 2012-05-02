package common.z;

import client.blogic.management.Controller;
import common.z.czt.visitors.DNFPredExtractor;
import common.z.czt.visitors.DeclsExtractor;
import java.util.List;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ZParaList;

/**
 * This class allows the user to generate the Valid Input Space (VIS) of an
 * operation unfolding only those schemas that are not selected as operations
 * nor as predicates.
 * @author Pablo Rodriguez Monetti
 */
public class VISGen {

    // A reference to the list of paragraphs of the loaded specification
    private static ZParaList zParaList;
    // A reference to the current instance of Controller, which is used to
    // determine if a given schema reference correspond or not to a schema 
    // selected as operation or as predicate. This instance of Controller is
    // also used to keep the previously calculated lists of predicates in DNF,
    // with each list associated to some schema of the specification; this 
    // information is important to avoid recalculating a list twice
    private static Controller controller;
    
    /**
     * Generates the Valid Input Space (VIS) for the specified operation,
     * unfolding only those schemas that are not selected as operations
     * nor as predicates.
     * @param opScheme the operation schema for which the VIS will be calculated
     * @return the VIS associated to the operation, which is a particular test
     * class
     */
    public static TClass generateVIS(OpScheme opScheme) {
        AxPara opAxPara = opScheme.getMyAxPara();
        String opName = SpecUtils.getAxParaName(opAxPara);

        // The declaration part of the VIS is calculated unfolding the schemas
        // that are not selected as operations nor as predicates
        DeclList visDeclList = opAxPara.accept(new DeclsExtractor(zParaList,
                controller));

        // The predicate part of the VIS is the conjunction of the following
        // list of predicates, each of them in DNF. The list is calculated 
        // unfolding the schemas that are not selected as operations nor as 
        // predicates
        List<Pred> dnfPredList =
                opAxPara.accept(new DNFPredExtractor(zParaList, controller));
        // The list is saved for future use (in the DNF calculation)
        controller.putInSchemaDNFPredList(opName, dnfPredList);
        Pred visPred = SpecUtils.createAndPred(dnfPredList);

        AxPara visAxPara = SpecUtils.createAxPara(visDeclList, visPred);

        return new TClassImpl(visAxPara, opName + "_VIS");
    }

    /**
     * Sets the reference to the list of paragraphs
     * @param paraList the reference to the list of paragraphs
     */
    public static void setZParaList(ZParaList paraList) {
        zParaList = paraList;
    }

    /**
     * Sets the reference to the Controller
     * @param control the reference to the Controller
     */
    public static void setController(Controller control) {
        controller = control;
    }
}
