package common.z.czt.visitors;

import client.blogic.management.Controller;
import common.fastest.FastestUtils;
import common.z.SpecUtils;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.CompExpr;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.DecorExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.InclDecl;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.ast.PreExpr;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SchExpr;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.AndExprVisitor;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.z.visitor.CompExprVisitor;
import net.sourceforge.czt.z.visitor.DecorExprVisitor;
import net.sourceforge.czt.z.visitor.OrExprVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.SchExprVisitor;

/**
 * This class was made to calculate the predicate of the Valid Input Space of an
 * operation. It is calculated in the form of a list of predicates in DNF; where
 * that list represents the conjunction of the predicates that compose the list. 
 * For example, the list [p1, p2, ..., pN] represents the predicate 
 * p1/\ p2 /\ ... /\ pN.
 * 
 * The list of predicates are calculated recursively, based on the Visitor design
 * pattern, unfolding only those  schemas that are not selected as operations 
 * nor as predicates.
 * 
 * When calculating the list of predicates in DNF  for an schema, the list is 
 * composed of:
 *  - each element of the list of predicates in DNF associated to each included
 *    schema that is not selected as operation nor as predicate
 *  - the explicit predicate of the schema, in DNF (removing atomic predicates
 *    with output variables and replacing each schema reference for 
 *    the predicate of the referred schema, if the referred schema is not 
 *    selected as operation nor as predicate)
 * 
 * When calculating the list of predicates in DNF associated to a disjunction
 * of schemas, the list of predicates in DNF for each disjunct is calculated
 * and then the cartesian product of the list conforms the list of predicates
 * in DNF associated to the disjunction of schemas * 
 * 
 * @author Pablo Rodriguez Monetti
 */
