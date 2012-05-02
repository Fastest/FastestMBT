package compserver.management;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.z.jaxb.JaxbXmlReader;
import net.sourceforge.czt.base.util.XmlReader;
import java.io.*;
import java.net.*;

import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

public class SocketReader
{
	//private Socket socket;	
	//private InputStream in;
	//private InputStreamReader reader;
	//private BufferedReader bufferedReader;

	SocketReader()
	{
	}

	public static synchronized Spec readSpec(InputStream in)
	{
		Spec spec=null;
		try
		{
		DataInputStream inReader = new DataInputStream(in);
		// We obtain the size of the compressed file that contains the Spec
		long fileSize = inReader.readLong();
		//System.out.println("El tamaño del archivo es: "+fileSize);
		// We obtain the file that contains the Spec
		int	random = (int)(Math.random()*1000000);
		int count;
		byte[] data = new byte[2048]; 
		String fileName = "servertempSP" + random + ".temp";	
		File tempFile = new File(fileName);
		FileOutputStream fileWriter = new FileOutputStream(fileName);
		int bytesRead = 0;
		byte[] buf = new byte[2048];  
		int c;  
		while(bytesRead  < fileSize){  
			if(fileSize - bytesRead > 2048){  
			c = in.read(buf, 0, 2048);  
			} else{  
			c = in.read(buf, 0, (int)(fileSize - bytesRead));  
			}  
			bytesRead += c;  
			fileWriter.write(buf, 0, c);  
		}     
		fileWriter.close();  
		//Descomprimimos el fichero
		random = (int)(Math.random()*1000000);
		String fileNameUnZip = "servertempSPZ" + random + ".temp";	
		File tempFileUnZip = new File(fileNameUnZip);
		BufferedOutputStream dest = null;
		FileInputStream fis = new FileInputStream(tempFile);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry;
		if((entry = zis.getNextEntry()) != null)
		{
		String entryName = entry.getName();
		fileWriter = new FileOutputStream(fileNameUnZip);
		dest = new BufferedOutputStream(fileWriter,2048);
		while( (count = zis.read(data,0,2048)) != -1)
			dest.write( data, 0, count );
		dest.flush();
		dest.close();
		}
		zis.close();
		// Parseamos la especificacion descomprimida
		XmlReader xmlReader = new JaxbXmlReader();
		spec = (Spec) xmlReader.read(tempFileUnZip);
		tempFile.delete();
		tempFileUnZip.delete();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return spec;		
	}
	public static synchronized AxPara readAxPara(InputStream in)
	{
		AxPara axPara=null;
		try
		{
		DataInputStream inReader = new DataInputStream(in);
		// We obtain the size of the compressed file that contains the AxPara
		long fileSize = inReader.readLong();
		//System.out.println("El tamaño del archivo es: "+fileSize);
		// We obtain the file that contains the AxPara
		int random = (int)(Math.random()*1000000);
		int count;
		byte[] data = new byte[2048]; 
		int c;
		String fileName = "servertempCL" + random + ".temp";	
		File tempFile = new File(fileName);
		FileOutputStream fileWriter = new FileOutputStream(fileName);
		int bytesRead = 0;
		byte[] buf = new byte[2048];
		while(bytesRead  < fileSize){  
			if(fileSize - bytesRead > 2048){  
			c = in.read(buf, 0, 2048);  
			} else{  
			c = in.read(buf, 0, (int)(fileSize - bytesRead));  
			}  
			bytesRead += c;  
			fileWriter.write(buf, 0, c);  
		}     
		fileWriter.close();  
		//Descomprimimos el fichero
		random = (int)(Math.random()*1000000);
		String fileNameUnZip = "servertempCLZ" + random + ".temp";	
		File tempFileUnZip = new File(fileNameUnZip);
		BufferedOutputStream dest = null;
		FileInputStream fis = new FileInputStream(tempFile);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry;
		if((entry = zis.getNextEntry()) != null)
		{
		String entryName = entry.getName();
		fileWriter = new FileOutputStream(fileNameUnZip);
		dest = new BufferedOutputStream(fileWriter,2048);
		while( (count = zis.read(data,0,2048)) != -1)
			dest.write( data, 0, count );
		dest.flush();
		dest.close();
		}
		zis.close();
		// Parseamos el AxPara recibido del archivo tempFile y lo mostramos por pantalla
		XmlReader xmlReader = new JaxbXmlReader();
		axPara = (AxPara) xmlReader.read(tempFileUnZip);
		tempFile.delete();
		tempFileUnZip.delete();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return axPara;

	}

	public static synchronized int readInt(InputStream in)
	{
		try
		{
		DataInputStream inReader = new DataInputStream(in);
		int result = inReader.readInt();
		return result;
		}
		catch(Exception e)
		{
			System.out.println("There was an error while reading an integer from the socket");
			e.printStackTrace();
		}
		return -1;
	} 
	public static synchronized Character readChar(InputStream in)
	{
		try
		{
		DataInputStream inReader = new DataInputStream(in);
		Character result = inReader.readChar();
		return result;
		}
		catch(Exception e)
		{
			System.out.println("There was an error while reading a char from the socket");
			e.printStackTrace();
		}
		return null;
	}
	public static synchronized void readBytes(InputStream in, byte[] param)
	{
		try
		{
		DataInputStream inReader = new DataInputStream(in);
		inReader.read(param);
		return;
		}
		catch(Exception e)
		{
			System.out.println("There was an error while reading an array of bytes from the socket");
			e.printStackTrace();
		}
		return;
	} 
}