package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;
import common.z.TClass;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;




/**
 * An instance of this class allows the pruning of test trees, specifying the
 * name of the test class where the test tree will be pruned.
 * An instance of this class allows the pruning of test trees, specifying the
 * name of the test class whose node is the dad of the nodes where the test tree
 * will be pruned.
 * @author Pablo Rodriguez Monetti
 */
public class TTreeFromPruner implements TTreeVisitor<Boolean>{

	private String schName;
    private boolean decision;   // Determines whether the test class, if exists,
                                // will be pruned or unpruned

    /**
     * Creates new instances of TTreeFromPruner.
     * @param schName
     * @param unfoldOrder
     */
	public TTreeFromPruner(String schName, boolean decision){
		this.schName = schName;
        this.decision = decision;
	}



    /**
     * Visits the specified test class node.
     * @param tClassNode
     * @return
     */
	public Boolean visitTClassNode(TClassNode tClassNode){
		TClass tClass = (TClass) tClassNode.getValue();
		if(tClass.getSchName().equals(schName) && tClassNode.isPruned() != decision){
            tClassNode.setPruned(decision);
            return new Boolean(true);
		}

        AbstractRepository<? extends TTreeNode> tTreeNodeRep = tClassNode.getChildren();
		AbstractIterator<? extends TTreeNode> tTreeNodeIt = tTreeNodeRep.createIterator();
        Boolean result = new Boolean(false);
		while(tTreeNodeIt.hasNext() && result.booleanValue() == false)
            result = tTreeNodeIt.next().acceptVisitor(this);
        
        return result;
    }     
        
    
    
    /**
     * Visits the specified test case node.
     * @param tClassNode
     * @return always false
     */
	public Boolean visitTCaseNode(TCaseNode tCaseNode){
        return new Boolean(false);
	}


}


