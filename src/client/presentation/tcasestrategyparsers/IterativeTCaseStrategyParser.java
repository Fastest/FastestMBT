/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.tcasestrategyparsers;

import java.util.*;
import java.io.*;
import java.math.*;

import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.ApplExpr;
import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;

import common.z.SpecUtils;
import compserver.tcasegen.strategies.*;
import compserver.tcasegen.fm.intfm.IntFiniteModel;
import compserver.tcasegen.fm.natfm.NatFiniteModel;
import compserver.tcasegen.fm.intfm.GivenIntFiniteModel;
import compserver.tcasegen.fm.natfm.GivenNatFiniteModel;
import compserver.tcasegen.fm.intfm.SeedsIntFiniteModel;
import compserver.tcasegen.fm.natfm.SeedsNatFiniteModel;
import compserver.tcasegen.fm.FromToFiniteModel;
import common.z.czt.UniqueZLive;
import compserver.tcasegen.fm.FiniteModel;
import compserver.tcasegen.fm.SetFiniteModel;
import common.z.TClass;
import common.z.czt.CztPrinter;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.animation.eval.ZLive;
import common.z.czt.UniqueZLive;
import net.sourceforge.czt.session.Markup;
import client.presentation.ClientTextUI;
import client.blogic.management.Controller;



/**
 * An instance of this class performs the parsing of the parameters passed to the command
 * that select the iterative strategy of test cases generation.
 * @author Pablo Rodriguez Monetti
 */
public class IterativeTCaseStrategyParser implements TCaseStrategyParser{

    private ClientTextUI clientTextUI;

