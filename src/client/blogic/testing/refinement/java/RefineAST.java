package client.blogic.testing.refinement.java;

//--------------------------------------------------------------------------------
import client.blogic.testing.refinement.ConcreteTCase;
import client.blogic.testing.refinement.TCaseAssignment;
//--------------------------------------------------------------------------------
import net.sourceforge.czt.base.ast.ListTerm;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;
import net.sourceforge.czt.z.util.OperatorName;
//--------------------------------------------------------------------------------
import common.z.AbstractTCase;
import common.z.SpecUtils;
import common.z.czt.visitors.AndPredClausesExtractor;
//--------------------------------------------------------------------------------
import common.repository.*;
//--------------------------------------------------------------------------------
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
//--------------------------------------------------------------------------------

public class RefineAST {

    static String specID;
    static ZExprList specIDAssignment;
    static String ruleName;

    // Creates the CONCRETE TEST CASE
    static ConcreteTCase ctc = new ConcreteTCase();

    //EXAMPLE: (specID, ruleName )

    public static HashMap<String, String> specIDsToRulenameTable = new HashMap<String, String>();
    public static HashMap<String, Expr> specIDsToExprTable = new HashMap<String, Expr>();

    // OPERATOR CODES LIST
    static int[] relOP = {32, 95, 32, 8596, 32, 95, 32, 0, 0, 0};
    static int[] funOP = {32, 95, 32, 8594, 32, 95, 32, 0, 0, 0};
    static int[] pfunOP = {32, 95, 32, 8696, 32, 95, 32, 0, 0, 0};

    static int[][] opFunctionsList = {relOP,funOP,pfunOP};

/**
 * This function obtains all the specIDs contained in refinement law maps with its 
 * rule name and stores them in specIDsToRulenameTable
 */
    public static void specIDsToRulenameTable(NodeRules rules) {

        // Extracts the SpecIDs inside the REFINEMENT LAW
        Set<String> setRuleNames = rules.getKeys();
        Iterator<String> iterRuleNames = setRuleNames.iterator();

        while (iterRuleNames.hasNext()) {
            String ruleName = iterRuleNames.next();
            NodeRule rule = rules.getRule(ruleName);

            // Extracts the SpecIDs inside of each RULE
            List<String> ruleSpecIDs = ((RuleRefinement) rule).getSpecIDs();
            Iterator<String> iterRuleSpecIDs = ruleSpecIDs.iterator();

            while (iterRuleSpecIDs.hasNext()) {
                String ruleSpecID = iterRuleSpecIDs.next();

                specIDsToRulenameTable.put(ruleSpecID,
                        ruleName);
            }
        }
    }


/**
 * This function obtains the names of the variables in the abstract case and their types,
 * and stores them in specIDsToExprTable
 */
    public static void generateSpecIDsToExprTable(AbstractTCase atc) {

        // Gets the AxPara of the ABSTRACT TEST CASE
        AxPara axPara = atc.getMyAxPara();
        // Gets the DECLARATION of the ABSTRACT TEST CASE
        DeclList refDeclList = SpecUtils.getAxParaListOfDecl(axPara);
        ZDeclList refZDeclList = (ZDeclList) refDeclList;

        Iterator<Decl> declIt = refZDeclList.iterator();
        Decl decl;
        VarDecl varDecl;
        ZNameList varNameList;
        while (declIt.hasNext()) {
            decl = declIt.next();
            if (decl instanceof VarDecl) {
                varDecl = (VarDecl) decl;
                varNameList = varDecl.getName();
                Iterator<Name> varNameListIt = varNameList.iterator();
                Name varName;
                String varNameString;
                Expr varExp;
                while (varNameListIt.hasNext()) {
                    varName = varNameListIt.next();
                    varNameString = varName.toString();
                    varExp = varDecl.getExpr();
                    specIDsToExprTable.put(varNameString,
                            varExp);
                }
            }
        }
    }


    public static int[] getOperatorCode(String operatorString) {
        int opIndex;
        int[] opCode = new int[10];
        int length = operatorString.length();

        for (opIndex = 0; opIndex < length; opIndex++) {
            opCode[opIndex] = operatorString.codePointAt(opIndex);
        }

        return opCode;
    }

