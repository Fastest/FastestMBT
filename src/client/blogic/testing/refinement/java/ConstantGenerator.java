package client.blogic.testing.refinement.java;

import java.util.*;

//import client.blogic.testing.refinement.ConcreteTCase;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.util.OperatorName;

/**
 * Represents the MODULE that GENERATES CONSTANT for the REFINEMENT process
 * 
 * @author  Pablo D. Coca
 *
 * @since   v1.0
 *
 * @version 2.0
 */
public class ConstantGenerator {

    public static String PLTypeToString(String type, Expr expr) throws IllegalArgumentException {
        if (type.equals("byte")) {
            return toByte(expr);
        } else if (type.equals("short")) {
            return toShort(expr);
        } else if (type.equals("int")) {
            return toInt(expr);
        } else if (type.equals("long")) {
            return toLong(expr);
        } else if (type.equals("float")) {
            return toFloat(expr);
        } else if (type.equals("double")) {
            return toDouble(expr);
        } else if (type.equals("char")) {
            return toChar(expr);
        } else if (type.equals("String")) // String is supported as PRIMITIVE TYPE
        {
            return toString(expr);
        } else if (type.equals("boolean")) {
            return toBoolean(expr);
        } else {
            throw new IllegalArgumentException();
        }

        //-------------------------------------------------
        // INFO:
        //-------------------------------------------------
        // JAVA - Primitive Data Types
        //-------------------------------------------------
        //    	Data Type	Default Value (for fields)
        //    	byte	0
        //    	short	0
        //    	int		0
        //    	long	0L
        //    	float	0.0f
        //    	double	0.0d
        //    	char	'\u0000'
        //    	String (or any object)  	null
        //    	boolean	false
        //--------------------------------------------------------------------------------
        //    	The String class is not technically a primitive data type,
        //    	but considering the special support given to it by the language,
        //    	you'll probably tend to think of it as such.
        //    	You'll learn more about the String class in Simple Data Objects
        //--------------------------------------------------------------------------------
        //    	You may have noticed that the new keyword isn't used
        //    	when initializing a variable of a primitive type.
        //    	Primitive types are special data types built into the language;
        //    	they are not objects created from a class.
        //--------------------------------------------------------------------------------
        //    	use 'single quotes' for char literals and "double quotes" for String literals.
        //    	The Java programming language also supports a few special escape sequences
        //    	for char and String literals:
        //    	\b (backspace), \t (tab), \n (line feed), \f (form feed), \r (carriage return),
        //    	\" (double quote), \' (single quote), and \\ (backslash).
        //--------------------------------------------------------------------------------
    }

