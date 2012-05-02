/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas.pred;

import java.io.*;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.animation.eval.*;

import common.z.SpecUtils;


/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class SimplifTest {
    
    
    
     public static void main(String[] args) {	

 		ZLive zLive = new ZLive();

		try{
 			TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
			Pred pred = (Pred) textUI.parseTerm("((1>0 \\lor 5>3) \\land 2=4) \\land 1>0", new PrintWriter(System.out, true));

            System.out.println("Predicado original:");
            System.out.println(SpecUtils.termToLatex(pred));
            
            pred = SpecUtils.simplifyAndPred(pred);
            
            System.out.println("\nPredicado simplificado:");
            System.out.println(SpecUtils.termToLatex(pred));

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
    
    
    
    
    
    
    
    
    
    

}
