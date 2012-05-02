package compserver.tcasegen.strategies;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
import common.z.czt.visitors.AndPredClausesExtractor;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
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
import common.fastest.FastestUtils;
import net.sourceforge.czt.z.ast.DeclList;
import client.blogic.testing.ttree.*;
import client.blogic.testing.ttree.visitors.TTreeNodeFinder;

/**
 * An instance of this class represents an strategy of test case generation. In particular,
 * in this strategy we obtain from the predicate of the test class that is passed for generate a
 * test case all the atomic predicates. Then we iterate over this atomic predicates and try to reuse
 * all the information collected in other iterations. For example, if an atomic predicate is originally
 * from the father of a test class and we calculate the values in the finite model that satisfies this
 * atomic predicate, we use this value as finite model.
 */
public class AtomicPredTCaseStrategy implements TCaseStrategy {

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
    // A reference to the testing trees
    private Map<String, TClassNode> mapTTree;
    // An indicator to know if the strategy was created automatically or by the user using
    // setfinitemodel command
    boolean setByUser = false;

    /**
     * Creates instances of AtomicPredTCaseStrategy.
     */
    public AtomicPredTCaseStrategy() {
    }

    /**
     * Creates instances of AtomicPredTCaseStrategy.
     * @param fMSize
     * @param intFiniteModel
     * @param natFiniteModel
     */
    public AtomicPredTCaseStrategy(int fMSize, int maxEval,
            Map<RefExpr, Expr> axDefsValues,
            Map<String, List<String>> basicAxDefs,
            IntFiniteModel intFiniteModel, NatFiniteModel natFiniteModel, Map<String, TClassNode> mapTTree) {
        this.fMSize = fMSize;
        this.maxEval = maxEval;
        this.axDefsValues = axDefsValues;
        this.basicAxDefs = basicAxDefs;
        this.intFiniteModel = intFiniteModel;
        this.natFiniteModel = natFiniteModel;
        this.exprMap = null;
        this.mapTTree = mapTTree;
    }

