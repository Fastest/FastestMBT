/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.tcasestrategyparsers;

import compserver.tcasegen.strategies.*;
import common.z.TClass;
import client.presentation.ClientTextUI;


/**
 * Interface that abstract a parser of the received parameters when the command that selects
 * the strategy of test case generation is used.
 * @author Pablo Rodriguez Monetti
 */
public interface TCaseStrategyParser {
    /**
     * Parse the received parameters and sets, according to them, the strategy of test cases
     * generation.
     * @param str
     * @param tcs
     * @return
     */
    public boolean parse(TClass tClass, String str, TCaseStrategy tcs);
    public void setClientTextUI(ClientTextUI c);
    public ClientTextUI getClientTextUI();
    
}