    /**
     * Parse the received parameters and sets, according to them, the strategy of test cases
     * generation.
     * @param args
     * @param tcs
     * @return
     * @throws java.lang.IllegalArgumentException
     *      */
    public boolean parse(TClass tClass, String args, TCaseStrategy tcs)
        throws IllegalArgumentException{

        try{

            if(!(tcs instanceof IterativeTCaseStrategy))
                throw new IllegalArgumentException();
                
            final String argv[] = args.split(" ");
            int argc = argv.length;                   
            
            
            IterativeTCaseStrategy iterativeTCaseStrategy = (IterativeTCaseStrategy) tcs;
            IntFiniteModel intFiniteModel = new GivenIntFiniteModel();
            NatFiniteModel natFiniteModel = new GivenNatFiniteModel();
            boolean btSizeSetted = false;
            boolean nzSizeSetted = false;
            boolean sizeSetted = false;



            for(int index = 0, argCount=1; index < argc; index += argCount ){
                if(argv[index].equals("-size")){
                    String sizeSrt = argv[index+1];
                    int size = Integer.parseInt(sizeSrt);
                    iterativeTCaseStrategy.setFiniteModelSize(size);
                    argCount += 2;
                    sizeSetted = true;
                }
                else if(argv[index].equals("-btsize")){
                    String btSizeSrt = argv[index+1];
                    int btSize = Integer.parseInt(btSizeSrt);
                    iterativeTCaseStrategy.setGivenTypeFiniteModelSize(btSize);
                    argCount += 2;
                    btSizeSetted = true;
                }
                else if(argv[index].equals("-nzsize")){
                    String nzSizeSrt = argv[index+1];
                    int nzSize = Integer.parseInt(nzSizeSrt);
                    iterativeTCaseStrategy.setIntNatFiniteModelSize(nzSize);
                    argCount += 2;
                    nzSizeSetted = true;
                }
                else if(argv[index].equals("-fm")){
                    String option = "";
                    int fmOptionArgListSize = 0;
                    for(int k=index; k<argv.length; k++){
                        fmOptionArgListSize++;
                        option += argv[k] + " ";
                    }
                    
                    final String parts[] = option.split("\"",3);
                    String listOfFMStr = parts[1];

                    fmOptionArgListSize -= parts[2].length();

                    String finiteModels[] = listOfFMStr.split(";");
                    ZLive zLive = UniqueZLive.getInstance();
                    TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
                    Map<Expr, FiniteModel> exprMap = new HashMap<Expr,FiniteModel>();
                    for(int k=0; k < finiteModels.length;k++){
                        String finiteModel = finiteModels[k];
                        String finiteModelParts[] = finiteModel.split("==");
                        String varOrType = finiteModelParts[0].trim();
                        String exprStr = finiteModelParts[1];

                        int beginIndex = exprStr.indexOf("\\upto");

                        if(varOrType.equals("\num") && exprStr.equals("Seeds") ){
                            intFiniteModel = new SeedsIntFiniteModel();

                        }
                        else if(varOrType.equals("\nat") && exprStr.equals("Seeds") ){
                            natFiniteModel = new SeedsNatFiniteModel();
                        }
                        else{

                            // We parse exprStr as a Latex expression
                            Term varOrTypeTerm = ParseUtils.parsePred(
                                new StringSource(varOrType),
                                zLive.getCurrentSection(),
                                zLive.getSectionManager() );

                            if(varOrTypeTerm instanceof ExprPred)
                                varOrTypeTerm = ((ExprPred)varOrTypeTerm).getExpr();

                            Expr varOrTypeExpr = (Expr) varOrTypeTerm;

                            boolean isAVariable = false;
                            boolean isAType = false;

                            AxPara opAxPara = (AxPara) tClass.getMyAxPara();

                            // We check if varOrType is a variable or a type
                            //declared in  the test class
                            DeclList declList = SpecUtils.getAxParaListOfDecl(opAxPara);
                            if(declList instanceof ZDeclList){
                                ZDeclList zDeclList = (ZDeclList)declList;
                                ZName zNameFound = null;
                                for(int j=0; j < zDeclList.size() && !isAType && !isAVariable; j++){
                                    Decl decl = ((ZDeclList)declList).get(j);
                                    if(decl instanceof VarDecl){
                                        VarDecl varDecl = (VarDecl) decl;
                                        NameList nameList = varDecl.getName();
                                        Expr varDeclExpr = varDecl.getExpr();
                                        if(SpecUtils.areEqualTerms(varOrTypeExpr, varDeclExpr))
                                            isAType = true;
                                        else if (nameList instanceof ZNameListImpl){
                                            ZNameListImpl zNameListImpl= (ZNameListImpl)nameList;
                                            for (int l=0; l< zNameListImpl.size() ; l++){
                                                ZName zName = (ZName) zNameListImpl.get(l);
                                                String auxVarName = zName.toString();
                                                if(auxVarName.equals(varOrType)){
                                                    zNameFound = zName;
                                                    isAVariable = true;
                                                }
                                            }
                                        }
                                    }
                                }

                                if(isAVariable && isAType){
                                    System.out.println(varOrType + " can not be a " +
                                            "type and a variable simultaneously.");
                                    return false;
                                }
                                else if (!isAVariable && !isAType){
                                    System.out.println(varOrType + " is not a " +
                                            "type nor a variable of the specified " +
                                            "test class.");
                                    return false;
                                }

                                

                                // We parse exprStr as a Latex expression
                                //System.out.println("La frase es '" + exprStr + "'");
                                Term finiteModelTerm = ParseUtils.parsePred(
                                    new StringSource(exprStr),
                                    zLive.getCurrentSection(),
                                    zLive.getSectionManager() );

                                if(finiteModelTerm instanceof ExprPred)
                                    finiteModelTerm = ((ExprPred)finiteModelTerm).getExpr();
                     
                                //CztPrinter.printExpr((Expr)finiteModelTerm,0);                                     

                                FiniteModel fM = null;

                                if(finiteModelTerm instanceof SetExpr){
                                    SetExpr setExpr = (SetExpr) finiteModelTerm;
                                    fM = new SetFiniteModel(setExpr.getZExprList());

                                }else if(finiteModelTerm instanceof ApplExpr){
                                    ApplExpr applExpr = (ApplExpr) finiteModelTerm;
                                    if(!applExpr.getMixfix()){
                                        return false;
                                    }
                                    Expr leftExpr = applExpr.getLeftExpr();
                                    if(!(leftExpr instanceof RefExpr)){
                                        return false;
                                    }

                                    RefExpr refExpr = (RefExpr) leftExpr;
                                    String refExprName = refExpr.getName().toString();
                                    if(!refExprName.equals(" _ .. _ ")){
                                        return false;
                                    }


                                    Expr rightExpr = applExpr.getRightExpr();
                                    if(!(rightExpr instanceof TupleExpr)){
                                        return false;
                                    }


                                    TupleExpr tupleExpr = (TupleExpr) rightExpr;
                                    ZExprList zExprList = tupleExpr.getZExprList();
                                    if (zExprList.size() !=2){
                                        return false;
                                    }

                                    if(!(zExprList.get(0) instanceof NumExpr)){
                                        return false;
                                    }

                                    if(!(zExprList.get(1) instanceof NumExpr)){
                                        return false;
                                    }

                                    BigInteger bigInteger0 = ((NumExpr)zExprList.get(0)).getValue();
                                    int left = (int) bigInteger0.doubleValue();
                                    BigInteger bigInteger1 = ((NumExpr)zExprList.get(1)).getValue();
                                    int right = (int) bigInteger1.doubleValue();

                                    fM = new FromToFiniteModel(left,right);
                                }

                                ZFactory zFactory = new ZFactoryImpl();

                                if(isAVariable){
                                    RefExpr refExpr = zFactory.createRefExpr(
                                        zNameFound, zFactory.createZExprList(), false, false);
                                    exprMap.put(refExpr, fM);
                                }
                                else if (isAType){
                                    exprMap.put(varOrTypeExpr, fM);
                                }
                                else{
                                    // Unreachable statement (this case was pre-
                                    // viously considered)
                                    return false;
                                }


                            }else{
                                // The test class variable declaration is not
                                // a ZDeclList
                                return false;
                            }

                        }
                        iterativeTCaseStrategy.setExprMap(exprMap);
                    }
                    argCount += fmOptionArgListSize;
                }else{
                    // The option is not -size, -btsize, -nzsize, -fm
                    return false;
                }


            }
            
            if(!sizeSetted)
                iterativeTCaseStrategy.setFiniteModelSize(3);

            if(!btSizeSetted)
                iterativeTCaseStrategy.setGivenTypeFiniteModelSize(-1);


            if(!nzSizeSetted)
                iterativeTCaseStrategy.setIntNatFiniteModelSize(-1);



            iterativeTCaseStrategy.setIntFiniteModel(intFiniteModel);
            iterativeTCaseStrategy.setNatFiniteModel(natFiniteModel);

            Controller controller = clientTextUI.getMyController();
            int maxEval = controller.getMaxEval();
            iterativeTCaseStrategy.setMaxEval(maxEval);
            
            
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public void setClientTextUI(ClientTextUI clientTextUI){
        this.clientTextUI = clientTextUI;
    }


    public ClientTextUI getClientTextUI(){
        return clientTextUI;
    }

    
}