    /**
     * Tries to generate an abstract test case for the specified test class and returns it if
     * the creation was possible, null if not.
     * @param spec
     * @param tClass
     * @return the generated abstract test case
     */
        public AbstractTCase generateAbstractTCase(Spec spec, TClass tClass) {

        // We obtain the instance of the module that contains the precalculated finite models for atomic
        // predicates
        PreCalculatedFiniteModels preCalculatedFiniteModels = PreCalculatedFiniteModels.getInstance();

        String tClassName = tClass.getSchName();
        // We obtain the name of the operation related to this test class
        String opName = FastestUtils.getOperationName(tClassName);

        // We obtain the testing tree related to this operation
        TClassNode tClassNode = mapTTree.get(opName);

        // Now we obtain the node in the testing tree coresponding to this test class and the node
        // corresponding to the father of this test class
        TTreeNode tTreeNode = tClassNode.acceptVisitor(new TTreeNodeFinder(tClassName));
        TClassNode nodeDad = tTreeNode.getDadNode();
        Pred dadPred = null;
        TClass tClassDad = null;
        String dadName = "";
        if (nodeDad != null) {
            tClassDad = nodeDad.getUnfoldedValue();
            dadName = tClassDad.getSchName();
            dadPred = SpecUtils.getAxParaPred(tClassDad.getMyAxPara());
        }

        AxPara tClassAxPara = (AxPara) tClass.getMyAxPara().accept(new CZTCloner());
        DeclList declList = SpecUtils.getAxParaListOfDecl(tClassAxPara);
        // We use a map structure for store the assignments for each variable that satisfy the atomic predicates. This map will filtering in each iteration
        Map<RefExpr, List<Expr>> matchMap = new HashMap<RefExpr, List<Expr>>();
        // We will be storing the finite models associated to a set of variables.
        // This map will filtering in each iteration
        Map<VariablesSet, List<Map<RefExpr, Expr>>> signFMMap = new HashMap<VariablesSet, List<Map<RefExpr, Expr>>>();

        // We replace in predicate the values for axiomatic definitions
        if (axDefsValues != null) {

            Pred pred = SpecUtils.getAxParaPred(tClassAxPara);
            Set<Map.Entry<RefExpr, Expr>> set = axDefsValues.entrySet();
            Iterator<Map.Entry<RefExpr, Expr>> iterator = set.iterator();
            CZTReplacer replaceVisitor = new CZTReplacer();

            while (iterator.hasNext()) {
                Map.Entry<RefExpr, Expr> mapEntry = iterator.next();
                RefExpr refExpr = mapEntry.getKey();
                Expr expr = mapEntry.getValue();
                replaceVisitor.setOrigTerm(refExpr);
                replaceVisitor.setNewTerm(expr);
                pred = (Pred) pred.accept(replaceVisitor);
            }

            SpecUtils.setAxParaPred(tClassAxPara, pred);
        }

        tClass = new TClassImpl(tClassAxPara, tClassName);


        // We obtain finite models for numeric types
        ArrayList<BigInteger> intArrayList = tClassAxPara.accept(new NumericConstsExtractor());
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

        System.out.println("Trying to generate a test case for the class: " + tClassName);

        // We obtain an instance of the FiniteModelCreator
        FiniteModelCreator finiteModelCreator = new FiniteModelCreator(
                exprMap, fMSize, size, intFiniteModel, natFiniteModel, basicAxDefs);
        spec.accept(finiteModelCreator);


        Pred pred = SpecUtils.getAxParaPred(tClassAxPara);
        // We consider the case of a test class without predicates
        if (pred == null) {
            TClassFiniteModel auxFiniteModel = new TClassFiniteModel(tClass, finiteModelCreator);
            Map<RefExpr, Expr> assign = auxFiniteModel.next();
            AbstractTCase abstractTCase = new AbstractTCaseImpl(tClass.getMyAxPara(), tClass.getSchName(), assign);
            return abstractTCase;
        }

        // We obtain the number of atomic predicates in the predicate
        int nroAtPreds = getNumberOfAtomicPreds(pred);
        int indexPred = 1;
        boolean trunkEvaluations = false;

        // Now we obtain the atomic predicates from the predicate of the test class
        AbstractRepository<Pred> atomicPreds = pred.accept(new AndPredClausesExtractor());

        // We rearrange the predicate taking in account the size of the finite
        // models of the atomic predicates
        List<Pred> reArrangedPred = reorderPredicate(atomicPreds, tClass, finiteModelCreator, dadPred);
        // We separate the atomic predicates in two groups: the atomic predicates that are inherit from
        // the father in the testing tree and the atomic predicates which are of this class
        List<Pred> fatherListPreds = new ArrayList<Pred>();
        List<Pred> classListPreds = new ArrayList<Pred>();
        boolean isFromDad = true;
        int index;
        for (index = 0; index < reArrangedPred.size() && isFromDad; index++) {
            if (isFromFatherPred(dadPred, reArrangedPred.get(index))) {
                fatherListPreds.add(reArrangedPred.get(index));
            } else {
                isFromDad = false;
            }
        }
        index -= 1;
        // In index is kept the index of the last predicate from reArrangedPred
        // that is from the father; the following ones are all proper of the class
        for (int i = index; i < reArrangedPred.size(); i++) {
            classListPreds.add(reArrangedPred.get(i));
        }

        boolean found = false;
        boolean result = true;
        //for(int sz=1;sz<=fMSize && !found;sz++){
        SchemeEvaluator schemeEvaluator = new SchemeEvaluator();

        for (int k = 0; k < reArrangedPred.size() && result; k++) {
            Map<RefExpr, List<Expr>> auxMatchMap = new HashMap<RefExpr, List<Expr>>();
            List<Map<RefExpr, Expr>> assignments = new ArrayList<Map<RefExpr, Expr>>();
            boolean preCalculated = false;
            boolean isFromFatherAndToBeSaved = false;
            boolean isIndependent = false;

            Pred atomicPred = reArrangedPred.get(k);
/*
            System.out.println("*************");
            System.out.println("Predicado: " + SpecUtils.termToLatex(atomicPred));
*/

            // We obtain the variables that appear in the atomic predicate
            List<RefExpr> varsInPred = SpecUtils.getVariablesInPred(atomicPred, declList);
            VariablesSet varsSet = new VariablesSet(varsInPred);
            // We check if the finite model for this atomic predicate was calculated

            boolean isFromFather = isFromFatherPred(dadPred, atomicPred);
            if (isFromFather && !setByUser) {
                isFromFatherAndToBeSaved = true;
//                System.out.println("Del padre: SI");
                PreCalculatedPredicate auxPre = new PreCalculatedPredicate(dadName, atomicPred);
                if (preCalculatedFiniteModels.get(auxPre) != null) {
                    List<Map<RefExpr, Expr>> auxList = preCalculatedFiniteModels.get(auxPre);
                    assignments.addAll(auxList);
                    preCalculated = true;
//                    System.out.println("Precalculado: SI");
                }
            }
            if (!preCalculated) {
                if (!isFromFather) {
                    isIndependent = isIndependentPredicate(atomicPred,
                            classListPreds, declList, k - index);
                }

                // We check if a finite model was calculated for the variables
                // that appears in the predicate and, in that case, we use it.
                // If not, we must calculate it
                if (signFMMap.get(varsSet) != null) {
//                    System.out.println("Modelo para variables: SI");
                    assignments = signFMMap.get(varsSet);
                    // We must filter this list
                    for (int i = 0; i < assignments.size(); i++) {
                        Map<RefExpr, Expr> assign = assignments.get(i);
                        Set<Map.Entry<RefExpr, Expr>> set = assign.entrySet();
                        Iterator<Map.Entry<RefExpr, Expr>> it = set.iterator();
                        boolean illegalEntry = false;
                        while (it.hasNext() && !illegalEntry) {
                            Map.Entry<RefExpr, Expr> mapEntry = it.next();
                            RefExpr key = mapEntry.getKey();
                            Expr value = mapEntry.getValue();

                            if (matchMap.get(key) != null) {
                                List<Expr> exprs = matchMap.get(key);
                                if (!exprs.contains(value)) {
                                    illegalEntry = true;
                                }
                            }
                        }
                        if (illegalEntry) {
                            assignments.remove(i);
                            i--;
                        }
                    }
                } else {
//                    System.out.println("Modelo para variables: NO");
                    // We obtain the finite model for this atomic predicate in three steps
                    List<Map<RefExpr, Expr>> combinations = getMatchesCombinations(matchMap, varsInPred);
                    TClassFiniteModel auxFiniteModel;
                    if (isFromFather) {
                        auxFiniteModel = new TClassFiniteModel(tClass,
                                finiteModelCreator, atomicPred, matchMap, fatherListPreds);
                    } else {
                        auxFiniteModel = new TClassFiniteModel(tClass,
                                finiteModelCreator, atomicPred, matchMap, reArrangedPred);
                    }

                    List<Map<RefExpr, Expr>> auxList = mergePreCalculatedAndFiniteModel(auxFiniteModel, combinations);
                    assignments.addAll(auxList);
                }

            }

            boolean satisfied = false;

            int assignmentsSize = assignments.size();

            // We calculate the number of evaluations

            int limit = maxEval / nroAtPreds;
            if (assignmentsSize < limit) {
                limit = assignmentsSize;
            } else {
                trunkEvaluations = true;
            }

            int matches = 0;
            boolean withVars = true;
/*
            System.out.println("------- Assignements:");
            // Imprimimos assignements, para debug
            for (int i = 0; i < assignments.size(); i++) {
                    Map<RefExpr, Expr> assign = assignments.get(i);
                    Set<Map.Entry<RefExpr, Expr>> set = assign.entrySet();
                    Iterator<Map.Entry<RefExpr, Expr>> it = set.iterator();
                    boolean illegalEntry = false;
                    while (it.hasNext() && !illegalEntry) {
                        Map.Entry<RefExpr, Expr> mapEntry = it.next();
                        RefExpr key = mapEntry.getKey();
                        Expr value = mapEntry.getValue();
                        System.out.println("Variable: " + SpecUtils.termToLatex(key));
                        System.out.println("Valor: " + SpecUtils.termToLatex(value));
                    }
            }

*/

            // If the finite model for this atomic predicate not was previously evaluated we must
            // send to it to the evaluator
            List<Map<RefExpr, Expr>> newFiniteModel = new ArrayList<Map<RefExpr, Expr>>();
            if (!preCalculated) {
                for (int i = 0; i < assignments.size() && i < limit; i++) {
                    Map<RefExpr, Expr> varExprMap = assignments.get(i);
                    EvaluationResp evaluationResp = schemeEvaluator.evalAtomicPredSat(atomicPred, varExprMap);

                    satisfied = evaluationResp.getResult();
                    // If the atomic predicate is the last in the predicate or is indepent of other
                    // atomic predicates we stop the loop with the first match
                    if (satisfied) {
                        if (indexPred == nroAtPreds || isIndependent) {
                            i = limit;
                        }
                        // We add this entry for the new finite model
                        newFiniteModel.add(varExprMap);
                        matches++;
                        // We actualize auxMatchMap
                        Set<Map.Entry<RefExpr, Expr>> set = varExprMap.entrySet();
                        // We considerate the case in which the atomic predicate has not variables
                        if (set.size() == 0) {
                            withVars = false;
                        }
                        Iterator<Map.Entry<RefExpr, Expr>> it = set.iterator();
                        while (it.hasNext()) {
                            Map.Entry<RefExpr, Expr> mapEntry = it.next();
                            RefExpr refExpr = mapEntry.getKey();
                            Expr expr = mapEntry.getValue();
                            List<Expr> auxList = auxMatchMap.get(refExpr);
                            if (auxList != null) {
                                if (!auxList.contains(expr)) {
                                    auxList.add(expr);
                                }
                            } else {
                                auxList = new ArrayList<Expr>();
                                auxList.add(expr);
                            }
                            auxMatchMap.put(refExpr, auxList);
                        }
                    }
                }
            }
            // We actualize the repository that contains the predicates previously evaluated
            if (!preCalculated && isFromFatherAndToBeSaved) {
                PreCalculatedPredicate auxPre = new PreCalculatedPredicate(dadName, atomicPred);
                List<Map<RefExpr, Expr>> auxList = new ArrayList<Map<RefExpr, Expr>>();
                auxList.addAll(newFiniteModel);
  //              System.out.println("guardamos el predicado");
                preCalculatedFiniteModels.put(auxPre, auxList);
            }
            if (preCalculated) {
                // Es imposible que sea el ultimo
                // We actualize the finite model associated to this set of variables
                signFMMap.put(varsSet, assignments);
                // We actualize the map with the assignments that satisfy this atomic predicate

                // **** Agregado por Pablo Rodriguez para eliminar entradas,
                // en predicados precalculados, que no deban ser tenidas en cuenta
                // por ser ilegales
                for (int i = 0; i < assignments.size(); i++) {
                    Map<RefExpr, Expr> assign = assignments.get(i);
                    Set<Map.Entry<RefExpr, Expr>> set = assign.entrySet();
                    Iterator<Map.Entry<RefExpr, Expr>> it = set.iterator();
                    boolean illegalEntry = false;
                    while (it.hasNext() && !illegalEntry) {
                        Map.Entry<RefExpr, Expr> mapEntry = it.next();
                        RefExpr key = mapEntry.getKey();
                        Expr value = mapEntry.getValue();

                        if (matchMap.get(key) != null) {
                            List<Expr> exprs = matchMap.get(key);
                            if (!exprs.contains(value)) {
                                illegalEntry = true;
                            }
                        }
                    }
                    if (illegalEntry) {
                        assignments.remove(i);
                        i--;
                    }
                }
                // ****




                for (int i = 0; i < assignments.size(); i++) {
                    Map<RefExpr, Expr> varExprMap = assignments.get(i);
                    Set<Map.Entry<RefExpr, Expr>> set = varExprMap.entrySet();
                    Iterator<Map.Entry<RefExpr, Expr>> it = set.iterator();
                    while (it.hasNext()) {
                        Map.Entry<RefExpr, Expr> mapEntry = it.next();
                        RefExpr refExpr = mapEntry.getKey();
                        Expr expr = mapEntry.getValue();
                        List<Expr> auxList = auxMatchMap.get(refExpr);
                        if (auxList != null) {
                            if (!auxList.contains(expr)) {
                                auxList.add(expr);
                            }
                        } else {
                            auxList = new ArrayList<Expr>();
                            auxList.add(expr);
                        }
                        auxMatchMap.put(refExpr, auxList);
                    }
                }

                matchMap.putAll(auxMatchMap);
            } else {
                signFMMap.put(varsSet, newFiniteModel);
                if (auxMatchMap.size() == 0 && withVars) {
                    result = false;
                } else {
                    matchMap.putAll(auxMatchMap);
                }
            }
            indexPred++;
            // Imprimo para debug matchMap
/*
            System.out.println("Mapa!");
            Set<Map.Entry<RefExpr, List<Expr>>> set = matchMap.entrySet();
            Iterator<Map.Entry<RefExpr, List<Expr>>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<RefExpr, List<Expr>> entry = it.next();
                RefExpr key = entry.getKey();
                List<Expr> values = entry.getValue();
                System.out.println("Var: " + SpecUtils.termToLatex(key));
                System.out.println("Modelo");
                for (int i = 0; i < values.size(); i++) {
                    System.out.println(SpecUtils.termToLatex(values.get(i)));
                }
            }
*/
        }




        if (result) {
            Set<Map.Entry<VariablesSet, List<Map<RefExpr, Expr>>>> setSign = signFMMap.entrySet();
            Iterator<Map.Entry<VariablesSet, List<Map<RefExpr, Expr>>>> itSign = setSign.iterator();
            while (itSign.hasNext() && result) {
                Map.Entry<VariablesSet, List<Map<RefExpr, Expr>>> mapEntrySign = itSign.next();
                VariablesSet variableSet = mapEntrySign.getKey();
                List<Map<RefExpr, Expr>> values = mapEntrySign.getValue();
                for (int i = 0; i < values.size(); i++) {
                    Map<RefExpr, Expr> assign = values.get(i);
                    Set<Map.Entry<RefExpr, Expr>> set = assign.entrySet();
                    Iterator<Map.Entry<RefExpr, Expr>> it = set.iterator();
                    boolean illegalEntry = false;
                    while (it.hasNext() && !illegalEntry) {
                        Map.Entry<RefExpr, Expr> mapEntry = it.next();
                        RefExpr key = mapEntry.getKey();
                        Expr value = mapEntry.getValue();

                        if (matchMap.get(key) != null) {
                            List<Expr> exprs = matchMap.get(key);
                            if (!exprs.contains(value)) {
                                illegalEntry = true;
                            }
                        }
                    }
                    if (illegalEntry) {
                        values.remove(i);
                        i--;
                    }
                }
                if (values.size() == 0) {
                    result = false;
                }

            }
        }



/*   FORMA ORIGINAL DE EXTRACCION DE UN CASO DE PRUEBA
        AbstractTCase abstractTCase = null;
        if (result) {
            // We return an abstract test case taking one element for each entry
            // in matchMap
            Map<RefExpr, Expr> map = new HashMap<RefExpr, Expr>();
            Set<Map.Entry<RefExpr, List<Expr>>> set = matchMap.entrySet();
            Iterator<Map.Entry<RefExpr, List<Expr>>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<RefExpr, List<Expr>> mapEntry = it.next();
                RefExpr key = mapEntry.getKey();
                List<Expr> value = mapEntry.getValue();
                //int valueSize = value.size();
                //int index = (int) Math.random() * valueSize;
                map.put(key, value.get(0));
            }


            TClassFiniteModel tCFM = new TClassFiniteModel(tClass, finiteModelCreator);
            Map<RefExpr, Expr> mapComplete = tCFM.next();
            mapComplete.putAll(map);
            abstractTCase = new AbstractTCaseImpl(tClass.getMyAxPara(), tClass.getSchName(), mapComplete);
            //System.out.println("Encontro para "+tClassName);
        } else if (!result) {
            // If the generation of an abstract test case was interrupted
            // because the limit of evaluations had been reached then
            // we return an abstract test case with a null AxPara.
            if (trunkEvaluations) {
                abstractTCase = new AbstractTCaseImpl(null, "");
            } else {
                return null;
            }
        }

  */

        AbstractTCase abstractTCase = null;
             if (result) {
                // We return an abstract test case taking one element for each entry
                // in matchMap
                Map<RefExpr, Expr> map = new HashMap<RefExpr, Expr>();
                Set<Map.Entry<RefExpr, List<Expr>>> set = matchMap.entrySet();
                Iterator<Map.Entry<RefExpr, List<Expr>>> it = set.iterator();

                List<Integer> listsSizes = new ArrayList();
                int numberOfLists = 0;
                int numberOfCombinations = 1;
                while (it.hasNext()) {
                    Map.Entry<RefExpr, List<Expr>> mapEntry = it.next();
                    RefExpr key = mapEntry.getKey();
                    List<Expr> value = mapEntry.getValue();
                    Integer listSize = new Integer(value.size());
                    listsSizes.add(listSize);
                    numberOfCombinations *= listSize;
                    //int valueSize = value.size();
                    //int index = (int) Math.random() * valueSize;
                    map.put(key, value.get(0));
                    numberOfLists++;
                }

                // The first combination from matchUp is taken and the predicate
                // of the test class is evaluated with those values
                EvaluationResp evaluationResp =
                        schemeEvaluator.evalAtomicPredSat(pred, map);

                boolean satisfied = evaluationResp.getResult();

                int[] indexes = new int[numberOfLists];


                for (int i = 0; i < numberOfLists; i++) {
                    indexes[i] = 0;
                }

                // If the predicated was not satisfied then others combinations
                // from matchUp are considered
                for (int i = 1; i < numberOfCombinations && !satisfied; i++) {
                    boolean hasInc = false;
                    it = set.iterator();
                    int listIndex = 0;
                    while (it.hasNext() && !hasInc) {
                        Map.Entry<RefExpr, List<Expr>> mapEntry = it.next();
                        RefExpr key = mapEntry.getKey();
                        List<Expr> listOfValues = mapEntry.getValue();
                        if (indexes[listIndex] + 1 < listsSizes.get(listIndex)) {
                            hasInc = true;
                            indexes[listIndex]++;
                        } else {
                            indexes[listIndex] = 0;
                        }
                        map.put(key, listOfValues.get(indexes[listIndex]));
                        listIndex++;
                    }
/*
                    System.out.println("************");
                    Set<Map.Entry<RefExpr,Expr>> mapSet = map.entrySet();
                    Iterator<Map.Entry<RefExpr,Expr>> iterator = mapSet.iterator();
                    while(iterator.hasNext()){
                    Map.Entry<RefExpr, Expr> entry = iterator.next();
                    RefExpr var = entry.getKey();
                    Expr valor = entry.getValue();
                    System.out.println("(RefExpr, Expr) = (" + SpecUtils.termToLatex(var) +
                    "," + SpecUtils.termToLatex(valor) + ")" );

                    }
*/
                    evaluationResp = schemeEvaluator.evalAtomicPredSat(pred, map);
                    satisfied = evaluationResp.getResult();

                }


            if(satisfied){
                TClassFiniteModel tCFM = new TClassFiniteModel(tClass, finiteModelCreator);
                Map<RefExpr, Expr> mapComplete = tCFM.next();
                mapComplete.putAll(map);
                abstractTCase = new AbstractTCaseImpl(tClass.getMyAxPara(), tClass.getSchName(), mapComplete);
            }
            else{
                if (trunkEvaluations) {
                    abstractTCase = new AbstractTCaseImpl(null, "");
                } else {
                    return null;
                }
            }

            //System.out.println("Encontro para "+tClassName);

        } else if (!result) {
            // If the generation of an abstract test case was interrupted
            // because the limit of evaluations had been reached then
            // we return an abstract test case with a null AxPara.
            if (trunkEvaluations) {
                abstractTCase = new AbstractTCaseImpl(null, "");
            } else {
                return null;
            }
        }
        return abstractTCase;

    }

