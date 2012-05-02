package client.blogic.testing.refinement;


import java.util.*;
import java.io.*;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import common.z.SpecUtils;
import java.lang.reflect.Field;

/**
* This module obtains code for assign values to the refined variables encapsulated in
* instances of TCaseAssignment
*/

public class RefineExprJava implements RefineExpr{

    /**
     * Returns a list with TCaseAssignment in Java languaje
     * @param rule The rule used to refine
     * @param atcPred The predicate of the abstract test case 
     * @throws java.lang.IllegalArgumentException if there is an inconsistency in this
     * stage of the refinement 
     * @return a list with the assignments
     */
    public List<TCaseAssignment> refineRule(RuleRefinement rule, Pred atcPred)
    throws IllegalArgumentException{
	ArrayList<String> specIDs = rule.getSpecIDs();
	Set<String> implIDs = rule.getImplIDs();
	Iterator<String> iterImplIDs = implIDs.iterator();
	int specIDsNum = specIDs.size();
	int implIDsNum = implIDs.size();
	// We analyze the number of variables of the implementation and the variables of the
	// specification involved in this rule and proceed
	// Simple refinement
	if(specIDsNum==1 && implIDsNum==1){
		String implID = iterImplIDs.next();
		String specID = specIDs.get(0);
		// We obtain the type of the variable of the implementation
		NodeType implType = rule.getNodeType(implID);
		// We obtain the MemPred in the atcPred related to the specID
		MemPred memPred = Utils.getRelatedEquality(atcPred,specID);
		if(memPred == null)
			throw new IllegalArgumentException("The variable "+ specID +" is not present in the predicate\n"+SpecUtils.termToLatex(atcPred));
		// We obtain the value of the assignment
		ZExprList exprList=(ZExprListImpl)(((SetExpr)(memPred.getRightExpr())).getZExprList());
		Expr value = exprList.get(0); 
		return simpleRefinement(implID, specID, implType, value);
	}
	// Refinement with abstraction code
	else if(specIDsNum==1 && implIDsNum>1){
		// We obtain the specID
		String specID = specIDs.get(0);
		// We obtain the list of implIDs and their types
		List<String> listImplIDs = new ArrayList<String>();
		List<NodeType> listImplType = new ArrayList<NodeType>();
		while(iterImplIDs.hasNext()){
			String auxImplID = iterImplIDs.next();
			listImplIDs.add(auxImplID);
			listImplType.add(rule.getNodeType(auxImplID));
		}
		// We obtain the MemPred in the atcPred related to the specID
		MemPred memPred = Utils.getRelatedEquality(atcPred,specID);
		if(memPred == null)
			throw new IllegalArgumentException("The variable "+ specID +" is not present in the predicate\n"+SpecUtils.termToLatex(atcPred));
		// We obtain the value of the assignment
		ZExprList exprList=(ZExprListImpl)(((SetExpr)(memPred.getRightExpr())).getZExprList());
		Expr value = exprList.get(0); 
		// We obtain the abstraction code
		Abstraction abstraction = rule.getAbstraction();
		return refineWithAbstractionCode(specID, listImplIDs, listImplType, value, abstraction);
	}
	// Refinement with refinement code - No esta implementado!!!
	else if(specIDsNum==1 && implIDsNum>1){
		return refineWithRefinementCode(rule, atcPred);
	}
	else{
		throw new IllegalArgumentException("Fastest can not refine a rule with "+ specIDsNum+" variables of the specification and "+implIDsNum+ "variables of the implementation");
	}
    }

