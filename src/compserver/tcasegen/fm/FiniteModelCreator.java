package compserver.tcasegen.fm;

import compserver.tcasegen.fm.natfm.NatFiniteModel;
import compserver.tcasegen.fm.intfm.IntFiniteModel;
import java.util.*;

import java.math.*;

import net.sourceforge.czt.z.ast.TupleExpr;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.PowerExpr;
import net.sourceforge.czt.z.ast.ProdExpr;
import net.sourceforge.czt.z.ast.ApplExpr;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZNameList;
import net.sourceforge.czt.z.ast.BranchList;
import net.sourceforge.czt.z.ast.ZBranchList;
import net.sourceforge.czt.z.ast.Freetype;
import net.sourceforge.czt.z.ast.GivenPara;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.ApplExprVisitor;
import net.sourceforge.czt.z.visitor.PowerExprVisitor;
import net.sourceforge.czt.z.visitor.SetExprVisitor;
import net.sourceforge.czt.z.visitor.ProdExprVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.FreetypeVisitor;
import net.sourceforge.czt.z.visitor.GivenParaVisitor;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.base.ast.Term;


import common.z.UtilSymbols;

/**
 * An instance of this class allow the traverse over an specification in order to create
 * finite model generators (instances of FiniteModel) associated to the types that are defined 
 * globally in the specification. FiniteModelCreator is based on the Visitor design pattern.
 * @author Pablo Rodriguez Monetti
 */
