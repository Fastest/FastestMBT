package common.util;

import java.util.*;
import common.z.SpecUtils;
import common.z.TClass;
import net.sourceforge.czt.z.ast.DeclList;
import net.sourceforge.czt.z.ast.Pred;

/**
 * Provides general  utilities related to math operations
 */
public class MathUtils {

	public static <X> List<List<X>> getPowerSet(List<X> list)
	{
		List<List<X>> powerSet = new ArrayList<List<X>>();
		int cardinality = (int) Math.pow(2,list.size());
		for(int i=0;i<cardinality;i++){
			List<X> set = new ArrayList<X>();
			String indexSet = intToBinary(i,list.size());
			for(int j=0;j<list.size();j++)
				if(indexSet.charAt(j)=='1')
					set.add(list.get(j));
			powerSet.add(set);
		}
		return powerSet;
	}

	public static <X> List<List<X>> getPowerSet(List<X> list, int maxSize)
	{
		List<List<X>> powerSet = new ArrayList<List<X>>();
		int cardinality = (int) Math.pow(2,list.size());
		//System.out.println("El tamaño de la lista es: "+list.size());
		//System.out.println("La cardinalidad es: "+cardinality);
		for(int i=1;i<cardinality;i++){
			int bits = Integer.bitCount(i);
			//System.out.println("Bitcount: "+bits);
			if(bits < (maxSize+1)){
			List<X> set = new ArrayList<X>();
			String indexSet = intToBinary(i,list.size());
			//System.out.println("El binario es: "+indexSet);
			//for(int j=0;j<list.size();j++)
			//for(int j=0;j<indexSet.length();j++)
			for(int j=indexSet.length()-1;j>-1;j--)
				if(indexSet.charAt(j)=='1'){
					//System.out.println("El indice es: "+(indexSet.length()-1-j));
					set.add(list.get(indexSet.length()-1-j));
				}
			//for(int j=0;j<set.size();j++)
				//System.out.println(SpecUtils.termToLatex(set.get(j)));
			powerSet.add(set);
			}
			//if(set.size()<(maxSize+1))
			//	powerSet.add(set);
		}
		return powerSet;
	}

	public static <X> List<List<X>> getPermutations(List<X> list)
	{ 
		List<X> aux = new ArrayList<X>();
		List<List<X>> finalList = new ArrayList<List<X>>();
		getPermutations(aux, list, finalList); 
		/*for(int i=0;i<finalList.size();i++){
			System.out.println("Permutacion "+i+":");
			List<X> imp = finalList.get(i);
			for(int j=0;j<imp.size();j++)
				System.out.print(imp.get(j)+" ");
		}*/
		return finalList;
	}

	private static <X> void getPermutations(List<X> aux, List<X> list, List<List<X>> finalList)
	{
		int size = list.size();
		if (size == 0) 
			finalList.add(aux);
		else {
			for (int i = 0; i < size; i++){
			List<X> aux2 = new ArrayList<X>();
			aux2.addAll(aux);
			aux2.add(list.get(i));
			List<X> aux3 = new ArrayList<X>();
			aux3.addAll(list);
			X removed = aux3.remove(i); 
			getPermutations(aux2, aux3, finalList);
			}	
		}
	}


	/**
	* Gets a string that represent the binary representation of a integer using ndigits 
	* @param description
	*/
	private static String intToBinary(int binary, int ndigits) {
	
	String temp = Integer.toBinaryString(binary);
	return temp;
	}


}