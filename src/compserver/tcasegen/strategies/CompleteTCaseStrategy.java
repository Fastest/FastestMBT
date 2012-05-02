package compserver.tcasegen.strategies; 

import java.util.*;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZFactory;

import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.AbstractTCase;
import common.z.AbstractTCaseImpl;
import common.z.SpecUtils;
import common.z.TClass;
//import common.specelems.CztPrinter;

import compserver.tcasegen.eval.NormalTypeAndFM;
import compserver.tcasegen.eval.SchemeEvaluator;
import compserver.tcasegen.eval.EvaluationResp;
import compserver.tcasegen.fm.TypeFMsGenVisitor;



/**
 * An instance of this class represents an strategy of test case generation. In particular,
 * this strategy consists of, at first, calculate the finite models for every type which is
 * defined globally in the specification. Then the finite models for the types of the variables
 * declared in the test class. Finally, the strategy iterates over the assigments (taken from
 * the finite models) of values to variables until one of them satisfies the analized test
 * class predicate. In order to perform the evaluations, this class uses the evalSchemeSat()
 * method from the SchemeEvaluator class.
 * @author Pablo Rodriguez Monetti
 */
public class CompleteTCaseStrategy implements TCaseStrategy{

	private int fMSize;

    
    /**
     * Creates an instance of CompleteTCaseStrategy
     * @param fMSize
     */
	public CompleteTCaseStrategy(int fMSize){
		this.fMSize = fMSize;
	}
    
    
    /**
     * Creates an instance of CompleteTCaseStrategy
     * @param fMSize
     */
	public CompleteTCaseStrategy(){

	}    
        
    /**
     * Tries to generate an abstract test case for the specified test class and returns it if
     * the creation was possible, null if not.
     * @param spec
     * @param tClass
     * @return the generated abstract test case 
     */
	public AbstractTCase generateAbstractTCase(Spec spec, TClass tClass){
        
		TypeFMsGenVisitor typeFMsGenVisitor = new TypeFMsGenVisitor(null,fMSize);
		spec.accept(typeFMsGenVisitor);
		Map<Expr,NormalTypeAndFM> typeFMs = typeFMsGenVisitor.getExprMap();
/*
		Set<Map.Entry<Expr, FMPair>> set = typeFMs.entrySet();
		Iterator<Map.Entry<Expr, FMPair>> iterator = set.iterator();


		System.out.println("\nModelos finitos de los tipos globales:");
		while(iterator.hasNext()){
			Map.Entry<Expr, FMPair> mapEntry = iterator.next();
			Expr expr = mapEntry.getKey();
			FMPair fMPair = mapEntry.getValue();
			System.out.println("<--------*********************-------->");
			System.out.println("Expresion");		
			CztPrinter.printExpr(expr,0);
			System.out.println("\ntipo normalizado");	
			CztPrinter.printExpr(fMPair.getNormalType(),0);
			System.out.println("\nModelos finito");	
			CztPrinter.printSetExpr(fMPair.getFM(),0);
		}
		System.out.println("<--------*********************-------->");
*/

        /* We add to varSetExprList the pairs (variable, finite model associated to the
            type of the declared variable) for each variable declared in the test class
         */
		AxPara axPara = tClass.getMyAxPara();

		Map<RefExpr, SetExpr> varSetExprMap = new HashMap<RefExpr, SetExpr>();
		ZFactory zFactory = new ZFactoryImpl();
		ZExprList zExprList = zFactory.createZExprList();

		DeclList declList = SpecUtils.getAxParaListOfDecl(axPara);
		if(declList instanceof ZDeclList){
			ZDeclList zDeclList = (ZDeclList)declList;
			for(int j=0; j< zDeclList.size(); j++){
				Decl decl = ((ZDeclList)declList).get(j);
				if(decl instanceof VarDecl){
					VarDecl varDecl = (VarDecl)decl;
					Expr expr = varDecl.getExpr();
					NormalTypeAndFM fMPair = typeFMs.get(expr);
					if(fMPair == null){
						try{
                            // If the declaration type is not equal to any global type,
                            // we calculate its finite model and normalized type
							fMPair = expr.accept(typeFMsGenVisitor);
						}
						catch(Exception e){
							e.printStackTrace();
						}
						typeFMs.put(expr, fMPair);
					}
                    // If it could not be calculated, we return null.
					if(fMPair == null){
						System.out.println("El tipo NO tiene modelo finito");
						return null;
					}
					SetExpr setExpr = fMPair.getFM();

					NameList nameList = varDecl.getName();
					if (nameList instanceof ZNameListImpl){
						ZNameListImpl zNameListImpl= (ZNameListImpl)nameList;
						for (int k=0; k< zNameListImpl.size(); k++){
							RefExpr refExpr = zFactory.createRefExpr(zNameListImpl.get(k),
											zExprList , false, false);
							varSetExprMap.put(refExpr, setExpr);
						}
					}
				}
			}
		}


        /* We iterate over the elements of the finite model and replace the variables with 
         * them until either we find and assignment that satisfies the test class predicate or
         * there is no more assignements to do.
         */
		Set<Map.Entry<RefExpr, SetExpr>> set = varSetExprMap.entrySet();

		Object[] mapArray =  set.toArray();

		int varSetExprMapSize = set.size();
		int[] sizes = new int[varSetExprMapSize];
		int[] currentIndex = new int[varSetExprMapSize];
		int product = 1;

		for(int i=0; i<varSetExprMapSize; i++){
			Object object = mapArray[i];
			Map.Entry<RefExpr, SetExpr> entry = (Map.Entry<RefExpr, SetExpr>) object;
			sizes[i] = entry.getValue().getZExprList().size();
			product *= sizes[i];
			currentIndex[i] = 0;
		}


		EvaluationResp evaluationResp;
		boolean satisfied = false;
		SchemeEvaluator schemeEvaluator = new SchemeEvaluator();
		Map<RefExpr, Expr> varExprMap = null;
		for(int i=0; i<product && !satisfied; i++){
			ZExprList tupleZExprList = zFactory.createZExprList();
			varExprMap = new HashMap<RefExpr, Expr>();

			for(int j=0; j<varSetExprMapSize; j++){
				Map.Entry<RefExpr, SetExpr> varSetExprMapping = (Map.Entry<RefExpr, SetExpr>) mapArray[j];
				RefExpr refExpr = varSetExprMapping.getKey();
				Expr expr = varSetExprMapping.getValue().getZExprList().get(currentIndex[j]);
				varExprMap.put(refExpr,expr);
			}

			evaluationResp = schemeEvaluator.evalSchemeSat(tClass, varExprMap);

			satisfied = evaluationResp.getResult();

			currentIndex[0]++;
			for(int j=0; j<varSetExprMapSize-1; j++){
				if(currentIndex[j] == sizes[j]){
					currentIndex[j+1]++;
					currentIndex[j] = 0;
				}
			}
		}

		AbstractTCase abstractTCase = null;
		if(satisfied){	
			abstractTCase = new AbstractTCaseImpl(axPara, tClass.getSchName(), varExprMap);
			AxPara abstractTCaseTCaseAxPara = abstractTCase.getMyAxPara();
		}
		
		return abstractTCase;

	}
    
    /**
     * Sets the size of the finite models.
     * @param fMSize
     */
    public void setFMSize(int fMSize){
        this.fMSize = fMSize;
    }
    
    
    /**
     * Gets the size of the finite models.
     * @return
     */
    public int getFMSize(){
        return fMSize;
    }
    private static final long serialVersionUID = 1L;
    
}
