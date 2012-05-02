/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas;


import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.z.visitor.NameVisitor;
import net.sourceforge.czt.base.visitor.VisitorUtils;
/**
 *
 * @author Pablo Rodriguez Monetti
 */
public class RefExprPrinter {
    
	public static void main(String[] args) {	
		SectionManager manager = new SectionManager();
		if(args.length!=1){
			System.out.println("Ingresar nombre de archivo!!!!");
			return;
		}
		FileSource source = new FileSource(args[0]);
		manager.put(new Key(source.getName(), Source.class), source);
		try{
			Spec spec = (Spec) 
			manager.get(new Key(source.getName(), Spec.class));
            spec.accept(new RefExprPrinterVisitor());

		}
		catch(Exception e){
			System.out.println(e);
		}
	}
    
}




class RefExprPrinterVisitor	
	implements NameVisitor<Term>,
                TermVisitor<Term>
{


	public Term visitTerm(Term term){
    	Term result = VisitorUtils.visitTerm(this, term, false);
    	return result;
	}

    public Term visitName(Name name){
        String str = name.toString();
        
            System.out.print(str);
            System.out.print("\t\t\t\t");
            
            for(int i=0; i< str.length(); i++){
                 System.out.print((int) str.charAt(i) + " ");
            }
            System.out.println();
        return name;                
    }

}

