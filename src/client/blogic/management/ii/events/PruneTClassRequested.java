package client.blogic.management.ii.events;

import common.z.TClass;
import client.blogic.management.communic.ServerConfig;

/**
 * Represents the implicit invocation event that must be announced when the user orders the
 * prunning of a test class.
 */
public class PruneTClassRequested extends Event_{

	private TClass tClass;
	private ServerConfig serverConfig;


    /**
     * Creates instances of PruneTClassRequested.
     * @param tClass The test class
     * @param serverConfig The configuration of the server
     */
	public PruneTClassRequested(TClass tClass, ServerConfig serverConfig){
		this.tClass = tClass;
		this.serverConfig = serverConfig;
		super.setEventName("pruneTClassRequested");
	}

    /**
     * Sets the test class
     * @param tClass The test class
     */
	public void setTClass(TClass tClass){
		this.tClass = tClass;
	}
    /**
     * Gets the test class
     * @return
     */
	public TClass getTClass(){
		return tClass;
	}	

    /**
     * Sets the configuration of the server
     * @param serverConfig The configuration
     */
	public void setServerConfig(ServerConfig serverConfig){
		this.serverConfig = serverConfig;
	}
    /**
     * Gets the configuration of the server
     * @return
     */
	public ServerConfig getServerConfig(){
		return serverConfig;
	}
}