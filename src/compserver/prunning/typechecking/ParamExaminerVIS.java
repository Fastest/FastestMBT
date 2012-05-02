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
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.Decl;
import common.z.SpecUtils;
import common.z.UtilSymbols;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.ZFactory;
import common.util.MathUtils;
import common.z.czt.visitors.ExpressionsExtractor;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Type;
import net.sourceforge.czt.z.ast.Ann; // PRUEBA
import net.sourceforge.czt.z.ast.TypeAnn; // PRUEBA


/**
 * An instance of this class allows the searching of theorems whose arguments
 * match with the arguments of a TClass.
 */
public class ParamExaminerVIS{

	public ParamExaminerVIS()
	{
		mapFR = new HashMap<String,Expr>();
		powerMap = new HashMap<String,List<List<Decl>>>();
		permutationsMap = new HashMap<String,List<List<Decl>>>();
	}

	public Map<String,List<ZDeclList>> searchTheorems(TClass tClass)
	{
		Map<String, List<ZDeclList>> theoremsMatch = new HashMap<String, List<ZDeclList>>();


		// We obtain the DeclList of the tClass
		DeclList declList = SpecUtils.getAxParaListOfDecl(tClass);
		ZDeclList zDeclList = null;
		if(declList instanceof ZDeclList)
			zDeclList = (ZDeclList) declList;
		// We obtain an equivalent DeclList in which every VarDecl has his NameList
		// with cardinality equals one
		zDeclList = expandDeclList(zDeclList);


		Pred tClassPred = SpecUtils.getAxParaPred(tClass);
		Map<Expr,Type> map = tClassPred.accept(new ExpressionsExtractor());
		System.out.println("Las expresiones son: ");
            	Set<Map.Entry<Expr, Type>> set = map.entrySet();
            	Iterator<Map.Entry<Expr, Type>> iterator = set.iterator();
		List<Map.Entry<Expr,Type>> list = new ArrayList<Map.Entry<Expr,Type>>();
		while(iterator.hasNext()){
               		Map.Entry<Expr, Type> mapEntry = iterator.next();
                	Expr auxExpr = mapEntry.getKey();
			//Type auxType = mapEntry.getValue();
			TypeAnn typeAnn = auxExpr.getAnn(TypeAnn.class);
			Type auxType = typeAnn.getType();
			System.out.println("El termino:\n"+ SpecUtils.termToLatex(auxExpr));
			System.out.println("Su tipo:\n"+ SpecUtils.termToLatex(auxType));
		}

		// We travel the repository with the Theorems and search that theorms whose
		// arguments "match" with zDeclList
		TheoremsControl theoremsControl = TheoremsControl.getInstance();
		AbstractIterator<Theorem> theoremsIt = theoremsControl.createIterator();
		while(theoremsIt.hasNext()){
			List<ZDeclList> zListMatch = new ArrayList<ZDeclList>();
			Theorem theTheorem = theoremsIt.next();
			int paramSize = (theTheorem.getFormalParamList()).size();
			/*int paramSize = 0;
			List<Variable> formalParamList = theTheorem.getFormalParamList();
			for(int i=0;i<formalParamList.size();i++){
				Variable auxVar = formalParamList.get(i);
				String type = formalParamList.getType();
				if(type.startsWith("\\const"))
					paramSize++;
			}*/
			String theoremName = theTheorem.getName();
			ZDeclList zTheoremDeclList = (ZDeclList)theTheorem.getZDeclList();
			List<List<Decl>> powerSet = getPowerSet(zDeclList, theoremName);
			List<List<Decl>> powerSetN = new ArrayList<List<Decl>>();
			for(int i=0;i<powerSet.size();i++)
				if(powerSet.get(i).size()==paramSize)
					powerSetN.add(powerSet.get(i));

			List<List<Decl>> permutations = getPermutations(powerSetN, theoremName);

			boolean match = false; 
			for(int i=0;i<permutations.size();i++){
				List<Decl> listParam = permutations.get(i);
				ZFactory zFactory = new ZFactoryImpl();
				ZDeclList zDeclListPerm = zFactory.createZDeclList();
				for(int j=0;j<listParam.size();j++)
					zDeclListPerm.add(listParam.get(j));
				if(matchSignatures(zTheoremDeclList,zDeclListPerm)){
					match = true; 
					zListMatch.add(zDeclListPerm);
				}
				}
				if(match)
					theoremsMatch.put(theTheorem.getName(), zListMatch);
			}
		
		return theoremsMatch;
	}

