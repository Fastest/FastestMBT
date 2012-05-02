package compserver.prunning.typechecking;

import java.util.*;
import compserver.prunning.TheoremsControl;
import compserver.prunning.Theorem;
import compserver.prunning.Variable;
import common.z.TClass;
import common.repository.AbstractIterator;

import net.sourceforge.czt.z.ast.Name;
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
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.SectTypeEnvAnn;
import net.sourceforge.czt.z.ast.ZFactory;
import common.util.MathUtils;
import common.z.czt.visitors.ExpressionsExtractor;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.DeleteParenAnn;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Type;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.TypeAnn;
import net.sourceforge.czt.z.ast.PowerType;
import net.sourceforge.czt.z.ast.ProdType;
import net.sourceforge.czt.z.ast.GivenType;

import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.session.Markup;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import common.z.czt.UniqueZLive;
import net.sourceforge.czt.session.*;
import client.blogic.management.Controller;

import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.AxPara;

/**
 * An instance of this class allows the searching of theorems whose arguments
 * match with the arguments of a TClass.
 */
public class ParamExaminerPred{

	public ParamExaminerPred()
	{
		// This structure stores a map between formal parameters and real parameters
		mapFR = new HashMap<String,Type>();
		powerMap = new HashMap<String,List<List<Expr>>>();
		permutationsMap = new HashMap<String,List<List<Expr>>>();
		// This structure stores the list of parameters that pass the typecking stage
		// for a fixed theorem signature
		signaturesCalculated = new HashMap<TheoremSignature,List<List<Expr>>>();
	}

	public void setTClass(TClass tClass)
	{
		//System.out.println("Seteos de clase");
		//Calendar cal = Calendar.getInstance();
		//long inicio = cal.getTimeInMillis();
		constantsList = new ArrayList<Expr>();
		varList = new ArrayList<Expr>();

		// We obtain the DeclList of the tClass
		DeclList declList = SpecUtils.getAxParaListOfDecl(tClass);
		zDeclListTClass = null;
		if(declList instanceof ZDeclList)
			zDeclListTClass = (ZDeclList) declList;
		//zDeclListTClass = SpecUtils.expandDeclList(zDeclListTClass);
		zDeclListTClass = expandDeclList(zDeclListTClass);
		String tClassName = SpecUtils.getAxParaName(tClass);

		// We obtain the name of the operation being anlyzed
		rootName = tClassName.substring(0,tClassName.indexOf("_"));
		AxPara axParaAux = tClass.getMyAxPara();
		Pred tClassPred = SpecUtils.getAxParaPred(axParaAux);
		strPred = SpecUtils.termToLatex(tClassPred);
		// We obtain the expressions in the predicate of tClass and look for
		// constants expressions
		Map<Expr,Type> map = tClassPred.accept(new ExpressionsExtractor());
		//cal = Calendar.getInstance();
		//long fin1 = cal.getTimeInMillis();
		//System.out.println("Extrajo todas las expresiones en: "+(fin1-inicio));
            	Set<Map.Entry<Expr, Type>> set = map.entrySet();
            	Iterator<Map.Entry<Expr, Type>> iterator = set.iterator();
		List<Map.Entry<Expr,Type>> list = new ArrayList<Map.Entry<Expr,Type>>();
		

		//List<String> auxStrExprList = new ArrayList<String>();
		List<Expr> auxExprList = new ArrayList<Expr>();

		while(iterator.hasNext()){
               		Map.Entry<Expr, Type> mapEntry = iterator.next();
                	Expr auxExpr = mapEntry.getKey();
			auxExpr = (Expr) auxExpr.accept(new DeleteParenAnn());
			/*String strExpr = SpecUtils.termToLatex(auxExpr);
			boolean exists = false;
			for(int i=0;i<auxStrExprList.size() && !exists;i++){
				if(auxStrExprList.get(i).equals(strExpr))
					exists = true;
			}*/
			boolean exists = false;
			for(int i=0;i<auxExprList.size() && !exists;i++){
				if(SpecUtils.areEqualTerms(auxExprList.get(i),auxExpr))
					exists = true;
			}
			if(!exists){
			//auxStrExprList.add(strExpr);
			auxExprList.add(auxExpr);
			Type auxType = mapEntry.getValue();
			//System.out.println("EXPR: "+tClassName);
			//System.out.println(SpecUtils.termToLatex(auxExpr)+"\n"+SpecUtils.termToLatex(auxType));
			if(SpecUtils.isConstant(zDeclListTClass, auxExpr)){
				constantsList.add(auxExpr);
			}
			else{
				//System.out.println("Es variable "+ SpecUtils.termToLatex(auxExpr));
				//varList.add(auxExpr);
			}
			varList.add(auxExpr);
			}
		}
		//cal = Calendar.getInstance();
		//long fin2 = cal.getTimeInMillis();
		//System.out.println("Termino de colocarlas en la estructura en: "+(fin2-fin1));
		powerSet = getPowerSet(varList, tClassName, maxCard);
		/*for(int i=0;i<powerSet.size();i++){
			System.out.println("\nConjunto "+i);
			for(int j=0;j<powerSet.get(i).size();j++)
				System.out.print(SpecUtils.termToLatex(powerSet.get(i).get(j))+", ");
		}*/
		//cal = Calendar.getInstance();
		//long fin3 = cal.getTimeInMillis();
		//System.out.println("Conjunto de partes en: "+(fin3-fin2));
	}

