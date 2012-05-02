/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.tcasestrategyparsers;

import compserver.tcasegen.fm.intfm.*;

/**
 * An instance of this class performs the parsing of the parameters passed to the command
 * that select the complete strategy of test cases generation, in the case a finite model
 * generator for the integer type is specified.
 * @author Pablo Rodriguez Monetti
 */
public interface IntFiniteModelParser {
    
    /**
     * 
     * @param argv
     * @param intFiniteModel
     * @return
     */
    public int parse(String[] argv, IntFiniteModel intFiniteModel);

}
