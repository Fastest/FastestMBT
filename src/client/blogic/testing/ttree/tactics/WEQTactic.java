package client.blogic.testing.ttree.tactics;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.ZName;
import net.sourceforge.czt.z.ast.Para;
import net.sourceforge.czt.z.ast.ParaList;
import net.sourceforge.czt.z.ast.ZParaList;
import net.sourceforge.czt.z.ast.ZExprList;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.SetExpr;
import net.sourceforge.czt.z.ast.ZFactory;
import net.sourceforge.czt.z.ast.And;
import net.sourceforge.czt.z.ast.MemPred;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.ZSchText;
import net.sourceforge.czt.z.ast.ExistsPred;
import net.sourceforge.czt.z.ast.ForallPred;
import net.sourceforge.czt.z.ast.QntPred;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.NegPred;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.animation.eval.TextUI;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.impl.ZFactoryImpl;
import net.sourceforge.czt.session.CommandException;
import net.sourceforge.czt.session.Key;
import net.sourceforge.czt.session.Source;
import net.sourceforge.czt.session.FileSource;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.NarrPara;
import net.sourceforge.czt.z.ast.LatexMarkupPara;
import net.sourceforge.czt.z.ast.Sect;
import net.sourceforge.czt.z.ast.ZSect;
import net.sourceforge.czt.z.ast.SectTypeEnvAnn;
import net.sourceforge.czt.z.ast.TupleExpr;
import common.z.TClassImpl;
import common.z.czt.visitors.OrPredClausesExtractor;
import common.z.czt.visitors.CZTCloner;
import common.z.czt.visitors.CZTReplacer;
import common.z.SpecUtils;
import common.z.TClass;
import common.z.czt.UniqueZLive;
import common.z.UtilSymbols;
import common.z.czt.visitors.ContainsTermInPredVerifier;

/**
 * Implementation of the tactic Weak Existential Quantifier (WEQ).
 * @author Pablo Rodriguez Monetti
 */
public class WEQTactic extends AbstractTactic {

    private WEQTacticInfo WEQTacticInfo;

    /**
     * Creates new instances of WEQTactic.
     */
    public WEQTactic() {
        if (tClassNumbersMap == null) {
            tClassNumbersMap = new HashMap<String, Integer>();
        }
        description = "* WEQ (Weak Existential Quantifier). This tactic \n";
        description += "  generates new test classes from a test class that \n";
        description += "  has, in its predicate, either an existencial  \n";
        description += "  quantification or the negation of an universal  \n";
        description += "  quantification. The new test classes MIGHT NOT conform \n";
        description += "  a partition of the original test class. The tactic \n";
        description += "  takes as parameter the existencial quantification (or\n";
        description += "  the negation of the universal quantification) and a  \n";
        description += "  finite model for each bounded variable of the  \n";
        description += "  quantification. It is also possible to specify a  \n";
        description += "  finite model for the type of any of the bounded  \n";
        description += "  variables, in place of a finite model for each specific   \n";
        description += "  variable of that type.\n";
        description += "  Usage: addtactic <op_name> WEQ '\"'<quantification>'\"' \n";
        description += "       '\"'<bindings>'\"'\n";
        description += "  where quantification and bindings must be enclosed\n";
        description += "  between quotes. quantification must be a sequence of \n";
        description += "  assignations divided by ';' such that each assignation \n";
        description += "  is denoted by '==', has the name of a bounded variable \n";
        description += "  or a type of a bounded variable as left member and a\n";
        description += "  a set of values of the appropiate type as right member.\n";
        description += "  Example: addtactic Op WEQ \"\\exists i,j: \\nat @ i>2 \n";
        description += "  \\land j>3\" \"i==\\{1,2\\};j==\\{8,9,10\\}\" \n";
    }

    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param tClass
     * @return
     */
    public List<TClass> applyTactic(TClass tClass) {

        Pred mainPred = obtainMainPred();
        List<Pred> predList = obtainPredList(mainPred);
        return obtainTClassList(tClass, predList);
    }

