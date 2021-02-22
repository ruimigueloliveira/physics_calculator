import java.util.HashMap;

public class UnidadeSec extends Unidade {

    private HashMap<String, Integer> unidades;

    public UnidadeSec(String tipo, String simbolo, String nome, HashMap<String, Integer> m) {
        super(tipo, simbolo, nome);
        unidades = m;
        setIsSec(); // Define Unidade as Sec
    }

    public HashMap<String, Integer> getUnidades() {
        return unidades;
    }

    public boolean equals(UnidadeSec uni) {
        return uni.getUnidades().equals(unidades);
    }

}
