package client.presentation.commands;

import java.io.*;

import client.presentation.ClientTextUI;
import client.blogic.testing.abstraction.AbstractionLawRepository;
import compserver.abstraction.parser.*;
import compserver.abstraction.AbstractionLaw;
import org.antlr.runtime.*;


public class AddAbsLawCommand implements Command{

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
	public void run(ClientTextUI clientTextUI, String args){

		PrintWriter output = clientTextUI.getOutput();
		final String parts[] = args.split(" ",1);
		try{
		if (args == null || "".equals(args)){
	    	output.println("Invalid parameters.  Try 'help'.");
			return;
		}
		String path = parts[0];
		TRALLexer lex = new TRALLexer(new ANTLRFileStream(path));
		TokenRewriteStream tokens = new TokenRewriteStream(lex);
		TRALParser g = new TRALParser(tokens);
		AbstractionLaw absLaw = null;
		absLaw = g.abstractionLaw();
		AbstractionLawRepository absLawRepository = AbstractionLawRepository.getInstance();
		if(absLaw!=null && absLaw.getAbstractionRules()!=null){
			absLawRepository.addElement(absLaw);
		}
		else
			output.println("There was an Error while parsing the abstraction law.");
		}
 		catch (RecognitionException e) {
		output.println("There was an error while parsing the abstraction law.");
		e.printStackTrace(System.out);
		return;
		}
		catch(Exception e){
		output.println("Error: " + e);
		e.printStackTrace(output);
		}

	}
}

