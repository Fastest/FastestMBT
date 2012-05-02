package client.blogic.management.ii;


/**
 * Represents an association between an event and an order to be invoked when
 * that event is announced.
 * @author Pablo Rodriguez Monetti
 */
public class Subscriptor{

	private String eventName;
	private IIOrder iIOrder;

    /**
     * Creates instances of Subscriptor.
     * @param eventName
     * @param iIOrder
     */
	public Subscriptor(String eventName, IIOrder iIOrder){
		this.eventName = eventName;
		this.iIOrder = iIOrder;
	}

    
    
    /**
     * Gets the name of the event associated to this object.
     * @return
     */
	public String getEventName(){
		return eventName;
	}
	
    
    
    /**
     * Gets the order associated to this object.
     * @return
     */
	public IIOrder getIIOrder(){
		return iIOrder;
	}

} 