	public void getTheorems()
	{
		TheoremsControl theoremsControl = TheoremsControl.getInstance();
		maxCard = theoremsControl.getMaxCardinality();
		theoremsIt = theoremsControl.createIterator();
	}

	// This operation is used only for the command searchtheorems
    /**
     * Returns a map with the theorems names and the respective combinations of expressions 
     * in the TClass that match with his signature 
     * @param tClass
     * @return the list of combinations 
     */
	public Map<String, List<List<Expr>>> searchTheorems(TClass tClass)
	{
		Map<String, List<List<Expr>>> theoremsMatch = new HashMap<String, List<List<Expr>>>();
		ZFactory zFactory = new ZFactoryImpl();
		// We travel the repository with the Theorems and search that theorems whose
		// arguments match with tClass
		while(theoremsIt.hasNext()){
			List<List<Expr>> zListMatch = new ArrayList<List<Expr>>();
			Theorem theTheorem = theoremsIt.next();
			int paramSize = 0;
			List<Variable> formalParamList = theTheorem.getFormalParamList();
			for(int i=0;i<formalParamList.size();i++){
				Variable auxVar = formalParamList.get(i);
				String type = auxVar.getType();
				if(!type.startsWith("\\const"))
					paramSize++;
			}
			constantsSize = formalParamList.size()- paramSize;
			String theoremName = theTheorem.getName();
			ZDeclList zTheoremDeclList = (ZDeclList)theTheorem.getZDeclList();
			List<List<Expr>> powerSetN = new ArrayList<List<Expr>>();
			for(int i=0;i<powerSet.size();i++){
				if(powerSet.get(i).size()==paramSize)
					powerSetN.add(powerSet.get(i));
			}
			List<List<Expr>> permutations = null;
			if(powerSetN.size()>0)
				permutations = getPermutations(powerSetN, tClassName);
			if(permutations!=null){
			// We create a ZDeclList with the VarDecl that no represent constants
			ZDeclList zListNoCtes = zFactory.createZDeclList();
			for(int i=0;i<zTheoremDeclList.size();i++){
			Decl decl = zTheoremDeclList.get(i);
			if(decl instanceof VarDecl){
			VarDecl varDecl = (VarDecl) decl;
			Expr formalExpr = varDecl.getExpr();
			if(formalExpr instanceof RefExpr){
				RefExpr refExpr = (RefExpr) formalExpr;
				String refExprName = refExpr.getZName().getWord().toString();
				if(!refExprName.startsWith("CONST"))
					zListNoCtes.add(decl);
			}
			else
				zListNoCtes.add(decl); // REVISAR!!!
			}
			}
			boolean match = false; 
			for(int i=0;i<permutations.size();i++){
				List<Expr> listExpr = permutations.get(i);
				if(matchSignatures(zListNoCtes,listExpr)){
					if(formalParamList.size()>paramSize){
					List<List<Expr>> listWithConstants = addConstants(zTheoremDeclList, constantsList, listExpr);
					if(listWithConstants!=null){
					match = true; 
					zListMatch.addAll(listWithConstants);
					}
					}
					else{ 
						match = true;
						zListMatch.add(listExpr);
					}
				}
				mapFR.clear();
				}
				if(match)
					theoremsMatch.put(theTheorem.getName(), zListMatch);
			}
			}
		
		return theoremsMatch;
	}

