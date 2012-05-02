package pruebas.eval;

import java.io.*;
import java.util.*;

import net.sourceforge.czt.z.ast.Pred;
import net.sourceforge.czt.animation.eval.*;
import net.sourceforge.czt.typecheck.z.*;

// Evalua 500 veces en 12 threads un mismo predicado.
// Si no se amplia la memoria de la JVM, termina con una excepcion dado el gran consumo del objeto ZLive
// o en un crash de la JVM, por problemas también de memoria o bien por la caracteristica no thread-safe de 
// esta clase.

public class EvalTest{
	public static void main(String[] args) {	

		for(int i=0; i<12; i++){
			(new ThreadTest(i)).start();
		}
	}
} 


class ThreadTest extends Thread{

	private int threadNumber;
 	private ZLive zLive = new ZLive();

	public ThreadTest(int threadNumber){
		this.threadNumber = threadNumber;
	}

    @Override
	public void run(){
		try{
 			TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));
/*
			String string = "0 \\in \\dom \\{ ( 1 , \\negate 1 ) , ( 2 , \\negate 1 ) \\} \\land 1 > 0 \\land 1";
			string += " \\leq \\{ ( 1 , \\negate 1 ) , ( 2 , \\negate 1 ) \\} 1 \\land";
			string += "\\{ ( 1 , \\negate 1 ) , ( 2 , \\negate 1 ) \\} 0 > 10000";
*/

			String string = "\\langle 1	\\rangle = 	\\langle 1	\\rangle";

			Pred pred = (Pred) textUI.parseTerm(string, new PrintWriter(System.out, true));
			List<? extends ErrorAnn> errors = 
				TypeCheckUtils.typecheck(pred, zLive.getSectionManager(), false, zLive.getCurrentSection());
 			if(errors.size() >0)
				return;


			for(int i=0; i<500; i++){
				System.out.println("Thread: " + threadNumber + " Iteración: " + i) ;
				zLive.evalPred(pred);
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}