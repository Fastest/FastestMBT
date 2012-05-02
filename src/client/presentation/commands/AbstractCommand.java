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
import client.blogic.management.ii.events.TCaseAbstractRequested;
import common.fastest.FastestUtils;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import common.z.SpecUtils;

/**

 */
public class AbstractCommand implements Command{ 
    
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
		String name = args.trim();

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
			else{
				// Exijo que todas tengan codigo de refinamiento.
				// Podria modificarlo
				boolean withoutCC = false;
				for(int i=0;i<refineds.size() && !withoutCC;i++){
					ConcreteTCase auxCase = refineds.get(i);
					if(auxCase.getAbsLawName()==null)
						withoutCC = true;
				}
				if(withoutCC){
				output.println("Some of the leaves of "+name +" were not modified to capure the output of the UUT");
				output.println("You must use the addcapturecode command before attempting to run this case.");
				return;
				}
			}
		}
		else{
			if(concreteTCase.getAbsLawName()==null){
			output.println(name +" was not modified to capure the output of the UUT");
			output.println("You must use the addcapturecode command before attempting to run this case.");
			return;
			}
			// We add the specific concrete case to the list refineds
			refineds.add(concreteTCase);
		}

		AbstractionLawRepository lawsRepository = AbstractionLawRepository.getInstance();

		boolean someEventAnnounced = false;
		// We announce the events for abstract each concrete case
		for(int i=0;i<refineds.size();i++){
			// Analizar problemas de sincronizacion en la UI
			someEventAnnounced = true;
			ConcreteTCase ctCase = refineds.get(i);

			// We obtain the AxPara of the operation associated with this concrete case
			String opName = ctCase.getOpName();
			Spec unfoldedSpec = controller.getUnfoldedSpec();
			AxPara originalAxPara = null;
			for (Sect sect : unfoldedSpec.getSect()) {
				if (sect instanceof ZSect) {
					ParaList paraList = ((ZSect) sect).getParaList();
					if (paraList instanceof ZParaList){
						originalAxPara = SpecUtils.axParaSearch(opName, (ZParaList)paraList);
					}
				}
			}
		
			// We look for the abstraction law in the repository that was used 
			// to start the abstraction process
			String absLawName = ctCase.getAbsLawName();
			AbstractIterator<AbstractionLaw> lawsIt = lawsRepository.createIterator();
			AbstractionLaw law = null;
			while(lawsIt.hasNext() && law==null)
			{
				AbstractionLaw auxLaw = lawsIt.next();
				if(auxLaw.getLawName().equals(absLawName))
					law = auxLaw;
			}

        		EventAdmin eventAdmin = EventAdmin.getInstance();
			TCaseAbstractRequested event = new TCaseAbstractRequested(ctCase,law,originalAxPara);
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
 
