package client.blogic.testing.ttree.tactics;

import java.util.*;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZStrokeList;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.TClass;
import common.z.TClassImpl;
import common.z.SpecUtils;
import common.z.czt.visitors.CZTCloner;
import common.z.UtilSymbols;
import net.sourceforge.czt.z.ast.TypeAnn;

/**
 * Implementation of the tactic Numeric Ranges (NR).
 * @author Pablo Rodriguez Monetti
 */
public class NRTactic extends AbstractTactic {

    private NRTacticInfo nRTacticInfo;

    /**
    * Creates new instances of NRTactic.
    */
    public NRTactic() {
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* NR (Numeric Ranges). This tactic takes as input the \n";
        description += "  name of a natural or integer variable declared in the \n";
        description += "  specified operation and a list of numbers. If the list \n";
        description += "  contains n numbers then 2*n + 1 classes will be generated. \n";
        description += "  For example, if the first argument is x and the list contains\n";
        description += "  the numeric contants a and b then the classes are five: \n";
        description += "  x < a, x = a, x > a \\land x < b, x = b, x > b. \n";
        description += "  Usage: addtactic Op NR <variable> <list_of_constants>.\n";
    }

    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param tClass
     * @return
     */
    public List<TClass> applyTactic(TClass tClass) {
        String varName = nRTacticInfo.getVar();
        List<Integer> numbers = nRTacticInfo.getNumbers();

        ZFactory zFactory = new ZFactoryImpl();
        ZExprList zExprList = zFactory.createZExprList();
        ZName varZName = null;
        ZStrokeList zStrokeList = zFactory.createZStrokeList();
        if (varName.endsWith("?")) {
            zStrokeList.add(zFactory.createInStroke());
            // We remove the ? character from the variable name
            varName = varName.substring(0, varName.length() - 1);
            varZName = zFactory.createZName(varName, zStrokeList, "neg");
        } else {
            varZName = zFactory.createZName(varName, zFactory.createZStrokeList(), "neg");
        }

        RefExpr varRefExpr = zFactory.createRefExpr(varZName, zExprList, false, false);

        List<Pred> predList = new ArrayList<Pred>();

        int size = numbers.size();

        if (size == 1) {
            Integer number = numbers.get(0);
            Pred pred1 = SpecUtils.getLessThanPred(varRefExpr, number);
            predList.add(pred1);
            Pred pred2 = SpecUtils.getEqualityPred(varRefExpr, number);
            predList.add(pred2);
            Pred pred3 = SpecUtils.getGreaterThanPred(varRefExpr, number);
            predList.add(pred3);
        } else {
            for (int i = 0; i < size; i++) {
                Integer number = numbers.get(i);
                Pred pred = SpecUtils.getEqualityPred(varRefExpr, number);
                predList.add(pred);
                if (i == 0) {
                    Pred pred1 = SpecUtils.getLessThanPred(varRefExpr, number);
                    predList.add(pred1);
                }

                if (i == size - 1) {
                    Pred pred2 = SpecUtils.getGreaterThanPred(varRefExpr, number);
                    predList.add(pred2);
                } else {
                    Integer numberI1 = numbers.get(i + 1);
                    Pred pred11 = SpecUtils.getGreaterThanPred(varRefExpr, number);
                    Pred pred12 = SpecUtils.getLessThanPred(varRefExpr, numberI1);
                    Pred pred1 = SpecUtils.andPreds(pred11, pred12);
                    predList.add(pred1);
                }
            }
        }

        AxPara tClassAxPara = tClass.getMyAxPara();
        AxPara opAxPara = (AxPara) originalOp.getMyAxPara().accept(new CZTCloner());
        String opName = SpecUtils.getAxParaName(opAxPara);
        Integer tClassInteger = tClassNumbersMap.get(opName);
        if (tClassInteger == null) {
            tClassInteger = new Integer(1);
            tClassNumbersMap.put(opName, tClassInteger);
        }

        List<TClass> tClassList = new ArrayList<TClass>();
        CZTCloner cloneVisitor = new CZTCloner();

        for (int j = 0; j < predList.size(); j++) {
            Pred pred = predList.get(j);


            int tClassNumber = tClassNumbersMap.get(opName).intValue();
            String tClassName = SpecUtils.getAxParaName(opAxPara) + "_NR_"
                    + tClassNumber;
            tClassNumbersMap.put(opName, tClassNumber + 1);
            AxPara newAxPara = (AxPara) tClassAxPara.accept(cloneVisitor);

            Pred newAxParaPred = SpecUtils.getAxParaPred(newAxPara);
            SpecUtils.setAxParaPred(newAxPara, SpecUtils.andPreds(newAxParaPred,
                    pred));

            // The test class is added to the list of test classes
            TClass auxTClass = new TClassImpl((AxPara) tClass.getMyAxPara().accept(new CZTCloner()), tClassName);
            AxPara auxTClassAxPara = auxTClass.getMyAxPara();

            ZName zName = zFactory.createZName(tClass.getSchName(),
                    zFactory.createZStrokeList(), "IncludedTClass");
            RefExpr refExpr = zFactory.createRefExpr(zName,
                    zFactory.createZExprList(), false, false);

            ZDeclList zDeclList = zFactory.createZDeclList();
            zDeclList.add(0, zFactory.createInclDecl(refExpr));
            SpecUtils.setAxParaName(auxTClassAxPara, tClassName);
            SpecUtils.setAxParaListOfDecl(auxTClassAxPara, zDeclList);
            SpecUtils.setAxParaPred(auxTClassAxPara, pred);
            tClassList.add(auxTClass);
        }
        return tClassList;
    }

