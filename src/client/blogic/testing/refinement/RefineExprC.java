package client.blogic.testing.refinement;


import java.util.*;
import java.math.BigInteger;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import common.z.SpecUtils;
import common.z.UtilSymbols;

public class RefineExprC implements RefineExpr{

    /**
     * Returns a list with TCaseAssignment in C languaje
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
	// Simple refinement
	if(specIDsNum==1 && implIDsNum==1){
		String implID = iterImplIDs.next();
		String specID = specIDs.get(0);
		// We obtain the type of the variable of the implementation
		NodeType implType = rule.getNodeType(implID);
		// We obtain the MemPred in the atcPred related to the specID
		MemPred memPred = Utils.getRelatedEquality(atcPred,specID);
		// CONTEMPLAR ERROR!!!
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
		// We obtain the value of the assignment
		ZExprList exprList=(ZExprListImpl)(((SetExpr)(memPred.getRightExpr())).getZExprList());
		Expr value = exprList.get(0); 
		// We obtain the abstraction code
		Abstraction abstraction = rule.getAbstraction();
		return refineWithAbstractionCode(specID, listImplIDs, listImplType, value, abstraction);
	}
	// Refinement with refinement code
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

    public List<TCaseAssignment> simpleRefinement(String implID, String specID, NodeType implType, Expr value) throws IllegalArgumentException{
	// We analyze the type of the variable in the implementation and call
	// the corresponding method to refine
	List<TCaseAssignment> assignList = new ArrayList<TCaseAssignment>();
	if (implType instanceof NodePointer){
		NodePointer implPointer = (NodePointer) implType;
		TCaseAssignment assignment = refineWithPointer(specID,value,implPointer,implID);
		assignList.add(assignment);
		return assignList;
	}
	else if (implType instanceof NodePLType){
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
		throw new IllegalArgumentException("The type "+implType+" is not supported by the C refiner module");
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

    public List<TCaseAssignment> refineWithAbstractionCode(String specID, List<String> implIDs, List<NodeType> implTypes, Expr value, Abstraction abstraction) throws IllegalArgumentException{

	List<TCaseAssignment> assignments = new ArrayList<TCaseAssignment>();

	List<Equality> equalities = abstraction.getEqualities(specID);

	for(int i=0;i<equalities.size();i++){
		Equality equality = equalities.get(i);
		// We obtain the implID related to this equality
		String implID = Utils.getRelatedImplID(equality);
		// Now we obtain the NodeType related to this implID
		int index;
		boolean found = false;
		for(index = 0;index<implIDs.size() && !found;index++)
			if(implIDs.get(index).equals(implID))
				found = true;
		if(!found){
			throw new IllegalArgumentException("There was a trouble in the refinement of "+ specID+".");
		}
		index -= 1;
		System.out.println("El indice es: "+index);
		NodeType implType = implTypes.get(index);

		// We obtain the preOperator related to this equality
		String preOperator = equality.getPreOperatorSpecID();




		// Esto tendria que ser un poquito mas extendible, no solo depender del preOp
		// x ej: casos en los que hay restrcciones y cosas asi
		// ver como se parsea
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
		} else if(preOperator.equals("#")) {
			if(value instanceof SetExpr){
			SetExpr setValue = (SetExpr) value;
			ZFactory zFactory = new ZFactoryImpl();
			int cardinality = setValue.getZExprList().size();
			String strCard = String.valueOf(cardinality);
			BigInteger bInt = new BigInteger(strCard);
			ZNumeral zNumeral = zFactory.createZNumeral(bInt);
			NumExpr numExpr = zFactory.createNumExpr(zNumeral);
			assignments.addAll(simpleRefinement(implID,specID,implType,numExpr));
			// Now we analyze if we must send to refine other implID
			if(implIDs.size()==2){
			// We obtain the ID of the other variable
			String mainImplID;
			NodeType mainNodeType;
			if(index==0){
				mainImplID = implIDs.get(1);
				mainNodeType = implTypes.get(1);
			}
			else if(index==1){
				mainImplID = implIDs.get(0);
				mainNodeType = implTypes.get(0);
			}
			}
			}	
		}
		
	}
	
	/*for(int i=0;i<implIDs.size();i++){
		String implID = implIDs.get(i);

		NodeType implType = implTypes.get(i);
		Equality equality = Utils.getAssociatedEquality(implID,equalities);
                String preOperator = equality.getPreOperatorSpecID();
                String plCode = equality.getPLCodeEquality();
		// Esto tendria que ser un poquito mas extendible, no solo depender del preOp
		// x ej: casos en los que hay restrcciones y cosas asi
		// ver como se parsea
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
	}*/
	return assignments;
    }

    public List<TCaseAssignment> refineWithRefinementCode(RuleRefinement rule, Pred atcPred)
    throws IllegalArgumentException{
	return null;
    }

    public static TCaseAssignment refineWithPointer(String specID, Expr expr, NodePointer implType, String implID) throws IllegalArgumentException{
        String ref="";
	NodeType pointerType = implType.getType();
        if (pointerType instanceof NodePLType){
            NodePLType type = (NodePLType) (pointerType);
            if (type.getType().contains("char")){
                ref = implID + " = ";
                ref = ref.concat(ConstantGenerator.PLTypeToString(type.getType(),expr,implID) + ";");
            }
            else{
                ref = "*" + implID + " = ";
                ref = ref.concat(ConstantGenerator.PLTypeToString(type.getType(),expr,implID) + ";");
            }
        }
        else{
            String fieldName;
            String head = implID + "[" + 0 + "]";
            fieldName="field" + (new Integer(0)).toString();
	    // Aca podria haber un problema. Debo analizar refineSubExpr()
            ref = ref.concat(refineSubExpr(head,pointerType,expr,0,fieldName,implID));
        }
        return new TCaseAssignment(specID, ref);
    }

    public static TCaseAssignment refineWithEnum(String specID, Expr expr, NodeEnumeration implType, String implID) throws IllegalArgumentException{

	String ref = implID + " = ";
        NodeType enumerationType = implType.getType();
	if(enumerationType instanceof NodePLType){
        String pltype = ((NodePLType) enumerationType).getType();

	if(expr instanceof RefExpr){
        RefExpr refExpr = (RefExpr) expr;
        ZName elementZName = refExpr.getZName();
        String element = elementZName.getWord();
        String constant = implType.getConstant(element);
        if (constant != null) {
            constant = constant.trim();

            //CREATE a NEW RefExpr with the associated CONSTANT
            ZFactory factory = new ZFactoryImpl();

            ZStrokeList strokeList = factory.createZStrokeList();
            ZName constantName = factory.createZName(constant, strokeList, constant);

            RefExpr constantRefExpr = factory.createRefExpr();

            constantRefExpr.setName(constantName);

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
		throw new IllegalArgumentException("An instance of a enumerated free type must be refined to PLType"); // Debo manejarlo mas arriba
	}
    }

    public static TCaseAssignment refineWithPLType(String specID, Expr expr, NodePLType implType, String implID){
        String ref = implID + " = ";
        ref = ref.concat(ConstantGenerator.PLTypeToString(implType.getType(),expr,implID) + ";");
        return new TCaseAssignment(specID, ref);
    }

    public static TCaseAssignment refineWithArray(String specID , Expr expr, NodeArray implType, String implID) throws IllegalArgumentException{
        String ref="";
        String fieldName;
        List<Expr> list = Utils.getListExpr(expr);
	// If this array have a variable that point to the next free position in the array
	// we must initialize it
	String lastPosPointerID = implType.getLastPosPointerID();
	if(lastPosPointerID!=null)
		ref += lastPosPointerID + " = "+ list.size()+";\n";


        for (int i=0; i<list.size(); i++){
            String head = implID + "[" + i + "]";
            fieldName="field" + (new Integer(i)).toString();
	    // Aca podria haber un problema. Debo analizar refineSubExpr()
            ref = ref.concat(refineSubExpr(head,implType.getType(),list.get(i),i,fieldName,implID));
        }
        return new TCaseAssignment(specID, ref);
    }

    public static TCaseAssignment refineWithStruc(String specID, Expr expr, NodeStructure implType, String implID) throws IllegalArgumentException{
        String ref="";
        List<NodeElement> elements = implType.getElements();
        List<Expr> list = Utils.getListExpr(expr);
        for (int i=0;i<list.size();i++){
            Expr elem=list.get(i);
            String head = implID + "." + (elements.get(i)).getID();
	    // Aca podria haber un problema. Debo analizar refineSubExpr()
            ref = ref.concat(refineSubExpr(head,(elements.get(i)).getType(), elem,i,(elements.get(i)).getID(),head));
        }
        return new TCaseAssignment(specID, ref);
    }

    public static TCaseAssignment refineWithList(String specID, Expr expr, NodeList implType, String implID) throws IllegalArgumentException{
        String ref="";
        String name = implType.getName();
        String linkType = implType.getLinkType();
        String linkNext = implType.getLinkNextName();
        String linkPrev = implType.getLinkPrevName();
        String mem = implType.getMemAlloc();



        List<NodeElement> fields = implType.getFields();
        List<Expr> list = Utils.getListExpr(expr);

	// No entiendo para que es...???
        String refNameWithoutPointer=implID.replace("*", "");
        String rnwop=refNameWithoutPointer;

        //Cada elemento de list tiene que ser un nodo de la lista
        if (list.size() == 0){
	    ref = rnwop+" = NULL;\n";
            return new TCaseAssignment(specID, ref);
        }

        ref=ref.concat("struct " + name + " *" + rnwop + "_prev = NULL;\n");

        for (int i=0; i<list.size();i++){
	    // If this is the first iteration we must allocate memory for the node, otherwise
	    // the allocation will be done in other part of the code
	    if(i==0){
            ref=ref.concat("struct " + name + " *" + rnwop + "_node" + i + " = ");
            if (mem==null){
                //Usa forma convencional para pedir memoria
                ref=ref.concat("malloc(sizeof(struct " + name + "));\n");
            }
            else{
                //Define la forma de pedir memoria el tester
                 ref=ref.concat(mem.substring(1, mem.length()-1) + ";\n");
            }
	    }

            Expr nodo = list.get(i);

            List<Expr> sublist = Utils.getListExpr(nodo);

            //Cada elemento de sublist es un campo de un nodo

            if ((linkType.equals("CLL") || linkType.equals("DCLL")) && (sublist.size() > 0))
                if (i==0){
                //Si es circular, al principio, el primero apunta a si mismo
                ref=ref.concat(rnwop + "_prev = " + rnwop + "_node0;\n");
		}

            for (int j=0; j<sublist.size();j++){
                Expr campo=sublist.get(j);
                String head = rnwop + "_node" + i + "->" + (fields.get(j)).getID();
		// Aca podria haber un problema. Debo analizar refineSubExpr()
		String id = implID+"."+(fields.get(j)).getID();
		String refined = refineSubExpr(head,(fields.get(j)).getType(),campo,i,(fields.get(j)).getID(),id);
		//System.out.println("Esto me devuelve el refineSubExpr:\n"+refined);
                ref=ref.concat(refined);
            }
            if (linkType.equals("DLL") || linkType.equals("DCLL")){
                //Si es doblemente enlazada, se linkea el PREV
                ref=ref.concat(rnwop + "_node" + i + "->" + linkPrev + " = " + rnwop + "_prev;\n");
                if (linkType.equals("DCLL"))
                    //si es circular el PREV del primero apunta al último
                    ref=ref.concat(rnwop + "_node0->" + linkPrev + " = " + rnwop + "_node" + i + ";\n");
            }
            if (linkType.equals("CLL") || linkType.equals("DCLL"))
                //Si es cirular, el ultimo siempre apunta al primero
                ref=ref.concat(rnwop + "_node" + i + "->" + linkNext + " = " + rnwop + "_node0;\n");
            else{
		if(i==list.size()-1)
                	ref=ref.concat(rnwop + "_node" + i + "->" + linkNext + " = NULL;\n");
		else{
			ref=ref.concat("struct " + name + " *" + rnwop + "_node" + (i+1) + " = ");
			if (mem==null){
				//Usa forma convencional para pedir memoria
				ref=ref.concat("malloc(sizeof(struct " + name + "));\n");
			}
			else{
				//Define la forma de pedir memoria el tester
				ref=ref.concat(mem.substring(1, mem.length()-1) + ";\n");
			}

			ref=ref.concat(rnwop + "_node" + i + "->" + linkNext + " = "+rnwop + "_node" + (i+1) +";\n");
		}
	    }
            //ref=ref.concat(rnwop + "_prev = " + rnwop + "_node" + i +";\n\n");
            //ref=ref.concat(rnwop + "_prev->" + linkNext + " = " + rnwop + "_node" + i + ";\n");
        }
	// Esto lo cambie ahora porque no compilaba
	// MODIFICADO
        //ref=ref.concat(implID + " = *" + rnwop + "_node0;\n" );
        ref=ref.concat(implID + " = " + rnwop + "_node0;\n" );
        return new TCaseAssignment(specID, ref);
    }


    public static TCaseAssignment refineWithFile(String specID, Expr expr, NodeFile implType, String implID) throws IllegalArgumentException{
        String path = (implType.getPath()).substring(1, (implType.getPath()).length()-1);
        String name = (implType.getName()).substring(1, (implType.getName()).length()-1);
        String fileName = "file_" + name.replace(".", ""); //El quito el punto de la ext, si tiene
        String del = implType.getDelimiter();
        String structure = implType.getStructure();
	String eol = implType.getEol();
	String eof = implType.getEof();

        String ref = "FILE *" + fileName + ";\n";
        ref = ref.concat(fileName + " = fopen(\"" + path + name + "\", \"w\");\n");
        ref = ref.concat("if (" + fileName + " == NULL) {printf(\"OPEN ERROR!\"); exit(-1);}\n");

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
        ref=ref.concat("if (fprintf(" + fileName + ", \"%s\", \"" + fileContent + "\") < 0) " +
                    "{printf(\"WRITE ERROR!\"); exit(-1);}\n");
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

    public static String refineSubExpr(String head, NodeType type, Expr expr, int nestLevel, String fieldName, String implID)
    {
        if ((expr instanceof RefExpr) || (expr instanceof NumExpr) || (expr instanceof ApplExpr)){
		// We contemplate the case that expr is equal to the empty set
		if(expr instanceof RefExpr){
			RefExpr refExpr = (RefExpr) expr;
			String strRefExpr = refExpr.getZName().getWord();
			if(strRefExpr.equals(UtilSymbols.emptySetSymbol()))
				return "";
			//	return head + " = NULL;\n";
			// Ver que hacer en otros casos!!!
		}
            if (type instanceof NodePLType) // No se si es correcto head
                return head.concat(" = " + ConstantGenerator.PLTypeToString(((NodePLType)type).getType(), expr,implID) + ";\n");
            else if (type instanceof NodeStructure){
                return ConstantGenerator.valExprToStruct(head,(NodeStructure)type,expr);
            }
            else if (type instanceof NodePointer){
                return ConstantGenerator.pointerToPLType(head,(NodePLType)(((NodePointer)type).getType()),expr,implID);
            }
            else if (type instanceof NodeEnumeration){
			NodeEnumeration nodeEnum = (NodeEnumeration) type;
			NodeType enumerationType = nodeEnum.getType();
			if(enumerationType instanceof NodePLType){
			String pltype = ((NodePLType) enumerationType).getType();
		
			if(expr instanceof RefExpr){
			RefExpr refExpr = (RefExpr) expr;
			ZName elementZName = refExpr.getZName();
			String element = elementZName.getWord();
			String constant = nodeEnum.getConstant(element);
			if (constant != null) {
			constant = constant.trim();
		
			//CREATE a NEW RefExpr with the associated CONSTANT
			ZFactory factory = new ZFactoryImpl();
		
			ZStrokeList strokeList = factory.createZStrokeList();
			ZName constantName = factory.createZName(constant, strokeList, constant);
		
			RefExpr constantRefExpr = factory.createRefExpr();
		
			constantRefExpr.setName(constantName);
			String constantGenerated = ConstantGenerator.PLTypeToString(pltype,
				constantRefExpr,implID);

			ConstantStore constantStore = ConstantStore.getInstance();
			constantStore.set(implID, constant,expr);

			return head+" = "+constantGenerated+";\n";

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
            else
                throw new IllegalArgumentException();
        }
        else{
            List<Expr> list = Utils.getListExpr(expr);
            String ref="";
            Expr elem;
            if (type instanceof NodePointer){
                //ARREGLAR!!!
                ref=ref.concat(refineSubExpr(head, ((NodePointer)type).getType(), expr, nestLevel + 1, fieldName,implID));
                return ref;
            }
            if (type instanceof NodeArray){
		String lastPosPointerID = ((NodeArray) type).getLastPosPointerID();
		    ref += lastPosPointerID +"["+nestLevel+ "] = "+list.size()+";\n";
                for (int i=0; i<list.size(); i++){
                    elem=list.get(i);
                    String subHead = head.concat("[" + i + "]");
                    fieldName.concat((new Integer(i)).toString());
                    ref = ref.concat(refineSubExpr(subHead,((NodeArray)type).getType(),elem,nestLevel + 1,fieldName,implID));
                }
                return ref;
            }
            else if (type instanceof NodeStructure){
                List<NodeElement> fields = ((NodeStructure)type).getElements();
                for (int i=0; i<list.size(); i++){
                    elem=list.get(i);
                    NodeElement field = fields.get(i);
                    String subHead = head.concat("." + field.getID());
                    ref = ref.concat(refineSubExpr(subHead,(fields.get(i)).getType(),elem,nestLevel + 1,(fields.get(i)).getID(),implID));
                }
                return ref;
            }
            else if (type instanceof NodeList){
                if (list.size() == 0) return "NULL";
                NodeList nodeList = (NodeList)type;
                String name=nodeList.getName();
                String linkType = nodeList.getLinkType();
                String linkNext = nodeList.getLinkNextName();
                String linkPrev = nodeList.getLinkPrevName();
                String mem = nodeList.getMemAlloc();
                List<NodeElement> fields = nodeList.getFields();
                String nestLevelString = (new Integer(nestLevel)).toString();
                ref=ref.concat("struct " + name + " *" + fieldName + "_prev" + nestLevelString + " = NULL;\n");
                for (int i=0; i<list.size();i++){
                    ref=ref.concat("struct " + name + " *" + fieldName + "_node" + nestLevelString + "_" + i + " = ");
                    if (mem==null){
                        //Usa forma convencional para pedir memoria
                        ref=ref.concat("malloc(sizeof(struct " + name + "));\n");
                    }
                    else{
                        //Define la forma de pedir memoria el tester
                        ref=ref.concat(mem.substring(1, mem.length()-1) + "\n");
                    }

                    if ((linkType.equals("CLL") || linkType.equals("DCLL")) && (list.size() > 0))
                        //Si es circular, al principio, el primero apunta a si mismo
                        if (i==0)
                            ref=ref.concat(fieldName + "_prev" + nestLevelString + " = " + fieldName + "_node" + nestLevelString + "_0;\n");

                    Expr nodo=list.get(i);

                    List<Expr> sublist = Utils.getListExpr(nodo);
                    //Cada elemento de sublist es un campo de un nodo
                    for (int j=0; j<sublist.size();j++){
                        String subhead = fieldName + "_node" + nestLevelString + "_" + i + "->" + (fields.get(j)).getID();
                        Expr campo=sublist.get(j);
                        ref=ref.concat(refineSubExpr(subhead,(fields.get(j)).getType(),campo,nestLevel + 1,(fields.get(j)).getID(),implID));
                    }
                    if (linkType.equals("DLL") || linkType.equals("DCLL")){
                        //Si es doblemente enlazada, se linkea el PREV
                        ref=ref.concat(fieldName + "_node" + nestLevelString + "_" + i + "->" + linkPrev + " = " + fieldName + "_prev" + nestLevelString + ";\n");
                        if (linkType.equals("DCLL"))
                            //si es circular el PREV del primero apunta al último
                            ref=ref.concat(fieldName + "_node" + nestLevelString + "_0->" + linkPrev + " = " + fieldName + "_node" + nestLevelString + "_" + i + ";\n");
                    }
                    if (linkType.equals("CLL") || linkType.equals("DCLL"))
                        //Si es cirular, el ultimo siempre apunta al primero
                        ref=ref.concat(fieldName + "_node" + nestLevelString + "_" + i + "->" + linkNext + " = " + fieldName + "_node" + nestLevelString + "_0;\n");
                    else //si no, el ultimo apunta a NULL
                        ref=ref.concat(fieldName + "_node" + nestLevelString + "_" + i + "->" + linkNext + " = NULL;\n");

                    ref=ref.concat(fieldName +"_prev" + nestLevelString + " = " + fieldName + "_node" + nestLevelString + "_" + i + ";\n\n");
                    if (i!=0)
                        ref=ref.concat(fieldName + "_prev" + nestLevelString + "->" + linkNext + " = " + fieldName + "_node" + nestLevelString + "_" + i + ";\n");
                    
                }
                ref=ref.concat(head + " = *" + fieldName + "_node" + nestLevelString + "_0;\n" );
                return ref;
            }
/*
            else if (type instanceof NodeFile){

            }

            else if (type instanceof NodeDB){

            }
*/
            else{ //Si llego acá hay un erro... es para probar
                System.out.println("EN EL ELSE, TIPO: ");
                Utils.printType(type);
		System.out.println(SpecUtils.termToLatex(expr));
                return "ALGO!!";
            }
        }
    }

}
