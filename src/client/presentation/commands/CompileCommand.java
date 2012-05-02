package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import client.blogic.testing.execution.Executer;
import client.blogic.testing.refinement.ConcreteTCase;
import compserver.abstraction.capture.execution.CompilationInfo;

/**

 */
public class CompileCommand implements Command{ 
    
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */    
    @Override
    public void run(ClientTextUI clientTextUI, String args){
		
	PrintWriter output = clientTextUI.getOutput();
	Controller controller = clientTextUI.getMyController();
	try{

		if (args == null || "".equals(args)){
    		output.println("Invalid parameters.  Try 'help'.");
			return;
		}
		// First we analyze which type of compilation must be done
		final String parts[] = args.split("[ ]+",2);
		if (parts.length < 2){
			output.println("Invalid parameters.  Try 'help'.");
			return;
		}
		CompilationInfo cInfo = controller.getCompilationInfo();
		if(cInfo==null){
			output.println("The information of compilation has not been loaded.");
			return;
		}
		String mode = "single";
		String compileCode="";
		String ctcName="";
		String opName="";
		if(parts[0].equals("-all")){
			mode = "complete";
			compileCode = parts[1].substring(1,parts[1].length()-1);
		}
		else if(!parts[0].equals("-op")){
			ctcName = parts[0];
			compileCode = parts[1].substring(1,parts[1].length()-1);
		}
		else if(parts[0].equals("-op")){
			mode = "operation";
			String parts2[] = parts[1].split("[ ]+",2);
			opName = parts2[0];
			compileCode = parts2[1].substring(1,parts2[1].length()-1);
		}
	
		// Now we proceed
		Map<String, ConcreteTCase> atcCtcMap = controller.getAbsTCaseConcrTCaseMap();
		Set<Map.Entry<String, ConcreteTCase>> set = atcCtcMap.entrySet();
		Iterator<Map.Entry<String, ConcreteTCase>> iterator = set.iterator();
		if(mode.equals("single")){
		ConcreteTCase ctCase = null;
		while(iterator.hasNext() && ctCase == null){
			Map.Entry<String, ConcreteTCase> mapEntry = iterator.next();
			ConcreteTCase auxCTCase = mapEntry.getValue();
			String ctcAuxName = auxCTCase.getConcreteTCaseName();
			if(ctcAuxName.equals(ctcName)){
				ctCase = auxCTCase;
			}
		}
		if(ctCase==null){
			System.out.println(ctcName+" is not the name of a refined case");
			return;
		}
		// First we replace the name of the concrete case in each place that
		// the user indicates
		compileCode = compileCode.replace("(.*)",ctcName);

		String ctcCaseStr = ctCase.toString();
		String workingPath = cInfo.getWorkingDirectory();
		if(!workingPath.endsWith(File.separator))
			workingPath += File.separator; 

		// Creo los archivos de codigo fuente en el mismo path que la UUT
		// Esto seguramente será modificado
                PrintWriter printer = new PrintWriter(new FileWriter(workingPath + ctcName + getExtension(cInfo)));
                printer.println(ctcCaseStr);
                printer.flush();


		Executer.execute(compileCode, workingPath);
		}
		else if(mode.equals("complete")){
		while(iterator.hasNext()){
			Map.Entry<String, ConcreteTCase> mapEntry = iterator.next();
			ConcreteTCase auxCTCase = mapEntry.getValue();
			String auxCTCaseName = auxCTCase.getConcreteTCaseName();

			String workingPath = cInfo.getWorkingDirectory();
			if(!workingPath.endsWith(File.separator))
				workingPath += File.separator; 
			String ctcCaseStr = auxCTCase.toString();
			PrintWriter printer = new PrintWriter(new FileWriter(workingPath + auxCTCaseName + getExtension(cInfo)));
			printer.println(ctcCaseStr);
			printer.flush();

			// First we replace the name of the concrete case in each place that
			// the user indicates
			String auxCompileCode = compileCode.replace("(.*)",auxCTCaseName);
			//compileCode = compileCode.replace("(.*)",auxCTCaseName);

			Executer.execute(auxCompileCode, workingPath);
		}
		}
		else if(mode.equals("operation")){
			Map<String, List<ConcreteTCase>> opList = controller.getOpTCaseRefinedMap();
			Set<Map.Entry<String, List<ConcreteTCase>>> setOp = opList.entrySet();
			Iterator<Map.Entry<String, List<ConcreteTCase>>> iteratorOp = setOp.iterator();
			boolean found = false;
			while(iteratorOp.hasNext() && !found){
				Map.Entry<String, List<ConcreteTCase>> mapEntry = iteratorOp.next();
				List<ConcreteTCase> auxCTCases = mapEntry.getValue();
				String opNameSpec = mapEntry.getKey();
				if(opNameSpec.equals(opName)){
				found = true;
				for(int i=0;i<auxCTCases.size();i++){
					ConcreteTCase auxCTCase = auxCTCases.get(i);
					String auxCTCName = auxCTCase.getConcreteTCaseName();

					String workingPath = cInfo.getWorkingDirectory();
					if(!workingPath.endsWith(File.separator))
						workingPath += File.separator; 
					String ctcCaseStr = auxCTCase.toString();
					PrintWriter printer = new PrintWriter(new FileWriter(workingPath + auxCTCName + getExtension(cInfo)));
					printer.println(ctcCaseStr);
					printer.flush();

			// First we replace the name of the concrete case in each place that
			// the user indicates
			String auxCompileCode = compileCode.replace("(.*)",auxCTCName);
			//compileCode = compileCode.replace("(.*)",auxCTCName);

					Executer.execute(auxCompileCode, workingPath);
				}
				}
			}
			if(!found){
				System.out.println(opName+" is not the name of a refined operation");
				return;
			}
		}
	}
	catch(Exception e){
		e.printStackTrace(System.out);
	}
    }
    private String getExtension(CompilationInfo compilationInfo){
	String targetLanguaje = compilationInfo.getTargetLanguaje();
	if(targetLanguaje.equals("C"))
		return ".c";
	else if(targetLanguaje.equals("Java"))
		return ".java";
	else return null;
    }


}
 
