/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.z;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The aim of this program is to order Spivey Z specifications to have decls
 * before their uses. The program expects two files, a Lisp and a Latex file.
 * The Latex file should contain a Spivey specification while the Lisp file
 * should contain the Lisp output that Fuzz produces from the Latex file.
 * If the name of the Latex file is of the form a.tex, the sorted specification
 * is written on a file called a_sorted.tex.
 * This program also prints, on standard output, a table that maps line numbers
 * of the new file to line numbers of the original one. The line numbers of the
 * new file that have not an associated line number in the original file are
 * mapped to -1.
 * @author Pablo Rodriguez Monetti
 */
public class SpiveySpecsSorter {

    public static void main(String args[])
            throws IOException {

        if (args.length != 2) {
            System.out.println("This program expects two parameters, a Lisp "
                    + "file name followed by a Latex file name.");
            return;
        }

        String lispFileName = args[0];
        String latexFileName = args[1];
        int latexFileNameLen = latexFileName.length();
        String newlatexFileName = latexFileName.substring(0, latexFileNameLen - 4)
                + "_sorted.tex";

        int lineTable[] = sortLatexFile(lispFileName, latexFileName, newlatexFileName);


        if (lineTable != null) {
            System.out.println("New Line Number\t\tOriginal Line Number");
            for (int i = 1; i < lineTable.length; i++) {
                System.out.println(i + "\t\t\t" + lineTable[i]);

            }
        }

    }

    /**
     * This method order the Spivey Z specification on the file called
     * latexFileName considering the lisp output on the file called lispFileName.
     * The new specification is written on the file called newLatexFileName. The
     * method returns an array that maps new line numbers to old line numbers.
     * The first component of the array should not be considered because it is
     * related to line 0, that does not actually exist.
     */
    public static int[] sortLatexFile(String lispFileName,
            String latexFileName, String newlatexFileName) {

        // The line numbers of the Z paragraphs are extracted from the lisp file
        int lispLineNumbers[] = getLispLineNumbers(lispFileName);

        if (lispLineNumbers == null) {
            return null;
        }

        List<String> latexLines = new ArrayList<String>();
        File newLatexFile = new File(newlatexFileName);

        try {
            // The Latex file is read
            BufferedReader latexReader =
                    new BufferedReader(new FileReader(latexFileName));
            String readLine;

            while ((readLine = latexReader.readLine()) != null) {
                latexLines.add(readLine);
            }

            latexReader.close();

            PrintWriter printer =
                    new PrintWriter(new FileWriter(newLatexFile));


            List<Integer> lineTableList = new ArrayList<Integer>();

            // The line number 0 does not have an associated line number
            lineTableList.add(new Integer(-1));


            for (int i = 0; i < lispLineNumbers.length; i++) {

                int paraPointer = lispLineNumbers[i];

                String paraPointerLine = latexLines.get(paraPointer);

                boolean paraEndFound = false;

                if (paraPointerLine.contains("\\begin")) {
                    int j = paraPointer;
                    while (j < latexLines.size() && !paraEndFound) {
                        String line = latexLines.get(j);
                        printer.println(line);
                        lineTableList.add(new Integer(j + 1));
                        if (line.contains("\\end")) {
                            paraEndFound = true;
                        }
                        j++;
                    }

                } else {
                    // This paragraph is located inside a \begin{zed} \end{zed}
                    // environment
                    printer.println("\\begin{zed}");
                    lineTableList.add(new Integer(-1));
                    int j = paraPointer;
                    // nextParaPointer points to the begining of the next Z
                    // paragraph
                    int nextParaPointer = getNextIntValue(lispLineNumbers,
                            paraPointer);
                    // We print the Latex lines until either the end of this
                    // paragraph or the end of the file is reached
                    while (j < latexLines.size() && !paraEndFound) {
                        String line = latexLines.get(j);
                        // The end of the paragraph is at the first line that
                        // verifies either that it contains "\end" of its line
                        // number is equal to nextParaPointer
                        if (line.contains("\\end") || j == nextParaPointer) {
                            paraEndFound = true;
                        } else {
                            printer.println(line);
                            lineTableList.add(new Integer(j + 1));
                        }
                        j++;
                    }
                    printer.println("\\end{zed}");
                    lineTableList.add(new Integer(-1));
                }

                // If there are remaining paragraphs, an empty line is printed
                if (i < lispLineNumbers.length - 1) {
                    printer.println();
                    lineTableList.add(new Integer(-1));
                }


                if (!paraEndFound) {
                    System.out.println("Error reaching Latex end of file.");
                    return null;
                }
            }
            printer.flush();
            printer.close();

            int lineTableSize = lineTableList.size();
            int lineTable[] = new int[lineTableSize];
            for (int i = 0; i < lineTableSize; i++) {
                lineTable[i] = lineTableList.get(i);
            }

            return lineTable;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * This method takes the name of a file that contains the lisp output that
     * fuzz produces from a Spivey Z specification and returns an array with
     * the line numbers of the Z paragraphs listed on such specification. The
     * line numbers are all decreased in one unit in order to have the first
     * line, of the Latex file, referenced by 0.
     */
    public static int[] getLispLineNumbers(String lispFileName) {
        int lispLineNumbers[] = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(lispFileName));
            String lispLine;
            List<Integer> integerList = new ArrayList<Integer>();
            while ((lispLine = in.readLine()) != null) {
                if (lispLine.contains("(GIVEN")
                        || lispLine.contains("(AXDEF")
                        || lispLine.contains("(EQEQ")
                        || lispLine.contains("(DATA")
                        || lispLine.contains("(SDEF")
                        || lispLine.contains("(DEFEQ")
                        || lispLine.contains("(PRED")
                        || lispLine.contains("(DEFINE")) {
                    String parts[] = lispLine.split(" ");
                    String lastString = parts[parts.length - 1];

                    Integer integer = Integer.parseInt(lastString);
                    integerList.add(integer);
                }
            }
            int integerListSize = integerList.size();
            lispLineNumbers = new int[integerListSize];

            for (int i = 0; i < integerListSize; i++) {
                lispLineNumbers[i] = integerList.get(i).intValue() - 1;
            }
            return lispLineNumbers;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * This method takes an array of positive integers, arr, and an integer, n,
     * and returns the 'next value' with respect to n in arr, ie. the minumum
     * element of arr that is greater than n. If such element does not exist, -1
     * is returned.
     */
    public static int getNextIntValue(int[] arr, int n) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            int value = arr[i];
            // If value is greater than n and if either it is the first
            // element of the array greater than n or it is slower than the
            // previously found
            if (value > n && (index == -1 || value < arr[index])) {
                index = i;
            }
        }
        if (index == -1) {
            return -1;
        } else {
            return arr[index];
        }
    }
}