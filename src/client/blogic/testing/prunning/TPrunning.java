package client.blogic.testing.prunning;

import java.util.*;
import client.blogic.management.ii.events.PruneTTreeRequested;
import client.blogic.management.ii.events.PruneTClassRequested;
import client.blogic.management.ii.events.PrunningResult;
import client.blogic.management.ii.events.Event_;
import client.blogic.management.ii.EventAdmin;
import client.blogic.management.ii.IIComponent;
import common.repository.AbstractRepository;
import common.repository.AbstractIterator;
import common.z.TClass;
import client.blogic.management.communic.*;
import java.net.InetAddress;


/**
 * The instances of this class (although we assume there is only one in the 
 * system) manage events of type PruneTTreeRequested, announcing an event of type
 * PruneTClassRequested, for each test class the test tree has as a leaf
 */

public class TPrunning extends IIComponent{

	private List<ServerConfig> serversList;
	private Map<InetAddress, Integer> mapRequests;
	// Cantidad maxima de solicitudes a un servidor en particular
	private final int maxRequests = 4;
	private AbstractIterator<TClass> tClassIt;
	private boolean blocked;
	private int serverNumber;

    /**
     * Creates new instances of TPrunning.
     */
	public TPrunning(){
		mapRequests = new HashMap<InetAddress, Integer>();
		blocked = false;
		serverNumber = 0;
	}

    /**
     * Manages an implicit invocation event.
     * @param event_
     * @throws java.lang.IllegalArgumentException if event_ is not instance of 
     * PruneTTreeRequested
     */
	public synchronized void manageEvent(Event_ event_)
		throws IllegalArgumentException{

		if(event_ instanceof PruneTTreeRequested){
			try{
				blocked = false;
				mapRequests = new HashMap<InetAddress, Integer>();
				serverNumber = 0;
				serversList = CServersControl.getInstance().getServersList();
				PruneTTreeRequested pruneTTreeRequested = (PruneTTreeRequested) event_;
				AbstractRepository<TClass> leaves = pruneTTreeRequested.getLeaves();
				/*AbstractIterator<TClass> tClassItAux = leaves.createIterator();
				int nroLeaves=0;
				while(tClassItAux.hasNext())
					nroLeaves++;*/
				tClassIt = leaves.createIterator();
				//EventAdmin eventAdmin = EventAdmin.getInstance();
				while(tClassIt.hasNext() && !blocked){
					blocked = requestPrune();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(event_ instanceof PrunningResult){
			PrunningResult prunningResult = (PrunningResult) event_;
			ServerConfig auxServer = prunningResult.getServerConfig();
			//InetAddress auxAddress = auxServer.getInetAddress();
			//int oldValue = mapRequests.get(auxAddress).intValue();
			//mapRequests.put(auxAddress, new Integer(oldValue - 1));
			try{
			EventAdmin eventAdmin = EventAdmin.getInstance();
			if(tClassIt.hasNext()){
        			TClass tClass = tClassIt.next();
				PruneTClassRequested pruneTClassRequested = 
                           		new PruneTClassRequested(tClass, auxServer);
				eventAdmin.announceEvent(pruneTClassRequested);
				//mapRequests.put(inetAddress,new Integer(req+1));
			}
			}
			catch(Exception e){
			e.printStackTrace();
			}
		}
		else{
			throw new IllegalArgumentException();
		}
	}
	private boolean requestPrune()
	{
		try{
		ServerConfig serverConfig;
		EventAdmin eventAdmin = EventAdmin.getInstance();
		if(serverNumber<serversList.size()){
			serverConfig = serversList.get(serverNumber);
			serverNumber++;
		}
		else{
			serverConfig = serversList.get(0);
			serverNumber = 1;
		}
		InetAddress inetAddress = serverConfig.getInetAddress();
		if(mapRequests.containsKey(inetAddress)){
			Integer value = mapRequests.get(inetAddress);
			int req = value.intValue();
			if(req<maxRequests){
        			TClass tClass = tClassIt.next();
				PruneTClassRequested pruneTClassRequested = 
                           		new PruneTClassRequested(tClass, serverConfig);
				eventAdmin.announceEvent(pruneTClassRequested);
				mapRequests.put(inetAddress,new Integer(req+1));
			}
			else{
				serverConfig = getNextFreeServer();
				if(serverConfig!=null){
				inetAddress = serverConfig.getInetAddress();
        			TClass tClass = tClassIt.next();
				PruneTClassRequested pruneTClassRequested = 
                           		new PruneTClassRequested(tClass, serverConfig);
				eventAdmin.announceEvent(pruneTClassRequested);
				int oldValue = mapRequests.get(inetAddress).intValue();
				mapRequests.put(inetAddress,new Integer(oldValue+1));
				}
				else
					blocked = true;
			}
		}
		else{
			mapRequests.put(inetAddress,new Integer(1));
			TClass tClass = tClassIt.next();
			PruneTClassRequested pruneTClassRequested = 
                           		new PruneTClassRequested(tClass, serverConfig);
			eventAdmin.announceEvent(pruneTClassRequested);
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return blocked;
	}

	private ServerConfig getNextFreeServer()
	{
		for(int i=0;i<serversList.size();i++){
			ServerConfig auxConfig = serversList.get(i);
			InetAddress auxAddress = auxConfig.getInetAddress();
			if(mapRequests.containsKey(auxAddress))
				if(mapRequests.get(auxAddress).intValue()<maxRequests){
					serverNumber = i;
					return auxConfig;
				}
		}
		return null;
	}
}
