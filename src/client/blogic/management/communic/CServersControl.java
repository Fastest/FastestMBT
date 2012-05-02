package client.blogic.management.communic;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import java.util.*;


/**
 * Mantains and manages configuration of every system's computation server.
 * @author Pablo Rodriguez Monetti
 */
public class CServersControl implements AbstractRepository<ServerConfig>{

	private static CServersControl cServersControl;
	private AbstractRepository<ServerConfig> repository;

    
    
    /**
     * Creates a new instance of CServersControl. It can only be called from
     * the getInstance() method.
     */
	private CServersControl(){
		repository = new ConcreteRepository<ServerConfig>();
	}

    
    
    /**
     * Gets the unique instance of this class, creating it if necessary.
     * @return
     */
	public static CServersControl getInstance(){
	
		if(cServersControl==null)
			cServersControl = new CServersControl();
		return cServersControl;
	}

    
    /**
     * Obtains an iterator to traverse the CServersInfo's information.
     * @return
     */
	public AbstractIterator<ServerConfig> createIterator(){
		return repository.createIterator();
	}

    
    
    /**
     * Adds the configuration of a computation server to this CServersControl.
     * @param serverInfo
     */
	public void addElement(ServerConfig serverInfo){
		repository.addElement(serverInfo);
	}

    
    
    /**
     * Gets the configuration of a random computation server. It allows the
     * client to retrieve the information needed to connect to that server.
     * @return
     */
	public ServerConfig getRandomServerInfo(){
		
		// We get the number of available servers
		AbstractIterator<ServerConfig> iterator = repository.createIterator();
		int size = 0;
		while(iterator.hasNext()){
			iterator.next();
			size++;
		}
        
        // We choose a random number between 0 and the number of available 
        // servers minus 1
		int random = (int)(Math.random()*size);

        // We return the random-th ServerConfig instance of this CServersControl
		iterator = repository.createIterator();
		int i = 0;
		ServerConfig serverInfo = null;
		while(iterator.hasNext() && i <= random){
			serverInfo = iterator.next();
			i++;
		}
		return serverInfo;
	}

	public List<ServerConfig> getServersList(){
		List<ServerConfig> listServers = new ArrayList<ServerConfig>();
		AbstractIterator<ServerConfig> iterator = repository.createIterator();
		iterator = repository.createIterator();
		while(iterator.hasNext())
			listServers.add(iterator.next());
		return listServers;
	}


}