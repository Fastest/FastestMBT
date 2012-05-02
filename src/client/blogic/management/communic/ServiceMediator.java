package client.blogic.management.communic;

import java.io.*;
import java.net.*;
import java.util.*;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.ast.RefExpr;
import net.sourceforge.czt.z.ast.Expr;
import net.sourceforge.czt.base.util.XmlReader;
import net.sourceforge.czt.z.jaxb.JaxbXmlReader;
import net.sourceforge.czt.base.util.UnmarshalException;

import common.z.TClass;
import compserver.tcasegen.strategies.TCaseStrategy;
import common.z.AbstractTCase;
import common.z.AbstractTCaseImpl;
import compserver.tcasegen.eval.NormalTypeAndFM;
import compserver.prunning.ResultPrune;
import compserver.prunning.SpecInfo;
import net.sourceforge.czt.session.SectionManager;

/**
 * Establish the direct connection (through socket interfaces) between the client
 * components and the computation servers, codifying client component's requests 
 * and decodifying the results before sending them to the client components.
 * 
 * @author Pablo Rodriguez Monetti
 */
public class ServiceMediator{

	private InetAddress inetAddress;
	private int port;

	private Spec spec;
	//private String opName;
	private TClass tClass;
	private SpecInfo specInfo;
	private TCaseStrategy tCaseStrategy;	
	private SectionManager manager;
	private String sectionName;

    
    
    /**
     * Creates a new instance of ServiceMediator.
     * @param inetAddress
     * @param port
     */
	public ServiceMediator(InetAddress inetAddress, int port){
		this.inetAddress = inetAddress;
		this.port = port;
	}

    
    