    /**
     * Obtains a list with assignments in a refinement involving one variable of the
     * specification and one variable of the implementation
     * @param implID The identifier of the variable of the implementation
     * @param specID The identifier of the variable of the specification
     * @param implType The object that represents the type of the variable of the
     * implementation
     * @param value The value corresponding in the abstract test case to the specification
     * variable
     * @throws java.lang.IllegalArgumentException if there is an inconsistency in this
     * stage of the refinement 
     * @return a list with the assignments
     */
    private List<TCaseAssignment> simpleRefinement(String implID, String specID, NodeType implType, Expr value) throws IllegalArgumentException{
	// We analyze the type of the variable in the implementation and call
	// the corresponding method to refine
	List<TCaseAssignment> assignList = new ArrayList<TCaseAssignment>();
	if (implType instanceof NodePLType){
		NodePLType implPLType = (NodePLType) implType;
		TCaseAssignment assignment = refineWithPLType(specID,value,implPLType,implID);
		assignList.add(assignment);
		return assignList;
	}
	else if (implType instanceof NodeArray){
		NodeArray implArray = (NodeArray) implType;
		TCaseAssignment assignment = refineWithArray(specID,value,implArray,implID);
		assignList.add(assignment);
		return assignList;
	}
	else if (implType instanceof NodeStructure){
		NodeStructure implStruct = (NodeStructure) implType;
		TCaseAssignment assignment = refineWithStruc(specID,value,implStruct,implID);
		assignList.add(assignment);
		return assignList;
	}
	else if (implType instanceof NodeList){
		NodeList implList = (NodeList) implType;
		TCaseAssignment assignment = refineWithList(specID,value,implList,implID);
		assignList.add(assignment);
		return assignList;
	}
	else if (implType instanceof NodeFile){
		NodeFile implFile = (NodeFile) implType;
		TCaseAssignment assignment = refineWithFile(specID,value,implFile,implID);
		assignList.add(assignment);
		return assignList;
	}
	else if (implType instanceof NodeDB){
		NodeDB implDB = (NodeDB) implType;
		TCaseAssignment assignment = refineWithDB(specID,value,implDB,implID);
		assignList.add(assignment);
		return assignList;
	}
	else if (implType instanceof NodeEnumeration){
		NodeEnumeration implEnum = (NodeEnumeration) implType;
		TCaseAssignment assignment = refineWithEnum(specID,value,implEnum,implID);
		assignList.add(assignment);
		return assignList;
	}
	else
		throw new IllegalArgumentException("The type "+implType+" is not supported by the Java refiner module");
    }

    /**
     * Obtains a list with assignments in a refinement involving one variable of the
     * specification and more than one variable of the implementation
     * @param specID The identifier of the variable of the specification
     * @param implIDs The identifiers of the variables of the implementation
     * @param implTypes The objects that represents the types of the variables of the
     * implementation
     * @param value The value corresponding in the abstract test case to the specification
     * variable
     * @param abstraction The auxiliar code used to relate the variables of implementation
     * with the variable of the specification
     * @throws java.lang.IllegalArgumentException if there is an inconsistency in this
     * stage of the refinement 
     * @return a list with the assignments
     */
    private List<TCaseAssignment> refineWithAbstractionCode(String specID, List<String> implIDs, List<NodeType> implTypes, Expr value, Abstraction abstraction) throws IllegalArgumentException{

	List<TCaseAssignment> assignments = new ArrayList<TCaseAssignment>();
	// The number of equalities in abstraction must be the same that the number of the
	// variables of the implementation to refine
	List<Equality> equalities = abstraction.getEqualities(specID);
	if(equalities.size() != implIDs.size()){
		throw new IllegalArgumentException("The number of assignments in ABSTRACTION code must match with the number of variables of the impletation to refine with this rule");
	}
	
	for(int i=0;i<implIDs.size();i++){
		String implID = implIDs.get(i);
		NodeType implType = implTypes.get(i);
		Equality equality = Utils.getAssociatedEquality(implID,equalities);
                String preOperator = equality.getPreOperatorSpecID();
                String plCode = equality.getPLCodeEquality();
		// Esto tendria que ser un poquito mas extendible, no solo depender del preOp
		// x ej: casos en los que hay restricciones
		// x ej: producto cartesiano
		if (preOperator.equals("dom")) {
			if(value instanceof SetExpr){
			SetExpr setValue = (SetExpr) value;
			SetExpr domValue = Utils.extractDomValues(setValue);
			assignments.addAll(simpleRefinement(implID,specID,implType,domValue));
			}
		} else if (preOperator.equals("ran")) {
			if(value instanceof SetExpr){
			SetExpr setValue = (SetExpr) value;
			SetExpr ranValue = Utils.extractRanValues(setValue);
			assignments.addAll(simpleRefinement(implID,specID,implType,ranValue));
			}
		}
	}
	return assignments;
    }

