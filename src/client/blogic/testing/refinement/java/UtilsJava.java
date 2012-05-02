package client.blogic.testing.refinement.java;

//import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import client.blogic.testing.refinement.ConcreteTCase;
import client.blogic.testing.refinement.TCaseAssignment;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;

//import common.z.CztPrinter;

import client.blogic.testing.refinement.java.*;

import client.blogic.testing.refinement.java.TCRL_Parser.*;

public class UtilsJava {

    public static List<Expr> getListExpr(Expr expr)
            throws IllegalArgumentException {

        List<Expr> list = new ArrayList<Expr>();


        //--------------------------------------------------------------------------------
        if (expr instanceof NumExpr) {
            list.add(expr);
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        else if (expr instanceof RefExpr) {
            list.add(expr);
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        else if (expr instanceof SetExpr) {
            list = ((SetExpr) expr).getZExprList();

//            System.out.println("Refinar un conjunto");
//            ZExprListImpl setList = (ZExprListImpl) ((SetExpr)expr).getZExprList();
//            System.out.println("setList size: " + setList.size());
//            for (int i=0;i<setList.size();i++){
//                list.add(setList.get(i));
//                System.out.println("Agregar a lista elemento de un conjunto:");
//                CztPrinter.printExpr(list.get(i), i);
//                System.out.println("FIN");
//            }
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        else if (expr instanceof TupleExpr) {
            list = ((TupleExpr) expr).getZExprList();
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        else if (expr instanceof ApplExpr) {
//            System.out.println("Refinar una secuencia");

            ZExprListImpl applList = (ZExprListImpl) ((SetExpr) (((ApplExpr) expr).getRightExpr())).getZExprList();

            for (int i = 0; i < applList.size(); i++) {
                Expr elem = (((TupleExpr) (applList.get(i))).getZExprList()).get(1);

                list.add(elem);
//                System.out.println("Agregar elemento de una secuencia:");
//                CztPrinter.printExpr(elem, i);
//                System.out.println("FIN");
            }
        } //--------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------
        else {
            throw new IllegalArgumentException();
        }
        //--------------------------------------------------------------------------------


        return list;
    }

    public static void printRefFunction(TCRL_AST r) {

        System.out.println();
        System.out.println("**************************************************");
        System.out.println("RefFunction");
        System.out.println("**************************************************");
        System.out.println("Nombre: " + r.getName());
        System.out.println("Preambulo: " + r.getPreamble());
        System.out.println("Reglas: ");

        NodeRules rules = r.getRules();
        NodeRule rule;

        Set<String> keys = rules.getKeys();
        Iterator<String> iter = keys.iterator();

        while (iter.hasNext()) {
            rule = rules.getRule(iter.next());

            if (rule instanceof RuleSynonym) {
                printRuleSynonym((RuleSynonym) rule);
            } else {
                printRuleRefinement((RuleRefinement) rule);
            }
        }
    }

    public static void printRuleSynonym(RuleSynonym rule) {
        System.out.println();
        System.out.println("Sinonimo");
        System.out.println("Nombre: " + rule.getName());
        System.out.print("Tipo: ");
        printType(rule.getNodeType());
        //printType(rule.getType()); CHANGE
    }

    public static void printRuleRefinement(RuleRefinement rule) {
        if (rule == null) {
            System.out.println("Rule NULL!!!");
        }
        System.out.println();
        System.out.println("Refinamiento");
        System.out.println("Los nombres de los identificadores son:");
        List<String> idsList = rule.getSpecIDs();
        for (int i = 0; i < idsList.size(); i++) {
            System.out.println("id: " + idsList.get(i));
        }
        //System.out.println("Nombre en Especificaión: " + rule.getSpecName()); CHANGE
        //System.out.println("Nombre en Implementación: " + rule.getRefName()); CHANGE
        // HACER ANALOGO AL ANTERIOR
        //System.out.print("Tipo: ");
        //printType(rule.getNodeType()); CHANGE
        // HACER ANALOGO ANTERIOR
    }

    public static void printType(NodeType type) {
        if (type instanceof NodeSynonym) {
            System.out.println("NodeSynonym: " + ((NodeSynonym) type).getID());
        } // System.out.println("NodeSynonym: " + ((NodeSynonym)type).getType());
        else if (type instanceof NodePLType) {
            System.out.println("PLType: " + ((NodePLType) type).getType());
        } else if (type instanceof NodePointer) {
            System.out.println("Puntero a");
            System.out.print("  ");
            printType(((NodePointer) type).getType());
        } else if (type instanceof NodeArray) {
            System.out.print("Arreglo de tamaño " + ((NodeArray) type).getSize() + " de tipo: ");
            printType(((NodeArray) type).getType());
        } else if (type instanceof NodeStructure) {
            NodeStructure structure = (NodeStructure) type;
            //List<NodeElement>	elements =	record.getElements(); CHANGE
            List<NodeElement> elements = structure.getElements();
            Iterator<NodeElement> iter = elements.iterator();

            //System.out.println("Estructura: " + "Nombre: " + record.getName()); CHANGE
            System.out.println("Estructura: " + "Nombre: " + structure.getName());

            int i = 1;
            NodeElement element;

            while (iter.hasNext()) {
                element = iter.next();

                System.out.println("Campo " + i + ": ");
                i++;

                //System.out.println("Nombre: " + (element.getName())); CHANGE
                System.out.println("Nombre: " + (element.getID()));
                System.out.print("Tipo: ");
                printType(element.getType());
            }
        } else if (type instanceof NodeList) {
            NodeList list = (NodeList) type;
            //List<NodeField>		fields	=	list.getFields(); CHANGE
            List<NodeElement> fields = list.getFields();
            //Iterator<NodeField> iter	= fields.iterator(); CHANGE
            Iterator<NodeElement> iter = fields.iterator();

            System.out.println("Lista");
            System.out.println("Nombre: " + list.getName());
            System.out.println("LinkType: " + list.getLinkType());
            System.out.println("LinkNexName: " + list.getLinkNextName());

            if (list.getLinkPrevName() != null) {
                System.out.println("LinkPrevName: " + list.getLinkPrevName());
            }

            int i = 1;

            NodeElement field;
            while (iter.hasNext()) {
                field = iter.next();
                System.out.println("Campo " + i + ":");
                i++;
                //System.out.println("Nombre: " + (field.getName())); CHANGE
                System.out.println("Nombre: " + (field.getID()));
                System.out.print("Tipo: ");
                printType(field.getType());
            }
            //if (list.getMemalloc() != null ) CHANGE
            if (list.getMemAlloc() != null) {
                System.out.println("MemallocFunction: ");
                //System.out.println(list.getMemalloc()); CHANGE
                System.out.println(list.getMemAlloc());
            }
        } else if (type instanceof NodeFile) {
            NodeFile file = (NodeFile) type;

            System.out.println("Archivo");
            System.out.println("Nombre: " + file.getName());
            System.out.println("Ruta: " + file.getPath());
            System.out.println("Delimitador: " + file.getDelimiter());

            if (file.getEof() != null) {
                System.out.println("EOF: " + file.getEof());
            }
            if (file.getEol() != null) {
                System.out.println("EOL: " + file.getEol());
            }
            if (file.getStructure() != null) {
                System.out.println("Estructura: " + file.getStructure());
            }
        } else if (type instanceof NodeDB) {
            NodeDB db = (NodeDB) type;
            List<NodeDBColumn> regs = db.getColumns();
            Iterator<NodeDBColumn> iter = regs.iterator();

            System.out.println("Base de Datos");
            System.out.println("DBMS: " + db.getDBMSID());
            //System.out.println("connectionID "		+ db.getConnID()); CHANGE
            System.out.println("connectionID " + db.getConnectionID());

            //System.out.println("Table name: "		+ db.getTable()); CHANGE
            System.out.println("Table name: " + db.getTableName());
            System.out.println("Registros:");

            int i = 1;

            while (iter.hasNext()) {
                NodeDBColumn rec = iter.next();
                System.out.println("Registro " + i + ": " + rec.getColType() + " tipo: " + rec.getColType());
                i++;
            }
        }
    }

    public static String cTypeToString(String ty, Expr expr) {
        if (ty.contains("int")) {
            return toInt(expr);
        }
        if (ty.contains("char")) {
            return toString(expr);
        }
        if (ty.contains("float")) {
            return toFloat(expr);
        }
        if (ty.contains("double")) {
            return toDouble(expr);
        }
        return null;
    }

    public static String toInt(Expr expr) {
        Integer i;
        if (expr instanceof RefExpr) {
            return stringToInt((((RefExpr) expr).getName()).toString());
        } else if (expr instanceof NumExpr) {
            i = new Integer((((NumExpr) expr).getNumeral()).toString());
            return i.toString();
        } else {
            return null;
        }

    }

    public static String toString(Expr expr) {
        if (expr instanceof RefExpr) {
            return ("\"" + ((((RefExpr) expr).getName()).toString()) + "\"");
        } else if (expr instanceof NumExpr) {
            return ("\"" + ((((NumExpr) expr).getNumeral()).toString()) + "\"");
        }
        return null;
    }

    public static String toFloat(Expr expr) {
        return null;
    }

    public static String toDouble(Expr expr) {
        return null;
    }

    public static String stringToInt(String str) {
        String strInt = "";
        char[] chrArr = str.toCharArray();
        for (int i = 0; i < chrArr.length; i++) {
            if (((int) chrArr[i]) < 10) {
                strInt.concat("00");
                Integer integer = new Integer(chrArr[i]);
                strInt = strInt.concat(integer.toString());
            } else if (((int) chrArr[i]) < 100) {
                strInt.concat("0");
                Integer integer = new Integer(chrArr[i]);
                strInt = strInt.concat(integer.toString());
            } else {
                Integer integer = new Integer(chrArr[i]);
                strInt = strInt.concat(integer.toString());
            }
        }
        return strInt;
    }

    /*
    public static String arrayToString(NodeArray array, Expr expr){
    String ref="";
    if (array.getType() instanceof NodeCType){
    return arrayCTypeToString(array, expr);
    }
    if (array.getType() instanceof NodeArray){
    if (expr instanceof SetExpr){



    ref=ref.concat("");
    }
    return arrayToString(((NodeArray)array.getType()), expr);
    }
    return ref;
    }

    public static String arrayCTypeToString(NodeArray array, Expr expr){
    ApplExpr applExpr=(ApplExpr)expr;
    if (applExpr.getRightExpr() instanceof SetExpr){
    ZExprListImpl list=(ZExprListImpl) (((SetExpr)expr).getZExprList());
    if (list.size() == 1)
    return "";
    else{
    String ref="";
    for (int i=0; i<list.size(); i++){
    if (list.get(i) instanceof RefExpr){
    }
    }
    }
    }

    return ref;
    }
     */
    public static String printCTC(String testName, ConcreteTCase ctc) {
        String ctcString = "";		// EMPTY STRING

        //--------------------------------------------------------------------------------
        // CONCATENATES the TESTNAME
        //--------------------------------------------------------------------------------
        ctcString = ctcString.concat("//--------------------------------------------------------------------------------" + "\n");
        ctcString = ctcString.concat(
                "// " + testName + "\n");
        ctcString = ctcString.concat("//--------------------------------------------------------------------------------" + "\n");
        ctcString = ctcString.concat("\n");
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        String preamble = ctc.getPreamble();
        String preambleWithOutPackageAndImports = "";

        BufferedReader preambleReader = new BufferedReader(new StringReader(preamble));

        CharSequence packageChar = new StringBuffer("package");
        CharSequence importChar = new StringBuffer("import");

        String preambleLine;
        try {
            while ((preambleLine = preambleReader.readLine()) != null) {
                if (preambleLine.contains(packageChar) || preambleLine.contains(importChar)) {
                    ctcString = ctcString.concat(preambleLine);
                    ctcString = ctcString.concat("\n");
                } else {
                    preambleWithOutPackageAndImports =
                            preambleWithOutPackageAndImports.concat(preambleLine) + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // PRINT the TEST CLASS HEADER
        //--------------------------------------------------------------------------------
        ctcString = ctcString.concat("\n");
        ctcString = ctcString.concat("public class ");
        ctcString = ctcString.concat(testName);			// SCHEMA NAME
        ctcString = ctcString.concat("\n");
        ctcString = ctcString.concat("{");
        ctcString = ctcString.concat("\n");
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // CONCATENATES the PREAMBLE WITHOUT PACKAGE and IMPORTS
        //--------------------------------------------------------------------------------
        ctcString = ctcString.concat(preambleWithOutPackageAndImports);
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // PRINT the RUNTEST CLASS HEADER
        //--------------------------------------------------------------------------------
        ctcString = ctcString.concat("\n");
        ctcString = ctcString.concat("\t");
        ctcString = ctcString.concat("public static void runTest_");
        ctcString = ctcString.concat(testName);			// SCHEMA NAME
        ctcString = ctcString.concat("_TCASE()");
        ctcString = ctcString.concat("\n");
        ctcString = ctcString.concat("\t");
        ctcString = ctcString.concat("{");
        ctcString = ctcString.concat("\n");
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // CONCATENATES the REFINEMENT TEXT of ALL ASSIGNMENTS
        //--------------------------------------------------------------------------------
        List<TCaseAssignment> assignments = ctc.getAssigments();
        Iterator<TCaseAssignment> iterAssignments = assignments.iterator();
        TCaseAssignment assignment;
        String assignmentRefText;

        while (iterAssignments.hasNext()) {
            assignment = iterAssignments.next();

            assignmentRefText = assignment.getRefText();

            // CONCATS the REFIMENT TEXT of ASSIGNMENT
            ctcString = ctcString.concat(
                    "\t\t" // INDENTATION
                    + assignmentRefText + "\n" + "\n");
        }
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // CONCATS the EPILOGUE
        //--------------------------------------------------------------------------------
        ctcString = ctcString.concat("\t\t" + "//--------------------------------------------------------------------------------" + "\n");
        ctcString = ctcString.concat("\t\t" + "//                                   EPILOGUE" + "\n");
        ctcString = ctcString.concat("\t\t" + "//--------------------------------------------------------------------------------" + "\n");

        String epilogue = ctc.getEpilogue();
        BufferedReader epilogueReader = new BufferedReader(new StringReader(epilogue));
        String epilogueLine;

        try {
            while ((epilogueLine = epilogueReader.readLine()) != null) {
                ctcString = ctcString.concat("\t\t" // INDENTATION
                        + epilogueLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // CLOSE the RUNTEST CLASS.
        //--------------------------------------------------------------------------------
        ctcString = ctcString.concat("\t");
        ctcString = ctcString.concat("}");
        ctcString = ctcString.concat("\n");
        //--------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------
        // CLOSE the TEST CLASS.
        //--------------------------------------------------------------------------------
        ctcString = ctcString.concat("}");
        //--------------------------------------------------------------------------------

        return ctcString;
    }

    public static void printErrorInvalidNumberOfTestCase(String arg) {
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("Must be only ONE TEST CASE in: " + arg);
        System.out.println("**************************************************");
    }

    /**
     * Prints on the "System.out" Stream that a specified VARIABLE has not been specified.

     * @author  Pablo D. Coca
     *
     * @since   v2.0
     * @version 2.0
     * @param		var		the VARIABLE not specified
     *
     * @return		void 
     * */
    public static void printErrorVariableNotSpecified(String var) {
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("The VARIABLE: " + var + " is not specified in the TCRL file.");
        System.out.println("**************************************************");
    }

    /**
     * Prints on the "System.out" stream that the ZTYPE of specID is not soported.
     * 
     * @author  Pablo D. Coca
     *
     * @since   v2.0
     * @version 2.0
     * @param		specIDZType		the ZTYPE not soported by TCRL
     *
     * @return		void
     * */
    public static void printSpecIDZTypeNotSoported(String specIDZType) {
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("The ZTYPE: " + specIDZType + " is not soported yet.");
        System.out.println("**************************************************");
    }

    /**
     * Prints on the "System.out" Stream that the ENUMERATION ELEMENT "element" from an
     * ENUMERATED Z TYPE has not been specified.
     * 
     * @author  Pablo D. Coca
     *
     * @since   v2.0
     * @version 2.0
     * @param		element		the ENUMERATION ELEMENT not specified
     *
     * @return		void
     * */
    public static void printErrorEnumerationElementNotSpecified(String element) {
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("The ENUMERATION ELEMENT: " + element + " is not specified in the TCRL file.");
        System.out.println("**************************************************");
    }

    public static void printErrorInvalidFormatArgumentforVariable(String arg) {
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("The FORMAT of ARGUMENT: " + arg + " is INVALID.");
        System.out.println("**************************************************");
    }

    public static void printErrorAbstractionNotSpecified(String specID, String ruleName) {
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("THE RULE: " + ruleName +
                ", with the VARIABLE: " + specID +
                ", MUST HAVE AN @ABSTRACTION SECTION.");
        System.out.println("**************************************************");
    }
}

