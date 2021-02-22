import java.util.*;

public class TestDataStrtOp {
    public static void main(String[] args) {

        int i = 0;
        DataStrt ds = new DataStrt();
        ds.inserirUniPrim("distancia", "m", "int");                                                     // distancia
        ds.inserirUniPrim("tempo", "s", "int");                                                         // tempo
        HashMap<String, Integer> map = new HashMap<>();
		map.put("distancia", 1);
		map.put("tempo", -1);
        ds.inserirUniSec("velocidade", map);                                                            // velocidade
        HashMap<String, Integer> map2 = new HashMap<>();
		map2.put("distancia", 1);
        System.out.println(ds.inserirUniSec("distancia2", map2));                                                           // distancia2

        System.out.println(1 + " " + ds.unidadeOp(ds.getUni("distancia"), ds.getUni("distancia"), "-"));          // m + m = m
        System.out.println(2 + " " + ds.unidadeOp(ds.getUni("distancia"), ds.getUni("distancia"), "*"));          // m * m = m^(2) = m
        System.out.println(3 + " " + ds.unidadeOp(ds.getUni("distancia"), ds.getUni("distancia"), "/"));          // m / m = nada visto que a unidade anulou-se
        System.out.println(4 + " " + ds.unidadeOp(ds.getUni("distancia"), ds.getUni("tempo"), "+"));              // m + s = null, unidades diferentes
        System.out.println(5 + " " + ds.unidadeOp(ds.getUni("distancia"), ds.getUni("velocidade"), "-"));         // m + m^(1) s^(-1) = null, unidades diferentes
        System.out.println(6 + " " + ds.unidadeOp(ds.getUni("velocidade"), ds.getUni("tempo"), "*"));             // m^(1) s^(-1) * s = m^(1)
        System.out.println(7 + " " + ds.unidadeOp(ds.getUni("tempo"), ds.getUni("velocidade"), "*"));             // m^(1) s^(-1) * s = m^(1)
        System.out.println(8 + " " + ds.unidadeOp(ds.getUni("velocidade"), ds.getUni("distancia2"), "+"));        // m^(1) s^(-1) + m^(1) = null, unidades diferentes
        System.out.println(9 + " " + ds.unidadeOp(ds.getUni("velocidade"), ds.getUni("velocidade"), "*"));        // m^(1) s^(-1) * m^(1) s^(-1) = m^(2) s^(-2)
        System.out.println(10 + " " + ds.unidadeOp(ds.getUni("velocidade"), ds.getUni("velocidade"), "/"));        // m^(1) s^(-1) / m^(1) s^(-1) = nada visto que a unidade anulou-se

        System.out.println(11 + " " + ds.compararUni(ds.getUni("distancia"), ds.getUni("tempo")));                 // m = t false
        System.out.println(12 + " " + ds.compararUni(ds.getUni("distancia"), ds.getUni("distancia2")));            // m = m^(1) true
        System.out.println(13 + " " + ds.compararUni(ds.getUni("distancia2"), ds.getUni("distancia")));            // m^(1) = m true
        System.out.println(14 + " " + ds.compararUni(ds.getUni("distancia"), ds.getUni("velocidade")));            // m = m^(1) s^(-1) false
        System.out.println(15 + " " + ds.compararUni(ds.getUni("velocidade"), ds.getUni("distancia")));            // m^(1) s^(-1) = m false
        System.out.println(16 + " " + ds.compararUni(ds.getUni("velocidade"), ds.getUni("distancia2")));            // m^(1) s^(-1) = m^(1) false

        System.out.println(17 + " " + ds.unidadeOp(ds.getUni("numero"), ds.getUni("numero"), "-"));
        System.out.println(18 + " " + ds.unidadeOp(ds.getUni("numero"), ds.getUni("tempo"), "+"));
        System.out.println(19 + " " + ds.unidadeOp(ds.getUni("numero"), ds.getUni("numero"), "*"));
        System.out.println(20 + " " + ds.unidadeOp(ds.getUni("tempo"), ds.getUni("numero"), "*"));
        System.out.println(21 + " " + ds.unidadeOp(ds.getUni("numero"), ds.getUni("tempo"), "/"));
        System.out.println(22 + " " + ds.unidadeOp(ds.getUni("velocidade"), ds.getUni("numero"), "*"));
        System.out.println(23 + " " + ds.unidadeOp(ds.getUni("numero"), ds.getUni("velocidade"), "/"));

        System.out.println(24 + " " + ds.compararUni(ds.getUni("velocidade"), ds.getUni("numero")));
        System.out.println(25 + " " + ds.compararUni(ds.getUni("tempo"), ds.getUni("numero")));


    }
}