    // Esta opcion no esta soportada aun
    private List<TCaseAssignment> refineWithRefinementCode(RuleRefinement rule, Pred atcPred)
    throws IllegalArgumentException{
	return null;
    }

    private TCaseAssignment refineWithEnum(String specID, Expr expr, NodeEnumeration implType, String implID) throws IllegalArgumentException{

        NodeType enumerationType = implType.getType();
	if(enumerationType instanceof NodePLType){
	String ref = "";
	
        String pltype = ((NodePLType) enumerationType).getType();
	ref += pltype+" "+implID+";\n";
	ref += implID + " = ";
	if(expr instanceof RefExpr){

	// We create a new RefExpr in which we replace the actual value for the value
	// corresponding in the enumeration
        RefExpr refExpr = (RefExpr) expr;
        ZName elementZName = refExpr.getZName();
        String element = elementZName.getWord();
        String constant = implType.getConstant(element);
        if (constant != null) {
            constant = constant.trim();
            ZFactory factory = new ZFactoryImpl();
            ZStrokeList strokeList = factory.createZStrokeList();
            ZName constantName = factory.createZName(constant, strokeList, constant);
            RefExpr constantRefExpr = factory.createRefExpr();
            constantRefExpr.setName(constantName);

	    // Now we use the new refExpr to obtain the assignment value
            String constantGenerated = ConstantGenerator.PLTypeToString(pltype,
                    constantRefExpr,implID);

	    // We must to set again the entry in ConstantStore.
	    // The new entry will map the value in the implementation to the
	    // corresponding value in the specification
	    ConstantStore constantStore = ConstantStore.getInstance();
	    constantStore.set(implID, constant,expr);

            ref = ref.concat(constantGenerated + ";");

            return new TCaseAssignment(specID, ref);
        } else {
            Utils.printErrorEnumerationElementNotSpecified(element);
            throw new IllegalArgumentException();
        }
	}
	else{
		throw new IllegalArgumentException(SpecUtils.termToLatex(expr)+" is not an instance of an enumerated free type"); // Debo manejarlo mas arriba
	}
	}
	else{
		throw new IllegalArgumentException("An instance of a enumerated free type must be refined to PLType");
	}
    }

    private TCaseAssignment refineWithPLType(String specID, Expr expr, NodePLType implType, String implID){
	String ref = "";
	// We obtain the type of this variable in the implementation and declare it
	String javaType = implType.getType();
	ref += javaType + " " + implID + ";\n";
        ref += implID + " = ";
        ref += ConstantGenerator.PLTypeToString(implType.getType(),expr,implID) + ";";
        return new TCaseAssignment(specID, ref);
    }

