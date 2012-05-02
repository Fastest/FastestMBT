/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.tcasestrategyparsers;

import compserver.tcasegen.fm.natfm.NatFiniteModel;

/**
 * An instance of this class allow the parsing of the received parameters when the command
 * that selects the strategy of test cases generation is used and in the case the finite model
 * generator for the natural type is the one that forms the finite model with the a specified
 * number of values greater than zero.
 * @author Pablo Rodriguez Monetti
 */
public class ZeroNatFiniteModelParser implements NatFiniteModelParser{

    /**
     * Parse the received parameters and sets, according to them, the specified generator 
     * of finite models for the type of naturals.. 
     * @param argv
     * @param natFiniteModel
     * @return
     */
    public int parse(String[] argv, NatFiniteModel natFiniteModel){
        return 0;
    }  
    
}  