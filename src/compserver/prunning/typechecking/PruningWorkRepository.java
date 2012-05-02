package compserver.prunning.typechecking;

import java.util.*;

/**
 * The instance of this class (which is a singleton) contains the work repository
 * thats helps to save time in the typechecking stage
 */
public class PruningWorkRepository{
    
    private static PruningWorkRepository pruningWorkRepository;
    private Map<String, Map<PairDeclParam, Boolean>> mapExpressions; 
    
    /**
     * Creates intances of PruningWorkRepository.
     */
	private PruningWorkRepository(){
		//mapExpressions = new HashMap<String, Map<PairDeclParam, Boolean>>();
		mapExpressions = Collections.synchronizedMap(new HashMap<String, Map<PairDeclParam, Boolean>>());
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public static PruningWorkRepository getInstance(){	
		if(pruningWorkRepository==null)
			pruningWorkRepository = new PruningWorkRepository();
		return pruningWorkRepository;
	}

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
	public Map<String, Map<PairDeclParam, Boolean>> getMapExpressions(){	
		return mapExpressions;
	}
}