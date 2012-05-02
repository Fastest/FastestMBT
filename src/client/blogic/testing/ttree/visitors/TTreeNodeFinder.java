package client.blogic.testing.ttree.visitors;

import java.util.Map;
import java.util.HashMap;

import client.blogic.testing.ttree.*;
import common.z.AbstractTCase;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import common.z.TClass;



/**
 * Instances of this class take a testing tree an look for a node whose name match with a 
 * given string an returns it
 */
public class TTreeNodeFinder implements TTreeVisitor<TTreeNode>{

    /**
     * Creates instances of TTreeNodeFinder.
     */
    public TTreeNodeFinder(String nodeName){
	this.nodeName = nodeName;
    }


    /**
     * Visits the specified instance of TClassNode, ordering the recursive visit
     * over its children.
     * @param tClassNode
     * @return
     */
    public TTreeNode visitTClassNode(TClassNode tClassNode){
	// We obtain the name of the TClassNode
	TClass tClass = (TClass) tClassNode.getUnfoldedValue().clone();
	String tClassNodeName = tClass.getSchName();
	if(nodeName.equals(tClassNodeName))
		return tClassNode;

	// We obtain the childrens of this node
        AbstractRepository<? extends TTreeNode> children = tClassNode.getChildren();
        AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();
	TTreeNode tTreeNode = null;
        while(childrenIt.hasNext() && tTreeNode==null){
            tTreeNode = childrenIt.next().acceptVisitor(this);
        }
	return tTreeNode;
    }



    /**
     * @param tCaseNode
     * @return
     */
    public TTreeNode visitTCaseNode(TCaseNode tCaseNode){
	// We obtain the name of this TCaseNode
        AbstractTCase abstractTCase = null;
        abstractTCase = (AbstractTCase) tCaseNode.getValue().clone();
	String tCaseNodeName = abstractTCase.getSchName();
	if(tCaseNodeName.equals(nodeName))
		return tCaseNode;
	else
		return null;
    }

    private String nodeName;
}
