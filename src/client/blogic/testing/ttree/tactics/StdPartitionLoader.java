/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.blogic.testing.ttree.tactics;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.session.StringSource;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.parser.z.ParseUtils;
import net.sourceforge.czt.animation.eval.ZLive;
import net.sourceforge.czt.z.ast.AndPred;
import net.sourceforge.czt.z.ast.And;

import common.z.czt.UniqueZLive;

/**
 * Instances of this class have the functionality of parsing the configuration
 * file that contains the system standard partitions' definitions and load those
 * standard partitions as instances of StdPartition into the unique instance of
 * StdPartitionsControl in the system.
 * @author Pablo Rodriguez Monetti
 */
public class StdPartitionLoader {

    private String fileName;

    /**
     * Creates instances of StdPartitionLoader.
     * @param fileName the file where the standard partitions are defined.
     */
    public StdPartitionLoader(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Load the standard partitions from the configuration file into the unique
     * instance of StdPartitionsControl.
     */
    public void loadStdPartitions() {

        StdPartitionsControl stdPartitionsControl =
                StdPartitionsControl.getInstance();

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            String text = "";
            while ((line = in.readLine()) != null) {
                text += line;
            }
            // We extract the standard partitions's definitions  from the file.
            String stdPartitionArr[] = text.split("end operator;");

            ZLive zLive = UniqueZLive.getInstance();
            for (int i = 0; i < stdPartitionArr.length; i++) {
                String stdDefinition = stdPartitionArr[i];
                StdPartition stdPartition = new StdPartition();
                stdPartition.setDefinition(stdDefinition);

                // We separate the i-th standard partition definition  in lines
                String stdPartitionLines[] = stdDefinition.split(";");
                /*
                for(int j=0; j< stdPartitionLines.length; j++){       
                System.out.println("Linea " + i + " " + stdPartitionLines[j]);
                }
                 */
                String stsPartitionHeader = stdPartitionLines[0].trim();

                // We extract the operator from the first line of the definition
                int endOperatorIndex = stsPartitionHeader.lastIndexOf(":");
                String operatorStr = stsPartitionHeader.substring(0,
                        endOperatorIndex).trim();
                //System.out.println("'" + operatorStr + "'");          
                stdPartition.setOperator(operatorStr);

                // We extract the formal parameters from the definition
                int firstParamIndex = stsPartitionHeader.lastIndexOf("(") + 1;
                String paramStr = stsPartitionHeader.substring(firstParamIndex,
                        stsPartitionHeader.length() - 1);
                String formalParamArr[] = paramStr.split(",");
                List<String> formalParamList = new ArrayList<String>();
                for (int j = 0; j < formalParamArr.length; j++) {
                    //System.out.println("'" + formalParamArr[j] + "'");
                    formalParamList.add(formalParamArr[j]);
                }
                stdPartition.setFormalParamList(formalParamList);

                List<Pred> predList = new ArrayList<Pred>();
                for (int j = 1; j < stdPartitionLines.length; j++) {
                    String predStrArr[] = stdPartitionLines[j].split(",");
                    String predStr = predStrArr[0];
                    for (int k = 1; k < predStrArr.length; k++) {
                        predStr += " \\land " + predStrArr[k].trim();
                    }

                    // We convert the expression that contains the operator from
                    // Latex to AST
                    Pred parsedPred = (Pred) ParseUtils.parsePred(
                            new StringSource(predStr), zLive.getCurrentSection(),
                            zLive.getSectionManager());
                    if (parsedPred instanceof AndPred) {
                        AndPred andPred = (AndPred) parsedPred;
                        andPred.setAnd(And.NL);
                        predList.add(andPred);
                    } else {
                        predList.add(parsedPred);
                    }
                }
                stdPartition.setPredList(predList);
                stdPartitionsControl.addElement(stdPartition);
            }

        } catch (FileNotFoundException e) {
            System.out.println("The standard partitions' configuration file "
                    + "could not be found.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("IOException");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
