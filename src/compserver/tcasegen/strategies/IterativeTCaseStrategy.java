package compserver.tcasegen.strategies;

import java.util.ArrayList;
import java.util.Map;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import common.z.AbstractTCase;
import common.z.AbstractTCaseImpl;
import common.z.TClass;
import compserver.tcasegen.eval.SchemeEvaluator;
import compserver.tcasegen.eval.EvaluationResp;
import compserver.tcasegen.fm.TClassFiniteModel;
import compserver.tcasegen.fm.FiniteModelCreator;
import common.z.czt.visitors.NumericConstsExtractor;
import compserver.tcasegen.fm.intfm.IntFiniteModel;
import compserver.tcasegen.fm.natfm.NatFiniteModel;
import compserver.tcasegen.fm.intfm.ZeroIntFiniteModel;
import compserver.tcasegen.fm.natfm.ZeroNatFiniteModel;
import compserver.tcasegen.fm.FiniteModel;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.CZTReplacer;
import common.z.TClassImpl;
import common.z.SpecUtils;


/**
 * An instance of this class represents an strategy of test case generation. In particular,
 * this strategy consists of generating assignations of values to the variables declared in
 * the specified test class until one of them makes the test class predicate satisfiable. 
 * Unlike the CompleTCaseStrategy, this strategy does not need to generate the complete finite
 * model before starting to evalue predicates. Although this strategy is less efficient with 
 * respect to the temporal complexity order, it is highly better with respect to the spacial
 * order. In order to perform the evaluations, this class uses the evalSchemeSat() method 
 * from the SchemeEvaluator class.
 * @author Pablo Rodriguez Monetti
 */
public class IterativeTCaseStrategy implements TCaseStrategy {

    private int fMSize = 3;
    private int btSize = -1;
    private int nzSize = -1;
    private IntFiniteModel intFiniteModel;
    private NatFiniteModel natFiniteModel;
    private Map<Expr, FiniteModel> exprMap;
    private int maxEval;
    // Gives the values of the varibles defined in axiomatic definitions
    private Map<RefExpr, Expr> axDefsValues;
    // Gives the variables, whose type is basic, defined in axiomatic
    // definition
    private Map<String, List<String>> basicAxDefs;

    /**
     * Creates instances of IterativeTCaseStrategy.
     */
    public IterativeTCaseStrategy() {
    }

    /**
     * Creates instances of IterativeTCaseStrategy.
     * @param fMSize
     * @param intFiniteModel
     * @param natFiniteModel
     */
    public IterativeTCaseStrategy(int fMSize, int maxEval,
            Map<RefExpr, Expr> axDefsValues,
            Map<String, List<String>> basicAxDefs,
            IntFiniteModel intFiniteModel, NatFiniteModel natFiniteModel) {
        this.fMSize = fMSize;
        this.maxEval = maxEval;
        this.axDefsValues = axDefsValues;
        this.basicAxDefs = basicAxDefs;
        this.intFiniteModel = intFiniteModel;
        this.natFiniteModel = natFiniteModel;
        this.exprMap = null;
    }

