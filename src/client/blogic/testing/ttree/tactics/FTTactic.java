package client.blogic.testing.ttree.tactics;

import java.util.*;

import net.sourceforge.czt.z.ast.BranchList;
import net.sourceforge.czt.z.ast.Branch;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.FreetypeList;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZStrokeList;
import net.sourceforge.czt.z.ast.FreePara;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.Freetype;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.impl.ZFreetypeListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZBranchListImpl;

import common.z.TClass;
import common.z.TClassImpl;
import common.z.SpecUtils;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.ContainsVarDeclVerifier;

/**
 * Implementation of the tactic Free Types (FT).
 * @author Pablo Rodriguez Monetti
 */
public class FTTactic extends AbstractTactic {


    private FTTacticInfo fTTacticInfo;
  
    /**
     * Creates new instances of FTTactic.
     */
    public FTTactic() {
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* FT (Free Type). This tactic generates as many test\n";
        description += "  classes as elements a free type (enumerated) has. In\n";
        description += "  other words if your model has type\n";
        description += "  COLOUR ::= red | blue | green and some operation uses c\n";
        description += "  of type COLOUR, then by applying this tactic you will\n";
        description += "  get three test classes: one in which c equals red , the\n";
        description += "  other in which c equals blue, and the third where c\n";
        description += "  equals green.\n";
        description += "  Usage: addtactic <op_name> FT <var_name>\n";
        description += "  where op_name is the name of a selected operation\n";
        description += "  and var_name is the name of a variable whose type is\n";
        description += "  a free type.\n";
    }

    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param tClass
     * @return
     */
    public List<TClass> applyTactic(TClass tClass) {
        ZFactory zFactory = new ZFactoryImpl();

        AxPara opAxPara = (AxPara) originalOp.getMyAxPara().
                accept(new CZTCloner());
        List<TClass> tClassList = new ArrayList<TClass>();


        List<AxPara> axParaList = new ArrayList<AxPara>();


        AxPara tClassAxPara = tClass.getMyAxPara();
        CZTCloner cloneVisitor = new CZTCloner();


        String opName = SpecUtils.getAxParaName(opAxPara);
        Integer tClassInteger = tClassNumbersMap.get(opName);
        if (tClassInteger == null) {
            tClassInteger = new Integer(1);
            tClassNumbersMap.put(opName, tClassInteger);
        }

        BranchList branchList = fTTacticInfo.getType().getBranchList();
        if (branchList instanceof ZBranchListImpl) {
            ZBranchListImpl zBranchListImpl = (ZBranchListImpl) branchList;

            String varNameStr = fTTacticInfo.getVar();
            ZExprList zExprList = zFactory.createZExprList();
            ZName varZName = null;
            ZStrokeList zStrokeList = zFactory.createZStrokeList();
            if (varNameStr.endsWith("?")) {
                zStrokeList.add(zFactory.createInStroke());
                varNameStr = varNameStr.substring(0, varNameStr.length() - 1);
                varZName = zFactory.createZName(varNameStr, zStrokeList, "neg");
            } else {
                varZName = zFactory.createZName(varNameStr, zFactory.createZStrokeList(), "neg");
            }

            RefExpr varRefExpr = zFactory.createRefExpr(varZName, zExprList,
                    false, false);

            // For each possible value V of the enumerated type  T, a predicate
            // var = V will be generated
            for (int j = 0; j < zBranchListImpl.size(); j++) {
                Branch branch = zBranchListImpl.get(j);
                Name branchName = branch.getName();

                RefExpr valueRefExpr = zFactory.createRefExpr(branchName,
                        zExprList, false, false);

                //We create the SetExpr that contains the right expr
                ZExprList setZExprList = zFactory.createZExprList();
                setZExprList.add(0, valueRefExpr);
                SetExpr setExpr = zFactory.createSetExpr(setZExprList);

                //We create the MemPred that will be added to the new test class
                ZExprList memPredExprList = zFactory.createZExprList();
                memPredExprList.add(0, varRefExpr);
                memPredExprList.add(1, setExpr);
                MemPred memPred = zFactory.createMemPred(memPredExprList, true);

                int tClassNumber = tClassNumbersMap.get(opName).intValue();

                String tClassName = SpecUtils.getAxParaName(opAxPara) + "_FT_"
                        + tClassNumber;
                tClassNumbersMap.put(opName, tClassNumber + 1);
                AxPara newAxPara = (AxPara) tClassAxPara.accept(cloneVisitor);

                Pred newAxParaPred = SpecUtils.getAxParaPred(newAxPara);
                SpecUtils.setAxParaPred(newAxPara, SpecUtils.andPreds(newAxParaPred,
                        memPred));


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
                SpecUtils.setAxParaPred(auxTClassAxPara, memPred);
                tClassList.add(auxTClass);
            }
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
        if (parts.length != 1) {
            System.out.println("Invalid parameters.  Try 'help'." + parts.length);
            return false;
        }
        String varName = parts[0];

        if (varName.endsWith("!")) {
            System.out.println("The specified variable can not be an output variable.");
            return false;
        }

        AxPara opAxPara = (AxPara) originalOp.getMyAxPara();

        // We check if the specified variable is declared in the scheme declaration and we 
        // check if its type CAN BE a free type (we do not warrant here that the type IS a 
        // free type), verifying that it is represented by a RefExpr. In other words, we
        // check a necessary condition, not a sufficient condition.
        
        
        String typeName = opAxPara.accept(new ContainsVarDeclVerifier(varName, spec, controller));

        
        if (typeName == null) {
            System.out.println("The specified variable is not declared in "
                    + "the specified operation's schema.");
            return false;
        }        

        if (typeName.equals("")) {
            System.out.println("The specified variable's type is not a free type.");
            return false;
        }



        // We check if the specified variable's type is a free type.
        Freetype freeType = null;
        for (Sect sect : spec.getSect()) {
            if (sect instanceof ZSect) {
                ParaList paraList = ((ZSect) sect).getParaList();
                if (paraList instanceof ZParaList) {
                    ZParaList zParaList = (ZParaList) paraList;
                    for (int i = 0; i < zParaList.size(); i++) {
                        Para para = zParaList.get(i);
                        if (para instanceof FreePara) {
                            FreetypeList freetypeList = ((FreePara) para).getFreetypeList();
                            if (freetypeList instanceof ZFreetypeListImpl) {
                                ZFreetypeListImpl zFreetypeListImpl = (ZFreetypeListImpl) freetypeList;
                                for (int j = 0; j < zFreetypeListImpl.size(); j++) {
                                    Freetype auxFreetype = zFreetypeListImpl.get(j);
                                    String freeTypeName = auxFreetype.getName().toString();
                                    if (freeTypeName.equals(typeName)) {
                                        freeType = auxFreetype;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (freeType == null) {
            System.out.println("The specified variable's type is not a type "
                    + "defined in the loaded specification.");
        }

        fTTacticInfo = new FTTacticInfo();
        fTTacticInfo.setVar(varName);
        fTTacticInfo.setType(freeType);

        return true;
    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalArgumentException if tacticInfo is not an instance of
     * ETTacticInfo.
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalArgumentException {

        if (tacticInfo instanceof FTTacticInfo) {
            fTTacticInfo = (FTTacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return fTTacticInfo;
    }
}
