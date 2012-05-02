package client.blogic.testing.ttree;

import client.blogic.testing.ttree.visitors.TTreeVisitor;
import common.repository.AbstractRepository;
import common.z.Scheme;
import client.blogic.testing.ttree.tactics.TacticInfo;

/**
 * The instances of this interface represents test tree nodes.
 * @author Pablo Rodriguez Monetti
 */
public interface TTreeNode {

    /**
     * Sets the children of this node.
     * @param tTreeNodeRep
     * @throws java.lang.IllegalAccessException
     */
    public void setChildren(AbstractRepository<? extends TTreeNode> tTreeNodeRep) throws IllegalAccessException;

    /**
     * Gets the children of this node.
     * @return 
     */
    public AbstractRepository<? extends TTreeNode> getChildren();

    /**
     * Sets the node which is the dad of this node.
     * @param tClassNode
     */
    public void setDadNode(TClassNode tClassNode);

    /**
     * Gets the node which is the dad of this node.
     * @return
     */
    public TClassNode getDadNode();

    /**
     * Accepts a visitor to apply a function to this node.
     * @param <R>
     * @param tTreeVisitor
     * @return
     */
    public <R> R acceptVisitor(TTreeVisitor<R> tTreeVisitor);

    /**
     * Sets the instance of either the TClass related to the folded test 
     * class associated to this object or the AbstractTCase related to the folded
     * test class associated to this object.
     * @param abstractTCase
     */
    public void setValue(Scheme scheme);

    /**
     * Gets the instance of either the TClass related to the folded test class 
     * associated to this object or the AbstractTCase related to the abstract test
     * case associated to this object.
     * @return
     */
    public Scheme getValue();


    /**
     * Gets the instance of either the TClass related to the unfolded test 
     * class associated to this object or the AbstractTCase related to the 
     * unfolded test class associated to this object.
     * @return
     */
    public Scheme getUnfoldedValue();

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalAccessException
     */
    public void setTacticInfo(TacticInfo tacticInfo) throws IllegalAccessException;

    ;
	
    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo();
}
