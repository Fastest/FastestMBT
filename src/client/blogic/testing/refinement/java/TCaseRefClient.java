package client.blogic.testing.refinement.java;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;

import common.z.AbstractTCase;
import common.z.AbstractTCaseImpl;
import common.z.SpecUtils;
import common.z.TClass;

import client.blogic.management.ii.events.Event_;
import client.blogic.management.ii.events.TCaseGenerated;
import client.blogic.management.ii.events.TCaseRefineRequestedJAVA;
import client.blogic.management.ii.events.RefLawSelected;

import client.blogic.testing.refinement.ConcreteTCase;
//import client.blogic.testing.refinement.Utils;

/**
 * Represents test case refinement to run as a client.
 * 
 * @author  Diego Hollman
 * @author  Pablo D. Coca
 *
 * @since   v1.0
 *
 * @version 2.0
 */
public class TCaseRefClient {

    private AbstractTCase abstractTCase;
    private List<AbstractTCase> abstractTCaseList;
    private List<ConcreteTCase> concreteTCaseList;
    private String opName;
    private TClass tClass;
    private File tcrlFile;
    private String refLawName;

    /**
     * Constructor of the class.
     * 
     * @author  Diego Hollman
     * @author  Pablo D. Coca
     *
     * @since   v1.0
     *
     * @version 2.0
     * 
     * @param		abstractTCaseList		abstract test case list
     * @param		concreteTCaseList		concrete test case list
     * */
    public TCaseRefClient() {
        abstractTCaseList = new ArrayList<AbstractTCase>();
        concreteTCaseList = new ArrayList<ConcreteTCase>();
    }

    public synchronized void manageEvent(Event_ event_)
            throws IllegalArgumentException {

        if (event_ instanceof RefLawSelected) {

            refLawName = ((RefLawSelected) event_).getRefLawName();
            tcrlFile = ((RefLawSelected) event_).getTCRLFile();

        }

        if (event_ instanceof TCaseGenerated) {

            abstractTCaseList.add(((TCaseGenerated) event_).getAbstractTCase());
        } else if (event_ instanceof TCaseRefineRequestedJAVA) {

            TCaseRefineRequestedJAVA tCaseRefineRequestEvent = (TCaseRefineRequestedJAVA) event_;

            opName = tCaseRefineRequestEvent.getOpName();
            tClass = tCaseRefineRequestEvent.getTClass();
            abstractTCase = tCaseRefineRequestEvent.getAbstractTCase();

            // Runs the test case refinement
            new TCaseRefClientRunner(opName, tClass, abstractTCase, tcrlFile);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // The main() was created to do tests INDEPENDENTLY of the rest of the system
    public static void main(String[] args) {
        TCRL_AST ast;
        SectionManager manager = new SectionManager();
        ConcreteTCase ctc;

        try {
            //--------------------------------------------------------------------------------
            // Gets the FIRST ARGUMENT, the TCRL FILE (.tcrl)
            File f = new File(args[0]);

            //TCRL_Parser parser	= new TCRL_Parser(new FileInputStream(f));
            new TCRL_Parser(new FileInputStream(f));

            // PARSE the TCRL FILE
            ast = TCRL_Parser.parse();
            //--------------------------------------------------------------------------------

            //--------------------------------------------------------------------------------
            // REPLACES the SYNONYMS in the TCRL AST
            ast = TCRL_PreProcessor.preProcess(ast);
            //--------------------------------------------------------------------------------

            // Pasando 2 veces el PREPROCESADOR no quedan SIN�NIMOS
            // ast					= TCRL_PreProcessor.preProcess(ast);

            //--------------------------------------------------------------------------------
            // Gets the SECOND ARGUMENT, the TEX FILE (.tex)
            FileSource source = new FileSource(args[1]);
            //--------------------------------------------------------------------------------

            manager.put(new Key(source.getName(),
                    Source.class),
                    source);

            Spec spec = (Spec) manager.get(new Key(source.getName(),
                    Spec.class));

            //--------------------------------------------------------------------------------


            List<AxPara> axParaList = Test.getAxParaList(spec);

            int axParaListSize = axParaList.size();

            if (axParaListSize == 1) {
//            for (int i = 0; i < axParaListSize; i++)
//            {

                //AxPara		axPara		=	axParaList.get(i);

                AxPara axPara = axParaList.get(0);	// the only that MUST be.

                // EXTRACTS the NAME of an Z TEST CASE.
                // EXAMPLE: "Depositar_DNF_1_TCASE"
                String testName = SpecUtils.getAxParaName(axPara);

                AbstractTCaseImpl atc = new AbstractTCaseImpl(axPara, testName);

                ctc = RefineAST.refine(ast, atc);

                System.out.println(
                        UtilsJava.printCTC(testName, ctc));
//            }
            } else {
                UtilsJava.printErrorInvalidNumberOfTestCase(axParaList.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
