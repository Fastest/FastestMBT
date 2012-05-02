package common.z.czt.visitors;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.visitor.TermVisitor;
import net.sourceforge.czt.base.visitor.VisitorUtils;
import net.sourceforge.czt.print.ast.*;
import net.sourceforge.czt.util.CztLogger;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.util.Factory;
import net.sourceforge.czt.z.util.OperatorName;
import net.sourceforge.czt.z.visitor.*;
import net.sourceforge.czt.print.z.PrecedenceVisitor;
import common.z.SpecUtils;
import net.sourceforge.czt.parser.util.OpTable;

public class PrecedenceParenAnnVisitor
  implements TermVisitor<Term>,
             PredVisitor<Term>,
             ExprVisitor<Term>,
             ProdExprVisitor<Term>,
             ApplicationVisitor<Term>,
             OperatorApplicationVisitor<Term>
{
  /**
   * A factory used to create the parenthesis annotations.
   */
  private Factory factory_ = new Factory();
  private OpTable opTable;

	public PrecedenceParenAnnVisitor(OpTable opTable){
		this.opTable = opTable;
	}
  public Term visitTerm(Term term)
  {
	System.out.println("Entra a Term");
    VisitorUtils.visitTerm(this, term);
	System.out.println("Sale del Term");
    return term;
  }

  public Term visitPred(Pred term)
  {
	System.out.println("Entra a Pred");
    VisitorUtils.visitTerm(this, term);
    preservePrecedence(term);
	System.out.println("Luego del preserve en Pred");
    return term;
  }

  public Term visitExpr(Expr term)
  {
	System.out.println("Entra a Expr");
    VisitorUtils.visitTerm(this, term);
    preservePrecedence(term);
	System.out.println("Luego del preserve en Expr");
    return term;
  }

  protected void preservePrecedence(Term term)
  {
	System.out.println("Entra a preserve");
    Precedence prec = precedence(term);
    if (prec != null) {
	System.out.println("Preserve no nulo");
      Object[] children = term.getChildren();
      for (int i = 0; i < children.length; i++) {
        Object child = children[i];
        if (child instanceof List) {
          List list = (List) child;
          for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object elem = iter.next();
            addParenAnnIfNecessary(elem, prec);
          }
        }
        else if (child instanceof Term) {
          addParenAnnIfNecessary((Term) child, prec);
        }
      }
    }
	System.out.println("Preserve nulo");
  }

  public Term visitApplication(Application appl)
  {
	System.out.println("Entra a Application");
    VisitorUtils.visitTerm(this, appl);
    preservePrecedence(appl);
    Expr rightExpr = appl.getRightExpr();
    if (rightExpr instanceof Application) {
      addParenAnn(rightExpr);
    }
    return appl;
  }

  protected boolean isInfix(OperatorName opName)
  {
    if (opName == null) return false;
    return OperatorName.Fixity.INFIX.equals(opName.getFixity());
  }

  public Term visitOperatorApplication(OperatorApplication appl)
  {
	System.out.println("Entra a Operator");
    VisitorUtils.visitTerm(this, appl);
    preservePrecedence(appl);
    OperatorName opName = appl.getOperatorName();
    if (isInfix(opName)) {
      Assoc assoc = appl.getAssoc();
      if (assoc == null) {
        String message = "Cannot find associativity for '" + opName
          + "'; assume leftassoc";
        CztLogger.getLogger(PrecedenceParenAnnVisitor.class).warning(message);
        assoc = Assoc.Left;
      }
      if (Assoc.Right.equals(assoc)) {
        Object firstArg = appl.getArgs().get(0);
        if (firstArg instanceof OperatorApplication) {
          OperatorApplication childApp = (OperatorApplication) firstArg;
          if (isInfix(childApp.getOperatorName())) {
            addParenAnn(childApp);
          }
        }
      }
      else {
        Object lastArg = appl.getArgs().get(appl.getArgs().size() - 1);
        if (lastArg instanceof OperatorApplication) {
          OperatorApplication childApp = (OperatorApplication) lastArg;
          if (isInfix(childApp.getOperatorName())) {
            addParenAnn(childApp);
          }
        }
      }
    }
    return appl;
  }

  public Term visitProdExpr(ProdExpr prodExpr)
  {
	System.out.println("Entra a ProdExpr");
    VisitorUtils.visitTerm(this, prodExpr);
    preservePrecedence(prodExpr);
    for (Iterator<Expr> iter = prodExpr.getZExprList().iterator();
         iter.hasNext(); ) {
      Expr child = iter.next();
      if (child instanceof ProdExpr) {
        addParenAnn((ProdExpr) child);
      }
    }
    return prodExpr;
  }

  /**
   * Adds parenthesis annotations to the given object
   * if it is an annotable term and
   * the precedence of the parent is greater than
   * the precedence of the given term.
   *
   * @param object the term to which annotations are added, if necesasry.
   * @param parentPrec the precedence of the parent.
   */
  protected void addParenAnnIfNecessary(Object object, Precedence parentPrec)
  {
	System.out.println("Entra a AddIfNecessary");
    if (object instanceof Term) {
      addParenAnnIfNecessary((Term) object, parentPrec);
    }
  }

  /**
   * Adds parenthesis annotations to the given term
   * if the precedence of the parent is greater than
   * the precedence of the given term.
   *
   * @param term the term to which annotations are added, if necessary.
   * @param parentPrec the precedence of the parent.
   */
  protected void addParenAnnIfNecessary(Term term, Precedence parentPrec)
  {
	System.out.println("Entra a AddIfNecessary2");
    Precedence prec = precedence(term);
    if (prec != null && parentPrec.compareTo(prec) > 0) {
      addParenAnn(term);
    }
  }

  protected void addParenAnn(Term term)
  {
	System.out.println("Entra a AddParenAnn");
    term.getAnns().add(factory_.createParenAnn());
  }

  public Precedence precedence(Term term)
  {
	System.out.println("Entra a Precedence");
	if(term!=null)
		System.out.println("No nulo "+SpecUtils.termToLatex(term));
	else
		System.out.println("Nulo");
	System.out.println("Antes del visitante");
	Precedence p = null;
	try{
	p = term.accept(new PrecedenceVisitor(opTable));
	}
	catch(Exception e){System.out.println("Exception!!!"+e);}
	System.out.println("Despues del visitante");
    return p;
  }
}
