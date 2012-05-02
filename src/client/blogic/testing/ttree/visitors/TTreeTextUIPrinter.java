package client.blogic.testing.ttree.visitors;

import java.io.*;
import java.util.*;

import client.blogic.testing.ttree.TTreeNode;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.testing.ttree.TCaseNode;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;



/**
 * Instances of this class make possible the printing of test trees, displaying
 * only the names of test class and test cases. The content of each one is not
 * displayed. The test classes which are pruned are not printed.
 * @author Pablo Rodriguez Monetti
 */
 public class TTreeTextUIPrinter implements TTreeVisitor<Void>{

	private int nroTab = 0;
    private List<Boolean> auxList = new ArrayList<Boolean>();
	private PrintWriter out;

    
    /**
     * Creates instances of TTreeTextUIPrinter.
     * @param out
     */
	public TTreeTextUIPrinter(PrintWriter out){
        this.out = out;
		nroTab = 0;
	}

    
    
    /**
     * Visit the specified instance of TClassNode, printing the associated test 
     * class's name and ordering the recursive application of this visitor to 
     * the children of the specified node, only if the TClassNode is not pruned.
     * @param tClassNode
     * @return
     */
	public Void visitTClassNode(TClassNode tClassNode){
		if(!tClassNode.isPruned()){     
            String tClassStr = tClassNode.getValue().getSchName();
            out.println(getStrTab(nroTab, true) + tClassStr);
            out.flush();
            AbstractRepository<? extends TTreeNode> children = tClassNode.getChildren();	
            AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();
            boolean hasChildren = false;
            int i=0;
            while(childrenIt.hasNext()){
                i++;
                TTreeNode child = childrenIt.next();
                
                // We check if some of the next children is not prune
                boolean someNotPruned = false;
                while(childrenIt.hasNext() && !someNotPruned){
                    TTreeNode tTreeNode = childrenIt.next();
                    if(tTreeNode instanceof TClassNode && 
                            !((TClassNode)tTreeNode).isPruned())
                        someNotPruned = true;                        
                }
                
                childrenIt = children.createIterator();
                
                
                for(int j=0; j<i; j++)
                    childrenIt.next();
                    
                if(someNotPruned)    
                   auxList.add(new Boolean(true));
                else
                   auxList.add(new Boolean(false));
                   
                nroTab++;
                child.acceptVisitor(this);
                nroTab--;
                auxList.remove(auxList.size()-1);
                
                hasChildren = true;
            }
            if(hasChildren){
                String strTab2 = getStrTab(nroTab, false);
                if(strTab2.contains("|"))
                    out.println(strTab2);
            }
        }
        return null;        
        
	}

    
    
    /**
     * Visit the specified instance of TClassNode, printing the associated test 
     * class's name.
     * @param tCaseNode
     * @return
     */
	public Void visitTCaseNode(TCaseNode tCaseNode){
		String tCaseStr = tCaseNode.getValue().getSchName();
		String strTab = getStrTab(nroTab, true);
		out.println(strTab + tCaseStr);
        out.flush();
        return null;
	}


	private String getStrTab(int nroTab, boolean withName){
		String strTab = "";
		for(int i=0; i<nroTab-1; i++){
            if(auxList.get(i).booleanValue())
                strTab += "  |\t";
            else
                strTab += "  \t";
        }
			
		if(nroTab>0 && withName)
			strTab += "  !______";                  
        else if(nroTab>0 && auxList.get(nroTab-1).booleanValue())
            strTab += "  |";
		return strTab;
	}


}