package client.blogic.testing.ttree.tactics;

import java.util.*;

import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.impl.ZFactoryImpl;

import common.z.SpecUtils;
import common.z.TClass;
import common.z.TClassImpl;

import java.math.BigInteger;

/**
 * Implementation of the tactic Disjunctive Normal Form (DNF).
 * @author Pablo Rodriguez Monetti
 */
public class DNFTactic extends AbstractTactic {

    private DNFTacticInfo dnfTacticInfo;

    /**
     * Creates new instances of DNFTactic.
     */
    public DNFTactic() {
        dnfTacticInfo = new DNFTacticInfo();
        description = "* DNF (Disjunctive Normal Form). This tactic is applied "
                + "\n  by default.\n";
        description += "  Usage: addtactic <op_name> DNF\n";
        description += "  where op_name is the name of a selected operation.\n";
    }

    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param vis 
     * @return
     */
    public List<TClass> applyTactic(TClass vis) {
        String opName = SpecUtils.getAxParaName(originalOp.getMyAxPara());

        List<TClass> tClassList = new ArrayList<TClass>();
        AxPara visAxPara = vis.getMyAxPara();

        Pred visPred = SpecUtils.getAxParaPred(visAxPara);

        if (visPred == null) {
            return tClassList;
        }

        List<Pred> conjunctsList = controller.getSchemaDNFPredList(opName);

        int maxNumberOfDNFPredsToMultiply = controller.getMaxDNFPredsToMultiply();


        if (conjunctsList == null) {
            return tClassList;
        }

        if (conjunctsList.size() >= maxNumberOfDNFPredsToMultiply) {
            System.out.println("DNF too large for operation " + opName
                    + "; select other operations to avoid DNF explosion.");
            return tClassList;
        }


        DNFIterator dnfIterator = new DNFIterator(conjunctsList);
        int maxNumberOfChildren = controller.getMaxDNFClasses();
        int maxNumberOfPredsToAnalize = controller.getMaxPredsToAnalize();


        List<Pred> disjunctsList = new ArrayList<Pred>();


        BigInteger size = dnfIterator.getSize();

        BigInteger maxNumOfChildrenBI = BigInteger.valueOf(maxNumberOfChildren);
        BigInteger limit = null;

        // limit will be the minimum between the max number of children and
        // the real number of test classes
        if (((size.compareTo(maxNumOfChildrenBI) == 1))) {
            limit = maxNumOfChildrenBI;
        } else {
            limit = size;
        }

        boolean showCounter = ((limit.compareTo(BigInteger.valueOf(999)) == 1));
        int k = 0;
        while (disjunctsList.size() < maxNumberOfChildren && dnfIterator.hasNext()
                && k < maxNumberOfPredsToAnalize) {

            Pred disjunct = dnfIterator.next();

            disjunct = SpecUtils.simplifyAndPred(disjunct);

            boolean isPredRepeated = false;
            for (int j = 0; j < disjunctsList.size() && !isPredRepeated; j++) {
                Pred jPred = disjunctsList.get(j);
                if (SpecUtils.areEqualTerms(jPred, disjunct)) {
                    isPredRepeated = true;
                }
            }
            if (!isPredRepeated) {
                disjunctsList.add(disjunct);
            }

            if (showCounter) {
                System.out.print("\rAnalysing predicate number " + ((k++) + 1) + ". "
                        + disjunctsList.size() + " test classes generated of at most " + limit + ". ");
            }

        }

        System.out.println();
        if (showCounter) 
            System.out.println();
        
        int numberOfDisjuncts = disjunctsList.size();
        if (numberOfDisjuncts < 2) {
            return tClassList;
        }

        ZFactory zFactory = new ZFactoryImpl();
        for (int i = 0; i < numberOfDisjuncts; i++) {
            Pred disjunct = disjunctsList.get(i);

            ZName zName = zFactory.createZName(vis.getSchName(),
                    zFactory.createZStrokeList(), "IncludedTClass");

            RefExpr refExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(), false, false);

            ZDeclList tClassZDeclList = zFactory.createZDeclList();
            tClassZDeclList.add(0, zFactory.createInclDecl(refExpr));

            AxPara tClassAxPara = SpecUtils.createAxPara(tClassZDeclList, disjunct);
            String tClassName = opName + "_DNF_" + (i + 1);
            TClass newTClass = new TClassImpl(tClassAxPara, tClassName);
            tClassList.add(newTClass);

        }
        return tClassList;
    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param args
     * @return
     */
    public boolean parseArgs(String args) {
        String parts[] = args.split(" ", 2);
        if (parts.length > 2 || parts.length < 2) {
            return false;
        }
        dnfTacticInfo = new DNFTacticInfo();
        return true;
    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalArgumentException
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalArgumentException {

        if (tacticInfo instanceof DNFTacticInfo) {
            dnfTacticInfo = (DNFTacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return dnfTacticInfo;
    }
}
