package compserver.tcasegen.fm;

import java.util.*;

import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ApplExpr;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.PowerExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.Name;

import common.z.SpecUtils;
import common.z.czt.visitors.ContainsTermVerifier;
import common.z.czt.visitors.AndPredClausesExtractor;
import common.z.TClass;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import common.z.UtilSymbols;

/**
 * An instance of this this class allow the generation of a finite model associated to the 
 * test class for which is wanted a test case. TClassFiniteModel allow one to obtain every 
 * assignment of values to the variables declared in such test class. In order to obtain these
 * value, this class uses a finite model generator for each declared variable type.
 * It is importante to note that TClassFiniteModel does not implements FiniteModel.
 * @author Pablo Rodriguez Monetti
 */
public class TClassFiniteModel {

    private int size;
    private int index;
    private ZExprList zExprList;
    private ZExprList varList;
    private List<FiniteModel> finiteModelList;
    private ZFactory zFactory;

    /**
     * Creates an instance of TClassFiniteModel.
     * @param tClass
     * @param finiteModelCreator
     */
    public TClassFiniteModel(TClass tClass, FiniteModelCreator finiteModelCreator) {
        // Inicializamos el indice
        index = 0;

        // Inicializamos la lista de variables y la lista de FiniteModel
        zFactory = new ZFactoryImpl();
        finiteModelList = new ArrayList<FiniteModel>();
        varList = zFactory.createZExprList();



        Map<RefExpr, SetExpr> varSetExprMap = new HashMap<RefExpr, SetExpr>();
        zExprList = zFactory.createZExprList();


        AxPara axPara = tClass.getMyAxPara();
        DeclList declList = SpecUtils.getAxParaListOfDecl(axPara);
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    Expr type = varDecl.getExpr();
                    // We create a FiniteModel for the declaration type
                    // If there are more than one variable in this declaration, we will
                    // clone the instance of FiniteModel
                    FiniteModel typeFiniteModel =
                            finiteModelCreator.getFiniteModel(type);
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        ZExprList tempZExprList = zFactory.createZExprList();
                        for (int k = 0; k < zNameListImpl.size(); k++) {
                            //System.out.println(zNameListImpl.get(k));
                            RefExpr varRefExpr = zFactory.createRefExpr
                                    (zNameListImpl.get(k), tempZExprList, false, false);
                            FiniteModel varFiniteModel =
                                    finiteModelCreator.getFiniteModel(varRefExpr);
                            if (varFiniteModel == null) {
                                varFiniteModel = typeFiniteModel;
                            }
                            Pred tClassPred = SpecUtils.getAxParaPred(axPara);
                            if (tClassPred != null) {
                                AbstractRepository<Pred> predRep =
                                        tClassPred.accept(new AndPredClausesExtractor());
                                AbstractIterator<Pred> predIt = predRep.createIterator();
                                boolean inEquality = false;
                                // We verify whether the variable is equal to a constant
                                // expression
                                while (predIt.hasNext() && !inEquality) {
                                    Pred pred = predIt.next();
                                    // At first, we verify whether the variable is equal to some
                                    // expression, independently whether it is constant or not.

                                    if (pred instanceof MemPred) {
                                        MemPred memPred = (MemPred) pred;
                                        Expr leftExpr = memPred.getLeftExpr();
                                        Expr rightExpr = memPred.getRightExpr();
                                        boolean mixfix = memPred.getMixfix();
                                        if (mixfix
                                                && SpecUtils.areEqualTerms(varRefExpr, leftExpr)
                                                && rightExpr instanceof SetExpr
                                                && ((SetExpr) rightExpr).getZExprList().size() == 1) {
                                            inEquality = true;
                                            Expr expr = ((SetExpr) rightExpr).getZExprList().get(0);
                                            String strExpr = SpecUtils.termToLatex(expr);

                                            // Then we check whether the expression is constant,
                                            // verifying that it does not contain ocurrences of
                                            // any of the variables declared in the specified
                                            // test class.
                                            for (int i = 0; i < zDeclList.size() && inEquality; i++) {
                                                Decl dec = zDeclList.get(i);
                                                //System.out.println(SpecUtils.termToLatex(dec));
                                                if (dec instanceof VarDecl) {
                                                    VarDecl vardecl = (VarDecl) dec;
                                                    //System.out.println(SpecUtils.termToLatex(vardecl));
                                                    ZNameList zList = vardecl.getName();
                                                    for (int jj = 0; jj < zList.size() && inEquality; jj++) {
                                                        Name name = zList.get(jj);
                                                        if (expr.accept(new ContainsTermVerifier(name))) {
                                                            inEquality = false;
                                                        }
                                                    }
                                                }
                                            }

                                            if (inEquality) {
                                                ZExprList auxZExprList = zFactory.createZExprList();
                                                String emptySetStr = UtilSymbols.emptySetSymbol();
                                                if ((expr instanceof RefExpr
                                                        && ((RefExpr) expr).getZName().toString().equals(emptySetStr))
                                                        || (expr instanceof SetExpr && ((SetExpr) expr).getZExprList().isEmpty())) {
                                                    ZName zName = zFactory.createZName(emptySetStr,
                                                            zFactory.createZStrokeList(), "empty");
                                                    ZExprList refZExprL = zFactory.createZExprList();
                                                    Expr varType = varFiniteModel.getNormalizedType();
                                                    // Agregamos el tipo de los elementos del
                                                    // conjunto que es tipo de la variable que
                                                    // estamos analizando
                                                    refZExprL.add(((PowerExpr) varType).getExpr());
                                                    RefExpr emptySetExpr = zFactory.createRefExpr(zName, refZExprL, false, false);
                                                    auxZExprList.add(emptySetExpr);
                                                    //RefExpr emptySetExpr = (RefExpr) varFiniteModel.next();
                                                    //ZExprList zL = emptySetExpr.getZExprList();
                                                    //for(int r=0;r<zL.size();r++)
                                                    //	System.out.println("Vacio: "+ SpecUtils.termToLatex(zL.get(r)));
                                                    //auxZExprList.add(emptySetExpr);
                                                } else if (expr instanceof ApplExpr
                                                        && ((ApplExpr) expr).getLeftExpr() instanceof RefExpr
                                                        && ((RefExpr) ((ApplExpr) expr).getLeftExpr()).getZName().toString().equals(UtilSymbols.seqAnglesSymbol())
                                                        && ((ApplExpr) expr).getRightExpr() instanceof SetExpr
                                                        && ((SetExpr) ((ApplExpr) expr).getRightExpr()).getZExprList().isEmpty()
                                                        && ((ApplExpr) expr).getMixfix()) {
                                                    ZName zName = zFactory.createZName(emptySetStr,
                                                            zFactory.createZStrokeList(), "empty");
                                                    //ZExprList prodZExprList = //zFactory.createZExprList();
                                                    //prodZExprList.add(SpecUtils.getNumRefExpr());
                                                    //prodZExprList.add(varFiniteModel.getNormalizedType());
                                                    //ProdExpr prodExpr = zFactory.createProdExpr(prodZExprList);
                                                    ZExprList refZExprL = zFactory.createZExprList();

                                                    Expr varType = varFiniteModel.getNormalizedType();
                                                    // Agregamos el tipo de los elementos del
                                                    // conjunto que es tipo de la variable que
                                                    // estamos analizando
                                                    refZExprL.add(((PowerExpr) varType).getExpr());
                                                    Expr emptySeqexpr = zFactory.createRefExpr(zName, refZExprL, false, false);
                                                    //RefExpr emptySeqexpr = (RefExpr) varFiniteModel.next();
                                                    auxZExprList.add(emptySeqexpr);

                                                } else {
                                                    auxZExprList.add(expr);
                                                }
                                                finiteModelList.add(new SetFiniteModel(auxZExprList));
                                                varList.add(varRefExpr);
                                            }
                                        }
                                    }
                                }
                                // We verify if the variable is contained in the test
                                //class predicate 
                                boolean inPred = tClassPred.accept
                                        (new ContainsTermVerifier(varRefExpr)).booleanValue();
                                if (!inEquality && inPred) {
                                    finiteModelList.add((FiniteModel) varFiniteModel.clone());
                                    varList.add(varRefExpr);
                                } else if (!inEquality) {
                                    ZExprList auxZExprList = zFactory.createZExprList();
                                    if (varFiniteModel != null) {
                                        if (varFiniteModel.hasNext()) {
                                            auxZExprList.add(((FiniteModel) varFiniteModel.clone()).next());
                                            finiteModelList.add(new SetFiniteModel(auxZExprList));
                                            varList.add(varRefExpr);
                                        }
                                    }
                                    //else
                                    //	System.out.println("No tenia elementos");
                                }
                            }
                            //varList.add(varRefExpr);
                        }
                    }
                }
            }
        }


        // We initialize zExprList with the first element returned by each FiniteModel of finiteModelList
        zExprList = zFactory.createZExprList();
        for (int i = 0; i < finiteModelList.size(); i++) {
            zExprList.add(i, finiteModelList.get(i).next());
        }


        // We calculate the size
        size = 1;
        for (int i = 0; i < finiteModelList.size(); i++) {
            FiniteModel finiteModel = finiteModelList.get(i);
            size *= finiteModel.getFMSize();
            //System.out.println("El tamaño de "+i+"es: "+finiteModel.getFMSize());
        }
        if (size < 0) {
            size = 9999999;
        }

    }

    /**
     * Creates an instance of TClassFiniteModel.
     * @param tClass
     * @param finiteModelCreator
     */
    public TClassFiniteModel(TClass tClass, FiniteModelCreator finiteModelCreator,
            Pred atomicPred, Map<RefExpr, List<Expr>> matchMap, List<Pred> groupPreds) {

        // Inicializamos el indice
        index = 0;

        // Inicializamos la lista de variables y la lista de FiniteModel
        zFactory = new ZFactoryImpl();
        finiteModelList = new ArrayList<FiniteModel>();
        varList = zFactory.createZExprList();



        Map<RefExpr, SetExpr> varSetExprMap = new HashMap<RefExpr, SetExpr>();
        zExprList = zFactory.createZExprList();


        AxPara axPara = tClass.getMyAxPara();
        DeclList declList = SpecUtils.getAxParaListOfDecl(axPara);
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    Expr type = varDecl.getExpr();
                    // We create a FiniteModel for the declaration type
                    // If there are more than one variable in this declaration, we will
                    // clone the instance of FiniteModel
                    FiniteModel typeFiniteModel = finiteModelCreator.getFiniteModel(type);
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        ZExprList tempZExprList = zFactory.createZExprList();
                        for (int k = 0; k < zNameListImpl.size(); k++) {
                            //System.out.println(zNameListImpl.get(k));
                            RefExpr varRefExpr =
                                    zFactory.createRefExpr
                                    (zNameListImpl.get(k), tempZExprList, false, false);
                            if (atomicPred.accept
                                    (new ContainsTermVerifier(varRefExpr))
                                    && matchMap.get(varRefExpr) == null) {

                                FiniteModel varFiniteModel = finiteModelCreator.getFiniteModel(varRefExpr);
                                if (varFiniteModel == null) {
                                    varFiniteModel = typeFiniteModel;
                                }
                                //Pred tClassPred = SpecUtils.getAxParaPred(axPara);
                                //Pred tClassPred = atomicPred;
                                //if(tClassPred!=null)
                                if (groupPreds.size() > 0) {
                                    //AbstractRepository<Pred> predRep =
                                    // tClassPred.accept(new AndPredClausesExtractor());
                                    // AbstractIterator<Pred> predIt = predRep.createIterator();
                                    boolean inEquality = false;
                                    // We verify whether the variable is equal to a constant
                                    // expression

                                    for (int index = 0; index < groupPreds.size(); index++) {
                                        Pred pred = groupPreds.get(index);
                                        // At first, we verify whether the variable is equal to some
                                        // expression, independently whether it is constant or not.

                                        if (pred instanceof MemPred) {
                                            MemPred memPred = (MemPred) pred;
                                            Expr leftExpr = memPred.getLeftExpr();
                                            Expr rightExpr = memPred.getRightExpr();
                                            boolean mixfix = memPred.getMixfix();
                                            if (mixfix
                                                    && SpecUtils.areEqualTerms(varRefExpr, leftExpr)
                                                    && rightExpr instanceof SetExpr
                                                    && ((SetExpr) rightExpr).getZExprList().size() == 1) {
                                                inEquality = true;
                                                Expr expr = ((SetExpr) rightExpr).getZExprList().get(0);
                                                String strExpr = SpecUtils.termToLatex(expr);

                                                // Then we check whether the expression is constant,
                                                // verifying that it does not contain ocurrences of
                                                // any of the variables declared in the specified
                                                // test class.
                                                for (int i = 0; i < zDeclList.size() && inEquality; i++) {
                                                    Decl dec = zDeclList.get(i);
                                                    //System.out.println(SpecUtils.termToLatex(dec));
                                                    if (dec instanceof VarDecl) {
                                                        VarDecl vardecl = (VarDecl) dec;
                                                        //System.out.println(SpecUtils.termToLatex(vardecl));
                                                        ZNameList zList = vardecl.getName();
                                                        for (int jj = 0; jj < zList.size() && inEquality; jj++) {
                                                            Name name = zList.get(jj);
                                                            if (expr.accept(new ContainsTermVerifier(name))) {
                                                                inEquality = false;
                                                            }
                                                        }
                                                    }
                                                }

                                                if (inEquality) {
                                                    ZExprList auxZExprList = zFactory.createZExprList();
                                                    String emptySetStr = UtilSymbols.emptySetSymbol();
                                                    if ((expr instanceof RefExpr
                                                            && ((RefExpr) expr).getZName().toString().equals(emptySetStr))
                                                            || (expr instanceof SetExpr && ((SetExpr) expr).getZExprList().isEmpty())) {
                                                        ZName zName = zFactory.createZName(emptySetStr,
                                                                zFactory.createZStrokeList(), "empty");
                                                        ZExprList refZExprL = zFactory.createZExprList();
                                                        Expr varType = varFiniteModel.getNormalizedType();
                                                        // Agregamos el tipo de los elementos del
                                                        // conjunto que es tipo de la variable que
                                                        // estamos analizando
                                                        refZExprL.add(((PowerExpr) varType).getExpr());
                                                        RefExpr emptySetExpr = zFactory.createRefExpr(zName, refZExprL, false, false);
                                                        auxZExprList.add(emptySetExpr);
                                                        //RefExpr emptySetExpr = (RefExpr) varFiniteModel.next();
                                                        //ZExprList zL = emptySetExpr.getZExprList();
                                                        //for(int r=0;r<zL.size();r++)
                                                        //	System.out.println("Vacio: "+ SpecUtils.termToLatex(zL.get(r)));
                                                        //auxZExprList.add(emptySetExpr);
                                                    } else if (expr instanceof ApplExpr
                                                            && ((ApplExpr) expr).getLeftExpr() instanceof RefExpr
                                                            && ((RefExpr) ((ApplExpr) expr).getLeftExpr()).getZName().toString().equals(UtilSymbols.seqAnglesSymbol())
                                                            && ((ApplExpr) expr).getRightExpr() instanceof SetExpr
                                                            && ((SetExpr) ((ApplExpr) expr).getRightExpr()).getZExprList().isEmpty()
                                                            && ((ApplExpr) expr).getMixfix()) {
                                                        ZName zName = zFactory.createZName(emptySetStr,
                                                                zFactory.createZStrokeList(), "empty");
                                                        //ZExprList prodZExprList = //zFactory.createZExprList();
                                                        //prodZExprList.add(SpecUtils.getNumRefExpr());
                                                        //prodZExprList.add(varFiniteModel.getNormalizedType());
                                                        //ProdExpr prodExpr = zFactory.createProdExpr(prodZExprList);
                                                        ZExprList refZExprL = zFactory.createZExprList();

                                                        Expr varType = varFiniteModel.getNormalizedType();
                                                        // Agregamos el tipo de los elementos del
                                                        // conjunto que es tipo de la variable que
                                                        // estamos analizando
                                                        refZExprL.add(((PowerExpr) varType).getExpr());
                                                        Expr emptySeqexpr = zFactory.createRefExpr(zName, refZExprL, false, false);
                                                        //RefExpr emptySeqexpr = (RefExpr) varFiniteModel.next();
                                                        auxZExprList.add(emptySeqexpr);

                                                    } else {
                                                        auxZExprList.add(expr);
                                                    }
                                                    finiteModelList.add(new SetFiniteModel(auxZExprList));
                                                    varList.add(varRefExpr);
                                                }
                                            }
                                        }
                                    }
                                    // We verify if the variable is contained in the test
                                    //class predicate
                                    //boolean inPred = tClassPred.accept(new ContainsTermVerifier(varRefExpr)).booleanValue();
                                    boolean inPred = atomicPred.accept(new ContainsTermVerifier(varRefExpr)).booleanValue();
                                    if (!inEquality && inPred) {
                                        finiteModelList.add((FiniteModel) varFiniteModel.clone());
                                        varList.add(varRefExpr);
                                    } else if (!inEquality) {
                                        ZExprList auxZExprList = zFactory.createZExprList();
                                        if (varFiniteModel != null) {
                                            if (varFiniteModel.hasNext()) {
                                                auxZExprList.add(((FiniteModel) varFiniteModel.clone()).next());
                                                finiteModelList.add(new SetFiniteModel(auxZExprList));
                                                varList.add(varRefExpr);
                                            }
                                        }
                                        //else
                                        //	System.out.println("No tenia elementos");
                                    }
                                }
                            }

                            //varList.add(varRefExpr);
                        }
                    }
                }
            }
        }

        //System.out.println("El modelo finito tiene "+finiteModelList.size()+" elementos.");

        // We initialize zExprList with the first element returned by each FiniteModel of finiteModelList
        zExprList = zFactory.createZExprList();
        for (int i = 0; i < finiteModelList.size(); i++) {
            zExprList.add(i, finiteModelList.get(i).next());
        }


        // We calculate the size
        size = 1;
        for (int i = 0; i < finiteModelList.size(); i++) {
            FiniteModel finiteModel = finiteModelList.get(i);
            size *= finiteModel.getFMSize();
            //System.out.println("El tamaño de "+i+"es: "+finiteModel.getFMSize());
        }
        if (size < 0) {
            size = 9999999;
        }

    }

    /**
     * Initialize index to 0, each FiniteModel of finiteModelList and zExprList with the first element
     * returned by each FiniteModel from finiteModelList.
     */
    public void initialize() {
        index = 0;
        zExprList.clear();
        for (int i = 0; i < finiteModelList.size(); i++) {
            FiniteModel finiteModel = finiteModelList.get(i);
            finiteModel.initialize();
            zExprList.add(i, finiteModel.next());
        }
    }

    /**
     * Returns true whether the iteration has more elements.
     * @return true whether the iterator has more elements.
     */
    public boolean hasNext() {
        return index < size;
    }

    public Map<RefExpr, Expr> next() {

        Map<RefExpr, Expr> varExprMap = new HashMap<RefExpr, Expr>();

        for (int i = 0; i < zExprList.size(); i++) {
            varExprMap.put((RefExpr) varList.get(i), zExprList.get(i));
//System.out.println(i+" "+SpecUtils.termToLatex(varList.get(i))+" "+ SpecUtils.termToLatex(zExprList.get(i)));
        }

        /* We modify zExprList in order to return the correct value the next time next()
        is called
         */
        boolean hasInc = false;
        for (int j = 0; j < finiteModelList.size() && !hasInc; j++) {
            FiniteModel finiteModel = finiteModelList.get(j);
            if (finiteModel.hasNext()) {
                hasInc = true;
            } else {
                finiteModel.initialize();
            }
            Expr auxExpr = finiteModel.next();
            //System.out.println(j+" :"+SpecUtils.termToLatex(auxExpr));
            zExprList.set(j,/*finiteModel.next()*/ auxExpr);
        }

        index++;
        return varExprMap;

    }

    /**
     * Gets the finite model size related to this instance of TClassFiniteModel.
     * @return
     */
    public int getFMSize() {
        return size;
    }
} 
 