    /**
     * Tries to generate an abstract test case for the specified test class and returns it if
     * the creation was possible, null if not.
     * @param spec
     * @param tClass
     * @return the generated abstract test case 
     */
    public AbstractTCase generateAbstractTCase(Spec spec, TClass tClass) {

        String tClassName = tClass.getSchName();
        AxPara tClassAxPara = (AxPara) tClass.getMyAxPara().accept(new CZTCloner());


        if (axDefsValues != null) {

            Pred pred = SpecUtils.getAxParaPred(tClassAxPara);
            Set<Map.Entry<RefExpr, Expr>> set = axDefsValues.entrySet();


            Iterator<Map.Entry<RefExpr, Expr>> iterator = set.iterator();
            CZTReplacer replaceVisitor = new CZTReplacer();

            //int l=0;
            while (iterator.hasNext()) {
                Map.Entry<RefExpr, Expr> mapEntry = iterator.next();
                RefExpr refExpr = mapEntry.getKey();
                Expr expr = mapEntry.getValue();
                replaceVisitor.setOrigTerm(refExpr);
                replaceVisitor.setNewTerm(expr);
                /*     System.out.println("******" + l);
                System.out.println("Variable " + refExpr.getName().toString());
                System.out.println("Valor " + SpecUtils.termToLatex(expr) + "\n");

                l++;*/
                pred = (Pred) pred.accept(replaceVisitor);
            }
            
            SpecUtils.setAxParaPred(tClassAxPara, pred);
        }
        

        tClass = new TClassImpl(tClassAxPara, tClassName);
        //System.out.println("Se generara un caso para:\n"+SpecUtils.termToLatex(tClassAxPara));
        ArrayList<BigInteger> intArrayList = tClassAxPara.accept(new NumericConstsExtractor());

        /*
        System.out.println("\nVariables con valores dados en la spec");
        Set<Map.Entry<RefExpr, Expr>> set2 = axDefsValues.entrySet();
        Iterator<Map.Entry<RefExpr, Expr>> it2 = set2.iterator();
        while(it2.hasNext()){
        Map.Entry<RefExpr, Expr> entry = it2.next();
        RefExpr refExpr = entry.getKey();
        System.out.println(refExpr.getName());
        }

        System.out.println("\nVariables de tipo básico:");
        Set<Map.Entry<RefExpr, List<RefExpr>>> set3 = basicAxDefs.entrySet();
        Iterator<Map.Entry<RefExpr, List<RefExpr>>> it3 = set3.iterator();
        while(it3.hasNext()){
        Map.Entry<RefExpr, List<RefExpr>> entry = it3.next();
        List<RefExpr> list = entry.getValue();
        for(int i = 0; i<list.size(); i++){
        System.out.println(list.get(i).getName());
        }
        }
         */

        if (intArrayList.size() > 0) {

            ArrayList<BigInteger> natArrayList = new ArrayList<BigInteger>();

            for (int i = 0; i < intArrayList.size(); i++) {
                natArrayList.add(intArrayList.get(i));
            }

            intFiniteModel.initialize(intArrayList);
            natFiniteModel.initialize(natArrayList);

        } else {
            int size = nzSize;
            if (size == -1) {
                size = fMSize;
            }
            intFiniteModel = new ZeroIntFiniteModel();
            intFiniteModel.initialize(size);
            natFiniteModel = new ZeroNatFiniteModel();
            natFiniteModel.initialize(size);
        }


        int size = btSize;
        if (btSize == -1) {
            size = fMSize;
        }

        FiniteModelCreator finiteModelCreator = new FiniteModelCreator(
                exprMap, fMSize, size, intFiniteModel, natFiniteModel, basicAxDefs);
        spec.accept(finiteModelCreator);

        TClassFiniteModel tClassFiniteModel = new TClassFiniteModel(tClass, finiteModelCreator);


        Map<RefExpr, Expr> varExprMap = null;
        boolean satisfied = false;
        SchemeEvaluator schemeEvaluator = new SchemeEvaluator();
        int tClassFiniteModelSize = tClassFiniteModel.getFMSize();
        int limit = maxEval;
        if (tClassFiniteModelSize < maxEval) {
            limit = tClassFiniteModelSize;
        }

        System.out.println(tClass.getSchName() + " finite model size: "
                + tClassFiniteModelSize + " - Evaluations to perform: at most " + limit);
        int i = 1;
        while (tClassFiniteModel.hasNext() && !satisfied && i <= maxEval) {
            varExprMap = tClassFiniteModel.next();
            EvaluationResp evaluationResp = schemeEvaluator.evalSchemeSat(tClass, varExprMap);
            System.out.println(evaluationResp.getLog());
            satisfied = evaluationResp.getResult();
            i++;
        }

        AbstractTCase abstractTCase = null;
        if (satisfied) {
            // We return an abstract test case with an equality for each
            // assignation of values to variables.
            abstractTCase = new AbstractTCaseImpl(tClass.getMyAxPara(), tClass.getSchName(), varExprMap);
        } else if (!satisfied && i > maxEval) {
            // If the generation of an abstract test case was interrupted
            // because the limit of evaluations had been reached then
            // we return an abstract test case with a null AxPara.
            abstractTCase = new AbstractTCaseImpl(null, "");
        }

        return abstractTCase;

    }

    /**
     * Sets the size of the finite models.
     * @param fMSize
     */
    public void setFiniteModelSize(int fMSize) {
        this.fMSize = fMSize;
    }

    /**
     * Gets the size of the finite models.
     * @return
     */
    public int getFiniteModelSize() {
        return fMSize;
    }

    /**
     * Sets the size of the finite models.
     * @param fMSize
     */
    public void setGivenTypeFiniteModelSize(int btSize) {
        this.btSize = btSize;
    }

    /**
     * Gets the size of the finite models.
     * @return
     */
    public int getGivenTypeFiniteModelSize() {
        return btSize;
    }

    /**
     * Sets the size of the finite models.
     * @param fMSize
     */
    public void setIntNatFiniteModelSize(int nzSize) {
        this.nzSize = nzSize;
    }

    /**
     * Gets the size of the finite models.
     * @return
     */
    public int getIntNatFiniteModelSize() {
        return nzSize;
    }

    /**
     * Sets the IntFiniteModel associated to this strategy.
     * @param intFiniteModel
     */
    public void setIntFiniteModel(IntFiniteModel intFiniteModel) {
        this.intFiniteModel = intFiniteModel;
    }

    /**
     * Gets the IntFiniteModel associated to this strategy.
     * @return
     */
    public IntFiniteModel getIntFiniteModel() {
        return intFiniteModel;
    }

    /**
     * Sets the IntFiniteModel associated to this strategy.
     * @param natFiniteModel
     */
    public void setNatFiniteModel(NatFiniteModel natFiniteModel) {
        this.natFiniteModel = natFiniteModel;
    }

    /**
     * Gets the IntFiniteModel associated to this strategy.
     * @return
     */
    public NatFiniteModel getNatFiniteModel() {
        return natFiniteModel;
    }

    public void setExprMap(Map<Expr, FiniteModel> exprMap) {
        this.exprMap = exprMap;
        /*if(exprMap == null)
        System.out.println("En setExprMap ES NULL");
        else
        System.out.println("En setExprMap NO ES NULL");
         */
    }

    public void setMaxEval(int maxEval) {
        this.maxEval = maxEval;
    }

    public int getMaxEval() {
        return maxEval;
    }
}
