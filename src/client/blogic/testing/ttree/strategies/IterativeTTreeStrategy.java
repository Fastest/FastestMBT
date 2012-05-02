package client.blogic.testing.ttree.strategies;

import client.blogic.testing.ttree.tactics.*;
import java.util.*;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.z.TClass;
import common.z.VISGen;
import common.z.OpScheme;
import client.blogic.testing.ttree.TClassNode;
import net.sourceforge.czt.z.ast.ZParaList;

/**
 * The instances of this class represents a strategy of tactics application 
 * which is used to generate the test tree for an operation. In particular, 
 * this strategy takes a list of tactics and applies each tactic to each new 
 * generated level of the test tree.
 * @author Pablo Rodriguez Monetti
 */
public class IterativeTTreeStrategy implements TTreeStrategy {

    /**
     * Generates the TClassNode that is the root of a testing tree
     * @param opScheme
     * @param zParaList
     * @param unfoldedParaList
     * @return
     */
    public TClassNode generateRootNode(OpScheme opScheme,
            ZParaList zParaList) {
        
        TClass tClass = VISGen.generateVIS(opScheme);
        return new TClassNode(tClass, null);
    }

    /**
     * Generates the test tree for the specified node using the specified
     * list of tactics.
     * @param opScheme
     * @param tacticList
     * @return
     */
    public TClassNode generateTTree(TClassNode tClassNode, List<Tactic> tacticList) {
        generateTTreeHelper(tClassNode, tacticList);
        return tClassNode;
    }

    private void generateTTreeHelper(TClassNode tClassNode, List<Tactic> tacticList) {
        TClass tClass = tClassNode.getValue();
        boolean anyTacticApplied = false;
        // We traverse the list of tactics until we can apply one of them, that
        // is, until one of them generates new test classes
        for (int i = 0; i < tacticList.size() && !anyTacticApplied; i++) {
            Tactic tactic = tacticList.get(i);
            List<TClass> tClassList = tactic.applyTactic(tClass);
            if (!tClassList.isEmpty()) {
                anyTacticApplied = true;
                TacticInfo tacticInfo = tactic.getTacticInfo();
                tClassNode.setTacticInfo(tacticInfo);

                AbstractRepository<TClassNode> tClassChildrenRep =
                        new ConcreteRepository<TClassNode>();

                for (int j = 0; j < tClassList.size(); j++) {
                    TClass childTClass = tClassList.get(j);
                    TClassNode tClassChildNode = new TClassNode(childTClass
                            ,tClassNode);
                    tClassChildrenRep.addElement(tClassChildNode);
                    List<Tactic> newTacticList = new ArrayList<Tactic>();
                    // We copy the list of tactics without the tactic recently
                    // applied and generates recursively the test subtree
                    for (int k = 0; k < tacticList.size(); k++) {
                        if (k > i) {
                            newTacticList.add(tacticList.get(k));
                        }
                    }
                    generateTTreeHelper(tClassChildNode, newTacticList);
                }
                tClassNode.setChildren(tClassChildrenRep);
            }
        }
    }
}
