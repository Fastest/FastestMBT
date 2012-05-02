package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;

/**
 * Instances of the classes that implement this interface make possible the
 * application of a particular visitor to a test tree.
 * @author Pablo Rodriguez Monetti
 * @param <R>
 */

public interface TTreeVisitor<R>{
    /**
     * Visits the instance of the specified TClassNode.
     * @param tClassNode
     * @return
     */
	public R visitTClassNode(TClassNode tClassNode);
    
    
    /**
     * Visits the instance of the specified TCaseNode.
     * @param tCaseNode
     * @return
     */
	public R visitTCaseNode(TCaseNode tCaseNode);

} 
