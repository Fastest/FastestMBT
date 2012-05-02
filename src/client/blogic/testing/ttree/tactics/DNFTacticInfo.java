package client.blogic.testing.ttree.tactics;

/* Instances of this class contains relevant information about the related tactic, 
 * the DNF (Disjunctive Normal Form) tactic. The only information this tactic
 * maintains in this object is its name.
 * @author Pablo Rodriguez Monetti
 */
public class DNFTacticInfo extends TacticInfo {

    /**
     * Creates instances of DNFTacticInfo
     */
    public DNFTacticInfo() {
        super.setTacticName("DNF");
    }
}