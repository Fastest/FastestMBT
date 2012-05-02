package compserver.management;

import java.io.*;
import java.net.*;
import java.util.*;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.base.util.XmlReader;
import net.sourceforge.czt.base.util.XmlWriter;
import net.sourceforge.czt.z.jaxb.JaxbXmlReader;
import net.sourceforge.czt.z.jaxb.JaxbXmlWriter;

import common.z.AbstractTCase;
import common.z.TClass;
import common.z.TClassImpl;
import common.z.SpecUtils;
import compserver.tcasegen.TCaseGen;
import compserver.tcasegen.strategies.TCaseStrategy;
import net.sourceforge.czt.session.SectionManager;
import compserver.prunning.TreePruner;
import compserver.prunning.ResultPrune;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import client.blogic.management.communic.SocketWriter;

/**
 * Instances of this class give attention to requests for services that come from the clients.
 * The idea is provide the service executing the run() method, in a separate thread.
 * @author Pablo Rodriguez Monetti
 */
public class ServerThread extends Thread{

	private Socket socket;
	InputStream in;
	DataInputStream inReader;
	BufferedReader bufferedReader;
	OutputStream out;
	DataOutputStream outWriter;
	AxPara axPara;
	Spec spec;

    
    
    /**
     * Creates an instance of ServerThread.
     * @param socket
     */
	ServerThread(Socket socket){
		this.socket = socket;
	}

    /**
     * Provides a requested service.
     */
    @Override
	public void run(){
 	try 
	{
			System.out.println("Client connected!\n");
			in = socket.getInputStream();
			inReader = new DataInputStream(in);
        		out = socket.getOutputStream();
			outWriter = new DataOutputStream(out);
			// Esperamos del cliente el numero de operacion
			int opNumber = inReader.readInt();
			if(opNumber==0){
				genAbstractTCase();
			}
			else if(opNumber==1){
				pruneTTree();
			}
        }
		catch (Exception e) {
			e.printStackTrace();
        		System.exit(1);
        }

	}


    /**
     * Provides the service of generating an abstract test case.
     */
	public void genAbstractTCase(){

		try{
			spec = SocketReader.readSpec(in);
			axPara = SocketReader.readAxPara(in);
			//Esperamos del cliente el nombre del tClass
			int nameSize = inReader.readInt();
			byte[] name = new byte[nameSize];
			inReader.read(name);
			String schName = new String(name);
			System.out.println("Generating test case for '" + schName +  "' test class.\n");

			// Le mandamos al cliente un ack de las cosas recibidas
			//outWriter.writeBytes("OK");

			//Esperamos del cliente un flujo de bytes que corresponde a un objeto serializado y lo escribimos en el archivo tempFile
			long fileSize = inReader.readLong();
			//System.out.println("El tamaño de la strategy: "+fileSize);

			int random = (int)(Math.random()*1000000);
			String fileName = "servertempTR" + random + ".temp";	
			File tempFile3 = new File(fileName);
        		FileOutputStream fileWriter = new FileOutputStream(tempFile3);
			int bytesRead = 0;
			byte[] buf3 = new byte[2048];
			int c;
			while(bytesRead  < fileSize){  
				if(fileSize - bytesRead > 2048){  
				c = in.read(buf3, 0, 2048);  
				} else{  
				c = in.read(buf3, 0, (int)(fileSize - bytesRead));  
				}  
				bytesRead += c;  
				fileWriter.write(buf3, 0, c);  
			}     
			fileWriter.close();  

/*	        	while ((c = in.read()) != 1000) {
                		outFile.write(c);
            		}*/


			//Parseamos el archivo tempFile para construir un objeto y verificamos que sea 
			// de la clase TCaseStrategy
			FileInputStream fileInputStream = new FileInputStream(tempFile3);
			ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream);
			TCaseStrategy tCaseStrategy = (TCaseStrategy) objInputStream.readObject();
			fileInputStream.close();
			objInputStream.close();
			tempFile3.delete();

			// Intentamos encontrar un caso de prueba
			TClass tClass = new TClassImpl(axPara, schName);
			AbstractTCase abstractTCase = TCaseGen.generateAbstractTCase(spec, tClass, tCaseStrategy);

			if(abstractTCase == null){
                            // If the test case generation failed, we indicate
                            // it to the client                       
                            outWriter.writeChar('n');
                            System.out.println(schName + "_TCASE test case COULD NOT BE generated.\n");
                        }
                        else if(abstractTCase.getMyAxPara() == null){
                            outWriter.writeChar('p');
                            System.out.println(schName + "_TCASE test case COULD " +
                                    "NOT BE generated but not all the evaluations" +
                                    "were performed.\n");
                        }
                        else{
                            //Si pudo generarse un caso de prueba, se lo indicamos al client        			outWriter.writeChar('y');
                            // Le pasamos al Client el AxPara del abstractTCase generado escribiendolo primero en xml en un archivo y
                            // enviando despues el contenido del archivo
                            axPara = abstractTCase.getMyAxPara();
                            System.out.println(schName + "_TCASE test case generated\n" + SpecUtils.termToLatex(axPara));
                            random = (int)(Math.random()*1000000);
                            fileName = "servertempCA" + random + ".temp";
                            File tempFile4 = new File(fileName);
                            FileWriter fwriter = new FileWriter(tempFile4);
                            XmlWriter xmlWriter = new JaxbXmlWriter();
                            xmlWriter.write((Term) axPara, fwriter);
                            fileWriter.close();
                            // We send to the client the size of the file
                            fileSize = tempFile4.length();
                            outWriter.writeLong(fileSize);
                            //System.out.println("El tamaño del caso es de: "+fileSize);
                            FileInputStream fi = new FileInputStream(tempFile4);
                            BufferedInputStream origin = new BufferedInputStream(fi,2048);
                            int count;
                            byte[] data = new byte[2048];
                            while((count = origin.read(data, 0,2048)) != -1)
                            	outWriter.write(data, 0, count);
                            origin.close();
                            tempFile4.delete();
			}

			//We close the streams and the sockets

	      	inReader.close();
        	outWriter.close();
	      	socket.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
    /**
     * Provides the service of analize the prunning of a test case.
     */
	public void pruneTTree()
	{
		try{
			AxPara axPara = SocketReader.readAxPara(in);
			String axParaName = SpecUtils.getAxParaName(axPara);
			TClass tClass = new TClassImpl(axPara, axParaName);
			TreePruner treePruner = new TreePruner();
			// NO FUNCIONA!!!
			// COn el cambio para definiciones axiomaticas hay que cambiar el socket tambien
			// No esta hecho!!!
			ResultPrune deleted = treePruner.pruneTree(tClass,null);
			String tClassName = deleted.getTClassName();
			String theoremName = deleted.getTheoremName();
			List<String> params = deleted.getParams();
			int paramsSize = params.size();
			boolean result = deleted.getResult();
			// We send to the client the name of the TClass
			SocketWriter.writeString(out, tClassName);
			// We send to the client the name of the elimination theorem
			SocketWriter.writeString(out, theoremName);
			// We send to the client the number of parameters of the theorem
			SocketWriter.writeInteger(out, paramsSize);
			// We send to the client the name of the parameters
			for(int i=0;i<paramsSize;i++)
				SocketWriter.writeString(out, params.get(i));
			if(result)
				SocketWriter.writeChar(out,'y');
			else
				SocketWriter.writeChar(out,'n');
			Character resp = SocketReader.readChar(in);

	      	inReader.close();
        	outWriter.close();
	      	socket.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}



