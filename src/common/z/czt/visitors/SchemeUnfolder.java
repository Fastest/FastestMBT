/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.z.czt.visitors;

import common.repository.AbstractIterator;
import common.repository.AbstractRepository;
import common.z.DeclDecoration;
import common.z.SpecUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.util.Visitor;
import net.sourceforge.czt.z.ast.AndExpr;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Box;
import net.sourceforge.czt.z.ast.ConstDecl;
import net.sourceforge.czt.z.ast.Decl;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.DecorExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ExprPred;
import net.sourceforge.czt.z.ast.IffExpr;
import net.sourceforge.czt.z.ast.IffPred;
import net.sourceforge.czt.z.ast.ImpliesExpr;
import net.sourceforge.czt.z.ast.ImpliesPred;
import net.sourceforge.czt.z.ast.InclDecl;
import net.sourceforge.czt.z.ast.OrExpr;
import net.sourceforge.czt.z.ast.OrPred;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Pred2;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SchExpr;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.z.visitor.AndExprVisitor;
import net.sourceforge.czt.z.visitor.AxParaVisitor;
import net.sourceforge.czt.z.visitor.ExprVisitor;
import net.sourceforge.czt.z.visitor.IffExprVisitor;
import net.sourceforge.czt.z.visitor.ImpliesExprVisitor;
import net.sourceforge.czt.z.visitor.OrExprVisitor;
import net.sourceforge.czt.z.visitor.RefExprVisitor;
import net.sourceforge.czt.z.visitor.SchExprVisitor;
import net.sourceforge.czt.z.visitor.SpecVisitor;

