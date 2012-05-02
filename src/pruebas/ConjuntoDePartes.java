package pruebas;

import java.io.*;
import java.util.*;

public class ConjuntoDePartes{
	public static void main(String[] args) {	
		
		Reader in1 = new InputStreamReader(System.in);
		String token = "";
	
		List<List<String>> listList = new ArrayList<List<String>>();
		char character[] = new char[1];


		List<String> strList = new ArrayList<String>();
		int c = 0;

		try{

			List<String> linesList = new ArrayList<String>();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String line = in.readLine();

			String[] tokens = line.split(" ");
			List<String> wordList = new ArrayList<String>();
			for(int k=0; k<tokens.length; k++)
				wordList.add(tokens[k]);

			System.out.println();
			System.out.println();

			//Tomamos todas las combinaciones de elementos de la lista usando la representacion binaria de los numeros entre 0 y
			//2 elevado al tamaño de la lista, menos uno, de forma tal de agregar a la lista solo los elementos asociados a 
			//un bit en 1.
			int size = wordList.size();
			int powerSize = (int) Math.pow((double)2,(double)size);
			for(int i=0; i<powerSize; i++){
				List<String> auxList = new ArrayList<String>();
				int aux = i;
				for(int j=0; j<size; j++)
					if((i & 1<<j) != 0)
						auxList.add(wordList.get(j));	
				listList.add(auxList);
			}

			System.out.println("{");
			for(int i=0; i<listList.size(); i++){
				List<String> auxList = listList.get(i);
				System.out.print("{");
				for(int j=0; j<auxList.size()-1;j++)
					System.out.print(auxList.get(j) + ",");
				if(auxList.size()>0)
					System.out.print(auxList.get(auxList.size()-1));
				System.out.println("}");

			}
			System.out.println("}");

		}
		catch(Exception e){
			e.printStackTrace();

		}

	}
}
 
