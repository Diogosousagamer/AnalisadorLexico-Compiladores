/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 19/08/2026
* Ultima alteracao...: 13/09/2026
* Nome...............: Automato
* Funcao.............: Classe que designa as operacoes de um automato.
                     
*************************************************************** */

package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class Automato {
	// Variaveis e instancias
	private ArrayList<Character> alfabeto;
	private Hashtable<Integer, Estado> estados; 	//<Id,Estado>
	private Hashtable<Integer, Estado> estadosFinais;
	private Hashtable<Integer, Hashtable<Character, Integer>> funcaoDeTransicao; //<Id, lista de transicoes>
	private Estado estadoInicial;
	private Estado estadoAtual;

	/*
   * ***************************************************************
   * Metodo: Automato
   * Funcao: inicializa uma nova instancia da classe Automato
   * Parametros: ArrayList<Character> alfabeto - lista de simbolos de entrada
   * Retorno: nenhum
   ****************************************************************/

	public Automato(ArrayList<Character> alfabeto) {
		this.alfabeto = alfabeto;
		estados = new Hashtable<>();
		estadosFinais = new Hashtable<>();
		funcaoDeTransicao = new Hashtable<>();
	}
	
	/*
   * ***************************************************************
   * Metodo: Automato
   * Funcao: inicializa uma nova instancia da classe Automato
   * Parametros: nenhum parametro foi definido para esta funcao
   * Retorno: nenhum
   ****************************************************************/

	public Automato() {
		alfabeto = new ArrayList<Character>();
		estados = new Hashtable<>();
		estadosFinais = new Hashtable<>();
		funcaoDeTransicao = new Hashtable<>();
	}

	/*
   * ***************************************************************
   * Metodo: addSimbolo
   * Funcao: adiciona um novo simbolo no alfabeto de entrada
   * Parametros: char simbolo - simbolo (caractere) a ser adicionado
   * Retorno: void
   ****************************************************************/

	public void addSimbolo(char simbolo) {
		alfabeto.add(simbolo);
	}

	/*
   * ***************************************************************
   * Metodo: addEstado
   * Funcao: adiciona um novo estado para o automato
   * Parametros: Estado estado - estado a ser adicionado
   * Retorno: void
   ****************************************************************/

	public void addEstado(Estado estado) {
    // Caso ja exista um estado com este Id
		if (estadoJaExiste(estado.getId())) {
			throw new RuntimeException(); // Lanca excecao
		}

		// Se o estado for final
		if (estado.ehFinal()) {
			estadosFinais.putIfAbsent(estado.getId(), estado); // Adiciona o estado na lista de estados finais
		}
		
		if (estado.ehInicial()) { // Se o estado for inicial
			// Marca ele como o estado inicial do automato
      estadoInicial = estado;
		}

		// Adiciona o estado na tabela hash de estados
		estados.putIfAbsent(estado.getId(), estado);

		// Inicializa uma tabela hash de transicoes para o estado adicionado
    funcaoDeTransicao.putIfAbsent(estado.getId(), new Hashtable<>());
	}

  /*
   * ***************************************************************
   * Metodo: estadoJaExiste
   * Funcao: verifica se um estado especifico faz parte do automato
   * Parametros: int id - identificador do estado a ser verificado
   * Retorno: boolean
   ****************************************************************/

	private boolean estadoJaExiste(int id) {
		return estados.containsKey(id);
	}

	/*
   * ***************************************************************
   * Metodo: addEstado
   * Funcao: adiciona uma nova transicao para determinado estado
   * Parametros: Estado partida - estado de partida da transicao
   							 char simbolo - simbolo que serve de gatilho para a transicao
   							 Estado destino - estado de destino da transicao
   * Retorno: void
   ****************************************************************/

	public void addTransicao(int partida, char simbolo, int destino) {
    // Caso ja exista um estado com este Id
		if (!estadoJaExiste(partida)) {
			throw new RuntimeException(); // Lanca excecao
		}

		// Acessa a tabela de transicoes do estado de partida
		Hashtable<Character, Integer> transicoes = funcaoDeTransicao.get(partida);

		// Adiciona uma nova transicao na tabela, onde o simbolo serve como chave 
		// para acessar o estado de destino
		transicoes.putIfAbsent(simbolo, destino);
	}

	public Estado funcaoDeTransicao(char simbolo) {
		if (estadoAtual == null) estadoAtual = estadoInicial;
		estadoAtual = buscarEstado(funcaoDeTransicao.get(estadoAtual.getId()).get(simbolo));

		return estadoAtual;
	}

  /*
   * ***************************************************************
   * Metodo: funcaoDeTransicaoEstendida
   * Funcao: computa uma cadeia de simbolos atraves das funcoes de transicao, 
   					 no intuito de verificar se ela eh reconhecida pelo automato
   * Parametros: String simbolos - cadeia de simbolos a ser computada
                                   pelo automato
   * Retorno: Estado
   ****************************************************************/

	public Estado funcaoDeTransicaoEstendida(String simbolos) {
		// Realiza a computacao atraves de uma funcao auxiliar recursiva
    return funcaoDeTransicaoEstendidaAux(simbolos, estadoInicial);
	}

	/*
   * ***************************************************************
   * Metodo: funcaoDeTransicaoEstendidaAux
   * Funcao: funcao recursiva que computa uma cadeia de simbolos 
   					 atraves das funcoes de transicao, no intuito de verificar
						 se ela eh reconhecida pelo automato
   * Parametros: String simbolos - cadeia de simbolos a serem computados
                                   pelo automato
                 Estado estadoAtual - estado onde a transicao se encontra
                                      durante a chamada da funcao
   * Retorno: Estado
   ****************************************************************/

	private Estado funcaoDeTransicaoEstendidaAux(String simbolos, Estado estadoAtual) {
		// Busca o primeiro caractere da cadeia
    char simboloAtual = simbolos.charAt(0);

    // Usa a substring pra cortar o primeiro caractere
    simbolos = simbolos.substring(1);

    // Acessa a tabela de transicoes para buscar o identificador do proximo estado acessado pelo simbolo
    // a partir do estado atual
    int idProximoEstado = funcaoDeTransicao.get(estadoAtual.getId()).get(simboloAtual);

    // Busca o proximo estado a partir do identificador
    Estado proximoEstado = buscarEstado(idProximoEstado);

    // Caso a cadeia estiver vazia
    if (simbolos.isEmpty()) {
    	// Retorna o proximo estado
      return proximoEstado;
    }

    // Caso ainda restar simbolos para serem computados, retorna uma chamada recursiva
    // passando a cadeia com os simbolos restantes e o proximo estado (que se torna o estado atual)
    return funcaoDeTransicaoEstendidaAux(simbolos, proximoEstado);
	}

	/*
   * ***************************************************************
   * Metodo: buscarEstado
   * Funcao: busca um estado dentro da tabela de estados
   * Parametros: int id - identificador do estado a ser buscado
   * Retorno: Estado
   ****************************************************************/

	private Estado buscarEstado(int id) {
		return estados.get(id);
	}

	public void definirEstadoInicial(int id) {
		Estado e = buscarEstado(id);

		if (e != null) {
			e.setEhInicial(true);
			estadoInicial = e;
		}
	}

	public void definirEstadoFinal(int id) {
		Estado e = buscarEstado(id);

		if (e != null) {
			e.setEhFinal(true);
			estadosFinais.putIfAbsent(id, e);
		}
	}

	public Estado getEstadoInicial() {
		return estadoInicial;
	}
}