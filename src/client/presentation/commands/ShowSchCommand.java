package client.presentation.commands;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.z.ast.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import common.z.SpecUtils;
import common.z.Scheme;
import client.blogic.testing.ttree.TClassNode;
import client.blogic.testing.ttree.visitors.SchemeTTreeFinder;
import client.blogic.testing.ttree.visitors.TClassNodeTextUIPrinter;
import client.blogic.testing.ttree.visitors.TCaseNodeTextUIPrinter;
import client.blogic.testing.ttree.visitors.TTreeVisitor;

/**
 * An instance of this class allow the presentation of schemas which are in the context of
 * Fastest: operation schemas, test classes schemas and test cases schemas. It is possible
 * to print every schema individually, to print all test cases, and to print  all test cases. 
 * Printing can be done on screen or on a Latex file, that can then be compiled.
 * @author Pablo Rodriguez Monetti
 */
public class ShowSchCommand implements Command {

    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
    public void run(ClientTextUI clientTextUI, String args) {
        PrintWriter stdout = clientTextUI.getOutput();
        try {
            final String argv[] = args.split(" ");
            int argc = argv.length;

            String firstArg = "";
            if (argc > 0 && !argv[0].equals("")) {
                firstArg = argv[0];
            }

            int unfoldOrder = 0;
            PrintWriter printer = stdout;
            String opName = "";

            for (int index = 1, argCount = 1; index < argc; index += argCount) {
                if (argv[index].equals("-u")) {
                    unfoldOrder = Integer.decode(argv[index + 1]);
                    argCount = 2;
                } else if (argv[index].equals("-p")) {
                    if (!firstArg.equals("-tca") && !firstArg.equals("-tcl")) {
                        stdout.println("Invalid parameters.  Try 'help'.");
                        return;
                    }
                    opName = argv[index + 1];
                    argCount = 2;
                } else if (argv[index].equals("-o")) {
                    String fileName = argv[index + 1];
                    printer = new PrintWriter(new FileWriter(fileName));
                    String latexPreface = "\\documentclass{article}\n";
                    latexPreface += "\\usepackage{czt}\n";
                    latexPreface += "\\newenvironment{machine}[1]{\n";
                    latexPreface += "\\begin{tabular}{@{\\qquad}l}";
                    latexPreface += "\\textbf{\\kern-1em machine}\\ #1\\\\ }{\n";
                    latexPreface += "\\\\ \\textbf{\\kern-1em end} ";
                    latexPreface += "\\end{tabular} }\n";
                    latexPreface += "\\begin{document}\n";
                    printer.println(latexPreface);
                    printer.flush();
                    argCount = 2;
                } else {
                    stdout.println("Invalid parameters.  Try 'help'.");
                    return;
                }
            }

            Controller controller = clientTextUI.getMyController();
            Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();

            TTreeVisitor<Void> tTreeVisitor;
            if (firstArg.equals("-tcl")) {
                // Se van a imprimir clases de prueba
                tTreeVisitor = new TClassNodeTextUIPrinter(printer, unfoldOrder);
                printTTSchemes(tTreeVisitor, opTTreeMap, opName);
            } else if (firstArg.equals("-tca")) {
                // Se van a imprimir casos de prueba     
                tTreeVisitor = new TCaseNodeTextUIPrinter(printer, unfoldOrder);
                printTTSchemes(tTreeVisitor, opTTreeMap, opName);
            } else {
                // Se va a imprimir un esquema
                String schName = firstArg;

                // Vemos si corresponde a un esquema de operacion
                Spec spec = null;
                if (unfoldOrder > 0 || unfoldOrder == -1) {
                    spec = controller.getUnfoldedSpec();
                } else {
                    spec = controller.getOriginalSpec();
                }


                if (spec == null) {
                    stdout.println("There is not any loaded specification.");
                    return;
                }


                AxPara axPara = null;
                for (Sect sect : spec.getSect()) {
                    if (sect instanceof ZSect) {
                        ParaList paraList = ((ZSect) sect).getParaList();
                        if (paraList instanceof ZParaList) {
                            axPara = SpecUtils.axParaSearch(schName, (ZParaList) paraList);
                        }
                    }
                }

                if (axPara == null) {
                    // No es el esquema de una operacion
                    // Vemos si corresponde al de un caso de prueba o a una clase de prueba
                    Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
                    Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();

                    Scheme scheme = null;
                    while (iterator.hasNext() && scheme == null) {
                        Map.Entry<String, TClassNode> mapEntry = iterator.next();
                        opName = mapEntry.getKey();
                        TClassNode opTTreeRoot = mapEntry.getValue();

                        scheme = opTTreeRoot.acceptVisitor(new SchemeTTreeFinder(schName, unfoldOrder));
                    }

                    if (scheme == null) {
                        stdout.println("'" + schName + "' is not the name of any schema.");
                        return;
                    } else {
                        axPara = scheme.getMyAxPara();
                    }
                }
                // Imprimimos el esquema
                printer.println("\n" + SpecUtils.termToLatex(axPara));
                printer.flush();
            }

            // Si se imprimio en un archivo, imprimimos la ultima linea del latex
            if (printer != stdout) {
                printer.println("\\end{document}\n");
                printer.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTTSchemes(TTreeVisitor<Void> tTreeVisitor, Map<String, TClassNode> opTTreeMap, String opName) {
        if (opName.equals("")) {
            // Imprimos todos los casos de prueba o clases de prueba (de acuerdo al tTreeVisitor)
            // de todas las operaciones

            Collection<TClassNode> opTTreeCollection = opTTreeMap.values();
            Iterator<TClassNode> tTreeIt = opTTreeCollection.iterator();
            while (tTreeIt.hasNext()) {
                TClassNode opTTreeRoot = tTreeIt.next();
                opTTreeRoot.acceptVisitor(tTreeVisitor);
            }

        } else {
            // Imprimos todos los casos de prueba o clases de prueba (de acuerdo al tTreeVisitor)
            // de la operacion cuyo nombre es opName
            TClassNode opTTreeRoot = opTTreeMap.get(opName);
            if (opTTreeRoot != null) {
                opTTreeRoot.acceptVisitor(tTreeVisitor);
            }

        }
    }
}
