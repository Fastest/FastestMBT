package client.presentation.commands;

import java.io.*;

import net.sourceforge.czt.z.ast.*;

import client.presentation.ClientTextUI;
import client.blogic.management.Controller;
import common.z.SpecUtils;

/**
 * An instance of this class allow the presentation of the loaded specificacion,
 * if any.
 * @author Pablo Rodriguez Monetti
 */
public class ShowSpecCommand implements Command {

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

            boolean unfold = false;
            PrintWriter printer = stdout;
            for (int index = 0, argCount = 1; index < argc; index += argCount) {
                if (argv[index].equals("-u")) {
                    unfold = true;
                    argCount = 1;
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
                    argCount = 2;
                } else if (!argv[index].equals("")) {
                    stdout.println("Invalid parameters.  Try 'help'.");
                    return;
                }
            }


            Controller controller = clientTextUI.getMyController();
            Spec spec = null;

            if (unfold) {
                spec = controller.getUnfoldedSpec();
            } else {
                spec = controller.getOriginalSpec();
            }


            if (spec == null) {
                stdout.println("There is not any loaded specification.");
            } else {

                for (Sect sect : spec.getSect()) {

                    if (sect instanceof ZSect) {
                        ZSect zSect = (ZSect) sect;
                        ParaList paraList = zSect.getParaList();
                        if (paraList instanceof ZParaList) {
                            ZParaList zParaList = (ZParaList) paraList;
                            for (int i = 0; i < zParaList.size(); i++) {
                                Para para = zParaList.get(i);
                                if (!(para instanceof NarrPara ||
                                        para instanceof LatexMarkupPara)) {
                                    printer.println("\n" +
                                            SpecUtils.termToLatex(para));
                                }
                                printer.flush();
                            }
                        }

                    }
                }

                // If we have printed on a file,
                // we print the last line of the latex document
                if (printer != stdout) {
                    printer.println("\\end{document}\n");
                    printer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

