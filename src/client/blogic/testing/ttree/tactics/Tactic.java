package client.blogic.testing.ttree.tactics;

import client.blogic.management.Controller;
import java.util.*;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.session.SectionManager;

import common.z.TClass;
import common.z.OpScheme;

/**
 * Interface that abstracts a testing tactic (needed to generate test trees) and
 * makes possible its application to a test class in order to generate new ones.
 * @author Pablo Rodriguez Monetti
 */
public interface Tactic {

    /**
     * Applies this tactic to the specified test class and returns the list with
     * the generated test classes.
     * @param tClass
     * @return
     */
    public List<TClass> applyTactic(TClass tClass);

    /**
     * Sets the specification of the system under test.
     * @param opScheme
     */
    public void setSpec(Spec spec);

    /**
     * Gets the schema of the operation under test.
     * @return
     */
    public Spec getSpec();

    /**
     * Sets the schema of the operation under test.
     * @param opScheme
     */
    public void setOriginalOp(OpScheme opScheme);

    /**
     * Gets the schema of the operation under test.
     * @return
     */
    public OpScheme getOriginalOp();

    /**
     * Parses the parameters of this tactic.
     * @param str
     * @return
     */
    public boolean parseArgs(String str);

    /**
     * Sets the instance of TacticInfo associated to this object.
     * @param tacticInfo
     */
    public void setTacticInfo(TacticInfo tacticInfo);

    /**
     * Gets the instance of TacticInfo associated to this object.
     * @return
     */
    public TacticInfo getTacticInfo();

    /**
     * Gets the descrption of this tactic.
     * @return the string with the description of this tactic.
     */
    public String getDescription();

    /**
     * Sets the descrption of this tactic.
     * @param description
     */
    public void setDescription(String description);

    /**
     * Sets the section manager of this tactic.
     * @param manager
     */
    public void setSectionManager(SectionManager manager);

    /**
     * Returns the section manager of this tactic.
     * @return
     */
    public SectionManager getSectionManager();

    /**
     * Clear all the entries in the map used to assign the names to the classes
     */
    public void resetNumbersMap();

    /**
     * Sets a reference to the controller
     * @param controller
     */
    public void setController(Controller controller);

    /**
     * Gets a reference to the controller
     * @return
     */
    public Controller getController();
}
