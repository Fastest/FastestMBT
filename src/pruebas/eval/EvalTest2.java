package pruebas.eval;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.animation.eval.*;
import net.sourceforge.czt.typecheck.z.*;

// Evalua 60000 veces un mismo predicado
public class EvalTest2{
	public static void main(String[] args) {	

 		ZLive zLive = new ZLive();

		try{
 			TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
			Pred pred = (Pred) textUI.parseTerm("\\negate 1 \\in \\dom \\" +
                    "{(\\negate 1, 0), (2,3) \\}", new PrintWriter(System.out, true));
            Calendar cal = Calendar.getInstance();
            long inicial = cal.getTimeInMillis();
            for(int i=0; i<50; i++){
				System.out.println( "Iteración: " + i) ;
				zLive.evalPred(pred);
			}
            Calendar cal2 = Calendar.getInstance();
            long finale = cal2.getTimeInMillis();
            System.out.println("Demora: " + (finale - inicial) );

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
} 
