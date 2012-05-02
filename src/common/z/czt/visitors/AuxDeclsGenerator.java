package common.z.czt.visitors;

import common.z.SpecUtils;
import common.z.UtilSymbols;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.DecorExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Expr2;
import net.sourceforge.czt.z.ast.InclDecl;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SchExpr;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.z.visitor.Expr2Visitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.SchExprVisitor;

/**
 * This class was made to allow the construction of auxiliar schemas that were 
 * used to define the declaration part of the VIS of tested operations. The 
 * schemas are kept in a ZParaList. Each schema only declares input and 
 * not-primed variables (explicitly or through schema inclusion of other
 * auxiliar schema) and does not  have predicate (ie. its predicates is true; 
 * null in the implementation).
 * 
 * The list of schemas is calculated recursively, based on the Visitor design
 * pattern.
 * 
 * @author Pablo Rodriguez Monetti
 */
public class AuxDeclsGenerator implements SchExprVisitor<ZDeclList>,
        RefExprVisitor<ZDeclList>,
        AxParaVisitor<ZDeclList>,
        Expr2Visitor<ZDeclList> {

    private ZParaList zParaList;

    public AuxDeclsGenerator(ZParaList zParaList) {
        this.zParaList = zParaList;
    }

    public ZDeclList visitSchExpr(SchExpr schExpr) {
        ZSchText zSchText = schExpr.getZSchText();
        DeclList declList = zSchText.getDeclList();
        ZFactory zFactory = new ZFactoryImpl();
        ZDeclList newZDeclList = zFactory.createZDeclList();

        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int i = 0; i < zDeclList.size(); i++) {
                Decl decl = zDeclList.get(i);
                if (decl instanceof InclDecl) {
                    InclDecl inclDecl = (InclDecl) decl;
                    Expr inclExpr = inclDecl.getExpr();
                    String inclName = "";
                    if (inclExpr instanceof RefExpr) {
                        inclName = ((RefExpr) inclExpr).getName().toString();
                        int firstCharCode = (int) inclName.charAt(0);
                        if (firstCharCode == 916) {
                            //The included schema is a \Delta
                            inclName = inclName.substring(1);
                        } else if (firstCharCode == 926) {
                            //The included schema is a \Xi
                            inclName = inclName.substring(1);
                        }
                    } else if (inclExpr instanceof DecorExpr) {
                        inclName = ((RefExpr) ((DecorExpr) inclExpr).getExpr()).getName().toString();
                    }

                    String newInclName = inclName + "_Decls";
                    ZName zName = zFactory.createZName(newInclName,
                            zFactory.createZStrokeList(), newInclName);
                    ZExprList zExprList = zFactory.createZExprList();
                    RefExpr newRefExpr = zFactory.createRefExpr(zName, zExprList, false,
                            false);
                    InclDecl newInclDecl = zFactory.createInclDecl(newRefExpr);

                    newZDeclList.add(newInclDecl);
                    AxPara inclAxPara = SpecUtils.axParaSearch(inclName,
                            zParaList);
                    inclAxPara.accept(this);

                } else if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl.accept(new CZTCloner());
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        for (int k = 0; k < zNameListImpl.size(); k++) {
                            String varName = zNameListImpl.get(k).toString();
                            if (varName.endsWith("!")
                                    || varName.endsWith(UtilSymbols.primeSymbol())) {
                                zNameListImpl.remove(k);
                            }
                        }
                        if (zNameListImpl.size() != 0) {
                            newZDeclList.add(varDecl);
                        }

                    }
                }

            }
        }
        return newZDeclList;
    }

    public ZDeclList visitAxPara(AxPara axPara) {
        Expr axParaExpr = null;
        ZDeclList newDeclList = null;
        ZSchText zSchText = axPara.getZSchText();
        String axParaName = SpecUtils.getAxParaName(axPara);
        DeclList declList = zSchText.getDeclList();
        if (declList instanceof ZDeclList) {
            for (int j = 0; j < ((ZDeclList) declList).size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    axParaExpr = constDecl.getExpr();
                    newDeclList = axParaExpr.accept(this);
                }
            }
        }

        AxPara newAxPara = SpecUtils.createAxPara(newDeclList, null);
        String newAxParaName = axParaName + "_Decls";
        SpecUtils.setAxParaName(newAxPara, newAxParaName);
        if (SpecUtils.axParaSearch(newAxParaName, zParaList) == null) {
            zParaList.add(newAxPara);
        }
        return newDeclList;
    }

    

    public ZDeclList visitRefExpr(RefExpr refExpr) {
        // All the associated AxParas are generated recursively
        String refExprName = refExpr.getZName().getWord().toString();

        int firstCharCode = (int) refExprName.charAt(0);
        if (firstCharCode == 916) {
            //The included schema is a \Delta
            refExprName = refExprName.substring(1);
        } else if (firstCharCode == 926) {
            //The included schema is a \Xi
            refExprName = refExprName.substring(1);
        }

        (SpecUtils.axParaSearch(refExprName, zParaList)).accept(this);

        ZFactory zFactory = new ZFactoryImpl();

        String newInclName = refExprName + "_Decls";
        ZName zName = zFactory.createZName(newInclName,
                zFactory.createZStrokeList(), newInclName);
        ZExprList zExprList = zFactory.createZExprList();
        RefExpr newRefExpr = zFactory.createRefExpr(zName, zExprList, false,
                false);
        InclDecl newInclDecl = zFactory.createInclDecl(newRefExpr);
        ZDeclList newZDeclList = zFactory.createZDeclList();
        newZDeclList.add(newInclDecl);

        return newZDeclList;
    }

    public ZDeclList visitExpr2(Expr2 expr2) {
        ZDeclList leftZDeclList = expr2.getLeftExpr().accept(this);
        ZDeclList rightZDeclList = expr2.getRightExpr().accept(this);
        for (int i = 0; i < rightZDeclList.size(); i++) {
            Decl decl = rightZDeclList.get(i);
            leftZDeclList.add(decl);
        }
        return leftZDeclList;
    }
}