    /**
     * Returns the combinations of expressions in the TClass that match with the signature of
     * the next theorem in the thorems's repository
     * @param tClass
     * @return the list of combinations 
     */
	public Map<String, List<List<Expr>>> searchTheoremsParam(TClass tClass)
	{
		Map<String, List<List<Expr>>> theoremsMatch = new HashMap<String, List<List<Expr>>>();
		List<List<Expr>> zListMatch = new ArrayList<List<Expr>>();

		ZFactory zFactory = new ZFactoryImpl();
		// We obtain the next theorem in the repository and search that theorems whose
		// arguments match with tClass
		if(theoremsIt.hasNext()){
			Theorem theTheorem = theoremsIt.next();
			List<List<List<String>>> reservedWords = theTheorem.getReservedWords();
			boolean reservedsInPredicate = true; 
			// We check if the necessary operators are present in the predicate
			for(int x1=0;x1<reservedWords.size() && reservedsInPredicate;x1++){
				List<List<String>> alterna = reservedWords.get(x1);
				boolean someAlternative = false;
				for(int x2=0;x2<alterna.size() && !someAlternative;x2++){
					List<String> subList = alterna.get(x2);
					boolean thisAlternative = true;
					for(int x3=0;x3<subList.size() && thisAlternative;x3++){
						if(!strPred.contains(subList.get(x3)))
						thisAlternative = false;
					}
					if(thisAlternative)
						someAlternative = true;
				}
				if(!someAlternative)
					reservedsInPredicate = false;
			}

			// If the operators are present we do the analysis
			if(reservedsInPredicate){
			int paramSize = 0;
			List<Variable> formalParamList = theTheorem.getFormalParamList();
			for(int i=0;i<formalParamList.size();i++){
				Variable auxVar = formalParamList.get(i);
				String type = auxVar.getType();
				if(!type.startsWith("\\const"))
					paramSize++;
			}
			constantsSize = formalParamList.size()- paramSize;
			String theoremName = theTheorem.getName();
			ZDeclList zTheoremDeclList = (ZDeclList)theTheorem.getZDeclList();
			ZDeclList zListNoCtes = zFactory.createZDeclList();
			for(int i=0;i<zTheoremDeclList.size();i++){
			Decl decl = zTheoremDeclList.get(i);
			if(decl instanceof VarDecl){
			VarDecl varDecl = (VarDecl) decl;
			Expr formalExpr = varDecl.getExpr();
			if(formalExpr instanceof RefExpr){
				RefExpr refExpr = (RefExpr) formalExpr;
				String refExprName = refExpr.getZName().getWord().toString();
				if(!refExprName.startsWith("CONST"))
					zListNoCtes.add(decl);
			}
			else
				zListNoCtes.add(decl);
			}
			}
			TheoremSignature theoremSignature = new TheoremSignature(zTheoremDeclList);
			// We check if the analasys was do for this particular signature
			if(signaturesCalculated.containsKey(theoremSignature)){
				List<List<Expr>> calculatedList = signaturesCalculated.get(theoremSignature);
				if(calculatedList!=null){
					theoremsMatch.put(theTheorem.getName(), calculatedList);
					}
			}
			else{
			List<List<Expr>> powerSetN = new ArrayList<List<Expr>>();
			for(int i=0;i<powerSet.size();i++){
				if(powerSet.get(i).size()==paramSize)
					powerSetN.add(powerSet.get(i));
			}
			List<List<Expr>> permutations = null;
			if(powerSetN.size()>0)
				permutations = getPermutations(powerSetN, tClassName);
			boolean match = false; // El match es para ver si alguno matcheo
			if(permutations!=null){
			// Obtengo una referencia al repositorio de trabajo
			PruningWorkRepository workRepository = PruningWorkRepository.getInstance();
			Map<String, Map<PairDeclParam, Boolean>> workMap = workRepository.getMapExpressions();
			for(int i=0;i<permutations.size();i++){
				//System.out.println("Permutacion "+i);
				List<Expr> listExpr = permutations.get(i);
				//System.out.println("Prueba con:");
				//for(int j=0;j<listExpr.size();j++)
				//System.out.println(SpecUtils.termToLatex(listExpr.get(j)));
				PairDeclParam auxPair = new PairDeclParam(zListNoCtes, listExpr);
				// Chequeo si ya se calculo para alguna clase con la misma raiz
				boolean calculated = true;
				boolean boolCalculated = false;
				synchronized(workMap){
				if(workMap.containsKey(rootName)){
					Map<PairDeclParam, Boolean> entryRoot = workMap.get(rootName);
					synchronized(entryRoot){
					if(entryRoot.containsKey(auxPair)){
					boolCalculated = entryRoot.get(auxPair).booleanValue();
					}
					else{
					calculated = false;
					}
					}
				}
				else
					calculated = false;
				}
				if(calculated){
					if(boolCalculated){
					if(formalParamList.size()>paramSize){
					// We set the value of the generics formal parameters
					// of the theorem
					setMapFR(zTheoremDeclList, listExpr);
					List<List<Expr>> listWithConstants = addConstants(zTheoremDeclList, constantsList, listExpr);
					if(listWithConstants!=null){
					match = true; 
					zListMatch.addAll(listWithConstants);
					}
					else{
					// Chequear validez
					boolCalculated = false;
					}
					}
					else{ 
						match = true;
						zListMatch.add(listExpr);
					}
					}
				}
				else{
				if(matchSignatures(zListNoCtes,listExpr)){
					boolCalculated = true;
					if(formalParamList.size()>paramSize){
					List<List<Expr>> listWithConstants = addConstants(zTheoremDeclList, constantsList, listExpr);
					if(listWithConstants!=null){
					match = true; 
					//boolCalculated = true;
					zListMatch.addAll(listWithConstants);
					}
					}
					else{
						match = true;
						//boolCalculated = true;
						zListMatch.add(listExpr);
					}
				}

				synchronized(workMap){
				if(workMap.containsKey(rootName)){
					Map<PairDeclParam, Boolean> entryRoot = workMap.get(rootName);
					entryRoot.put(auxPair,new Boolean(boolCalculated));
				}
				else{
					//Map<PairDeclParam, Boolean> newEntryRoot = new HashMap<PairDeclParam, Boolean>(10000);
					Map<PairDeclParam, Boolean> newEntryRoot = Collections.synchronizedMap(new HashMap<PairDeclParam, Boolean>(10000));
					newEntryRoot.put(auxPair,new Boolean(boolCalculated));
					workMap.put(rootName,newEntryRoot);
				}

				}
				}
				mapFR.clear();
				}
				//mapFR.clear();
				/*if(zListMatch.size()>0)
					signaturesCalculated.put(theoremSignature, zListMatch);
				else
					signaturesCalculated.put(theoremSignature, null);

				if(match){
					theoremsMatch.put(theTheorem.getName(), zListMatch);
					}*/
			}
			else{
					List<List<Expr>> listWithConstants = addConstants(zTheoremDeclList, constantsList, new ArrayList<Expr>());
					if(listWithConstants!=null){
					match = true; 
					zListMatch.addAll(listWithConstants);
					}
			}
				if(zListMatch.size()>0)
					signaturesCalculated.put(theoremSignature, zListMatch);
				else
					signaturesCalculated.put(theoremSignature, null);

				if(match){
					theoremsMatch.put(theTheorem.getName(), zListMatch);
					}

			}
			return theoremsMatch;
			}
			else{
				theoremsMatch.put(theTheorem.getName(), zListMatch);
				return theoremsMatch;
			}
			}
			return null;
	}