/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class SchemeUnfolder implements SpecVisitor<Term>,
        AxParaVisitor<Term>,
        AndExprVisitor<Term>,
        OrExprVisitor<Term>,
        SchExprVisitor<Term>,
        RefExprVisitor<Term>,
        ImpliesExprVisitor<Term>,
        IffExprVisitor<Term>,
        ExprVisitor<Term> {

    private AbstractRepository<String> opNames;
    private AbstractRepository<String> schPredNames;
    private Map<String, AxPara> unfoldedAxParas;
    private ZParaList zParaList;

    public SchemeUnfolder(AbstractRepository<String> opNames,
            AbstractRepository<String> schPredNames) {
        this.opNames = opNames;
        this.schPredNames = schPredNames;
    }

    public Spec visitSpec(Spec spec) {
        Visitor<Term> cloneVisitor = new CZTCloner();
        Spec unfoldedSpec = (Spec) spec.accept(cloneVisitor);
        unfoldedAxParas = new HashMap<String, AxPara>();
        for (Sect sect : unfoldedSpec.getSect()) {
            if (sect instanceof ZSect) {
                ParaList paraList = ((ZSect) sect).getParaList();
                if (paraList instanceof ZParaList) {
                    zParaList = (ZParaList) paraList;
                    for (int i = 0; i < zParaList.size(); i++) {
                        Para para = zParaList.get(i);
                        if (para instanceof AxPara) {
                            AxPara axPara = (AxPara) para;
                            if (axPara.getBox() != Box.AxBox) {
                                String axParaName = SpecUtils.getAxParaName(axPara);

                                // The scheme will be unfolded only if it is a
                                // selected operation
                                boolean isSelOp = false;
                                AbstractIterator<String> opNamesIt =
                                        opNames.createIterator();
                                while (opNamesIt.hasNext() && !isSelOp) {
                                    String opName = opNamesIt.next();
                                    if (opName.equals(axParaName)) {
                                        isSelOp = true;
                                    }
                                }
                                if (isSelOp) {
                                    axPara = (AxPara) axPara.accept(this);
                                }
                            }

                        }
                    }
                }
            }
        }
        return unfoldedSpec;
    }

    public AxPara visitAxPara(AxPara axPara) {
        String axParaName = SpecUtils.getAxParaName(axPara);
        if (axParaName.equals("")) {
            return axPara;
        }
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();
        if (declList instanceof ZDeclList) {
            for (int j = 0; j < ((ZDeclList) declList).size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    Expr axParaExpr = constDecl.getExpr();
                    Expr visitedAxParaExpr = (Expr) axParaExpr.accept(this);
                    constDecl.setExpr(visitedAxParaExpr);
                    if (visitedAxParaExpr instanceof SchExpr) {
                        axPara.setBox(Box.SchBox);
                    }
                }
            }
        }

        unfoldedAxParas.put(axParaName, axPara);
        return axPara;
    }


    public SchExpr visitExpr(Expr expr) {
        System.out.println("ERROR! Expression not supported: ");
        System.out.println("\tExpression: " + SpecUtils.termToLatex(expr));
        System.out.println("\tClass: " + SpecUtils.termToLatex(expr));
        return null;
    }


    public SchExpr visitSchExpr(SchExpr schExpr) {
        ZSchText zSchText = schExpr.getZSchText();
        ZDeclList zDeclList = zSchText.getZDeclList();
        Pred pred = zSchText.getPred();
        int declListSize = zDeclList.size();
        for (int j = 0; j < declListSize; j++) {
            Decl decl = zDeclList.get(j);
            if (decl instanceof InclDecl) {
                Expr expr = ((InclDecl) decl).getExpr();
                String includedSchemeName = "";
                DeclDecoration declDecoration = DeclDecoration.NOT_PRIMED;
                boolean isXi = false;

                if (expr instanceof RefExpr) {
                    String refExprName = ((RefExpr) expr).getName().toString();
                    int firstCharCode = (int) refExprName.charAt(0);
                    if (firstCharCode == 916) {
                        //El esquema a expandir es un delta
                        includedSchemeName = refExprName.substring(1);
                        declDecoration = DeclDecoration.BOTH;
                        isXi = false;
                    } else if (firstCharCode == 926) {
                        //El esquema a expandir es un Xi
                        includedSchemeName = refExprName.substring(1);
                        declDecoration = DeclDecoration.BOTH;
                        isXi = true;
                    } else {
                        //El esquema a expandir no está primado, no es un delta ni un Xi
                        includedSchemeName = refExprName;
                        declDecoration = DeclDecoration.NOT_PRIMED;
                        isXi = false;
                    }
                } else if (expr instanceof DecorExpr) {
                    //El esquema a expandir esta primado
                    //ZLive zLive = new ZLive();
                    //TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
                    //System.out.println(textUI.printTerm(expr, Markup.LATEX));
                    includedSchemeName = ((RefExpr) ((DecorExpr) expr).getExpr()).getName().toString();
                    declDecoration = DeclDecoration.PRIMED;
                    isXi = false;
                }

                //Buscamos el esquema a expandir
                AxPara axParaToIncl = SpecUtils.axParaSearch(includedSchemeName, zParaList);
                //System.out.println("Incluido: "+nameToSearch);
                if (axParaToIncl == null) {
                    System.out.println("Could not find scheme:" + includedSchemeName);
                    return null;
                } else {
                    // If there is not an unfolded version of the referred 
                    // scheme previously calculated, then a copy of this is
                    // unfolded now
                    AxPara unfoldedAxPara = unfoldedAxParas.get(includedSchemeName);
                    SchExpr unfoldedAxParaSchExpr = null;
                    if (unfoldedAxPara == null) {
                        AxPara cloneAxPara = (AxPara) axParaToIncl.accept(new CZTCloner());
                        unfoldedAxPara = (AxPara) cloneAxPara.accept(this);
                    }

                    unfoldedAxParaSchExpr = SpecUtils.getAxParaSchExpr(unfoldedAxPara);

                    ZSchText zSchText2 = unfoldedAxParaSchExpr.getZSchText();
                    ZDeclList zDeclList2 = zSchText2.getZDeclList();
                    Pred pred2 = zSchText2.getPred();

                    AbstractRepository<String> varNameRep =
                            SpecUtils.getVarNames(unfoldedAxParaSchExpr);
                    PrimeVarsMaker primeVisitor = new PrimeVarsMaker(varNameRep);


                    boolean isSelOpOrSelPred = false;
                    AbstractIterator<String> opNamesIt =
                            opNames.createIterator();
                    while (opNamesIt.hasNext()) {
                        String opName = opNamesIt.next();
                        if (opName.equals(includedSchemeName)) {
                            isSelOpOrSelPred = true;
                        }
                    }

                    AbstractIterator<String> schPredNamesIt =
                            schPredNames.createIterator();
                    while (schPredNamesIt.hasNext()) {
                        String schPredName = schPredNamesIt.next();
                        if (schPredName.equals(includedSchemeName)) {
                            isSelOpOrSelPred = true;
                        }
                    }

                    // The scheme will be unfolded only if it was
                    // not selected as an operation nor as a
                    // predicate
                    if (!isSelOpOrSelPred) {

                        if (zDeclList2 instanceof ZDeclList) {

                            int deltaSize = 0;
                            switch (declDecoration) {
                                case NOT_PRIMED:
                                    // The scheme to unfold is not primed
                                    /*Insertamos las declaraciones de la ZDeclList declList2 en la posicion j de
                                    la ZDeclList declList y aumentamos deltaSize en el numero de declaraciones
                                    insertadas
                                     */

                                    zDeclList.remove(j);
                                    deltaSize += SpecUtils.insertZDeclList(zDeclList,
                                            zDeclList2, j);
                                    if (pred2 != null) {
                                        zSchText.setPred(SpecUtils.andPreds(pred, pred2));
                                    }
                                    break;


                                case PRIMED:
                                    // El schema a expandir está primado
				/*Insertamos las declaraciones de la ZDeclList declList2 en la posicion j de
                                    la ZDeclList declList (primandolas previamente) y aumentamos deltaSize en el
                                    numero de declaraciones insertadas
                                     */
                                    zDeclList.remove(j);
                                    deltaSize += SpecUtils.insertZDeclList(zDeclList,
                                            (ZDeclList) zDeclList2.accept(primeVisitor), j);
                                    if (pred2 != null) {
                                        zSchText.setPred(SpecUtils.andPreds(pred, (Pred) pred2.accept(primeVisitor)));
                                    }
                                    break;
                                case BOTH:
                                    //El esquema a expandir es un delta o un Xi, es decir,
                                    //incluye variables primadas y no primadas
								/*Insertamos las declaraciones de la ZDeclList declList2 en la posicion j de
                                    la ZDeclList declList  (primandolas previamente) y aumentamos deltaSize en el numero de declaraciones
                                    insertadas
                                     */
                                    zDeclList.remove(j);
                                    deltaSize += SpecUtils.insertZDeclList(zDeclList,
                                            (ZDeclList) zDeclList2.accept(primeVisitor), j);
                                    /*Insertamos las declaraciones de la ZDeclList declList2 en la posicion j de
                                    la ZDeclList declList y aumentamos deltaSize en el
                                    numero de declaraciones insertadas
                                     */
                                    deltaSize += SpecUtils.insertZDeclList(zDeclList,
                                            zDeclList2, j);

                                    if (pred2 != null) {
                                        zSchText.setPred(SpecUtils.andPreds(SpecUtils.andPreds(pred, pred2),
                                                (Pred) pred2.accept(primeVisitor)));
                                    }

                                    if (isXi) {
                                        // El esquema a expandir es un Xi, por lo que se agregan la conjunción de las igualdades
                                        // entre las variables del esquema a expandir y sus correspondientes variables primadas
                                        AbstractIterator<String> it = varNameRep.createIterator();
                                        while (it.hasNext()) {
                                            zSchText.setPred(SpecUtils.andPreds(zSchText.getPred(),
                                                    SpecUtils.createUnchangedPred(it.next())));
                                        }
                                    }

                                    break;
                            }


                            j = j + deltaSize - 1;
                            declListSize = declListSize + deltaSize - 1;

                        }
                    }
                }
            }
        }
        zSchText.setPred((Pred) pred.accept(this));
        return schExpr;
    }


    public SchExpr visitAndExpr(AndExpr andExpr) {
        Expr leftExpr = andExpr.getLeftExpr();
        Expr rightExpr = andExpr.getRightExpr();

        SchExpr visitedLeftExpr = (SchExpr) leftExpr.accept(this);
        SchExpr visitedRightExpr = (SchExpr) rightExpr.accept(this);

        SpecUtils.makeDeclListUnion(visitedLeftExpr, visitedRightExpr);
        ZSchText leftZSchText = visitedLeftExpr.getZSchText();
        ZSchText rightZSchText = visitedRightExpr.getZSchText();
        Pred leftPred = leftZSchText.getPred();
        Pred rightPred = rightZSchText.getPred();

        Pred pred = null;
        if (leftPred == null) {
            pred = rightPred;
        } else if (rightPred == null) {
            pred = leftPred;
        } else if (leftPred != null && rightPred != null) {
            AndPred andPred = (new ZFactoryImpl()).createAndPred();
            andPred.setLeftPred(leftPred);
            andPred.setRightPred(rightPred);
            pred = andPred;
        }

        leftZSchText.setPred(pred);
        return visitedLeftExpr;
    }



    public SchExpr visitOrExpr(OrExpr orExpr) {
        Expr leftExpr = orExpr.getLeftExpr();
        Expr rightExpr = orExpr.getRightExpr();

        SchExpr visitedLeftExpr = (SchExpr) leftExpr.accept(this);
        SchExpr visitedRightExpr = (SchExpr) rightExpr.accept(this);

        SpecUtils.makeDeclListUnion(visitedLeftExpr, visitedRightExpr);
        ZSchText leftZSchText = visitedLeftExpr.getZSchText();
        ZSchText rightZSchText = visitedRightExpr.getZSchText();
        Pred leftPred = leftZSchText.getPred();
        Pred rightPred = rightZSchText.getPred();

        Pred pred = null;
        if (leftPred == null) {
            pred = rightPred;
        } else if (rightPred == null) {
            pred = leftPred;
        } else if (leftPred != null && rightPred != null) {
            OrPred orPred = (new ZFactoryImpl()).createOrPred();
            orPred.setLeftPred(leftPred);
            orPred.setRightPred(rightPred);
            pred = orPred;
        }

        leftZSchText.setPred(pred);
        return visitedLeftExpr;
    }




    public SchExpr visitRefExpr(RefExpr refExpr) {
        SchExpr newSchExpr = null;
        DeclDecoration declDecoration = DeclDecoration.NOT_PRIMED;
        boolean isXi = false;

        String refExprName = refExpr.getName().toString();
        int firstCharCode = (int) refExprName.charAt(0);
        if (firstCharCode == 916) {
            //El esquema a expandir es un delta
            refExprName = refExprName.substring(1);
            declDecoration = DeclDecoration.BOTH;
            isXi = false;
        } else if (firstCharCode == 926) {
            //El esquema a expandir es un Xi
            refExprName = refExprName.substring(1);
            declDecoration = DeclDecoration.BOTH;
            isXi = true;
        } else {
            //El esquema a expandir no está primado, no es un delta ni un Xi
            declDecoration = DeclDecoration.NOT_PRIMED;
            isXi = false;
        }

        //Buscamos el esquema a expandir
        AxPara refAxPara = SpecUtils.axParaSearch(refExprName, zParaList);
        //System.out.println("Incluido: " + nameToSearch);
        if (refAxPara == null) {
            System.out.println("Could not find scheme:" + refExprName);
            return null;
        } else {
            // If there is not an previously calculated unfolded version of
            //the referred scheme , then a copy of this is unfolded now
            AxPara unfoldedAxPara = unfoldedAxParas.get(refExprName);
            SchExpr unfoldedAxParaSchExpr = null;
            if (unfoldedAxPara == null) {
                AxPara cloneAxPara = (AxPara) refAxPara.accept(new CZTCloner());
                unfoldedAxPara = (AxPara) cloneAxPara.accept(this);
            }

            unfoldedAxParaSchExpr = SpecUtils.getAxParaSchExpr(unfoldedAxPara);


            AbstractRepository<String> varNameRep =
                    SpecUtils.getVarNames(unfoldedAxParaSchExpr);
            PrimeVarsMaker primeVisitor = new PrimeVarsMaker(varNameRep);

            boolean isSelOpOrSelPred = false;
            AbstractIterator<String> opNamesIt = opNames.createIterator();
            while (opNamesIt.hasNext()) {
                String opName = opNamesIt.next();
                if (opName.equals(refExprName)) {
                    isSelOpOrSelPred = true;
                }
            }

            AbstractIterator<String> schPredNamesIt =
                    schPredNames.createIterator();
            while (schPredNamesIt.hasNext()) {
                String schPredName = schPredNamesIt.next();
                if (schPredName.equals(refExprName)) {
                    isSelOpOrSelPred = true;
                }
            }


            switch (declDecoration) {
                case NOT_PRIMED:
                    // The scheme to unfold is not primed

                    // The scheme will be unfolded only if it was
                    // not selected as an operation nor as a
                    // predicate

                    if (!isSelOpOrSelPred) {
                        newSchExpr = unfoldedAxParaSchExpr;
                    } else {
                        ZFactory zFactory = new ZFactoryImpl();
                        List<Decl> auxDeclList = new ArrayList<Decl>();
                        InclDecl inclDecl = zFactory.createInclDecl(refExpr);
                        auxDeclList.add(inclDecl);
                        ZDeclList newZDeclList =
                                zFactory.createZDeclList(auxDeclList);
                        ZSchText newSchText =
                                zFactory.createZSchText(newZDeclList, null);
                        newSchExpr = zFactory.createSchExpr(newSchText);
                    }
                    break;

                case BOTH:
                    //El esquema a expandir es un delta o un Xi, es decir,
                    //incluye variables primadas y no primadas
                    if (!isSelOpOrSelPred) {
                        ZSchText unfoldedAxParaZSchText =
                                unfoldedAxParaSchExpr.getZSchText();

                        ZSchText newSchText = (ZSchText) unfoldedAxParaZSchText.accept(new CZTCloner());
                        ZDeclList unfoldedAxParaZDeclList = unfoldedAxParaZSchText.getZDeclList();
                        Pred unfoldedAxParaPred = unfoldedAxParaZSchText.getPred();
                        SpecUtils.insertZDeclList(unfoldedAxParaZDeclList,
                                (ZDeclList) unfoldedAxParaZDeclList.accept(primeVisitor), unfoldedAxParaZDeclList.size());
                        if (unfoldedAxParaPred != null) {
                            unfoldedAxParaZSchText.setPred(SpecUtils.andPreds(
                                    unfoldedAxParaPred,
                                    (Pred) unfoldedAxParaPred.accept(primeVisitor)));
                        }
                        if (isXi) {
                            // El esquema a expandir es un Xi, por lo que se agregan la conjunción de las igualdades
                            // entre las variables del esquema a expandir y sus correspondientes variables primadas
                            AbstractIterator<String> it = varNameRep.createIterator();
                            while (it.hasNext()) {
                                unfoldedAxParaZSchText.setPred(SpecUtils.andPreds(
                                        unfoldedAxParaZSchText.getPred(),
                                        SpecUtils.createUnchangedPred(it.next())));
                            }
                        }
                        newSchExpr = (new ZFactoryImpl()).createSchExpr(newSchText);
                    } else {
                        ZFactory zFactory = new ZFactoryImpl();
                        List<Decl> auxDeclList = new ArrayList<Decl>();
                        InclDecl inclDecl = zFactory.createInclDecl(refExpr);
                        auxDeclList.add(inclDecl);
                        ZDeclList newZDeclList =
                                zFactory.createZDeclList(auxDeclList);
                        ZSchText newSchText =
                                zFactory.createZSchText(newZDeclList, null);
                        newSchExpr = zFactory.createSchExpr(newSchText);
                    }

                    break;
            }
        }
        return newSchExpr;
    }



    public Term visitIffExpr(IffExpr iffExpr) {
        Expr leftExpr = iffExpr.getLeftExpr();
        Expr rightExpr = iffExpr.getRightExpr();

        SchExpr visitedLeftExpr = (SchExpr) leftExpr.accept(this);
        SchExpr visitedRightExpr = (SchExpr) rightExpr.accept(this);

        SpecUtils.makeDeclListUnion(visitedLeftExpr, visitedRightExpr);
        ZSchText leftZSchText = visitedLeftExpr.getZSchText();
        ZSchText rightZSchText = visitedRightExpr.getZSchText();
        Pred leftPred = leftZSchText.getPred();
        Pred rightPred = rightZSchText.getPred();

        Pred pred = null;

        if (leftPred == null) {
            pred = rightPred;
        } else if (rightPred == null) {
            pred = leftPred;
        } else if (leftPred != null && rightPred != null) {
            IffPred iffPred = (new ZFactoryImpl()).createIffPred();
            iffPred.setLeftPred(leftPred);
            iffPred.setRightPred(rightPred);
            pred = iffPred;
        }

        leftZSchText.setPred(pred);
        return visitedLeftExpr;
    }




    public SchExpr visitImpliesExpr(ImpliesExpr impliesExpr) {
        Expr leftExpr = impliesExpr.getLeftExpr();
        Expr rightExpr = impliesExpr.getRightExpr();

        SchExpr visitedLeftExpr = (SchExpr) leftExpr.accept(this);
        SchExpr visitedRightExpr = (SchExpr) rightExpr.accept(this);

        SpecUtils.makeDeclListUnion(visitedLeftExpr, visitedRightExpr);
        ZSchText leftZSchText = visitedLeftExpr.getZSchText();
        ZSchText rightZSchText = visitedRightExpr.getZSchText();
        Pred leftPred = leftZSchText.getPred();
        Pred rightPred = rightZSchText.getPred();

        Pred pred = null;
        if (leftPred == null) {
            pred = rightPred;
        } else if (rightPred == null) {
            pred = leftPred;
        } else if (leftPred != null && rightPred != null) {
            ImpliesPred impliesPred = (new ZFactoryImpl()).createImpliesPred();
            impliesPred.setLeftPred(leftPred);
            impliesPred.setRightPred(rightPred);
            pred = impliesPred;
        }

        leftZSchText.setPred(pred);
        return visitedLeftExpr;
    }



    public Pred visitPred2(Pred2 pred2) {
        Pred leftPred = pred2.getLeftPred();
        Pred rightPred = pred2.getRightPred();
        pred2.setLeftPred((Pred) leftPred.accept(this));
        pred2.setRightPred((Pred) rightPred.accept(this));
        return pred2;
    }



    public Pred visitExprPred(ExprPred exprPred) {
        Expr expr = exprPred.getExpr();
        Pred newPred = exprPred;
        if (expr instanceof RefExpr) {

            RefExpr refExpr = (RefExpr) expr;
            String refExprName = refExpr.getName().toString();

            boolean isSelOpOrSelPred = false;
            AbstractIterator<String> opNamesIt = opNames.createIterator();
            while (opNamesIt.hasNext()) {
                String opName = opNamesIt.next();
                if (opName.equals(refExprName)) {
                    isSelOpOrSelPred = true;
                }
            }

            AbstractIterator<String> schPredNamesIt =
                    schPredNames.createIterator();
            while (schPredNamesIt.hasNext()) {
                String schPredName = schPredNamesIt.next();
                if (schPredName.equals(refExprName)) {
                    isSelOpOrSelPred = true;
                }
            }


            // The predicate is unfolded only if the schema reference refers
            // to a schema that is not selected as an operation nor as a
            // predicate
            if(!isSelOpOrSelPred){
                SchExpr schExpr = (SchExpr) refExpr.accept(this);
                ZSchText zSchText = schExpr.getZSchText();
                newPred = zSchText.getPred();
            }
        }
        return newPred;
    }
}
