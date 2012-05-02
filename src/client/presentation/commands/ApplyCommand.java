package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.blogic.management.Controller;
import client.presentation.ClientTextUI;
import compserver.prunning.PruneAnalizer;
import compserver.prunning.TreePruner;
import compserver.prunning.PruneUtils;
import common.z.TClass;
import net.sourceforge.czt.session.SectionManager;
import compserver.prunning.typechecking.TypecheckingUtils;
import net.sourceforge.czt.z.ast.AxPara;


/**
 * An instance of this class allows the application of a theorem for prune  
 * empty classes. The command needs the name of the theorem, the name of the 
 * test class that will be pruned and the arguments for the theorem.
 */
public class ApplyCommand implements Command{
    
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
		final String parts[] = args.split("[ ]+",3);
		if(parts.length<3){
			output.println("Invalid parameters.  Try 'help'.");
			return;
		}

		String theoremName = parts[0];
		String tClassName = parts[1];
		String parameters = parts[2];
		List<String> params = new ArrayList<String>();
		List<String> outParams = new ArrayList<String>();
		//String lastParts[] = parameters.split(" ");
		//for(int i=0;i<lastParts.length;i++)
		//	params.add(lastParts[i].substring(1,lastParts[i].length()-1));
		int indexInf = 1;
		int indexSup;
		// We extract the parameters
		while(indexInf > -1){
			indexInf = parameters.indexOf("\"");
			parameters = parameters.substring(indexInf+1);
			indexSup = parameters.indexOf("\"");
			params.add(parameters.substring(0, indexSup));
			outParams.add(parameters.substring(0, indexSup));
			parameters = parameters.substring(indexSup+1);
			indexInf = parameters.indexOf("\"");
		}
		// We replace some escape characters
		for(int i=0;i<params.size();i++){
			String auxParam = params.get(i);
			//System.out.println("El parametro inicial: "+auxParam);
			auxParam = auxParam.replaceAll("\\\\","\\\\\\\\");
			auxParam = auxParam.replaceAll("\\{","\\\\\\{");
			auxParam = auxParam.replaceAll("\\}","\\\\\\}");
			//auxParam = auxParam.replaceAll("\\?","\\\\?");
			//auxParam = auxParam.replaceAll("\\+","\\\\+");
			//System.out.println("El parametro final: "+auxParam);
			params.set(i, auxParam);
		}
		Controller controller = clientTextUI.getMyController();
		TClass tClass = PruneUtils.searchTClass(tClassName, controller);
		if(tClass==null){
			output.println(tClassName+" is not the name of any test class of the generated test trees");
		}
		SectionManager manager = controller.getTypeCheckerManager();
		AxPara axPara = tClass.getMyAxPara();
		axPara = TypecheckingUtils.deleteUnnecessaryParenthesis(axPara, manager);
		PruneAnalizer pruneAnalizer = new PruneAnalizer();
		Calendar cal = Calendar.getInstance();
		long inicio = cal.getTimeInMillis();
		boolean result = pruneAnalizer.analize(theoremName, tClass, params);
		cal = Calendar.getInstance();
		long fin = cal.getTimeInMillis();
		if(result){
			boolean finalResp = (new TreePruner(controller)).pruneFrom(tClassName);
			if(finalResp){
			output.print("Test Objetive "+tClassName+" pruned by \n"+ theoremName+"(");
			for(int i=0;i<outParams.size()-1;i++)
				output.print(outParams.get(i)+", ");
			output.println(outParams.get(outParams.size()-1)+")");
			System.out.println("Elapsed time: "+(fin-inicio));
			}
			else
				output.println("Fastest cannot prune "+tClassName);	
		}
		else
			output.println(tClassName+" cannot be pruned");	
	}
	catch(Exception e){
	e.printStackTrace();
	}
		
	}
	}