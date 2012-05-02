/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.TTreeNode;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.testing.ttree.TCaseNode;
import common.z.TClass;
import client.blogic.testing.prunning.PrePruner;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;


/**
 * An instance of this class performs the pre-pruning of test trees. An
 * specified test tree is traverse and as soon as a node containing an empty
 * test class is found, it is pruned from the tree.
 * @author Pablo Rodriguez Monetti
 */
public class PrePrunerVisitor implements TTreeVisitor<Boolean>{


    /**
     * Visits the specified test class node.
     * @param tClassNode
     * @return true, if the test tree associated to the specified test tree
     * results pre-pruned after the traversal of this visitor.
     */
	public Boolean visitTClassNode(TClassNode tClassNode){
                TClass tClass = (TClass) tClassNode.getValue();
                TClass tClass2 = (TClass) tClassNode.getUnfoldedValue();
		if(PrePruner.isTClassEmpty(tClass)){
                    tClassNode.setPruned(true);
                    System.out.println("Test Objective "+ tClass.getSchName() +" pruned. Explicit contradiction found.");
                    return new Boolean(true);
		}

                AbstractRepository<? extends TTreeNode> tTreeNodeRep =
                        tClassNode.getChildren();
		AbstractIterator<? extends TTreeNode> tTreeNodeIt =
                        tTreeNodeRep.createIterator();
                Boolean result = new Boolean(false);
		while(tTreeNodeIt.hasNext())
                    result = (tTreeNodeIt.next().acceptVisitor(this)).booleanValue() && result;

                return result;
        }


    /**
     * Visits the specified test case node.
     * @param tCaseNode
     * @return always false
     */
	public Boolean visitTCaseNode(TCaseNode tCaseNode){
            return new Boolean(false);
	}

}
