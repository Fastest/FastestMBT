package client.blogic.testing.ttree;

import java.util.concurrent.locks.*;
import java.util.*;

import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;

import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.IIComponent;
import client.blogic.management.ii.events.TTreeGenerated;
import client.blogic.management.ii.events.TTreeRequested;
import client.blogic.management.ii.events.Event_;
import client.blogic.management.ii.events.SpecLoaded;
import common.z.OpScheme;
import common.z.OpSchemeImpl;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import common.z.SpecUtils;

import client.presentation.ClientUI;
import client.blogic.management.Controller;
import common.z.VISGen;

/**
 * Instances of this class (although we assume there is only one in the 
 * system) manage events of type TTreeRequested and order the generation of test
 * trees.  
 * @author Pablo Rodriguez Monetti
 */
public class TTreeGen extends IIComponent {

    private Spec spec;
    private String unitToTest;
    private TClassNode tClassNode;
    private List<Tactic> tacticList;
    private TTreeStrategy ttreeStrategy;
    private ZParaList zParaList;
    private Lock myLock;

    /**
     * Creates new instances of TTreeGen.
     */
    public TTreeGen() {
        myLock = new ReentrantLock();
    }

    /**
     * Manages an implicit invocation event.
     * @param event_
     * @throws java.lang.IllegalArgumentException if event_ is neither instance of
     * SpecLoaded nor TTreeRequested.
     */
    public void manageEvent(Event_ event_)
            throws IllegalArgumentException {

        myLock.lock();

        if (event_ instanceof TTreeRequested) {

            TTreeRequested tTreeRequested = (TTreeRequested) event_;
            unitToTest = tTreeRequested.getUnitToTest();
            tacticList = tTreeRequested.getTacticList();
            ttreeStrategy = tTreeRequested.getTTreeStrategy();
            tClassNode = tTreeRequested.getTClassNode();

            if (spec != null) {
                generateTTree2();
            } else {
                System.out.println("The specification was not correctly loaded.");
            }

        } else if (event_ instanceof SpecLoaded) {
            spec = ((SpecLoaded) event_).getSpec();
        } else {
            throw new IllegalArgumentException();
        }
        myLock.unlock();
    }

    private void generateTTree2() {

        if (tClassNode == null) {
            // The test tree must be generated from the beginning
            System.out.println("Generating test tree for '" + unitToTest + "' operation.");

            for (Sect sect : spec.getSect()) {
                if (sect instanceof ZSect) {
                    ParaList paraList = ((ZSect) sect).getParaList();
                    if (paraList instanceof ZParaList) {
                        zParaList = (ZParaList) paraList;
                    }
                }
            }

            AxPara opAxPara = SpecUtils.axParaSearch(unitToTest,
                    (ZParaList) zParaList);
            OpScheme opScheme = new OpSchemeImpl(opAxPara);
            // References to the SectionManager and to the SectionName are
            // obtained
            ClientUI clientUI = getMyClientUI();
            Controller controller = clientUI.getMyController();

            VISGen.setController(controller);
            VISGen.setZParaList(zParaList);

            tClassNode = ttreeStrategy.generateRootNode(opScheme,
                    zParaList);
        }

        TClassNode newNode = ttreeStrategy.generateTTree(tClassNode, tacticList);

        try {
            EventAdmin eventAdmin = EventAdmin.getInstance();
            TTreeGenerated tTreeGenerated = new TTreeGenerated(unitToTest, newNode);
            eventAdmin.announceEvent(tTreeGenerated);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
