

public class Unidade {

	private boolean isSec = false;
	private String tipo; // int ou double
	private String simbolo; // m, s, m/s
	private String nome; //velocidade, tempo

	public Unidade(String s1, String s2, String s3) {
		if (verify(s1, s2)) {
			tipo = s1;
			simbolo = s2;
			nome = s3;
		} else {
			throw new IllegalArgumentException();
		}
		
	}

	public boolean verify(String s1, String s2) {
		return !s1.equals(s2) && (s1.equals("int") || s1.equals("double"));
	}

	// Getters
	public String getTipo() {
		return tipo;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public String getNome() {
		return nome;
	}

	public boolean getIsSec() {
		return isSec;
	}

	public void setIsSec() {
		isSec = true;
	}
	
	public String toString() {
		return simbolo;
	}

	public boolean equals(Unidade uni) {
		return (nome.equals(uni.getNome())) || (simbolo.equals(uni.getSimbolo()));
	}
	
}
