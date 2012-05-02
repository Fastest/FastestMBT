/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;

import net.sourceforge.czt.z.ast.Freetype;


/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class FTTacticInfo extends TacticInfo{
    
    
    private String var;
    private Freetype freetype;
    
            
    public void setVar(String var){
        this.var = var;        
    }
    
    
    public String getVar(){
        return var;
    }

    public void setType(Freetype freetype){
        this.freetype = freetype;
    }

    public Freetype getType(){
        return freetype;
    }    
    
}
