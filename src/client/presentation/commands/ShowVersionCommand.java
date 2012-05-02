package client.presentation.commands;

import java.io.*;

import client.presentation.ClientTextUI;


/**
 * An instance of this class allow the presentation of the version 
 * @author Pablo Rodriguez Monetti
 */
public class ShowVersionCommand implements Command{ 
    /**
     * Runs this command.
     * @param clientTextUI
     * @param args
     */
    @Override
	public void run(ClientTextUI clientTextUI, String args){
		PrintWriter out = clientTextUI.getOutput();
		out.println(ClientTextUI.getBanner());

	}
} 
