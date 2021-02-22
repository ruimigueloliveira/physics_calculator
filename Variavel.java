
public class Variavel {
	
	private String nome;
	private Unidade unidade; // distancia, veloridade ...
	private Double valor; // 2, 4.5 ...
	
	public Variavel(Unidade u, String n) {
		if (verify(u)) {
			unidade = u;
			//valor = d;
			nome = n;
		} else { //Completar!!
		}
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(double v) {
		valor = v;
	}

	public String getNome() {
		return nome;
	}

	public String getTipo() {
		return unidade.getTipo();
	}
	
	public boolean verify(Unidade u) { // Completar
		
		return true;
	}

	public boolean equals(Variavel v) {
		return nome.equals(v.getNome());
	}
	
	public String toString() {
		String s = " " + unidade;
		if (valor != null) {
			if (unidade.getTipo().equals("int")) return (int) ((double) valor) + s;
			else return valor + s;
		}
		return s;
	}
	

}
