package client.blogic.management.communic;

import java.net.*;
import java.io.*;


/**
 * Loads the configuration of the computation servers into the system's (unique)
 * instance of CServersControl from the specified configuration file.
 * @author Pablo Rodriguez Monetti
 */
public class CServersConfigLoader{
    
    private String fileName;
    
    /**
     * Creates a new instance of CServersConfigLoader.
     * @param fileName
     */
    public CServersConfigLoader(String fileName){
        this.fileName = fileName;
    }

    
    
    /**
     * Loads the configuration of the computation servers into the system's (unique)
     * instance of CServersControl from the specified configuration file.
     */
	public void loadCServersInfo(){

		CServersControl cServersControl = CServersControl.getInstance();

		try{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = in.readLine())!= null){
				String lineParts[] = line.split(" ");
				// We extract the server's name from the file
				String serverName = lineParts[0];
				// We extract the server's IP from the file
				String strIP = lineParts[1];
				InetAddress ip = InetAddress.getByName(strIP);
				// We extract the server's port from the file
				String strPort = lineParts[2];
				int port = Integer.parseInt(strPort);
				// We create a new ServerInfo object
				ServerConfig serverInfo = new ServerConfig(serverName, ip, port);
				// We add the object to the CServersInfoControl
				cServersControl.addElement(serverInfo);	
			}

			
		}
		catch(FileNotFoundException e){
			System.out.println("The computation servers' configuration file could not be found.");
			System.exit(0);
		}	
		catch(IOException e){
			System.out.println("IOException");
			System.exit(0);
		}	
	}
}
