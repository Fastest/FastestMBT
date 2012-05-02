package client.blogic.management.communic;

import net.sourceforge.czt.z.ast.Spec;
import net.sourceforge.czt.z.ast.AxPara;
import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.z.jaxb.JaxbXmlWriter;
import net.sourceforge.czt.base.util.XmlWriter;
import java.io.*;
import java.net.*;
import compserver.tcasegen.strategies.TCaseStrategy;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import net.sourceforge.czt.session.SectionManager;
import common.z.TClass;

public class SocketWriter
{
	SocketWriter()
	{
	}

	public static synchronized void writeIdOperation(OutputStream out, int id)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
		outWriter.writeInt(id);
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the ID operation to the server");
			e.printStackTrace();
		}
	}

	public static synchronized void writeSpec(OutputStream out, Spec spec)
	{
		DataOutputStream outWriter = new DataOutputStream(out);
		try
		{
            	// We write the specification spec in the tempFile file in xml format
		int random = (int)(Math.random()*1000000);
		String fileName = "clienttempSP" + random + ".temp";	
		File tempFile = new File(fileName);
		FileWriter fileWriter = new FileWriter(tempFile);
		XmlWriter xmlWriter = new JaxbXmlWriter();	
		xmlWriter.write(spec, fileWriter);
		fileWriter.close();
		// We comprise the file before send it to the server
		random = (int)(Math.random()*1000000);
		String fileNameZip = "clienttempSPZ" + random + ".temp";	
		File tempFileZip = new File(fileNameZip);
		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(fileNameZip);
		ZipOutputStream outFile = new ZipOutputStream(new BufferedOutputStream( dest));
		byte[] data = new byte[2048];
		FileInputStream fi = new FileInputStream(tempFile);
		origin = new BufferedInputStream(fi,2048);
		ZipEntry entry = new ZipEntry(fileName);
		outFile.putNextEntry(entry);
		int count;
		while((count = origin.read(data, 0,2048)) != -1)
			outFile.write(data, 0, count);
		origin.close();
		outFile.close();
		//We send to the server the size of the file
		long fileSize = tempFileZip.length();
		outWriter.writeLong(fileSize);
		//System.out.println("El tamaño del archivo Spec: "+fileSize);
		//We send to the server the compressed file that contains the Spec
		fi = new FileInputStream(tempFileZip);
		origin = new BufferedInputStream(fi,2048);
		while((count = origin.read(data, 0,2048)) != -1)
			outWriter.write(data, 0, count);
		origin.close();
		// Considerar los flush()!!!
		tempFile.delete();
		tempFileZip.delete();
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the Spec to the server");
			e.printStackTrace();
		}
	}

	public static synchronized void writeAxPara(OutputStream out, AxPara axPara)
	{
		DataOutputStream outWriter = new DataOutputStream(out);
		try
		{
            // We write the tClass's AxPara instance in the file tempFile in
            // format XML
		int random = (int)(Math.random()*1000000);
		String fileName = "clienttempCL" + random + ".temp";
		File tempFile = new File(fileName);	
		FileWriter fileWriter = new FileWriter(tempFile);
		XmlWriter xmlWriter = new JaxbXmlWriter();
		xmlWriter.write((Term) axPara, fileWriter);	
		fileWriter.close();
		// We comprise the file before send it to the server
		random = (int)(Math.random()*1000000);
		String fileNameZip = "clienttempCLZ" + random + ".temp";	
		File tempFileZip = new File(fileNameZip);
		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(fileNameZip);
		ZipOutputStream outFile = new ZipOutputStream(new BufferedOutputStream( dest));
		byte[] data2 = new byte[2048];
		FileInputStream fi = new FileInputStream(tempFile);
		origin = new BufferedInputStream(fi,2048);
		ZipEntry entry = new ZipEntry(fileName);
		outFile.putNextEntry(entry);
		int count;
		while((count = origin.read(data2, 0,2048)) != -1)
			outFile.write(data2, 0, count);
		origin.close();
		outFile.close();
		//We send to the server the size of the file
		long fileSize = tempFileZip.length();
		outWriter.writeLong(fileSize);
		//System.out.println("El tamaño del archivo AxPara: "+fileSize);
		// We send to the server the compressed file that contains the AxPara
		fi = new FileInputStream(tempFileZip);
		origin = new BufferedInputStream(fi,2048);
		byte[] data = new byte[2048];
		while((count = origin.read(data, 0,2048)) != -1)
			outWriter.write(data, 0, count);
		origin.close();
		// Considerar los flush()!!!
		tempFile.delete();
		tempFileZip.delete();
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the AxPara to the server");
			e.printStackTrace();
		}
	}
	public static synchronized void writeClassName(OutputStream out, String tClassName)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
		int nameSize = tClassName.getBytes().length;
		outWriter.writeInt(nameSize);
		outWriter.writeBytes(tClassName);
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the tClass's name to the server");	
			e.printStackTrace();
		}
	}
	public static synchronized void writeString(OutputStream out, String text)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
		int nameSize = text.getBytes().length;
		outWriter.writeInt(nameSize);
		outWriter.writeBytes(text);
		}
		catch(Exception e)
		{
			System.out.println("There was an error while writing the string "+text+" in the socket");	
			e.printStackTrace();
		}
	}

	public static synchronized void writeChar(OutputStream out, int ch)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
		outWriter.writeChar(ch);
		}
		catch(Exception e)
		{
			System.out.println("There was an error while writing the char "+ch+" in the socket");	
			e.printStackTrace();
		}
	}
	public static synchronized void writeInteger(OutputStream out, int num)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
		outWriter.writeInt(num);
		}
		catch(Exception e)
		{
			System.out.println("There was an error while writing the integer "+num+" in the socket");	
			e.printStackTrace();
		}
	}
	public static synchronized void writeSectionName(OutputStream out, String sectionName)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
		int nameSize = sectionName.getBytes().length;
		outWriter.writeInt(nameSize);
		outWriter.writeBytes(sectionName);
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the section name of the Spec to the server");	
			e.printStackTrace();
		}
	}
	public static synchronized void writeCaseStrategy(OutputStream out, TCaseStrategy tCaseStrategy)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
                // We serialize the tCaseStrategy object into the file tempFile
		int random = (int)(Math.random()*1000000);
		String fileName = "clienttempTR" + random + ".temp";	
		File tempFile3 = new File(fileName);
		ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream(tempFile3));
		objOutputStream.writeObject(tCaseStrategy);
		objOutputStream.close();
		//We send to the server the file size
		long fileSize = tempFile3.length();
		outWriter.writeLong(fileSize);
		//System.out.println("El tamaño de la estrategia es: "+fileSize);
            // We send to the server the tCaseStrategy object sending the content
            // of the file tempFile
		FileInputStream fileInputStream = new FileInputStream(tempFile3);
        	int c;
        	while ((c = fileInputStream.read()) != -1) {
                outWriter.write(c);
		outWriter.flush();
        	}	
		//outWriter.write(1000);
		//outWriter.flush();
		tempFile3.delete();
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the TCaseStrategy to the server");
			e.printStackTrace();
		}
	}
	public static synchronized void writeSectionManager(OutputStream out, SectionManager manager)
	{
		DataOutputStream outWriter = new DataOutputStream(out);
		try
		{
		System.out.println("LLega 1");
		int random = (int)(Math.random()*1000000);
		String fileName = "clienttempCL" + random + ".temp";
		File tempFile = new File(fileName);	
		FileWriter fileWriter = new FileWriter(tempFile);
		XmlWriter xmlWriter = new JaxbXmlWriter();
		xmlWriter.write((Term) manager, fileWriter);	
		System.out.println("LLega 2");
		fileWriter.close();
		// We comprise the file before send it to the server
		random = (int)(Math.random()*1000000);
		String fileNameZip = "clienttempCLZ" + random + ".temp";	
		File tempFileZip = new File(fileNameZip);
		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(fileNameZip);
		ZipOutputStream outFile = new ZipOutputStream(new BufferedOutputStream( dest));
		byte[] data2 = new byte[2048];
		FileInputStream fi = new FileInputStream(tempFile);
		origin = new BufferedInputStream(fi,2048);
		ZipEntry entry = new ZipEntry(fileName);
		outFile.putNextEntry(entry);
		int count;
		while((count = origin.read(data2, 0,2048)) != -1)
			outFile.write(data2, 0, count);
		origin.close();
		outFile.close();
		//We send to the server the size of the file
		long fileSize = tempFileZip.length();
		outWriter.writeLong(fileSize);
		//System.out.println("El tamaño del archivo AxPara: "+fileSize);
		// We send to the server the compressed file that contains the AxPara
		fi = new FileInputStream(tempFileZip);
		origin = new BufferedInputStream(fi,2048);
		byte[] data = new byte[2048];
		while((count = origin.read(data, 0,2048)) != -1)
			outWriter.write(data, 0, count);
		origin.close();
		// Considerar los flush()!!!
		tempFile.delete();
		tempFileZip.delete();
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the Manager to the server");
			e.printStackTrace();
		}
	}
	public static synchronized void writeTClass(OutputStream out, TClass tClass)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
                // We serialize the manager object into the file tempFile
		int random = (int)(Math.random()*1000000);
		String fileName = "clienttempCL" + random + ".temp";	
		File tempFile3 = new File(fileName);
		ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream(tempFile3));
		objOutputStream.writeObject(tClass);
		objOutputStream.close();
		//We send to the server the file size
		long fileSize = tempFile3.length();
		outWriter.writeLong(fileSize);
		//System.out.println("El tamaño de la estrategia es: "+fileSize);
            // We send to the server the tCaseStrategy object sending the content
            // of the file tempFile
		FileInputStream fileInputStream = new FileInputStream(tempFile3);
        	int c;
        	while ((c = fileInputStream.read()) != -1) {
                outWriter.write(c);
		outWriter.flush();
        	}	
		//outWriter.write(1000);
		//outWriter.flush();
		tempFile3.delete();
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the TClass to the server");
			e.printStackTrace();
		}
	}


	public static void writeSpecHash(OutputStream out, int hashSpec)
	{
		try
		{
		DataOutputStream outWriter = new DataOutputStream(out);
		outWriter.writeInt(hashSpec);
		}
		catch(Exception e)
		{
			System.out.println("There was an error while sending the hash of the Spec");
			e.printStackTrace();
		}
	}

}