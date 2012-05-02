package compserver.prunning.typechecking;

import java.util.*;

import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.VarDecl;

public class TheoremSignature{

    /**
     * Creates an instance of TheoremSignature.
     * @param zDeclList
     * @param params
     */
	public TheoremSignature(ZDeclList zDeclList)
	{
		this.zDeclList = zDeclList;
		hash = 0;
		for(int i=0;i<zDeclList.size();i++){
			Decl decl = zDeclList.get(i);
			if(decl instanceof VarDecl){
				VarDecl varDecl = (VarDecl) decl;
				Expr expr = varDecl.getExpr();
				hash+= expr.hashCode();
			}
		}
		//System.out.println("El hash es: "+hash);
	}

    /**
     * Returns the signature
     * @return
     */
	public ZDeclList getZDeclList(){
		return zDeclList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TheoremSignature) {
			TheoremSignature auxSignature = (TheoremSignature) obj;
			ZDeclList comparedZList = auxSignature.getZDeclList();
			// Problemas con los que no son VarDecl???
			if(comparedZList.size()!=zDeclList.size())
				return false;
			//We check for the compatibility of the signatures
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
			for(int i=0;i<zDeclList.size();i++){
				Decl decl = zDeclList.get(i);
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
				if(signCountCompared.get(refType).intValue()!=countRefType)
					return false;
			}
			return true;
		}
	return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	private ZDeclList zDeclList;
	private int hash;
}