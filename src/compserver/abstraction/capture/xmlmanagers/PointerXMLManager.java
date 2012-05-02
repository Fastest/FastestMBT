package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;

/**
 * Provides utilities for interpret and encode values of a pointer in XML format
 */
public class PointerXMLManager {

    /**
     * Returns the string with the C Code for capture the output of a pointer
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		ImplNodePointer pointerNode = (ImplNodePointer) implNode;
		ImplNode pointerType = pointerNode.getType();
		String captureCode = "";
		String xmlCode = "<pointerValue>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		captureCode += WritersManager.getCaptureCode(varName,pointerType, fileDescriptor,returnID);
		xmlCode = "</pointerValue>";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		return captureCode;
	}
}