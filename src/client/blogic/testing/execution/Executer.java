package client.blogic.testing.execution;

import java.io.*;

public class Executer{

	public static void execute(String comando, String workingDir){
		try{
		Process p = Runtime.getRuntime().exec(comando, null,new File(workingDir)); 
		int x = p.waitFor();

            // We obtain the error stream
            InputStream is = p.getErrorStream();
            BufferedReader br = new BufferedReader (new InputStreamReader (is));
            String aux = br.readLine();
	    if(aux!=null)
	    	System.out.println("There was an error while executing\n"+comando);
            while (aux!=null)
            {
                System.out.println (aux);
                aux = br.readLine();
            }



            // We obtain the input stream
            InputStream is2 = p.getInputStream();
            BufferedReader br2 = new BufferedReader (new InputStreamReader (is2));
            String aux2 = br2.readLine();
	    if(aux2!=null)
	    	System.out.println("There was an error while executing\n"+comando);
            while (aux2!=null)
            {
                System.out.println (aux2);
                aux2 = br2.readLine();
            }


	    } catch(Exception e){
		e.printStackTrace(System.out);
	    }

	}
}


