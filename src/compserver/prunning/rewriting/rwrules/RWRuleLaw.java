package compserver.prunning.rewriting.rwrules; 

import net.sourceforge.czt.z.ast.ZDeclList;
import net.sourceforge.czt.z.ast.DeclList;
import java.util.*;
import net.sourceforge.czt.z.ast.Expr;

/**
 * Interface that abstracts a rewrite rule related with mathematics laws
 */
public interface RWRuleLaw extends RWRule{
    /**
     * Gets the regular expression related with the law
     * @return the regular expression
     */
    public String getRegEx();

    /**
     * Sets the regular expression related with the law
     * @param regex the regular expression
     */
    public void setRegEx(String regex);

    /**
     * Check if an atomic predicate matches with the regular expression of the law
     * @return a boolean value with the result of the evaluation
     */
    public boolean match(String predicate);

    /**
     * Sets the definition of this rewrite rule.
     * @param definition
     */
    public void setDefinition(String definition);

    /**
     * Gets the definition of this rewrite rule.
     * @return
     */
    public String getDefinition();

    /**
     * Sets the name of this rewrite rule
     * @param name
     */
    public void setName(String name);

    /**
     * Gets the name of this rewrite rule.
     * @return
     */
    public String getName();

    /**
     * Sets the list of declarations of variables
     */
	public void setZDeclList(ZDeclList zDeclList);

    /**
     * Gets the list of declarations of variables
     */
	public DeclList getZDeclList();

    /**
     * Gets a list with the Strings that match in the regular expression
     * @return
     */
    public List<String> getStrExpr();
    /**
     * Gets the types
     * @return
     */
    public List<Expr> getTypes();

    public String addRegExValues(String originalExpr);
} 