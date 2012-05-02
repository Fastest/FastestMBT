package client.blogic.testing.ttree.strategies;

import java.util.*;

import client.blogic.testing.ttree.tactics.*;
import client.blogic.testing.ttree.TClassNode;
import common.z.OpScheme;
import net.sourceforge.czt.z.ast.ZParaList;

/**
 * This interface abstracts the strategy of tactics application which is used
 * to generate the test tree for an operation. TTreeStrategy is based on the 
 * Strategy design pattern. Classes that implements this interface will be 
 * particular strategies. Each strategy determines the way that tactics are
 * applied in order to generate the test tree for the indicated operation.
 * 
 * @author Pablo Rodriguez Monetti
 */
public interface TTreeStrategy {

    /**
     * Generates the test tree for the specified node using the specified
     */
    public TClassNode generateTTree(TClassNode tClassNode, List<Tactic> tacticList);

    /**
     * Generates the TClassNode that is the root of a testing tree
     */
    public TClassNode generateRootNode(OpScheme opScheme, ZParaList zParaList);
}