public class FiniteModelCreator implements RefExprVisitor<FiniteModel>,
        ApplExprVisitor<FiniteModel>, PowerExprVisitor<FiniteModel>,
        ProdExprVisitor<FiniteModel>, SetExprVisitor<FiniteModel>,
        ExprVisitor<FiniteModel>, TermVisitor<FiniteModel>,
        AxParaVisitor<FiniteModel>, GivenParaVisitor<FiniteModel>,
        FreetypeVisitor<FiniteModel> {

    private Map<Expr, FiniteModel> exprMap;
    private int fMSize;
    private int btSize;
    private ZFactory zFactory;
    private IntFiniteModel intFiniteModel;
    private NatFiniteModel natFiniteModel;
    private Map<String, List<String>> basicAxDefs;

    /**
     * Creates instances of this class.
     * @param exprMap
     * @param fMSize
     * @param intFiniteModel
     * @param natFiniteModel
     */
    public FiniteModelCreator(Map<Expr, FiniteModel> exprMap, int fMSize,
            int btSize, IntFiniteModel intFiniteModel, NatFiniteModel natFiniteModel,
            Map<String, List<String>> basicAxDefs) {


        if (exprMap == null) {
            exprMap = new HashMap<Expr, FiniteModel>();
        }

        this.exprMap = exprMap;
        this.fMSize = fMSize;

        this.btSize = btSize;
        this.intFiniteModel = intFiniteModel;
        this.natFiniteModel = natFiniteModel;
        this.basicAxDefs = basicAxDefs;
        zFactory = new ZFactoryImpl();
    }

    /**
     * Visits an instance of the specified term and creates a FiniteModel associated to it.
     * @param term
     * @return a FiniteModel associated to the specified term.
     */
    public FiniteModel visitTerm(Term term) {
        VisitorUtils.visitTerm(this, term);
        return null;
    }

    /**
     * 
     * @param axPara
     * @return
     */
    public FiniteModel visitAxPara(AxPara axPara) {
        if (axPara.getBox() != Box.OmitBox) //Ver que hacer cuando el axPara es una definición axiomática o un esquema vertical
        {
            return null;
        }
        // Si es una definicion horizaontal...
        // Que para este momento solo pueden ser abreviaturas
        // Pues se supone que ya se expandieron los esquemas
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();


        FiniteModel finiteModel = null;
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = zDeclList.get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    ZName zName = constDecl.getZName();
                    // We create the RefExpr
                    ZExprList zExprList = zFactory.createZExprList();
                    RefExpr refExpr = zFactory.createRefExpr(zName, zExprList,
                            false, false);
                    finiteModel = exprMap.get(refExpr);
                    if (finiteModel == null) {
                        Expr rightExpr = constDecl.getExpr();
                        finiteModel = getFiniteModel(rightExpr);
                        if (finiteModel == null) {
                            return null;
                        }
                        finiteModel = (FiniteModel) finiteModel.clone();
                        //We add the mappings
                        exprMap.put(refExpr, finiteModel);
                    } else {
                        finiteModel = (FiniteModel) finiteModel.clone();
                    }
                }
            }
        }
        return finiteModel;
    }

    public FiniteModel visitGivenPara(GivenPara givenPara) {
        ZNameList zNameList = givenPara.getZNameList();
        FiniteModel finiteModel = null;



        for (int i = 0; i < zNameList.size(); i++) {
            // We create the RefExpr for the ith ZName
            Name name = zNameList.get(i);
            RefExpr refExpr =
                    zFactory.createRefExpr(name, zFactory.createZExprList(), false, false);

            List<String> constants = null;
            if (basicAxDefs != null) {
                constants = basicAxDefs.get(name.toString());
            }


            if (exprMap.get(refExpr) == null) //We add the mapping
            {
                exprMap.put(refExpr, new GivenFiniteModel(btSize, name.toString(), constants));
            }
        }
        // Devolvemos null porque en verdad pudimos haber generado varios GivenFiniteModel, que fueron agregados a exprMap
        return null;
    }

    public FiniteModel visitFreetype(Freetype freetype) {

        // We create the RefExpr for the ZName
        RefExpr refExpr = zFactory.createRefExpr(freetype.getZName(), zFactory.createZExprList(), false, false);
        FiniteModel finiteModel = exprMap.get(refExpr);
        if (finiteModel == null) {
            BranchList branchList = freetype.getBranchList();
            if (branchList instanceof ZBranchList) {
                //We add the mapping
                finiteModel = new FreetypeFiniteModel((ZBranchList) branchList);
                exprMap.put(refExpr, finiteModel);
            }
        } else {
            finiteModel = (FiniteModel) finiteModel.clone();
        }

        return finiteModel;
    }

    public FiniteModel visitRefExpr(RefExpr refExpr) {
        String refExprName = refExpr.getZName().getWord().toString();

        ZExprList setZExprList = zFactory.createZExprList();

        FiniteModel finiteModel = exprMap.get(refExpr);

        if (finiteModel != null) {
            return (FiniteModel) finiteModel.clone();
        }


        if (refExprName.equals(UtilSymbols.integerSymbol())) {
            //La refExpr denota a los enteros
            finiteModel = intFiniteModel;
            //We add the mapping
            exprMap.put(refExpr, finiteModel);
        } else if (refExprName.equals(UtilSymbols.naturalSymbol())) {
            //La refExpr denota a los naturales
            finiteModel = natFiniteModel;
            //We add the mapping
            exprMap.put(refExpr, finiteModel);
        } else if (refExprName.equals(UtilSymbols.relationSymbol())) {
            //La refExpr denota al conjunto de relaciones entre dos conjuntos A y B
            ZExprList refZExprList = refExpr.getZExprList();
            //El <-> es un operador binario entre A y B, aunque A<->B puede denotar A'<->A''<->B si A = A'<->A''
            //  o puede denotar A<->B'<->B'' si B = B'<->B''
            if (refZExprList.size() != 2) {
                return null;
            }

            FiniteModel finiteModelA = getFiniteModel(refZExprList.get(0));
            FiniteModel finiteModelB = getFiniteModel(refZExprList.get(1));

            if (finiteModelA == null || finiteModelB == null) {
                return null;
            }

            finiteModelA = (FiniteModel) finiteModelA.clone();
            finiteModelB = (FiniteModel) finiteModelB.clone();

            finiteModel = new RelFiniteModel(finiteModelA, finiteModelB);
            exprMap.put(refExpr, finiteModel);
        } else if (refExprName.equals(UtilSymbols.partialFunctionSymbol())) {
            //La refExpr denota a las funciones parciales
            ZExprList refZExprList = refExpr.getZExprList();
            //El -> es un operador binario entre A y B, aunque A->B puede denotar A'->A''->B si A = A'->A''
            //  o puede denotar A->B'->B'' si B = B'->B''
            if (refZExprList.size() != 2) {
                return null;
            }

            FiniteModel finiteModelA = getFiniteModel(refZExprList.get(0));
            FiniteModel finiteModelB = getFiniteModel(refZExprList.get(1));

            if (finiteModelA == null || finiteModelB == null) {
                return null;
            }

            finiteModelA = (FiniteModel) finiteModelA.clone();
            finiteModelB = (FiniteModel) finiteModelB.clone();
            finiteModel = new PFuncFiniteModel(finiteModelA, finiteModelB);
            exprMap.put(refExpr, finiteModel);
        } else if (refExprName.equals(UtilSymbols.totalFunctionSymbol())) {
            //La refExpr denota a las funciones totales
            ZExprList refZExprList = refExpr.getZExprList();
            //El -> es un operador binario entre A y B, aunque A->B puede denotar A'->A''->B si A = A'->A''
            //  o puede denotar A->B'->B'' si B = B'->B''
            if (refZExprList.size() != 2) {
                return null;
            }

            FiniteModel finiteModelA = getFiniteModel(refZExprList.get(0));
            FiniteModel finiteModelB = getFiniteModel(refZExprList.get(1));

            if (finiteModelA == null || finiteModelB == null) {
                return null;
            }

            finiteModelA = (FiniteModel) finiteModelA.clone();
            finiteModelB = (FiniteModel) finiteModelB.clone();

            finiteModel = new TFuncFiniteModel(finiteModelA, finiteModelB);
            exprMap.put(refExpr, finiteModel);
        } else if (refExprName.equals("seq _ ")) {
            //La refExpr denota a las secuencias
            ZExprList refZExprList = refExpr.getZExprList();
            if (refZExprList.size() != 1) {
                return null;
            }

            FiniteModel sonFiniteModel = getFiniteModel(refZExprList.get(0));

            if (sonFiniteModel == null) {
                return null;
            }

            sonFiniteModel = (FiniteModel) sonFiniteModel.clone();

            finiteModel = new SeqFiniteModel(sonFiniteModel);
            exprMap.put(refExpr, finiteModel);

        }
        return finiteModel;

    }

    public FiniteModel visitApplExpr(ApplExpr applExpr) {

        FiniteModel finiteModel = exprMap.get(applExpr);

        if (finiteModel != null) {
            return (FiniteModel) finiteModel.clone();
        }

        // Generamos el modelo finitos solo si la expresión es de la forma "a .. b" donde a y b son números
        if (!applExpr.getMixfix()) {
            return null;
        }

        Expr leftExpr = applExpr.getLeftExpr();
        if (!(leftExpr instanceof RefExpr)) {
            return null;
        }

        RefExpr refExpr = (RefExpr) leftExpr;
        String refExprName = refExpr.getName().toString();
        if (!refExprName.equals(" _ .. _ ")) {
            return null;
        }

        Expr rightExpr = applExpr.getRightExpr();
        if (!(rightExpr instanceof TupleExpr)) {
            return null;
        }

        TupleExpr tupleExpr = (TupleExpr) rightExpr;
        ZExprList zExprList = tupleExpr.getZExprList();
        if (zExprList.size() != 2) {
            return null;
        }

        if (!(zExprList.get(0) instanceof NumExpr)) {
            return null;
        }

        if (!(zExprList.get(1) instanceof NumExpr)) {
            return null;
        }

        BigInteger bigInteger0 = ((NumExpr) zExprList.get(0)).getValue();
        int left = (int) bigInteger0.doubleValue();
        BigInteger bigInteger1 = ((NumExpr) zExprList.get(1)).getValue();
        int right = (int) bigInteger1.doubleValue();

        finiteModel = new FromToFiniteModel(left, right);
        exprMap.put(applExpr, finiteModel);
        return finiteModel;
    }

    public FiniteModel visitPowerExpr(PowerExpr powerExpr) {
        FiniteModel finiteModel = exprMap.get(powerExpr);

        if (finiteModel != null) {
            return (FiniteModel) finiteModel.clone();
        }

        Expr expr = powerExpr.getExpr();
        FiniteModel auxFiniteModel = getFiniteModel(expr);


        if (auxFiniteModel == null) {
            return null;
        }
        auxFiniteModel = (FiniteModel) auxFiniteModel.clone();
        finiteModel = new PowerFiniteModel(auxFiniteModel);
        exprMap.put(powerExpr, finiteModel);

        return finiteModel;
    }

    public FiniteModel visitProdExpr(ProdExpr prodExpr) {

        FiniteModel finiteModel = exprMap.get(prodExpr);

        if (finiteModel != null) {
            return (FiniteModel) finiteModel.clone();
        }

        ZExprList zExprList = prodExpr.getZExprList();
        if (zExprList == null || zExprList.size() == 0) {
            return null;
        }

        List<FiniteModel> finiteModelList = new ArrayList<FiniteModel>();
        for (int i = 0; i < zExprList.size(); i++) {

            finiteModel = getFiniteModel(zExprList.get(i));
            if (finiteModel == null) {
                return null;
            }
            finiteModel = (FiniteModel) finiteModel.clone();
            finiteModelList.add(finiteModel);
        }

        FiniteModel prodFiniteModel = new ProdFiniteModel(finiteModelList);
        exprMap.put(prodExpr, prodFiniteModel);
        return prodFiniteModel;
    }

    public FiniteModel visitSetExpr(SetExpr setExpr) {
        FiniteModel finiteModel = new SetFiniteModel(setExpr.getZExprList());
        exprMap.put(setExpr, finiteModel);
        return finiteModel;
    }

    public FiniteModel visitExpr(Expr expr) {
        return null;
    }

    public Map<Expr, FiniteModel> getExprMap() {
        return exprMap;
    }

    public FiniteModel getFiniteModel(Expr expr) {
        /*
        if(expr instanceof RefExpr){
        RefExpr refExpr = (RefExpr) expr;
        System.out.println("Se busca la expresión " + refExpr.getZName());
        }

        Set<Map.Entry<Expr,FiniteModel>> entrySet = exprMap.entrySet();
        Iterator<Map.Entry<Expr,FiniteModel>> it = entrySet.iterator();
        System.out.println("*********");
        while(it.hasNext()){
        Map.Entry<Expr,FiniteModel> entry = it.next();
        Expr key = entry.getKey();
        if(key instanceof RefExpr){
        RefExpr keyRefExpr = (RefExpr) key;
        System.out.println(keyRefExpr.getZName());
        }

        }
        System.out.println("*********");
         */


        FiniteModel finiteModel = exprMap.get(expr);
        if (finiteModel == null) {
            finiteModel = expr.accept(this);
        }
        return finiteModel;

    }
}
 
