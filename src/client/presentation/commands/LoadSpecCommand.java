package client.presentation.commands;

import java.io.*;
import java.util.*;


import net.sourceforge.czt.session.CommandException;
import net.sourceforge.czt.session.Key;
import net.sourceforge.czt.session.Source;
import net.sourceforge.czt.session.FileSource;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.parser.util.CztError;
import net.sourceforge.czt.parser.util.CztErrorList;
import net.sourceforge.czt.z.ast.*;

import client.blogic.testing.ttree.tactics.Tactic;
import client.blogic.testing.ttree.strategies.TTreeStrategy;
import client.blogic.management.Controller;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.events.SpecLoaded;
import client.presentation.ClientTextUI;
import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.z.SpiveySpecsSorter;
import common.z.czt.UniqueZLive;
import common.z.czt.visitors.AxDefPredsExtractor;
import common.z.czt.visitors.AxDefsClassifier;
import common.z.czt.visitors.BasicTypeNamesExtractor;
import common.z.czt.visitors.EmptySetReplacer;
import common.z.czt.visitors.FreeTypeNamesExtractor;
import common.z.czt.visitors.OpNamesExtractor;
import common.z.czt.visitors.ParenthesisRemover;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import java.util.List;


/**
 * Instances of this class allow the load of the Z specification of the system to be tested
 * into the context of Fastest.
 * @author Pablo Rodriguez Monetti
 */
public class LoadSpecCommand implements Command {

    static private Runtime rt = java.lang.Runtime.getRuntime();
    private PrintWriter output = null;
    private String fuzzPath;

