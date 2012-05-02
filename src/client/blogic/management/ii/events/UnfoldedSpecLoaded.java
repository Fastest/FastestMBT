package client.blogic.management.ii.events;

import net.sourceforge.czt.z.ast.Spec;

/**
 * Represents the implicit invocation event that must be announced when the
 * specification of the system under test is loaded.
 * @author Pablo Rodriguez Monetti
 */
public class UnfoldedSpecLoaded extends Event_ {

    private Spec spec;

    /**
     * Creates instances of SpecLoaded.
     * @param spec
     */
    public UnfoldedSpecLoaded(Spec spec) {
        this.spec = spec;
        super.setEventName("specLoaded");
    }

    /**
     * Sets the spec associated to this object.
     * @param spec
     */
    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    /**
     * Gets the specification associated to this object.
     * @return
     */
    public Spec getSpec() {
        return spec;
    }
}
