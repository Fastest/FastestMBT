package compserver.abstraction.capture.xmlmanagers;

import java.net.URL;
import java.util.*;
import compserver.abstraction.AbstractionLaw;
import compserver.abstraction.AbstractionUtils;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;
import client.blogic.testing.refinement.ConcreteTCase;

public class ScriptTestWriterC implements ScriptTestWriter{

	public ScriptTestWriterC(){
	}
    /**
     * Adds the necessary code to captute the output of a UUT written in C languaje
     * @param script the script generated in the refinement stage
     * @param law the abstraction involved in this stage
     */
	public ConcreteTCase addCaptureCode(ConcreteTCase script, AbstractionLaw law){
		// We create the return ID needed for the return value in the file writing
		returnID = "written";
		// We create the file ID
		fileDescriptor = "xmlFile";	
		// We obtain the name of the script to give an unique name
		// to the XML file
		String scriptName = script.getConcreteTCaseName();
		// We obtain the variables in the implementation that are under monitoring
		List<ImplNode> monitoredVars = AbstractionUtils.getMonitoredVariables(law);
		// We store all the code for capture the output in a list
		List<String> codes = new ArrayList<String>();
		// Also we store the declarations needed for capture the code
		List<String> declarations = new ArrayList<String>();
		// We add the declaration of the returnID
		codes.add("int written;");
		// We set the first tag of instance of XML Schema
		codes.add(setFirstAssigmentsTag());
		// We analyze if we must redirect the output
		boolean redirectOutput = false;
		for(int i=0;i<monitoredVars.size();i++){
			ImplNode implNode = monitoredVars.get(i);
			if(implNode instanceof ImplNodeScreen)
				redirectOutput = true;
			String varID = implNode.getImplID();
			// We set the tag assign
			codes.add(setAssignTag(varID));
			codes.add(setFirstAssignValueTag());
			codes.add(WritersManager.getCaptureCode(varID, implNode, fileDescriptor, returnID));
			codes.add(setLastAssignValueTag());
			codes.add(setLastAssignTag());
			String declaration = WritersManager.getDeclarations(implNode);
			if(!declaration.equals(""))
				declarations.add(declaration);
		}
		codes.add(setLastAssigmentsTag());
		try{
		URL url = ScriptTestWriterC.class.getResource("ScriptTestWriterC.class");
		String urlStr = url.toString();
		String currentDir = urlStr.substring(9,urlStr.indexOf("fastest.jar"));
		declarations.add("FILE *xmlFile;\n");
		// Esto es una pequeña licencia. En realidad en el prologo van solo
		// las declaraciones. ARREGLAR
		if(redirectOutput){
		declarations.add("close(1);\n");
		declarations.add("FILE *outputFile;\n");
		declarations.add("outputFile = fopen(\""+currentDir+"screenFile"+scriptName+"\",\"w\");\n");
		}
		declarations.add("xmlFile = fopen(\""+currentDir+"xmlCapture"+scriptName+".xml\", \"w\");\n");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		// We add the declarations to the initDecls
		String initDecls = "";
		for(int i=0;i<declarations.size();i++){
			String declaration = declarations.get(i);
			initDecls += declaration;
		}
		/*String prologue = script.getPreamble()+"\n";
		for(int i=0;i<declarations.size();i++){
			String declaration = declarations.get(i);
			prologue += declaration;
		}
		script.setPreamble(prologue);*/
		script.setInitDeclarations(initDecls);
		// We add the capture code to the script
		String epilogue = script.getEpilogue();
		String captureCode = "";
		for(int i=0;i<codes.size();i++){
			String code = codes.get(i);
			captureCode += code+"\n";
		}	
		epilogue = captureCode + epilogue;
		script.setEpilogue(epilogue);
		return script;
	}
	private String setFirstAssigmentsTag(){
		String tag = returnID+" = fprintf("+fileDescriptor+",\"<assignments\\nxmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\"\\nxsi:noNamespaceSchemaLocation=\\\"types.xsd\\\">\\n\\n\");\n";
		return tag;
	}
	private String setLastAssigmentsTag(){
		String tag = returnID+" = fprintf("+fileDescriptor+",\"</assignments>\\n\");\n";
		return tag;
	}
	private String setAssignTag(String varName){
		String text = "<assign>\\n<varID>\\n"+varName+"\\n</varID>\\n";
		String tag = returnID+ " = fprintf("+fileDescriptor+",\""+text+"\");\n";
		return tag;
	}
	private String setFirstAssignValueTag(){
		String tag = returnID+" = fprintf("+fileDescriptor+",\"<varValue>\\n\");\n";
		return tag;
	}
	private String setLastAssignValueTag(){
		String tag = returnID+" = fprintf("+fileDescriptor+",\"</varValue>\\n\");\n";
		return tag;
	}
	private String setLastAssignTag(){
		String tag = returnID+" = fprintf("+fileDescriptor+",\"</assign>\\n\");\n";
		return tag;
	}
	private String returnID;
	private String fileDescriptor;
}