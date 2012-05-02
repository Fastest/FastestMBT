package compserver.abstraction.capture.uutmanipulers;

import java.io.*;
import org.antlr.runtime.*;
import compserver.abstraction.types.spectypes.*;
import compserver.abstraction.types.impltypes.*;
import java.util.ArrayList;
import java.util.List;

public class VisibilityChangerC implements VisibilityChanger {

	public void changeVisibility(String uutPath, List<ImplNode> monitoredVars){
		try {
		// First we create a copy of the file
		String nameWithoutExtension = uutPath.substring(0,uutPath.indexOf(".c"));
		String newName = nameWithoutExtension+"Original.c";
		File originalFile = new File(uutPath);
		File modifiedFile = new File(newName);
		//boolean wasModified = originalFile.renameTo(modifiedFile);
		FileInputStream in = new FileInputStream(originalFile);
		FileOutputStream out = new FileOutputStream(modifiedFile);
 		int c;
		while( (c = in.read() ) != -1)
			out.write(c);
 		in.close();
		out.close();

		// Now we proceed to modify the visibility in the original file
		CLexer lex = new CLexer(new ANTLRFileStream(uutPath));
		TokenRewriteStream tokens = new TokenRewriteStream(lex);
		CParser parser = new CParser(tokens);
		parser.makePublic(monitoredVars);
		String codeModified = tokens.toString();
		FileWriter fichero = new FileWriter(uutPath);
		PrintWriter pw = new PrintWriter(fichero);
		pw.println(codeModified);
		fichero.close();
		// El problema es que va a quedar el archivo salvado
		// Al final del proceso habria que restaurar al estado original!!!

		} catch (RecognitionException e) {
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
    }
}