    /**
     * This function takes a map that contains the variables that have a finite model calculated and a list
     * with the variables that appears in the predicate. For the variables that appears in the predicate this
     * function looks in the map for their corresponding entry and calculates the cartesian product of their
     * finite models
     */
    private List<Map<RefExpr, Expr>> getMatchesCombinations(Map<RefExpr, List<Expr>> matchMap, List<RefExpr> vars) {
        List<List<Map<RefExpr, Expr>>> mapLists = new ArrayList<List<Map<RefExpr, Expr>>>();
        for (int i = 0; i < vars.size(); i++) {
            Map<RefExpr, Expr> map = new HashMap<RefExpr, Expr>();
            RefExpr var = vars.get(i);
            //System.out.println("La variable es: "+SpecUtils.termToLatex(var));
            List<Expr> values = matchMap.get(var);
            if (values != null) {
                List<Map<RefExpr, Expr>> auxList = new ArrayList<Map<RefExpr, Expr>>();
                for (int j = 0; j < values.size(); j++) {
                    Map<RefExpr, Expr> singleMap = new HashMap<RefExpr, Expr>();
                    Expr value = values.get(j);
                    //System.out.println("Le agrega: "+SpecUtils.termToLatex(value));
                    singleMap.put(var, value);
                    auxList.add(singleMap);
                }
                mapLists.add(auxList);
            }

        }
        return getCombinations(mapLists);
    }

