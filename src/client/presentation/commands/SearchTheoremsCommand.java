package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import compserver.prunning.PruneUtils;
import common.z.TClass;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.z.ast.AxPara;
import common.z.SpecUtils;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.z.ast.Pred;
import compserver.prunning.typechecking.TypecheckingUtils;
import compserver.prunning.Theorem;
import compserver.prunning.TheoremsChecker;
import compserver.prunning.Variable;

/**
 * An instance of this class allows the searching of theorems whose arguments
 * match with a subset of expressions of a TClass. The command needs the name of the 
 * test class.
 */
public class SearchTheoremsCommand implements Command{
    
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
		Controller controller = clientTextUI.getMyController();
		SectionManager manager = controller.getTypeCheckerManager();
		String tClassName = args.trim();
		TClass tClass = PruneUtils.searchTClass(tClassName, controller);
		if(tClass==null){
			output.println("'" + tClassName + "' is not the name of any test class of the generated test trees."); 
			return;
		}
                Calendar cal = Calendar.getInstance();
		long inicio = cal.getTimeInMillis();
		AxPara axParaAux = tClass.getMyAxPara();
		AxPara axPara = TypecheckingUtils.deleteUnnecessaryParenthesis(axParaAux, manager);
		Pred tClassPred = null;
		try{
		// We force the typechecking of tClass to guarantee TypeAnns
		List<? extends ErrorAnn> errors = 
				TypeCheckUtils.typecheck(axPara, manager, false, "Specification");
		if(errors.size() >0)
			System.out.println("ErroreS: "+errors.toString());
		tClassPred = SpecUtils.getAxParaPred(axPara);
		SpecUtils.setAxParaPred(tClass, tClassPred);
		}
		catch(Exception e){
			System.out.println("There was an error in the typechecking stage!");
			System.out.println(e);
		}


		TheoremsChecker theoremsChecker = new TheoremsChecker(tClass);
		String theoremName = "";
		List<String> params = new ArrayList<String>();
		boolean result = false;

		System.out.println("Searching theorems for "+tClassName+"...");
		Map<String, List<Map<String,String>>> matches = theoremsChecker.findParameters();
		while(matches.size()>0){
			result = true;
			Set<Map.Entry<String, List<Map<String,String>>>> set = matches.entrySet();
			Iterator<Map.Entry<String, List<Map<String,String>>>> iterator = set.iterator();
			while(iterator.hasNext()){
				
				Map.Entry<String, List<Map<String,String>>> mapEntry = iterator.next();
				theoremName = mapEntry.getKey();
				List<Map<String,String>> listMatches = mapEntry.getValue();
				for(int i=0;i<listMatches.size();i++){
					params.clear();
					Map<String, String> mapFR = listMatches.get(i);

				//We extract the real parameters in the correct order
				Theorem theTheorem = PruneUtils.getTheorem(theoremName);
				List<Variable> formalParameters = theTheorem.getFormalParamList();
				for(int j=0;j<formalParameters.size();j++){
					Variable formalVar = formalParameters.get(j);
					String formalName = formalVar.getName();
					String realName = mapFR.get(formalName);
					params.add(realName);
				}
				System.out.print(theoremName+"(");
				for(int k=0;k<params.size()-1;k++)
					System.out.print(params.get(k)+",");
				System.out.println(params.get(params.size()-1)+")");
				}

			}
			matches = theoremsChecker.findParameters();
		}
                cal = Calendar.getInstance();
		long fin = cal.getTimeInMillis();
		if(!result)
			System.out.println("No elimination theorems found for "+tClassName);
		System.out.println("Elapsed time : "+(fin-inicio));

		}
		catch(Exception e){
		e.printStackTrace();
		}
		
	}
	}