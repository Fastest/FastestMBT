package client.blogic.testing.refinement.java;

import java.io.*;

import common.z.TClass;
import common.z.AbstractTCase;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.events.TCaseRefinedJAVA;

import client.blogic.testing.refinement.*;

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
public class TCaseRefClientRunner {
    
	private String opName;
	private TClass tClass;
	private AbstractTCase abstractTCase;
    private File tcrlFile;
    
    private TCRL_AST ast;
    private ConcreteTCase concreteTCase;
    
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
     * @param		opName			operation name of the test case refinement
     * @param		tClass			test class of the test case refinement
     * @param		abstractTCase	abstract test case of the case refinement
     * @param		tcrlFile		file in TCRL language that contains the refinement law
     */
    public TCaseRefClientRunner(String opName, TClass tClass, AbstractTCase abstractTCase, File tcrlFile){
		
    	this.opName = opName;
		this.tClass = tClass;
		this.abstractTCase = abstractTCase;
        this.tcrlFile = tcrlFile;
        
        this.start();
	}

    /**
     * Runs a test case refinement.
     * 
     * @author  Diego Hollman
     * @author  Pablo D. Coca
     *
     * @since   v1.0
     *
     * @version 2.0
     */
    private void start(){
        
    	try{
            
    		TCRL_Parser parser = new TCRL_Parser(new FileInputStream(tcrlFile));
    		//--------------------------------------------------------------------------------
    		//ast = parser.parse();		//RENOMBRADO, Sino tira un WARNING
            
    		ast = TCRL_Parser.parse();
    		//--------------------------------------------------------------------------------
    		ast = TCRL_PreProcessor.preProcess(ast);
    		//--------------------------------------------------------------------------------
    		concreteTCase = RefineAST.refine(ast, abstractTCase);
    		//--------------------------------------------------------------------------------
    		TCaseRefinedJAVA tCaseRefinedEvent = new TCaseRefinedJAVA(opName, tClass, concreteTCase);
            
    		try{
                
    			EventAdmin eventAdmin = EventAdmin.getInstance();
                eventAdmin.announceEvent(tCaseRefinedEvent);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            //--------------------------------------------------------------------------------
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
