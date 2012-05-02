package client.blogic.management.ii;

import client.presentation.ClientUI;


/**
 * Represents an implicit invocation component of the system, a component that 
 * can be interested in some event.
 * @author Pablo Rodriguez Monetti
 */
public class IIComponent{

	protected ClientUI myClientUI;

    
    
    /**
     * Sets the reference to the client user interface.
     * @param clientUI
     */
	public void setMyClientUI(ClientUI clientUI){
		myClientUI = clientUI;
	}

    
    
    /**
     * Gets the reference to the client user interface.
     * @return
     */
	public ClientUI getMyClientUI(){
		return myClientUI;
	}


} 
