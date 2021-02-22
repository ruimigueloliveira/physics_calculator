import java.util.*;

public class DataStrt {
	
	HashMap<String, Unidade> unidades;
	//HashMap<String, Variavel> variaveis;
	ArrayList<HashMap<String, Variavel>> variaveis;
	
	public DataStrt() {
		unidades = new HashMap<>();
		//variaveis = new HashMap<>();
		variaveis = new ArrayList<>();
		variaveis.add(new HashMap<String, Variavel>());
		inserirUniPrim("numero", "", "double");
		//Multiplicadores são constantes do tipo "numero"
		inserirConst("numero", "Y", Math.pow(10, 24));
		inserirConst("numero", "Z", Math.pow(10, 21));
		inserirConst("numero", "E", Math.pow(10, 18));
		inserirConst("numero", "P", Math.pow(10, 15));
		inserirConst("numero", "T", Math.pow(10, 12));
		inserirConst("numero", "G", Math.pow(10, 9));
		inserirConst("numero", "M", Math.pow(10, 6));
		inserirConst("numero", "k", Math.pow(10, 3));
		inserirConst("numero", "h", Math.pow(10, 2));
		inserirConst("numero", "da", Math.pow(10, 1));
		inserirConst("numero", "d", Math.pow(10, -1));
		inserirConst("numero", "c", Math.pow(10, -2));
		inserirConst("numero", "m", Math.pow(10, -3));
		inserirConst("numero", "u", Math.pow(10, -6));
		inserirConst("numero", "n", Math.pow(10, -9));
		inserirConst("numero", "p", Math.pow(10, -12));
		inserirConst("numero", "f", Math.pow(10, -15));
		inserirConst("numero", "a", Math.pow(10, -18));
		inserirConst("numero", "z", Math.pow(10, -21));
		inserirConst("numero", "y", Math.pow(10, -24));

	}
	
	public String inserirUniPrim(String nome, String simbolo, String tipo) { // Inserir unidade primária
		Unidade nova = new Unidade(tipo, simbolo, nome);
		String verify = verifyUniPrim(nome, nova);
		if(verify.equals("")) {
			if(unidades.put(nome, nova)!=null)
				return "Erro não previsto ao adicionar unidade primária \""+nome+"\"!";
		}
			
		return verify;
	}
	
	private String verifyUniPrim(String nome, Unidade uni) { // Verificar inserts de uma unid primária
		
		boolean existsAlready = false;
		
		for (Map.Entry<String, Unidade> entry : unidades.entrySet()) {
			if(entry.getValue().equals(uni)) {
				existsAlready = true;
				break;
			}
		}
		
		if(unidades.containsKey(nome))
			return "Unidade \""+nome+"\" já existe!";
		if(nome.equals(uni.getTipo()))
			return "Não se pode criar unidade com nome 'int' ou 'double'!";
		if(nome.equals(uni.getSimbolo()))
			return "Já existe um símbolo com o nome \""+nome+"\" (não tenho a certeza se o relato é este)";
		if(existsAlready)
			return "Unidade já existe2!";//Não percebo bem aqui
		
		return "";
		
	}

