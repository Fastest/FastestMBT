package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import java.net.URL;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;

/**
 * Provides utilities for interpret and encode values of the screen in XML format
 */
public class ScreenXMLManager {    

    /**
     * Returns the string with the C Code for capture the output of the screen
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		String captureCode="";
		if(implNode instanceof ImplNodeScreenPlane){
			ImplNodeScreenPlane screenPlane = (ImplNodeScreenPlane) implNode;
			ImplNodeEnumeration enumeration = screenPlane.getEnumeration();
			// Al igual que en file postergo la lectura para no leer 2 veces
			// Registro el tipo de pantalla que se trata y, si tiene, la enum
			String xmlCode = "<screen>\\n";
			xmlCode += "<screentype>\\n";
			xmlCode += "plane\\n";
			xmlCode += "</screentype>\\n";
			if(enumeration!=null){
			xmlCode += "<enum>\\n";
			Map<String, String> enumElements = enumeration.getElements();
			Set<Map.Entry<String,String>> set = enumElements.entrySet();
			Iterator<Map.Entry<String,String>> it = set.iterator();
			while(it.hasNext()){
				xmlCode += "<enumentry>\\n";
				Map.Entry<String,String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				//System.out.println("La clave es: "+key);
				//System.out.println("El valor es: "+value);
				xmlCode += "<implvalue>\\n";
				xmlCode += key+"\\n";
				xmlCode += "</implvalue>\\n";
				xmlCode += "<specvalue>\\n";
				xmlCode += value+"\\n";
				xmlCode += "</specvalue>\\n";
				xmlCode += "</enumentry>\\n";
			}
			xmlCode += "</enum>\\n";
			}
			xmlCode += "</screen>";
			captureCode = returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		}
		else if(implNode instanceof ImplNodeScreenTable){
			ImplNodeScreenTable screenTable = (ImplNodeScreenTable) implNode;
			ImplNodeEnumeration enumeration = screenTable.getEnumeration();
			String delimiter = screenTable.getDelimiter();
			int rowLB = screenTable.getRowLowerBound();
			int rowUB = screenTable.getRowUpperBound();
			int columnLB = screenTable.getColumnLowerBound();
			int columnUB = screenTable.getColumnUpperBound();
			String xmlCode = "<screen>\\n";
			xmlCode += "<screentype>\\n";
			xmlCode += "table\\n";
			xmlCode += "</screentype>\\n";
			xmlCode += "<delimiter>\\n";
			xmlCode += delimiter+"\\n";
			xmlCode += "</delimiter>\\n";
			xmlCode += "<rowLowerBound>\\n";
			xmlCode += rowLB+"\\n";
			xmlCode += "</rowLowerBound>\\n";
			xmlCode += "<rowUpperBound>\\n";
			xmlCode += rowUB+"\\n";
			xmlCode += "</rowUpperBound>\\n";
			xmlCode += "<columnLowerBound>\\n";
			xmlCode += columnLB+"\\n";
			xmlCode += "</columnLowerBound>\\n";
			xmlCode += "<columnUpperBound>\\n";
			xmlCode += columnUB+"\\n";
			xmlCode += "</columnUpperBound>\\n";
			if(enumeration!=null){
			xmlCode += "<enum>\\n";
			Map<String, String> enumElements = enumeration.getElements();
			Set<Map.Entry<String,String>> set = enumElements.entrySet();
			Iterator<Map.Entry<String,String>> it = set.iterator();
			while(it.hasNext()){
				xmlCode += "<enumentry>\\n";
				Map.Entry<String,String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				xmlCode += "<implvalue>\\n";
				xmlCode += key+"\\n";
				xmlCode += "</implvalue>\\n";
				xmlCode += "<specvalue>\\n";
				xmlCode += value+"\\n";
				xmlCode += "</specvalue>\\n";
				xmlCode += "</enumentry>\\n";
			}
			xmlCode += "</enum>\\n";
			}
			xmlCode += "</screen>";
			captureCode = returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		}
		return captureCode;
	}
}