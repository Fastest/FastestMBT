package pruebas;

public class CaracteresEsp{
	public static void main(String[] args) {	
		int[][] codePoints = {{8484}, {8469}, {55349}};
		for(int i=0; i<codePoints.length; i++){
			System.out.println(codePoints[i][0] + " " + new String(codePoints[i],0,1));
		}
	}

}
