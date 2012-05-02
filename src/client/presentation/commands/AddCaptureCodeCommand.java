package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import client.blogic.testing.refinement.ConcreteTCase;
import client.blogic.management.Controller;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import compserver.abstraction.AbstractionLaw;
import client.blogic.testing.abstraction.AbstractionLawRepository;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.events.TCaseAddCaptureCodeRequested;
import common.fastest.FastestUtils;

/**

 */
public class AddCaptureCodeCommand implements Command{ 
    
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */    
    @Override
    public void run(ClientTextUI clientTextUI, String args){
		
	PrintWriter output = clientTextUI.getOutput();
	try{
		if (args == null || "".equals(args)){
    		output.println("Invalid parameters.  Try 'help'.");
			return;
		}
	
		final String parts[] = args.split(" ");
		if (parts.length < 9){
			output.println("Invalid parameters.  Try 'help'.");
			return;
		}

		boolean someEventAnnounced = false;
	
		// addcapturecode to name to test UUT implemented in languaje with absLawName
		String name = parts[1];
		String pathUUT = parts[4];
		String targetLanguaje = parts[7];
		String absLawName = parts[9];

		Controller controller = clientTextUI.getMyController();
			
		boolean isOp = false;
		ConcreteTCase concreteTCase = null;

		//We check if the name of the operation is contained in the repository of loaded operations
		isOp = FastestUtils.isLoadedOperation(controller,name);

		//If not, we check if name is the name of a concrete test case and obtain it
		if(!isOp)
			concreteTCase = FastestUtils.getConcreteTCase(controller, name);

		
		if(!isOp && concreteTCase == null){
			// We finish returning an error message
			output.println("'" + name + "' is not the name of a loaded operation or a concrete test case."); 
			return;
		}
		
		// We check if the name of the abstraction law is contained in the 
		// repository of loaded laws
		AbstractionLawRepository absLawRepository = AbstractionLawRepository.getInstance();
		AbstractIterator<AbstractionLaw> lawsIt = absLawRepository.createIterator();
		boolean lawFounded = false;
		AbstractionLaw law = null;
		while(lawsIt.hasNext() && !lawFounded){
			law = lawsIt.next();
			String lawName = law.getLawName();
			if(lawName.equals(absLawName))
				lawFounded = true;
		}
		if(law==null){
			System.out.println("'"+absLawName+"' is not the name of a loaded abstraction law");
			return;
		}

		Map<String, List<ConcreteTCase>> mapOpsRef = controller.getOpTCaseRefinedMap();
		List<ConcreteTCase> refineds = new ArrayList<ConcreteTCase>();
		if(isOp){
		// We check if some of the leaves corresponding to this operationes were
		// refined
			refineds = mapOpsRef.get(name);
			if(refineds == null){
				output.println("'" + name + "' was not refined.");
				return;
			}
		}
		else{
		// We add the specific concrete case to the list refineds
			refineds.add(concreteTCase);
		}

		// We obtain the refined cases that was refined for this UUT 
		List<ConcreteTCase> toAbstract = new ArrayList<ConcreteTCase>();
		for(int i=0;i<refineds.size();i++){
			ConcreteTCase refinedTCase = refineds.get(i);
			String pathUUTRefined = refinedTCase.getPathUUT();
			if(pathUUTRefined.equals(pathUUT))
				toAbstract.add(refinedTCase);
		}
		// We announce the events for abstract each concrete case
		for(int i=0;i<toAbstract.size();i++){
			// Analizar problemas de sincronizacion en la UI
			someEventAnnounced = true;
			ConcreteTCase ctCase = toAbstract.get(i);
        		EventAdmin eventAdmin = EventAdmin.getInstance();
			TCaseAddCaptureCodeRequested event = new TCaseAddCaptureCodeRequested(ctCase,law,targetLanguaje);
			eventAdmin.announceEvent(event);
		}
		if(someEventAnnounced)
			synchronized(clientTextUI){
			clientTextUI.wait();
		}
	}
	catch(Exception e){
		e.printStackTrace(System.out);
	}
    }


}
 
