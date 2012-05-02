package client.blogic.management.ii.events;


/**
 * Represents the implicit invocation event that must be announced when all 
 * test trees requested are generated.
 * @author Pablo Rodriguez Monetti
 */
public class AllTTreesGenerated extends Event_{

	private String opName;

	public AllTTreesGenerated(){
		super.setEventName("allTTreesGenerated");
	}
}
