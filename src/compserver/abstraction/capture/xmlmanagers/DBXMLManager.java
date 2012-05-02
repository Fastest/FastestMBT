package compserver.abstraction.capture.xmlmanagers;

import java.util.*;
import compserver.abstraction.types.impltypes.*;
import compserver.abstraction.types.spectypes.*;

/**
 * Provides utilities for interpret and encode values of a DB in XML format
 */
public class DBXMLManager {    

    /**
     * Returns the string with the C Code for capture the output of a database
     * @param implNode the node with the information of type of a variable
     * @param varName the name of the variable under analysis
     * @param fileDescriptor the file descriptor of the file in which we write
     * the captured value in XML format
     * @param returnID the identifier of the return value of fprintf
     * @return the code
     */
	public static String getCaptureCode(String varName, ImplNode implNode, String fileDescriptor, String returnID){
		ImplNodeDB dbNode = (ImplNodeDB) implNode;
		String dbmsID = dbNode.getDbmsID();
		String connID = dbNode.getConnID();
		String tableName = dbNode.getTableName();
		List<ImplNodeDBColumn> columns = dbNode.getColumns();

		String captureCode = "";
		//Estoy considerando que va a haber una sola base de datos. SOLUCIONAR!!!
		String xmlCode = "<db>\\n";
		captureCode += returnID+" = fprintf("+fileDescriptor+",\""+xmlCode+"\");\n";
		captureCode += "if (sql_query(" + connID + ", \"SELECT * FROM " + tableName + "\")) {\n";
		captureCode += "\tfprintf(stderror,\"%s\\n\",mysql_error("+connID+"));\n";
		captureCode += "\tmysql_close("+connID+");\n";
		captureCode += "\texit(0);\n}\n";
		captureCode += "if((res"+varName+" = mysql_store_result("+connID+"))) {\n";
		captureCode += "\tnumRows"+varName+" = (int) mysql_num_rows(res"+varName+");\n";
		captureCode += "\tnumFields"+varName+" = (int) mysql_num_fields(res"+varName+");\n";
		captureCode += "\tfor(i"+varName+" = 0; i"+varName+" < numFields"+varName+"; i"+varName+"++) {\n";
		captureCode += "\t\tcolumn"+varName+" = mysql_fetch_field(res"+varName+");\n";
		captureCode += "\t\t"+returnID+" = fprintf("+fileDescriptor+",\"<column>\\n<columnID>\\n%s\\n</columnID>\\n\",column"+varName+"->name);\n";
		captureCode += "\t\tfor(j"+varName+" = 0 ; j"+varName+" < numRows"+varName+" ; j"+varName+"++) {\n";
		captureCode += "\t\t\tmysql_data_seek(res"+varName+",i"+varName+");\n";
		captureCode += "\t\t\trow"+varName+" = mysql_fetch_row(res"+varName+");\n";
		captureCode += "\t\t\tfor(k"+varName+"=0;k"+varName+"<nroFields"+varName+";k"+varName+"++)\n";
		captureCode += "\t\t\t\t"+returnID+" = fprintf("+fileDescriptor+",\"<columnValue>\\n%s\\n</columnValue>\\n\",row"+varName+"[k]);\n";
		captureCode += "\t\t}\n";
		captureCode += "\t"+returnID+" = fprintf("+fileDescriptor+",\"</column>\\n\");\n";
		captureCode += "\t}\n}";
		return captureCode;
	}
	public static String getDeclarations(ImplNode implNode)
	{
		ImplNodeDB dbNode = (ImplNodeDB) implNode;
		String varName = dbNode.getImplID();
		String declaration = "";
		declaration += "MYSQL_RES *res"+varName+";\n";
		declaration += "int numRows"+varName+";\n";
		declaration += "int numFields"+varName+";\n";
		declaration += "int i"+varName+";\n";
		declaration += "int j"+varName+";\n";
		declaration += "int k"+varName+";\n";
		declaration += "MYSQL_FIELD *column"+varName+";\n";
		declaration += "MYSQL_ROW *row"+varName+";\n";
		return declaration;
	}
}