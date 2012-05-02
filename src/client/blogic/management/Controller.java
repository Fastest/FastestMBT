package client.blogic.management;

import java.util.concurrent.locks.*;
import java.io.*;
import java.util.*;
import java.net.URL;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.ZDeclList;

import client.presentation.ClientUI;
import client.presentation.ClientTextUI;    
import client.blogic.testing.refinement.ConcreteTCase;
import client.blogic.testing.refinement.RefLawRepository;
import client.blogic.management.ii.events.*;
import client.blogic.management.ii.IIComponent;
import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.testing.ttree.visitors.TCaseNodeAdder;
import client.blogic.testing.ttree.visitors.TCaseDadFinder;
import common.repository.AbstractIterator;
import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.z.TClass;
import common.z.AbstractTCase;
import client.blogic.management.ii.EventAdmin;
import client.blogic.testing.ttree.visitors.SchemeTTreeFinder;
import common.z.Scheme;
import common.z.SpecUtils;
import common.z.czt.visitors.ContainsTermVerifier;
import compserver.prunning.TreePruner;
import compserver.abstraction.capture.execution.CompilationInfo;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.z.ast.AxPara;
import common.fastest.FastestUtils;
import compserver.prunning.PruneUtils;

/**
 * Maintains references to the specification (both folded and unfolded 
 * ocurrences), test trees, abstract test cases, the list of operations to be 
 * tested (together with the strategy of tactics application and tactics list
 * for each operation) and, if any, the functions of abstraction and refinement.
 * @author Pablo Rodriguez Monetti
 */
public class Controller extends IIComponent {

    private SectionManager sectionManager;
    private Spec originalSpec;
    private Spec unfoldedSpec;
    // Repository of the loaded operations from the loaded specification
    private AbstractRepository<String> loadedOpsRep;
    // Repository of the operations that the user wants to test
    private AbstractRepository<String> opsToTestRep;
    // Repository of the classes that the user wants to test
    private AbstractRepository<String> classToTestRep;
    // Repository of the operations that the user marks as predicates
    private AbstractRepository<String> schemaPredicatesRep;
    // Map from operations to tactic strategies
    private Map<String, TTreeStrategy> opTTreeStrategyMap;
    // Map from operations names, or test classes names, to repositories
    // of tactics
    private Map<String, List<Tactic>> tacticMap;
    // Map from operations to test trees
    private Map<String, TClassNode> opTTreeMap;
    // Map from operations to list of predicates whose conjunction is the
    // predicate of the VIS of the operation
    private Map<String, List<Pred>> dnfPredListMap;
    //Map from operations to refined test cases
    private Map<String, List<ConcreteTCase>> opRefTCaseMap;
    //Map from abstract test case name to refined test cases
    private Map<String, ConcreteTCase> absTCaseRefTCaseMap;
    //Refinament Laws Repository
    private RefLawRepository refLawRepository;
    //Indicates the selected refinement law
    private String selectedRefLaw;
    // Indicates how many abstract test cases are being calculated
    private int pendingAbsTCases;
    // Indicates how many concrete test cases are being abstracted
    private int pendingCTCases;
    // Indicates how many abstract test clases are being trying to be pruned
    private int pendingPrunnings;
    // Indicates how many test trees are being considered for obtaining abstract
    // test cases
    private int pendingTTrees;
    // Indicates if the refining process is active
    private boolean refining;
    // Indicates how many abstract test cases are being refined
    private int pendingToRef;
    // Gives the values assigned to the variables defined in axiomatic
    // definitions
    private Map<RefExpr, Expr> axDefsValues;
    // Gives the varibles (together with their types), defined in axiomatic
    // definitions, which do not have a value assigned by default (in the
    // predicate of the axiomatic definition) and that expect the user to
    // give that value
    private Map<String, Expr> axDefsRequired;
    // Indicates, for each variable x listed in axDefsValues, the list of
    // predicates that contains the variable x
    Map<String, List<Pred>> axDefsRequiredPreds;
    // Indicates, for each predicate of an axiomatic definition, the list of
    // variables, of not basic type, defined in axiomatic definitions
    Map<Pred, List<String>> axDefsPredVars;
    // Indicates, for each variable defined in axiomatic definitions, to which
    // a user has assigned a value V, the list of declaration of constants used
    // in V
    Map<String, ZDeclList> auxiliarDecls;
    // Indicates the list of variables, taken from axDefsRequired, for which
    // the user has assigned a value and a test class, that contains the variable
    // in its predicate, has been pruned. These variables are not allowed to
    // have their values setted with setaxdefs commands.
    List<String> blockedAxDefs;
    // Gives the variables, whose type is basic, defined in axiomatic
    // definition
    private Map<String, List<String>> basicAxDefs;
    private Lock myLock;
    long inicio;
    long inicioPoda;
    private AbstractRepository<TClass> leaves;
    // The maximun number of evaluations to be performed in order to find a test
    // case for a given test class
    private int maxEval;
    // The finite model size for basic types
    private int finiteModelSize;
    // The place where fuzz binary is located
    private String fuzzPath;
    private CompilationInfo compilationInfo;
    // The maximum number of classes that the DNF tactic produce when used
    private int maxDNFClasses;
    //If the predicate of the VIS of an operation contains more than 
    private int maxDNFPredsToMultiply;
    // 
    private int maxPredsToAnalize;

