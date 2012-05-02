package pruebas.eval;

import net.sourceforge.czt.animation.eval.ZLive;
import java.util.ArrayList;

// Agrega a una lista 50 objetos ZLive para mostrar como terminan consumiendo la memoria, 
// salvo que se amplie la que le corresponde a la JVM
// Puede ser util usar el comando top de Unix para monitorear el consumo de memoria por proceso
public class ZLiveConsumption{

	public static void main(String[] args) {	

		ArrayList<ZLive> list = new ArrayList<ZLive>();
		for(int i=0; i<50; i++){
			System.out.println(i);
			list.add(new ZLive());
	}

	}
} 