public class DNFPredExtractor implements SchExprVisitor<List<Pred>>,
        AndExprVisitor<List<Pred>>,
        OrExprVisitor<List<Pred>>,
        RefExprVisitor<List<Pred>>,
        AxParaVisitor<List<Pred>>,
        DecorExprVisitor<List<Pred>>,
        CompExprVisitor<List<Pred>> {

    // A reference to the list of paragraphs of the loaded specification
    private ZParaList zParaList;
    // A reference to the current instance of Controller, which is used to
    // determine if a given schema reference correspond or not to a schema 
    // selected as operation or as predicate. This instance of Controller is
    // also used to keep the previously calculated lists of predicates in DNF,
    // with each list associated to some schema of the specification; this 
    // information is important to avoid recalculating a list twice
    private Controller controller;

    /**
     * Creates the instance of DNFPredExtractor.
     * @param zParaList a reference to the list of paragraphs of the loaded 
     * specification.
     * @param controller a eference to the current instance of Controller.
     */
    public DNFPredExtractor(ZParaList zParaList, Controller controller) {
        this.zParaList = zParaList;
        this.controller = controller;
    }

    /**
     * Visits the specified instance of AxPara returning the list of predicates
     * in DNF associated to the instance.
     * @param axPara the instance of AxPara for which the list of predicates in
     * DNF will be calculated
     * @return the list of predicates in DNF associated to the specified AxPara.
     */
    public List<Pred> visitAxPara(AxPara axPara) {
        // The list of predicates in DNF is calculated recursively, considering 
        // the expression associated to the specified AxPara. The expression
        // could be an SchExpr (if the schema is in the (vertical or horizontal) 
        // form [D|P]), an AndExpr, an OrExpr, a reference to another schema,
        // etc.
        Expr axParaExpr = null;
        List<Pred> predList = null;
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();
        if (declList instanceof ZDeclList) {
            for (int j = 0; j < ((ZDeclList) declList).size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    axParaExpr = constDecl.getExpr();
                    predList = axParaExpr.accept(this);
                }
            }
        }
        return predList;
    }

    /**
     * Visits the specified instance of SchExpr returning the list of predicates
     * in DNF associated to the instance.
     * @param schExpr the instance of SchExpr for which the list of predicates in
     * DNF will be calculated
     * @return the list of predicates in DNF associated to the specified SchExpr.
     */
    public List<Pred> visitSchExpr(SchExpr schExpr) {
        List<Pred> dnfPredList = new ArrayList<Pred>();
        // When calculating the list of predicates for an schema, the list is 
        // composed of:
        //     - each predicate of the list of predicates in DNF associated 
        //       to each included schema that is not selected as operation nor 
        //       as predicate        
        //     - the explicit predicate of the schema, in DNF (removing atomic 
        //       predicates with output variables and replacing schema 
        //       references for its predicates, if the reference refers to an 
        //       schema not selected as operation nor as predicate)

        ZSchText zSchText = schExpr.getZSchText();
        DeclList declList = zSchText.getDeclList();
        ZFactory zFactory = new ZFactoryImpl();
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int i = 0; i < zDeclList.size(); i++) {
                Decl decl = zDeclList.get(i);
                if (decl instanceof InclDecl) {
                    InclDecl inclDecl = (InclDecl) decl;
                    Expr inclExpr = inclDecl.getExpr();
                    String inclName = "";
                    boolean primedInclusion = false;
                    if (inclExpr instanceof RefExpr) {
                        inclName = ((RefExpr) inclExpr).getName().toString();
                        int firstCharCode = (int) inclName.charAt(0);

                        if (firstCharCode == 916 || firstCharCode == 926) {
                            // The schema is a Delta or a Xi
                            inclName = inclName.substring(1);
                        }
                    } else if (inclExpr instanceof DecorExpr) {
                        inclName = ((RefExpr) ((DecorExpr) inclExpr).getExpr()).getName().toString();
                        primedInclusion = true;
                    }


                    boolean isSelOp = controller.isSelectedOperation(inclName);
                    boolean isSelPred = controller.isSelectedPredicate(inclName);
                    boolean isOp = FastestUtils.isLoadedOperation(controller, inclName);

                    if (isSelOp || (isSelPred && isOp)) {
                        // The included AxPara is a schema selected
                        // as operation to be tested or is a operation
                        // schema selected as a predicate.
                        // The extracted predicate is simply the
                        // precondition of the reference to the schema
                        // The schema is not unfolded
                        ZName inclZName = zFactory.createZName(inclName,
                                zFactory.createZStrokeList(), inclName);
                        RefExpr inclRefExpr = zFactory.createRefExpr(inclZName,
                                zFactory.createZExprList(), false, false);
                        PreExpr preExpr = zFactory.createPreExpr(inclRefExpr);
                        ExprPred exprPred = zFactory.createExprPred(preExpr);
                        dnfPredList.add(exprPred);
                    } else if (isSelPred && !isOp) {
                        // The included AxPara is a schema
                        // selected as a predicate but it is not
                        // a operation schema
                        // There is not extracted predicate
                    } else {
                        // The included axPara is a schema which
                        // is not a selected operation nor a selected predicate,
                        // case in which it is needed to unfold
                        // the schema, unless it is primed
                        if (!primedInclusion) {
                            // The list of DNF predicated for the included schema 
                            // could be calculated previously
                            // In such a case, it will not be calculated again
                            List<Pred> predList = controller.getSchemaDNFPredList(inclName);
                            if (predList == null) {
                                //System.out.println("A calcular la inclusion " + inclName);                                
                                AxPara inclAxPara = SpecUtils.axParaSearch(inclName, zParaList);
                                predList = inclAxPara.accept(this);
                            } 
                            // We add the resulting Pred to the repository for 
                            //then combine it with the other Preds.
                            if (predList != null) {
                                dnfPredList.addAll(predList);
                                controller.putInSchemaDNFPredList(inclName, predList);
                            }
                        }
                    }


                }
            }

            Pred schExprPred = schExpr.getZSchText().getPred();

            // The schemas used as predicates in the the explicit predicate of 
            // the schema are replaced by predicates if those schemas are not 
            // selected as operations  nor as predicates; then the DNF of the 
            // resulting predicate is calculated; finally the output variables 
            // are removed, and the predicate is added to the list of predicates 
            // in DNF associated to the specified schExpr
            if (schExprPred != null) {
                // The predicate of the original predicate is traversed in order to find
                // and expand, if needed, the schemas used as predicates and not
                // selected as operation nor as a predicate
                schExprPred.accept(new PredSchemasUnfolder(zParaList, controller));
                Pred dnfPred = SpecUtils.getDNF(schExprPred);
                if (dnfPred != null) {
                    // The atomic predicates that have output variables
                    // are removed from the predicate
                    dnfPred = dnfPred.accept(new OutputAtomicPredRemover());
                    if (dnfPred != null) {
                        dnfPredList.add(dnfPred);
                    }
                }
            }
        }
        return dnfPredList;
    }

    /**
     * Visits the specified instance of AndExpr returning the list of predicates
     * in DNF associated to the instance.
     * @param andExpr the instance of AndExpr for which the list of predicates in
     * DNF will be calculated
     * @return the list of predicates in DNF associated to the specified AndExpr.
     */
    public List<Pred> visitAndExpr(AndExpr andExpr) {
        Expr leftExpr = andExpr.getLeftExpr();
        Expr rightExpr = andExpr.getRightExpr();
        List<Pred> dnfPredList = new ArrayList<Pred>();
        dnfPredList.addAll(leftExpr.accept(this));
        dnfPredList.addAll(rightExpr.accept(this));

        return dnfPredList;
    }

    /**
     * Visits the specified instance of OrExpr returning the list of predicates
     * in DNF associated to the instance.
     * @param orExpr the instance of OrExpr for which the list of predicates in
     * DNF will be calculated
     * @return the list of predicates in DNF associated to the specified OrExpr.
     */
    public List<Pred> visitOrExpr(OrExpr orExpr) {
        
        List<List<Pred>> listOfDnfPredLists = new ArrayList<List<Pred>>();
        List<Expr> exprList = orExpr.accept(new OrExprExtractor());
        // For each disjunct of the disjunction, the list of predicates in DNF
        // is calculated. Once calculated all lists, which are kept in 
        // the list of lists listOfDnfPredLists, the lists that are contained in
        // some other list are removed
        for (int i = 0; i < exprList.size(); i++) {
            List<Pred> dnfPredList = (exprList.get(i)).accept(this);

            if (!dnfPredList.isEmpty()) {
                boolean containedInPreviousList = false;

                for (int j = 0; j < listOfDnfPredLists.size()
                        && !containedInPreviousList; j++) {
                    List<Pred> jDnfPredList = listOfDnfPredLists.get(j);
                    if (SpecUtils.isListContainedInList(dnfPredList, jDnfPredList)) {
                        containedInPreviousList = true;
                    } else if (SpecUtils.isListContainedInList(jDnfPredList, dnfPredList)) {
                        listOfDnfPredLists.remove(j);
                        j--;
                    }
                }
                if (!containedInPreviousList) {
                    listOfDnfPredLists.add(dnfPredList);

                }
            }
        }


        if (listOfDnfPredLists.isEmpty()) {
            // If the list of lists is empty, then a empty list of predicates in 
            // DNF is returned            
            return new ArrayList<Pred>();
        } else {
            // Else, the cartesian product of the lists is returned
            List<Pred> currentDnfPredList = listOfDnfPredLists.get(0);
            // The cartesian product is calculated combining the list pair by
            // pair
            for (int i = 1; i < listOfDnfPredLists.size(); i++) {
                List<Pred> indexDnfPredList = listOfDnfPredLists.get(i);

                // The preds from currentDnfPredList and indexDnfPredList are
                // combined in disjunction
                List<Pred> newDnfPredList = new ArrayList<Pred>();
                for (int j = 0; j < currentDnfPredList.size(); j++) {
                    Pred dnfPred1 = currentDnfPredList.get(j);
                    for (int k = 0; k < indexDnfPredList.size(); k++) {
                        Pred dnfPred2 = indexDnfPredList.get(k);
                        Pred pred = SpecUtils.createSimplifiedOrPred(dnfPred1, dnfPred2);
                        newDnfPredList.add(pred);
                    }
                }
                currentDnfPredList = newDnfPredList;
            }
            return currentDnfPredList;
        }

    }

    /**
     * Visits the specified instance of RefExpr returning the list of predicates
     * in DNF associated to the instance.
     * @param refExpr the instance of RefExpr for which the list of predicates in
     * DNF will be calculated
     * @return the list of predicates in DNF associated to the specified RefExpr.
     */
    public List<Pred> visitRefExpr(RefExpr refExpr) {

        String refExprName = refExpr.getZName().getWord().toString();

        int firstCharCode = (int) refExprName.charAt(0);
        if (firstCharCode == 916 || firstCharCode == 926) {
            // The schema is a Delta or a Xi
            refExprName = refExprName.substring(1);
        }

        List<Pred> predList = null;

        boolean isSelOp = controller.isSelectedOperation(refExprName);
        boolean isSelPred = controller.isSelectedPredicate(refExprName);
        boolean isOp = FastestUtils.isLoadedOperation(controller, refExprName);

        ZFactory zFactory = new ZFactoryImpl();
        if (isSelOp || (isSelPred && isOp)) {
            // The referred AxPara is a schema selected
            // as operation to be tested or is a operation
            // schema selected as a predicate.
            // The extracted predicate is simply the
            // precondition of the reference to the schema
            // The schema is not unfolded
            PreExpr preExpr = zFactory.createPreExpr(refExpr);
            ExprPred exprPred = zFactory.createExprPred(preExpr);
            predList = new ArrayList<Pred>();
            predList.add(exprPred);
            controller.putInSchemaDNFPredList(refExprName, predList);
        } else if (isSelPred && !isOp) {
            // The referred AxPara is a schema
            // selected as a predicate but it is not
            // a operation schema
            // There is not extracted predicate
        } else { /* if(!isSelOp && !isSelPred)   */
            // The referred axPara is a schema which
            // is not a selected operation nor a selected predicate,
            // case in which it is needed to unfold
            // the schema
            // The list of DNF predicated for the included schema 
            // could be calculated previously
            // In such a case, it will not be calculated again            
            predList = controller.getSchemaDNFPredList(refExprName);
            if (predList == null) {
                AxPara refAxPara = SpecUtils.axParaSearch(refExprName, zParaList);
                predList = refAxPara.accept(this);
            }
            // The resulting list of predicates is saved for eventual future
            // use
            controller.putInSchemaDNFPredList(refExprName, predList);
        }

        return predList;
    }

    /**
     * Visits the specified instance of DecorExpr returning the list of predicates
     * in DNF associated to the instance.
     * @param decorExpr the instance of DecorExpr for which the list of predicates in
     * DNF will be calculated
     * @return the list of predicates in DNF associated to the specified DecorExpr.
     */
    public List<Pred> visitDecorExpr(DecorExpr decorExpr) {
        // An empty list is returned because a DecorExpr represents a schema 
        // that is primed
        return new ArrayList<Pred>();
    }

    /**
     * Visits the specified instance of CompExpr returning the list of predicates
     * in DNF associated to the instance.
     * @param compExpr the instance of CompExpr for which the list of predicates in
     * DNF will be calculated
     * @return the list of predicates in DNF associated to the specified CompExpr.
     */
    public List<Pred> visitCompExpr(CompExpr compExpr) {
        // The VIS of the composition of two schemas is equal to the VIS of
        // the conjunction of the schemas so the predicates of both VIS (here
        // kept as lists (conjunctions) of predicates in DNF) are equal
        Expr leftExpr = compExpr.getLeftExpr();
        Expr rightExpr = compExpr.getRightExpr();
        AndExpr andExpr = (new ZFactoryImpl()).createAndExpr();
        andExpr.setLeftExpr(leftExpr);
        andExpr.setRightExpr(rightExpr);
        return andExpr.accept(this);
    }
}
