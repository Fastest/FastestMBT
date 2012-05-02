/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.blogic.testing.ttree.tactics;
import java.util.*;


/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class NRTacticInfo extends TacticInfo{


    private String var;
    private List<Integer> numbers;



    public void setVar(String var){
        this.var = var;
    }


    public String getVar(){
        return var;
    }


    public void setNumbers(List<Integer> numbers){
        this.numbers = numbers;
    }

    public List<Integer> getNumbers(){
        return numbers;
    }



}
