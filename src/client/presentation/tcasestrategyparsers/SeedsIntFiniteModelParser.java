/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.tcasestrategyparsers;

import compserver.tcasegen.fm.intfm.*;

/**
 * An instance of this class allow the parsing of the received parameters when the command
 * that selects the strategy of test cases generation is used and in the case the finite model
 * generator for the integer type is the one that takes the list of values that will form the
 * finite model, toghether with intermediate values between each pair of consecutive ones.
 * @author Pablo Rodriguez Monetti
 */
public class SeedsIntFiniteModelParser implements IntFiniteModelParser{

    /**
     * Parse the received parameters and sets, according to them, the specified generator 
     * of finite models for the type of integers.
     * @param argv
     * @param intFiniteModel
     * @return
     */
    public int parse(String[] argv, IntFiniteModel intFiniteModel){
        return 0;
    }  
    
}  