	private boolean matchSignatures(ZDeclList zDeclList, List<Expr> varList)
	{
		boolean match = true;
		for(int i=0;i<zDeclList.size() && match;i++){
			Decl decl = zDeclList.get(i);
			if(decl instanceof VarDecl){
			VarDecl varDecl = (VarDecl) decl;
			Expr formalExpr = varDecl.getExpr();
			Expr realExpr = varList.get(i);
			TypeAnn typeAnn = realExpr.getAnn(TypeAnn.class);
			Type realType = typeAnn.getType();
			match = analizeParams(formalExpr, realType);
			if(match){
				match = SubTyping.areCompatible(zDeclListTClass, formalExpr, realExpr);
			}
			}
		}
		return match;
	}

	private boolean analizeParams(Expr formalExpr, Type realType)
	{
		boolean match = true;
		if(formalExpr instanceof PowerExpr){
			PowerExpr powerExpr = (PowerExpr) formalExpr;
			if(realType instanceof PowerType){
				PowerType powerType = (PowerType) realType;
				match = analizeParams(powerExpr.getExpr(), powerType.getType());
				/*if(!(powerType.getType() instanceof ProdType)){
					match = analizeParams(powerExpr.getExpr(), powerType.getType());
				}
				else
					match = false;	*/
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
						match = analizeParams(refExpr.getZExprList().get(0) ,prodType.getType().get(1));
					}
					else if(prodType.getType().size() == refExpr.getZExprList().size()){
						for(int i=0;i<prodType.getType().size() && match;i++){
							match = analizeParams(refExpr.getZExprList().get(i) ,prodType.getType().get(i));
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
							match = analizeParams(prodExpr.getZExprList().get(i) ,prodType.getType().get(i));
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

	private void setMapFR(ZDeclList zDeclList, List<Expr> listExpr)
	{
		int i = 0;
		for(int j=0;j<zDeclList.size();j++){
			Decl decl = zDeclList.get(j);
			if(decl instanceof VarDecl){
			VarDecl varDecl = (VarDecl) decl;
			Expr formalExpr = varDecl.getExpr();
			boolean isConstant = false;
			if(formalExpr instanceof RefExpr){
				RefExpr refExpr = (RefExpr) formalExpr;
				String refExprName = refExpr.getZName().getWord().toString();
				if(refExprName.startsWith("CONST"))
					isConstant = true;
			}
			if(!isConstant){
				Expr realExpr = listExpr.get(i);
				TypeAnn typeAnn = realExpr.getAnn(TypeAnn.class);
				Type realType = typeAnn.getType();
				setMapFRRecursive(formalExpr, realType);
				i++;
			}
			}
		}
	}

	private void setMapFRRecursive(Expr formalExpr, Type realType)
	{
		if(formalExpr instanceof PowerExpr){
			PowerExpr powerExpr = (PowerExpr) formalExpr;
			if(realType instanceof PowerType){
				PowerType powerType = (PowerType) realType;
				setMapFRRecursive(powerExpr.getExpr(), powerType.getType());
			}
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
						setMapFRRecursive(refExpr.getZExprList().get(0) ,prodType.getType().get(1));
					}
					else if(prodType.getType().size() == refExpr.getZExprList().size()){
						for(int i=0;i<prodType.getType().size();i++){
							setMapFRRecursive(refExpr.getZExprList().get(i), prodType.getType().get(i));
						}
					}
				}
				}
			}
			else if(!refExpr.getMixfix() && !refExpr.getExplicit()){
			String refExprName = refExpr.getZName().getWord().toString();
			// No es reservada entonces es tipo generico
			boolean isReserved = false;
			for(int i=0; i< UtilSymbols.getNumOfSymbols() && !isReserved;i++)
				if(refExprName.equals(UtilSymbols.getSymbol(i)))
					isReserved = true;
			if(!isReserved)
				if(mapFR.get(refExprName)==null)
					mapFR.put(refExprName, realType);
			}
		}
	}


