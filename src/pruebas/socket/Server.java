package pruebas.socket;


import java.io.*;
import java.net.*;

import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.base.util.XmlReader;
import net.sourceforge.czt.z.jaxb.JaxbXmlReader;

import common.z.czt.CztPrinter;

import compserver.tcasegen.strategies.TCaseStrategy;
import compserver.tcasegen.strategies.CompleteTCaseStrategy;


// Para usar junto con Client

public class Server{
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
        	serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(7111));

	        Socket clientSocket = null;
			System.out.println("Esperando por un cliente...");
            clientSocket = serverSocket.accept();
			clientSocket.setReuseAddress(true);
			System.out.println("Cliente conectado!");
		
			InputStream in = clientSocket.getInputStream();
        	InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(reader);
        	PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);





			// Esperamos del cliente el numero de operacion
			String linea;
			linea = bufferedReader.readLine();
			System.out.println("El cliente solicito la operacion " + linea);




	
			// Esperamos del cliente la especificacion y la escribimos en el archivo tempFile1
			File tempFile1 = new File("temppFile1.txt");
			PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile1));
			while((linea = bufferedReader.readLine()) != null){
				if(linea.equals("Spec sent."))
					break;	
				fileWriter.println(linea);
			}
			fileWriter.close();



	
			// Esperamos del cliente un Para y lo escribimos en el archivo tempFile2
			File tempFile2 = new File("temppFile2.txt");
			fileWriter = new PrintWriter(new FileWriter(tempFile2));
			while((linea = bufferedReader.readLine()) != null){
				if(linea.equals("Para sent."))
					break;
				fileWriter.println(linea);
			}
			fileWriter.close();




			//Esperamos del cliente un flujo de bytes que corresponde a un objeto serializado
			// y lo escribimos en el archivo tempFile3
			File tempFile3 = new File("temppFile3.txt");
        	FileOutputStream out = new FileOutputStream(tempFile3);
			int c;
            while ((c = reader.read()) != 1000) {
                out.write(c);
            }





			// Le mandamos al cliente un ack de las cosas recibidas
			printWriter.println("ack para la spec, el para y el TCaseStrategy");





			// Parseamos la especificacion del archivo tempFile1 y lo mostramos por pantalla
			XmlReader xmlReader = new JaxbXmlReader();
			Spec spec = (Spec) xmlReader.read(tempFile1);
			CztPrinter.printSpec(spec);





			// Parseamos el Para recibido del archivo tempFile2 y lo mostramos por pantalla
			Para para = (Para) xmlReader.read(tempFile2);
			CztPrinter.printPara(para, 1, 1);





			//Parseamos el archivo tempFile3 para construir un objeto y verificamos que sea 
			// de la clase CompleteTCaseStrategy
			ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream(tempFile3));
			TCaseStrategy tCaseStrategy = (TCaseStrategy) objInputStream.readObject();
			objInputStream.close();

			if(tCaseStrategy instanceof CompleteTCaseStrategy)
				System.out.println("es un GenAllTCaseStrategy!!");



			//Borramos los archivos temporales y cerramos los streams y los sockets
			tempFile1.delete();
			tempFile2.delete();
			tempFile3.delete();
	      	bufferedReader.close();
        	printWriter.close();
	      	clientSocket.close();
        	serverSocket.close();
		
        }
		catch (Exception e) {
			e.printStackTrace();
        	System.exit(1);
        }
    }
}
