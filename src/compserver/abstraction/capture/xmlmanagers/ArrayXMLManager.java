package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;

/**
 * Provides utilities for interpret and encode values of an array in XML format
 */
public class ArrayXMLManager {

    /**
     * Returns the string with the C Code for capture the output of an array
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		ImplNodeArray arrayNode = (ImplNodeArray) implNode;
		int arraySize = arrayNode.getSize();
		ImplNode arrayType = arrayNode.getType();
		String lastPosPointerID = arrayNode.getLastPosPointerID();
		// We analyze if the varName corresponds to an array entry and, in that case,
		// we modify the lastPosPointerID
		if(varName.endsWith("]")){
			lastPosPointerID += varName.substring(varName.indexOf("["));
		}

		String realSize;
		if(lastPosPointerID!=null)
			realSize = lastPosPointerID;
		else
			realSize = String.valueOf(arraySize);

		// We use the IndexGenerator to avoid names conflicts
		IndexGenerator indexGenerator = IndexGenerator.getInstance();
		int indexNumber = indexGenerator.get();
		// We create the index used for iterate using indexNumber
		String index = "i"+indexNumber;

		String captureCode = "";

		captureCode += "{\n";
		String xmlCode = "<array>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";

		captureCode += "int "+index+";\n";
		captureCode += "for("+index+"=0;"+index+"<"+realSize+";"+index+"++){\n";
		// We create an auxiliar name corresponding to an entry in the array
		String entryName = varName+"["+index+"]";
		captureCode += getArrayEntryCode(entryName,fileDescriptor,returnID, arrayType, index);
		captureCode += "}\n";
		//captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		/*for(int i=0;i<arraySize;i++){
			captureCode += getArrayEntryCode(varName,i,fileDescriptor,returnID, arrayType);
		}*/
		xmlCode = "</array>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		captureCode += "}\n";
		return captureCode;
	}
	private static String getArrayEntryCode(String entryName, String fileDescriptor, String returnID, ImplNode arrayType, String index){
		String captureCode = "";
		//String entryArrayName = varName+"[i]";
		String xmlCode = "";
		xmlCode += "<arrayEntry>\\n";
		xmlCode += "<index>\\n";
		xmlCode += "%d\\n";
		xmlCode += "</index>\\n";
		xmlCode += "<indexValue>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\","+index+");\n";
		captureCode += WritersManager.getCaptureCode(entryName, arrayType, fileDescriptor, returnID)+"\n";
		xmlCode = "</indexValue>\\n";
		xmlCode += "</arrayEntry>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		return captureCode;
	}
}