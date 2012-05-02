package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;

/**
 * Provides utilities for interpret and encode values of an enumeration in XML format
 */
public class EnumerationXMLManager {

    /**
     * Returns the string with the C Code for capture the output of an enumeration
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		ImplNodeEnumeration enumNode = (ImplNodeEnumeration) implNode;
		ImplNode enumType = enumNode.getEnumType();
		Map<String,String> elements = enumNode.getElements();
		String captureCode = "";

		if(enumType instanceof ImplNodePLType){
			ImplNodePLType nodePLType = (ImplNodePLType) enumType;
			String plType = nodePLType.getType();

			Set<Map.Entry<String,String>> set = elements.entrySet();
			Iterator<Map.Entry<String,String>> iterator = set.iterator();
			while(iterator.hasNext()){
				Map.Entry<String,String> mapEntry = iterator.next();
				String implName = mapEntry.getKey();
				String specName = mapEntry.getValue();
				if(plType.equals("string")){
					captureCode += "if(strcmp("+varName+",\""+implName+"\") == 0){\n";
					captureCode += PLTypeXMLManager.getCaptureCode("\""+specName+"\"",nodePLType,fileDescriptor,returnID);
					captureCode += "}\n";
				}
				else{
					captureCode += "if("+varName+" == "+implName+"){\n";
					captureCode += PLTypeXMLManager.getCaptureCode(implName,nodePLType, fileDescriptor,returnID);
					captureCode += "}\n";
				}
				
			} 
		}
		else{
			System.out.println("Fastest only accept enumerations of primitive types");
			if(enumType==null)
				System.out.println("Nulo");
			else
				System.out.println("No nulo");
			return null;
		}
		return captureCode;
	}
}
