package client.presentation;

import java.io.*;
import java.util.*;
import java.net.URL;

import client.blogic.management.ii.EventAdmin;
import client.presentation.commands.Command;
import client.blogic.management.communic.CServersConfigLoader;
import compserver.prunning.TheoremsLoader;
import compserver.prunning.rewriting.rwrules.RWRulesLoader;
import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.FileNameCompletor;
import jline.SimpleCompletor;

public class ClientTextUI extends ClientUI {

    /** The current output */
    protected PrintWriter output_;
    protected BufferedReader input_;
    private ConsoleReader reader;

    /** Constructs a new Fastest textual user interface.
     */
    public ClientTextUI(BufferedReader input, PrintWriter output) {
        try {
            // The error output is closed; if you want to see the messages 
            //printed by exceptions, comment the following line.
            System.err.close();
            URL url = ClientTextUI.class.getResource("ClientTextUI.class");
            String urlStr = url.toString();
            String currentDir = urlStr.substring(9, urlStr.indexOf("fastest.jar"));
            EventAdmin.getInstance(this);
            (new CServersConfigLoader(currentDir + "lib/conf/cserversinfo.conf")).loadCServersInfo();
            (new RWRulesLoader(currentDir + "lib/conf/rwRules.tex")).loadRWRules();
            (new TheoremsLoader(currentDir + "lib/conf/elimTheorems.tex",
                    currentDir + "lib/conf/thmoperators.conf")).loadTheorems();

            reader = new ConsoleReader();
            List completors = new LinkedList();

            String commands[] = obtainCommands();
            completors.add(new SimpleCompletor(commands));
            completors.add(new FileNameCompletor());
            reader.addCompletor(new ArgumentCompletor(completors));
        } catch (IllegalAccessException e) {
            e.printStackTrace(System.out);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.exit(0);
        }

        if (output == null) {
            output_ = new PrintWriter(System.out, true); // with autoflush
        } else {
            output_ = output;
        }
        if (input == null) {
            input_ = new BufferedReader(new InputStreamReader(System.in));
        } else {
            input_ = input;
        }
    }

    /**
     * Set the current output writer.
     * @param output
     */
    public void setOutput(/*@non_null*/PrintWriter output) {
        output_ = output;
    }

    /**
     * Get the current output writer.
     * @return
     */
    public PrintWriter getOutput() {
        return output_;
    }

    /**
     * Set the current input reader.
     * @param input
     */
    public void setInput(/*@non_null*/BufferedReader input) {
        input_ = input;
    }

    /**
     * Get the current input reader.
     * @return
     */
    public BufferedReader getInput() {
        return input_;
    }

    /**
     * The main read-process loop.
     * @return
     * @throws java.io.IOException
     */
    public boolean readAction()
            throws IOException {

        String line = reader.readLine("Fastest> ");

        if (line != null) {
            line = line.trim();
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                return false;
            }

            if (line.equals("")) {
                return true;
            }

            String parts[] = line.split(" +", 2);
            processCmd(parts[0], parts.length > 1 ? parts[1] : "");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Process one input command and write output to writer.
     * @param cmd
     * @param args
     */
    private void processCmd(String cmd, String args) //throws IOException
    {
        String orderClassName = "unknown";
        Properties props = new Properties();
        try {
            URL url = Command.class.getResource("commands.properties");
            if (url != null) {
                props.load(url.openStream());
                orderClassName = (String) props.get(cmd);
                if (orderClassName != null) {

                    Class orderClass = Class.forName(orderClassName);
                    Command command = (Command) orderClass.newInstance();
                    command.run(this, args);
                } else {
                    System.out.println("Invalid command.  Try 'help'.");
                }
            } else {
                output_.println("commands.properties configuration file "
                        + "could not be found.");
            }
            output_.flush();
        } catch (ClassNotFoundException e) {
            output_.println("Error: The class " + orderClassName
                    + " could not be found.");
        } catch (InstantiationException e) {
            output_.println("Error: The class " + orderClassName
                    + " could not be instantiated.");
        } catch (IllegalAccessException e) {
            output_.println("Error: There was an IllegalAccessException"
                    + " while trying to instantiate the ");
            output_.print(orderClassName + " class.");
        } catch (IOException e) {
            output_.println("Error: There was an IOException while trying "
                    + "to loading the inpurorders properties. ");
        }
    }

    /**
     * Main entry point, which runs Fastest with System.in and System.out.
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String args[])
            throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter output = new PrintWriter(System.out, true); // with autoflush
        output.println(getBanner());

        ClientTextUI ui = new ClientTextUI(input, output);
        while (true) {
            if (!ui.readAction()) {
                break;
            }
        }
    }

    /**
     * Returns the name and current version of Fastest.
     * @return
     */
    public static String getBanner() {
        return "Fastest version " + getVersion() + ", (C) 2011, Flowgate "
                + "Consulting";
    }

    /**
     * Returns the version number as a String, or "unknown" if an error
     * occured when accessing the property containing the version
     * information.
     */
    public static String getVersion() {
        String version = "unknown";
        try {
            Properties props = new Properties();
            URL url = ClientTextUI.class.getResource("clienttextui.properties");
            if (url != null) {
                props.load(url.openStream());
                version = (String) props.get("version");
            }
        } catch (IOException e) {
        }
        return version;
    }

    /**
     * Returns the array with the names of the available commands.
     * @return the array with the names of the available commands.
     */
    private String[] obtainCommands() {
        String commands[] = {};
        Properties props = new Properties();
        try {
            URL url = Command.class.getResource("commands.properties");
            if (url != null) {
                props.load(url.openStream());
                Set<String> nameSet = props.stringPropertyNames();
                commands = new String[nameSet.size() + 1];
                Iterator<String> namesIt = nameSet.iterator();
                int i = 0;
                while (namesIt.hasNext()) {
                    String commandName = namesIt.next();
                    commands[i] = commandName;
                    i++;
                }
                commands[nameSet.size()] = "quit";
            }
        } catch (Exception e) {
        }
        return commands;
    }
}