    private TCaseAssignment refineWithArray(String specID , Expr expr, NodeArray implType, String implID) throws IllegalArgumentException{
        String ref="";
        List<Expr> list = Utils.getListExpr(expr);
	// If this array have a variable that point to the next free position in the array
	// we must initialize it
	String lastPosPointerID = implType.getLastPosPointerID();
	if(lastPosPointerID!=null)
		ref += lastPosPointerID + " = "+ list.size()+";\n";

	ref += "// We initialize the array "+implID+"\n";
	// We obtain the Java type of this array
	String javaType = getJavaType(implType);

	NodeType arrayType = implType.getType();
	int arraySize = implType.getSize();


	// We declare the array
	// TCRL solo permite arreglos unidimensionales
	ref += javaType+" "+implID +" = new "+javaType.substring(0,javaType.indexOf("[")+1)+arraySize+"];\n";

	int auxVarCount = 0;
        for (int i=0; i<list.size(); i++){
		Expr subExpr = list.get(i);
		if(arrayType instanceof NodePLType){
			// This is the simplest case. It is not necessary to define
			// auxiliar variables
			NodePLType nodePLType = (NodePLType) arrayType;
			String value=ConstantGenerator.PLTypeToString(nodePLType.getType(),subExpr,implID);
			ref += "// We set the "+i+"th entry in the array "+implID+"\n";
			ref += implID+"["+i+"] = "+value+";\n";
		}
		else{
			// We must declarate the auxiliar variable that will be added to the array
			String auxVarName = implID+"Aux"+auxVarCount;
			// We obtain the refinement code for this auxiliar variable
			String refAuxCode = obtainRefinementText(arrayType, auxVarName, subExpr);
			ref += refAuxCode;
			ref += "// We set the "+i+"th entry in the array "+implID+"\n";
			ref += implID+"["+i+"] = "+auxVarName+";\n";
		}

		auxVarCount++;
        }
        return new TCaseAssignment(specID, ref);
    }

    private TCaseAssignment refineWithStruc(String specID, Expr expr, NodeStructure implType, String implID) throws IllegalArgumentException{
        String ref="";
        List<NodeElement> elements = implType.getElements();
	String structName = implType.getName();
	// We obtain the name of the class from the namespace
	String structSimpleName = "";
	if(structName.contains("."))
		structSimpleName = structName.substring(structName.lastIndexOf(".")+1);
	else
		structSimpleName = structName;

        List<Expr> list = Utils.getListExpr(expr);

        if (list.size() == 0){
            return new TCaseAssignment(specID, ref);
        }

	// The cardinaly of the "fields" in the structure must match with the cardinality
	// of the "elements" in the list because we assume that each "element" in the list
	// will be refined to a "field" in the structure
	if(elements.size()!=list.size())
		throw new IllegalArgumentException("There was a trouble in the refinement of "+specID);

	ref += "\n// We create and set an instance of the class "+structSimpleName+" using Java Reflection\n";
	ref += "Class<?> class"+implID+" = Class.forName(\""+structName+"\");\n";
	ref += "Object object"+implID+" = class"+implID+".newInstance();\n";
	ref += structSimpleName+" "+implID+" = null;\n";
	ref += "if (object"+implID+" instanceof "+structSimpleName+")\n";
	ref += "\t"+implID+" = ("+structSimpleName+") object"+implID+";\n\n";
        for (int i=0;i<list.size();i++){
            Expr subExpr =list.get(i);
	    NodeElement nodeElement = elements.get(i);
	    ref += refineStructureMember(implID,nodeElement,subExpr);
        }
        return new TCaseAssignment(specID, ref);
    }

	private String refineStructureMember(String implID, NodeElement nodeElement, Expr expr){
		String ret = "";
		try{
			String memberID = nodeElement.getID();
			String classID = "class"+implID;
			String fieldID = "field"+memberID+implID;
			NodeType memberType = nodeElement.getType();
			String completeID = implID+"."+memberID;

			// We create the code to set a field of a class using Java Reflection
			ret += "Field "+fieldID+" = "+classID+".getDeclaredField(\""+memberID+"\");\n";
			ret += fieldID+".setAccessible(true);\n";
			if(memberType instanceof NodePLType){
				NodePLType plType = (NodePLType) memberType;
				String value = ConstantGenerator.PLTypeToString(plType.getType(),expr,completeID);
				String strType = plType.getType();
				if(strType.equals("byte"))
					ret += fieldID+".set( "+implID+", new Byte("+value+"));\n";
				else if(strType.equals("short"))
					ret += fieldID+".set( "+implID+", new Short("+value+"));\n";
				else if(strType.equals("int"))
					ret += fieldID+".set( "+implID+", new Integer("+value+"));\n";
				else if(strType.equals("long"))
					ret += fieldID+".set( "+implID+", new Long("+value+"));\n";
				else if(strType.equals("float"))
					ret += fieldID+".set( "+implID+", new Float("+value+"));\n";
				else if(strType.equals("double"))
					ret += fieldID+".set( "+implID+", new Double("+value+"));\n";
				else if(strType.equals("char"))
					ret += fieldID+".set( "+implID+", new Character('"+value+"'));\n";
				else if(strType.equals("String"))
					ret += fieldID+".set( "+implID+", "+value+");\n";
			}
			else{
				// We must create an auxiliar name to this field
				String auxImplID = memberID+implID;
				// We obtain the refinement code for this auxiliar variable
				String refAuxCode = obtainRefinementText(memberType, auxImplID, expr);
				ret += refAuxCode;
				ret += "\n"+fieldID+".set( "+implID+", "+auxImplID+");\n";
			}

		} catch(Exception e){
			e.printStackTrace(System.out);
		}
		return ret;
	}

