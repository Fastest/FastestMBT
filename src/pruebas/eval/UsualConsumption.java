package pruebas.eval;


// Itera de 0 a 9999999 e imprime  un mensaje
// Probar en conjunto con el comando top de Unix para ver el consumo minimo de memoria del proceso de la JVM, en comparación
// al que realiza la clase ZLiveConsumption
public class UsualConsumption{

	public static void main(String[] args) {

		for(int i=0; i<1000000; i++){
			System.out.println("Probando");
		}
	}
} 
