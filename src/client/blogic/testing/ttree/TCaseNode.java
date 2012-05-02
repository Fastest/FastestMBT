package client.blogic.testing.ttree;

import client.blogic.testing.ttree.visitors.TTreeVisitor;
import common.repository.AbstractRepository;
import common.z.Scheme;
import common.z.AbstractTCase;
import client.blogic.testing.ttree.tactics.TacticInfo;

/**
 * Represents a test tree node whose data is an abstract test case. More precisely,
 * every instance of TCaseNode maitains two instances of AbstractTCase that are
 * related to the abstract test case associated to the node: one with its 
 * included schemas completely unfolded and the other with its included schemas
 * totally collapsed.
 * @author Pablo Rodriguez Monetti
 */
public class TCaseNode implements TTreeNode {

    private AbstractTCase value;
    private AbstractTCase unfoldedValue;
    private TClassNode dadNode;

    /**
     * Creates new instances of TCaseNode.
     * @param abstractTCase
     * @param unfoldedAbsTCase
     * @param dadNode
     */
    public TCaseNode(AbstractTCase abstractTCase, AbstractTCase unfoldedAbsTCase, 
            TClassNode dadNode) {
        value = abstractTCase;
        unfoldedValue = unfoldedAbsTCase;
        this.dadNode = dadNode;
    }

    /**
     * Sets the children of this node.
     * @param tTreeNodeRep
     * @throws java.lang.IllegalAccessException always, since a test case node 
     * can not have children. It must be a leaf of a test tree.
     */
    public void setChildren(AbstractRepository<? extends TTreeNode> tTreeNodeRep)
            throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Gets the children of this node.
     * @return null. Always, since a test case node can not have any child. 
     * It must be a leaf of a test tree.
     */
    public AbstractRepository<? extends TTreeNode> getChildren() {
        return null;
    }

    /**
     * Sets the node which is the dad of this node.
     * @param dadNode
     */
    public void setDadNode(TClassNode dadNode) {
        this.dadNode = dadNode;
    }

    /**
     * Gets the node which is the dad of this node.
     * @return
     */
    public TClassNode getDadNode() {
        return dadNode;
    }

    /**
     * Accepts a visitor to apply a function to this node.
     * @param <R>
     * @param tTreeVisitor
     * @return
     */
    public <R> R acceptVisitor(TTreeVisitor<R> tTreeVisitor) {
        return tTreeVisitor.visitTCaseNode(this);
    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalAccessException
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return null;
    }

    /**
     * Sets the instance of AbstractTCase related to the folded abstract test 
     * case associated to this object. 
     * @param abstractTCase
     */
    public void setValue(Scheme abstractTCase) {
        value = (AbstractTCase) abstractTCase;
    }

    /**
     * Gets the instance of AbstractTCase related to the folded abstract test
     * case associated to this object.
     * @return
     */
    public AbstractTCase getValue() {
        return value;
    }

    /**
     * Sets the instance of AbstractTCase related to the unfolded abstract test 
     * case associated to this object. 
     * @param abstractTCase
     */
    public void setUnfoldedValue(Scheme abstractTCase) {
        unfoldedValue = (AbstractTCase) abstractTCase;
    }

    /**
     * Gets the instance of AbstractTCase related to the unfolded abstract test 
     * case associated to this object. 
     * @return
     */
    public AbstractTCase getUnfoldedValue() {
        return unfoldedValue;
    }
}