	private List<List<Expr>> getPowerSet(List<Expr> varList, String tClassName, int maxSize)
	{
//		if(powerMap.get(tClassName)==null){
//			System.out.println("No estaba calculado!");
			List<List<Expr>> powerSet = MathUtils.getPowerSet(varList, maxSize);
//			powerMap.put(tClassName,powerSet);
			return powerSet;
//		}
//		else
//			return powerMap.get(tClassName);
	}


	private ZDeclList expandDeclList(ZDeclList zList)
	{
		ZFactory zFactory = new ZFactoryImpl();
		for(int i=0;i<zList.size();i++){
			Decl auxDecl = zList.get(i);
			if(auxDecl instanceof VarDecl){
				VarDecl auxVarDecl = (VarDecl) auxDecl;
				ZNameList zNameList = auxVarDecl.getZNameList();
				if(zNameList.size()>1){
				Expr type = auxVarDecl.getExpr();
				for(int j=0;j<zNameList.size();j++){
					ZNameList newList = zFactory.createZNameList();
					newList.add(zNameList.get(j));
					VarDecl newVarDecl = zFactory.createVarDecl(newList, type);
					zList.add(newVarDecl);
				}
				Decl elim = zList.remove(i);
				i--;
				}
			}
		}
		return zList;
	}







