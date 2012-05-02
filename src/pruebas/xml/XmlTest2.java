package pruebas.xml;

import java.io.File;
import java.io.StringWriter;

import net.sourceforge.czt.z.jaxb.JaxbXmlReader;
import net.sourceforge.czt.z.jaxb.JaxbXmlWriter;

import net.sourceforge.czt.base.ast.Term;
import net.sourceforge.czt.base.util.*;

//Lee un archivo en .xml, obtiene el termino y lo vuelve a imprimir en xml
public class XmlTest2
{
 /**
  * Reads and writes back the given ZML file.
  */
 public static void main(String[] args)
   throws Exception
 {

 	if(args.length!=1){
		System.out.println("Ingresar nombre de archivo!!!!");
		return;
	}



   XmlReader reader = new JaxbXmlReader();
   Term term = reader.read(new File(args[0]));
   XmlWriter writer = new JaxbXmlWriter();
   StringWriter out = new StringWriter();
   writer.write(term, out);
   System.out.println(out.toString());
 }


} 