    /**
     * Runs this command.
     * @param clientTextUI
     * @param fileName
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {
        output = clientTextUI.getOutput();
        File texFileToRead = null;
        String texFileName = "";
        try {

            // Inicializamos el Controller
            Controller controller = clientTextUI.getMyController();
            fuzzPath = controller.getFuzzPath();

            // The following is made in order to load the ZLive instance at
            // Fastest initiation
            //ZLive zLive = UniqueZLive.getInstance();
	    UniqueZLive.getInstance();
            output.println("Loading specification..");

            final String argv[] = args.split(" ");
            int argc = argv.length;

            if (argc != 1 && argc != 2) {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }

            int lineNumbersTable[] = null;

            String texFileToReadName = "";
            File originalTexFile = null;
            if (argc == 1 && !argv[0].equals("") && !argv[0].equals("-spivey")) {
                texFileName = argv[0];
                originalTexFile = new File(texFileName);
                texFileToReadName = "temp/" + originalTexFile.getName() + ".tmp";
                // A copy of the original file is made, in order to avoid the
                // access restriction from other programs
                if(!copyFile(texFileName, texFileToReadName))
                    return;

            } else if (argc == 2 && argv[0].equals("-spivey")) {
                texFileName = argv[1];
                originalTexFile = new File(texFileName);
                // The method getName() of class File is used to get the name of
                // the file, instead of simply using texFileName, because
                // texFileName may contains parts of the absolute path of the file
                texFileToReadName = "temp/" + originalTexFile.getName() + ".tmp";
                texFileToRead = new File(texFileToReadName);

                lineNumbersTable = spiveyToStandard(originalTexFile,
                        texFileToRead);

                if (lineNumbersTable == null) {
                    return;
                }

            } else {
                output.println("Invalid parameters.  Try 'help'.");
                return;
            }
            texFileToRead = new File(texFileToReadName);
            FileSource source = new FileSource(texFileToRead);

            SectionManager manager = new SectionManager();
	    //SectionManager manager = zLive.getSectionManager();
            manager.put(new Key(texFileToReadName, Source.class), source);

            // The specification is loaded and set in the controller
            Spec spec = (Spec) manager.get(new Key(texFileToReadName, Spec.class));

                
            List<? extends ErrorAnn> errors =
                    TypeCheckUtils.typecheck(spec, manager, true);
            if (errors.size() > 0) {
                output.println("Specification has not been "
                        + "loaded because it has type errors.");
                for (int i = 0; i < errors.size(); i++) {
                    String errorStr = errors.get(i).toString();
                    errorStr = errorStr.substring(errorStr.indexOf(","));
                    errorStr = texFileName + errorStr;
                    output.println(errorStr + "\n");
                }
                return;
            }

            // The specification is traverse to identify the schemas that are
            // operation schemas
            OpNamesExtractor opNamesExtractor = new OpNamesExtractor(spec);
            spec.accept(opNamesExtractor);
            AbstractRepository<String> opNamesRep = opNamesExtractor.getOpNames();

            // The specification is traverse to identify the basic types
            List<String> basicTypeNames =
                    spec.accept(new BasicTypeNamesExtractor());
            
            // The specification is traverse to identify the free types
            List<String> freeTypeNames =
                    spec.accept(new FreeTypeNamesExtractor());


            Map<RefExpr, Expr> axDefsValues = new HashMap<RefExpr, Expr>();
            Map<String, Expr> axDefsRequired = new HashMap<String, Expr>();
            Map<String, List<String>> basicAxDefs =
                    new HashMap<String, List<String>>();
            List<RefExpr> noBasicAxDefVars = new ArrayList<RefExpr>();

            spec.accept(new AxDefsClassifier(basicTypeNames, freeTypeNames,
                    basicAxDefs, axDefsValues,
                    axDefsRequired, noBasicAxDefVars));

            Map<String, List<Pred>> axDefsRequiredPreds =
                    new HashMap<String, List<Pred>>();

            Map<Pred, List<String>> axDefsPredVars =
                    new HashMap<Pred, List<String>>();


            spec.accept(new AxDefPredsExtractor(axDefsRequiredPreds,
                    axDefsPredVars, noBasicAxDefVars));

            spec.accept(new ParenthesisRemover());


            /*
            System.out.println("\nVariables de tipo básico:");
            Set<Map.Entry<String, List<String>>> set3 = basicAxDefs.entrySet();
            Iterator<Map.Entry<String, List<String>>> it3 = set3.iterator();
            while(it3.hasNext()){
            Map.Entry<String, List<String>> entry = it3.next();
            List<String> list = entry.getValue();
            for(int i = 0; i<list.size(); i++){
            System.out.println(list.get(i));
            }
            }

            System.out.println("\nVariables para las que se espera valor," +
            " junto con los predicados que las restringen :");
            Set<Map.Entry<String, Expr>> set1 = axDefsRequired.entrySet();
            Iterator<Map.Entry<String, Expr>> it1 = set1.iterator();
            while(it1.hasNext()){
            Map.Entry<String, Expr> entry = it1.next();
            String varName = entry.getKey();
            System.out.println("* " + varName);
            List<Pred> predList = axDefsRequiredPreds.get(varName);
            for(int i=0; i< predList.size(); i++){
            System.out.println("\t* " + SpecUtils.termToLatex(predList.get(i)));
            }
            }

            System.out.println("\nVariables con valores dados en la spec");
            Set<Map.Entry<RefExpr, Expr>> set2 = axDefsValues.entrySet();  
            Iterator<Map.Entry<RefExpr, Expr>> it2 = set2.iterator();
            while(it2.hasNext()){
            Map.Entry<RefExpr, Expr> entry = it2.next();
            RefExpr refExpr = entry.getKey();
            System.out.println(refExpr.getName());
            }



            System.out.println("\nVariables por predicado");
            Set<Map.Entry<Pred, List<String>>> set5 = axDefsPredVars.entrySet();
            Iterator<Map.Entry<Pred, List<String>>> it5 = set5.iterator();
            while(it5.hasNext()){
            Map.Entry<Pred, List<String>> entry = it5.next();
            Pred pred = entry.getKey();
            if(pred == null)
            System.out.println("* " + "true");
            else
            System.out.println("* " + SpecUtils.termToLatex(pred));
            List<String> varNameList = entry.getValue();
            for(int i=0; i< varNameList.size(); i++){
            System.out.println("\t* " + varNameList.get(i));
            }
            }

             */