    private TCaseAssignment refineWithList(String specID, Expr expr, NodeList implType, String implID) throws IllegalArgumentException{
        String ref="";
        //String name = implType.getName();
	String name = implID;
	String listType = implType.getListType();
	NodeType nodeType = implType.getNodeType();
	if(nodeType==null)
		throw new  IllegalArgumentException("The refinement stage could not be completed because the arguments of the list "+name+" in the TCRL file are incorrect");

	// We declare the variable
	String type = getJavaType(nodeType);
	ref += "// We initialize the list "+name+"\n";
        ref += listType+"<"+type+"> "+name+" = new "+listType+"<"+type+">();\n";

        List<Expr> list = Utils.getListExpr(expr);
        //Each element in variable list must be a node in the java list
        if (list.size() == 0){
            return new TCaseAssignment(specID, ref);
        }


	int auxVarCount = 0;
        for (int i=0; i<list.size(); i++){
		Expr auxExpr = list.get(i);
		// We obtain a name for the auxiliar variable that will be added to the list
		String auxVarName = implID+"Aux"+auxVarCount;
		//ref += type+" "+auxVarName+";\n";
		if(nodeType instanceof NodePLType){
			NodePLType plType = (NodePLType) nodeType;
			String value = ConstantGenerator.PLTypeToString(plType.getType(),auxExpr,implID);
			String strType = plType.getType();
			if(strType.equals("byte"))
				ref += auxVarName+" = new Byte("+value+");\n";
			else if(strType.equals("short"))
				ref += auxVarName+" = new Short("+value+");\n";
			else if(strType.equals("int"))
				ref += auxVarName+" = new Integer("+value+");\n";
			else if(strType.equals("long"))
				ref += auxVarName+" = new Long("+value+");\n";
			else if(strType.equals("float"))
				ref += auxVarName+" = new Float("+value+");\n";
			else if(strType.equals("double"))
				ref += auxVarName+" = new Double("+value+");\n";
			else if(strType.equals("char"))
				ref += auxVarName+" = new Character('"+value+"');\n";
			else if(strType.equals("String"))
				ref += auxVarName+" = \""+value+"\");\n";
		}
		else{
			// We obtain the refinement code for this auxiliar variable
			String refAuxCode = obtainRefinementText(nodeType, auxVarName, auxExpr);
			ref += refAuxCode;
		}
		ref += name+".add("+auxVarName+");\n";
		auxVarCount++;
        }

        return new TCaseAssignment(specID, ref);
    }

/**
* Returns the "Java type" for the corresponding node
*/
    private String getJavaType(NodeType nodeType){
	String javaType = "";
	if(nodeType instanceof NodePLType){
		NodePLType plType = (NodePLType) nodeType;
		String strType = plType.getType();
		if(strType.equals("byte"))
			javaType = "Byte";
		else if(strType.equals("short"))
			javaType = "Short";
		else if(strType.equals("int"))
			javaType = "Integer";
		else if(strType.equals("long"))
			javaType = "Long";
		else if(strType.equals("float"))
			javaType = "Float";
		else if(strType.equals("double"))
			javaType = "Double";
		else if(strType.equals("char"))
			javaType = "Character";
		else if(strType.equals("String"))
			javaType = "String";
		else if(strType.equals("boolean"))
			javaType = "Boolean";
	}
	else if(nodeType instanceof NodeStructure){
		NodeStructure structType = (NodeStructure) nodeType;
		String structName = structType.getName();
		if(structName.contains("."))
			structName = structName.substring(structName.lastIndexOf(".")+1);
		javaType = structName;
	}
	else if(nodeType instanceof NodeFile){
		javaType = "File";
	}
	else if(nodeType instanceof NodeList){
		NodeList nodeList = (NodeList) nodeType;
		NodeType listNodeType = nodeList.getNodeType();
		javaType = "List<"+getJavaType(listNodeType)+">";
	}
	else if(nodeType instanceof NodeArray){
		NodeArray nodeArray = (NodeArray) nodeType;
		NodeType arrayType = nodeArray.getType();
		String arrayJavaType = getJavaType(arrayType);
		if(arrayJavaType.matches("Long|Float|Double|Short|Byte|Boolean"))
			arrayJavaType = arrayJavaType.toLowerCase();
		else if(arrayJavaType.equals("Integer"))
			arrayJavaType = "int";
		else if(javaType.equals("Character"))
			arrayJavaType = "char";
		javaType = arrayJavaType+"[]";
	}
	// Completar los casos que faltan
	return javaType;
    }


