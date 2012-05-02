package client.blogic.management.ii.events;

/**
 * Represents the implicit invocation event that must be announced when the user 
 * resets Fastest.
 * @author Pablo Rodriguez Monetti
 */
public class FastestResetted extends Event_{

    /**
     * Creates instances of AllTCasesGenerated.
     */
	public FastestResetted(){
		super.setEventName("fastestResetted");
	}
}