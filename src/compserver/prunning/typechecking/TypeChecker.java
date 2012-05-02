package compserver.prunning.typechecking;

import java.util.*;
import compserver.prunning.Theorem;
import common.z.TClass;
import compserver.prunning.Variable;

import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.PowerExpr;
import net.sourceforge.czt.z.ast.ProdExpr;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.Decl;
import common.z.SpecUtils;
import common.z.UtilSymbols;
import common.z.czt.visitors.ExpressionsExtractor;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Type;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.TypeAnn;
import net.sourceforge.czt.z.ast.PowerType;
import net.sourceforge.czt.z.ast.ProdType;
import net.sourceforge.czt.z.ast.GivenType;
import net.sourceforge.czt.z.ast.AxPara;
import common.regex.RegExUtils;

/**
 * Check if a list of strings that represents real parameters of a theorem are well defined in
 * the predicate of a test class and if satisfies the theorem signature
 */
public class TypeChecker{

	public TypeChecker(){}

    /**
     * Returns the analysis of the typechecking
     * @param theoremName The name of the theorem
     * @param tClass The test class
     * @param parameters The real parameters
     * @return
     */
	public boolean checkParametersTypes(Theorem theTheorem, TClass tClass, List<String> parameters){
		int count = 0;
		// We try to obtain the expressions that represents the list of parameters
		List<Expr> exprParams = new ArrayList<Expr>();

		//System.out.println("El teorema es: "+theTheorem.getName());
		AxPara axParaAux = tClass.getMyAxPara();
		String tClassName = tClass.getSchName();

		String axParaName = SpecUtils.getAxParaName(axParaAux);


		for(int i=0;i<parameters.size();i++){
			exprParams.add(null);
			//System.out.println("Parametro: "+parameters.get(i)+" de "+axParaName);
		}
		// We obtain the DeclList of the tClass
		DeclList declList = SpecUtils.getAxParaListOfDecl(tClass);
		ZDeclList zDeclListTClass = null;
		if(declList instanceof ZDeclList)
			zDeclListTClass = (ZDeclList) declList;
		zDeclListTClass = SpecUtils.expandDeclList(zDeclListTClass);

		// We obtain the expressions in the predicate of tClass and look for
		// constants expressions


		Pred tClassPred = SpecUtils.getAxParaPred(axParaAux);
		Map<Expr,Type> map = tClassPred.accept(new ExpressionsExtractor());
            	Set<Map.Entry<Expr, Type>> set = map.entrySet();
            	Iterator<Map.Entry<Expr, Type>> iterator = set.iterator();
		//System.out.println("Expresiones: ");
		while(iterator.hasNext() && count < parameters.size()){
               		Map.Entry<Expr, Type> mapEntry = iterator.next();
                	Expr auxExpr = mapEntry.getKey();
			//auxExpr = (Expr) auxExpr.accept(new DeleteParenAnn());
			String strExpr = SpecUtils.termToLatex(auxExpr);
			//if(tClassName.equals("DetectReferenceEvent_NR_179"))
			//	System.out.println(strExpr);
			boolean found = false;
			for(int i=0;i<parameters.size() /*&& !found*/;i++){
					String parRegEx = RegExUtils.addEscapeCharacters("([(][ ])*([~])*"+parameters.get(i)+"([ ][)])*([~])*");
					String exprRegEx = RegExUtils.addEscapeCharacters("([(][ ])*([~])*"+strExpr+"([ ][)])*([~])*");
					//System.out.println("La regula del parametro:\n" + parRegEx);
					//System.out.println("La regular de la expresion:\n"+ exprRegEx);
				//if(strExpr.equals(parameters.get(i))){
				if(strExpr.matches(parRegEx) || parameters.get(i).matches(exprRegEx)){
					found = true;
					if(exprParams.get(i)==null){
	//System.out.println("Va a setear en la posicion "+i+" : "+SpecUtils.termToLatex(auxExpr)+" para "+axParaName);
					exprParams.set(i,auxExpr);
					count++;
					}
				}
				//else
				//	System.out.println("No matche regular");
			}
		}
		// Habian matcheado cadenas que no se correspondian con expresion bien formadas
		/*if(tClassName.equals("DetectReferenceEvent_NR_179")){
			System.out.println("Llega 1: ");
			System.out.println(count);
			System.out.println(parameters.size());
		}*/
		if(count < parameters.size())
			return false;

		// We first check if the real parameters have the correct number of constants
		List<Variable> formalParameters = theTheorem.getFormalParamList();
		for(int i=0;i<formalParameters.size();i++){
			Variable var = formalParameters.get(i);
			String strType = var.getType();
			if(strType.startsWith("\\const"))
				if(!SpecUtils.isConstant(zDeclListTClass, exprParams.get(i)))
					return false;
		}
		//System.out.println("Llega 3: "+axParaName);
		// Now we check the types
		DeclList theoremDecl = theTheorem.getZDeclList();
		ZDeclList theoremZDecl = null;
		if(theoremDecl instanceof ZDeclList)
			theoremZDecl = (ZDeclList) theoremDecl;
		boolean result = matchSignatures(theoremZDecl,exprParams);
		//System.out.println("Llega 4: "+axParaName);
		return result;
	}

