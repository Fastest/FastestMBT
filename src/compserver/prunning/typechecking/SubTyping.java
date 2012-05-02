package compserver.prunning.typechecking;

import java.util.*;
import compserver.prunning.Variable;
import common.z.SpecUtils;
import common.z.TClass;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.Pred;
import common.z.UtilSymbols;

import net.sourceforge.czt.z.ast.GivenType;
import net.sourceforge.czt.z.ast.Type;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.Type;
import net.sourceforge.czt.z.ast.TypeAnn;
import net.sourceforge.czt.z.ast.PowerType;
import net.sourceforge.czt.z.ast.ProdType;
import net.sourceforge.czt.z.ast.GivenType;


/**
 * Provides general  utilities related to subtyping
 */
public class SubTyping {

	public static boolean isSubType(String formalRefName, String realRefName){
		if(formalRefName.equals(UtilSymbols.relationSymbol())){
			if(realRefName.equals(UtilSymbols.relationSymbol()) || realRefName.equals(UtilSymbols.totalFunctionSymbol()) || realRefName.equals(UtilSymbols.partialFunctionSymbol()) ||
			realRefName.equals(UtilSymbols.sequenceSymbol()))
				return true;
			else
				System.out.println("Falla!");
		}
		else if(formalRefName.equals(UtilSymbols.integerSymbol())){
			if(realRefName.equals(UtilSymbols.integerSymbol()) || realRefName.equals(UtilSymbols.naturalSymbol()))
				return true;
		}
		else if(formalRefName.equals(UtilSymbols.partialFunctionSymbol())){
			if(realRefName.equals(UtilSymbols.partialFunctionSymbol()) || realRefName.equals(UtilSymbols.sequenceSymbol()) || realRefName.equals(UtilSymbols.totalFunctionSymbol()))
				return true;
		}
		else if(formalRefName.equals(UtilSymbols.totalFunctionSymbol())){
			if(realRefName.equals(UtilSymbols.totalFunctionSymbol()))
				return true;
		}
		return false;
	}

	public static boolean isSubType(Type refType, Type trueType)
	{
		if(refType.equals(trueType))
			return true;
		else{
		if(refType instanceof GivenType){
			GivenType givenRef = (GivenType) refType;
			if(trueType instanceof GivenType){
				GivenType givenTrue = (GivenType) trueType;
				Name refName = givenRef.getName();
				Name trueName = givenTrue.getName();
				if(refName instanceof ZName && trueName instanceof ZName){
					ZName refZName = (ZName) refName;
					ZName trueZName = (ZName) trueName;
					String strRefName = refZName.getWord().toString();
					String strTrueName = trueZName.getWord().toString();
					return checkSubType(strRefName, strTrueName);
				}
				else
					return false;	
			}
			else
				return false;
		}
		}
		return false;
	}

	private static boolean checkSubType(String refType, String trueType)
	{
		if(refType.equals(UtilSymbols.relationSymbol())){
			if(trueType.equals(UtilSymbols.relationSymbol()) || trueType.equals(UtilSymbols.totalFunctionSymbol()) || trueType.equals(UtilSymbols.partialFunctionSymbol()) ||
			trueType.equals(UtilSymbols.sequenceSymbol())){
				return true;
			}
		}
		else if(refType.equals(UtilSymbols.integerSymbol())){
			if(trueType.equals(UtilSymbols.integerSymbol()) || trueType.equals(UtilSymbols.naturalSymbol()))
				return true;
		}
		else if(refType.equals(UtilSymbols.partialFunctionSymbol())){
			if(trueType.equals(UtilSymbols.partialFunctionSymbol()) || trueType.equals(UtilSymbols.sequenceSymbol())|| trueType.equals(UtilSymbols.totalFunctionSymbol()))
				return true;
		}
		else if(refType.equals(UtilSymbols.totalFunctionSymbol())){
			if(trueType.equals(UtilSymbols.totalFunctionSymbol()))
				return true;
		}
		else if(refType.equals(UtilSymbols.getSymbol(4)) || refType.equals(UtilSymbols.getSymbol(3))){
			if(trueType.equals(UtilSymbols.getSymbol(4)) || trueType.equals(UtilSymbols.getSymbol(3)) || trueType.equals(UtilSymbols.integerSymbol()) || trueType.equals(UtilSymbols.naturalSymbol()))
				return true;
		}
		else if(refType.equals(UtilSymbols.sequenceSymbol())){
			if(trueType.equals(UtilSymbols.sequenceSymbol()))
				return true;
		}
		else if(refType.equals(trueType))
			return true;
		return false;	
	}

	// Falta el de num!!!
	public static boolean areCompatible(ZDeclList zDeclListTClass, Expr formalExpr, Expr realExpr)
	{
		Expr varExpr = SpecUtils.getVarExpr(zDeclListTClass, realExpr);
		// If the expression isn't a variable
		if(varExpr==null){
			TypeAnn typeAnn = realExpr.getAnn(TypeAnn.class);
			Type realType = typeAnn.getType();
			if(realType instanceof PowerType){
			PowerType powerType = (PowerType) realType;
			if(powerType.getType() instanceof ProdType){
				if(formalExpr instanceof RefExpr){
				RefExpr refFormalExpr = (RefExpr) formalExpr;
				if(!refFormalExpr.getMixfix() && !refFormalExpr.getExplicit())
					return true;
				String formalOperatorName = refFormalExpr.getZName().getWord().toString();
				if(formalOperatorName.equals(UtilSymbols.relationSymbol()))
					return true;
				else
					return false;
				}
			}
			}
		}
		else if(formalExpr instanceof RefExpr && varExpr instanceof RefExpr){
			RefExpr refFormalExpr = (RefExpr) formalExpr;
			RefExpr refRealExpr = (RefExpr) varExpr;
			if(!refFormalExpr.getMixfix() && !refFormalExpr.getExplicit())
				return true;
			else if(refFormalExpr.getMixfix() && refFormalExpr.getExplicit()){
				String formalOperatorName = refFormalExpr.getZName().getWord().toString();
				String realOperatorName = refRealExpr.getZName().getWord().toString();
				return checkSubType(formalOperatorName, realOperatorName);
			}
		}
		return true;
	}

}