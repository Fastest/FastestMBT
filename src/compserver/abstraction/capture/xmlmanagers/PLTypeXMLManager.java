package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;

/**
 * Provides utilities for interpret and encode values of a native type of C in XML format
 */
public class PLTypeXMLManager {    

    /**
     * Returns the string with the C Code for capture the output of a native type
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		ImplNodePLType plTypeNode = (ImplNodePLType) implNode;
		String varType = plTypeNode.getType();
		String captureCode = "";
		// We analize the type of the variable
		if(varType.equals("int"))
			captureCode = addCaptureCodeInt(varName, fileDescriptor, returnID);
		else if(varType.equals("char"))
			captureCode = addCaptureCodeChar(varName, fileDescriptor, returnID);
		else if(varType.equals("double"))
			captureCode = addCaptureCodeDouble(varName, fileDescriptor, returnID);
		else if(varType.equals("float"))
			captureCode = addCaptureCodeFloat(varName, fileDescriptor, returnID);
		else if(varType.equals("string"))
			captureCode = addCaptureCodeString(varName, fileDescriptor, returnID);
		return captureCode;
	}
	public static String addCaptureCodeInt(String varName, String fileDescriptor, String returnID){
		String captureCode = returnID+" = ";
		String xmlCode1 = "";
		String xmlCode2 = "";
		xmlCode1 += "<intValue>\\n";
		xmlCode2 += "</intValue>\\n";
		captureCode += "fprintf("+fileDescriptor+",\""+xmlCode1+"%d\\n"+xmlCode2+"\","+varName+");";
		return captureCode;
	}
	private static String addCaptureCodeFloat(String varName, String fileDescriptor, String returnID){
		String captureCode = returnID+" = ";
		String xmlCode1 = "";
		String xmlCode2 = "";
		xmlCode1 += "<floatValue>\\n";
		xmlCode2 += "</floatValue>\\n";
		captureCode += "fprintf("+fileDescriptor+",\""+xmlCode1+"%f\\n"+xmlCode2+"\","+varName+");";
		return captureCode;
	}
	private static String addCaptureCodeDouble(String varName, String fileDescriptor, String returnID){
		String captureCode = returnID+" = ";
		String xmlCode1 = "";
		String xmlCode2 = "";
		xmlCode1 += "<doubleValue>\\n";
		xmlCode2 += "</doubleValue>\\n";
		captureCode += "fprintf("+fileDescriptor+",\""+xmlCode1+"%f\\n"+xmlCode2+"\","+varName+");";
		return captureCode;
	}
	private static String addCaptureCodeChar(String varName, String fileDescriptor, String returnID){
		String captureCode = returnID+" = ";
		String xmlCode1 = "";
		String xmlCode2 = "";
		xmlCode1 += "<charValue>\\n";
		xmlCode2 += "</charValue>\\n";
		captureCode += "fprintf("+fileDescriptor+",\""+xmlCode1+"%c\\n"+xmlCode2+"\","+varName+");";
		return captureCode;
	}
	private static String addCaptureCodeString(String varName, String fileDescriptor, String returnID){
		String captureCode = returnID+" = ";
		String xmlCode1 = "";
		String xmlCode2 = "";
		xmlCode1 += "<stringValue>\\n";
		xmlCode2 += "</stringValue>\\n";
		captureCode += "fprintf("+fileDescriptor+",\""+xmlCode1+"%s\\n"+xmlCode2+"\","+varName+");";
		return captureCode;
	}
}