	private boolean matchSignatures(Term formalTerm, Term realTerm)
	{
	boolean match = true;
	ZFactory zFactory = new ZFactoryImpl();

	if(formalTerm instanceof ZDeclList && realTerm instanceof ZDeclList){
		ZDeclList formalZDeclList = (ZDeclList) formalTerm;
		ZDeclList realZDeclList = (ZDeclList) realTerm;
		ZDeclList formalZVarList = zFactory.createZDeclList();
		ZDeclList realZVarList = zFactory.createZDeclList();
		for(int i=0;i<formalZDeclList.size();i++)
			if(formalZDeclList.get(i) instanceof VarDecl)
				formalZVarList.add((VarDecl) formalZDeclList.get(i));
		for(int i=0;i<realZDeclList.size();i++)
			if(realZDeclList.get(i) instanceof VarDecl)
				realZVarList.add((VarDecl) realZDeclList.get(i));
		for(int i=0;i<realZVarList.size() && match;i++)
			match = matchSignatures(formalZVarList.get(i),realZVarList.get(i));
		mapFR.clear();
	}
	else if(formalTerm instanceof VarDecl && realTerm instanceof VarDecl){
		VarDecl formalVarDecl = (VarDecl) formalTerm;	
		VarDecl realVarDecl = (VarDecl) realTerm;
		// Omiti los nombres en esta parte
		Expr formalExpr = formalVarDecl.getExpr();
		Expr realExpr = realVarDecl.getExpr();
		match = matchSignatures(formalExpr, realExpr);
	}
	else if(formalTerm instanceof Expr && realTerm instanceof Expr){
		Expr formalExpr = (Expr) formalTerm;
		Expr realExpr = (Expr) realTerm;
		if(formalExpr instanceof PowerExpr){
			if(realExpr instanceof PowerExpr){
				PowerExpr formalPower = (PowerExpr) formalExpr;
				PowerExpr realPower = (PowerExpr) realExpr;
				Expr formalPowerExpr = formalPower.getExpr();
				Expr realPowerExpr = realPower.getExpr();
				match = matchSignatures(formalPowerExpr, realPowerExpr);
			}
			else
				match = false;
		}

		if(formalExpr instanceof RefExpr){
			RefExpr formalRefExpr = (RefExpr) formalExpr;
			// We check if this RefExpr is an operator application
			if(formalRefExpr.getMixfix() && formalRefExpr.getExplicit()){
				if(realExpr instanceof RefExpr){
					RefExpr realRefExpr = (RefExpr) realExpr;
					if(realRefExpr.getMixfix() && realRefExpr.getExplicit()){
						ZName formalRefZName = formalRefExpr.getZName();
						ZName realRefZName = realRefExpr.getZName();
						String refExprFormalName = formalRefZName.getWord().toString();
						String refExprRealName = realRefZName.getWord().toString();
	// Mismo operador - Misma cantidad de argumentos
						if(refExprFormalName.equals(refExprRealName))
							for(int i=0; i<formalRefExpr.getZExprList().size() && match;i++)
								match = matchSignatures( formalRefExpr.getZExprList().get(i), realRefExpr.getZExprList().get(i));
						else if(SubTyping.isSubType( refExprFormalName, refExprRealName))
							for(int i=0; i<formalRefExpr.getZExprList().size() && match;i++)
								match = matchSignatures( formalRefExpr.getZExprList().get(i), realRefExpr.getZExprList().get(i));
						else
							match = false;

					}
					else
						match = false;
				}
				else
					match = false;
			} // We check if formalRefExpr is a reference
			else if(!formalRefExpr.getMixfix() && !formalRefExpr.getExplicit()){
				String refFormalName = formalRefExpr.getZName().getWord().toString();
				// No es reservada entonces es tipo generico
				boolean isReserved = false;
				for(int i=0; i< UtilSymbols.getNumOfSymbols() && !isReserved;i++)
					if(refFormalName.equals(UtilSymbols.getSymbol(i)))
						isReserved = true;
				if(isReserved){
				if(realExpr instanceof RefExpr){
					RefExpr realRefExpr = (RefExpr) realExpr;
				String refRealName = realRefExpr.getZName().getWord().toString();
				if(!refFormalName.equals(refRealName))
					if(!SubTyping.isSubType(refFormalName, refRealName))
						match = false;
				}
				else
					match = false;
				}
				else{
				if(mapFR.get(refFormalName)==null)
					mapFR.put(refFormalName, realExpr);
				else
					if(!SpecUtils.areEqualTerms(mapFR.get(refFormalName), realExpr))
						match = false;
				}

			}
		}
	}
	else 
		match = false;
	return match;
	}

	private ZDeclList expandDeclList(ZDeclList zList)
	{
		ZFactory zFactory = new ZFactoryImpl();
		//ZDeclList zDeclList = zFactory.createZDeclList();
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

	private List<List<Decl>> getPowerSet(ZDeclList zDeclList, String tClassName)
	{
		if(powerMap.get(tClassName)==null){
			List<List<Decl>> powerSet = MathUtils.getPowerSet(zDeclList);
			powerMap.put(tClassName,powerSet);
			return powerSet;
		}
		else
			return powerMap.get(tClassName);
	}

	private List<List<Decl>> getPermutations(List<List<Decl>> list, String tClassName)
	{
		List<List<Decl>> permutations = new ArrayList<List<Decl>>();
		int paramSize = (list.get(0)).size();
		if(permutationsMap.get(tClassName+"_CARD_"+paramSize)==null){
			for(int i=0;i<list.size();i++){
				List<Decl> auxList = list.get(i);
				permutations.addAll(MathUtils.getPermutations(auxList));
			}
			permutationsMap.put(tClassName+"_CARD_"+paramSize,permutations);
		}
		else
			permutations = permutationsMap.get(tClassName+"_CARD_"+paramSize);
		return permutations;
	}

	private Map<String,Expr> mapFR;
	private Map<String, List<List<Decl>>> powerMap;
	private Map<String, List<List<Decl>>> permutationsMap;
}