    private List<Map<RefExpr, Expr>> getCombinations(List<List<Map<RefExpr, Expr>>> mapLists) {

        //List<Map<RefExpr,Expr>> combinations = new ArrayList<Map<RefExpr,Expr>>();
        //System.out.println("El tamaño en combinations: "+mapLists.size());
        if (mapLists.size() == 0) {
            return (new ArrayList<Map<RefExpr, Expr>>());
        }
        if (mapLists.size() == 1) {
            //System.out.println("El tamaño de la lista actual: "+mapLists.get(0).size());
            return mapLists.get(0);
        }
        List<Map<RefExpr, Expr>> first = mapLists.get(0);
        mapLists.remove(0);
        List<Map<RefExpr, Expr>> combinationsSub = getCombinations(mapLists);
        List<Map<RefExpr, Expr>> combinations = new ArrayList<Map<RefExpr, Expr>>();

        //System.out.println("El tamaño de la lista actual: "+first.size());
        //System.out.println("El tamaño de la lista recursiva: "+combinationsSub.size());

        for (int i = 0; i < first.size(); i++) {
            Map<RefExpr, Expr> singleMap = first.get(i);
            for (int j = 0; j < combinationsSub.size(); j++) {
                Map<RefExpr, Expr> mapReturn = new HashMap<RefExpr, Expr>();
                Map<RefExpr, Expr> mapSub = combinationsSub.get(j);
                mapReturn.putAll(singleMap);
                mapReturn.putAll(mapSub);
                combinations.add(mapReturn);
            }
        }
        //System.out.println("El tamaño de salida en combinations: "+combinations.size());
        return combinations;
    }

