package client.presentation.commands;


import client.presentation.ClientTextUI;

/**
 * Interface that abstracts a text user interface's command. To add a new command to Fastest,
 * it is neccesary to define a class that implements this interface.
 * @author Pablo Rodriguez Monetti
 */
public interface Command{
    /**
     * Runs the command this interface implements.
     * @param clientTextUI
     * @param args
     */
	public void run(ClientTextUI clientTextUI, String args);

} 