    /**
     * Generates an abstract test case from the specified specification, the
     * specified test class and the specified test case generation's strategy.
     * 
     * @param spec
     * @param tClass
     * @param tCaseStrategy
     * @return the abstract test case generated or null, if any abstract test
     * case could not be generated.
     */
	public AbstractTCase generateAbstractTCase(Spec spec, TClass tClass, TCaseStrategy tCaseStrategy){

		this.spec = spec;
		//this.opName = opName;
		this.tClass = tClass;
		this.tCaseStrategy = tCaseStrategy;

		String tClassName = tClass.getSchName();
        try {
	       	Socket socket = new Socket(inetAddress, port);

		OutputStream out = socket.getOutputStream();
		DataOutputStream outWriter = new DataOutputStream(out);
		InputStream in = socket.getInputStream();
		DataInputStream inReader = new DataInputStream(in);


		// We notify the ID operation to the server
		SocketWriter.writeIdOperation(out, 0);

		//We send the hash of the Spec object
		//int specHash = spec.hashCode();
		//SocketWriter.writeSpecHash(out,specHash);
		// We send the spec to the server
		SocketWriter.writeSpec(out,spec);

		// We send the AxPara to the server 
		AxPara axPara = tClass.getMyAxPara();
		SocketWriter.writeAxPara(out, axPara);

		// We send to the server the tClass's name
		SocketWriter.writeClassName(out,tClassName);

		// We send to the server the TCaseStrategy
		SocketWriter.writeCaseStrategy(out,tCaseStrategy);


            // We wait for the acknowledge of every element sent to the server
/*		byte ack[] = new byte[2];
		int rs = inReader.read(ack,0,2);
		String resp = new String(ack); 
		System.out.println("Recibio todo: "+resp);*/

		// We send the TCaseStrategy to the server
		


            // We wait for a Char with information about the success or failure
            // in the test case generation
		Character resp = inReader.readChar();
		//System.out.println("Se pudo generar: "+resp);
	
		AbstractTCase abstractTCase = null;
		Map<Expr, NormalTypeAndFM> typeFMs = null;
		if(resp==null){
			System.out.println("An unexpected error has been detected during the case test generation of "+ tClassName + " test class.");
			}
		else if(resp.equals('y')){
                // The server generated a test case!!
                // We wait for the generated abstract test case's Axpara and
                // we write it in the file tempFile
			int random = (int)(Math.random()*1000000);
			String fileName = "clienttempCA" + random + ".temp";
			File tempFile4 = new File(fileName);
			// We obtain the size of the compressed file that contains the Spec
			long fileSize = inReader.readLong();
			//System.out.println("El tamaño del caso de prueba: "+fileSize);
			// We obtain the file that contains the Spec
			FileOutputStream fWriter = new FileOutputStream(fileName);
			int bytesRead = 0;
			int c;
			byte[] buf = new byte[2048];  
			while(bytesRead  < fileSize){  
				if(fileSize - bytesRead > 2048){  
				c = in.read(buf, 0, 2048);  
				} else{  
				c = in.read(buf, 0, (int)(fileSize - bytesRead));  
				}  
				bytesRead += c;  
				fWriter.write(buf, 0, c);  
			}     
			fWriter.close();  
			
                // We parse that AxPara
			XmlReader xmlReader = new JaxbXmlReader();
			axPara = (AxPara) xmlReader.read(tempFile4);
			tempFile4.delete();

			abstractTCase = new AbstractTCaseImpl(axPara, tClassName + "_TCASE");
		}
                else if(resp.equals('p')){
                    abstractTCase = new AbstractTCaseImpl(null, "");

                }
		inReader.close();
		outWriter.close();
        	socket.close();	
		return abstractTCase;
        } 
		catch(UnmarshalException e){
			System.out.println("\n\n\n");
			e.printStackTrace();
			return null;	
		}
		catch(InvalidClassException e){
			System.out.println("InvalidClassException");
			e.printStackTrace();
			return null;	
		}
		catch(NotSerializableException e){
			System.out.println("NotSerializableException");
			e.printStackTrace(System.out);
			System.out.println(e.getMessage());
			System.out.println(e);
			return null;	
		}
		catch(SocketException e){
			System.out.println("SocketException");
			e.printStackTrace();
			return null; //generateAbstractTCase(spec,tClass,tCaseStrategy);	
		}
		catch(IOException e){
			System.out.println("IOException");
			e.printStackTrace();
			return null;	
		}
		catch(Exception e){
			e.printStackTrace();
			return null;	
		}
	}
	public synchronized ResultPrune pruneTree(TClass tClass, SpecInfo specInfo)
	{
		this.tClass = tClass;
		this.specInfo = specInfo;

        try {
	       	Socket socket = new Socket(inetAddress, port);

		OutputStream out = socket.getOutputStream();
		DataOutputStream outWriter = new DataOutputStream(out);
		InputStream in = socket.getInputStream();
		DataInputStream inReader = new DataInputStream(in);

		String tClassName = tClass.getSchName();
		// We notify the ID operation to the server
		SocketWriter.writeIdOperation(out, 1);

		// We send to the server the tClass
		AxPara axPara = tClass.getMyAxPara();
		SocketWriter.writeAxPara(out, axPara);

		// We read the name of the tClass
		int tClassNameSize = inReader.readInt();
		byte[] tClassNameByte = new byte[tClassNameSize];
		for(int i=0;i<tClassNameSize;i++)
			tClassNameByte[i] = inReader.readByte();
		tClassName = new String(tClassNameByte);

		// We read the name of the elimination theorem
		int theoremNameSize = inReader.readInt();
		byte[] theoremNameByte = new byte[theoremNameSize];
		for(int i=0;i<theoremNameSize;i++)
			theoremNameByte[i] = inReader.readByte();
		String theoremName = new String(theoremNameByte);
		// We read the number of parameters
		int paramsSize = inReader.readInt();

		// We read the parameters
		List<String> params = new ArrayList<String>();
		for(int i=0;i<paramsSize;i++){
		int paramSize = inReader.readInt();
		byte[] param = new byte[paramSize];
		for(int j=0;j<paramSize;j++)
			param[j] = inReader.readByte();
		String parameter = new String(param);
		params.add(parameter);
		}
            	// We wait for a Char with information about the analysys of prunning
		// of the TClass
		Character resp = inReader.readChar();
		boolean result = false;
		if(resp.equals('y'))
			result = true;
		else if(resp.equals('n'))
			result = false;
		// We send an ack
		SocketWriter.writeChar(out,'y');
		ResultPrune resultPrune = new ResultPrune(tClassName, theoremName, params, result);
		inReader.close();
		outWriter.close();
        	socket.close();	
		return resultPrune;
        } 
		catch(InvalidClassException e){
			System.out.println("InvalidClassException");
			e.printStackTrace();
			return null;	
		}
		catch(NotSerializableException e){
			System.out.println("NotSerializableException");
			e.printStackTrace(System.out);
			System.out.println(e.getMessage());
			System.out.println(e);
			return null;	
		}
		catch(SocketException e){
			System.out.println("SocketException");
			e.printStackTrace();
			return null; 
		}
		catch(IOException e){
			System.out.println("IOException");
			e.printStackTrace();
			return null;	
		}
		catch(Exception e){
			e.printStackTrace();
			return null;	
		}
	}
}