    public static TCaseAssignment refineWithFile(String specID, Expr expr, NodeFile implType, String implID) throws IllegalArgumentException{
	// Tal vez deba cambiar los \n en fileContent por \\n
        String path = (implType.getPath()).substring(1, (implType.getPath()).length()-1);
        String name = (implType.getName()).substring(1, (implType.getName()).length()-1);
	//Le quito el punto de la ext, si tiene
        String fileName = "file_" + name.replace(".", ""); 
        String del = implType.getDelimiter();
        String structure = implType.getStructure();
	String eol = implType.getEol();
	String eof = implType.getEof();

	String ref = "";
	ref = "File " + fileName + " = new File(\""+path +File.separator+ name+"\");\n";


        String fileContent="";

        List<Expr> list = Utils.getListExpr(expr);
        //en list van a estar los registros

        for (int i=0; i<list.size(); i++){
            List<Expr> subList = Utils.getListExpr(list.get(i));
            //en subList van a estar los campos de un registro

            if ((list.get(i) instanceof RefExpr) || (list.get(i) instanceof NumExpr))
               ;//Ver que hacer con las cosas sueltas...

            for (int j=0; j<subList.size(); j++){
                String val;
                if (subList.get(j) instanceof NumExpr)
                    val = (((NumExpr)(subList.get(j))).getNumeral()).toString();
                else if (subList.get(j) instanceof RefExpr)
                    val = (((RefExpr)(subList.get(j))).getName()).toString();
                else
                    throw new IllegalArgumentException();
                if ((structure!=null) && (structure.equals("LINEAR"))){
                    //Todos los registros uno al lado del otro
                    fileContent=fileContent.concat(val + " " + del.substring(1, del.length()-1) + " ");
                }
                else if ((structure==null) || (structure.equals("RPL"))){
                    //Un registro por linea
                    if (j==(subList.size()-1)){
                        fileContent=fileContent.concat(val);
                        if (eol == null)
                            //No se define el EOL, se pone el estandar: \n
                            fileContent=fileContent.concat("\\n ");
                        else
                            //Si se defien, se agrega el EOL al final del registro
                            fileContent=fileContent.concat((eol).substring(1, (eol).length()-1));
                    }
                    else
                        fileContent=fileContent.concat(val + del.substring(1, del.length()-1));
                }
                else if ((structure!=null) && (structure.equals("FPL"))){
                    //Un campo por linea
                    fileContent=fileContent.concat(val);
                    if (eol == null)
                        //No se define el EOL, se pone el estandar: \n
                        fileContent=fileContent.concat("\n");
                    else
                        //Se define el EOL, se agrega el EOL al final del registro
                        fileContent=fileContent.concat((eol).substring(1, (eol).length()-1));
                }
            }

        }
        if (eof !=null)
            //Se especifica el indicador de fin de archivo, se agrega:
            fileContent=fileContent.concat((eof).substring(1, ((eof).length())-1));
        ref += "BufferedWriter bw"+fileName+" = new BufferedWriter(new FileWriter("+fileName+ "));\n";
	ref += "bw"+fileName+".write(\""+fileContent+"\");\n";
	ref += "bw"+fileName+".close();\n";
        return new TCaseAssignment(specID, ref);
    }

