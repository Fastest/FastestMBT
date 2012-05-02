/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.presentation.tcasestrategyparsers;

import compserver.tcasegen.strategies.*;
import common.z.TClass;
import client.presentation.ClientTextUI;

/**
 * An instance of this class performs the parsing of the parameters passed to the command
 * that select the complete strategy of test cases generation.
 * @author Pablo Rodriguez Monetti
 */
public class CompleteTCaseStrategyParser implements TCaseStrategyParser{
    /**
     * Parse the received parameters and sets, according to them, the specified strategy of 
     * test cases generation.
     * @param str
     * @param tcs
     * @return
     */

    private ClientTextUI clientTextUI;

    public boolean parse(TClass tClass, String args, TCaseStrategy tcs)
        throws IllegalArgumentException{
        
        try{
            if(!(tcs instanceof CompleteTCaseStrategy))
                throw new IllegalArgumentException();
                
            final String argv[] = args.split(" ");
            int argc = argv.length;                   
            
            String fmSizeStr = "";
            if(argc > 0 && !argv[0].equals("")){
                fmSizeStr = argv[0];
            }
            
            int fMSize = Integer.parseInt(fmSizeStr);
            CompleteTCaseStrategy completeTCaseStrategy = (CompleteTCaseStrategy) tcs;
            completeTCaseStrategy.setFMSize(fMSize);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }



    public void setClientTextUI(ClientTextUI clientTextUI){
        this.clientTextUI = clientTextUI;
    }


    public ClientTextUI getClientTextUI(){
        return clientTextUI;
    }
       
}
