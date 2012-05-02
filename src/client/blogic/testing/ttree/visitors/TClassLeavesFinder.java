package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;
import common.z.TClass;
import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;


/**
 * Instances of this class make possible the traversal over a test tree to 
 * obtain those test classes whose nodes are leaves of the test tree. Those test
 * classes are of interest because they are the source for generation of test
 * cases.
 * @author Pablo Rodriguez Monetti
 */
public class TClassLeavesFinder implements TTreeVisitor<AbstractRepository<TClass>>{
	    
    /**
     * Visit the specified instance of TClassNode and returns the test classes of
     * this subtree that are leaves of the test tree.
     * @param tClassNode
     * @return the test classes of this subtree that are leaves of the test tree.
     */
	public AbstractRepository<TClass> visitTClassNode(TClassNode tClassNode){

		AbstractRepository<TClass> tClassRep = new ConcreteRepository<TClass>();
		
        if(tClassNode.isPruned())
            return tClassRep;
        
        AbstractRepository<? extends TTreeNode> children = tClassNode.getChildren();
		AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();
		// This test class node will be consider a leaf if it has not any child 
        // or every child is a pruned test class node
        boolean isALeaf = true;
		while(childrenIt.hasNext()){
            TTreeNode tTreeNode = childrenIt.next();
            if(tTreeNode instanceof TClassNode && !((TClassNode) tTreeNode).isPruned()){
                    isALeaf = false;
            }
            else if(tTreeNode instanceof TCaseNode){
                isALeaf = false;
            }
            
			AbstractRepository<TClass> tClassChildRep = tTreeNode.acceptVisitor(this);
			if(tClassChildRep != null){
				AbstractIterator<TClass> tClassIt = tClassChildRep.createIterator();
				while(tClassIt.hasNext())
					tClassRep.addElement(tClassIt.next());
			}
		}
		if(isALeaf)
			tClassRep.addElement((TClass) tClassNode.getUnfoldedValue());
		return tClassRep;
	}

    
    /**
     * Since there is not any test class within the test tree under the
     * specified instance of TCaseNode, this method return null.
     * @param tCaseNode
     * @return null
     */
	public AbstractRepository<TClass> visitTCaseNode(TCaseNode tCaseNode){
		return null;
	}
}
