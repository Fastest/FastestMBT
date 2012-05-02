package common.z.czt.visitors;

import net.sourceforge.czt.base.ast.*;
import net.sourceforge.czt.base.visitor.*;
import net.sourceforge.czt.z.ast.Ann;
import net.sourceforge.czt.z.ast.ParenAnn;
import java.util.*;

/**
 * A visitor that deletes all ParenAnn from an AST.
 */
public class DeleteParenAnn
  implements TermVisitor
{
  public Object visitTerm(Term term)
  {
    VisitorUtils.visitTerm(this, term);
    ParenAnn parenAnn = term.getAnn(ParenAnn.class);
    List<Object> listAnns = term.getAnns();
    boolean removed = listAnns.remove(parenAnn);
    return term;
  }
}