    public static boolean checkOperatorCode(int[] operator, int[][] opList) {
        boolean ret = false;

        for (int opIndex = 0; opIndex < opList.length; opIndex++) {
            if (java.util.Arrays.equals(operator,
                    opList[opIndex])) {
                ret = true;
            }
        }

        return ret;
    }

/**
 * Checks if the pred that is passed as argument is an assignment
 */
    public static boolean isAnAssignment(Pred pred) {
        Boolean ret = false;

        if (pred instanceof MemPred) {
            MemPred memPred = ((MemPred) pred);

            Expr leftExpr = memPred.getLeftExpr();

            Expr rightExpr = memPred.getRightExpr();

            // CHECK if the PREDICATE has the form: VAR = EXP
            if ((leftExpr instanceof RefExprImpl) &
                    (rightExpr instanceof SetExprImpl)) {
                ret = true;
            }
        }

        return ret;
    }

/**
 * Returns the identifier of an asignment
 */
    public static String getSpecID(MemPred memPred) {
        Expr leftExpr = memPred.getLeftExpr();
        RefExpr leftRefExpr = (RefExpr) leftExpr;
        Name name = leftRefExpr.getName();
        String nameString = name.toString();

        return nameString;
    }

/**
 * Returns the right side of an asignment
 */
    public static ZExprListImpl getSpecIDAssignment(MemPred memPred) {
        Expr rightExpr = memPred.getRightExpr();
        SetExpr rightSetExpr = (SetExpr) rightExpr;
        ZExprList rightZExprList = rightSetExpr.getZExprList();

        return (ZExprListImpl) rightZExprList;
    }

/**
 * Checks if the identifier that is passed as argument is in specIDsToRulenameTable
 */
    public static boolean existRulename(String specID) {
        Boolean ret = false;

        String ruleName = specIDsToRulenameTable.get(specID);

        if (ruleName != null) {
            ret = true;
        }

        return ret;
    }


    public static ConcreteTCase refine(TCRL_AST ast, AbstractTCase atc) {

        // SET the PREAMBLE and EPILOGUE of CTC EQUALS to TCRL_AST
        ctc.setPreamble(ast.getPreamble());
        ctc.setEpilogue(ast.getEpilogue());

        // Gets the AST RULES
        NodeRules rules = ast.getRules();

        // GENERATES the "SpecIDs" TABLE.
        specIDsToRulenameTable(rules);

        // GENERATES the TABLE that maps "SpecIDs" with its type.
        generateSpecIDsToExprTable(atc);

        // GENERATES THE PARTICULAR REFINEMENT of each PREDICATE
        // Gets the AxPara of the ABSTRACT TEST CASE
        AxPara axPara = atc.getMyAxPara();
        // Gets the PREDICATE of the ABSTRACT TEST CASE
        Pred refPred = SpecUtils.getAxParaPred(axPara);
        AbstractRepository<Pred> predRep = refPred.accept(new AndPredClausesExtractor());
        AbstractIterator<Pred> predIt = predRep.createIterator();

        while (predIt.hasNext()) {
            Pred pred = predIt.next();
            // CHECK is the PREDICATE is an ASSIGNMENT
            if (isAnAssignment(pred)) {
                MemPred memPred = ((MemPred) pred);
                if (memPred.getMixfix())
                {
                    specID = getSpecID(memPred);
                    specIDAssignment = getSpecIDAssignment(memPred);
                    if (existRulename(specID)) {
                        // SEEKS for the RULE that REFINE the "specID" VARIABLE
                        ruleName = specIDsToRulenameTable.get(specID);
                        NodeRule rule = rules.getRule(ruleName);
                        RuleRefinement ruleRefinement = (RuleRefinement) rule;

                        Expr specIDZType = specIDsToExprTable.get(specID);

                        // SELECT the ZTYPE of the Z VARIABLE "specID"
                        // in the DECLARATION of the SCHEMA
                        selectSpecIDZType(ruleRefinement, specIDAssignment, specIDZType);
                    } else {
                        // ERROR: VARIBLE NOT SPECIFIED
                        UtilsJava.printErrorVariableNotSpecified(specID);
                    }
                } else {
                    // memPred.getMixfix() = false
                    // DO NOTHING
                }
            } else {
                // the PREDICATE is not an ASSIGNMENT (HAS NOT the form: VAR = EXP)
                return null;
            }
        }
        return ctc;
    }