	private boolean matchSignatures(ZDeclList zDeclList, List<Expr> varList)
	{
		//Map<String,Type> mapFR = new HashMap<String,Type>();
		mapFR = new HashMap<String,Type>();
		boolean match = true;
		for(int i=0;i<zDeclList.size() && match;i++){
			Decl decl = zDeclList.get(i);
			if(decl instanceof VarDecl){
			VarDecl varDecl = (VarDecl) decl;
			Expr formalExpr = varDecl.getExpr();
			Expr realExpr = varList.get(i);

			TypeAnn typeAnn = realExpr.getAnn(TypeAnn.class);
			Type realType = typeAnn.getType();
			match = analizeParams(formalExpr, realType, mapFR);

			}
		}
		return match;
	}

	private boolean analizeParams(Expr formalExpr, Type realType, Map<String,Type> mapFR)
	{
		boolean match = true;
		if(formalExpr instanceof PowerExpr){
			PowerExpr powerExpr = (PowerExpr) formalExpr;
			if(realType instanceof PowerType){
				PowerType powerType = (PowerType) realType;
				match = analizeParams(powerExpr.getExpr(), powerType.getType(), mapFR);
			}
			else
				match = false;
		}
		else if(formalExpr instanceof RefExpr){
			RefExpr refExpr = (RefExpr) formalExpr;
			// We check if this RefExpr is an operator application
			if(refExpr.getMixfix() && refExpr.getExplicit()){
				if(realType instanceof PowerType){
				PowerType powerType = (PowerType) realType;
				if(powerType.getType() instanceof ProdType){
					ProdType prodType = (ProdType) powerType.getType();

					if(refExpr.getZName().getWord().toString().equals(UtilSymbols.sequenceSymbol())){
						match = analizeParams(refExpr.getZExprList().get(0) ,prodType.getType().get(1), mapFR);
					}
					else if(prodType.getType().size() == refExpr.getZExprList().size()){
						for(int i=0;i<prodType.getType().size() && match;i++){
							match = analizeParams(refExpr.getZExprList().get(i) ,prodType.getType().get(i), mapFR);
						}
					}
					else{
						match = false;
					}
				}
				else
					match = false;	
			}
			else
				match = false;
			}
			else if(!refExpr.getMixfix() && !refExpr.getExplicit()){
			String refExprName = refExpr.getZName().getWord().toString();
			if(refExprName.startsWith("CONST"))
				refExprName = refExprName.substring(5);
			// No es reservada entonces es tipo generico
			boolean isReserved = false;
			for(int i=0; i< UtilSymbols.getNumOfSymbols() && !isReserved;i++)
				if(refExprName.equals(UtilSymbols.getSymbol(i)))
					isReserved = true;
			if(isReserved){
				if(realType instanceof GivenType){
				GivenType givenType = (GivenType) realType;
				if(givenType.getName() instanceof ZName){
				ZName zName = (ZName) givenType.getName();
				String realTypeStr = zName.getWord().toString();
				if(realTypeStr.equals(UtilSymbols.getSymbol(3)) || realTypeStr.equals(UtilSymbols.getSymbol(4)) || realTypeStr.equals(UtilSymbols.naturalSymbol()) || realTypeStr.equals(UtilSymbols.integerSymbol())){
					//System.out.println("Numerico!");
				}
				else
					match = false;
				}
				}
				else
					match = false;
			}
			else{
			if(mapFR.get(refExprName)==null){
				mapFR.put(refExprName, realType);
			}
			else
				if(!SubTyping.isSubType(mapFR.get(refExprName),realType)){
					match = false;
					//System.out.println("Fallo el subtipo");
				}
			}
			}
		}
		else if(formalExpr instanceof ProdExpr){
			ProdExpr prodExpr = (ProdExpr) formalExpr;
			if(realType instanceof ProdType){
				ProdType prodType = (ProdType) realType;
				if(prodType.getType().size() == prodExpr.getZExprList().size()){
						for(int i=0;i<prodType.getType().size() && match;i++){
							match = analizeParams(prodExpr.getZExprList().get(i) ,prodType.getType().get(i), mapFR);
						}
				}

			}
			else
				match = false;
		}
		else
			match = false;
		return match;
	}

     /**
     * Returns the map that contains the mapping used in the typechecking stage between the types of
     * the formal parameters and the type of real parameters
     */
	public Map<String,Type> getMapFR(){
		return mapFR;
	}

	private Map<String,Type> mapFR;

}