            spec = (Spec) spec.accept(new EmptySetReplacer());
            controller.setOriginalSpec(spec);
            controller.setUnfoldedSpec(spec);
            controller.setLoadedOpsRep(opNamesRep);
            controller.setOpsToTestRep(new ConcreteRepository<String>());
            controller.setOpTTreeStrategyMap(new HashMap<String, TTreeStrategy>());
            controller.setTacticMap(new HashMap<String, List<Tactic>>());
            controller.setTypeCheckerManager(manager);
            controller.setAxDefsRequired(axDefsRequired);
            controller.setAxDefsRequiredPreds(axDefsRequiredPreds);
            controller.setAxDefsPredVars(axDefsPredVars);
            controller.setAxDefsValues(axDefsValues);
            controller.setBasicAxDefs(basicAxDefs);
            EventAdmin eventAdmin = EventAdmin.getInstance();
            SpecLoaded specLoaded = new SpecLoaded(spec);
            eventAdmin.announceEvent(specLoaded);
            output.println("Specification loaded.");
        } catch (CommandException e) {
            Throwable cause = e.getCause();
            if (cause instanceof CztErrorList) {
                output.println("Specification has not been loaded because it "
                        + "has syntactic errors.\n");
                List<CztError> errors = new ArrayList<CztError>();
                errors.addAll(((CztErrorList) cause).getErrors());
                Collections.sort(errors);

                for (int i = 0; i < errors.size(); i++) {
                    String errorStr = errors.get(i).toString();
                    int fstQuoteIndex = errorStr.indexOf("\"");
                    int sndQuoteIndex = errorStr.indexOf("\"", fstQuoteIndex + 1);
                    errorStr = errorStr.substring(0, fstQuoteIndex + 1)
                            + texFileName + errorStr.substring(sndQuoteIndex);
                    output.println(errorStr + "\n");
                }

            } else if (cause instanceof IOException) {
                String message = cause.getMessage();
                output.println(message);
            } else {
                String message = cause + getClass().getName();
                output.println(message);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (texFileToRead != null) {
                texFileToRead.delete();
            }

        }

    }

    /**
     * This method copies the content of the file, whose name is equals to the
     * first specified string, into the file whose name is equals to the second
     * specified string.
     */
    private boolean copyFile(String originalFileName, String newFileName) {
        try {
            FileWriter writer = new FileWriter(newFileName);
            PrintWriter printer = new PrintWriter(writer);
            FileReader reader = new FileReader(originalFileName);
            BufferedReader in = new BufferedReader(reader);
            String line;

            while ((line = in.readLine()) != null) {
                printer.println(line);
            }
            printer.flush();

            printer.close();
            writer.close();

            in.close();
            reader.close();
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("The file " + originalFileName + " could not be found.");
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * This method transforms the Spivey Z specification contained in the first 
     * specified file, into a Standard Z specification, which is written in the
     * second specified file. The method returns an array of integers that
     * maps line numbers of the new file to line numbers of the original file.
     */
    public int[] spiveyToStandard(File texFile, File newTexFile) {

        String texFileName = texFile.getName();
        int texFileNameLen = texFileName.length();
        String lispFileName = "temp/" + texFileName.substring(0,
                texFileNameLen - 4) + ".ztr";
        File lispFile = new File(lispFileName);
        String sortedTexFileName = "temp/" + texFileName;
        File sortedTexFile = new File(sortedTexFileName);

        try {

            // The lisp output from the original .tex file is produced
            boolean lispGenRep = generateLispOutput(texFile, lispFile);

            if (!lispGenRep) {
                output.println("Lisp output could not be generated.");
                return null;
            }

            // A sorted version of the tex file is produced
            int lineNumbersTable[] = putUsesAfterDecls(lispFile, texFile,
                    sortedTexFile);

            if (lineNumbersTable == null) {
                output.println("Errors while reordering.");
                return null;
            }

            // The sorted tex file is transformed from Spivey Z to Standard Z
            boolean sortedSpiveyToStdRep =
                    sortedSpiveyToStandard(sortedTexFile, lispFile, newTexFile);

            if (!sortedSpiveyToStdRep) {
                output.println("Errors transforming Spivey to Standard.");
                return null;
            }

            return lineNumbersTable;

        } finally {
            lispFile.delete();
            sortedTexFile.delete();
        }
    }

    /**
     * This method generates the output in lisp that fuzz produces from the first
     * specified file. That output is written in the second specified file.
     * The method also returns true if the output cannot be generated. If the
     * output cannot be generated, because of a type-checking error in the
     * specification, a list of found errors is printed.
     */
    private boolean generateLispOutput(File texFile, File lispFile) {

        String os = System.getProperties().getProperty("os.name").toLowerCase();
        String cmdScriptFileName = "";
        String cmdScriptExec = "";
        String fuzzBin = "";
        String typeCheckOutputFileName = "";
        if (os.contains("windows")){
            fuzzBin = "fuzz";
            typeCheckOutputFileName = "temp\\typeCheckReport";
            cmdScriptFileName = "temp\\runFuzzScript";
            cmdScriptExec = cmdScriptFileName;
        }
        else{
            fuzzBin = "./fuzz";   
            typeCheckOutputFileName = "temp/typeCheckReport";
            cmdScriptFileName = "temp/runFuzzScript";
            cmdScriptExec = "bash " + cmdScriptFileName;
        }
         

        try {

            File typeCheckOutputFile = new File(typeCheckOutputFileName);

            String texFilePath = texFile.getAbsolutePath();
            String lispFilePath = lispFile.getAbsolutePath();
            String typeCheckOutputFilePath = typeCheckOutputFile.getAbsolutePath();


            BufferedWriter bw = new BufferedWriter(new FileWriter(cmdScriptFileName));
            bw.write(fuzzPath + fuzzBin + " -d " + texFilePath + " 2> " + typeCheckOutputFilePath);
            bw.write("\n");
            bw.write(fuzzPath + fuzzBin + " -d -l " + texFilePath + " > " + lispFilePath);
            bw.close();
            (rt.exec(cmdScriptExec)).waitFor();

            String typeCheckOutputMsg = "";
            BufferedReader in = new BufferedReader(
                    new FileReader(typeCheckOutputFile));

            String typeCheckOutputLine;
            while ((typeCheckOutputLine = in.readLine()) != null) {
                typeCheckOutputMsg += "\n" + typeCheckOutputLine;
            }

            if (!typeCheckOutputMsg.isEmpty()) {
                output.println(typeCheckOutputMsg);
                return false;
            }

            (new File(cmdScriptFileName)).delete();
            typeCheckOutputFile.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method order the Spivey Z specification on the first specified file
     * considering the lisp output on the second specified file.
     * The new specification is written on the third specified file. The
     * method returns an array that maps new line numbers to old line numbers.
     * The first component of the array should not be considered because it is
     * related to line 0, that does not actually exist.
     */
    private int[] putUsesAfterDecls(File lispFile, File texFile,
            File newTexFile) {

        return SpiveySpecsSorter.sortLatexFile(lispFile.getAbsolutePath(),
                texFile.getAbsolutePath(),
                newTexFile.getAbsolutePath());
    }

    /**
     * This method transforms the Spivey Z specification contained in the first
     * specified file, into a Standard Z specification, which is written in the
     * third specified file. The original specification must be previously
     * sorted in a way that every declaration is before its uses. The method
     * also takes as second argument a file that contains the lisp output that
     * fuzz produces from the tex file.
     */
    private boolean sortedSpiveyToStandard(File texFile, File lispFile,
            File newTexFile) {

        String os = System.getProperties().getProperty("os.name").toLowerCase();

        String cmdScriptFileName = "";
        String cmdScriptExec = "";
        String pythonScriptFilePath = "";
        String pythonErrOutputFilePath = "";

        if (os.contains("windows")){
            cmdScriptFileName = "temp\\spivToSdtScript";
            cmdScriptExec = cmdScriptFileName;
            pythonScriptFilePath = "lib\\spiveyToStd.py";
            pythonErrOutputFilePath = "temp\\pythonErrOutputPath.txt";
        }
        else{
            cmdScriptFileName = "temp/spivToSdtScript";
            cmdScriptExec = "bash " + cmdScriptFileName;
            pythonScriptFilePath = "lib/spiveyToStd.py";
            pythonErrOutputFilePath = "temp/pythonErrOutputPath.txt";
        }        

        try {
            String texFilePath = texFile.getAbsolutePath();
            String lispFilePath = lispFile.getAbsolutePath();
            String newTexFilePath = newTexFile.getAbsolutePath();
            
            File scriptFile = new File(cmdScriptFileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(scriptFile));
            String bashLine = "python " + pythonScriptFilePath + " " + texFilePath
                    + " " + lispFilePath + " " + newTexFilePath + " 2> " +
                    pythonErrOutputFilePath;
            bw.write(bashLine);
            bw.close();
            (rt.exec(cmdScriptExec)).waitFor();


            File pythonErrOutputFile = new File(pythonErrOutputFilePath);

            String pythonErrOutputMsg = "";
            BufferedReader in = new BufferedReader(
                    new FileReader(pythonErrOutputFile));

            String pythonErrOutputLine;
            while ((pythonErrOutputLine = in.readLine()) != null) {
                pythonErrOutputMsg += "\n" + pythonErrOutputLine;
            }

            if (!pythonErrOutputMsg.isEmpty()) {
                output.println(pythonErrOutputMsg);
                return false;
            }

            pythonErrOutputFile.delete();
            scriptFile.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}