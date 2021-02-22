import java.util.*;

public class TestDataStrt2 {
    public static void main(String[] args) {

        DataStrt ds = new DataStrt();

        ds.inserirUniPrim("distancia", "m", "int");
        ds.inserirUniPrim("tempo", "s", "int");
        
        // simples
        HashMap<String, Integer> map = new HashMap<>();
		map.put("distancia", 1);
		map.put("tempo", -1);
        ds.inserirUniSec("velocidade", map);
		ds.inserirVar("velocidade", "vel");
        ds.printVar("vel", 0);
        
        // com unidade secundaria (velocidade)
        HashMap<String,Integer> map2 = new HashMap<>();
        map2.put("velocidade", 1);
        map2.put("tempo", -1);
        ds.inserirUniSec("aceleracao", map2);
        ds.inserirVar("aceleracao", "ac");
        ds.printVar("ac", 0);

        // com unidade elevada a 0
        HashMap<String,Integer> map3 = new HashMap<>();
        map3.put("velocidade", 1);
        map3.put("tempo", 1);
        ds.inserirUniSec("aceleracao2", map3);
        ds.inserirVar("aceleracao2", "ac2");
        ds.printVar("ac2", 0);

        // erro ao adicionar unidade inexistente
        HashMap<String,Integer> map4 = new HashMap<>();
        map4.put("temperatura", 1);
        map4.put("tempo", -1);
        ds.inserirUniSec("tempTempo", map4);
        ds.inserirVar("tempTempo", "tt");
        ds.printVar("tt", 0);

        // com unidade secundaria (velocidade)
        HashMap<String,Integer> map5 = new HashMap<>();
        map5.put("velocidade", -1);
        map5.put("tempo", 1);
        ds.inserirUniSec("aceleracao3", map5);
        ds.inserirVar("aceleracao3", "ac3");
        ds.printVar("ac3", 0);

    }
}