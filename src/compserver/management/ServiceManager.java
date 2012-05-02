package compserver.management;

import java.net.*;
import java.io.*;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import compserver.prunning.TheoremsLoader;
import compserver.prunning.rewriting.rwrules.RWRulesLoader;

/**
 * An instance of this class is the one that implements the core of a server, processing 
 * the service requests done by the clients. Each request is processed executing the run() 
 * method of a ServerThread object, in a different thread, in order to allow the servicing 
 * of simultaneous services.
 * @author Pablo Rodriguez Monetti
 */
public class ServiceManager {

    /**
     * Runs this server.
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int port = 0;
	try {
		URL url = ServiceManager.class.getResource("ServiceManager.class");
		String urlStr = url.toString();
		String currentDir = urlStr.substring(9,urlStr.indexOf("fastest-server.jar"));
    		BufferedReader in = new BufferedReader(new FileReader(currentDir+"lib/conf/server-port.conf"));
		String line = in.readLine();
		String words[] = line.split(" ");
            	if(words.length != 2){
                	System.out.println("Error while reading port number.");
                	return;
           	}

		(new RWRulesLoader(currentDir+"/lib/conf/rwRules.tex")).loadRWRules();
		(new TheoremsLoader(currentDir+"/lib/conf/elimTheorems.tex", currentDir+"/lib/conf/thmoperators.conf")).loadTheorems();

            	port = Integer.parseInt(words[1]);

		Channel channel = System.inheritedChannel();
		ServerSocket serverSocket = null;
	
		if (channel instanceof ServerSocketChannel){
		// Service launched from xinetd.
		serverSocket = ((ServerSocketChannel) channel).socket();
		}else if (channel == null){
		// Service launched from the command line.
        	serverSocket = new ServerSocket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(new InetSocketAddress(port),1);
		System.out.println("Waiting for clients on port " + port + "..");
		} /*else {
		throw new IOException(
			"Unexpected channel returned from inheritedChannel: " +
			channel.toString());
		}*/

/*		ServerThread serverThread = new ServerThread();
		Remote proxy = null;
		try {
			proxy = UnicastRemoteObject.exportObject(server, 0);
		} catch (RemoteException e) {
			("Exception exporting service!!!\n" + e);
		}

		try{
		InitializeRegistry.initializeWithInheritedChannel(
			proxy, "ServiceInterface", port);
		
		} catch (Exception e) {
			System.out.println("Exception :"+e);
		}*/

		(new Thread(new RequestListener(serverSocket))).start();
		String str;
		in = new BufferedReader(new InputStreamReader(System.in));
		while(!(str = in.readLine()).equals("quit"));

        	serverSocket.close();

        } catch (IOException e) {
            System.err.println("Could not listen on port "+ port);
        }catch (NumberFormatException e){
            System.err.println("Please insert port number!");
        }
    }
}


class RequestListener implements Runnable{

	private ServerSocket serverSocket;

	public RequestListener(ServerSocket serverSocket){
		this.serverSocket = serverSocket;
	}

	public void run(){
	
        boolean listening = true;
		try{
        	while (listening)
		    	new ServerThread(serverSocket.accept()).start();
		}
		catch (SocketException e) {
        }
		catch (IOException e) {
			e.printStackTrace();
        }
		
	}

}