    /**
     * Parses the parameters of this tactic.
     * @param args
     * @return
     */
    public boolean parseArgs(String args) {

        String parts[] = args.split(" ", 2);
        if (parts.length != 2) {
            System.out.println("Invalid parameters.  Try 'help'." + parts.length);
            return false;
        }

        String varName = parts[0];

        if (varName.endsWith("!")) {
            System.out.println("The specified variable can not be an output variable.");
            return false;
        }
        AxPara opAxPara = (AxPara) originalOp.getMyAxPara();

        // We check if the specified variable is declared in the scheme declaration
        // and if its type is \nat or \int
        boolean varFound = false;
        DeclList declList = SpecUtils.getAxParaListOfDecl(opAxPara);
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;

            for (int j = 0; j < zDeclList.size() && !varFound; j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    NameList nameList = varDecl.getName();
                    Expr typeExpr = varDecl.getExpr();
                    String typeName = "";
                    if (typeExpr instanceof RefExpr) {
                        RefExpr typeRefExpr = (RefExpr) typeExpr;

                        List<Object> anns = typeRefExpr.getAnns();
                        for (int i = 0; i < anns.size(); i++) {
                            Object ann = anns.get(i);
                            if (ann instanceof TypeAnn) {
                                TypeAnn typeAnn = (TypeAnn) ann;
                                typeName = typeAnn.getType().toString();
                            }
                        }
                        //System.out.println("El tipo es: " + typeName);
                        String arithmosTypeName = "POWER GIVEN " + UtilSymbols.arithmosSymbol();
                        if (typeName.equals(arithmosTypeName)) {
                            if (nameList instanceof ZNameListImpl) {
                                ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                                for (int k = 0; k < zNameListImpl.size() && !varFound; k++) {
                                    String auxVarName = zNameListImpl.get(k).toString();
                                    if (auxVarName.equals(varName)) {
                                        varFound = true;
                                    }
                                }
                            }

                        }
                    }

                }
            }
        } else {
            System.out.println("There is some problem with the specified "
                    + "operation.");
        }

        if (!varFound) {
            System.out.println("The specified variable is not a variable, of "
                    + "type \\nat, \\num or any synonymous of them, declared in"
                    + " the operation scheme.");
            return false;
        }


        String sndArg = parts[1];

        String firstBracket = "\\langle";
        String secondBracket = "\\rangle";

        int lengthOfFirstBracket = firstBracket.length();
        int lengthOfSecondBracket = secondBracket.length();


        if (!sndArg.startsWith(firstBracket) || !sndArg.endsWith(secondBracket)) {
            System.out.println("The last argument is not valid: the list of "
                    + "numbers must be enclosed with square brackets.");
            return false;
        }
        
        sndArg = sndArg.substring(lengthOfFirstBracket, sndArg.length()
                - lengthOfSecondBracket);


        if (sndArg.equals("")) {
            System.out.println("The last argument is not valid: "
                    + "the list of numbers is empty.");
            return false;
        }

        String listOfArgsStr[] = sndArg.split(",");
        ArrayList<Integer> listOfArgs = new ArrayList<Integer>();


        if (listOfArgsStr.length == 0) {
            System.out.println("The last argument is not valid: "
                    + "the list of numbers is empty.");
            return false;
        }
        try {
            for (int i = 0; i < listOfArgsStr.length; i++) {
                listOfArgsStr[i] = listOfArgsStr[i].trim();
                Integer integer = Integer.parseInt(listOfArgsStr[i]);
                /*              if (varType.equals("nat") && integer.intValue() <= 0) {
                System.out.println("The last argument is not valid: one of the "
                + "numbers is non-positive while the type of the specified "
                + "variable is \\nat.");
                return false;
                }
                 */
                listOfArgs.add(integer);

            }


            // Eliminar nros repetidos
            // Quizas ordenarlos

            nRTacticInfo = new NRTacticInfo();
            nRTacticInfo.setVar(varName);
            nRTacticInfo.setNumbers(listOfArgs);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Invalid list of numbers.");
            return false;
        }
    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalArgumentException if tacticInfo is not an instance of
     * ETTacticInfo.
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalArgumentException {

        if (tacticInfo instanceof NRTacticInfo) {
            nRTacticInfo = (NRTacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return nRTacticInfo;
    }

}
