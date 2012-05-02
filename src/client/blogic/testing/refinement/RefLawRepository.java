package client.blogic.testing.refinement;

import java.io.*;
import java.util.*;

/**
 * The instance of this class (which is a singleton) stores the parsed refinemt laws
 * that were loaded in Fastest
 */

public class RefLawRepository {
    private Map<String,TCRL_AST> refLaws;
    private static RefLawRepository refLawRepository;

    private RefLawRepository(){
        refLaws = new HashMap<String,TCRL_AST>();
    }

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
     public static RefLawRepository getInstance(){	
	if(refLawRepository==null)
		refLawRepository = new RefLawRepository();
	return refLawRepository;
     }

    public void addRefLaw(String refLawName, TCRL_AST ast){
        refLaws.put(refLawName, ast);
    }

    public TCRL_AST getRefLaw(String refLawName){
        return refLaws.get(refLawName);
    }

    public int size(){
        return refLaws.size();
    }

    public void clear(){
        refLaws.clear();
    }

    public Set<String> getRefLawNames(){
        return refLaws.keySet();
    }
}
