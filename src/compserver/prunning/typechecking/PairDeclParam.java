package compserver.prunning.typechecking;

import java.util.*;

import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.VarDecl;

import common.z.czt.visitors.CZTCloner;
import common.z.SpecUtils;

public class PairDeclParam{

    /**
     * Creates an instance of PairDeclParam.
     * @param signature
     * @param params
     */
	public PairDeclParam(ZDeclList signature, List<Expr> params)
	{
		this.signature = signature;
		this.params = params;
		hash = 0;
		for(int i=0;i<params.size();i++){
			//String strParam = SpecUtils.termToLatex(params.get(i));
			//hash+= strParam.hashCode();
			Expr param = params.get(i);
			hash+= param.hashCode();
		}
		for(int i=0;i<signature.size();i++){
			Decl decl = signature.get(i);
			if(decl instanceof VarDecl){
				VarDecl varDecl = (VarDecl) decl;
				Expr expr = varDecl.getExpr();
				//String strExpr = SpecUtils.termToLatex(expr);
				//hash+= strExpr.hashCode();
				hash+= expr.hashCode();
			}
		}
	}

    /**
     * Returns the signature
     * @return
     */
	public ZDeclList getZDeclList(){
		return signature;
	}

    /**
     * Returns the parameters
     * @return
     */
	public List<Expr> getParameters(){
		return params;
	}

	@Override
	public boolean equals(Object obj) {
		try{
		if (obj instanceof PairDeclParam) {
			PairDeclParam comparedPair = (PairDeclParam) obj;

			ZDeclList comparedZList = comparedPair.getZDeclList();
			// Problemas con los que no son VarDecl???
			if(comparedZList.size()!=signature.size())
				return false;
			List<Expr> comparedParams = comparedPair.getParameters();
			if(comparedParams.size()!=params.size())
				return false;
			// We check for the compatibility of the parameters
			for(int i=0;i<params.size();i++){
				Expr originalExpr = params.get(i);
				Expr comparedExpr = comparedParams.get(i);
				
				if(!SpecUtils.areEqualTerms(originalExpr, comparedExpr)){
					return false;
				}
			}

			//We check for the compatibility of the signatures
			//Map<String,Integer> signCountCompared = new HashMap<String,Integer>();
			Map<Expr,Integer> signCountCompared = new HashMap<Expr,Integer>();
			// No va a funcionar con casos cambiados X por Y
			for(int i=0;i<comparedZList.size();i++){
				Decl decl = comparedZList.get(i);
				if(decl instanceof VarDecl){
					VarDecl varDecl = (VarDecl) decl;
					Expr expr = varDecl.getExpr();
					if(signCountCompared.containsKey(expr)){
						int oldValue = signCountCompared.get(expr).intValue();
						signCountCompared.put(expr, new Integer(oldValue+1));
					}
					else
						signCountCompared.put(expr,new Integer(1)); 
				}
			}
			Map<Expr,Integer> signCountOriginal = new HashMap<Expr,Integer>();
			// No va a funcionar con casos cambiados X por Y
			for(int i=0;i<signature.size();i++){
				Decl decl = signature.get(i);
				if(decl instanceof VarDecl){
					VarDecl varDecl = (VarDecl) decl;
					Expr expr = varDecl.getExpr();
					if(signCountOriginal.containsKey(expr)){
						int oldValue = signCountOriginal.get(expr).intValue();
						signCountOriginal.put(expr, new Integer(oldValue+1));
					}
					else 
						signCountOriginal.put(expr,new Integer(1));
				}
			}

			Set<Map.Entry<Expr, Integer>> set = signCountOriginal.entrySet();
			Iterator<Map.Entry<Expr, Integer>> iterator = set.iterator();
			while(iterator.hasNext()){
				Map.Entry<Expr, Integer> mapEntry = iterator.next();
				Expr refType = mapEntry.getKey();
				int countRefType = mapEntry.getValue().intValue();
				if(signCountCompared.get(refType)==null)
					return false;
				else if(signCountCompared.get(refType).intValue()!=countRefType)
					return false;
			}

			return true;
		}
	return false;
	}
	catch(Exception e){
		System.out.println("Excepcion!");
		e.printStackTrace(System.out);
	}
	return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	private ZDeclList signature;
	private List<Expr> params;
	private int hash;
}