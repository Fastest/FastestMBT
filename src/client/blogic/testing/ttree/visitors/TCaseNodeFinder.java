package client.blogic.testing.ttree.visitors;

import java.util.Map;
import java.util.HashMap;

import client.blogic.testing.ttree.*;
import common.z.AbstractTCase;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;



/**
 * Instances of this class make possible the printing of the test cases of a
 * specified test tree.
 * @author Hache
 */
public class TCaseNodeFinder implements TTreeVisitor<Map<String, AbstractTCase>>{

    Map<String, AbstractTCase> tcaMap;

    /**
     * Creates instances of TCaseNodeTextUIPrinter.
     * @param 
     */
    public TCaseNodeFinder(){
        tcaMap = new HashMap<String, AbstractTCase>();
    }


    /**
     * Visits the specified instance of TClassNode, ordering the recursive visit
     * over its children.
     * @param tClassNode
     * @return
     */
    @Override
    public Map<String, AbstractTCase> visitTClassNode(TClassNode tClassNode){
        if(tClassNode.isPruned()){
            return this.tcaMap;
        }
        AbstractRepository<? extends TTreeNode> children = tClassNode.getChildren();
        AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();
        while(childrenIt.hasNext()){
            childrenIt.next().acceptVisitor(this);
        }
        return this.tcaMap;
    }



    /**
     * @param tCaseNode
     * @return
     */
    @Override
    public Map<String, AbstractTCase> visitTCaseNode(TCaseNode tCaseNode){
        AbstractTCase abstractTCase = null;
        abstractTCase = (AbstractTCase) tCaseNode.getValue().clone();
        this.tcaMap.put(abstractTCase.getSchName(), abstractTCase);
        return this.tcaMap;
    }

}
