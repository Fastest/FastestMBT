package client.blogic.management.ii.events;

/**
 * Represents the implicit invocation event that must be announced when a request of
 * execution was processed
 */

public class RunFinished extends Event_{

    /**
     * Creates instances of RunFinished.
     */
	public RunFinished(){
		super.setEventName("runFinished");
	}
}