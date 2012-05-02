package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import client.blogic.testing.refinement.ConcreteTCase;
import client.blogic.testing.refinement.Utils;

/**
 * An instance of this class allow the presentation of the concrete test cases which are
 * stored in Fastest. It is possible to print a particular concrete test case, all the
 * concrete test cases related to an operation or all the concrete test cases generated
 * in the refinement stage.
 * Printing can be done on screen or on a file or a set of files. In the case of the files,
 * the user must specify a folder in which Fastest will create the files. 
 */

public class ShowConcreteTCaseCommand implements Command{
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args){
        PrintWriter output = clientTextUI.getOutput();

	if (args == null || "".equals(args)){
		output.println("Invalid parameters.  Try 'help'.");
		return;
	}

	final String argv[] = args.split(" ");
	int argc = argv.length;
	String ctcFilter = argv[0];
	String folderPath = "";
	if(argc == 2 || (argc == 3 && !argv[1].equals("-o"))){
		output.println("Invalid parameters.  Try 'help'.");
		return;
	}
	if(argc>2)
		folderPath = argv[2];


	List<ConcreteTCase> ctCases = new ArrayList<ConcreteTCase>();

        Controller controller = clientTextUI.getMyController();
	String ctcName = args;
        Map<String, List<ConcreteTCase>> opCtcMap = controller.getOpTCaseRefinedMap();
        Set<Map.Entry<String, List<ConcreteTCase>>> set = opCtcMap.entrySet();
        Iterator<Map.Entry<String, List<ConcreteTCase>>> iterator = set.iterator();
	if(ctcFilter.equals("-all")){
		while(iterator.hasNext()){
			Map.Entry<String, List<ConcreteTCase>> mapEntry = iterator.next();
			List<ConcreteTCase> auxCTCases = mapEntry.getValue();

			ctCases.addAll(auxCTCases);
		}
	}
	else{
		boolean founded = false;
		// We obtain all the cases related to an operation if ctcfilter is the
		// the name of a refined operation, or the concrete test case whose name
		// matches with ctcfilter, or return with an error message otherwise
		while(iterator.hasNext() && !founded){
			Map.Entry<String, List<ConcreteTCase>> mapEntry = iterator.next();
			List<ConcreteTCase> auxCTCases = mapEntry.getValue();
			String opName = mapEntry.getKey();
			if(opName.equals(ctcFilter)){
				ctCases.addAll(auxCTCases);
				founded = true;
			}
			else{
				for(int i=0;i<auxCTCases.size() && !founded;i++){
					ConcreteTCase auxCTCase = auxCTCases.get(i);
					String auxCTCName = auxCTCase.getConcreteTCaseName();
					if(auxCTCName.equals(ctcFilter)){
						ctCases.add(auxCTCase);
						founded = true;
					}
				}
			}
		}
		if(founded==false){
			output.println(ctcFilter+" is not the name of a concrete test case or the name of a refined operation");
			return;
		}
	}
	if(ctCases.size()==0){
		output.println(" There aren't refined cases");
		return;
	}
	// Now we decide if we print the results in the screen or in files
	if(folderPath.equals("")){
		// We must print in the screen
		for(int i=0;i<ctCases.size();i++){
			ConcreteTCase auxCTCase = ctCases.get(i);
			String auxCTCName = auxCTCase.getConcreteTCaseName();
			System.out.println(Utils.printCTC(auxCTCName,auxCTCase)+"\n\n");
		}
	}
	else{
		try{
		// We must print in files
		for(int i=0;i<ctCases.size();i++){
			ConcreteTCase auxCTCase = ctCases.get(i);
			String auxCTCName = auxCTCase.getConcreteTCaseName();
			String targetLanguaje = auxCTCase.getLanguaje();
			String fileExtension = "";
			if(targetLanguaje.equals("Java"))
				fileExtension = ".java";
			else if (targetLanguaje.equals("C"))
				fileExtension = ".c";
			File auxFile = new File(folderPath + File.separator + auxCTCName + fileExtension);
			BufferedWriter auxBW = new BufferedWriter(new FileWriter(auxFile));
			auxBW.write(Utils.printCTC(auxCTCName,auxCTCase));
			auxBW.close();
		}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
    }
}
