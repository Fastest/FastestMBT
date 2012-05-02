package common.z.czt;

import java.util.*;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.impl.*;

public class CztPrinter {

    public static void main(String[] args) {
        SectionManager manager = new SectionManager();
        if (args.length != 1) {
            System.out.println("Ingresar nombre de archivo!!!!");
            return;
        }
        FileSource source = new FileSource(args[0]);
        manager.put(new Key(source.getName(), Source.class), source);
        try {
            Spec spec = (Spec) manager.get(new Key(source.getName(), Spec.class));
            //Spec spec2 = SchemeUnfolder.unfoldSpec(spec);


            List<? extends ErrorAnn> errors =
                    TypeCheckUtils.typecheck(spec, manager, true);
            if (errors.size() > 0) {
                System.out.println("Specification has not been "
                        + "loaded because it has type errors.");
                for (int i = 0; i < errors.size(); i++) {
                    String errorStr = errors.get(i).toString();
                    System.out.println(errorStr + "\n");
                }
                return;
            }

            printSpec(spec);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void printSpec(Spec spec) {
        System.out.println("Spec Version: " + spec.getVersion());

        for (Sect sect : spec.getSect()) {
            System.out.print("Sect ->" + sect);
            if (sect instanceof ZSect) {
                printZSect((ZSect) sect, 0);
            } else {
                System.out.println();
            }
        }
    }

    public static void printZSect(ZSect zSect, int nroTab) {
        System.out.println(getStrTab(nroTab) + zSect.getName());
        List<Parent> parentList = zSect.getParent();
        int parentSize = parentList.size();
        System.out.println(getStrTab(nroTab) + "Nro de parents: " + parentSize);
        for (int i = 0; i < parentList.size(); i++) {
            System.out.println(getStrTab(nroTab) + parentList.get(i).getWord());
        }

        ParaList paraList = zSect.getParaList();
        if (paraList instanceof ZParaList) {
            ZParaList zParaList = (ZParaList) paraList;
            for (int i = 0; i < zParaList.size(); i++) {
                printPara(zParaList.get(i), nroTab + 1, i);
            }
        }
    }

    public static void printPara(Para para, int nroTab, int nroPara) {
        String strTab = getStrTab(nroTab);
        if (para instanceof AxPara) {
            AxPara axPara = (AxPara) para;

            /*
            ZFactory zFactory = new ZFactoryImpl();
            List<Parent> parentList = new ArrayList<Parent>();
            parentList.add(zFactory.createParent("standard_toolkit"));
            List<Para> paraList = new ArrayList<Para>();
            paraList.add(axPara);
            ZParaList zParaList = zFactory.createZParaList(paraList);
            ZSect zSect = zFactory.createZSect("Specification", parentList, zParaList);
            List<ZSect> sectList = new ArrayList<ZSect>();
            sectList.add(zSect);
            Spec spec = (new ZFactoryImpl()).createSpec(sectList,"2.1");

            SectionManager manager = new SectionManager();
            manager.put(new Key("spec", Spec.class), spec);

            StringWriter out = new StringWriter();
            PrintUtils.printLatex(spec, out, manager);
            System.out.println(out.toString());




             */

            printAxPara(axPara, nroTab, nroPara);

//		try{
/*			StringWriter out = new StringWriter();
            SectionManager manager = new SectionManager();
            manager.put(new Key("tclass", AxPara.class), (AxPara)para);
            PrintUtils.printLatex(para, out, manager);
            System.out.println(out.toString());
             */
            /*
            ZFactory zFactory = new ZFactoryImpl();

            List<Parent> parentList = new ArrayList<Parent>();
            parentList.add(zFactory.createParent("section"));
            List<Para> paraList = new ArrayList<Para>();
            paraList.add(para);
            ZParaList zParaList = zFactory.createZParaList(paraList);
            ZSect zSect = zFactory.createZSect("section", parentList, zParaList);
            List<ZSect> sectList = new ArrayList<ZSect>();
            sectList.add(zSect);
            Spec spec = (new ZFactoryImpl()).createSpec(sectList,"1.0");



            SectionManager manager = new SectionManager();
            manager.put(new Key("1.0", Spec.class), spec);
            LatexString latex = (LatexString)
            manager.get(new Key("1.0", LatexString.class));
            System.out.println(latex);
             */

            /*
            StringWriter out = new StringWriter();
            PrintUtils.printLatex(axPara, out, manager, zSect.getName());
            System.out.println(out.toString());



            }
            catch(Exception e){
            e.printStackTrace();
            }
             */
            /*


            SectionManager manager = new SectionManager();
            manager.reset();
            manager.put(new Key("tclass", AxPara.class), (AxPara)para);
            LatexString latex = (LatexString)
            manager.get(new Key("tclass", LatexString.class));
            System.out.println(latex);
            }
            catch(Exception e){
            e.printStackTrace();
            }*/

        } else if (para instanceof ConjPara) {
            System.out.println(nroPara + strTab + "ConjPara->");
        } else if (para instanceof FreePara) {
            printFreePara((FreePara) para, nroTab, nroPara);
        } else if (para instanceof GivenPara) {
            printGivenPara((GivenPara) para, nroTab, nroPara);
        } else if (para instanceof LatexMarkupPara) {
            System.out.println(nroPara + strTab + "LatexMarkupPara->");
        } else if (para instanceof NarrPara) {
            System.out.println(nroPara + strTab + "NarrPara->");
        } else if (para instanceof OptempPara) {
            System.out.println(nroPara + strTab + "OptempPara->");
        }
        System.out.println();

    }

    public static void printAxPara(AxPara axPara, int nroTab, int nroPara) {

        /*
        if(OpSchemeImpl.isOpScheme(axPara)){
        OpScheme opScheme = new OpSchemeImpl(axPara);
        TClass tClass = VISGen.generateVIS(opScheme);
        axPara = tClass.getMyAxPara();
        }
         */


        /*		OpScheme opScheme = new OpSchemeImpl(axPara);
        TClass tClass = VISGen.generateVIS(opScheme);
        tClass.setSchName(SpecUtils.getAxParaName(opScheme.getMyAxPara()) + "_VIS");

        DNFTactic fndTactic = new DNFTactic();
        fndTactic.setOriginalOp(opScheme);
        List<TClass> tClassList = fndTactic.applyTactic(tClass);
        for(int l=0; l<tClassList.size(); l++){
        axPara = tClassList.get(l).getMyAxPara();
         */

        /*
        if(OpSchemeImpl.isOpScheme(axPara)){
        OpScheme opScheme = new OpSchemeImpl((AxPara) axPara.accept(new CloneVisitor()));

        AbstractRepository<Tactic> tacticRep = new ConcreteRepository<Tactic>();
        DNFTactic fndTactic = new DNFTactic();
        fndTactic.setOriginalOp(opScheme);
        tacticRep.addElement(fndTactic);
        // 			System.out.println("posta1");
        TClassNode rootNode = new IterativeTTreeStrategy().generateTTree(opScheme, tacticRep);

        AbstractRepository<? extends TTreeNode> children = rootNode.getChildren();
        AbstractIterator<? extends TTreeNode> childrenIt = children.createIterator();

        while(childrenIt.hasNext()){
        TTreeNode child = childrenIt.next();
        if(child instanceof TClassNode){
        TClass tClass = ((TClassNode)child).getValue();
        axPara = tClass.getMyAxPara();
         */
        System.out.print(nroPara + getStrTab(nroTab) + "AxPara->");
        System.out.print(axPara.getBox().toString());

        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();


        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = zDeclList.get(j);
                if (decl instanceof ConstDecl) {
                    ConstDecl constDecl = (ConstDecl) decl;
                    ZName zName = constDecl.getZName();
                    String schemaName = zName.getWord();
                    System.out.print("\t********* " + schemaName + " ");
                    int len = 90 - schemaName.length();
                    for (int i = 0; i < len; i++) {
                        System.out.print("*");
                    }
                    System.out.println();

                    // Obtenemos e imprimimos el contenido del esquema
                    Expr expr = constDecl.getExpr();
                    printExpr(expr, nroTab + 2);
                } else if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    System.out.print("\t");
                    for (int i = 0; i < 101; i++) {
                        System.out.print("*");
                    }
                    System.out.print("axDef");
                    System.out.println();
                    printVarDecl(varDecl, nroTab + 2);
                    System.out.print(getStrTab(nroTab + 2));
                    for (int i = 0; i < 101; i++) {
                        System.out.print("-");
                    }
                    System.out.println();
                    printPred(zSchText.getPred(), nroTab + 2);
                }
            }
        }
        // Imprimimos la linea inferior del esquema
        System.out.print(getStrTab(nroTab + 2));
        for (int i = 0; i < 101; i++) {
            System.out.print("*");
        }
        System.out.println();
    }
    /*	}
    }
    }
     */

    public static void printGivenPara(GivenPara givenPara, int nroTab, int nroPara) {
        System.out.print(nroPara + getStrTab(nroTab) + "GivenPara-> [");
        ZNameList zNameList = givenPara.getZNameList();
        int i = 0;
        for (i = 0; i < zNameList.size() - 1; i++) {
            System.out.print(zNameList.get(i) + ",");
        }
        System.out.print(zNameList.get(i) + "]");
    }

    public static void printFreePara(FreePara freePara, int nroTab, int nroPara) {
        System.out.print(nroPara + getStrTab(nroTab) + "FreePara->");
        FreetypeList freetypeList = freePara.getFreetypeList();
        if (freetypeList instanceof ZFreetypeListImpl) {
            System.out.print("[");
            ZFreetypeListImpl zFreetypeListImpl = (ZFreetypeListImpl) freetypeList;
            for (int i = 0; i < zFreetypeListImpl.size(); i++) {
                if (i != 0) {
                    System.out.print(", ");
                }
                Freetype freetype = zFreetypeListImpl.get(i);
                System.out.print(freetype.getName() + "::= ");
                BranchList branchList = freetype.getBranchList();
                if (branchList instanceof ZBranchListImpl) {
                    ZBranchListImpl zBranchListImpl = (ZBranchListImpl) branchList;
                    for (int j = 0; j < zBranchListImpl.size(); j++) {
                        if (j != 0) {
                            System.out.print(" | ");
                        }
                        Branch branch = zBranchListImpl.get(j);
// 						printExpr(branch.getExpr(),0);
                        System.out.print(branch.getName());
                    }
                }
            }
            System.out.print("]");
        }
    }

    public static void printExpr(Expr expr, int nroTab) {

        if (expr instanceof SchExpr) {
            ZSchText zSchText = ((SchExpr) expr).getZSchText();
            printZSchText(zSchText, nroTab, 101);
        } else if (expr instanceof AndExpr) {
            printAndExpr((AndExpr) expr, nroTab);
        } else if (expr instanceof OrExpr) {
            printOrExpr((OrExpr) expr, nroTab);
        } else if (expr instanceof RefExpr) {
            printRefExpr((RefExpr) expr, nroTab, false);
        } else if (expr instanceof PowerExpr) {
            printPowerExpr((PowerExpr) expr, nroTab);
        } else if (expr instanceof SetExpr) {
            printSetExpr((SetExpr) expr, nroTab);
        } else if (expr instanceof TupleExpr) {
            printTupleExpr((TupleExpr) expr, nroTab);
        } else if (expr instanceof ApplExpr) {
            printApplExpr((ApplExpr) expr, nroTab);
        } else if (expr instanceof SetCompExpr) {
            printSetCompExpr((SetCompExpr) expr, nroTab);
        } else if (expr instanceof NumExpr) {
            printNumExpr((NumExpr) expr, nroTab);
        } else if (expr instanceof ProdExpr) {
            printProdExpr((ProdExpr) expr, nroTab);
        } else {

            System.out.println(getStrTab(nroTab) + "*Expr* " + expr);
        }
    }

    public static void printZSchText(ZSchText zSchText, int nroTab, int sepLineLen) {
        DeclList declList = zSchText.getDeclList();
        if(declList == null){
           System.out.print(getStrTab(nroTab));
           System.out.print("null");
        }
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = zDeclList.get(j);
                printDecl(decl, nroTab);
            }
        }
        Pred pred = zSchText.getPred();
        System.out.print(getStrTab(nroTab));
        for (int i = 0; i < sepLineLen; i++) {
            System.out.print("-");
        }
        System.out.println();
        printPred(pred, nroTab);
    }

    public static void printPred(Pred pred, int nroTab) {
        if (pred instanceof AndPred) {
            AndPred andPred = (AndPred) pred;
            Pred pred1 = andPred.getLeftPred();
            Pred pred2 = andPred.getRightPred();
            printPred(pred1, nroTab + 1);
            System.out.println(getStrTab(nroTab) + andPred.getAnd());
            printPred(pred2, nroTab + 1);
        } else if (pred instanceof OrPred) {
            OrPred orPred = (OrPred) pred;
            Pred pred1 = orPred.getLeftPred();
            Pred pred2 = orPred.getRightPred();
            printPred(pred1, nroTab + 1);
            System.out.println(getStrTab(nroTab) + "*OrPred*");
            printPred(pred2, nroTab + 1);
        } else if (pred instanceof MemPred) {
            MemPred memPred = (MemPred) pred;
            Expr expr1 = memPred.getLeftExpr();
            printExpr(expr1, nroTab + 1);
            boolean mixfix = memPred.getMixfix();
            System.out.println(getStrTab(nroTab) + "*MemPred* (Mixfix = " + mixfix + ")");
            Expr expr2 = memPred.getRightExpr();
            printExpr(expr2, nroTab + 1);
        } else if (pred instanceof NegPred) {
            NegPred negPred = (NegPred) pred;
            System.out.println(getStrTab(nroTab) + "*NegPred* ");
            printPred(negPred.getPred(), nroTab + 1);

        } else if (pred instanceof ImpliesPred) {
            ImpliesPred impliesPred = (ImpliesPred) pred;
            Pred pred1 = impliesPred.getLeftPred();
            Pred pred2 = impliesPred.getRightPred();
            printPred(pred1, nroTab + 1);
            System.out.println(getStrTab(nroTab) + "*ImpliesPred*");
            printPred(pred2, nroTab + 1);
        } else if (pred instanceof IffPred) {
            IffPred iffPred = (IffPred) pred;
            Pred pred1 = iffPred.getLeftPred();
            Pred pred2 = iffPred.getRightPred();
            printPred(pred1, nroTab + 1);
            System.out.println(getStrTab(nroTab) + "*IffPred*");
            printPred(pred2, nroTab + 1);
        } else if (pred instanceof ExprPred) {
            ExprPred exprPred = (ExprPred) pred;
            Expr expr = exprPred.getExpr();
            System.out.println(getStrTab(nroTab) + "*ExprPred*");
            printExpr(expr, nroTab + 1);
        } else if (pred == null) {
            System.out.println(getStrTab(nroTab) + "Predicado null!!!!");
        } else {
            System.out.println("Predicado sin procesar");
        }
    }

    public static void printDecl(Decl decl, int nroTab) {
        if (decl instanceof ConstDecl) {
            printConstDecl((ConstDecl) decl, nroTab);
        } else if (decl instanceof InclDecl) {
            printInclDecl((InclDecl) decl, nroTab);
        } else if (decl instanceof VarDecl) {
            printVarDecl((VarDecl) decl, nroTab);
        }
    }

    public static void printAndExpr(AndExpr andExpr, int nroTab) {
        Expr expr1 = andExpr.getLeftExpr();
        Expr expr2 = andExpr.getRightExpr();
        printExpr(expr1, nroTab + 1);
        System.out.println(getStrTab(nroTab) + "*AndExpr*");
        printExpr(expr2, nroTab + 1);
    }

    public static void printOrExpr(OrExpr orExpr, int nroTab) {
        Expr expr1 = orExpr.getLeftExpr();
        Expr expr2 = orExpr.getRightExpr();
        printExpr(expr1, nroTab + 1);
        System.out.println(getStrTab(nroTab) + "*OrExpr*");
        printExpr(expr2, nroTab + 1);
    }

    public static void printRefExpr(RefExpr refExpr, int nroTab, boolean noTabbed) {
        int nroTab2;
        if (noTabbed) {
            nroTab2 = 0;
        } else {
            nroTab2 = nroTab;
        }



// Para ver el tipo normalizado

/*
        List<Object> anns = refExpr.getAnns();
        for (int i = 0; i < anns.size(); i++) {
            Object ann = anns.get(i);
            if (ann instanceof TypeAnn) {
                TypeAnn typeAnn = (TypeAnn) ann;
                System.out.println("tipo: " + typeAnn.getType());
            }

        }
*/



        ZName zName = refExpr.getZName();

        System.out.print(getStrTab(nroTab2) + "*RefExpr* " + zName.getWord()
                + " " + "List Strokes:");
        printZStrokeList(zName.getZStrokeList());
        /*
        String str = refExpr.getName().toString();
        for(int i=0; i<str.length(); i++)
        System.out.println((int) str.charAt(i));
         */
        ZExprListImpl zExprListImpl = (ZExprListImpl) refExpr.getZExprList();
        for (int i = 0; i < zExprListImpl.size(); i++) {
            printExpr(zExprListImpl.get(i), nroTab + 2);
        }

    }

    public static void printDecorExpr(DecorExpr decorExpr, int nroTab) {
        System.out.println(getStrTab(nroTab) + "*DecorExpr* " + ((RefExpr) decorExpr.getExpr()).getName()
                + "" + decorExpr.getStroke());
    }

    public static void printZStrokeList(ZStrokeList zStrokeList) {
        System.out.print("[");
        for (int i = 0; i < zStrokeList.size(); i++) {
            System.out.print(zStrokeList.get(i).toString());
        }
        System.out.println("]");
    }

    public static void printPowerExpr(PowerExpr powerExpr, int nroTab) {
        System.out.println(getStrTab(nroTab) + "*PowerExpr* ");
        printExpr(powerExpr.getExpr(), nroTab + 1);
    }

    public static void printSetExpr(SetExpr setExpr, int nroTab) {
        System.out.println(getStrTab(nroTab) + "*SetExpr* ");
        ZExprListImpl zExprListImpl = (ZExprListImpl) setExpr.getZExprList();
        for (int i = 0; i < zExprListImpl.size(); i++) {
            printExpr(zExprListImpl.get(i), nroTab + 1);
        }

    }

    public static void printTupleExpr(TupleExpr tupleExpr, int nroTab) {
        System.out.println(getStrTab(nroTab) + "*TupleExpr* ");
        ZExprListImpl zExprListImpl = (ZExprListImpl) tupleExpr.getZExprList();
        for (int i = 0; i < zExprListImpl.size(); i++) {
            printExpr(zExprListImpl.get(i), nroTab + 1);
        }

    }

    public static void printApplExpr(ApplExpr applExpr, int nroTab) {
        double number = Math.random();
        Expr expr1 = applExpr.getLeftExpr();
        Expr expr2 = applExpr.getRightExpr();
        printExpr(expr1, nroTab + 1);
        boolean mixfix = applExpr.getMixfix();
        System.out.println(getStrTab(nroTab) + "*ApplExpr* (Mixfix = " + mixfix + ")");
        printExpr(expr2, nroTab + 1);

    }

    public static void printSetCompExpr(SetCompExpr setCompExpr, int nroTab) {
        System.out.println(getStrTab(nroTab) + "*SetCompExpr*");
        ZSchText zSchText = setCompExpr.getZSchText();
        System.out.println(getStrTab(nroTab + 1) + "*SchText del SetCompExpr*");
        int sepLineLen = 101 - 6 * (nroTab + 1);
        printZSchText(zSchText, nroTab + 2, sepLineLen);
        Expr expr = setCompExpr.getExpr();
        System.out.println(getStrTab(nroTab + 1) + "*Expr del SetCompExpr*");
        printExpr(expr, nroTab + 2);
    }

    public static void printNumExpr(NumExpr numExpr, int nroTab) {
        System.out.println(getStrTab(nroTab) + "*NumExpr* " + numExpr.getValue());
    }

    public static void printProdExpr(ProdExpr prodExpr, int nroTab) {
        System.out.println(getStrTab(nroTab) + "*ProdExpr* ");
        ZExprListImpl zExprListImpl = (ZExprListImpl) prodExpr.getZExprList();
        for (int i = 0; i < zExprListImpl.size(); i++) {
            printExpr(zExprListImpl.get(i), nroTab + 1);
        }
    }

    public static void printConstDecl(ConstDecl constDecl, int nroTab) {
        System.out.print(getStrTab(nroTab) + "* ConstDecl ->");
        System.out.println(((ZName) constDecl.getName()).getWord());
    }

    public static void printInclDecl(InclDecl inclDecl, int nroTab) {
        System.out.print(getStrTab(nroTab) + "* InclDecl -> ");
        Expr expr = inclDecl.getExpr();
        if (expr instanceof RefExpr) {
            printRefExpr((RefExpr) expr, nroTab + 2, true);
        }
        if (expr instanceof DecorExpr) {
            printDecorExpr((DecorExpr) expr, 0);
        }
    }

    public static void printVarDecl(VarDecl varDecl, int nroTab) {
        System.out.print(getStrTab(nroTab) + "* VarDecl ->");
        NameList nameList = varDecl.getName();
        if (nameList instanceof ZNameListImpl) {
            ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;

            System.out.print(nameList + ": ");
        }
        Expr expr = varDecl.getExpr();

        if (expr instanceof PowerExpr) {
            printPowerExpr((PowerExpr) expr, 0);
        } else if (expr instanceof RefExpr) {
            printRefExpr((RefExpr) expr, nroTab + 2, true);
        } else if (expr instanceof ApplExpr) {
            System.out.println();
            printApplExpr((ApplExpr) expr, nroTab + 1);
        } else {
            System.out.println(getStrTab(nroTab + 1) + expr);
        }
    }

    private static String getStrTab(int n) {
        String strTab = "";
        for (int i = 0; i < n; i++) {
            strTab += "\t";
        }
        return strTab;
    }
}
