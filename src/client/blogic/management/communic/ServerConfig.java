package client.blogic.management.communic;

import java.net.*;


/**
 * Maintains information about the configuration of a server.
 * @author Pablo Rodriguez Monetti
 */
public class ServerConfig{

	private String serverName;
	private InetAddress inetAddress;
	private int port;

    
    
    /**
     * Creates a new instance of ServerConfig.
     * @param serverName
     * @param inetAddress
     * @param port
     */
	public ServerConfig(String serverName, InetAddress inetAddress, int port){
		this.serverName = serverName;
		this.inetAddress = inetAddress;
		this.port = port;
	}

    
    
    /**
     * Sets the name of the server associated to this ServerConfig.
     * @param serverName
     */
	public void setServerName(String serverName){
		this.serverName = serverName;
	}

    
    
    /**
     * Gets the name of the server associated to this ServerConfig.
     * @return
     */
	public String getServerName(){
		return serverName;
	}

    
    
    /**
     * Sets the IP address of the server associated to this ServerConfig.
     * @param inetAddress
     */
	public void setInetAddress(InetAddress inetAddress){
		this.inetAddress = inetAddress;
	}

    
    
    /**
     * Gets the IP address of the server associated to this ServerConfig.
     * @return
     */
	public InetAddress getInetAddress(){
		return inetAddress;
	}

    
    
    /**
     * Sets the port number of the server associated to this ServerConfig.
     * @param port
     */
	public void setPort(int port){
		this.port = port;
	}

    
    
    /**
     * Gets the port number of the server associated to this ServerConfig.
     * @return
     */
	public int getPort(){
		return port;
	}

} 
