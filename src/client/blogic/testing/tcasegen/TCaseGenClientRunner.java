package client.blogic.testing.tcasegen;

import java.net.InetAddress;

import net.sourceforge.czt.z.ast.Spec;

import client.blogic.management.ii.events.TCaseGenerated;
import client.blogic.management.ii.EventAdmin;
import common.z.AbstractTCase;
import common.z.TClass;
import compserver.tcasegen.strategies.TCaseStrategy;
import compserver.tcasegen.strategies.AtomicPredTCaseStrategy;
import compserver.tcasegen.TCaseGen;
import client.blogic.management.communic.*;


/**
 * Instances of this class has the responsibility of request, to a computation 
 * server, for the generation of a test case for a particular test class of a 
 * test tree of an operation.  Each request must be done through a new instance
 * of ServiceMediator, which is the only one that has the knowledge about how
 * the connection with the servers are implemented.
 * @author Pablo Rodriguez Monetti
 */
public class TCaseGenClientRunner implements Runnable
{
	private Spec spec;
	private String opName;
	private TClass tClass;
	private TCaseStrategy tCaseStrategy;	

    
    
    /**
     * Creates new instances of TCaseGenClientRunner
     * @param spec
     * @param opName
     * @param tClass
     * @param tCaseStrategy
     */
	public TCaseGenClientRunner(Spec spec, String opName, TClass tClass, TCaseStrategy tCaseStrategy){
		this.spec = spec;
		this.opName = opName;
		this.tClass = tClass;
		this.tCaseStrategy = tCaseStrategy;
	}

    
    
    /**
     * Requests the generation of an abstract test case either to the client 
     * itself or to a computation server server. After a response arrives, it
     * announces a TCaseGenerated event.
     */
	public void run(){

		ServerConfig serverInfo;
		// If the strategy of case generation is instance of AtomicPredTCaseStrategy we send to
		// the same server all the test classes related to an operation
		if(tCaseStrategy instanceof AtomicPredTCaseStrategy){
			ServersOperationMap serversOperationMap = ServersOperationMap.getInstance();
			if(serversOperationMap.getServer(opName)!=null)
				serverInfo = serversOperationMap.getServer(opName);
			else{
				serverInfo = CServersControl.getInstance().getRandomServerInfo();
				serversOperationMap.setServer(opName, serverInfo);
			}
		}
		else
			serverInfo = CServersControl.getInstance().getRandomServerInfo();



		String serverName = serverInfo.getServerName();
		InetAddress inetAddress = serverInfo.getInetAddress();
		int port = serverInfo.getPort();

		AbstractTCase abstractTCase  = null;

		if(inetAddress.isLoopbackAddress()){
			// We call TCaseGen directly, and the the test case will be 
            		// generated, if possible, in the client
			abstractTCase = TCaseGen.generateAbstractTCase(spec, tClass, tCaseStrategy);
		}
		else{
			// We want ServiceMediator to do the request. The test case will
            		// be generated in a computation server
			abstractTCase = (new ServiceMediator(inetAddress, port)).generateAbstractTCase(spec, tClass, tCaseStrategy);
		}

		TCaseGenerated tCaseGenerated = new TCaseGenerated(opName, tClass, abstractTCase);
		try{
			EventAdmin eventAdmin = EventAdmin.getInstance();
			eventAdmin.announceEvent(tCaseGenerated);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


}
