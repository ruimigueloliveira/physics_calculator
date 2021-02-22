import java.util.HashMap;

public class TestDataStrt {

	public static void main(String[] args) {

		// for compiling and executing: in src folder
		// $./testDataStrt.sh

		DataStrt ds = new DataStrt();
		System.out.println("-> "+ds.verifyUni("Distancia", "m"));
		System.out.println("-> "+ds.verifyUni("Tempo", "s"));

		System.out.println("-> "+ds.inserirUniPrim("distancia", "m", "int")
			+ "\n-> "+ ds.inserirUniPrim("tempo", "s", "int")
			+ "\n-> "+	ds.inserirVar("distancia", "casaAUni"));
		ds.printVar("casaAUni", 0);
		System.out.println("-> "+ds.verifyUni("temp", "o"));
		
		String[] varNames = {"aLisboa", ""},//...
			uniNames = {"distancia", "massa", "tempo", "mole", "Ampere", "Temperatura"},
			uniSymbols = {"m", "g", "s", "mol", "A", "K"},
			uni2Names = {"velocity", "Hertz", "Coulomb", "Volts"},
			uni2Symbols = {"v", "Hz", "C", "V"}
			;
			
		if(uniNames.length != uniSymbols.length) {
			System.err.println("Os dois arrays têm de ter a mesma dimensão");
			System.exit(1);
		}
		for(int i = 0 ; i < uniNames.length ; i ++)
		{
			System.out.println("-> "+ds.inserirUniPrim(uniNames[i], uniSymbols[i], "int"));
		}
		
		if(uni2Names.length != uni2Symbols.length) {
			System.err.println("Os dois arrays têm de ter a mesma dimensão");
			System.exit(1);
		}
		for(int i = 0 ; i < uni2Names.length ; i++)
		{
			//ds.insertUniSec(uni2Names[i], uni2Symbols[i], /*hashMap...Maybe?/, "double");
		}

		HashMap<String, Integer> map = new HashMap<>();
		map.put("distancia", 1);
		map.put("tempo", -1);
		System.out.println("-> "+ds.inserirUniSec("velocidade", map)
			+ "\n-> " + ds.inserirVar("velocidade", "vel"));
		
		ds.printVar("velocidade", 0);
	}

}
