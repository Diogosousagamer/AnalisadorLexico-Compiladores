import java.util.ArrayList;
import java.util.HashTable;
import java.util.LinkedList;

public class Automato {

	private ArrayList<Character> alfabeto;
	private Hashtable<Integer,Estado> estados; 	//<Id,Estado>
	private ArrayList<Estado> estadosFinais;
	private Hashtable<Integer,Hashtable<Character,Estado>> funcaoDeTransicao; //<Id, lista de transicoes>

	private Estado estadoInicial;

	public Automato(ArrayList<Character> alfabeto){
		this.alfabeto = alfabeto;
	}

	public Automato(){
		alfabeto = new ArrayList<Character>();
		estados = new Hashtable<>();
		estadosFinais = new Hashtable<>();
	}

	public void addSimbolo(char simbolo){
		alfabeto.add(simbolo);
	}

	public void addEstado(Estado estado){
    //Caso ja exista um estado com este Id
		if (estadoJaExiste(estado.getId())){
			throw new RunTimeException();//Lanca excecao
		}
		if (estado.ehFinal()){
			estadosFinais.add(estado);
		}else if (estado.ehInicial()){
      estadoInicial = estado;
		}
		estados.putIfAbsent(estado.getId(),estado);
    funcaoDeTransicao.putIfAbsent(estado.getId(), new Hashtable<>());
	}

	private boolean estadoJaExiste(int id){
		for (Estado e : estados){
			if (e.getId() == id){
				return true;
			}
		}
		return false;
	}

	public void addTransicao(Hashtable<Character,Estado> transicoes, Estado estado){
    //Caso ja exista um estado com este Id
		if (!estadoJaExiste(estado.getId())){
			throw new RunTimeException();//Lanca excecao
		}

		funcaoDeTransicao.putIfAbsent(estado.getId(), transicoes);
	}

	public Estado funcaoDeTransicaoExtendida(LinkedList<Character> simbolos){
    return funcaoDeTransicaoExtendidaAux(simbolos, estadoInicial);
	}

	private Estado funcaoDeTransicaoExtendidaAux(LinkedList<Character> simbolos, Estado estadoAtual){
    if (!simbolos.isEmpty()){
      char simboloAtual = simbolos.get(0);
      simbolos.remove(0);

      Estado proximoEstado = funcaoDeTransicao.get(estadoAtual.getId()).get(simboloAtual);

      if (proximoEstado.ehFinal()){
        return proximoEstado;
      }

      return funcaoDeTransicaoExtendidaAux(simbolos, proximoEstado);
    }
    return estadoAtual;
	}
}
