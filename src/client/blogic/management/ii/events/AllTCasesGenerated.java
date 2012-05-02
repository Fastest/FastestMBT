package client.blogic.management.ii.events;

/**
 * Represents the implicit invocation event that must be announced when all 
 * test cases requested are generated.
 * @author Pablo Rodriguez Monetti
 */
public class AllTCasesGenerated extends Event_{

    /**
     * Creates instances of AllTCasesGenerated.
     */
	public AllTCasesGenerated(){
		super.setEventName("allTCasesGenerated");
	}
}