    /** Creates a new instance of Controller */
    public Controller() {
        loadedOpsRep = new ConcreteRepository<String>();
        opsToTestRep = new ConcreteRepository<String>();
        schemaPredicatesRep = new ConcreteRepository<String>();
        classToTestRep = new ConcreteRepository<String>();
        opTTreeStrategyMap = new HashMap<String, TTreeStrategy>();
        opTTreeMap = new HashMap<String, TClassNode>();
        dnfPredListMap = new HashMap<String, List<Pred>>();
        auxiliarDecls = new HashMap<String, ZDeclList>();
        blockedAxDefs = new ArrayList<String>();
        opRefTCaseMap = new HashMap<String, List<ConcreteTCase>>();
        absTCaseRefTCaseMap = new HashMap<String, ConcreteTCase>();
        refLawRepository = RefLawRepository.getInstance();
        selectedRefLaw = null;
        compilationInfo = null;
        refining = false;
        pendingToRef = 0;
        pendingAbsTCases = 0;
        pendingTTrees = 0;
        pendingCTCases = 0;
        pendingPrunnings = 0;
        leaves = new ConcreteRepository<TClass>();
        inicioPoda = 0;
        myLock = new ReentrantLock();
        File configFile = null;
        try {

            URL url = ClientTextUI.class.getResource("ClientTextUI.class");
            String urlStr = url.toString();
            String currentDir = urlStr.substring(9, urlStr.indexOf("fastest.jar"));
            configFile = new File(currentDir + "lib/conf/fastest.conf");
            BufferedReader in = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = in.readLine()) != null) {
                String lineParts[] = line.split("=");
                if (lineParts.length == 2) {
                    lineParts[0] = lineParts[0].trim();
                    lineParts[1] = lineParts[1].trim();
                    if (lineParts[0].equals("MAX_EVAL")) {
                        Integer i = Integer.decode(lineParts[1]);
                        maxEval = i.intValue();
                    } else if (lineParts[0].equals("DEF_SIZE_FINITE_SET")) {
                        Integer i = Integer.decode(lineParts[1]);
                        finiteModelSize = i.intValue();
                    } else if (lineParts[0].equals("FUZZ_PATH")) {
                        fuzzPath = lineParts[1];
                    } else if (lineParts[0].equals("MAX_DNF_CLASSES")) {
                        maxDNFClasses = Integer.decode(lineParts[1]);
                    } else if (lineParts[0].equals("MAX_DNF_PREDS_TO_MULTIPLY")) {
                        maxDNFPredsToMultiply = Integer.decode(lineParts[1]);
                    } else if (lineParts[0].equals("MAX_PREDS_TO_ANALIZE")) {
                        maxPredsToAnalize = Integer.decode(lineParts[1]);
                    }                   
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading file " + configFile.getAbsolutePath() + ".");
        }
    }

