package client.presentation.commands;

import java.io.*;

import client.presentation.ClientTextUI;
import client.blogic.testing.refinement.RefLawRepository;
import client.blogic.testing.refinement.Utils;
import client.blogic.management.Controller;


public class LoadRefinementLawCommand implements Command{

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

			if (parts.length != 1){
			    output.println("Invalid parameters.  Try 'help'.");
			    return;
			}

			// We obtain the file from the path
			File refLawFile = new File(parts[0]);
			if (!refLawFile.exists()){
			    output.println("File " + parts[0] + " not found");
			}
			else{
				// We parse the refinement law
				String refLawName = Utils.parseFile(refLawFile);
			}
		}
		catch(Exception e){
			output.println("The TCRL file have syntax errors.");
			e.printStackTrace(output);
		}
	}
}

