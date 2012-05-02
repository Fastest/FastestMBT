package client.blogic.management.communic;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import java.util.*;


/**
 * This module stores
 */
public class ServersOperationMap {

	private static ServersOperationMap serversOperationMap;
	private Map<String,ServerConfig> map;

    
    
    /**
     * Creates a new instance of ServersOperationMap. It can only be called from
     * the getInstance() method.
     */
	private ServersOperationMap(){
		map = new HashMap<String,ServerConfig>();
	}

    
    
    /**
     * Gets the unique instance of this class, creating it if necessary.
     * @return
     */
	public static ServersOperationMap getInstance(){
	
		if(serversOperationMap==null)
			serversOperationMap = new ServersOperationMap();
		return serversOperationMap;
	}

    /**
     * Gets the server associated to an operation
     * @param opName The name of the operation
     * @return the server configuration
     */

	public ServerConfig getServer(String opName){
		return map.get(opName);
	}

    /**
     * Sets the server associated to an operation
     * @param opName The name of the operation
     * @param serverConfig The configuration of the server
     */

	public void setServer(String opName, ServerConfig serverConfig){
		map.put(opName,serverConfig);
	}
}