    /**
     * Manages an implicit invocation event.
     * @param event_
     * @throws java.lang.IllegalArgumentException if event_ is neither instance of
     * AllTCaseGenerated, AllTTreeGenerated, TCaseGenerated nor TTreeGenerated.
     */
    public void manageEvent(Event_ event_)
            throws IllegalArgumentException {
        myLock.lock();
        String opName;
        if (event_ instanceof TTreeGenerated) {
            TTreeGenerated tTreeGenerated = (TTreeGenerated) event_;
            opName = tTreeGenerated.getOpName();
            TClassNode tClassNode = tTreeGenerated.getTTree();
            if (FastestUtils.isSelectedOperation(this, opName)) {
                opTTreeMap.put(opName, tClassNode);
            }

            boolean allTTreeGnrtd = true;

            boolean isTClass = false;
            AbstractIterator<String> iter = classToTestRep.createIterator();
            while (iter.hasNext()) {
                if (iter.next().equals(opName)) {
                    isTClass = true;
                    iter.remove();
                }
            }

            AbstractIterator<String> opNameIt = opsToTestRep.createIterator();
            while (opNameIt.hasNext() && allTTreeGnrtd) {
                if (!opTTreeMap.containsKey(opNameIt.next())) {
                    allTTreeGnrtd = false;
                }
            }

            if (isTClass) {
                iter = classToTestRep.createIterator();
                if (iter.hasNext()) {
                    allTTreeGnrtd = false;
                }
            }

            if (allTTreeGnrtd) {
                try {
                    EventAdmin eventAdmin = EventAdmin.getInstance();
                    AllTTreesGenerated allTTreeEvent = new AllTTreesGenerated();
                    eventAdmin.announceEvent(allTTreeEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (event_ instanceof PruneTTreeRequested) {
            PruneTTreeRequested pruneTTreeRequested = (PruneTTreeRequested) event_;
            //leaves = pruneTTreeRequested.getLeaves();
            AbstractRepository<TClass> auxLeaves = pruneTTreeRequested.getLeaves();
            AbstractIterator<TClass> tClassIt = auxLeaves.createIterator();
            while (tClassIt.hasNext()) {
                TClass auxTClass = tClassIt.next();
                leaves.addElement(auxTClass);
                pendingPrunnings++;
            }
            if (inicioPoda == 0) {
                Calendar cal = Calendar.getInstance();
                inicioPoda = cal.getTimeInMillis();
            }
        } else if (event_ instanceof PruneTClassRequested) {
            /*if (pendingPrunnings == 0){
            Calendar cal = Calendar.getInstance();
            inicioPoda = cal.getTimeInMillis();
            }
            pendingPrunnings++;*/
        } else if (event_ instanceof TCaseRefined) {
            TCaseRefined tCaseRefined = (TCaseRefined) event_;
            if (opRefTCaseMap.get(tCaseRefined.getOpName()) != null) {
                opRefTCaseMap.get(tCaseRefined.getOpName()).
                        add(tCaseRefined.getConcreteTCase());
            } else {
                List<ConcreteTCase> list = new ArrayList<ConcreteTCase>();
                list.add(tCaseRefined.getConcreteTCase());
                opRefTCaseMap.put(tCaseRefined.getOpName(), list);
            }
            absTCaseRefTCaseMap.put(tCaseRefined.getAbstractTCase().getSchName(),
                    tCaseRefined.getConcreteTCase());
            String tCaseName = tCaseRefined.getAbstractTCase().getSchName();
            System.out.println(tCaseName + " test case refination -> SUCCESS.");
            pendingToRef--;
            if (pendingToRef == 0) {
                if (!refining) {
                    processFinished();
                }
            }
        } else if (event_ instanceof AllTCasesRefined) {
            refining = false;
            if (pendingToRef == 0) {
                processFinished();
            }
        } else if (event_ instanceof RefLawSelected) {
            RefLawSelected refLawSelected = (RefLawSelected) event_;
            this.selectedRefLaw = refLawSelected.getRefLawName();
        } else if (event_ instanceof AllTCasesRefineRequested) {
            refining = true;
            try {
                AllTCasesRefineRequested allTCasesRefineRequested =
                        (AllTCasesRefineRequested) event_;
                AllTCasesRequested allTCasesRequested =
                        new AllTCasesRequested(allTCasesRefineRequested.getOpName(),
                        allTCasesRefineRequested.getTTree(), getMaxEval());
                EventAdmin eventAdmin = EventAdmin.getInstance();
                eventAdmin.announceEvent(allTCasesRequested);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event_ instanceof RunFinished) {
            try {
                ClientUI clientUI = getMyClientUI();
                if (clientUI instanceof ClientTextUI) {
                    synchronized (clientUI) {
                        clientUI.notify();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event_ instanceof TCaseAbstracted) {
            TCaseAbstracted caseAbstracted = (TCaseAbstracted) event_;
            String brotherName = caseAbstracted.getBrotherName();
            AbstractTCase cOutput = caseAbstracted.getAbstractTCase();
            String opNameSource = caseAbstracted.getOpName();
            TClassNode tClassNode = opTTreeMap.get(opNameSource);
            String dadName = tClassNode.acceptVisitor(new TCaseDadFinder(brotherName));
            Boolean correctlyadded = tClassNode.acceptVisitor(new TCaseNodeAdder(dadName, cOutput));
            System.out.println(dadName + " test case abstraction -> SUCCESS.");
            pendingCTCases--;
            if (pendingCTCases == 0) {
                try {
                    ClientUI clientUI = getMyClientUI();
                    if (clientUI instanceof ClientTextUI) {
                        synchronized (clientUI) {
                            clientUI.notify();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (event_ instanceof ScriptModified) {
            // We must actualize all the maps with ConcreteTCase's information
            pendingCTCases--;
            ScriptModified scriptModified = (ScriptModified) event_;
            ConcreteTCase ctCase = scriptModified.getConcreteTCase();
            String ctcName = ctCase.getConcreteTCaseName();

            Map<String, ConcreteTCase> auxMap = new HashMap<String, ConcreteTCase>();
            Set<Map.Entry<String, ConcreteTCase>> set = absTCaseRefTCaseMap.entrySet();
            Iterator<Map.Entry<String, ConcreteTCase>> iterator = set.iterator();
            boolean founded = false;
            while (iterator.hasNext() && !founded) {
                Map.Entry<String, ConcreteTCase> mapEntry = iterator.next();
                ConcreteTCase auxCTCase = mapEntry.getValue();
                String absTCaseName = mapEntry.getKey();
                String ctcAuxName = auxCTCase.getConcreteTCaseName();
                if (ctcAuxName.equals(ctcName)) {
                    auxMap.put(absTCaseName, auxCTCase);
                    founded = true;
                }
            }
            absTCaseRefTCaseMap.putAll(auxMap);

            Set<Map.Entry<String, List<ConcreteTCase>>> set2 = opRefTCaseMap.entrySet();
            Iterator<Map.Entry<String, List<ConcreteTCase>>> iterator2 = set2.iterator();
            while (iterator2.hasNext()) {
                Map.Entry<String, List<ConcreteTCase>> mapEntry = iterator2.next();
                List<ConcreteTCase> auxCTCases = mapEntry.getValue();
                String opNameSpec = mapEntry.getKey();
                for (int i = 0; i < auxCTCases.size(); i++) {
                    ConcreteTCase auxCTCase = auxCTCases.get(i);
                    String auxCTCName = auxCTCase.getConcreteTCaseName();
                    if (auxCTCName.equals(ctcName)) {
                        auxCTCases.set(i, ctCase);
                    }
                }
            }
            if (pendingCTCases == 0) {
                processFinished();
            }
        } else if (event_ instanceof PrunningResult) {
            PrunningResult prunningResult = (PrunningResult) event_;
            String tClassName = prunningResult.getTClassName();
            String theoremName = prunningResult.getTheoremName();
            List<String> params = prunningResult.getParams();
            boolean result = prunningResult.getResult();
            if (result) {
                System.out.print("Test Objective " + tClassName + " pruned by \n"
                        + theoremName + "(");
                for (int i = 0; i < params.size() - 1; i++) {
                    System.out.print(params.get(i) + ", ");
                }
                System.out.println(params.get(params.size() - 1) + ")\n");
                TreePruner treePruner = new TreePruner(this);
                boolean pruned = treePruner.pruneFrom(tClassName);

                blockSetAxDefCmd(tClassName);

            } else {
                System.out.println("Fastest cannot prune " + tClassName);
            }
            pendingPrunnings--;
            if (pendingPrunnings == 0) {
                // We check if appears new leaves. In that case we try to prune 
                // them. In the other case we finish the pruning.
                AbstractRepository<TClass> newLeaves =
                        PruneUtils.obtainTClasses(this);
                AbstractIterator<TClass> tClassItOld = leaves.createIterator();
                AbstractIterator<TClass> tClassItNew = newLeaves.createIterator();
                List<TClass> newList = new ArrayList<TClass>();
                List<TClass> oldList = new ArrayList<TClass>();
                while (tClassItNew.hasNext()) {
                    newList.add(tClassItNew.next());
                }
                while (tClassItOld.hasNext()) {
                    oldList.add(tClassItOld.next());
                }
                newList.removeAll(oldList);
                if (newList.size() > 0) {
                    AbstractRepository<TClass> alternativeLeaves = new ConcreteRepository<TClass>();
                    for (int i = 0; i < newList.size(); i++) {
                        alternativeLeaves.addElement(newList.get(i));
                    }

                    try {
                        PruneTTreeRequested pruneTTreeRequested = new PruneTTreeRequested(alternativeLeaves);
                        EventAdmin eventAdmin = EventAdmin.getInstance();
                        eventAdmin.announceEvent(pruneTTreeRequested);
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        System.out.println("Illegal Access");
                    }
                } else {
                    Calendar cal = Calendar.getInstance();
                    long fin = cal.getTimeInMillis();
                    System.out.println("Elapsed time (prunett): " + (fin - inicioPoda) + " miliseconds.");
                    // We reset inicioPoda
                    inicioPoda = 0;
                    try {
                        ClientUI clientUI = getMyClientUI();
                        if (clientUI instanceof ClientTextUI) {
                            synchronized (clientUI) {
                                clientUI.notify();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (event_ instanceof AllTCasesGenerated) {
            ClientUI clientUI = getMyClientUI();
            if (clientUI instanceof ClientTextUI) {
                synchronized (clientUI) {
                    clientUI.notify();
                }
            }
        } else if (event_ instanceof AllTTreesGenerated) {
            ClientUI clientUI = getMyClientUI();
            if (clientUI instanceof ClientTextUI) {
                synchronized (clientUI) {
                    clientUI.notify();
                }
            }
        } else if (event_ instanceof AllTCasesRequested) {
            pendingTTrees++;
        } else if (event_ instanceof TCaseAddCaptureCodeRequested) {
            Calendar cal = Calendar.getInstance();
            inicio = cal.getTimeInMillis();
            pendingCTCases++;
        } else if (event_ instanceof TCaseAbstractRequested) {
            pendingCTCases++;
        } else if (event_ instanceof NotTClassLeavesFounded) {
            pendingTTrees--;
            if (pendingTTrees == 0) {
                if (!refining) {
                    System.out.println("Error managing event NotTClassLeavesFounded in Controller");
                    processFinished();
                }

            }
        } else if (event_ instanceof TCaseRequested) {
            TCaseRequested tCaseStrategyEvent = (TCaseRequested) event_;
            opName = tCaseStrategyEvent.getOpName();
            TClass tClass = tCaseStrategyEvent.getTClass();
            if (pendingAbsTCases == 0) {
                Calendar cal = Calendar.getInstance();
                inicio = cal.getTimeInMillis();
            }

            pendingAbsTCases++;
        } else if (event_ instanceof TCaseGenerated) {
            TCaseGenerated tCaseGenerated = (TCaseGenerated) event_;
            opName = tCaseGenerated.getOpName();
            TClass tClass = tCaseGenerated.getTClass();
            String tClassName = tClass.getSchName();
            AbstractTCase abstractTCase = tCaseGenerated.getAbstractTCase();
            if (abstractTCase == null) {
                System.out.println(tClassName + " test case generation -> FAILED.");
            } else if (abstractTCase != null && abstractTCase.getMyAxPara() == null) {
                System.out.println(tClassName + " test case generation -> FAILED "
                        + "(without performing all the possible evaluations)");
            } else {
                String schName = abstractTCase.getSchName();
                System.out.println(tClassName + " test case generation -> SUCCESS.");
                TClassNode tClassNode = opTTreeMap.get(opName);
                Boolean correctlyadded =
                        tClassNode.acceptVisitor(new TCaseNodeAdder(tClassName, abstractTCase));
                // Voy a suponer que es un proceso secuencial
                // No se puede refinar sin que antes se hayan generado todos los casos de
                // una operacion
		/*if (refining){
                pendingToRef++;
                TCaseRefineRequested tCaseRefineRequested =
                new TCaseRefineRequested(opName, abstractTCase);
                ClientUI clientUI = getMyClientUI();
                if(clientUI instanceof ClientTextUI){
                clientUI.getMyController();
                try{
                EventAdmin eventAdmin = EventAdmin.getInstance();
                eventAdmin.announceEvent(tCaseRefineRequested);
                }
                catch(Exception e){
                e.printStackTrace();
                }
                }
                }*/
            }
            pendingAbsTCases--;
            //System.out.println("Quedan: "+pendingAbsTCases);
            if (pendingAbsTCases == 0) {
                pendingTTrees = 0;
                if (refining) {
                    try {
                        EventAdmin eventAdmin = EventAdmin.getInstance();
                        eventAdmin.announceEvent(new AllTCasesRefined());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    processFinished();
                }
            }
        } else if (event_ instanceof RefineAbsTCasesRequested) {
            Calendar cal = Calendar.getInstance();
            inicio = cal.getTimeInMillis();
            RefineAbsTCasesRequested refineAbsTCasesRequested =
                    (RefineAbsTCasesRequested) event_;
            Collection<AbstractTCase> tcaColl =
                    refineAbsTCasesRequested.getAbsTCasesColl();
            opName = refineAbsTCasesRequested.getOpName();
            String targetLanguaje = refineAbsTCasesRequested.getTargetLanguaje();
            String pathUUT = refineAbsTCasesRequested.getPathUUT();
            String refLawName = refineAbsTCasesRequested.getRefLawName();
            Iterator<AbstractTCase> iter = tcaColl.iterator();
            while (iter.hasNext()) {
                AbstractTCase absTCase = iter.next();
                pendingToRef++;
                TCaseRefineRequested tCaseRefineRequested =
                        new TCaseRefineRequested(opName, absTCase, pathUUT, targetLanguaje, refLawName);
                try {
                    EventAdmin eventAdmin = EventAdmin.getInstance();
                    eventAdmin.announceEvent(tCaseRefineRequested);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // No se si funcionara. Creo que no esta SINCRONiZADO!!!
            try {
                EventAdmin eventAdmin = EventAdmin.getInstance();
                eventAdmin.announceEvent(new AllTCasesRefined());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } /*else if (event_ instanceof TCaseRefineRequested){
        TCaseRefineRequested tCaseRefineRequested = (TCaseRefineRequested) event_;
        opName = tCaseRefineRequested.getOpName();
        AbstractTCase absTCase = tCaseRefineRequested.getAbstractTCase();
        String refLawName = tCaseRefineRequested.getRefLawName();
        String pathUUT = tCaseRefineRequested.getPathUUT();
        String targetLanguaje = tCaseRefineRequested.getTargetLanguaje();
        new TCaseRefClientRunner(opName, absTCase, pathUUT, targetLanguaje, refLawName);
        }*/ // Es incorrecto. Se mandara 2 veces a refinar el mismo caso!!!
        else if (event_ instanceof FastestResetted) {
            setOriginalSpec(null);
            setUnfoldedSpec(null);
            setLoadedOpsRep(new ConcreteRepository<String>());
            setOpsToTestRep(new ConcreteRepository<String>());
            setOpTTreeStrategyMap(new HashMap<String, TTreeStrategy>());
            setTacticMap(new HashMap<String, List<Tactic>>());
            setOpTTreeMap(new HashMap<String, TClassNode>());
            auxiliarDecls = new HashMap<String, ZDeclList>();
            blockedAxDefs = new ArrayList<String>();
            opRefTCaseMap.clear();
            absTCaseRefTCaseMap.clear();
            refLawRepository.clear();
            selectedRefLaw = null;
            refining = false;
            FastestUtils.resetTacticsNumbersMap();
        } else {
            throw new IllegalArgumentException();
        }
        myLock.unlock();
    }

    /**
     * Blocks the chance to set the values, with a setaxdef command, of the
     * variables, defined in axiomatic definitions, that appear in the predicate
     * of the specified test class.
     * @param tClassName the name of the test class C for which the variables,
     * defined in axiomatic definitions and contained in the predicate of C,
     * will be blocked to be setted through a setaxdef command
     */
    private void blockSetAxDefCmd(String tClassName) {
        // We block the setaxdef command for those axiomatic definitions
        // that are contained in the predicate of this pruned test class

        Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
        Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
        String opName;
        Scheme tClassScheme = null;
        while (iterator.hasNext() && tClassScheme == null) {
            Map.Entry<String, TClassNode> mapEntry = iterator.next();
            opName = mapEntry.getKey();
            TClassNode opTTreeRoot = mapEntry.getValue();

            tClassScheme = opTTreeRoot.acceptVisitor(new SchemeTTreeFinder(tClassName, 0));
        }

        AxPara tClassAxPara = tClassScheme.getMyAxPara();
        Pred tClassAxParaPred = SpecUtils.getAxParaPred(tClassAxPara);

        ContainsTermVerifier ctVerifier = new ContainsTermVerifier();

        Set<Map.Entry<RefExpr, Expr>> axDefsValuesSet =
                axDefsValues.entrySet();

        Iterator<Map.Entry<RefExpr, Expr>> axDefsValuesIt =
                axDefsValuesSet.iterator();

        while (axDefsValuesIt.hasNext()) {
            Map.Entry<RefExpr, Expr> entry = axDefsValuesIt.next();
            RefExpr varNameRefExpr = entry.getKey();
            ctVerifier.setTerm(varNameRefExpr);
            if (tClassAxParaPred.accept(ctVerifier)) {
                blockedAxDefs.add(varNameRefExpr.getName().toString());
            }
        }

    }

    /**
     * Sets the reference to the system's ClientIU object.
     * @param clientUI
     */
    @Override
    public void setMyClientUI(ClientUI clientUI) {
        myClientUI = clientUI;
        clientUI.setMyController(this);
    }

    /**
     * Sets the folded (original) specification of the system under test.
     * @param spec
     */
    public void setOriginalSpec(Spec spec) {
        originalSpec = spec;
    }

    /**
     * Gets the folded (original) specification of the system under test.
     * @return
     */
    public Spec getOriginalSpec() {
        return originalSpec;
    }

    /**
     * Sets the unfolded specification of the system under test.
     * @param spec
     */
    public void setUnfoldedSpec(Spec spec) {
        unfoldedSpec = spec;
    }

    /**
     * Gets the unfolded specification of the system under test.
     * @return
     */
    public Spec getUnfoldedSpec() {
        return unfoldedSpec;
    }

    /**
     * Sets the repository of the operations to be tested.
     * @param opRep
     */
    public void setOpsToTestRep(AbstractRepository<String> opRep) {
        opsToTestRep = opRep;
    }

    /**
     * Gets the repository of the operations to be tested.
     * @return
     */
    public AbstractRepository<String> getOpsToTestRep() {
        return opsToTestRep;
    }

    /**
     * Sets the repository of the clases to be tested.
     * @param opRep
     */
    public void setClassToTestRep(AbstractRepository<String> classRep) {
        classToTestRep = classRep;
    }

    /**
     * Gets the repository of the operations to be tested.
     * @return
     */
    public AbstractRepository<String> getClassToTestRep() {
        return classToTestRep;
    }

    /**
     * Sets the repository of the schemas marked as predicates
     * @param opRep
     */
    public void setSchemaPredicatesRep(AbstractRepository<String> schemaPredicatesRep) {
        this.schemaPredicatesRep = schemaPredicatesRep;
    }

    /**
     * Gets the repository of the schemas marked as predicates
     * @return
     */
    public AbstractRepository<String> getSchemaPredicatesRep() {
        return schemaPredicatesRep;
    }

    /**
     * Sets the repository of the operations loaded with the specification.
     * @param opRep
     */
    public void setLoadedOpsRep(AbstractRepository<String> opRep) {
        loadedOpsRep = opRep;
    }

    /**
     * Gets the repository of the operations loaded with the specification.
     * @return
     */
    public AbstractRepository<String> getLoadedOpsRep() {
        return loadedOpsRep;
    }

    /**
     * Sets the structure that maps operation names to associated strategies 
     * of tactic application.
     * @param opMap
     */
    public void setOpTTreeStrategyMap(Map<String, TTreeStrategy> opMap) {
        opTTreeStrategyMap = opMap;
    }

    /**
     * Gets the structure that maps operation names to associated strategies of 
     * tactic application.
     * @return
     */
    public Map<String, TTreeStrategy> getOpTTreeStrategyMap() {
        return opTTreeStrategyMap;
    }

    /**
     * Sets the structure that maps operation, and test classes names, to
     * associated tactics repositories.
     * @param opMap the structure that maps operation, and test classes names, to
     * associated tactics repositories.
     */
    public void setTacticMap(Map<String, List<Tactic>> opMap) {
        tacticMap = opMap;
    }

    /**
     * Returns the structure that maps operation, and test classes names, to
     * associated tactics repositories.
     * @return the structure that maps operation, and test classes names, to
     * associated tactics repositories.
     */
    public Map<String, List<Tactic>> getTacticMap() {
        return tacticMap;
    }

    /**
     * Sets the structure that maps operation names to associated test trees.
     * @param opMap the structure that maps operation names to associated test trees.
     */
    public void setOpTTreeMap(Map<String, TClassNode> opMap) {
        opTTreeMap = opMap;
    }

    /**
     * Gets the structure that maps operation names to associated test trees.
     * @return
     */
    public Map<String, TClassNode> getOpTTreeMap() {
        return opTTreeMap;
    }

    /**
     * Sets the SectionManager object with which the specification was loaded.
     * It is needed for typechecking tasks.
     * @param manager the SectionManager object with which the specification was
     * loaded.
     */
    public void setTypeCheckerManager(SectionManager manager) {
        sectionManager = manager;
    }

    /**
     * Returns the SectionManager object with which the specification was loaded.
     * It is needed for typechecking tasks.
     * @return the SectionManager object with which the specification was loaded.
     */
    public SectionManager getTypeCheckerManager() {
        return sectionManager;
    }

    /**
     * Sets the maximum number of evaluations to be performed in order to try
     * to obtain a test class for each test class.
     * @param maxEval
     */
    public void setMaxEval(int maxEval) {
        this.maxEval = maxEval;
    }

    /**
     * Returns the maximum number of evaluations to be performed in order to try
     * to obtain a test class for each test class.
     * @return the maximum number of evaluations to be performed in order to try
     * to obtain a test class for each test class.
     */
    public int getMaxEval() {
        return maxEval;
    }

    /**
     * Sets the finite model size to be used in the generation of test cases.
     * @param finiteModelSize
     */
    public void setFiniteModelSize(int finiteModelSize) {
        this.finiteModelSize = finiteModelSize;
    }

    /**
     * Returns the finite model size to be used in the generation of test cases.
     * @return the finite model size to be used in the generation of test cases.
     */
    public int getFiniteModelSize() {
        return finiteModelSize;
    }

    /**
     * Sets the structure that indicates the variables defined in axiomatic
     * definitions that requires the user to give a value.
     * @param axDefsRequired the structure that indicates the variables defined in axiomatic
     * definitions that requires the user to give a value.
     */
    public void setAxDefsRequired(Map<String, Expr> axDefsRequired) {
        this.axDefsRequired = axDefsRequired;
    }

    /**
     * Returns the structure that indicates the variables defined in axiomatic
     * definitions that requires the user to give a value.
     * @return the structure that indicates the variables defined in axiomatic
     * definitions that requires the user to give a value.
     */
    public Map<String, Expr> getAxDefsRequired() {
        return axDefsRequired;
    }

    /**
     * Sets the structure that maps variables defined in axiomatic definitions
     * to their values.
     * @param axDefsValues the structure that maps variables defined in axiomatic definitions
     * to their values.
     */
    public void setAxDefsValues(Map<RefExpr, Expr> axDefsValues) {
        this.axDefsValues = axDefsValues;
    }

    /**
     * Returns the structure that maps variables defined in axiomatic definitions
     * to their values.
     * @return the structure that maps variables defined in axiomatic definitions
     * to their values.
     */
    public Map<RefExpr, Expr> getAxDefsValues() {
        return axDefsValues;
    }

    /**
     * Sets the list of variables of basic types, defined in axiomatic
     * definitions.
     * @param basicAxDefs the list of variables of basic types, defined in axiomatic
     * definitions.
     */
    public void setBasicAxDefs(Map<String, List<String>> basicAxDefs) {
        this.basicAxDefs = basicAxDefs;
    }

    /**
     * Returns the list of variables of basic types, defined in axiomatic
     * definitions.
     * @return the list of variables of basic types, defined in axiomatic
     * definitions.
     */
    public Map<String, List<String>> getBasicAxDefs() {
        return basicAxDefs;
    }

    /**
     * Sets the structure that maps variables, defined in axiomatic definitions,
     * for which the user has to enter a value, to the predicates of axiomatic
     * definitions in which they appear.
     * @param axDefsRequiredPreds the structure that maps variables, defined in
     * axiomatic definitions, for which the user has to enter a value, to the
     * predicates of axiomatic definitions in which they appear.
     */
    public void setAxDefsRequiredPreds(Map<String, List<Pred>> axDefsRequiredPreds) {
        this.axDefsRequiredPreds = axDefsRequiredPreds;
    }

    /**
     * Returns the structure that maps variables, defined in axiomatic definitions,
     * for which the user has to enter a value, to the predicates of axiomatic
     * definitions in which they appear.
     * @return the structure that maps variables, defined in
     * axiomatic definitions, for which the user has to enter a value, to the
     * predicates of axiomatic definitions in which they appear.
     */
    public Map<String, List<Pred>> getAxDefsRequiredPreds() {
        return axDefsRequiredPreds;
    }

    /**
     * Sets the structure that indicates, for each predicate of an axiomatic
     * definition, the list of variables, of not basic type, defined in
     * axiomatic definitions.
     * @param axDefsPredVars the structure that indicates, for each predicate of
     * an axiomatic definition, the list of variables, of not basic type,
     * defined in axiomatic definitions.
     */
    public void setAxDefsPredVars(Map<Pred, List<String>> axDefsPredVars) {
        this.axDefsPredVars = axDefsPredVars;
    }

    /**
     * Returns the structure that indicates, for each predicate of an axiomatic
     * definition, the list of variables, of not basic type, defined in
     * axiomatic definitions.
     * @return the structure that indicates, for each predicate of an axiomatic
     * definition, the list of variables, of not basic type, defined in
     * axiomatic definitions.
     */
    public Map<Pred, List<String>> getAxDefsPredVars() {
        return axDefsPredVars;
    }

    /**
     * Sets the structure that indicates, for each variable defined in axiomatic
     * definitions, to which a user has assigned a value V, the list of
     * declaration of constants used in V
     * @param axDefsPredVars the structure that indicates, for each variable defined in axiomatic
     * definitions, to which a user has assigned a value V, the list of
     * declaration of constants used in V
     */
    public void setAuxiliarDecls(Map<String, ZDeclList> auxiliarDecls) {
        this.auxiliarDecls = auxiliarDecls;
    }

    /**
     * Returns the structure that indicates, for each variable defined in axiomatic
     * definitions, to which a user has assigned a value V, the list of
     * declarations of constants used in V.
     * @return the structure that indicates, for each variable defined in axiomatic
     * definitions, to which a user has assigned a value V, the list of
     * declarations of constants used in V
     */
    public Map<String, ZDeclList> getAuxiliarDecls() {
        return auxiliarDecls;
    }

    /**
     * Sets the structure that indicates the list of variables, taken from
     * axDefsRequired, for which the user has assigned a value and a test class,
     * that contains the variable in its predicate, has been pruned. These
     * variables are not allowed to have their values setted with setaxdefs
     * commands.
     * @param blockedAxDefs he structure that indicates the list of variables,
     * taken from axDefsRequired, for which the user has assigned a value and a
     * test class, that contains the variable in its predicate, has been pruned.
     * These variables are not allowed to have their values setted with
     * setaxdefs commands.
     */
    public void setBlockedAxDefs(List<String> blockedAxDefs) {
        this.blockedAxDefs = blockedAxDefs;
    }

    public Map<String, List<ConcreteTCase>> getOpTCaseRefinedMap() {
        return opRefTCaseMap;
    }

    /**
     * Returns the structure that indicates the list of variables, taken from
     * axDefsRequired, for which the user has assigned a value and a test class,
     * that contains the variable in its predicate, has been pruned. These
     * variables are not allowed to have their values setted with setaxdefs
     * commands.
     * @return the structure that indicates the list of variables, taken from
     * axDefsRequired, for which the user has assigned a value and a test class,
     * that contains the variable in its predicate, has been pruned. These
     * variables are not allowed to have their values setted with setaxdefs
     * commands.
     */
    public List<String> getBlockedAxDefs() {
        return blockedAxDefs;
    }

    /**
     * Gets the selected refinement law from the repository.
     * @return
     */
    public String getSelRefLaw() {
        return selectedRefLaw;
    }

    public RefLawRepository getRefLawRepository() {
        return refLawRepository;
    }

    public void setRefLawRepository(RefLawRepository refLawRepository) {
        this.refLawRepository = refLawRepository;
    }

    public Map<String, ConcreteTCase> getAbsTCaseConcrTCaseMap() {
        return this.absTCaseRefTCaseMap;
    }

    // Este metodo es solo para testear. Debe ser borrado
    public void setAbsTCaseConcrTCaseMap(Map<String, ConcreteTCase> map) {
        this.absTCaseRefTCaseMap = map;
    }

    public void setCompilationInfo(CompilationInfo compilationInfo) {
        this.compilationInfo = compilationInfo;
    }

    public CompilationInfo getCompilationInfo() {
        return compilationInfo;
    }

    private void processFinished() {
        Calendar cal = Calendar.getInstance();
        long fin = cal.getTimeInMillis();
        System.out.println("Elapsed time: " + (fin - inicio) + " miliseconds.");
        try {
            ClientUI clientUI = getMyClientUI();
            if (clientUI instanceof ClientTextUI) {
                synchronized (clientUI) {
                    clientUI.notify();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFuzzPath(String fuzzPath) {
        this.fuzzPath = fuzzPath;
    }

    public String getFuzzPath() {
        return fuzzPath;
    }

    /**
     * Checks if a string is contained in the repository of selected operations
     * @param controller a reference to the Controller
     * @param opName the string to search in the repository
     * @return
     */
    public boolean isSelectedOperation(String opName) {
        AbstractRepository<String> selectedOpsRep = getOpsToTestRep();
        AbstractIterator<String> it = selectedOpsRep.createIterator();
        boolean hasFound = false;
        while (it.hasNext() && !hasFound) {
            if (it.next().equals(opName)) {
                hasFound = true;
            }
        }
        return hasFound;
    }

    /**
     * Checks if a string is contained in the repository of selected predicates
     * @param controller a reference to the Controller
     * @param opName the string to search in the repository
     * @return
     */
    public boolean isSelectedPredicate(String opName) {
        AbstractRepository<String> schemaPredicates = this.getSchemaPredicatesRep();
        AbstractIterator<String> it = schemaPredicates.createIterator();
        boolean hasFound = false;
        while (it.hasNext() && !hasFound) {
            if (it.next().equals(opName)) {
                hasFound = true;
            }
        }
        return hasFound;
    }



    public void putInSchemaDNFPredList(String schemaName, List<Pred> predList){
        dnfPredListMap.put(schemaName, predList);
    }

    public List<Pred> getSchemaDNFPredList(String schemaName){
        return dnfPredListMap.get(schemaName);
    }
    
    public void clearSchemaDNFPredList(){
        dnfPredListMap.clear();
    }

    public int getMaxDNFClasses(){
        return maxDNFClasses;        
    }
    
    public int getMaxDNFPredsToMultiply(){
        return maxDNFPredsToMultiply;        
    }
    
    public int getMaxPredsToAnalize(){
        return maxPredsToAnalize;        
    }    
    
        
}