    /**
     * Parses the parameters of this tactic.
     * @param args
     * @return
     */
    public boolean parseArgs(String args) {

        if (!(args.startsWith("\""))) {
            System.out.println("Invalid parameters.  Try 'help'.");
            return false;
        }


        final String parts[] = args.split("\"");


        if (parts.length != 4) {
            System.out.println("Invalid parameters.  Try 'help'.");
            return false;
        }

        String eqExprStr = parts[1].trim();
        String valuesStr = parts[3].trim();

        ZLive zLive = UniqueZLive.getInstance();
        TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));

        String fileName = "WEQTactic.temp";
        File tempFile = null;
        Term predParsed = null;

        try {
            predParsed = ParseUtils.parsePred(new StringSource(eqExprStr),
            zLive.getCurrentSection(), zLive.getSectionManager());
        } catch (Exception e) {
            System.out.println("The first argument of the WEQ tactic is not"
                    + " sintactically correct.");
            return false;
        }



        // We check if the input expression is contained in the predicate of
        // the operation
        AxPara opAxPara = (AxPara) originalOp.getMyAxPara();
        if (!opAxPara.accept(new ContainsTermInPredVerifier(predParsed, spec, controller)).booleanValue()) {
            System.out.println("The first argument of the SEQ tactic is not"
                    + " correct: it must be contained in the predicate "
                    + "of the operation's schema.");

            return false;
        }


        Pred pred = null;
        // We check whether the term is an existencial quantification or
        // the negation of an universal quantification
        Map<String, String> boundedVars = new HashMap<String, String>();
        if (predParsed instanceof ExistsPred) {
            boundedVars = extractBoundedVars(eqExprStr, "\\exists");
            pred = (Pred) predParsed;
        } else if (predParsed instanceof NegPred) {
            NegPred negPred = (NegPred) predParsed;
            pred = negPred.getPred();
            if (pred instanceof ForallPred) {
                boundedVars = extractBoundedVars(eqExprStr, "\\forall");
            } else {
                System.out.println("The first argument of the WEQ tactic is not"
                        + " correct: it must be an existencial quantification "
                        + "or the negation of an"
                        + " universal quantification.");
                return false;

            }

        } else {
            System.out.println("The first argument of the WEQ tactic is not "
                    + "correct: it must be an existencial quantification or"
                    + " the negation of an universal quantification.");
            return false;
        }

        try {

            String valueParts[] = valuesStr.split(";");

            Map<String, String> valuesStrMap = new HashMap<String, String>();
            for (int i = 0; i < valueParts.length; i++) {
                valueParts[i] = valueParts[i].trim();
                String subParts[] = valueParts[i].split("==");
                if (subParts.length != 2) {
                    System.out.println("The second argument of the WEQ tactic is not "
                            + "correct: it must be a WEQuence (separed by ';') of "
                            + "assignations (denoted by '==') of"
                            + " sets of values to variables or types appearing in the"
                            + " declaration of the specified quantification.");
                    return false;
                }
                String varNameOrTypeStr = subParts[0];
                String valueStr = subParts[1];



                valuesStrMap.put(varNameOrTypeStr, valueStr);

                // We check if the variable or type appears in the quantification
                // declaration
                String typeStr = boundedVars.get(varNameOrTypeStr);
                boolean found = false;
                if (typeStr == null) {
                    Set<Map.Entry<String, String>> entrySet = boundedVars.entrySet();
                    Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
                    while (iterator.hasNext() && !found) {
                        Map.Entry<String, String> entry = iterator.next();
                        typeStr = entry.getValue();
                        if (varNameOrTypeStr.equals(typeStr)) {
                            found = true;
                        }
                    }
                } else {
                    found = true;
                }

                if (!found) {
                    System.out.println("The second argument of the WEQ tactic is not "
                            + "correct: '"
                            + varNameOrTypeStr
                            + "' is not a variable nor a type appearing in the"
                            + " declaration of the specified quantification.");
                    return false;
                }

                // We check if the variable or type has a value or correct type
                // assigned to it
                tempFile = new File(fileName);
                PrintWriter printer = new PrintWriter(new FileWriter(tempFile));
                for (Sect sect : spec.getSect()) {
                    if (sect instanceof ZSect) {
                        ZSect zSect = (ZSect) sect;
                        ParaList paraList = zSect.getParaList();
                        if (paraList instanceof ZParaList) {
                            ZParaList zParaList = (ZParaList) paraList;
                            for (int j = 0; j < zParaList.size(); j++) {
                                Para para = zParaList.get(j);
                                if (!(para instanceof NarrPara
                                        || para instanceof LatexMarkupPara)) {
                                    printer.println("\n" + SpecUtils.termToLatex(para));
                                }
                            }
                        }
                    }
                }

                int n = (int) (Math.random() * 100);
                printer.println("\\begin{axdef}");
                String varDeclStr = "aux" + n + ":" + typeStr;
                printer.println(varDeclStr);
                printer.println("\\where");
                String equalityStr = "aux" + n + " \\in " + valueStr;
                printer.println(equalityStr);
                printer.println("\\end{axdef}");
                printer.flush();

                Source source = new FileSource(fileName);
                manager.put(new Key(fileName, Source.class), source);

                spec = (Spec) manager.get(new Key(fileName, Spec.class));

                for (Sect sect : spec.getSect()) {
                    if (sect instanceof ZSect) {
                        ZSect zSect = (ZSect) sect;
                        // We typecheck the specification here
                        String sectionName = zSect.getName();
                        SectTypeEnvAnn envAnn = (SectTypeEnvAnn) manager.get(new Key(sectionName, SectTypeEnvAnn.class));
                    }
                }
                tempFile.delete();
            }

            // We check if some variable and its type have a different value
            // assigned to them
            Set<Map.Entry<String, String>> valuesStrSet = valuesStrMap.entrySet();
            Iterator<Map.Entry<String, String>> valuesStrIterator =
                    valuesStrSet.iterator();
            while (valuesStrIterator.hasNext()) {
                Map.Entry<String, String> entry = valuesStrIterator.next();
                String varOrTypeStr = entry.getKey();
                String valueStr = entry.getValue();

                String typeStr = boundedVars.get(varOrTypeStr);
                if (typeStr != null) {
                    // If varOrTypeStr corresponds to a variable x, not to a type
                    String valueOfType = valuesStrMap.get(typeStr);
                    if (valueOfType != null) {
                        // If the type of the variable x has an assigned value,
                        // it must be the same assigned to x, valueStr
                        if (!valueStr.equals(valueOfType)) {
                            System.out.println("The second argument of the WEQ "
                                    + "tactic is not correct: "
                                    + "the variable '" + varOrTypeStr + "' and its "
                                    + "type (" + typeStr + ") have values assigned"
                                    + " to them which are different.");
                            return false;
                        }
                    }
                }
            }

            ZFactory zFactory = new ZFactoryImpl();
            ZExprList zExprList = zFactory.createZExprList();
            Map<RefExpr, Expr> values = new HashMap<RefExpr, Expr>();
            // We check if each bounded variable, or its type, has a value
            // assigned to it.
            Set<Map.Entry<String, String>> entrySet = boundedVars.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String varName = entry.getKey();
                String typeStr = entry.getValue();
                String varValueStr = valuesStrMap.get(varName);
                String typeValueStr = valuesStrMap.get(typeStr);
                ZName zName = zFactory.createZName(varName,
                        zFactory.createZStrokeList(), "varName");
                RefExpr varNameRefExpr = zFactory.createRefExpr(zName, zExprList,
                        false, false);
                String valueStr = "";
                if (varValueStr == null && typeValueStr == null) {
                    System.out.println("The second argument of the WEQ "
                            + "tactic is not correct: the variable '" + varName
                            + "' has not a value assigned to it and its type ("
                            + typeStr + ") has not either.");
                    return false;
                } else if (varValueStr != null) {
                    valueStr = varValueStr;
                } else {
                    valueStr = typeValueStr;
                }

                Term valueParsed = ParseUtils.parseExpr
                        (new StringSource(valueStr),
                        zLive.getCurrentSection(), zLive.getSectionManager());

                if (!(valueParsed instanceof SetExpr)) {
                    System.out.println("The specified set of values is not"
                            + " correct. It must be a set by extension.");
                    return false;
                }
                values.put(varNameRefExpr, (Expr) valueParsed);

            }

            WEQTacticInfo = new WEQTacticInfo();
            WEQTacticInfo.setPred(pred);
            WEQTacticInfo.setValues(values);

            return true;


        } catch (CommandException e) {
            System.out.println("The second argument of the WEQ is not correct: "
                    + "some of the assigned values has incorrect type.");
            tempFile.delete();
            return false;
        } catch (Exception e) {
            tempFile.delete();
            return false;

        }
    }

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     * @throws java.lang.IllegalArgumentException if tacticInfo is not an instance of
     * ETTacticInfo.
     */
    public void setTacticInfo(TacticInfo tacticInfo)
            throws IllegalArgumentException {

        if (tacticInfo instanceof WEQTacticInfo) {
            WEQTacticInfo = (WEQTacticInfo) tacticInfo;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo() {
        return WEQTacticInfo;
    }


    /**
     * Returns, from the member pred of the TacticInfo, the predicate p from
     * from which predicates that will generate new test classes will be
     * generated. p is the conjunction of the range and the term of the pred
     * of the TacticInfo if it is a \exists quantification; or the conjunction of
     * the range and the negation of the term if the pred of TacticInfo is a
     * \forall quantification.
     *
     * @return from the member pred of the TacticInfo, the predicate p from
     * from which predicates that will generate new test classes will be
     * generated. p is the conjunction of the range and the term of the pred
     * of the TacticInfo if it is a \exists quantification; or the conjunction of
     * the range and the negation of the term if the pred of TacticInfo is a
     * \forall quantification.
     */
    private Pred obtainMainPred() {
        Pred pred = WEQTacticInfo.getPred();

        ZFactory zFactory = new ZFactoryImpl();

        QntPred qntPred = (QntPred) pred;
        ZSchText zSchText = qntPred.getZSchText();
        Pred rangePred = zSchText.getPred();
        Pred termPred = qntPred.getPred();
        Pred mainPred = null;

        // We construct the predicate from which we will obtain the predicates
        // that will determine new test classes
        if (pred instanceof ExistsPred) {
            mainPred = termPred;
        } else if (pred instanceof ForallPred) {
            if (termPred instanceof NegPred) {
                NegPred negPred = (NegPred) termPred;
                mainPred = negPred.getPred();
            } else {
                mainPred = zFactory.createNegPred(termPred);
            }
        } else {
            System.out.println("Unexpected error applying WEQ tactic.");
        }

        // If the quantification has a non-null range, we conjugate it to the
        // obtained predicate
        if (rangePred != null) {
            List<Pred> predList = new ArrayList<Pred>();
            predList.add(rangePred);
            predList.add(mainPred);
            mainPred = zFactory.createAndPred(predList, And.Wedge);
        }

        return mainPred;
    }

    /**
     * Returns the list of predicates that are obtained from the specified
     * predicate, transforming it to DNF and intantiating it for each assignation
     * of values to variables specified in the member values of the TacticInfo
     * of this class.
     * @param mainPred
     * @return the list of predicates that are obtained from the specified
     * predicate, transforming it to DNF and intantiating it for each assignation
     * of values to variables specified in the member values of the TacticInfo
     * of this class.
     */
    private List<Pred> obtainPredList(Pred mainPred) {

        ZFactory zFactory = new ZFactoryImpl();
        CZTCloner cztCloner = new CZTCloner();


        Map<RefExpr, Expr> values = WEQTacticInfo.getValues();
        Set<Entry<RefExpr, Expr>> entrySet = values.entrySet();


        // and consider each clause of the predicate, where we replace the
        // bounded variables of the quantification for each combination of values
        // present in the member values of the TacticInfo

        int numberOfVariables = values.size();
        int indexes[] = new int[numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            indexes[i] = 0;
        }
        int lengths[] = new int[numberOfVariables];
        Iterator<Entry<RefExpr, Expr>> valuesIterator = entrySet.iterator();
        int count = 0;
        int productSize = 1;
        Pred andPred = null;
        while (valuesIterator.hasNext()) {
            Entry<RefExpr, Expr> valueEntry = valuesIterator.next();
            RefExpr varRefExpr = valueEntry.getKey();
            Expr valueExpr = valueEntry.getValue();
            ZExprList innerZExprList = zFactory.createZExprList();
            innerZExprList.add(varRefExpr);

            if (valueExpr instanceof SetExpr) {
                SetExpr setExpr = (SetExpr) valueExpr;
                lengths[count] = setExpr.getZExprList().size();
                productSize *= lengths[count];
                innerZExprList.add(setExpr);
            }

            TupleExpr tupleExpr = zFactory.createTupleExpr(innerZExprList);
            ZName greaterThanSymbolZName = zFactory.createZName(UtilSymbols.getSymbol(20),
                    zFactory.createZStrokeList(), "notin");
            RefExpr greaterThanSymbolRefExpr = zFactory.createRefExpr(greaterThanSymbolZName,
                    zFactory.createZExprList(), false, false);
            ZExprList outerZExprList = zFactory.createZExprList();
            outerZExprList.add(tupleExpr);
            outerZExprList.add(greaterThanSymbolRefExpr);
            MemPred memPred = zFactory.createMemPred(outerZExprList, Boolean.TRUE);


            if (count == 0) {
                andPred = memPred;
            } else {
                List<Pred> andPredList = new ArrayList<Pred>();
                andPredList.add(memPred);
                andPredList.add(andPred);
                andPred = zFactory.createAndPred(andPredList, And.Wedge);
            }

            count++;
        }

        List<Pred> predList = new ArrayList<Pred>();
        CZTReplacer replaceVisitor = new CZTReplacer();


        // We transform the predicate mainPred to DNF
        Pred predInDNF = SpecUtils.getDNF(mainPred);
        List<Pred> disjunctionsList =
                predInDNF.accept(new OrPredClausesExtractor());
        for(int i=0; i<disjunctionsList.size(); i++){
            Pred predClause = disjunctionsList.get(i);
            for (int k = 0; k < productSize; k++) {
                Pred newPred = (Pred) predClause.accept(cztCloner);
                valuesIterator = entrySet.iterator();
                int p = 0;
                while (valuesIterator.hasNext()) {
                    Entry<RefExpr, Expr> valueEntry = valuesIterator.next();
                    RefExpr varRefExpr = valueEntry.getKey();
                    Expr expr = valueEntry.getValue();
                    if (expr instanceof SetExpr) {
                        SetExpr setExpr = (SetExpr) expr;
                        Expr valueExpr = setExpr.getZExprList().get(indexes[p]);

                        replaceVisitor.setOrigTerm(varRefExpr);
                        replaceVisitor.setNewTerm(valueExpr);
                        newPred = (Pred) newPred.accept(replaceVisitor);

                    }
                    p++;

                }

                predList.add(newPred);

                // We increment the current indexes for the next iteration
                boolean hasInc = false;
                for (int j = 0; j < numberOfVariables && !hasInc; j++) {
                    if (indexes[j] != lengths[j] - 1) {
                        indexes[j]++;
                        hasInc = true;
                    } else {
                        indexes[j] = 0;
                    }
                }
            }
        }

        Pred pred = WEQTacticInfo.getPred();
        QntPred qntPred = (QntPred) pred;
        ZSchText zSchText = qntPred.getZSchText();
        ZSchText complementSchText = (ZSchText) zSchText.accept(cztCloner);
        complementSchText.setPred(andPred);

        ExistsPred complementExistsPred =
                zFactory.createExistsPred(complementSchText, mainPred);

        NegPred complementPred = zFactory.createNegPred(complementExistsPred);

        predList.add(complementPred);

        return predList;
    }

    /**
     * Returns the map from name of variables to the corresponding values, repre
     * sented as strings.
     * @param decl
     * @param quant
     * @return the map from name of variables to the corresponding values, repre
     * sented as strings.
     */
    private Map<String, String> extractBoundedVars(String decl, String quant) {
        Map<String, String> boundedVars = new HashMap<String, String>();
        int beginIndex = decl.indexOf(quant) + 7;
        int endIndex = decl.indexOf("|");
        if (endIndex == -1) {
            endIndex = decl.indexOf("@");
        }
        String auxEqExprStr = decl.substring(beginIndex, endIndex);
        String declArr[] = auxEqExprStr.split(";");
        for (int i = 0; i < declArr.length; i++) {
            String varsAndTypes[] = declArr[i].split(":");
            String varsStr = varsAndTypes[0];
            String typeStr = varsAndTypes[1].trim();
            String varArr[] = varsStr.split(",");
            for (int j = 0; j < varArr.length; j++) {
                boundedVars.put(varArr[j].trim(), typeStr);
            }

        }
        return boundedVars;
    }

    /**
     * Returns the list of test classes given a test class and a list of predi-
     * cates.
     * @param tClass
     * @param predList
     * @return the list of test classes given a test class and a list of predi
     * cates.
     */
    private List<TClass> obtainTClassList(TClass tClass, List<Pred> predList) {
        ZFactory zFactory = new ZFactoryImpl();
        CZTCloner cztCloner = new CZTCloner();

        AxPara opAxPara = (AxPara) originalOp.getMyAxPara().accept(cztCloner);
        String opName = SpecUtils.getAxParaName(opAxPara);
        Integer tClassInteger = tClassNumbersMap.get(opName);
        if (tClassInteger == null) {
            tClassInteger = new Integer(1);
            tClassNumbersMap.put(opName, tClassInteger);
        }
        AxPara tClassAxPara = tClass.getMyAxPara();
        List<TClass> tClassList = new ArrayList<TClass>();

        for (int j = 0; j < predList.size(); j++) {
            Pred auxPred = predList.get(j);

            int tClassNumber = tClassNumbersMap.get(opName).intValue();
            String tClassName = SpecUtils.getAxParaName(opAxPara) + "_WEQ_"
                    + tClassNumber;
            tClassNumbersMap.put(opName, tClassNumber + 1);
            AxPara newAxPara = (AxPara) tClassAxPara.accept(cztCloner);

            Pred newAxParaPred = SpecUtils.getAxParaPred(newAxPara);
            SpecUtils.setAxParaPred(newAxPara, SpecUtils.andPreds(newAxParaPred,
                    auxPred));

            // The test class is added to the list of test classes
            TClass auxTClass = new TClassImpl((AxPara) tClass.getMyAxPara().accept(cztCloner), tClassName);
            AxPara auxTClassAxPara = auxTClass.getMyAxPara();

            ZName zName = zFactory.createZName(tClass.getSchName(),
                    zFactory.createZStrokeList(), "IncludedTClass");
            RefExpr refExpr = zFactory.createRefExpr(zName,
                    zFactory.createZExprList(), false, false);

            ZDeclList zDeclList = zFactory.createZDeclList();
            zDeclList.add(0, zFactory.createInclDecl(refExpr));

            SpecUtils.setAxParaName(auxTClassAxPara, tClassName);
            SpecUtils.setAxParaListOfDecl(auxTClassAxPara, zDeclList);
            SpecUtils.setAxParaPred(auxTClassAxPara, auxPred);

            tClassList.add(auxTClass);
        }
        return tClassList;

    }

}