	public String inserirUniSec(String nome, HashMap<String, Integer> map) { // Completar o símbolo!!!
		UnidadeSec nova;
		HashMap<String, Integer> novoMap = new HashMap<>();
		boolean i = true;
		boolean div = false;
		String simbolo = "";
		String tipo;
		if (verifyUniSec(nome).equals("")) {
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if (!unidades.containsKey(entry.getKey())) {
					return "Unidade "+entry.getKey()+" não existe!"; //não existe esta unidade na estrutura
				}
				Unidade n = unidades.get(entry.getKey());
				if (n.getTipo().equals("int")) i = false; //existe pelo menos um double ent o tipo = "double"
				if (entry.getValue() < 0) div = true; //como existe uma divisão ent o tipo = "double"
				if (n.getIsSec()) {
					for (Map.Entry<String, Integer> entry2 : ((UnidadeSec) n).getUnidades().entrySet()) {
						//System.out.println(entry2.getKey() + " " + entry2.getValue());
						if (n.getTipo().equals("int")) i = false; //existe pelo menos um double ent o tipo = "double"
						if (entry2.getValue() < 0) div = true; //como existe uma divisão ent o tipo = "double"
						if (novoMap.containsKey(entry2.getKey())){ novoMap.put(entry2.getKey(), novoMap.get(entry2.getKey()) + entry.getValue() * entry2.getValue()); }
						else novoMap.put(entry2.getKey(), entry.getValue() * entry2.getValue());
						//if (entry2.getValue() != 0) novoMap.put(entry2.getKey(), entry2.getValue());
					}
				} else {
					//System.out.println(entry.getKey() + " " + entry.getValue());
					if (novoMap.containsKey(entry.getKey())) { novoMap.put(entry.getKey(), novoMap.get(entry.getKey()) + entry.getValue()); }
					else if (entry.getValue() != 0) { novoMap.put(entry.getKey(), entry.getValue());}
					//if (novoMap.get(entry.getKey()) == 0) { novoMap.remove(entry.getKey()); System.out.println("err");}
					//System.out.println(entry.getKey() + " " + entry.getValue());
				}
				//System.out.println(entry.getKey() + " " + entry.getValue());
			}
			if (!i || div) tipo = "double";
			else tipo = "int";
			for (Map.Entry<String, Integer> entry : novoMap.entrySet()) {
				if (novoMap.get(entry.getKey()) == 0) { 
					novoMap.remove(entry.getKey()); //Remover unidade levantada a 0
					//System.out.println("err");
				} else {
					simbolo = simbolo + unidades.get(entry.getKey()) + "^(" + entry.getValue() + ") ";  //Para fazer o símbolo
				}
			}
			nova = new UnidadeSec(tipo, simbolo, nome, novoMap);
			for (Map.Entry<String, Unidade> entry : unidades.entrySet()) {
				if (compararUni(entry.getValue(), nova)) 
					return "Já existe uma unidade com o nome \""+entry.getValue()+"\"";
			}
			unidades.put(nome, nova);
			return "";
		}
		return verifyUniSec(nome);
	}

	private String verifyUniSec(String nome) {
					
		if(unidades.containsKey(nome))		// Name of unit cannot exist in the database of the 
			return "Já existe uma unidade com o nome \""+nome+"\"";
		return "";
	}

	public Unidade getUni(String uni) {
		return unidades.get(uni);
	}

	public boolean verifyUni(String nome, String simb) {
		//boolean b = true;
		for (Map.Entry<String, Unidade> entry : unidades.entrySet()) {
			if(entry.getValue().getSimbolo().equals(simb))
				return true;
		}
		return unidades.containsKey(nome);
	}
	
	public String inserirVar(String uni, String nome) {
		if (verifyInserirVar(uni, nome).equals("")) {
			//System.out.println("Hey");
			Variavel nova = new Variavel(unidades.get(uni), nome);
			variaveis.get(variaveis.size()-1).put(nome, nova);
			return "";
			//if (variaveis.put(nome, nova) != null) return true; // Como verificar se existe uma variavel igual a esta ?
		} 
		return verifyInserirVar(uni, nome);
	}

	public void destroyContext() {
		if (variaveis.size() > 0) variaveis.remove(variaveis.size()-1);
	}

	public void insertContext() {
		HashMap<String, Variavel> novo = new HashMap<>();
		variaveis.add(novo);
	}
	
	// Verifies if the requested name for var already exists, defining another existing variable or unit
	private String verifyInserirVar(String uni, String nome) {
		if(!unidades.containsKey(uni))
			return "Não existe a unidade \""+uni+"\"";
		if(getVar(nome) != null) {
			if(isPrefix(nome))
				return "Não pode ser criada uma variável com o nome \""+nome+"\" visto que o nome já identifica um prefixo";
			return "Já existe uma variável com o nome \""+nome+"\"";
		}
		return "";
	}
	
	private static boolean isPrefix(String nome) {
		
		String[] prefixes =  {
			"Y","Z","E","P","T","G","M","k",
			"h","da","d","c","m","u","n","p","f","a","z","y"
		};
		
		for(String s : prefixes)
			if(s.equals(nome))
				return true;
				
		return false;
		
	}
	
	public String existsVar(String nome) {
		if(getVar(nome)!=null)
			return "";
		return "Variável \""+nome+"\" não existe!";
	}
	
	public String verifyInitVar(String nome) {
	
		String s;	
		if(!(s = existsVar(nome)).equals(""))
			return s;
		Variavel v = getVar(nome);
		if(v.getValor()==null)
			return "Variável \""+nome+"\" não foi inicializada!";
		return "";
			
	}

	public Variavel getVar(String nome) {
		int cont = 0;
		while(cont < variaveis.size()) {
			if (variaveis.get(cont).containsKey(nome)) {
				//System.out.println("return var");
				return variaveis.get(cont).get(nome);
			}
			cont++;
		}
		return null;
	}
	
	public void printVar(String key, int i) {
		Variavel v = getVar(key);
		if (v != null) {
			System.out.println(key + " = " + v);
		} else { // Completar
			System.out.println("Variável \""+key+"\" não existe!");
		}
	}

	public Unidade unidadeOp(Unidade v1, Unidade v2, String op) { //Retorna null se a variavel não existir ?
		if (v1 == null) return null;
		if (v2 == null) return null;
		String uniNome = "uni";

		if (op.equals("+") || op.equals("-")) {
			if (v1.equals(v2)) return v1; //Se ambas as variaveis foram da mesma unidade pode retornar qualquer uma destas
			else return null; //retorna null visto que não é possível fazer a operação
		} else if (op.equals("*") || op.equals("/")) {
			Unidade novaUni;
			HashMap<String, Integer> novoMap;
			//boolean cloneV2 = false; //para saber se o novoMap foi clonado de v2 para inverter as unidades caso a operação seja "/"
			if (v1.getIsSec()) {
				novoMap = new HashMap<String,Integer>(((UnidadeSec) v1 ).getUnidades()); //clona o map da unidade1
				//novoMap =((UnidadeSec) v1.getUnidade()).getUnidades().clone(); //clona o map da variável v1
				if (v2.getIsSec()) { //Se a unidade da variavel2 for secundaria
					//System.out.println("Olá");
					for (Map.Entry<String, Integer> entry : ((UnidadeSec) v2).getUnidades().entrySet()) {
						//System.out.println(entry.getKey());
						if (novoMap.containsKey(entry.getKey())) { // Se o novoMap contiver esta unidade
							//System.out.println("Olá2");
							if (op.equals("*")) novoMap.put(entry.getKey(), (Integer) (novoMap.get(entry.getKey())) + entry.getValue()); // "*"
							else novoMap.put(entry.getKey(), (Integer) (novoMap.get(entry.getKey())) - entry.getValue()); // "/"
							if ((Integer)(novoMap.get(entry.getKey())) == 0) novoMap.remove(entry.getKey()); //Se a unidade ficar elevada a 0 no novoMap, é apagada
						} else { // Se o novoMap não contiver esta unidade
							if (op.equals("*")) novoMap.put(entry.getKey(), entry.getValue());
							else novoMap.put(entry.getKey(), -entry.getValue());
						}
					}
				} else { //Se a unidade2 não for secundaria
					if (!v2.getNome().equals("numero")) {
						if (novoMap.containsKey(v2.getNome())) { // Se o novoMap contiver a unidade2
							if (op.equals("*")) novoMap.put(v2.getNome(), (Integer) (novoMap.get(v2.getNome())) + 1); // "*"
							else novoMap.put(v2.getNome(), (Integer) (novoMap.get(v2.getNome())) - 1); // "/"
							if ((Integer)(novoMap.get(v2.getNome())) == 0) novoMap.remove(v2.getNome()); //Se a unidade ficar elevada a 0 no novoMap, é apagada
						} else { // Se o novoMap não contiver a unidade2
							if (op.equals("*")) novoMap.put(v2.getNome(), 1);
							else novoMap.put(v2.getNome(), -1);
						}
					}
				}
			}
			else if (v2.getIsSec()) { // unidade1 não é secundaria mas unidade2 é
				novoMap = new HashMap<String,Integer>(((UnidadeSec) v2).getUnidades()); //clona o map da unidade2
				if (op.equals("/")) { // Inverte os expoentes das unidades caso seja um operação "/"
					for (Map.Entry<String, Integer> entry : novoMap.entrySet()) { 
						novoMap.put(entry.getKey(), -entry.getValue());
					}
				}
				if (!v1.getNome().equals("numero")) {
					if (novoMap.containsKey(v1.getNome())) {
						novoMap.put(v1.getNome(), (Integer)(novoMap.get(v1.getNome())) + 1); // Adiciona 1 ao expoente visto que é uma unidade principal
						if ((Integer)(novoMap.get(v1.getNome())) == 0) novoMap.remove(v1.getNome()); //Se a unidade ficar elevada a 0 no novoMap, é apagada
					} else {
						novoMap.put(v1.getNome(), 1); // Coloca 1 no expoente na nova unidade
					}
				}
			}
			else { //nenhuma unidade é secundária

				if (v1.getNome().equals("numero")) {
					if (v2.getNome().equals("numero")) return v1;
				}
				if (v2.getNome().equals("numero")) return v1;

				novoMap = new HashMap<String, Integer>();
				novoMap.put(v1.getNome(), 1);
				if (op.equals("*")) {
					if (v1.getNome().equals(v2.getNome())) {
						novoMap.put(v2.getNome(), 2);
					} else {
						if (v1.getNome().equals("numero")) return v2;
						novoMap.put(v2.getNome(), 1);
					}
				} else {
					//novoMap.put(v1.getNome(), 1);
					if (v1.getNome().equals(v2.getNome())) {
						novoMap.remove(v1.getNome());
					} else {
						if (v1.getNome().equals("numero")) {
							novoMap.remove("numero");
						}
						novoMap.put(v2.getNome(), -1);
					}
				}
			}

			String simbolo = "";
			for (Map.Entry<String, Integer> entry : novoMap.entrySet()) {
				if (novoMap.get(entry.getKey()) == 0) { 
					novoMap.remove(entry.getKey()); //Remover unidade levantada a 0
					//System.out.println("err");
				} else {
					simbolo = simbolo + unidades.get(entry.getKey()) + "^(" + entry.getValue() + ") ";  //Para fazer o símbolo
				}
			} 

			return new UnidadeSec("double", simbolo, uniNome, novoMap); //é preciso calcular este tipo ?
			
		} else { //Completar!!
			//return null; //O return null está destinado a indicar que não é possível fazer a operação!
		}
		return null;
	}

	public boolean compararUni(Unidade uni1, Unidade uni2) {
		if ((uni1 == null) || (uni2 == null)) return false; //Completar!!!
		if (uni2.getNome().equals("numero")) return true;
		if (uni1.getIsSec()) {
			if (uni2.getIsSec()) { //ambas são unidades Secundarias
				return ((UnidadeSec) uni1).equals((UnidadeSec) uni2);
			} else { //uni1 é secundaria mas uni2 é primaria
				HashMap<String, Integer> auxMap = ((UnidadeSec) uni1).getUnidades();
				if (auxMap.containsKey(uni1.getNome())) {
					return ((auxMap.size() == 1) && (auxMap.get(uni2.getNome()) == 1));
				} else {
					return false;
				}
			}
		} else {
			if (uni2.getIsSec()) { //uni1 é primaria mas uni2 é secundaria
				HashMap<String, Integer> auxMap = ((UnidadeSec) uni2).getUnidades();
				//System.out.println(auxMap.size());
				//System.out.println(auxMap.get(uni1.getNome()));
				if (auxMap.containsKey(uni1.getNome())) {
					return ((auxMap.size() == 1) && (auxMap.get(uni1.getNome()) == 1));
				} else {
					return false;
				}
			} else { //ambas são Primarias
				return uni1.equals(uni2);
			}
		}
	}

	public String inserirConst(String uni, String nome, Double val) {
		String x = inserirVar(uni, nome);
		if (x.equals("")) getVar(nome).setValor(val);
		return x;
	}
 
}
