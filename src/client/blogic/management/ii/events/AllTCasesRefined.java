package client.blogic.management.ii.events;

/**
 * Represents the implicit invocation event that must be announced when all
 * abstract test cases requested are refined.
 * @author Hache
 */
public class AllTCasesRefined extends Event_{

    /**
     * Creates instances of AllTCasesRefined.
     */
    public AllTCasesRefined(){
        super.setEventName("allTCasesRefined");
    }
}
