package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;
import java.io.*;

import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;

import common.z.AbstractTCase;
import common.z.SpecUtils;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;

/**
 * Instances of this class make possible the printing of the test cases of a
 * specified test tree.
 * @author Pablo Rodriguez Monetti
 */
public class TCaseNodeTextUIPrinter implements TTreeVisitor<Void> {

    private PrintWriter printer;
    private int unfoldOrder;

    /**
     * Creates instances of TCaseNodeTextUIPrinter.
     * @param printer
     * @param unfoldOrder
     */
    public TCaseNodeTextUIPrinter(PrintWriter printer, int unfoldOrder) {
        this.printer = printer;
        this.unfoldOrder = unfoldOrder;
    }

    /**
     * Visits the specified instance of TClassNode, ordering the recursive visit 
     * over its children.
     * @param tClassNode
     * @return
     */
    public Void visitTClassNode(TClassNode tClassNode) {
        if (tClassNode.isPruned()) {
            return null;
        }

        AbstractRepository<? extends TTreeNode> children = tClassNode.getChildren();
        AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();
        while (childrenIt.hasNext()) {
            childrenIt.next().acceptVisitor(this);
        }
        return null;
    }

    /**
     * Visits the specified instance of TCaseNode, printing the content of the
     * associated node . The printing takes in account the unfoldOrder parameter
     * passed as an argument to he constructor of this class. The schema is 
     * printed with as many unfoldings as unfoldOrder indicates, if unfoldOrder
     * is between 0 and the length of the path from the specified instance of
     * TClassNode and the test tree root. If unfoldOrder is less than 0 or
     * greater than that lengh, the schema is printed completely unfolded.
     * @param tCaseNode
     * @return
     */
    public Void visitTCaseNode(TCaseNode tCaseNode) {
        AbstractTCase abstractTCase = null;

        if (unfoldOrder < 0) {
            abstractTCase = (AbstractTCase) tCaseNode.getUnfoldedValue().clone();
        } else {
            abstractTCase = (AbstractTCase) tCaseNode.getValue().clone();
        }


        AxPara axPara = abstractTCase.getMyAxPara();
        Pred pred = SpecUtils.getAxParaPred(axPara);
        TTreeNode tTreeNode = tCaseNode;
        for (int i = 0; i < unfoldOrder; i++) {
            tTreeNode = tTreeNode.getDadNode();
            if (tTreeNode == null) {
                break;
            }
            axPara = tTreeNode.getValue().getMyAxPara();
            pred = SpecUtils.andPreds(SpecUtils.getAxParaPred(axPara), pred);
        }

        if (pred != null) {
            pred = SpecUtils.simplifyAndPred(pred);
        }
        AxPara abstractTCaseAxPara = abstractTCase.getMyAxPara();
        SpecUtils.setAxParaListOfDecl(abstractTCaseAxPara, SpecUtils.getAxParaListOfDecl(axPara));
        SpecUtils.setAxParaPred(abstractTCaseAxPara, pred);

        printer.println("\n" + SpecUtils.termToLatex(abstractTCaseAxPara));
        printer.flush();
        return null;
    }
}
