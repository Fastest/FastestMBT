package client.blogic.management;

import java.util.*;
import java.io.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.parser.z.ParseUtils;
import client.blogic.testing.ttree.TClassNode;
import common.z.czt.visitors.ContainsTermVerifier;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.Pred;
import common.z.TClass;
import client.blogic.testing.ttree.visitors.TClassLeavesFinder;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;

import net.sourceforge.czt.session.*;
import net.sourceforge.czt.animation.eval.*;

import net.sourceforge.czt.typecheck.z.TypeCheckUtils;
import net.sourceforge.czt.typecheck.z.ErrorAnn;
import net.sourceforge.czt.session.SectionManager;
import net.sourceforge.czt.z.ast.RefExpr;

import common.z.czt.UniqueZLive;
import common.z.UtilSymbols;
import common.z.SpecUtils;
public class Checker
{
	public Checker(Controller controller)
	{
		this.controller = controller;
		termList = new ArrayList<Term>();
	}
	public List<Term> checkValidExpression(String tClassName, String[] args)
	{
		ZLive zLive = UniqueZLive.getInstance();
		SectionManager manager = zLive.getSectionManager();

		// Este texto deberia ser reemplazado por la llamada al metodo de PruneUtils
		Map<String, TClassNode> opTTreeMap = controller.getOpTTreeMap();
            	Set<Map.Entry<String, TClassNode>> set = opTTreeMap.entrySet();
            	Iterator<Map.Entry<String, TClassNode>> iterator = set.iterator();
            	TClass tClassFounded = null;
            	while(iterator.hasNext()  && tClassFounded == null){
                	Map.Entry<String, TClassNode> mapEntry = iterator.next();
                	TClassNode opTTreeRoot = mapEntry.getValue();
			AbstractRepository<TClass> tClassLeaves = opTTreeRoot.acceptVisitor(new TClassLeavesFinder());
			AbstractIterator<TClass> tClassIt = tClassLeaves.createIterator();
			while(tClassIt.hasNext() && tClassFounded == null){
		                    TClass tClass = tClassIt.next();
                		    String auxTClassName = tClass.getSchName();
                    			if(auxTClassName.equals(tClassName))
                        			tClassFounded = tClass;
			}
            	}
            if(tClassFounded == null){
                System.out.println("The specified test class "+tClassName+" could not be found.");
                return null;
            }else
		{
			AxPara axPara = tClassFounded.getMyAxPara();
			try
			{			
			manager.put(new Key(tClassName, AxPara.class), axPara);
			axPara = (AxPara) manager.get(new Key(tClassName, AxPara.class)); 

			Pred tClassPred = SpecUtils.getAxParaPred(axPara);
			System.out.println("El predicado:\n"+SpecUtils.termToLatex(tClassPred));
			TextUI textUI = new TextUI(zLive, new PrintWriter(System.out, true));

			String param;
			for(int i=0;i<args.length;i++)
			{
				param = args[i];
				//StringSource src = new StringSource(param);
				//src.setMarkup(Markup.LATEX);
				//Term parsedExpr = ParseUtils.parseExpr(src, "standard\\_toolkit", zLive.getSectionManager());
				Term parsedExpr = ParseUtils.parseExpr(new StringSource(param),zLive.getCurrentSection(), zLive.getSectionManager());

				if((Expr)parsedExpr instanceof RefExpr)
				{
					RefExpr ref = (RefExpr) parsedExpr;
					String refExprStr = ref.getZName().toString();
					boolean isReserved = false;
					for(int j=0; j< UtilSymbols.getNumOfSymbols();j++)
				           if(refExprStr.equals(UtilSymbols.getSymbol(j)))
               					isReserved = true;       
       				if(isReserved)
					System.out.println("Reservada");
				else
					System.out.println("Es una RefExpr corriente!!!");
				} 
				Boolean b = tClassPred.accept(new ContainsTermVerifier(parsedExpr));
				boolean result = b.booleanValue();
				if(!result)
				{
					System.out.println("The specified expression (" + param + ") is "+"not contained in the predicate of the operation's " + "schema.");
					//return null;
					List<? extends ErrorAnn> errors = TypeCheckUtils.typecheck(parsedExpr, zLive.getSectionManager(),false,zLive.getCurrentSection());


					if(errors.size() >0){
						System.out.println("The expr "+param+" did not pass the typechecking stage\n");
						System.out.println(errors);
						//return null;
					}
					else{         
						//CztPrinter.printPred(newPred,0);
						System.out.println("Funca perfect!!!");
						termList.add(parsedExpr);
					}
				}
				else
				{
					System.out.println("Found expression "+param);
					System.out.println(textUI.printTerm(parsedExpr, Markup.LATEX));
					List<? extends ErrorAnn> errors = TypeCheckUtils.typecheck(parsedExpr, zLive.getSectionManager(),false,zLive.getCurrentSection());
					//zLive.typecheck(parsedExpr);
					if(errors.size() >0){
						System.out.println("The expr "+param+" did not pass the typechecking stage!\n");
						System.out.println(errors);
						//return null;
					}
					else{         
						//CztPrinter.printPred(newPred,0);
						System.out.println("Funca!!!");
						termList.add(parsedExpr);
					}
				}
				
			}
				return termList;
			}
			catch(EvalException e)
			{
				System.out.println("Error de tipos!!!");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		return termList;
		}
		
	}
	private Controller controller;
	private List<Term> termList;
}

/*
NOTAS
Modularizar un poco.
Chequear la jerarquia de herencias entre Expr y Term
*/