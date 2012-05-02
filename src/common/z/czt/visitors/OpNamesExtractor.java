package common.z.czt.visitors;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.z.SchemeType;
import common.z.SpecUtils;
import common.z.UtilSymbols;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.CompExpr;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.DecorExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.IffExpr;
import net.sourceforge.czt.z.ast.ImpliesExpr;
import net.sourceforge.czt.z.ast.InclDecl;
import net.sourceforge.czt.z.ast.Name;
import net.sourceforge.czt.z.ast.NameList;
import net.sourceforge.czt.z.ast.NextStroke;
import net.sourceforge.czt.z.ast.NumExpr;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.ast.OutStroke;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.PowerExpr;
import net.sourceforge.czt.z.ast.ProdExpr;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SchExpr;
import net.sourceforge.czt.z.ast.SchExpr2;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.Stroke;
import net.sourceforge.czt.z.ast.StrokeList;
import net.sourceforge.czt.z.ast.VarDecl;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.ZStrokeList;
import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.visitor.AndExprVisitor;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.z.visitor.CompExprVisitor;
import net.sourceforge.czt.z.visitor.DecorExprVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.IffExprVisitor;
import net.sourceforge.czt.z.visitor.ImpliesExprVisitor;
import net.sourceforge.czt.z.visitor.NumExprVisitor;
import net.sourceforge.czt.z.visitor.OrExprVisitor;
import net.sourceforge.czt.z.visitor.PowerExprVisitor;
import net.sourceforge.czt.z.visitor.ProdExprVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.SchExprVisitor;
import net.sourceforge.czt.z.visitor.SetExprVisitor;
import net.sourceforge.czt.z.visitor.SpecVisitor;

/**
 * This visitor makes it possible to extract the operation names of an
 * specification.
 * 
 * This class is based on the Visitor design pattern.
 * 
 * The class is implemented assuming that every schema is of one of the
 * following types:
 *     1- Input 
 *     2- Output
 *     3- Operation
 *     4- Without Vars
 * 
 * @author Pablo Rodriguez Monetti
 */
