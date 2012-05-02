/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.tcasestrategyparsers;

import compserver.tcasegen.fm.natfm.*;


/**
 * An instance of this class performs the parsing of the parameters passed to the command
 * that selects the complete strategy of test cases generation, in the case a finite model
 * generator for the natural type is specified.
 * @author Pablo Rodriguez Monetti
 */
public interface NatFiniteModelParser {
    
    public int parse(String[] argv, NatFiniteModel natFiniteModel);

}