    private boolean isIndependentPredicate(Pred atomicPred,
            List<Pred> classPredicates, DeclList declList, int index) {
        String strAtomicPred = SpecUtils.termToLatex(atomicPred);
        boolean isIndependent = true;
        List<RefExpr> varsInPred = SpecUtils.getVariablesInPred(atomicPred, declList);
        for (int i = index + 1; i < classPredicates.size() && isIndependent; i++) {
            //for(int i=0;i<classPredicates.size() && isIndependent;i++){
            Pred auxPred = classPredicates.get(i);
            String strAuxPred = SpecUtils.termToLatex(auxPred);
            if (!strAuxPred.equals(strAtomicPred)) {
                List<RefExpr> varsInAuxPred = SpecUtils.getVariablesInPred(auxPred, declList);
                for (int j = 0; j < varsInPred.size() && isIndependent; j++) {
                    RefExpr refExpr = varsInPred.get(j);
                    if (varsInAuxPred.contains(refExpr)) {
                        isIndependent = false;
                    }
                }
            }
        }
        return isIndependent;
    }

    private List<Pred> reorderPredicate(AbstractRepository<Pred> originalPred, TClass tClass, FiniteModelCreator finiteModelCreator, Pred dadPred) {
        Map<RefExpr, List<Expr>> auxMap = new HashMap<RefExpr, List<Expr>>();
        List<Pred> fatherPreds = new ArrayList<Pred>();
        List<Pred> classPreds = new ArrayList<Pred>();
        List<Integer> fatherSizes = new ArrayList<Integer>();
        List<Integer> classSizes = new ArrayList<Integer>();
        AbstractIterator<Pred> predClausesIt = originalPred.createIterator();
        while (predClausesIt.hasNext()) {
            Pred atomicPred = predClausesIt.next();
            List<Pred> auxPredList = new ArrayList<Pred>();
            TClassFiniteModel auxFiniteModel = new TClassFiniteModel(tClass, finiteModelCreator, atomicPred, auxMap, auxPredList);
            int finiteModelSize = auxFiniteModel.getFMSize();
            if (isFromFatherPred(dadPred, atomicPred)) {
                boolean inserted = false;
                for (int i = 0; i < fatherSizes.size() && !inserted; i++) {
                    if (finiteModelSize < fatherSizes.get(i).intValue()) {
                        fatherPreds.add(i, atomicPred);
                        fatherSizes.add(i, new Integer(finiteModelSize));
                        inserted = true;
                    }
                }
                if (!inserted) {
                    fatherPreds.add(atomicPred);
                    fatherSizes.add(new Integer(finiteModelSize));
                }
            } else {
                boolean inserted = false;
                for (int i = 0; i < classSizes.size() && !inserted; i++) {
                    if (finiteModelSize < classSizes.get(i).intValue()) {
                        classPreds.add(i, atomicPred);
                        classSizes.add(i, new Integer(finiteModelSize));
                        inserted = true;
                    }
                }
                if (!inserted) {
                    classPreds.add(atomicPred);
                    classSizes.add(new Integer(finiteModelSize));
                }
            }
        }
        List<Pred> finalPred = new ArrayList<Pred>();
        finalPred.addAll(fatherPreds);
        finalPred.addAll(classPreds);
        return finalPred;
    }