	private List<List<Expr>> getPermutations(List<List<Expr>> list, String tClassName)
	{
		List<List<Expr>> permutations = new ArrayList<List<Expr>>();
		int paramSize = (list.get(0)).size();  // Agregar condicion fatal
		if(permutationsMap.get(tClassName+"_CARD_"+paramSize)==null){
			for(int i=0;i<list.size();i++){
				List<Expr> auxList = list.get(i);
				permutations.addAll(MathUtils.getPermutations(auxList));
			}
			permutationsMap.put(tClassName+"_CARD_"+paramSize,permutations);
		}
		else
			permutations = permutationsMap.get(tClassName+"_CARD_"+paramSize);
		return permutations;
	}

	private List<List<Expr>> addConstants(ZDeclList zDeclList, List<Expr> ctsList, List<Expr> nonConstantsList)
	{
		List<List<Expr>> combinations = new ArrayList<List<Expr>>();
		List<List<Expr>> powerSet = MathUtils.getPowerSet(ctsList, maxCard);
		List<List<Expr>> powerSetN = new ArrayList<List<Expr>>();
		List<List<Expr>> permutations = new ArrayList<List<Expr>>();
		for(int i=0;i<powerSet.size();i++){
			if(powerSet.get(i).size()==constantsSize)
				powerSetN.add(powerSet.get(i));
		}

		for(int i=0;i<powerSetN.size();i++){
			List<Expr> auxList = powerSetN.get(i);
			permutations.addAll(MathUtils.getPermutations(auxList));
		}
		boolean match = false;
		boolean areConstants = false;
		for(int i=0;i<permutations.size();i++){
			int index = 0;
			boolean found = true;
			List<Expr> constantsList = permutations.get(i);

			List<Expr> withOutConstants = new ArrayList<Expr>();
			for(int k=0;k<nonConstantsList.size();k++)
				withOutConstants.add(nonConstantsList.get(k));
			//System.out.println("En la permutacion");
			for(int j=0;j<zDeclList.size()&&found;j++){
			Decl decl = zDeclList.get(j);
			if(decl instanceof VarDecl){
			VarDecl varDecl = (VarDecl) decl;
			Expr formalExpr = varDecl.getExpr();
			if(formalExpr instanceof RefExpr){
				RefExpr refExpr = (RefExpr) formalExpr;
				String refExprName = refExpr.getZName().getWord().toString();
				if(refExprName.startsWith("CONST")){
				areConstants = true;
				refExprName = refExprName.substring(5);
				if(refExprName.equals(UtilSymbols.getSymbol(3)) || refExprName.equals(UtilSymbols.getSymbol(4)) || refExprName.equals(UtilSymbols.naturalSymbol()) || refExprName.equals(UtilSymbols.integerSymbol())){
				if(constantsList.get(index) instanceof NumExpr){
					withOutConstants.add(j, constantsList.get(index));
					index++;
				}
				else
					found = false;
				}
				else if(mapFR.get(refExprName)!=null){
					if(constantsList.get(index) instanceof RefExpr){
					RefExpr refExprConst = (RefExpr) constantsList.get(index);
					String refExprConstName = refExprConst.getZName().getWord().toString();
					TypeAnn refTypeAnn = refExprConst.getAnn(TypeAnn.class);
					Type refType = refTypeAnn.getType();
					if(SubTyping.isSubType(mapFR.get(refExprName),refType)){
					withOutConstants.add(j, constantsList.get(index));
					index++;
					}
					else
						found = false;
					}
					else
						found = false;
				}
				else
					found = false;

			}
			}
			}

			}
		if(found&&areConstants){
			combinations.add(withOutConstants);
			match = true;
		}
		}
		if(match)
			return combinations;
		else
			return null;
	}

	private Map<String,Type> mapFR;
	private Map<String, List<List<Expr>>> powerMap;
	private Map<String, List<List<Expr>>> permutationsMap;
	private Controller controller;
	private ZDeclList zDeclListTClass;
	private int constantsSize;
	private String tClassName;
	private List<Expr> constantsList;
	private List<Expr> varList;
	AbstractIterator<Theorem> theoremsIt;
	private List<List<Expr>> powerSet;
	private int maxCard;
	private String rootName;
	private String strPred;
	private Map<TheoremSignature,List<List<Expr>>> signaturesCalculated;
}