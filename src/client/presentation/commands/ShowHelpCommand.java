package client.presentation.commands;

import java.io.*;
import client.presentation.ClientTextUI;


/**
 * An instance of this class allow the presentation of the Fastest help.
 * @author Pablo Rodriguez Monetti
 */
public class ShowHelpCommand implements Command{ 

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */    
    @Override
	public void run(ClientTextUI clientTextUI, String args){

	PrintWriter out = clientTextUI.getOutput();
        String dots = "-----------------------------------";
	out.println("\n" + dots + "Fastest Help" + dots);
        out.println("Fastest commands:\n");
        
        out.println("loadspec <file_name>");
        out.println("\tReads a Z specification into Fastest.\n");
        
        out.println("showspec [-u] [-o <file_name>]");	
        out.println("\tShows the loaded specification.\n");
        
	out.println("showloadedops");
        out.println("\tShows the operation names of the loaded specification.\n");
        
	out.println("selop <op_name>");
	out.println("\tSelects the specified operation as one to be tested.\n");
        
	out.println("showselops");
	out.println("\tShows all selected operations to be tested.\n");
        
	out.println("unselop <op_name>");	
        out.println("\tRemoves the selection to be tested from the specified " +
                "operation.\n");
        
	out.println("unselallops");
        out.println("\tRemoves the selection to be tested from all the operations.\n");    
        
	out.println("selpred <sch_name>");
	out.println("\tSelects the specified schema as a predicate.\n");
        
	out.println("showselpreds");
	out.println("\tShows all schemas selected as predicates.\n");
        
	out.println("unselpred <sch_name>");
        out.println("\tRemoves the selection, as a predicate, of the specified"
                + " schema.\n");
        
	out.println("unselallpreds");
	out.println("\tRemoves the selection, as predicates, of all the schemas.\n");
        
        out.println("showtactics");	
        out.println("\tShows the available testing tactics in Fastest.\n");
        
        out.println("addtactic <op_name> <tactic_name> <tactic_parameters>");
	out.println("\tAdds a tactic and its parameters to the specified operation.\n");
        
	out.println("genalltt");	
	out.println("\tGenerates the test tree for every selected operation.\n");
        
        out.println("prunefrom <test_class_name>");
        out.println("\tPrunes a test tree from the specified test class.\n");
        
        out.println("prunebelow <test_class_name>");
        out.println("\tPrunes a test tree below the specified test class.\n");
        
        out.println("unprune <test_class_name>");	
        out.println("\tUnprunes a test tree from the specified test class.\n");
/*        
        out.println("setfinitemodel <tclass_name> [-size <size>] " +
                "[-btsize <size>]\n \t[-nzsize <size>] [-fm <list_of_finite_models>]");
        out.println("\tSets the finite model for the specified test class.\n");
        out.println("apply <theorem_name> <class_name> <parameter_list>");
        out.println("\tApplies the specified elimination theorem to the specified " +
                "test class\n"+
                "\tin order to see if it is possible to prune the test class " +
                "pruned.\n");
        out.println("loadelimtheorems");
        out.println("\tReloads the elimination theorems.\n");
        out.println("prunett");
        out.println("\tPrunes the previously generated test trees.\n");
        out.println("searchtheorems <class_name>");
        out.println("\tSearches the library for elimination theorems that might be used to\n" +
                "\tprune the specified test class.\n");
        out.println("setaxdef <var_name> ['\"'<constants_declaration>'\"'] " +
                "'\"'<value>'\"'");
        out.println("\tSets the value of a variable given in an axiomatic definition.\n");
        out.println("showaxdefs [-o <file_name>]");
        out.println("\tShows the axiomatic definitions loaded with the specification.\n");
        out.println("showaxdefvalues [-o <file_name>]");
        out.println("\tShows the values assigned to the variables given in an\n\t" +
                "axiomatic definition.\n");
        out.println("genalltca");        
	out.println("\tGenerates the test cases for every selected operation.\n");
 * 
 */
	out.println("showtt [-p <op_name>] [-o <file_name>] [-x]");
	out.println("\tShows the previously generated test trees.\n");
        
        out.println("showsch <sch_name> [-u <unfold_order>] [-o <file_name>]");
	out.println("\tShows the specified schema.\n");
        
        out.println("showsch -tcl [-p <op_name>] [-u <unfold_order>] [-o <file_name>]");
	out.println("\tShows the previously generated test classes.\n");
/*        
        out.println("showsch -tca [-p <op_name>] [-u <unfold_order>] [-o <file_name>]");
	out.println("\tShows the previously generated test cases.\n");
        out.println("eval <term>");        
	out.println("\tEvaluates the specified term.\n");
 * 
 */
	out.println("version");
	out.println("\tDisplays the current version of Fastest.\n");
        
    	out.println("help");
    	out.println("\tDisplays this help summary.\n");
        
       	out.println("reset");
       	out.println("\tRemoves all specification and testing information.\n");
        
    	out.println("quit / exit");
    	out.println("\tExits the Fastest program.");
    	out.println();
	}

} 
