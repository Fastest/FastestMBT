package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;

/**
 * Provides utilities for interpret and encode values of a structure in XML format
 */
public class StructureXMLManager {

    /**
     * Returns the string with the C Code for capture the output of a structure
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		ImplNodeStructure structureNode = (ImplNodeStructure) implNode;
		List<ImplNodeField> fields = structureNode.getFields();
		String captureCode = "";
		String xmlCode = "";
		xmlCode += "<structure>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		for(int i=0;i<fields.size();i++){
			ImplNodeField field = fields.get(i);
			String fieldName = field.getName();
			xmlCode = "<field>\\n";
			captureCode += returnID+ " = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
			captureCode += getFieldCode(varName+"."+fieldName,field, fileDescriptor,returnID);
			xmlCode = "</field>\\n";
			captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		}
		xmlCode = "</structure>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		return captureCode;
	}
	private static String getFieldCode(String varName, ImplNodeField fieldNode, String fileDescriptor, String returnID){
		String fieldName = fieldNode.getName();
		ImplNode fieldType = fieldNode.getType();
		String captureCode = "";
		String xmlCode = "";
		xmlCode += "<elementID>\\n";
		xmlCode += fieldName+"\\n";
		xmlCode += "</elementID>\\n";
		xmlCode += "<elementValue>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		captureCode += WritersManager.getCaptureCode(varName,fieldType, fileDescriptor,returnID)+"\n";
		xmlCode = "</elementValue>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		return captureCode;
	}
}