    static void selectSpecIDZType(RuleRefinement ruleRefinement, ZExprList specIDAssignment,
            Expr specIDZType) {
        // The variable in the specification is a RefExpr
        if (specIDZType instanceof RefExpr) {
            RefExpr refSpecIDZType = (RefExpr) specIDZType;
            ZName refSpecIDZTypeZName = refSpecIDZType.getZName();
            OperatorName refSpecIDZOperator = refSpecIDZTypeZName.getOperatorName();

	    // If operatorName equals null the RefExpr has not an operator associated
            // The TYPE of the specID is a: "BASIC TYPE" or ....
            // EXAMPLE:	specID : NAME
	    // EXAMPLE: specID : \nat
            if (refSpecIDZOperator == null) {
                isBasicType(ruleRefinement, specIDAssignment);
            }

            // The TYPE of the specID have an OPERATOR.
            if (refSpecIDZOperator != null) {
                String specIDZOperatorString = refSpecIDZOperator.getWord();

                int[] opCode = getOperatorCode(specIDZOperatorString);

                Boolean unary = refSpecIDZOperator.isUnary();
                Boolean prefix = refSpecIDZOperator.isPrefix();
                Boolean infix = refSpecIDZOperator.isInfix();
                Boolean postfix = refSpecIDZOperator.isPostfix();

                // PREFIX OPERATOR
                // EXAMPLES:  memd : \seq MDATA
                if (prefix == true) {
                    // The OPERATOR is: SEQ
                    // EXAMPLE:	memd	: \seq MDATA
                    boolean seqCheck = specIDZOperatorString.equalsIgnoreCase("seq _ ");

                    if (seqCheck) {
                        isSequence(ruleRefinement, specIDAssignment);
                    }
                    // Put the OTHERS PREFIX OPERATORS HERE !!! (if any)
                } 
                // INFIX OPERATOR
                // EXAMPLES:
                // specID : NCTA \pfun SALDO ,NCTA -/-> SALDO, specID : NCTA \rel SALDO
		// NCTA <--> SALDO, specID : NCTA \fun SALDO ,NCTA --> SALDO
                else if (infix == true) {
                    // The OPERATOR is a FUNCTION.
                    // EXAMPLE:	specID : A \pfun B
                    boolean functionCheck = checkOperatorCode(opCode, opFunctionsList);
                    if (functionCheck) //CHECK if it's a FUNCTION.
                    {
                        isFunction(ruleRefinement, specIDAssignment, refSpecIDZType);
                    }
		    else{
			//ACA deberia ir un mensaje de tipo no soportado
		    }
                } 
                // POSTFIX OPERATOR
                else if (postfix == true) {
                    // Put the POSTFIX OPERATORS HERE !!! (if any)
		    //ACA deberia ir un mensaje de tipo no soportado
                }
            }
        }
        // The variable in the specification is a "PowerExpr"
        else if (specIDZType instanceof PowerExpr) {
            isPartition(ruleRefinement, specIDAssignment, specIDZType);
        }
        // The specIDZType is NOT SOPORTED by RefineAST
        else {
            UtilsJava.printSpecIDZTypeNotSoported(specIDZType.toString());
            throw new IllegalArgumentException();
        }
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // ASSIGNMENT
    //--------------------------------------------------------------------------------
    static void assignment(RuleRefinement ruleRefinement,
            ZExprList specIDAssignment) {
        List<TCaseAssignment> assignments = RefineExpr.refineExprList(ruleRefinement,
                specIDAssignment);
        ctc.addTCaseAssignment(assignments);
    }
    //--------------------------------------------------------------------------------

/**
* This function request the refinement of a variable whose type is a basic type
*/
    static void isBasicType(RuleRefinement ruleRefinement, ZExprList specIDAssignment) {
        assignment(ruleRefinement, specIDAssignment);
    }

    // The TYPE of the specID is a: "SEQUENCE"
    // EXAMPLE:	 memd	: \seq MDATA
/**
* This function obtains all the elements of a sequence and stores them in a list,
* and call the assignment function for request the refinement of this values
*/
    static void isSequence(RuleRefinement ruleRefinement, ZExprList specIDAssignment) {
        Expr expr = specIDAssignment.get(0);

        if (expr instanceof ApplExpr) {
            ApplExpr applExpr = (ApplExpr) expr;
            ListTerm<Expr> termList = applExpr.getExpr();

            // Discards the refExpr
            Expr refExpr = termList.get(0);
            // Only use the setExpr
            Expr setExpr = termList.get(1);

            SetExprImpl set = (SetExprImpl) setExpr;
            ZExprList zExprList = set.getZExprList();

            int index;
            int size = zExprList.size();

            ZFactory zFactory = new ZFactoryImpl();
            ZExprList zExprListNew = zFactory.createZExprList();
            Expr exprInsideList;

            for (index = 0; index < size; index++) {
                exprInsideList = zExprList.get(index);

                TupleExpr tuple = (TupleExpr) exprInsideList;

                ZExprList tupleComponents = tuple.getZExprList();

                Expr tupleFirstComponent;
                Expr tupleSecondComponent;

                tupleFirstComponent = tupleComponents.get(0);
                tupleSecondComponent = tupleComponents.get(1);

                zExprListNew.add(tupleSecondComponent);

            }

            specIDAssignment = zExprListNew;
        } else {
        }
        assignment(ruleRefinement, specIDAssignment);
    }

    // The TYPE of the specID is a: "PARTITION"
    // EXAMPLE:	 categs : \power CATEGORY
    //--------------------------------------------------------------------------------
    static void isPartition(RuleRefinement ruleRefinement, ZExprList specIDAssignment, Expr specIDZType) {
	// Como minimo veo una limitacion para \power \power
        PowerExpr refSpecIDZType = (PowerExpr) specIDZType;
        Expr powerType = refSpecIDZType.getExpr();
        selectSpecIDZType(ruleRefinement, specIDAssignment, powerType);
    }


    //--------------------------------------------------------------------------------
    // The OPERATOR is a: FUNCTION.
    // EXAMPLE:	specID : A \pfun B
    //--------------------------------------------------------------------------------
    static void isFunction(RuleRefinement ruleRefinement, ZExprList specIDAssignment, RefExpr refSpecIDZType) {
        Boolean declarationExplcicit = refSpecIDZType.getExplicit();
        ExprList declarationExprList = refSpecIDZType.getExprList();
        ZExprList declarationZExprList = refSpecIDZType.getZExprList();

        Iterator<Expr> declarationZExprListIterator = declarationZExprList.iterator();

        int operatingIndex = 0;

        OperatorName[] declarationOpOperatings = new OperatorName[10];

        while (declarationZExprListIterator.hasNext()) {
            Expr declarationExpr = declarationZExprListIterator.next();

	    // ERROR: puede no ser una refExpr
            RefExpr declarationRefExpr = (RefExpr) declarationExpr;

            ZName declarationZName = declarationRefExpr.getZName();

            OperatorName declarationOp = declarationZName.getOperatorName();

            declarationOpOperatings[operatingIndex] = declarationOp;
            operatingIndex++;
        }

	// Tambien podria ser otra RefExpr con operador nulo. Ej: \nat
        // The DECLARATION has the FORM: BASIC TYPE \pfun BASIC TYPE
        // EXAMPLE: cajas : NCTA \pfun SALDO
        if ((declarationOpOperatings[0] == null) // is a BASIC TYPE. Not an OPERATOR
                &
                (declarationOpOperatings[1] == null) // is a BASIC TYPE. Not an OPERATOR
                ) {
            Abstraction abstraction = ruleRefinement.getAbstraction();

            List<Equality> equalities = abstraction.getEqualities(specID);

            if ((equalities == null) || equalities.isEmpty()) {
                // ERROR: THE RULE with the VARIABLE MUST HAVE
                // AN @ABSTRACTION SECTION
                UtilsJava.printErrorAbstractionNotSpecified(specID, ruleName);
                //---------------------------------------------
            } 
            // The DECLARATION has NOT the FORM: A \pfun B
            else {
                assignment(ruleRefinement, specIDAssignment);
            }
        } //--------------------------------------------------------------------------------
        // DEVELOP this part to include other CASES
        //--------------------------------------------------------------------------------
        else {
        }
    }
}
