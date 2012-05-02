package client.blogic.testing.ttree.visitors;

import client.blogic.testing.ttree.*;
import java.io.*;

import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;

import common.z.TClass;
import common.z.SpecUtils;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;

/** Instances of this class make possible the printing of every test class of a
 * specified test tree.
 */
public class TClassNodeTextUIPrinter implements TTreeVisitor<Void> {

    private PrintWriter printer;
    private int unfoldOrder;

    /**
     * Creates instances of TClassNodeTextUIPrinter.
     * @param printer
     * @param unfoldOrder
     */
    public TClassNodeTextUIPrinter(PrintWriter printer, int unfoldOrder) {
        this.printer = printer;
        this.unfoldOrder = unfoldOrder;
    }

    /**
     * Visits the specified instance of TClassNode, printing the content of the
     * associated node and ordering the recursive visit over its children. The
     * printing takes in account the unfoldOrder parameter passed as an argument
     * to he constructor of this class. The schema is printed with as many
     * unfoldings as unfoldOrder indicates, if unfoldOrder is between 0 and the
     * length of the path from the specified instance of TClassNode and the test
     * tree root. If unfoldOrder is less than 0 or greater than that lengh, the 
     * schema is printed completely unfolded.
     * @param tClassNode
     * @return
     */
    public Void visitTClassNode(TClassNode tClassNode) {

        if (tClassNode.isPruned()) {
            return null;
        }

        TClassNode auxTClassNode = tClassNode;

        TClass tClass = null;

        if (unfoldOrder < 0) {
            tClass = (TClass) tClassNode.getUnfoldedValue();
        } else {
            tClass = (TClass) tClassNode.getValue();
        }


        String tClassName = tClass.getSchName();
        if (tClassName.endsWith("_VIS")) {
        } else {
            AxPara axPara = tClass.getMyAxPara();
            Pred pred = SpecUtils.getAxParaPred(axPara);
            for (int i = 0; i < unfoldOrder; i++) {
                auxTClassNode = auxTClassNode.getDadNode();
                if (auxTClassNode == null) {
                    break;
                }
                axPara = auxTClassNode.getValue().getMyAxPara();
                pred = SpecUtils.andPreds(SpecUtils.getAxParaPred(axPara), pred);
            }

            if (pred != null) {
                pred = SpecUtils.simplifyAndPred(pred);
            }

            AxPara tClassAxPara = tClass.getMyAxPara();
            SpecUtils.setAxParaListOfDecl(tClassAxPara, SpecUtils.getAxParaListOfDecl(axPara));
            SpecUtils.setAxParaPred(tClassAxPara, pred);

            printer.println("\n" + SpecUtils.termToLatex(tClassAxPara));

            printer.flush();
        }






        AbstractRepository<? extends TTreeNode> children = tClassNode.getChildren();

        AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();
        while (childrenIt.hasNext()) {
            childrenIt.next().acceptVisitor(this);
        }

        return null;
    }

    /**
     * Does not do anything. It only exists to satisfy this visitor interface.
     * @param tCaseNode
     * @return
     */
    public Void visitTCaseNode(TCaseNode tCaseNode) {
        return null;
    }
}
