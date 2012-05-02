package compserver.tcasegen.strategies;

import java.util.*;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;


/**
 * The instance of this class (which is a singleton) mantains a map with the information of finite models
 * precalculated for the atomic predicates. This finite models are obtained from the original finite model
 * for the predicate filtering those entries that not satisfy the predicate.
 */
public class PreCalculatedFiniteModels{
    
    private static PreCalculatedFiniteModels preCalculatedFiniteModels;
    private Map<PreCalculatedPredicate, List<Map<RefExpr,Expr>>> map;
    
    
    
    /**
     * Creates intances of PreCalculatedFiniteModels.
     */
	private PreCalculatedFiniteModels(){
		map = new HashMap<PreCalculatedPredicate, List<Map<RefExpr,Expr>>>();
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static PreCalculatedFiniteModels getInstance(){	
		if(preCalculatedFiniteModels==null)
			preCalculatedFiniteModels = new PreCalculatedFiniteModels();
		return preCalculatedFiniteModels;
	}

	public List<Map<RefExpr,Expr>> get(PreCalculatedPredicate predicate){
		return map.get(predicate);
	}

	public void put(PreCalculatedPredicate predicate, List<Map<RefExpr,Expr>> list){
		map.put(predicate, list);
	}

	public void clear(){
		map = new HashMap<PreCalculatedPredicate, List<Map<RefExpr,Expr>>>();
	}

}