public class OpNamesExtractor implements SpecVisitor<SchemeType>,
        AxParaVisitor<SchemeType>,
        AndExprVisitor<SchemeType>,
        OrExprVisitor<SchemeType>,
        SchExprVisitor<SchemeType>,
        RefExprVisitor<SchemeType>,
        SetExprVisitor<SchemeType>,
        NumExprVisitor<SchemeType>,
        ImpliesExprVisitor<SchemeType>,
        IffExprVisitor<SchemeType>,
        CompExprVisitor<SchemeType>,
        DecorExprVisitor<SchemeType>,
        PowerExprVisitor<SchemeType>,        
        ProdExprVisitor<SchemeType>,        
        ExprVisitor<SchemeType> {

    // The list of paragraph that conform the loaded specification
    private ZParaList zParaList;
    // The names of the operations that have been found
    private AbstractRepository<String> opNames;
    // The types of schemas previously calculated
    private Map<String, SchemeType> schemesTypes;

    public OpNamesExtractor(Spec spec) {
        opNames = new ConcreteRepository<String>();
        schemesTypes = new HashMap<String, SchemeType>();

        for (Sect sect : spec.getSect()) {
            if (sect instanceof ZSect) {
                ParaList paraList = ((ZSect) sect).getParaList();
                if (paraList instanceof ZParaList) {
                    zParaList = (ZParaList) paraList;
                }
            }
        }
    }

    public SchemeType visitSpec(Spec spec) {
        for (Sect sect : spec.getSect()) {
            if (sect instanceof ZSect) {
                ParaList paraList = ((ZSect) sect).getParaList();
                if (paraList instanceof ZParaList) {
                    for (int i = 0; i < ((ZParaList) paraList).size(); i++) {
                        Para para = ((ZParaList) paraList).get(i);
                        if (para instanceof AxPara) {
                            AxPara axPara = (AxPara) para;
                            if (axPara.getBox() != Box.AxBox) {
                                axPara.accept(this);
                            }
                        }
                    }
                }
            }
        }
        return SchemeType.WITHOUT_VARS;
    }

    public SchemeType visitAxPara(AxPara axPara) {
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();
        String schemeName = "";
        SchemeType schemeType = null;
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = zDeclList.get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    ZName zName = constDecl.getZName();
                    schemeName = zName.getWord();
                    // If the type of this scheme was previously calculated
                    // it is returned
                    schemeType = schemesTypes.get(schemeName);
                    if (schemeType != null) {
                        return schemeType;
                    }
                    Expr expr = constDecl.getExpr();
                    schemeType = expr.accept(this);
                }
            }
        }
        schemesTypes.put(schemeName, schemeType);


        if(schemeType == SchemeType.OP)
            opNames.addElement(schemeName);
        return schemeType;
    }

    public SchemeType visitSchExpr(SchExpr schExpr) {
        ZSchText zSchText = schExpr.getZSchText();
        DeclList declList = zSchText.getDeclList();
        SchemeType schemeType = SchemeType.WITHOUT_VARS;
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = zDeclList.get(j);
                if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        for (int i = 0; i < zNameListImpl.size(); i++) {
                            Name name = zNameListImpl.get(i);
                            boolean isNotPrimedOrInputVariable = true;
                            if (name instanceof ZName) {
                                ZName zName = (ZName) name;
                                StrokeList strokeList = zName.getStrokeList();
                                if (strokeList instanceof ZStrokeList) {
                                    ZStrokeList zStrokeList =
                                            (ZStrokeList) strokeList;
                                    for (int k = 0; k < zStrokeList.size(); k++) {
                                        Stroke stroke = zStrokeList.get(k);
                                        if (stroke instanceof OutStroke
                                                || stroke instanceof NextStroke) {
                                            isNotPrimedOrInputVariable = false;
                                        }
                                    }
                                    if (isNotPrimedOrInputVariable) {
                                        if (schemeType == SchemeType.WITHOUT_VARS) {
                                            schemeType = SchemeType.IN;
                                        } else if (schemeType == SchemeType.OUT) {
                                            schemeType = SchemeType.OP;
                                        }
                                    } else {
                                        if (schemeType == SchemeType.WITHOUT_VARS) {
                                            schemeType = SchemeType.OUT;
                                        } else if (schemeType == SchemeType.IN) {
                                            schemeType = SchemeType.OP;
                                        }

                                    }
                                }
                            }
                        }
                    }
                } else if (decl instanceof InclDecl) {
                    Expr expr = ((InclDecl) decl).getExpr();
                    String includedSchemeName = "";
                    String inclusionType = "unknown";
                    if (expr instanceof RefExpr) {
                        String refExprName = ((RefExpr) expr).getName().toString();
                        int firstCharCode = (int) refExprName.charAt(0);
                        if (firstCharCode == 916 || firstCharCode == 926) {
                            // The included scheme is a Delta or a Xi
                            includedSchemeName = refExprName.substring(1);
                            inclusionType = "change";

                        } else {
                            // The included scheme is not a Delta nor a Xi nor is
                            // primed
                            includedSchemeName = refExprName;
                            inclusionType = "state";
                        }
                    } else if (expr instanceof DecorExpr) {
                        // The included scheme is primed
                        includedSchemeName = ((RefExpr) ((DecorExpr) expr).getExpr()).getName().
                                toString();
                        inclusionType = "primed";

                    }

                    SchemeType includedSchemeType =
                            schemesTypes.get(includedSchemeName);
                    if (includedSchemeType == null) {
                        // If the type of the included scheme was not previously
                        // calculated, we calculate it
                        AxPara includedSchemeAxPara =
                                SpecUtils.axParaSearch(includedSchemeName, zParaList);
                        if (includedSchemeAxPara != null) {
                            includedSchemeType =
                                    includedSchemeAxPara.accept(this);
                        }
                    }
                    if (inclusionType.equals("change")) {
                        if (includedSchemeType == SchemeType.IN) {
                            schemeType = SchemeType.OP;
                        } else {
                            System.out.println("UNEXPECTED SCHEME INCLUSION 1: " +
                                    includedSchemeName);
                        }

                    } else if (inclusionType.equals("state")) {
                        if (includedSchemeType == SchemeType.OP) {
                            schemeType = SchemeType.OP;
                        } else if (includedSchemeType == SchemeType.IN) {
                            if (schemeType == SchemeType.WITHOUT_VARS) {
                                schemeType = SchemeType.IN;
                            } else if (schemeType == SchemeType.OUT) {
                                schemeType = SchemeType.OP;
                            }
                        } else if (includedSchemeType == SchemeType.OUT) {
                            if (schemeType == SchemeType.WITHOUT_VARS) {
                                schemeType = SchemeType.OUT;
                            } else if (schemeType == SchemeType.IN) {
                                schemeType = SchemeType.OP;
                            }
                        }
                    } else if (inclusionType.equals("primed")) {
                        if (includedSchemeType == SchemeType.IN) {
                            if (schemeType == SchemeType.WITHOUT_VARS) {
                                schemeType = SchemeType.OUT;
                            } else if (schemeType == SchemeType.IN) {
                                schemeType = SchemeType.OP;
                            }
                        } else {
                            System.out.println("UNEXPECTED SCHEME INCLUSION 2.");
                        }
                    } else if (inclusionType.equals("unknown")) {
                        System.out.println("UNEXPECTED SCHEME INCLUSION 3.");
                    }
                }

            }
        }

        return schemeType;
    }

    public SchemeType visitRefExpr(RefExpr refExpr) {
        SchemeType schemeType = SchemeType.WITHOUT_VARS;

        String refExprName = refExpr.getZName().getWord().toString();

        if (refExprName.equals(UtilSymbols.integerSymbol())
                || refExprName.equals(UtilSymbols.naturalSymbol())
                || refExprName.equals("?")
                || refExpr.getZExprList().size() != 0) {
            return SchemeType.WITHOUT_VARS;
        }

        String refType = "unknown";
        String refSchemeName = "";
        int firstCharCode = (int) refExprName.charAt(0);
        if (firstCharCode == 916 || firstCharCode == 926) {
            // The included scheme is a Delta or a Xi
            refSchemeName = refExprName.substring(1);
            refType = "change";

        } else {
            // The included scheme is not a Delta nor a Xi nor is
            // primed
            refType = "state";
            refSchemeName = refExprName;
        }

        SchemeType referredSchemeType =
                schemesTypes.get(refSchemeName);
        if (referredSchemeType == null) {
            // If the type of the referred scheme was not previously
            // calculated, it is calculated now
            AxPara referredSchemeAxPara =
                    SpecUtils.axParaSearch(refSchemeName, zParaList);
            if (referredSchemeAxPara != null) {
                referredSchemeType =
                        referredSchemeAxPara.accept(this);
            }
        }
        if (refType.equals("change")) {
            if (referredSchemeType == SchemeType.IN) {
                schemeType = SchemeType.OP;
            } else {
                System.out.println("UNEXPECTED SCHEME INCLUSION 1.");
            }

        } else if (refType.equals("state")) {
                schemeType = referredSchemeType;
        } else if (refType.equals("primed")) {
            if (referredSchemeType == SchemeType.IN) {
                    schemeType = SchemeType.OUT;                
            } else {
                System.out.println("UNEXPECTED SCHEME INCLUSION 2.");
            }
        } else if (refType.equals("unknown")) {
            System.out.println("UNEXPECTED SCHEME INCLUSION 3.");
        }
        return schemeType;
    }


    public SchemeType visitAndExpr(AndExpr andExpr) {
        return visitOrAndImpliesIffExpr(andExpr);
    }

    public SchemeType visitOrExpr(OrExpr orExpr) {
        return visitOrAndImpliesIffExpr(orExpr);
    }

    public SchemeType visitImpliesExpr(ImpliesExpr impliesExpr) {
        return visitOrAndImpliesIffExpr(impliesExpr);
    }

    public SchemeType visitIffExpr(IffExpr iffExpr) {
        return visitOrAndImpliesIffExpr(iffExpr);
    }


    public SchemeType visitSetExpr(SetExpr setExpr){
        return SchemeType.WITHOUT_VARS;
    }

    public SchemeType visitNumExpr(NumExpr numExpr){
        return SchemeType.WITHOUT_VARS;
    }


    public SchemeType visitCompExpr(CompExpr compExpr){
        return SchemeType.WITHOUT_VARS;
    }
    
    
    public SchemeType visitDecorExpr(DecorExpr decorExpr){
        return SchemeType.OUT;
    }    


    public SchemeType visitProdExpr(ProdExpr prodExpr){
        return SchemeType.WITHOUT_VARS;       
    }
    
    public SchemeType visitPowerExpr(PowerExpr prodExpr){
        return SchemeType.WITHOUT_VARS;       
    }    
    
    
    
    public SchemeType visitExpr(Expr expr) {

        System.out.println("Unexpected expression while looking for scheme "
                + "operations: " + SpecUtils.termToLatex(expr) + " of type " +
                expr.getClass().toString());
        return SchemeType.WITHOUT_VARS;
    }

    // The visit method for SchExpr2 was not directly defined because not all
    // the subtypes of SchExpr2 are analized in the same way
    private SchemeType visitOrAndImpliesIffExpr(SchExpr2 schExpr2) {
        SchemeType schemeType = SchemeType.WITHOUT_VARS;
        Expr leftExpr = schExpr2.getLeftExpr();
        Expr rightExpr = schExpr2.getRightExpr();
        SchemeType type1 = leftExpr.accept(this);
        SchemeType type2 = rightExpr.accept(this);

        if (type1 == SchemeType.OP || type2 == SchemeType.OP) {
            schemeType = SchemeType.OP;
        } else if (type1 == SchemeType.IN && type2 == SchemeType.OUT) {
            schemeType = SchemeType.OP;
        } else if (type2 == SchemeType.IN && type1 == SchemeType.OUT) {
            schemeType = SchemeType.OP;
        } else if (type1 == SchemeType.IN || type2 == SchemeType.IN) {
            schemeType = SchemeType.IN;
        } else if (type1 == SchemeType.OUT || type2 == SchemeType.OUT) {
            schemeType = SchemeType.OUT;
        } else {
            schemeType = SchemeType.WITHOUT_VARS;
        }
        return schemeType;
    }


    public AbstractRepository<String> getOpNames(){
        return opNames;
    }
}
