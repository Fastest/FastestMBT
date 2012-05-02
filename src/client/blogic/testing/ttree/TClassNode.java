package client.blogic.testing.ttree;

import client.blogic.testing.ttree.visitors.TTreeVisitor;
import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.z.Scheme;
import common.z.TClass;
import client.blogic.testing.ttree.tactics.TacticInfo;

/**
 * Represents a test tree node whose data is a test class. 
 */
public class TClassNode implements TTreeNode {

    private TClass tClass;
    private TacticInfo tacticInfo;
    private AbstractRepository<? extends TTreeNode> children;
    private TClassNode dadNode;
    private boolean pruned;

    /**
     * Creates new instances of TClassNode.
     * @param tClass
     * @param dadNode
     */
    public TClassNode(TClass tClass, TClassNode dadNode) {
        this.tClass = tClass;
        tacticInfo = null;
        children = new ConcreteRepository<TTreeNode>();
        this.dadNode = dadNode;
    }

    /**
     * Sets the children of this node.
     * @param tTreeNodeRep
     * @throws java.lang.IllegalAccessException.
     */
    public void setChildren(AbstractRepository<? extends TTreeNode> tTreeNodeRep) {
        children = tTreeNodeRep;
    }

    /**
     * Gets the children of this node.
     * @return 
     */
    public AbstractRepository<? extends TTreeNode> getChildren() {
        return children;
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
        return tTreeVisitor.visitTClassNode(this);
    }

    /**
     * Sets the instance of TacticInfo associated to this node. The TacticInfo
     * contains information about the tactic used to generate this node.
     */
    public void setTacticInfo(TacticInfo tacticInfo) {
        this.tacticInfo = tacticInfo;
    }

    /**
     * Gets the instance of TacticInfo associated to this object. The TacticInfo
     * contains information about the tactic used to generate this node.
     */
    public TacticInfo getTacticInfo() {
        return tacticInfo;
    }

    /**
     * Sets the instance of TClass related to the test class associated to this
     * node. 
     * @param abstractTCase
     */
    public void setValue(Scheme tClass) {
        this.tClass = (TClass) tClass;
    }

    /**
     * Gets the instance of TClass related to the test class associated to this
     * node.
     * @return
     */
    public TClass getValue() {
        return tClass;
    }
 

    /**
     * Gets the instance of TClass related to the unfolded test class associated
     * to this object. 
     * @return the instance of TClass related to the unfolded test class 
     * associated to this object. 
     */
    public TClass getUnfoldedValue() {
        return tClass;
    }

    /**
     * Sets the pruning status of this class (ie. whether it is pruned or not) as the specified
     * boolean parameter indicates.
     * @param pruned
     */
    public void setPruned(boolean pruned) {
        this.pruned = pruned;
    }

    /**
     * Gets the pruning status of this class (ie. whether it is pruned or not).
     * @return the prune status of this class.
     */
    public boolean isPruned() {
        return pruned;
    }
}
