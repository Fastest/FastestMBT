package client.blogic.testing.refinement;

import java.util.*;
import net.sourceforge.czt.z.ast.Pred;

/**
 * Interface from which inherit the modules that obtain the code to initialize the input
 * variables in the refinement stage. There will be an inheritor for each languaje 
 * supported by Fastest in this stage.
 */
public interface RefineExpr{
    /**
     * Returns a list with TCaseAssignment in the corresponding languaje
     * @param rule The rule used to refine
     * @param atcPred The predicate of the abstract test case 
     */
    public List<TCaseAssignment> refineRule(RuleRefinement rule, Pred atcPred);
} 