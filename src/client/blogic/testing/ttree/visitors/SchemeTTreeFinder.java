package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;

import common.z.SpecUtils;
import common.z.AbstractTCase;
import common.z.Scheme;
import common.z.TClass;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;


/**
 * Instances of this class allow the traversal of a test tree to find an
 * specified test class or test case. The required schema must be given by its
 * name, through the argument passed to the constructor of this class.
 * @author Pablo Rodriguez Monetti
 */
public class SchemeTTreeFinder implements TTreeVisitor<Scheme>{

	private String schName;
	private int unfoldOrder;

    /**
     * Creates new instances of SchemeTTreeFinder.
     * @param schName
     * @param unfoldOrder
     */
	public SchemeTTreeFinder(String schName, int unfoldOrder){
		this.schName = schName;
		this.unfoldOrder = unfoldOrder;
	}



    /**
     * Traverses the subtree, whose root is the specified tClassNode, until a node
     * with a test class called, as schName indicates, is found. Returns the test 
     * class making unfoldOrder unfoldings, if unfoldOrder is between 0 and the 
     * length of the path from tClassNode to the test tree's root; or the test class
     * completely unfolded, if unfoldOrder is less than 0 or greater than the
     * length from the tClassNode to the test tree's root.
     * @param tClassNode
     * @return
     */
	public Scheme visitTClassNode(TClassNode tClassNode){
		//TClass tClass = (TClass) tClassNode.getValue().clone();
		TClass tClass = (TClass) tClassNode.getValue();
		if(tClass.getSchName().equals(schName)){
			if (unfoldOrder < 0)
				return tClassNode.getUnfoldedValue();
			AxPara axPara = tClass.getMyAxPara();
			Pred pred = SpecUtils.getAxParaPred(axPara);
			for(int i=0; i< unfoldOrder; i++){
				tClassNode = tClassNode.getDadNode();
				if(tClassNode == null)
                    break;
				axPara = tClassNode.getValue().getMyAxPara();
				pred = SpecUtils.andPreds(SpecUtils.getAxParaPred(axPara), pred);
			}
                        pred = SpecUtils.simplifyAndPred(pred);
			AxPara tClassAxPara = tClass.getMyAxPara();
			SpecUtils.setAxParaListOfDecl(tClassAxPara, SpecUtils.getAxParaListOfDecl(axPara));
			SpecUtils.setAxParaPred(tClassAxPara, pred);
			return tClass;
		}
		else{
			AbstractRepository<? extends TTreeNode> tTreeNodeRep = tClassNode.getChildren();
			AbstractIterator<? extends TTreeNode> tTreeNodeIt = tTreeNodeRep.createIterator();
			Scheme scheme = null;
			while(tTreeNodeIt.hasNext() && scheme == null)
				scheme = tTreeNodeIt.next().acceptVisitor(this);
			return scheme;
		}

	}
    
    
    
    /**
     * Traverses the subtree, whose root is the specified tCaseNode, until a node
     * with a test case called, as schName indicates, is found. Returns the test 
     * class making unfoldOrder unfoldings, if unfoldOrder is between 0 and the 
     * length of the path from tCaseNode to the test tree's root; or the test case
     * completely unfolded, if unfoldOrder is less than 0 or greater than the
     * length from the tCaseNode to the test tree's root.
     * @param tClassNode
     * @return
     */
	public Scheme visitTCaseNode(TCaseNode tCaseNOde){
		AbstractTCase abstractTCase = (AbstractTCase) tCaseNOde.getValue().clone();
		if(abstractTCase.getSchName().equals(schName)){
			if (unfoldOrder == -1)
				return tCaseNOde.getUnfoldedValue();
			AxPara axPara = abstractTCase.getMyAxPara();
			Pred pred = SpecUtils.getAxParaPred(axPara);
			TTreeNode tTreeNode = tCaseNOde;
			for(int i=0; i< unfoldOrder; i++){
				tTreeNode = tTreeNode.getDadNode();
				if(tTreeNode == null)
					break;
				axPara = tTreeNode.getValue().getMyAxPara();
				pred = SpecUtils.andPreds(SpecUtils.getAxParaPred(axPara), pred);
			}
			AxPara abstractTCaseAxPara = abstractTCase.getMyAxPara();
			SpecUtils.setAxParaListOfDecl(abstractTCaseAxPara, SpecUtils.getAxParaListOfDecl(axPara));
			SpecUtils.setAxParaPred(abstractTCaseAxPara, pred);
			return abstractTCase;
		}
		else
			return null;
	}


	}

