package pruebas;

import java.io.*;
import java.util.*;

public class Cartesiano{
	public static void main(String[] args) {	
		
		Reader in1 = new InputStreamReader(System.in);
		String token = "";
	
		List<List<String>> listList = new ArrayList<List<String>>();
		char character[] = new char[1];


		List<String> strList = new ArrayList<String>();
		int c = 0;

		try{


			String line = "";
			List<String> linesList = new ArrayList<String>();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while((line = in.readLine())!= null)
				linesList.add(line);


			for(int j=0; j < linesList.size(); j++){
				String[] tokens = linesList.get(j).split(" ");
				List<String> wordList = new ArrayList<String>();
				for(int k=0; k<tokens.length; k++)
					wordList.add(tokens[k]);
					
				listList.add(wordList);
			}
			

			System.out.println();
			System.out.println();

			int m = listList.size();

			int product=1;
			int[] listSizes = new int[m], a = new int[m];

			for(int i=0; i<m; i++){
				int size = listList.get(i).size();
				product *= size;
				listSizes[i] = size;
				a[i] = 0;
			}

			System.out.print("");
			for(int i=0; i<product; i++){
				System.out.print("(");
				for(int j=0; j<m-1; j++)
					System.out.print(listList.get(j).get(a[j]) + ",");
				System.out.println(listList.get(m-1).get(a[m-1]) + ")");
				a[0]++;
				for(int j=0; j<m-1; j++){
					if(a[j] == listSizes[j]){
						a[j+1]++;
						a[j] = 0;
					}
				}
			}


		}
		catch(Exception e){
			e.printStackTrace();

		}

	}
}
 
