package client.blogic.testing.refinement.java;

import java.util.*;

import net.sourceforge.czt.base.ast.ListTerm;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.TupleExprImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import client.blogic.testing.refinement.java.*;
import client.blogic.testing.refinement.TCaseAssignment;

/**
 * Represents the MODULE of REFINAMENT to JAVA.
 * 
 * @author  Pablo D. Coca
 *
 * @since   v2.0
 *
 * @version 2.0
 */
public class RefineExpr {

    static List<TCaseAssignment> assignments = new ArrayList<TCaseAssignment>();
    static int elementsIndex;

    public static List<TCaseAssignment> refineExprList(RuleRefinement rule, ZExprList exprList) throws IllegalArgumentException {

        Set<String> implIDs = rule.getImplIDs();
        Iterator<String> iterImplIDs = implIDs.iterator();

        // elementsIndex	=	0;
        assignments.clear();

        // ITERATE in each IMPLEMENTATION ID of the RULES
        while (iterImplIDs.hasNext()) {
            elementsIndex = 0;	// AGREGADO
            String implID = iterImplIDs.next();
            NodeType nodeType = rule.getNodeType(implID);

            int exprListIndex;
            int size = exprList.size();

            for (exprListIndex = 0; exprListIndex < size; exprListIndex++) {
                Expr expr = exprList.get(exprListIndex);

                selectExprInstance(implID, nodeType, rule, expr);

                elementsIndex++;
            }
        }
        return assignments;
    }

/**
* This functions analyzes the type of the expresions in the specification and call the
* the appropiate function to obtain the assignments
*/
    public static void selectExprInstance(String implID, NodeType nodeType, RuleRefinement rule, Expr expr) {
        // Expr is a: "NumExpr"
        if (expr instanceof NumExpr) {
            assignments.add(refineExpr(implID,nodeType,rule,expr));
        }
        // Expr is a: "RefExpr"
        else if (expr instanceof RefExpr) {
	    // Entiendo que chequea que no se trate ni del conjunto vacio
	    // ni de la secuencia vacia. En ese caso manda a refinar.
            RefExpr refExpr = (RefExpr) expr;
            ZName zname = refExpr.getZName();
            String zNameString = zname.getWord();
            int[] opCode = RefineAST.getOperatorCode(zNameString);

            // EXCEPTION CODES LIST
            final int[] emptySetCode = {8709, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            final int[] seqCode = {10216, 32, 44, 44, 32, 10217, 0, 0, 0, 0};

            // EXCEPTION LIST
            final int[][] exceptionCodeList = {emptySetCode,
                seqCode
            };

            boolean exceptionCheck = RefineAST.checkOperatorCode(opCode,
                    exceptionCodeList);
            if (exceptionCheck == false) {
                assignments.add(refineExpr(implID, nodeType, rule, expr));
            }
        } 
        // Expr is a: "ApplExpr"
        else if (expr instanceof ApplExpr) {
            ApplExpr applExpr = (ApplExpr) expr;
            ListTerm<Expr> termList = applExpr.getExpr();

            Expr exprInsideAppl;

            Iterator<Expr> applIt = termList.iterator();

            while (applIt.hasNext()) {
                exprInsideAppl = applIt.next();
                selectExprInstance(implID, nodeType, rule, exprInsideAppl);
                elementsIndex++;
            }
            elementsIndex = 0;
        } 
        // is a TUPLE
        else if (expr instanceof TupleExpr) {
	    // No se considera el caso del producto cartesiano
            TupleExpr tupleExpr = (TupleExpr) expr;

            TupleExprImpl tuple = (TupleExprImpl) tupleExpr;

            ZExprList tupleComponents = tuple.getZExprList();

            Expr tupleFirstComponent;
            Expr tupleSecondComponent;

            tupleFirstComponent = tupleComponents.get(0);
            tupleSecondComponent = tupleComponents.get(1);

            Abstraction abstraction = rule.getAbstraction();

            ArrayList<String> specIDs = rule.getSpecIDs();

            // OJO SOLO TOMO el PRIMERO porque se estima que una LEY
            // de este tipo NO tendria que tener mas de un specID //Ej: cajas
	    // Esto es una limitacion. Podria darse
            String specID = specIDs.get(0);

	    // Podria refinarse a una base de datos o a un archivo. No necesariamente con
	    // codigo de abstraccion
            List<Equality> equalities = abstraction.getEqualities(specID);

            int equalitiesIndex;

            for (equalitiesIndex = 0; equalitiesIndex < equalities.size(); equalitiesIndex++) {
                Equality equality = equalities.get(equalitiesIndex);

                String preOperator = equality.getPreOperatorSpecID();

                String plCode = equality.getPLCodeEquality();
                plCode = plCode.trim();

                // Mejorar este CHEQUEO
                boolean check = plCode.contains(implID.subSequence(0,
                        implID.length()));
                if (check) {
                    if (preOperator == "dom") {
                        assignments.add(refineExpr(implID,
                                nodeType,
                                rule,
                                tupleFirstComponent));

                        continue;
                    } else if (preOperator == "ran") {
                        assignments.add(refineExpr(implID,
                                nodeType,
                                rule,
                                tupleSecondComponent));
                        continue;
                    }
                }
            }
        }
        // Expr is a: "SetExpr"
        else if (expr instanceof SetExpr) {
            elementsIndex = 0;	// AGREGADO

            SetExpr setExpr = (SetExpr) expr;
            ZExprList zExprList = setExpr.getZExprList();
            Expr exprInsideSet;

            ListIterator<Expr> setIt = zExprList.listIterator();

            while (setIt.hasNext()) {
                exprInsideSet = setIt.next();

                selectExprInstance(implID, nodeType, rule, exprInsideSet);

                elementsIndex++;
            }

            elementsIndex = 0;

        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        // FALTARIA AGREGAR los diferentes TIPOS que pueden haber.
        // Por EJEMPLO ver como se representan:
        // las SECUENCIAS
        //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        else {
            throw new IllegalArgumentException();
        }
        //--------------------------------------------------------------------------------

    }

    public static TCaseAssignment refineExpr(String implID, NodeType nodeType, RuleRefinement rule, Expr expr) throws IllegalArgumentException {
        TCaseAssignment assignment;

        if (nodeType instanceof NodePLType) {
            assignment = refineWithPLType(implID,nodeType,rule,expr);
        } else if (nodeType instanceof NodeEnumeration) {
            assignment = refineWithNodeEnumeration(implID,nodeType,rule,expr);
        } else if (nodeType instanceof NodePointer) {
            assignment = refineWithPointer(implID,nodeType,rule,expr);
        } else if (nodeType instanceof NodeArray) {
            assignment = refineWithArray(implID,nodeType,rule,expr);
        } // El PROXIMO que hay que hacer
        //    	else if (nodeType instanceof NodeStructure)
        //    	{
        //    		assignments.add(refineWithStructure(rule, exprList.get(0)));
        //    	}
        //    	else if (nodeType instanceof NodeList)
        //    	{
        //    		assignments.add(refineWithList(rule, exprList.get(0)));
        //    	}
        //    	else if (nodeType instanceof NodeFile)
        //    	{
        //    		assignments.add(refineWithFile(rule, exprList.get(0)));
        //    	}
        //    	else if (nodeType instanceof NodeDB)
        //    	{
        //    		assignments.add(refineWithDB(rule, exprList.get(0)));
        //    	}
        else {
            throw new IllegalArgumentException();
        }

        return assignment;

    }
    // Deberia pasar tambien el SPECID
    public static TCaseAssignment refineWithPLType(String implID, NodeType nodeType, RuleRefinement rule, Expr expr) {
	// Aca se asume que se puede refinar a un tipo "basico" del lenguaje de programacion
	// una y solo una variable de la especificacion. Me parece razonable.
        String ref = implID + " = ";
        String plTypeType = ((NodePLType) nodeType).getType();
        String constantGenerated = ConstantGenerator.PLTypeToString(plTypeType, expr);
        ref = ref.concat(constantGenerated + ";");
        return new TCaseAssignment((rule.getSpecIDs()).get(0), ref);	// CORREGIR para VARIOS SpecIDs, no solo el primero!!!
	// Por lo dicho anteriormente me parece que no hay demasiado para corregir
    }

    public static TCaseAssignment refineWithNodeEnumeration(String implID, NodeType nodeType, RuleRefinement rule, Expr expr) {
	// Mapea.
        String ref = implID + " = ";

        NodeType enumerationType = ((NodeEnumeration) nodeType).getType();
        String pltype = ((NodePLType) enumerationType).getType();
        // GET the CONSTANT
        RefExpr refExpr = (RefExpr) expr;
        ZName elementZName = refExpr.getZName();
        String element = elementZName.getWord();
        String constant = ((NodeEnumeration) nodeType).getConstant(element);
        if (constant != null) {
            constant = constant.trim();

            //CREATE a NEW RefExpr with the associated CONSTANT
            ZFactory factory = new ZFactoryImpl();

            ZStrokeList strokeList = factory.createZStrokeList();
            ZName constantName = factory.createZName(constant, strokeList, constant);

            RefExpr constantRefExpr = factory.createRefExpr();

            constantRefExpr.setName(constantName);
            String constantGenerated = ConstantGenerator.PLTypeToString(pltype,
                    constantRefExpr);

            ref = ref.concat(constantGenerated + ";");

            return new TCaseAssignment((rule.getSpecIDs()).get(0), ref);	// CORREGIR para VARIOS SpecIDs, no solo el primero!!!
        } else {
            UtilsJava.printErrorEnumerationElementNotSpecified(element);
            throw new IllegalArgumentException();
        }
    }

    public static TCaseAssignment refineWithPointer(String implID, NodeType nodeType, RuleRefinement rule, Expr expr) throws IllegalArgumentException {
	// Esta funcion es solo para C.
	// Igual que en el codigo de Diego sigue la restriccion de las variables
        String ref = "";

        NodeType pointerType = ((NodePointer) nodeType).getType();

        if (pointerType instanceof NodePLType) {
            // Debo mandar directo a la Funcion PLTypeToString

            NodePLType plType = (NodePLType) pointerType;

            if (plType.getType().contains("char")) {
                ref = implID + " = ";

                ref = ref.concat(ConstantGenerator.PLTypeToString(plType.getType(),
                        expr) + ";");
            } else {
                ref = "*" + implID + " = ";

                ref = ref.concat(ConstantGenerator.PLTypeToString(plType.getType(),
                        expr) + ";");
            }
        } else
        {
            String fieldName;

            String head = implID + "[" + 0 + "]";

            fieldName = "field" + (new Integer(0)).toString();

            ref = ref.concat(refineSubExpr(head, 0, fieldName, expr, pointerType, rule));
        }

        return new TCaseAssignment((rule.getSpecIDs()).get(0), ref);	// CORREGIR para VARIOS SpecIDs, no solo el primero!!!

    }

    public static TCaseAssignment refineWithArray(String implID, NodeType nodeType, RuleRefinement rule, Expr expr) throws IllegalArgumentException {
        String ref = "";
        String fieldName;
        String head = implID + "[" + elementsIndex + "]";
	// EXAMPLE: agenda[0]
        NodeType arrayType = ((NodeArray) nodeType).getType();
        fieldName = "field" + (new Integer(elementsIndex)).toString();
        ref = ref.concat(refineSubExpr(head,elementsIndex,fieldName,expr,arrayType,rule));
        return new TCaseAssignment((rule.getSpecIDs()).get(0), ref);	
	// CORREGIR para VARIOS SpecIDs, no solo el primero!!!
    }

    public static String refineSubExpr(String head, int nestLevel, String fieldName, Expr expr, NodeType type, RuleRefinement rule) {
        if ((expr instanceof RefExpr) ||
                (expr instanceof NumExpr)) {
            if (type instanceof NodePLType) {
                String PLTypeType = ((NodePLType) type).getType();

                String constantGenerated = ConstantGenerator.PLTypeToString(PLTypeType, expr);

                return head.concat(" = " +
                        constantGenerated +
                        ";" // ";\n"
                        );
            } //--------------------------------------------------------------------------------
            else if (type instanceof NodePointer) {
                return ConstantGenerator.pointerToPLType(head, (NodePLType) (((NodePointer) type).getType()), expr);
            } //--------------------------------------------------------------------------------
            else if (type instanceof NodeStructure) {
                return ConstantGenerator.valExprToStruct(head, (NodeStructure) type, expr);
            } //--------------------------------------------------------------------------------
            else {
                throw new IllegalArgumentException();
            }
            //--------------------------------------------------------------------------------
        } //--------------------------------------------------------------------------------
        //        if ( expr instanceof TupleExpr )
        //        {
        //        	//--------------------------------------------------------------------------------
        //            if (type instanceof NodePLType)
        //            {
        //            	String	PLTypeType			= ((NodePLType) type).getType();
        //
        //            	String	constantGenerated	= ConstantGenerator.PLTypeToString(PLTypeType, expr);
        //
        //            	head.concat	(	" = " 				+
        //            					constantGenerated	+
        //            					";\n"
        //            				);
        //
        //            	return head.concat(" = " + ConstantGenerator.PLTypeToString(((NodePLType)type).getType(), expr) + ";\n");
        //            }
        //            else
        //            {
        //                throw new IllegalArgumentException();
        //            }
        //
        //        }
        // Hay que agregar el caso cuando expr es de tipo TupleExpr
        else {
            List<Expr> list = UtilsJava.getListExpr(expr);	// Ver que HACE !!!

            String ref = "";

            Expr elem;
            //--------------------------------------------------------------------------------
            if (type instanceof NodePointer) {
                //ARREGLAR!!!
                ref = ref.concat(refineSubExpr(head,
                        nestLevel + 1,
                        fieldName,
                        expr,
                        ((NodePointer) type).getType(),
                        rule));

                return ref;
            } //--------------------------------------------------------------------------------
            else if (type instanceof NodeArray) {
                for (int i = 0; i < list.size(); i++) {
                    elem = list.get(i);

                    String subHead = head.concat("[" + i + "]");

                    fieldName.concat((new Integer(i)).toString());

                    ref = ref.concat(refineSubExpr(subHead,
                            nestLevel + 1,
                            fieldName,
                            elem,
                            ((NodeArray) type).getType(),
                            rule));
                }

                return ref;
            } //--------------------------------------------------------------------------------
            else if (type instanceof NodeStructure) {
                List<NodeElement> elements = ((NodeStructure) type).getElements();

                for (int i = 0; i < list.size(); i++) {
                    elem = list.get(i);
                    NodeElement element = elements.get(i);
                    String elementID = element.getID();

                    String subHead = head.concat("." + elementID);

                    ref = ref.concat(refineSubExpr(subHead,
                            nestLevel + 1,
                            //(elements.get(i)).getID()
                            elementID,
                            elem,
                            (elements.get(i)).getType(),
                            rule));
                }

                return ref;
            } //--------------------------------------------------------------------------------
            else if (type instanceof NodeList) {
                if (list.size() == 0) {
                    return "NULL";
                }

                NodeList nodeList = (NodeList) type;
                String name = nodeList.getName();
                String linkType = nodeList.getLinkType();
                String linkNext = nodeList.getLinkNextName();
                String linkPrev = nodeList.getLinkPrevName();
                String mem = nodeList.getMemAlloc();
                List<NodeElement> fields = nodeList.getFields();
                String nestLevelString = (new Integer(nestLevel)).toString();
                ref = ref.concat("struct " + name + " *" + fieldName + "_prev" + nestLevelString + " = NULL;\n");

                for (int i = 0; i < list.size(); i++) {
                    ref = ref.concat("struct " + name + " *" + fieldName + "_node" + nestLevelString + "_" + i + " = ");
                    if (mem == null) {
                        //Usa forma convencional para pedir memoria
                        ref = ref.concat("malloc(sizeof(struct " + name + "));\n");
                    } else {
                        //Define la forma de pedir memoria el tester
                        ref = ref.concat(mem.substring(1, mem.length() - 1) + "\n");
                    }

                    if ((linkType.equals("CLL") || linkType.equals("DCLL")) && (list.size() > 0)) //Si es circular, al principio, el primero apunta a si mismo
                    {
                        if (i == 0) {
                            ref = ref.concat(fieldName + "_prev" + nestLevelString + " = " + fieldName + "_node" + nestLevelString + "_0;\n");
                        }
                    }

                    Expr nodo = list.get(i);

                    List<Expr> sublist = UtilsJava.getListExpr(nodo);
                    //Cada elemento de sublist es un campo de un nodo
                    for (int j = 0; j < sublist.size(); j++) {
                        String subhead = fieldName + "_node" + nestLevelString + "_" + i + "->" + (fields.get(j)).getID();
                        Expr campo = sublist.get(j);
                        ref = ref.concat(refineSubExpr(subhead,
                                nestLevel + 1,
                                (fields.get(j)).getID(),
                                campo,
                                (fields.get(j)).getType(),
                                rule));
                    }
                    if (linkType.equals("DLL") || linkType.equals("DCLL")) {
                        //Si es doblemente enlazada, se linkea el PREV
                        ref = ref.concat(fieldName + "_node" + nestLevelString + "_" + i + "->" + linkPrev + " = " + fieldName + "_prev" + nestLevelString + ";\n");
                        if (linkType.equals("DCLL")) //si es circular el PREV del primero apunta al último
                        {
                            ref = ref.concat(fieldName + "_node" + nestLevelString + "_0->" + linkPrev + " = " + fieldName + "_node" + nestLevelString + "_" + i + ";\n");
                        }
                    }
                    if (linkType.equals("CLL") || linkType.equals("DCLL")) //Si es cirular, el ultimo siempre apunta al primero
                    {
                        ref = ref.concat(fieldName + "_node" + nestLevelString + "_" + i + "->" + linkNext + " = " + fieldName + "_node" + nestLevelString + "_0;\n");
                    } else //si no, el ultimo apunta a NULL
                    {
                        ref = ref.concat(fieldName + "_node" + nestLevelString + "_" + i + "->" + linkNext + " = NULL;\n");
                    }

                    ref = ref.concat(fieldName + "_prev" + nestLevelString + " = " + fieldName + "_node" + nestLevelString + "_" + i + ";\n\n");
                    if (i != 0) {
                        ref = ref.concat(fieldName + "_prev" + nestLevelString + "->" + linkNext + " = " + fieldName + "_node" + nestLevelString + "_" + i + ";\n");
                    }

                }

                ref = ref.concat(head + " = *" + fieldName + "_node" + nestLevelString + "_0;\n");

                return ref;
            } else {
                System.out.println("INVALID NodeType: ");

                UtilsJava.printType(type);

                return "ALGO!!";
            }
        }
    }
}