    private boolean isFromFatherPred(Pred fatherPred, Pred atomicPred) {
        // We obtain the atomic predicates from the predicate of fatherPred
        if (fatherPred == null) {
            return false;
        }
        AbstractRepository<Pred> atomicPreds = fatherPred.accept(new AndPredClausesExtractor());
        AbstractIterator<Pred> predClausesIt = atomicPreds.createIterator();
        boolean found = false;
        while (predClausesIt.hasNext() && !found) {
            Pred auxPred = predClausesIt.next();
            //System.out.println("Compara:");
            //System.out.println(SpecUtils.termToLatex(auxPred));
            //System.out.println("con:");
            //System.out.println(SpecUtils.termToLatex(atomicPred));
            //if(SpecUtils.areEqualTerms(auxPred,atomicPred))
            //	found = true;
            if ((SpecUtils.termToLatex(auxPred)).equals(SpecUtils.termToLatex(atomicPred))) {
                found = true;
            }
        }
        return found;
    }

    /**
     * This function calculates the cartesian product between the finite model for that variables in the
     * predicate that has not a precalculated finite model and the cartesian product of the finite models
     * of the variables that has a finite model precalculated
     */
    private List<Map<RefExpr, Expr>> mergePreCalculatedAndFiniteModel(TClassFiniteModel finiteModel, List<Map<RefExpr, Expr>> preCalculatedList) {
        List<Map<RefExpr, Expr>> mergedList = new ArrayList<Map<RefExpr, Expr>>();
        boolean fmIsEmpty = true;
        int count = 0;
        while (finiteModel.hasNext() && count < maxEval) {
            fmIsEmpty = false;
            Map<RefExpr, Expr> fmMap = finiteModel.next();
            if (preCalculatedList.size() > 0) {
                for (int i = 0; i < preCalculatedList.size(); i++) {
                    Map<RefExpr, Expr> preCalculatedMap = preCalculatedList.get(i);
                    Map<RefExpr, Expr> returnMap = new HashMap<RefExpr, Expr>();
                    returnMap.putAll(preCalculatedMap);
                    returnMap.putAll(fmMap);
                    mergedList.add(returnMap);
                }
            } else {
                mergedList.add(fmMap);
            }
            count++;
            //System.out.println(++count);
        }
        //System.out.println("Afuera!");
        if (fmIsEmpty) {
            return preCalculatedList;
        } else {
            return mergedList;
        }
    }

    private int getNumberOfAtomicPreds(Pred pred) {
        int count = 0;
        AbstractRepository<Pred> atomicPreds = pred.accept(new AndPredClausesExtractor());
        AbstractIterator<Pred> predClausesIt = atomicPreds.createIterator();
        while (predClausesIt.hasNext()) {
            Pred auxPred = predClausesIt.next();
            count++;
        }
        return count;
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

    public void setTTreeMap(Map<String, TClassNode> mapTTree) {
        this.mapTTree = mapTTree;
    }

    public Map<String, TClassNode> getTTreeMap() {
        return mapTTree;
    }

    public void isSetByUser(boolean value) {
        setByUser = value;
    }
}