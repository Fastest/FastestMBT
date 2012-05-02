package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;

/**
 * Provides utilities for interpret and encode values of a list in XML format
 */
public class ListXMLManager {    

    /**
     * Returns the string with the C Code for capture the output of a list
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){

		ImplNodeList listNode = (ImplNodeList) implNode;
		String linkType = listNode.getLinkType();
		String listName = listNode.getName();
		String linkNextName = listNode.getLinkNextName();
		List<ImplNodeField> fields = listNode.getFields();

		String captureCode = "";
		String xmlCode = "";
		xmlCode += "<list>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		if(linkType.equals("SLL")||linkType.equals("DLL")){
			// Tengo que crear una variable auxiliar para recorrer
			captureCode += "if("+varName+ " != NULL){\n";
			captureCode += "auxList"+varName+" = "+varName+";\n";
			captureCode += "while(auxList"+varName+"!=NULL){\n";
			xmlCode = "<listNode>\\n";
			captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";


			for(int i=0;i<fields.size();i++){
				ImplNodeField field = fields.get(i);
				String fieldName = field.getName();
				xmlCode = "<field>\\n";
				captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
				captureCode += "\t"+ getFieldCode("auxList"+varName+"->"+fieldName,field, fileDescriptor,returnID);
				xmlCode = "</field>\\n";
				captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
			}
			xmlCode = "</listNode>\\n";
			captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
			captureCode += "\t"+"auxList"+varName+" = auxList"+varName+"->"+linkNextName+";\n}\n}\n";
		}
		else if(linkType.equals("CLL")||linkType.equals("DCLL")){
			// Tengo que crear una variable auxiliar para recorrer
			// Estoy asumiendo que tiene un elemento - ARREGLAR
			//captureCode += "struct "+listName+" *auxList = "+varName+";\n";
			captureCode += "auxList"+varName+" = "+varName+";\n";
			captureCode += "do{";
			xmlCode = "<listNode>\\n";
			captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
			for(int i=0;i<fields.size();i++){
				ImplNodeField field = fields.get(i);
				String fieldName = field.getName();
				xmlCode = "<field>\\n";
				captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
				captureCode += "\t"+ getFieldCode("auxList"+varName+"->"+fieldName,field, fileDescriptor,returnID);
				xmlCode = "</field>\\n";
				captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
			}
			xmlCode = "</listNode>\\n";
			captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
			captureCode += "\t"+"auxList"+varName+" = auxList"+varName+"->"+linkNextName+";\n}";
			captureCode += "while(auxList"+varName+"!="+varName+");";
		}
		xmlCode = "</list>\\n";
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
		captureCode += "\t"+WritersManager.getCaptureCode(varName,fieldType, fileDescriptor,returnID)+"\n";
		xmlCode = "</elementValue>";
		captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";

		return captureCode;
	}
	public static String getDeclarations(ImplNode implNode)
	{
		ImplNodeList listNode = (ImplNodeList) implNode;
		String varName = listNode.getImplID();
		String listName = listNode.getName();
		String declaration = "struct "+listName+" *auxList"+varName+";\n";
		return declaration;
	}
}