package client.presentation.commands;

import java.io.*;
import java.util.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import compserver.abstraction.capture.execution.CompilationInfo;

/**

 */
public class SetCompilationCommand implements Command{ 
    
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
	
		final String parts[] = args.split(" ",5);
		if (parts.length < 4){
			output.println("Invalid parameters.  Try 'help'.");
			return;
		}

		// Sacamos las ""	
		String targetLanguaje = parts[0].substring(1,parts[0].length()-1);
		String platform = parts[1].substring(1,parts[1].length()-1);
		String compiler = parts[2].substring(1,parts[2].length()-1);
		String workingDirectory = parts[3].substring(1,parts[3].length()-1);
		//String compileOptions = parts[4].substring(1,parts[4].length()-1);
		String compileOptions = "";
		Controller controller = clientTextUI.getMyController();
		CompilationInfo compilationInfo = new CompilationInfo(targetLanguaje, platform, compiler, compileOptions, workingDirectory);
		controller.setCompilationInfo(compilationInfo);
	}
	catch(Exception e){
		e.printStackTrace(System.out);
	}
    }


}
 
