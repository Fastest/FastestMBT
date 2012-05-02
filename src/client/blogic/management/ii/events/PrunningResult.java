package client.blogic.management.ii.events;

import java.util.*;
import client.blogic.management.communic.*;

public class PrunningResult extends Event_{

	public PrunningResult(String tClassName, String theoremName, List<String> params, boolean result, ServerConfig serverConfig)
	{
		this.tClassName = tClassName;
		this.theoremName = theoremName;
		this.params = params;
		this.result = result;
		this.serverConfig = serverConfig;
		super.setEventName("prunningResult");
	}

    /**
     * Sets the name of the TClass
     * @param tClassName
     */
	public void setTClassName(String tClassName){
        	this.tClassName = tClassName;
    	}

    /**
     * Gets the name of the TClass
     */
	public String getTClassName(){
        	return tClassName;
    }

    /**
     * Sets the name of the Theorem
     * @param theoremName
     */
	public void setTheoremName(String theoremName){
        	this.theoremName = theoremName;
    	}

    /**
     * Gets the name of the Theorem
     */
	public String getTheoremName(){
        	return theoremName;
    }

    /**
     * Sets the list of the parameters of the theorem
     * @param params
     */
	public void setParams(List<String> params){
        	this.params = params;
    	}

    /**
     * Gets the list of the parameters of the theorem
     */
	public List<String> getParams(){
        	return params;
    }

    /**
     * Sets the result of the prunning
     * @param params
     */
	public void setResult(boolean result){
        	this.result = result;
    	}

    /**
     * Gets the result of the prunning
     */
	public boolean getResult(){
        	return result;
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

	private String tClassName;
	private String theoremName;
	private List<String> params;
	private boolean result;
	private ServerConfig serverConfig;
}
 
