package common.z;

import java.util.*;
import java.math.*;
import java.io.*;

import net.sourceforge.czt.z.impl.ZNameListImpl;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.animation.eval.ZLive;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import common.repository.ConcreteRepository;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.ContainsTermVerifier;
import common.z.czt.visitors.ImpliesPredRemover;
import common.z.czt.visitors.AndOrPredDistributor;
import common.z.czt.visitors.NegPredDistributor;
import common.z.czt.visitors.AndPredClausesExtractor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.parser.z.ParseUtils;
import common.z.czt.UniqueZLive;
import common.z.czt.visitors.AndPredClausesExtractor2;
import common.z.czt.visitors.OrPredClausesExtractor;
import common.z.czt.visitors.PredInOrVerifier;
import net.sourceforge.czt.z.ast.*;

/**
 * Provides general  utilities related to Z constructions.
 * @author Pablo Rodriguez Monetti
 */
public class SpecUtils {

    public static ConstDecl getConstDecl(AxPara axPara) {
        ConstDecl constDecl = null;
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();
        if (declList instanceof ZDeclList) {
            int declListSize = ((ZDeclList) declList).size();
            for (int j = 0; j < declListSize; j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof ConstDecl) {
                    constDecl = (ConstDecl) decl;
                }
            }
        }
        return constDecl;
    }

    /**
     * Returns the name of the specified AxPara object, unless the AxPara is an axiomatic 
     * definition.
     * @param the name of the specified AxPara object, unless the AxPara is an axiomatic 
     * definition, in which case it returns null.
     * @return
     */
    public static String getAxParaName(AxPara axPara) {
        String axParaName = "";
        ConstDecl constDecl = getConstDecl(axPara);
        if (constDecl != null) {
            Name name = constDecl.getName();
            if (name instanceof ZName) {
                axParaName = (((ZName) name).getWord());
            }
        }
        return axParaName;
    }

    /**
     * Sets the name of the specified AxPara object unless the AxPara is an axiomatic 
     * definition.
     * @param axPara
     * @param newName
     */
    public static void setAxParaName(AxPara axPara, String newName) {
        ConstDecl constDecl = getConstDecl(axPara);
        if (constDecl != null) {
            Name name = constDecl.getName();
            if (name instanceof ZName) {
                ((ZName) name).setWord(newName);
            }
        }
    }

    /**
     * Returns the SchExpr of the specified AxPara object.
     * @param axPara
     * @return the SchExpr of the specified AxPara object.
     */
    public static SchExpr getAxParaSchExpr(AxPara axPara) {
        SchExpr schExpr = null;
        ConstDecl constDecl = getConstDecl(axPara);
        if (constDecl != null) {
            Expr expr = constDecl.getExpr();
            if (expr instanceof SchExpr) {
                schExpr = (SchExpr) expr;
            }
        }
        return schExpr;
    }



    /**
     * Returns the declaration list of the specified AxPara object.
     * @param axPara
     * @return the declaration list of the specified AxPara object.
     */
    public static DeclList getAxParaListOfDecl(AxPara axPara) {
        DeclList declList = null;
        SchExpr schExpr = getAxParaSchExpr(axPara);
        if (schExpr != null) {
            ZSchText zSchText = schExpr.getZSchText();
            if (zSchText != null) {
                declList = zSchText.getDeclList();
            }
        }
        return declList;
    }

    /**
     * Sets the declaration list of the specified AxPara object with the specified declaration
     * list.
     * @param axPara
     * @param declList
     */
    public static void setAxParaListOfDecl(AxPara axPara, DeclList declList) {
        SchExpr schExpr = getAxParaSchExpr(axPara);
        if (schExpr != null) {
            ZSchText zSchText = schExpr.getZSchText();
            if (zSchText != null) {
                zSchText.setDeclList(declList);
            }
        }
    }

    /**
     * Obtains from a specification a list with the free types paragraphs
     * @param spec A specification
     */
    public static List<FreePara> getFreeTypes(Spec spec) {
        List<FreePara> list = new ArrayList<FreePara>();
        for (Sect sect : spec.getSect()) {
            if (sect instanceof ZSect) {
                ParaList paraList = ((ZSect) sect).getParaList();
                if (paraList instanceof ZParaList) {
                    for (int i = 0; i < ((ZParaList) paraList).size(); i++) {
                        Para para = ((ZParaList) paraList).get(i);
                        if (para instanceof FreePara) {
                            list.add((FreePara) para);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static AxPara createAxPara(DeclList declList, Pred pred) {
        ZLive zLive = UniqueZLive.getInstance();
        String fooSchema = "\\begin{schema}{Aux}\n";
        //fooSchema += "a: \\nat"
        fooSchema += "\\end{schema}";
        AxPara axPara = null;
        try {
            Term parsedTerm = ParseUtils.parse(new StringSource(fooSchema), zLive.getSectionManager());
            Spec spec = (Spec) parsedTerm;
            for (Sect sect : spec.getSect()) {
                if (sect instanceof ZSect) {
                    ParaList paraList = ((ZSect) sect).getParaList();
                    if (paraList instanceof ZParaList) {
                        for (int i = 0; i < ((ZParaList) paraList).size(); i++) {
                            Para para = ((ZParaList) paraList).get(i);
                            if (para instanceof AxPara) {
                                axPara = (AxPara) para;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        if (axPara != null) {
            setAxParaListOfDecl(axPara, declList);
            setAxParaPred(axPara, pred);
        }
        return axPara;
    }

    /**
     * Returns the predicate of the specified AxPara object.
     * @param axPara
     * @return the predicate of the specified AxPara object.
     */
    public static Pred getAxParaPred(AxPara axPara) {
        SchExpr schExpr = getAxParaSchExpr(axPara);
        if (schExpr != null) {
            return schExpr.getZSchText().getPred();
        } else {
            return null;
        }
    }

    /**
     * Sets the predicate of the specified AxPara object.
     * @param axPara
     * @param pred
     */
    public static void setAxParaPred(AxPara axPara, Pred pred) {
        ZSchText schText = getAxParaSchExpr(axPara).getZSchText();
        schText.setPred(pred);
    }



    public static AbstractRepository<String> getVarNames(SchExpr schExpr) {
        AbstractRepository<String> varNamesRep = new ConcreteRepository<String>();
        DeclList declList = schExpr.getZSchText().getZDeclList();
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                NameList nameList;
                if (decl instanceof VarDecl) {
                    nameList = ((VarDecl) decl).getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        for (int k = 0; k < zNameListImpl.size(); k++) {
                            varNamesRep.addElement(zNameListImpl.get(k).toString());
                        }
                    }
                }
            }
        }
        return varNamesRep;
    }

    /**
     * Returns an AbstractRepository&lt;String&gt; with the names of the included 
     * schemas.
     * @param axPara
     * @return an AbstractRepository&lt;String&gt; with the names of the included
     * schemas.
     */
    public static AbstractRepository<String> getInclDeclNames(AxPara axPara) {
        AbstractRepository<String> inclNames = new ConcreteRepository<String>();
        ArrayList<String> namesNoRep = new ArrayList<String>();
        ZSchText zSchText = axPara.getZSchText();
        DeclList declList = zSchText.getDeclList();
        String name = "";
        //System.out.println(((ConstDecl)axPara.getZSchText().getZDeclList().get(0)).getZName());

        //System.out.println("Antes de indagar!!!");
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = zDeclList.get(j);
                //System.out.println("Entra 2");
                if (decl instanceof ConstDecl) {
                    //System.out.println("Entra 3");
                    ConstDecl constDecl = (ConstDecl) decl;
                    Expr expr = constDecl.getExpr();
                    if (expr instanceof SchExpr) {
                        //System.out.println("Entra 4");
                        SchExpr schExpr = (SchExpr) expr;
                        ZSchText zSchText2 = schExpr.getZSchText();
                        DeclList declList2 = zSchText2.getDeclList();
                        if (declList2 instanceof ZDeclList) {
                            //System.out.println("Entra 5");
                            ZDeclList zDeclList2 = (ZDeclList) declList2;
                            for (int k = 0; k < zDeclList2.size(); k++) {
                                Decl decl2 = zDeclList2.get(k);
                                //System.out.println("Entra 6");

                                if (decl2 instanceof InclDecl) {
                                    //System.out.println("Llega al InclDecl");
                                    Expr expr2 = ((InclDecl) decl2).getExpr();
                                    if (expr2 instanceof RefExpr) {
                                        name = ((RefExpr) expr2).getName().toString();
                                        int firstCharCode = (int) name.charAt(0);
                                        if (firstCharCode == 916) {
                                            //El esquema a expandir es un delta
                                            name = name.substring(1);
                                            //System.out.println("Es un Delta "+name);
                                        } else if (firstCharCode == 926) {
                                            //El esquema a expandir es un Xi
                                            name = name.substring(1);
                                            //System.out.println("Es un Xi "+name);
                                        } else {
                                            //El esquema a expandir no está primado, no es un delta ni un Xi
                                            //System.out.println("Ninguno de los anteriores!!!"+name);
                                        }
                                    } else if (expr instanceof DecorExpr) {
                                        name = ((RefExpr) ((DecorExpr) expr2).getExpr()).getName().toString();
                                        //System.out.println("The name is: "+name);
                                    }
                                    //else
                                    //System.out.println("No le cabe ninguna!!!"+name);
                                }
                                if (!namesNoRep.contains(name)) {
                                    namesNoRep.add(name);
                                    inclNames.addElement(name);
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("Despues de indagar!!!");
        return inclNames;
    }

    public static AbstractRepository<String> getInclDeclNames(SchExpr schExpr) {
        AbstractRepository<String> inclNames = new ConcreteRepository<String>();
        ArrayList<String> namesNoRep = new ArrayList<String>();
        String name = "";

        ZSchText zSchText2 = schExpr.getZSchText();
        DeclList declList2 = zSchText2.getDeclList();
        if (declList2 instanceof ZDeclList) {
            //System.out.println("Entra 5");
            ZDeclList zDeclList2 = (ZDeclList) declList2;
            for (int k = 0; k < zDeclList2.size(); k++) {
                Decl decl2 = zDeclList2.get(k);
                //System.out.println("Entra 6");

                if (decl2 instanceof InclDecl) {
                    //System.out.println("Llega al InclDecl");
                    Expr expr2 = ((InclDecl) decl2).getExpr();
                    if (expr2 instanceof RefExpr) {
                        name = ((RefExpr) expr2).getName().toString();
                        int firstCharCode = (int) name.charAt(0);
                        if (firstCharCode == 916) {
                            //El esquema a expandir es un delta
                            name = name.substring(1);
                            //System.out.println("Es un Delta "+name);
                        } else if (firstCharCode == 926) {
                            //El esquema a expandir es un Xi
                            name = name.substring(1);
                            //System.out.println("Es un Xi "+name);
                        } else {
                            //El esquema a expandir no está primado, no es un delta ni un Xi
                            //System.out.println("Ninguno de los anteriores!!!"+name);
                        }
                    }
                }
                if (!namesNoRep.contains(name)) {
                    namesNoRep.add(name);
                    inclNames.addElement(name);
                }
            }
        }

        //System.out.println("Despues de indagar!!!");
        return inclNames;
    }

    /**
     * Returns the schema that results from the conjunction of the specified two schemas.
     * @param schExpr1
     * @param schExpr2
     * @return the schema that results from the conjunction of the specified two schemas.
     */
    public static SchExpr andSchExprs(SchExpr schExpr1, SchExpr schExpr2) {
        makeDeclListUnion(schExpr1, schExpr2);
        ZSchText zSchText1 = schExpr1.getZSchText();
        ZSchText zSchText2 = schExpr2.getZSchText();
        zSchText1.setPred(andPreds(zSchText1.getPred(), zSchText2.getPred()));
        return schExpr1;
    }


    /**
     * Returns the schema that results from the merge of the list of declarations of the 
     * specified two schemas, modifying the first of them, schExpr1.
     * @param schExpr1
     * @param schExpr2
     * @return the schema that results from the merge of the list of declarations of the 
     * specified two schemas.
     */
    public static SchExpr makeDeclListUnion(SchExpr schExpr1, SchExpr schExpr2) {
        DeclList declList1 = schExpr1.getZSchText().getDeclList();
        DeclList declList2 = schExpr2.getZSchText().getDeclList();
        if (declList1 instanceof ZDeclList && declList2 instanceof ZDeclList) {
            ZDeclList zDeclList1 = (ZDeclList) declList1;
            ZDeclList zDeclList2 = (ZDeclList) declList2;
            insertZDeclList(zDeclList1, zDeclList2, zDeclList1.size());

        }
        return schExpr1;
    }

    /**
     * Returns the AxPara with the specified name from the specified ZParaList, if any.
     * @param name
     * @param zParaList
     * @return the AxPara with the specified name from the specified ZParaList, if any.
     */
    public static AxPara axParaSearch(String name, ZParaList zParaList) {
        AxPara axPara = null;
        Para para;
        for (int i = 0; i < zParaList.size(); i++) {
            para = zParaList.get(i);
            if (para instanceof AxPara && getAxParaName((AxPara) para).equals(name)) {
                axPara = ((AxPara) para);
            }
        }
        return axPara;
    }

    /**
     * Inserts in the index position of the declaration list zDeclList1 those declarations  
     * from zDeclList2 whose names do not appear in the first list and returns the number
     * of inserted declarations.
     * @param zDeclList1
     * @param zDeclList2
     * @param index
     * @return
     */
    public static int insertZDeclList(ZDeclList zDeclList1, ZDeclList zDeclList2, int index) {
        int declListSize2 = zDeclList2.size();
        int numOfDeclInserted = 0;
        for (int i = 0; i < declListSize2; i++) {
            Decl decl = zDeclList2.get(i);
            if (insertDecl(zDeclList1, decl, index + numOfDeclInserted)) {
                numOfDeclInserted++;
            }
        }
        return numOfDeclInserted;
    }

    /**
     * Inserts, in the index position of the list of declarations zDeclList, the
     * declaration decl. If decl is a VarDecl, the declaration of those
     * variables whose names appear in some declaration of variables from
     * zDeclList will not be inserted. Returns true if the insertion was
     * successful, ie. if the declaration was inserted with at least one
     * variable. Returns false if the declaration was not inserted, due to the
     * occurrence of each of its variable in some  declaration of zDeclList.
     * The verification that two variables, one from zDeclList and
     * the other from decl, with the same name has the same type is not performed.
     * @param zDeclList
     * @param varDecl
     * @param index
     * @return
     */
    public static boolean insertDecl(ZDeclList zDeclList, Decl decl, int index) {
        boolean zDeclListChanged = false;
        CZTCloner cloneVisitor = new CZTCloner();
        if (decl instanceof VarDecl) {
            VarDecl varDecl = (VarDecl) decl.accept(cloneVisitor);
            NameList nameList = varDecl.getName();
            if (nameList instanceof ZNameListImpl) {
                ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                for (int k = 0; k < zNameListImpl.size(); k++) {
                    if (containsName(zDeclList, zNameListImpl.get(k))) {
                        zNameListImpl.remove(k);
                    } else {
                        zDeclListChanged = true;
                    }
                }
                if (zDeclListChanged) {
                    zDeclList.add(index, varDecl);
                }
            }
        } else {
            zDeclList.add(index, decl);
            zDeclListChanged = true;
        }

        return zDeclListChanged;
    }

    /**
     * Determines whether the list of declarations zDeclList contains a variable declaration
     * where the specified varName appears.
     * @param zDeclList
     * @param varName
     * @return true, if the list of declarations zDeclList contains a variable declaration
     * where the specified varName appears and false, otherwise.
     */
    public static boolean containsName(ZDeclList zDeclList, Name varName) {
        for (int i = 0; i < zDeclList.size(); i++) {
            Decl decl = zDeclList.get(i);
            if (decl instanceof VarDecl) {
                NameList nameList = ((VarDecl) decl).getName();
                if (nameList instanceof ZNameListImpl) {
                    ZNameList zNameList =  (ZNameListImpl) nameList;
                    for (int k = 0; k < zNameList.size(); k++) {
                        String kNameStr = (zNameList.get(k)).toString();
                        String varNameStr = varName.toString();
                        if (kNameStr.equals(varNameStr)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the predicate that results from the conjunction of the specified two
     * predicates.
     * @param pred1
     * @param pred2
     * @return the predicate that results from the conjunction of the specified two
     * predicates.
     */
    public static Pred andPreds(Pred pred1, Pred pred2) {
        if (pred1 == null) {
            return pred2;
        } else if (pred2 == null) {
            return pred1;
        }
        // If pred1 and pred2 are not equal to null
        AndPred newPred = (new ZFactoryImpl()).createAndPred();
        newPred.setLeftPred(pred1);
        newPred.setRightPred(pred2);
        newPred.setAnd(And.NL);
        return newPred;
    }

    /**
     * Returns the predicate that results from the disjunction of the specified two predicates.
     * @param pred1
     * @param pred2
     * @return the predicate that results from the disjunction of the specified two predicates.
     */
    public static Pred orPreds(Pred pred1, Pred pred2) {
        if (pred1 == null) {
            return pred2;
        } else if (pred2 == null) {
            return pred1;
        }
        // If pred1 and pred2 are not equal to null
        OrPred newOrPred = (new ZFactoryImpl()).createOrPred();
        newOrPred.setLeftPred(pred1);
        newOrPred.setRightPred(pred2);
        return newOrPred;
    }

    /**
     * Returns all the variables that appears in the predicate
     * @param pred
     * @param declList
     */
    public static List<RefExpr> getVariablesInPred(Pred pred, DeclList declList) {
        ZFactory zFactory = new ZFactoryImpl();
        List<RefExpr> list = new ArrayList<RefExpr>();
        if (declList instanceof ZDeclList) {
            ZDeclList zDeclList = (ZDeclList) declList;
            for (int j = 0; j < zDeclList.size(); j++) {
                Decl decl = ((ZDeclList) declList).get(j);
                if (decl instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) decl;
                    NameList nameList = varDecl.getName();
                    if (nameList instanceof ZNameListImpl) {
                        ZNameListImpl zNameListImpl = (ZNameListImpl) nameList;
                        for (int k = 0; k < zNameListImpl.size(); k++) {
                            RefExpr varRefExpr = zFactory.createRefExpr(zNameListImpl.get(k), zFactory.createZExprList(), false, false);
                            if (pred.accept(new ContainsTermVerifier(varRefExpr))) {
                                list.add(varRefExpr);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * Returns the predicate that results from the equality of the form a' = a, being a the
     * name specified by the specified String argument. 
     * @param varName
     * @return the predicate that results from the equality of the form a' = a, being a the
     * name specified by the specified String argument. 
     */
    public static Pred createUnchangedPred(String varName) {

        ZFactory zFactory = new ZFactoryImpl();

        // We create the left RefExpr

        ZStrokeList zStrokeList = zFactory.createZStrokeList();
        zStrokeList.add(zFactory.createNextStroke());
        ZName leftZName = zFactory.createZName(varName, zStrokeList, "left");
        ZExprList leftZExprList = zFactory.createZExprList();
        RefExpr leftRefExpr = zFactory.createRefExpr(leftZName, leftZExprList, false, false);

        // We create the right RefExpr
        ZName rightZName = zFactory.createZName(varName, zFactory.createZStrokeList(), "right");
        ZExprList rightZExprList = zFactory.createZExprList();
        RefExpr rightRefExpr = zFactory.createRefExpr(rightZName, rightZExprList, false, false);

        //We create the SetExpr that contains the right RefExpr
        ZExprList setZExprList = zFactory.createZExprList();
        setZExprList.add(0, rightRefExpr);
        SetExpr setExpr = zFactory.createSetExpr(setZExprList);

        //We create the MemPred that the method will return
        ZExprList memPredExprList = zFactory.createZExprList();
        memPredExprList.add(0, leftRefExpr);
        memPredExprList.add(1, setExpr);
        MemPred newMemPred = zFactory.createMemPred(memPredExprList, true);

        return newMemPred;
    }

    /**
     * Returns an alternative ZDeclList that results from expand the original ZDeclList
     * @param zList
     * @return the expanded ZDeclList 
     */
    public static ZDeclList expandDeclList(ZDeclList zList) {
        ZFactory zFactory = new ZFactoryImpl();
        for (int i = 0; i < zList.size(); i++) {
            Decl auxDecl = zList.get(i);
            if (auxDecl instanceof VarDecl) {
                VarDecl auxVarDecl = (VarDecl) auxDecl;
                ZNameList zNameList = auxVarDecl.getZNameList();
                if (zNameList.size() > 1) {
                    Expr type = auxVarDecl.getExpr();
                    for (int j = 0; j < zNameList.size(); j++) {
                        ZNameList newList = zFactory.createZNameList();
                        newList.add(zNameList.get(j));
                        VarDecl newVarDecl = zFactory.createVarDecl(newList, type);
                        zList.add(newVarDecl);
                    }
                    Decl elim = zList.remove(i);
                    i--;
                }
            }
        }
        return zList;
    }

    /**
     * Returns a boolean value that indicates if an expressions is a variable in the
     * given ZDeclList
     * @param zDeclList
     * @param expr
     * @return boolean
     */
    public static boolean isVariable(ZDeclList zDeclList, Expr expr) {
        String strExpr = termToLatex(expr);
        if (strExpr.endsWith("?") || strExpr.endsWith("!")) {
            strExpr = strExpr.substring(0, strExpr.length() - 1);
        }
        boolean isVar = false;
        for (int i = 0; i < zDeclList.size() && !isVar; i++) {
            Decl decl = zDeclList.get(i);
            VarDecl varDecl = null;
            if (decl instanceof VarDecl) {
                varDecl = (VarDecl) decl;
                ZNameList zNameList = varDecl.getZNameList();
                for (int j = 0; j < zNameList.size() && !isVar; j++) {
                    ZName zName = (ZName) zNameList.get(j);
                    if (zName.getWord().equals(strExpr)) {
                        isVar = true;
                    }
                }
            }
        }
        return isVar;
    }

    public static Expr getVarExpr(ZDeclList zDeclList, Expr expr) {
        String strExpr = termToLatex(expr);
        if (strExpr.endsWith("?")) {
            strExpr = strExpr.substring(0, strExpr.length() - 1);
        }
        Expr varExpr = null;
        for (int i = 0; i < zDeclList.size() && varExpr == null; i++) {
            Decl decl = zDeclList.get(i);
            VarDecl varDecl = null;
            if (decl instanceof VarDecl) {
                varDecl = (VarDecl) decl;
                ZNameList zNameList = varDecl.getZNameList();
                for (int j = 0; j < zNameList.size() && varExpr == null; j++) {
                    ZName zName = (ZName) zNameList.get(j);
                    if (zName.getWord().equals(strExpr)) {
                        varExpr = varDecl.getExpr();
                    }
                }
            }
        }
        return varExpr;
    }

    /**
     * Returns a boolean value that indicates if an expressions is a constant. 
     * Equivalently, returns a boolean value that indicates if an expression is 
     * present in the given ZDeclList
     * @param zDeclList
     * @param expr
     * @return boolean
     */
    public static boolean isConstant(ZDeclList zDeclList, Expr expr) {
        String strExpr = termToLatex(expr);
        boolean isConst = true;
        for (int i = 0; i < zDeclList.size() && isConst; i++) {
            Decl decl = zDeclList.get(i);
            VarDecl varDecl = null;
            if (decl instanceof VarDecl) {
                varDecl = (VarDecl) decl;
                ZNameList zNameList = varDecl.getZNameList();
                for (int j = 0; j < zNameList.size() && isConst; j++) {
                    ZName zName = (ZName) zNameList.get(j);
                    String strZName = SpecUtils.termToLatex(zName);

// Condicion necesaria no suficiente Ej: ESPACIOprocsXX - ARREGLAR
                    if (strZName.equals(strExpr)) {
                        isConst = false;
                    } else if (strExpr.contains(strZName)) {
                        String replaced = strZName.replaceAll("\\?", "\\\\?");
                        Pattern regex = Pattern.compile("[ ~]*" + replaced + "[ ~]*");
                        Matcher matcher = regex.matcher(strExpr);
                        //System.out.println("Comparaa "+strZName+" con "+strExpr);
                        while (matcher.find() && isConst) {
                            String pattern = matcher.group();
                            //System.out.println("El pattern es:"+pattern);
                            if (!pattern.equals(strZName)) {
                                isConst = false;
                            }
                        }
                    }
                }
            }
        }
        return isConst;
    }

    /**
     * Returns the conjunction of equalities constructed from the pairs (var, value) which
     * are present in the specified Map. 
     * @param varExprMap
     * @return the conjunction of equalities constructed from the pairs (var, value) which
     * are present in the specified Map. 
     */
    public static Pred createAndPred(Map<RefExpr, Expr> varExprMap) {
        ZFactory zFactory = new ZFactoryImpl();
        Set<Map.Entry<RefExpr, Expr>> set = varExprMap.entrySet();
        Iterator<Map.Entry<RefExpr, Expr>> iterator = set.iterator();
        Pred pred = null;
        while (iterator.hasNext()) {
            Map.Entry<RefExpr, Expr> mapEntry = iterator.next();
            RefExpr refExpr = mapEntry.getKey();
            Expr expr = mapEntry.getValue();

            //We create the SetExpr that contains the right expr
            ZExprList setZExprList = zFactory.createZExprList();
            setZExprList.add(0, expr);
            SetExpr setExpr = zFactory.createSetExpr(setZExprList);

            //We create the MemPred that the method will return
            ZExprList memPredExprList = zFactory.createZExprList();
            memPredExprList.add(0, refExpr);
            memPredExprList.add(1, setExpr);
            MemPred memPred = zFactory.createMemPred(memPredExprList, true);

            if (pred != null) {
                pred = andPreds(pred, memPred);
            } else {
                pred = memPred;
            }
        }
        return pred;

    }

    /**
     * Returns the specified predicate in Disjunctive Normal Form.
     * @param pred
     * @return the specified predicate in Disjunctive Normal Form.
     */
    public static Pred getDNF(Pred pred) {
        Pred predClone = (Pred) pred.accept(new CZTCloner());
        //System.out.println("Antes de aplicar el implica:\n"+ termToLatex(predClone));
        predClone = predClone.accept(new ImpliesPredRemover());
        //System.out.println("Antes de aplicar el Neg:\n"+ termToLatex(predClone));
        predClone = predClone.accept(new NegPredDistributor());
        //System.out.println("Antes de aplicar el AndOr:\n"+ termToLatex(predClone));
        predClone = predClone.accept(new AndOrPredDistributor());
        //System.out.println("Antes de salir:\n"+ termToLatex(predClone));
        return predClone;
    }

    /**
     * Returns the RefExpr corresponding to an specification identifier
     * @param specID
     * @return the RefExpr related to this specID
     */
    public static RefExpr createVarRefExpr(String specID) {
        ZFactory zFactory = new ZFactoryImpl();
        ZExprList zExprList = zFactory.createZExprList();
        ZName varZName = null;
        ZStrokeList zStrokeList = zFactory.createZStrokeList();
        if (specID.endsWith("?")) {
            zStrokeList.add(zFactory.createInStroke());
            // We remove the ? character from the variable name
            specID = specID.substring(0, specID.length() - 1);
            varZName = zFactory.createZName(specID, zStrokeList, "neg");
        } else if (specID.endsWith("!")) {
            zStrokeList.add(zFactory.createOutStroke());
            // We remove the ! character from the variable name
            specID = specID.substring(0, specID.length() - 1);
            varZName = zFactory.createZName(specID, zStrokeList, "neg");
        } else if (specID.endsWith("'")) {
            zStrokeList.add(zFactory.createNextStroke());
            // We remove the ? character from the variable name
            specID = specID.substring(0, specID.length() - 1);
            varZName = zFactory.createZName(specID, zStrokeList, "neg");
        } else {
            varZName = zFactory.createZName(specID, zFactory.createZStrokeList(), "neg");
        }
        RefExpr varRefExpr = zFactory.createRefExpr(varZName, zExprList, false, false);
        return varRefExpr;
    }

    /**
     * Returns the String with the specified term printed in format Latex.
     * @param term
     * @return
     */
    public static String termToLatex(Term term) {
        StringWriter out = new StringWriter();
        ZLive zLive = UniqueZLive.getInstance();
        zLive.printTerm(new PrintWriter(out), term, zLive.getMarkup());
        return out.toString();
    }

    /**
     * Determines if the specified terms are equal.
     * @param term1
     * @param term2
     * @return true if both terms are equal and false, otherwise.
     */
    public static boolean areEqualTerms(Term term1, Term term2) {
        if (term1 == term2) {
            return true;
        }

        if (term1.getClass() != term2.getClass()) {
            return false;
        }

        Object[] array1 = term1.getChildren();
        Object[] array2 = term2.getChildren();


        if (array1.length != array2.length) {
            return false;
        }

        boolean areEqual = true;
        for (int i = 0; i < array1.length && areEqual; i++) {
            Object object1 = array1[i];
            Object object2 = array2[i];
            /*
            if(object1 != null && object2 != null){
            System.out.println("objeto 1: " + object1.getClass());
            System.out.println("objeto 2: " + object2.getClass());            
            }
             */

            if (object1 instanceof ZName && object2 instanceof ZName) {
                ZName zName1 = (ZName) object1;
                ZName zName2 = (ZName) object2;
                String string1 = zName1.toString();
                String string2 = zName2.toString();
                // We compare all of the fields of each ZName object,
                // except for the ID's because they are irrelevant for us.
                areEqual = string1.equals(string2);
                areEqual = areEqual && SpecUtils.areEqualTerms(zName1.getZStrokeList(), zName2.getZStrokeList());
                areEqual = areEqual && (zName1.getWord().equals(zName2.getWord()));

            } else if (object1 instanceof String && object2 instanceof String) {
                String string1 = object1.toString();
                String string2 = object2.toString();
                if (!string1.equals(string2)) {
                    //                  System.out.println("cadenas difererentes: " +string1 + " "+ string2 +  ", en i = " + i +
                    //                        ", de " + SpecUtils.termToLatex(term1) + " y " + SpecUtils.termToLatex(term2));
                    //    System.out.println("Setea a false 1");
                    areEqual = false;
                }
            } else if (object1 instanceof BigInteger && object2 instanceof BigInteger) {
                BigInteger bigInteger1 = (BigInteger) object1;
                BigInteger bigInteger2 = (BigInteger) object2;
                if (!bigInteger1.equals(bigInteger2)) {
                    // System.out.println("Setea a false 2");
                    areEqual = false;
                }
            } else if (object1 instanceof Term && object2 instanceof Term) {
                areEqual = areEqualTerms((Term) object1, (Term) object2);
            } else if (!(object1 instanceof Term) && !(object2 instanceof Term)); else {
                //System.out.println("Setea a false 3");
                areEqual = false;
                //   System.out.println("objetos difererentes: " +object1.getClass() + " "+ object2.getClass());
            }
        }
        //System.out.println("Sale por 3");
        return areEqual;
    }

    /**
     * Returns the RefExpr that represents integer numbers
     * @return the RefExpr that represents integer numbers
     */
    public static RefExpr getNumRefExpr() {
        ZFactory zFactory = new ZFactoryImpl();
        ZName zName = zFactory.createZName(UtilSymbols.integerSymbol(), zFactory.createZStrokeList(), "nat");
        ZExprList refZExprList = zFactory.createZExprList();
        return zFactory.createRefExpr(zName, refZExprList, false, false);
    }

    /**
     * Returns the SetExpr that represents empty set 
     * @return the SetExpr that represents integer numbers
     */
    public static SetExpr getEmptySetExpr() {
        ZFactory zFactory = new ZFactoryImpl();
        return zFactory.createSetExpr();
    }

    public static Pred getLessThanPred(RefExpr varNameRefExpr, Integer number) {
        ZFactory zFactory = new ZFactoryImpl();
        ZExprList innerZExprList = zFactory.createZExprList();
        innerZExprList.add(varNameRefExpr);
        innerZExprList.add(getNumberExpr(number));
        TupleExpr tupleExpr = zFactory.createTupleExpr(innerZExprList);
        ZName lessThanSymbolZName = zFactory.createZName(" _ < _ ",
                zFactory.createZStrokeList(), "neg");
        RefExpr lessThanSymbolRefExpr = zFactory.createRefExpr(lessThanSymbolZName,
                zFactory.createZExprList(), false, false);
        ZExprList outerZExprList = zFactory.createZExprList();
        outerZExprList.add(tupleExpr);
        outerZExprList.add(lessThanSymbolRefExpr);
        MemPred memPred = zFactory.createMemPred(outerZExprList, Boolean.TRUE);
        return memPred;
    }

    public static Pred getGreaterThanPred(RefExpr varNameRefExpr, Integer number) {
        ZFactory zFactory = new ZFactoryImpl();
        ZExprList innerZExprList = zFactory.createZExprList();
        innerZExprList.add(varNameRefExpr);
        innerZExprList.add(getNumberExpr(number));
        TupleExpr tupleExpr = zFactory.createTupleExpr(innerZExprList);
        ZName greaterThanSymbolZName = zFactory.createZName(" _ > _ ",
                zFactory.createZStrokeList(), "neg");
        RefExpr greaterThanSymbolRefExpr = zFactory.createRefExpr(greaterThanSymbolZName,
                zFactory.createZExprList(), false, false);
        ZExprList outerZExprList = zFactory.createZExprList();
        outerZExprList.add(tupleExpr);
        outerZExprList.add(greaterThanSymbolRefExpr);
        MemPred memPred = zFactory.createMemPred(outerZExprList, Boolean.TRUE);
        return memPred;
    }

    public static Pred getEqualityPred(RefExpr varNameRefExpr, Integer number) {
        ZFactory zFactory = new ZFactoryImpl();
        //We create the SetExpr that contains the right expr
        ZExprList setZExprList = zFactory.createZExprList();
        setZExprList.add(0, getNumberExpr(number));
        SetExpr setExpr = zFactory.createSetExpr(setZExprList);

        //We create the MemPred that will be added to the new test class
        ZExprList memPredExprList = zFactory.createZExprList();
        memPredExprList.add(0, varNameRefExpr);
        memPredExprList.add(1, setExpr);
        MemPred memPred = zFactory.createMemPred(memPredExprList, true);
        return memPred;
    }

    public static Expr getNumberExpr(Integer i) {
        ZFactory zFactory = new ZFactoryImpl();
        int value = i.intValue();
        ZNumeral zNumeral = zFactory.createZNumeral(BigInteger.valueOf(Math.abs(value)));
        NumExpr numExpr = zFactory.createNumExpr(zNumeral);
        Expr expr = null;
        if (value >= 0) {
            expr = numExpr;
        } else {
            ZName zName = zFactory.createZName("- _ ", zFactory.createZStrokeList(), "neg");
            RefExpr negRefExpr = zFactory.createRefExpr(zName, zFactory.createZExprList(), false, false);
            ZExprList zExprList = zFactory.createZExprList();
            zExprList.add(negRefExpr);
            zExprList.add(numExpr);
            expr = zFactory.createApplExpr(zExprList, true);
        }
        return expr;
    }

    /**
     * Gets the pairs (variable, value) that appears in the specified predicate,
     * assuming that the predicate is a conjunction.
     * @param pred
     * @return
     */
    public static Map<String, Expr> getAssignedValues(Pred pred) {
        Map<String, Expr> assignedValues = new HashMap<String, Expr>();
        if (pred == null) {
            return assignedValues;
        }
        AbstractRepository<Pred> predClauses = pred.accept(new AndPredClausesExtractor());
        AbstractIterator<Pred> predClausesIt = predClauses.createIterator();
        while (predClausesIt.hasNext()) {
            Pred auxPred = predClausesIt.next();
            if (auxPred instanceof MemPred) {
                MemPred memPred = (MemPred) auxPred;
                if (memPred.getMixfix()) {
                    Expr leftExpr = memPred.getLeftExpr();
                    Expr rightExpr = memPred.getRightExpr();
                    if (leftExpr instanceof RefExpr
                            && rightExpr instanceof SetExpr) {
                        RefExpr refExpr = (RefExpr) leftExpr;
                        SetExpr setExpr = (SetExpr) rightExpr;
                        ZExprList zExprList = setExpr.getZExprList();
                        if (zExprList.size() == 1) {
                            assignedValues.put(refExpr.getName().toString(), zExprList.get(0));
                        }

                    }
                }
            }
        }
        return assignedValues;
    }




  /**
     * Combine a list of predicates in one predicate that is the disjunction of
     * those predicates
     * @param dnfPreds a list of predicates
     * @return the disjunction of the predicates taken from the given list
     */
    public static Pred createOrPred(List<Pred> listPred) {
        Pred pred = null;
        if(!listPred.isEmpty())
            pred = listPred.get(0);
        for(int i=1; i< listPred.size(); i++) {
            Pred rightPred = listPred.get(i);
            OrPred orPred = (new ZFactoryImpl()).createOrPred();
            orPred.setLeftPred(pred);
            orPred.setRightPred(rightPred);
            pred = orPred;
        }
        return pred;
    }
    
    
  /**
     * Combine a list of predicates in one predicate that is the conjunction of
     * those predicates
     * @param dnfPreds a list of predicates
     * @return the conjunction of the predicates taken from the given list
     */
    public static Pred createAndPred(List<Pred> listPred) {
        Pred pred = null;
        if(!listPred.isEmpty())
            pred = listPred.get(0);
        for(int i=1; i< listPred.size(); i++) {
            Pred rightPred = listPred.get(i);
            AndPred andPred = (new ZFactoryImpl()).createAndPred();
            andPred.setLeftPred(pred);
            andPred.setRightPred(rightPred);
            andPred.setAnd(And.NL);
            pred = andPred;
        }
        return pred;

    }


    /**
     *
     * @param predList1
     * @param predList2
     * @return true if all the elements of predList1 are elements of predList2;
     * otherwise return false
     */
    public static boolean isListContainedInList(List<Pred> predList1, List<Pred> predList2){
        boolean allPredsInList = true;
        for(int i=0; i< predList1.size() && allPredsInList; i++){
            Pred pred1 = predList1.get(i);
            boolean predInList = false;
            for(int j=0; j<predList2.size() && !predInList; j++){
                Pred pred2 = predList2.get(j);
                if(SpecUtils.areEqualTerms(pred1, pred2)){
                    predInList = true;
                }
            }
            if(!predInList){
                allPredsInList = false;
            }
        }
        return allPredsInList;
    }


    /**
     * Creates a predicate in DNF making the disjunction
     * of two predicates in DNF, pred1 and pred2, removing
     * duplicated conjunctions of atomic predicates or
     * negations of atomic predicates.
     * @param pred1
     * @param pred2
     * @return
     */
    public static Pred createSimplifiedOrPred(Pred pred1, Pred pred2){
        OrPredClausesExtractor orPredClausesExtractor = new OrPredClausesExtractor();
        List<Pred> predList1 = pred1.accept(orPredClausesExtractor);
        List<Pred> predList2 = pred2.accept(orPredClausesExtractor);

        for(int i=0; i< predList1.size();i++){
            Pred iPred = predList1.get(i);
            for(int j=0; j< predList2.size(); j++){
                Pred jPred = predList2.get(j);
                if(SpecUtils.areEqualTerms(iPred,jPred)){
                    predList2.remove(j);
                    j--;
                }
            }
        }

        predList1.addAll(predList2);

        return SpecUtils.createOrPred(predList1);
    }

    /**
     * This methos applies the rules of conjunction
     * idempotence  (p1 /\ p1 = p1) and conjunction
     * absortion  (p1 /\ (p1 \/ p2) = p1).
     * @param andPred
     * @return
     */
    public static Pred simplifyAndPred(Pred pred){
        List<Pred> predList =
                pred.accept(new AndPredClausesExtractor2());

        for(int i=0; i<predList.size(); i++){
            Pred iPred = predList.get(i);
            boolean isRepeated = false;
            for(int j=0; j<i && !isRepeated; j++){
                Pred jPred = predList.get(j);
                if(SpecUtils.areEqualTerms(iPred, jPred) ||
                        (iPred.accept(new PredInOrVerifier(jPred)).booleanValue())){
                    predList.remove(i);
                    isRepeated = true;
                    i--;
                }
                else if(jPred.accept(new PredInOrVerifier(iPred)).booleanValue()){
                    predList.remove(j);
                    j--;
                    i--;
                }
            }
        }

        return SpecUtils.createAndPred(predList);

    }
}