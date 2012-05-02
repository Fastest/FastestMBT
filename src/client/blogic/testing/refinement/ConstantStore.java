package client.blogic.testing.refinement;

import java.util.*;
import net.sourceforge.czt.z.ast.Expr;
import common.z.SpecUtils;


/**
 * The instance of this class (which is a singleton) contains a map between
 * expression in Z and constants. This module is filled for ConstantGenerator
 */
public class ConstantStore {
    
    private static ConstantStore constantStore;
    private Map<ConstantStoreKey,Expr> repository;
    

    /**
     * Creates intances of ConstantStore.
     */
	private ConstantStore(){
		repository = new HashMap<ConstantStoreKey,Expr>();
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static ConstantStore getInstance(){	
		if(constantStore==null)
			constantStore = new ConstantStore();
		return constantStore;
	}


    /**
     * Sets an entry in the repository
     * @return
     */
	public void set(String implID, String constant, Expr expression){
		ConstantStoreKey key = new ConstantStoreKey(implID, constant);
		repository.put(key,expression);
	}
	public void set(ConstantStoreKey key, Expr expression){
		repository.put(key,expression);
	}
    /**
     * Gets an entry of the repository
     * @return
     */
	public Expr get(String implID, String constant){
		ConstantStoreKey key = new ConstantStoreKey(implID, constant);
		return repository.get(key);
	}
	public Expr get(ConstantStoreKey key){
		return repository.get(key);
	}

	public void show(){
		Set<Map.Entry<ConstantStoreKey,Expr>> set = repository.entrySet();
		Iterator<Map.Entry<ConstantStoreKey,Expr>> it = set.iterator();
		while(it.hasNext()){
			Map.Entry<ConstantStoreKey,Expr> mapEntry = it.next();
			ConstantStoreKey key = mapEntry.getKey();
			String id = key.getImplID();
			String constant = key.getConstant();
			Expr expr = mapEntry.getValue();
			System.out.println("ID: "+id);
			System.out.println("Constante: "+constant);
			System.out.println("Expr: "+SpecUtils.termToLatex(expr));
		}
	}

}