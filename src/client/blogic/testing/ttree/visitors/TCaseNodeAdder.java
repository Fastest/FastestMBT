package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;

import common.z.TClass;
import common.z.AbstractTCaseImpl;
import common.z.AbstractTCase;
import common.z.SpecUtils;
import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;


/**
 * Instances of this class make possible the adding of a TCaseNode as a leaf of
 * the specified test tree. The constructor of this class takes the name of the
 * test class which is the data of the node where the TCaseNode will be added as 
 * a son, and the instance of AbstractTCase that corresponds to the unfolded
 * test case that will be part of the added TCaseNode's data. The other 
 * part of the added TCaseNode's data, the unfolded test case,  will be
 * obtained from the folded test case and from the dad node's test class.
 * This class implements a test tree visitor.
 * @author Pablo Rodriguez Monetti
 */
public class TCaseNodeAdder implements TTreeVisitor<Boolean>{
	
	private String tClassName;
	private AbstractTCase foldedAbsTCase;

    /**
     * Creates intances of TCaseNodeAdder.
     * @param tClassName
     * @param foldedAbsTCase
     */
	public TCaseNodeAdder(String tClassName, AbstractTCase foldedAbsTCase){
		this.tClassName = tClassName;
		this.foldedAbsTCase = foldedAbsTCase;
	}

    
    /**
     * Determines if the specified instance of TClassNode has, as its data, the 
     * test class specified by its name in the class' constructor. If so, the
     * instance of AbstractTCase is added as a son and true is returned.  If not,
     * the same is test recursively through the children of the specified 
     * TClassNode, returning false if the search is successful and false otherwise.
     * @param tClassNode
     * @return
     */
	public Boolean visitTClassNode(TClassNode tClassNode){

		TClass tClass = (TClass) tClassNode.getUnfoldedValue().clone();
		if(tClass.getSchName().equals(tClassName)){
			AxPara unfoldedAxPara = tClass.getMyAxPara();
			Pred unfoldedPred = SpecUtils.getAxParaPred(unfoldedAxPara);
			AxPara foldedAxPara = foldedAbsTCase.getMyAxPara();
			unfoldedPred = SpecUtils.andPreds(unfoldedPred, SpecUtils.getAxParaPred(foldedAxPara));
			SpecUtils.setAxParaPred(unfoldedAxPara, unfoldedPred);
			SpecUtils.setAxParaName(unfoldedAxPara, SpecUtils.getAxParaName(foldedAxPara));
			AbstractTCase unfoldedAbsTCase = new AbstractTCaseImpl(unfoldedAxPara, foldedAbsTCase.getSchName());
			TTreeNode tCaseNode = new TCaseNode(foldedAbsTCase, unfoldedAbsTCase, tClassNode);
            AbstractRepository<? extends TTreeNode> children = tClassNode.getChildren();
            AbstractRepository<TTreeNode> newChildren = new ConcreteRepository<TTreeNode>();
            AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();
            if(children != null){
                while(childrenIt.hasNext())
                    newChildren.addElement(childrenIt.next());
            }
			newChildren.addElement(tCaseNode);


			tClassNode.setChildren(newChildren);
			return new Boolean(true);
		}
		else{
			AbstractRepository<? extends TTreeNode> tTreeNodeRep = tClassNode.getChildren();
			AbstractIterator<? extends TTreeNode> tTreeNodeIt = tTreeNodeRep.createIterator();
			boolean tClassFound = false;
			while(tTreeNodeIt.hasNext() && tClassFound == false)
				tClassFound = tTreeNodeIt.next().acceptVisitor(this).booleanValue();
			return new Boolean(tClassFound);
		}
	}


    /**
     * Return false because of the failure in the search of the TClassNode which
     * has the specified test class as a data - a test tree leaf has been found.
     * @param tCaseNode
     * @return
     */
	public Boolean visitTCaseNode(TCaseNode tCaseNode){
		return new Boolean(false);
	}
}
