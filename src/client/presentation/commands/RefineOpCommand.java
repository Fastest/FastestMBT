package client.presentation.commands;

import java.io.*;
import java.util.*;


import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;


import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import client.blogic.testing.ttree.tactics.Tactic;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import common.z.SpecUtils;
import common.z.OpScheme;
import common.z.OpSchemeImpl;
import client.blogic.management.ii.EventAdmin;
import common.z.AbstractTCase;
import common.fastest.FastestUtils;
import client.blogic.testing.refinement.RefLawRepository;
import client.blogic.testing.ttree.visitors.TCaseNodeFinder;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.management.ii.events.RefineAbsTCasesRequested;

/**
* An instance of this class is used to require the refinement of an abstract test case
* or the refinement of all the leaves of an operation. 
*/

public class RefineOpCommand implements Command{ 
    
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
		
		String name = parts[0];
		String pathUUT = parts[3];
		String targetLanguaje = parts[6];
		String refLawName = parts[8];

		Controller controller = clientTextUI.getMyController();
		EventAdmin eventAdmin = EventAdmin.getInstance();

		boolean isOp = false;
		AbstractTCase abstractTCase = null;

		//We check if the name of the operation is contained in the repository of loaded operations
		isOp = FastestUtils.isLoadedOperation(controller,name);

		//If not, we check if name is the name of an abstract test case
		if(!isOp)
			abstractTCase = FastestUtils.getAbstractTCase(controller, name);

		
		if(!isOp && abstractTCase == null){
			// We finish returning an error message
			output.println("'" + name + "' is not the name of a loaded operation or a abstract Test case."); 
			return;
		}

		// We check if the name of the abstraction law is contained in the 
		// repository of loaded laws
		RefLawRepository refLawRepository = controller.getRefLawRepository();
		if (refLawRepository.getRefLaw(refLawName)== null){
			output.println("'"+refLawName+"' is not the name of a loaded refinement law");
			return;
		}

		// We obtain the abstract test cases to refine. If we try to refine
		// an operation then we send to refine all the leafs of this operation.
		// If we try to refine an abstract test case then we send a repository 
		// with only this element
		Collection<AbstractTCase> absTCasesColl;
		if(isOp){
		// We obtain the testing tree related for this opName	
		Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
		TClassNode opTTreeRoot = opTTreeMap.get(name);
	
		// We obtain all the leafs for this testing tree
		Map<String, AbstractTCase> tcaMap = opTTreeRoot.acceptVisitor(new TCaseNodeFinder());
		absTCasesColl =  tcaMap.values();
		}
		else{
                        absTCasesColl = new ArrayList<AbstractTCase>();
                        absTCasesColl.add(abstractTCase);
		}

		eventAdmin.announceEvent(new RefineAbsTCasesRequested(name, absTCasesColl, pathUUT,targetLanguaje,refLawName));
	
		synchronized(clientTextUI){
		clientTextUI.wait();
		}

        }
        catch(Exception e){
                e.printStackTrace();
        }
    }


}
 
