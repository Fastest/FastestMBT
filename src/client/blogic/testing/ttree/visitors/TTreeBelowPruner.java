package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;
import common.z.TClass;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;


/**
 * An instance of this class allows the pruning of test trees, specifying the
 * name of the test class whose test class children will be pruned.
 * @author Pablo Rodriguez Monetti
 */
public class TTreeBelowPruner implements TTreeVisitor<Boolean>{

	private String schName;
    private boolean decision;   // Determines whether the test class, if exists, will be
                                // pruned or unpruned

    /**
     * Creates new instances of TTreeBelowPruner.
     * @param schName
     * @param unfoldOrder
     */
	public TTreeBelowPruner(String schName, boolean decision){
		this.schName = schName;
        this.decision = decision;
	}



    /**
     * Visits the specified test class node.
     * @param tClassNode
     * @return true, if a test class node under this test class node is pruned.
     * False, otherwise.
     */
	public Boolean visitTClassNode(TClassNode tClassNode){
		TClass tClass = (TClass) tClassNode.getValue();
        AbstractRepository<? extends TTreeNode> tTreeNodeRep = tClassNode.getChildren();
        AbstractIterator<? extends TTreeNode> tTreeNodeIt = tTreeNodeRep.createIterator();
		if(tClass.getSchName().equals(schName)  &&
                !tClassNode.isPruned()){
            while(tTreeNodeIt.hasNext()){
                TTreeNode tTreeNode = tTreeNodeIt.next();
                if(tTreeNode instanceof TClassNode)
                    ((TClassNode) tTreeNode).setPruned(decision);
            }
            return new Boolean(true);
		}


        Boolean result = new Boolean(false);
		while(tTreeNodeIt.hasNext() && result.booleanValue() == false)
            result = tTreeNodeIt.next().acceptVisitor(this);

        return result;
    }



    /*
     * Visits the specified test case node.
     * @param tCaseNode
     * @return always false.
     */
	public Boolean visitTCaseNode(TCaseNode tCaseNode){
        return new Boolean(false);
	}


}


