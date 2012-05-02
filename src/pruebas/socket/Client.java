package pruebas.socket;

import java.io.*;
import java.net.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.session.*;
import net.sourceforge.czt.z.ast.*;
import net.sourceforge.czt.z.jaxb.JaxbXmlWriter;
import net.sourceforge.czt.base.util.*;

import compserver.tcasegen.strategies.CompleteTCaseStrategy;

// Para usar junto con Server
public class Client{ 
    public static void main(String[] args){

        try {
        	Socket socket = new Socket(new String(), 7111);
			socket.setReuseAddress(true);

			OutputStream out = socket.getOutputStream();
        	OutputStreamWriter outWriter = new OutputStreamWriter(out);
        	PrintWriter printWriter = new PrintWriter(out , true);


			InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        	BufferedReader bufferedReader = new BufferedReader(reader);


			SectionManager manager = new SectionManager();
			if(args.length!=1){
				System.out.println("Ingresar nombre de archivo!!!!");
				return;
			}



			// Le informamos al server el numero de operacion
			printWriter.println(1);
	

	
			// Escribimos la especif spec en el archivo tempFile en formato xml
			FileSource source = new FileSource(args[0]);
			manager.put(new Key(source.getName(), Source.class), source);
			Spec spec = (Spec) 
				manager.get(new Key(source.getName(), Spec.class));
			File tempFile = new File("tempFile.txt");
			FileWriter fileWriter = new FileWriter(tempFile);
			XmlWriter xmlWriter = new JaxbXmlWriter();	
			xmlWriter.write(spec, fileWriter);
			fileWriter.close();
			
	



			//Le enviamos al Server la especificacion en xml desde el archivo tempFile
			BufferedReader fileReader = new BufferedReader(new FileReader(tempFile));
			String linea = "";
			while((linea = fileReader.readLine())!= null){
				printWriter.println(linea);
			}
			printWriter.println("Spec sent.");
			tempFile.delete();




			// Le pasamos al Server un Para escribiendolo primero en xml en un archivo y
			// enviando despues el contenido del archivo
			for (Sect sect : spec.getSect()) {
				if (sect instanceof ZSect){
					ZSect zSect = (ZSect) sect;
					ParaList paraList = zSect.getParaList();
					if (paraList instanceof ZParaList) {
						ZParaList zParaList = (ZParaList) paraList;
						tempFile = new File("tempFile.txt");
						fileWriter = new FileWriter(tempFile);
						xmlWriter.write((Term) (zParaList.get(7)), fileWriter);	
						fileWriter.close();
		
						fileReader = new BufferedReader(new FileReader(tempFile));
						while((linea = fileReader.readLine())!= null){
							printWriter.println(linea);
						}
						printWriter.println("Para sent.");
					}
    			}
			}




			// Serializamos un objeto CompleteTCaseStrategy en el archivo tempFile
			CompleteTCaseStrategy genAllTCaseStrategy = new CompleteTCaseStrategy(3);
			ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream(tempFile));
			objOutputStream.writeObject(genAllTCaseStrategy);
			objOutputStream.close();




			// Le pasamos al server el objeto enviando el contenido del archivo tempFile
			FileInputStream fileInputStream = new FileInputStream(tempFile);
        	int c;
        	while ((c = fileInputStream.read()) != -1) {
                outWriter.write(c);
				outWriter.flush();
        	}	
			outWriter.write(1000);
			outWriter.flush();



			// Esperamos ack de todas las cosas enviadas de parte del server
			String resp = bufferedReader.readLine();
			System.out.println(resp);


			tempFile.delete();
			bufferedReader.close();
			printWriter.close();
        	socket.close();	
        } 
		catch(Exception e){
			e.printStackTrace();
		}
   }
}