    //--------------------------------------------------------------------------------
    // BYTE
    //--------------------------------------------------------------------------------
    public static String toByte(Expr expr) {
        Byte ret;
        String str = exprToString(expr);

        if (str.length() > 1) {
            String str2 = stringToByte(str);
            ret = new Byte(str2);
        } else {
            ret = new Byte(str);
        }

        return ret.toString();
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // SHORT
    //--------------------------------------------------------------------------------
    public static String toShort(Expr expr) {
        Short ret;
        String str = exprToString(expr);

        if (isShort(str) == true) {
            ret = new Short(str);
        } else {
            String generatedInt = stringToShort(str);
            ret = new Short(generatedInt);
        }

        return ret.toString();
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // INT
    //--------------------------------------------------------------------------------
    public static String toInt(Expr expr) {
        Integer ret;
        String str = exprToString(expr);

        if (isInt(str) == true) {
            ret = new Integer(str);
        } else {
            String generatedInt = stringToInt(str);
            ret = new Integer(generatedInt);
        }
        return ret.toString();
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // LONG
    //--------------------------------------------------------------------------------
    public static String toLong(Expr expr) {
        Long ret;
        String str = exprToString(expr);

        if (isLong(str) == true) {
            ret = new Long(str);
        } else {
            String generatedInt = stringToLong(str);
            ret = new Long(generatedInt);
        }

        return ret.toString();
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // FLOAT
    //--------------------------------------------------------------------------------
    public static String toFloat(Expr expr) {
        String str = exprToString(expr);

        Float ret = new Float(str);

        return (ret.toString() +
                "f");
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // DOUBLE
    //--------------------------------------------------------------------------------
    public static String toDouble(Expr expr) {
        String str = exprToString(expr);

        Double ret = new Double(str);

        return ret.toString();
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // CHAR
    //--------------------------------------------------------------------------------
    public static String toChar(Expr expr) {
        String str = exprToString(expr);

        // SELECTS the FIRST CHAR of the STRING
        Character firstChar = str.charAt(0);

        String ret = new String("\'" +
                firstChar +
                "\'");
        return ret;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // STRING
    //--------------------------------------------------------------------------------
    public static String toString(Expr expr) {
        String str = exprToString(expr);

        String ret = new String("\"" +
                str +
                "\"");
        return ret;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // BOOLEAN
    //--------------------------------------------------------------------------------
    public static String toBoolean(Expr expr) {
        String ret;
        String str = exprToString(expr);

        if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("on") || str.equalsIgnoreCase("1")) {
            ret = "true";
        } else if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("no") || str.equalsIgnoreCase("off") || str.equalsIgnoreCase("0")) {
            ret = "false";
        } else {
            // Generates the CONSTANT ("true" or "false")
            ret = stringToBoolean(str);
        }

        return ret;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    public static String exprToString(Expr expr) {
        String ret;

        //--------------------------------------------------------------------------------
        if (expr instanceof NumExpr) {
            Numeral numeral = ((NumExpr) expr).getNumeral();

            ret = numeral.toString();
        } //--------------------------------------------------------------------------------
        else if (expr instanceof RefExpr) {
            Name name = ((RefExpr) expr).getName();
            ret = name.toString();
        } //--------------------------------------------------------------------------------
        else if (expr instanceof ApplExpr) {
            String parcialRet = new String();

            ApplExpr applExpr = (ApplExpr) expr;
            Expr leftExpr = applExpr.getLeftExpr();
            Expr rightExpr = applExpr.getRightExpr();

            if (leftExpr instanceof RefExpr) {
                RefExpr refExpr = (RefExpr) leftExpr;
                ZName zName = refExpr.getZName();
                OperatorName operator = zName.getOperatorName();
                String operatorString = operator.getWord();
                int[] opCode = RefineAST.getOperatorCode(operatorString);

                //--------------------------------------------------------------------------------
                // PLUS all REQUIRED
                //--------------------------------------------------------------------------------
                int[] negateOp = {45, 32, 95, 32, 0, 0, 0, 0, 0, 0};
                //--------------------------------------------------------------------------------

                //--------------------------------------------------------------------------------
                // CASE: negateOp
                //--------------------------------------------------------------------------------
                if (java.util.Arrays.equals(opCode,
                        negateOp)) {
                    parcialRet = "-";
                }
                //--------------------------------------------------------------------------------
            }

            if (rightExpr instanceof NumExpr) {
                NumExpr numExpr = (NumExpr) rightExpr;
                Numeral numeral = numExpr.getNumeral();
                String numeralString = numeral.toString();

                parcialRet = parcialRet + numeralString;
            }

            ret = parcialRet;
        } //--------------------------------------------------------------------------------
        else if (expr instanceof TupleSelExpr) {
            Expr enterPart = ((TupleSelExpr) expr).getExpr();
            Numeral decimalPart = ((TupleSelExpr) expr).getNumeral();

            String enterPartString = enterPart.toString();
            String decimalPartString = decimalPart.toString();

            ret = enterPartString + "." + decimalPartString;
        } //--------------------------------------------------------------------------------
        else {
            UtilsJava.printErrorInvalidFormatArgumentforVariable(expr.toString());
            ret = null;
        }
        //--------------------------------------------------------------------------------

        return ret;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // Is the STRING a SHORT ?
    //--------------------------------------------------------------------------------
    public static boolean isShort(String s) {
        try {
            Short.parseShort(s);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // Is the STRING an INT ?
    //--------------------------------------------------------------------------------
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // Is the STRING a LONG ?
    //--------------------------------------------------------------------------------
    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
    //--------------------------------------------------------------------------------    

    //--------------------------------------------------------------------------------   
    public static String stringToByte(String str) {
        Byte hashCode = new Byte((byte) str.hashCode());

        return hashCode.toString();
    }
    //--------------------------------------------------------------------------------

    public static String stringToShort(String str) {
        int hashCode = str.hashCode();

        Short ret = new Short((short) hashCode);

        return ret.toString();
    }
    //--------------------------------------------------------------------------------    

    public static String stringToInt(String str) {
        String strInt = "";

        Integer sint = new Integer(str.hashCode());
        strInt = sint.toString();

        return strInt;
    }
    //--------------------------------------------------------------------------------

    public static String stringToLong(String str) {
        Long hashCode = new Long((long) str.hashCode());

        return hashCode.toString();
    }
    //--------------------------------------------------------------------------------    

    public static String stringToFloat(String str) {
        Float hashCode = new Float((float) str.hashCode());

        return hashCode.toString();
    }
    //--------------------------------------------------------------------------------   

    public static String stringToDouble(String str) {
        Double hashCode = new Double((double) str.hashCode());

        return hashCode.toString();
    }
    //--------------------------------------------------------------------------------    

    public static String stringToChar(String str) {
        Character hashCode = new Character((char) str.hashCode());

        return hashCode.toString();
    }
    //--------------------------------------------------------------------------------    

    public static String stringToBoolean(String str) {
        Boolean ret;

        int hashCode = str.hashCode();   	//	[-2,147,483,648, ... , 2,147,483,647]

        if (hashCode >= 0) {
            ret = new Boolean(true);
        } else {
            ret = new Boolean(false);
        }

        return ret.toString();

    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------    
    // VER si se puede usar esta GEN�RICAMENTE
    //--------------------------------------------------------------------------------
    public static String stringToHashCode(String str) {
        Integer hashCode = str.hashCode();

        return hashCode.toString();
    }
    //--------------------------------------------------------------------------------    

    //--------------------------------------------------------------------------------    
    // VESTIGIOS de la ANTIGUA IMPLEMENTACI�N de DIEGO HOLLMAN
    //--------------------------------------------------------------------------------      
    //--------------------------------------------------------------------------------
    public static String valExprToStruct(String head, NodeStructure structure, Expr expr) {
        List<NodeElement> fields = structure.getElements();

        return head;
    }
    //--------------------------------------------------------------------------------

    public static String pointerToPLType(String head, NodePLType plType, Expr expr) {
        head = head.concat(" = ");

        head = head.concat(PLTypeToString(plType.getType(), expr) + ";\n");

        return head;
    }
    //--------------------------------------------------------------------------------
}
