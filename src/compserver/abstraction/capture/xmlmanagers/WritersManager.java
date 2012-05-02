package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;

/**
 * Analizes the type of a variable and obtain the code for capture the output of 
 * that variable 
 */
public class WritersManager {    

    /**
     * Returns the string with the C Code for capture the output of a variable
     * @param implNode the node with the information of type of a variable
     * @param fileDescriptor the descriptor of the file
     * @param returnID the identifier of the return variable for fprintf()
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		String captureCode="";
		if(implNode instanceof ImplNodeArray)
			captureCode = ArrayXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		else if(implNode instanceof ImplNodeDB)
			captureCode = DBXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		else if(implNode instanceof ImplNodeFile)
			captureCode = FileXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		else if(implNode instanceof ImplNodeList)
			captureCode = ListXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		else if(implNode instanceof ImplNodePointer){
			// Analizamos primero si es un puntero a char
			// Esto deberia hacerlo el preprocesador!!!
			ImplNodePointer nodePointer = (ImplNodePointer) implNode;
			ImplNode pointerType = nodePointer.getType();
			if(pointerType instanceof ImplNodePLType){
			ImplNodePLType plType = (ImplNodePLType) pointerType;
			if(plType.getType().equals("char")){
				ImplNodePLType stNode = new ImplNodePLType("string");
				captureCode = PLTypeXMLManager.getCaptureCode(varName, stNode, fileDescriptor, returnID);
			}
			else 
			captureCode = PointerXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
			}
			else
			captureCode = PointerXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		}
		else if(implNode instanceof ImplNodeScreen)
			captureCode = ScreenXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		else if(implNode instanceof ImplNodeStructure)
			captureCode = StructureXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		else if(implNode instanceof ImplNodePLType)
			captureCode = PLTypeXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		else if(implNode instanceof ImplNodeEnumeration)
			captureCode = EnumerationXMLManager.getCaptureCode(varName, implNode, fileDescriptor, returnID);
		return captureCode;
	}

    /**
     * Returns the declarations of auxiliar variables that are needer for capture the code
     * @return the declarations
     */
	public static String getDeclarations(ImplNode implNode){
		String declarations = "";
		if(implNode instanceof ImplNodeList)
			declarations = ListXMLManager.getDeclarations(implNode);
		else if(implNode instanceof ImplNodeDB)
			declarations = DBXMLManager.getDeclarations(implNode);
		return declarations;
	}

}