    public static TCaseAssignment refineWithDB(String specID, Expr expr, NodeDB implType, String implID) throws IllegalArgumentException{
        String connID = implType.getConnectionID();
        String table = implType.getTableName();
        String dbmsID = implType.getDBMSID();
        List<NodeDBColumn> columns = implType.getColumns();
        String colNames = "";
        String ref = "";
        List<Expr> list = Utils.getListExpr(expr);
        //en list van a estar los registros

        for (int i=0; i<list.size(); i++){
            List<Expr> subList = Utils.getListExpr(list.get(i));
            //en subList van a estar los campos de un registro

            if ((list.get(i) instanceof RefExpr) || (list.get(i) instanceof NumExpr))
               ;//Ver que hacer con las cosas sueltas...

            ref=ref.concat("if (!sql_query(" + connID + ", \"INSERT INTO " + table + " VALUES(");

            for (int j=0;j<subList.size();j++){
                NodeDBColumn rec = columns.get(j);
		String columnName = rec.getColName();
		String id = implID+"."+columnName;
                if ((rec.getColType()).equals("INT"))
                    ref=ref.concat(ConstantGenerator.toInt(subList.get(j),id));
                else if ((rec.getColType()).equals("CHAR")){
                    String val = ConstantGenerator.toString(subList.get(j),id);
                    ref=ref.concat("'" + val.substring(1, val.length() - 1) + "'");
                }
                else
                    throw new IllegalArgumentException();
                if (j < subList.size() - 1)
                    ref=ref.concat(",");
                else{
                    ref=ref.concat(")\")){\n");
                    ref=ref.concat("\tfprintf(stderr, \"%s\\n\", mysql_error(conn));\n");
                    ref=ref.concat("\texit(0);\n}");
                }
            }
        }
        return new TCaseAssignment(specID, ref);
    }

/**
* This method obtains the refinement code for auxiliar variables
*/
    private String obtainRefinementText(NodeType nodeType, String implID, Expr expr){
	String ref = "";
	if(nodeType instanceof NodeStructure){
		NodeStructure nodeStructure = (NodeStructure) nodeType;
		TCaseAssignment auxAssign = refineWithStruc("",expr,nodeStructure,implID);
		ref = auxAssign.getRefText();
	}
	else if(nodeType instanceof NodeFile){
		NodeFile nodeFile = (NodeFile) nodeType;
		TCaseAssignment auxAssign = refineWithFile("",expr,nodeFile,implID);
		ref = auxAssign.getRefText();
	}
	else if(nodeType instanceof NodeList){
		NodeList nodeList = (NodeList) nodeType;
		TCaseAssignment auxAssign = refineWithList("",expr,nodeList,implID);
		ref = auxAssign.getRefText();
	}
	else if(nodeType instanceof NodeDB){
		NodeDB nodeDB = (NodeDB) nodeType;
		TCaseAssignment auxAssign = refineWithDB("",expr,nodeDB,implID);
		ref = auxAssign.getRefText();
	}
	else if(nodeType instanceof NodeArray){
		NodeArray nodeArray = (NodeArray) nodeType;
		TCaseAssignment auxAssign = refineWithArray("",expr,nodeArray,implID);
		ref = auxAssign.getRefText();
	}
	return ref;
    }


}


