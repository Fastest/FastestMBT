package client.blogic.testing.refinement;

import java.io.*;

import common.z.AbstractTCase;
import common.z.SpecUtils;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.events.TCaseRefined;

/**
 * @author Hache
 */
public class TCaseRefClientRunner {
    private String opName;
    private AbstractTCase abstractTCase;
    private ConcreteTCase concreteTCase;
    private File tcrlFile;
    private String pathUUT;
    private String targetLanguaje;
    private String refLawName;
    private TCRL_AST ast;

    public TCaseRefClientRunner(String opName, AbstractTCase abstractTCase, String pathUUT, String targetLanguaje, String refLawName){
        this.opName = opName;
        this.abstractTCase = abstractTCase;
	this.pathUUT = pathUUT;
	this.targetLanguaje = targetLanguaje;
        this.refLawName = refLawName;
        this.start();
    }

    public ConcreteTCase getConcreteTCase(){
	return this.concreteTCase;
    }

    private void start(){
        try{
	    RefLawRepository refLawRepository = RefLawRepository.getInstance();
	    ast = refLawRepository.getRefLaw(refLawName);
            concreteTCase = RefineAST.refine(ast, abstractTCase, pathUUT, targetLanguaje);
            // We set the operation related to this concrete case
	    concreteTCase.setOpName(opName);
	    // We set the name of this concrete test case
	    String abstractName = SpecUtils.getAxParaName(abstractTCase);
	    String concreteName = abstractName.substring(0,abstractName.indexOf("_TCASE"))+"_CTCASE";
	    concreteTCase.setConcreteTCaseName(concreteName);
	    concreteTCase.setAbstractTCase(abstractTCase);
	    TCaseRefined tCaseRefinedEvent = new TCaseRefined(opName, abstractTCase, concreteTCase);
            try{
                EventAdmin eventAdmin = EventAdmin.getInstance();
                eventAdmin.announceEvent(tCaseRefinedEvent);
            }
            catch(Exception e){
                e.printStackTrace(System.out);
		System.exit(0);
            }
        }
        catch(Exception e){
            e.printStackTrace(System.out);
	    System.exit(0);
        }
    }

}
