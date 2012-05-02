package client.blogic.management.ii.events;

/**
 * Represents an implicit invocation event.
 * @author Pablo Rodriguez Monetti
 */
public class Event_{
	
	private String eventName;	

    /**
     * Sets the name of the event.
     * @param eventName
     */
	public void setEventName(String eventName){
		this.eventName = eventName;
	}

    
    
    /**
     * Gets the name of the event.
     * @return
     */
	public String getEventName(){
		return eventName;
	}
} 
