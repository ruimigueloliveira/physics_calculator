public class instruction2{

	public static void main(String[] args){
		double b = 100*1000.0;
		mostrarGrandezas(b);
	}


	public static void mostrarGrandezas(double b){
		if((b)>0.001 && (b)<10000) System.out.printf("%.4f %s",b,"s^(-6) m^(6) ");
		else System.out.printf("%e %s",b,"s^(-6) m^(6) ");
	}

}