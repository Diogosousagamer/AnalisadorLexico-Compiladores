package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class Automato {
	private ArrayList<Character> alfabeto;
	private Hashtable<Integer, Estado> estados; 	//<Id,Estado>
	private Hashtable<Integer, Estado> estadosFinais;
	private Hashtable<Integer, Hashtable<Character, Integer>> funcaoDeTransicao; //<Id, lista de transicoes>
	private Estado estadoInicial;

	public Automato(ArrayList<Character> alfabeto) {
		this.alfabeto = alfabeto;
		estados = new Hashtable<>();
		estadosFinais = new Hashtable<>();
		funcaoDeTransicao = new Hashtable<>();
	}
	
	public Automato() {
		alfabeto = new ArrayList<Character>();
		estados = new Hashtable<>();
		estadosFinais = new Hashtable<>();
		funcaoDeTransicao = new Hashtable<>();
	}

	public void addSimbolo(char simbolo) {
		alfabeto.add(simbolo);
	}

	public void addEstado(Estado estado) {
    // Caso ja exista um estado com este Id
		if (estadoJaExiste(estado.getId())) {
			throw new RuntimeException(); // Lanca excecao
		}

		if (estado.ehFinal()) {
			estadosFinais.putIfAbsent(estado.getId(), estado);
		}
		else if (estado.ehInicial()) {
      estadoInicial = estado;
		}

		estados.putIfAbsent(estado.getId(), estado);
    funcaoDeTransicao.putIfAbsent(estado.getId(), new Hashtable<>());
	}

	private boolean estadoJaExiste(int id) {
		for (Estado e : estados.values()) {
			if (e.getId() == id) {
				return true;
			}
		}

		return false;
	}

	public void addTransicao(Estado partida, char simbolo, Estado destino) {
    // Caso ja exista um estado com este Id
		if (!estadoJaExiste(partida.getId())) {
			throw new RuntimeException(); // Lanca excecao
		}

		Hashtable<Character, Integer> transicoes = funcaoDeTransicao.get(partida.getId());
		transicoes.putIfAbsent(simbolo, destino.getId());
	}

	public Estado funcaoDeTransicaoEstendida(String simbolos) {
    return funcaoDeTransicaoEstendidaAux(simbolos, estadoInicial);
	}

	private Estado funcaoDeTransicaoEstendidaAux(String simbolos, Estado estadoAtual) {
    char simboloAtual = simbolos.charAt(0);
    simbolos = simbolos.substring(1);

    int idProximoEstado = funcaoDeTransicao.get(estadoAtual.getId()).get(simboloAtual);
    Estado proximoEstado = buscarEstado(idProximoEstado);

    if (simbolos.isEmpty()) {
      return proximoEstado;
    }

     return funcaoDeTransicaoEstendidaAux(simbolos, proximoEstado);
	}

	private Estado buscarEstado(int id) {
		for (Estado e : estados.values()) {
			if (e.getId() == id) {
				return e;
			}
		}

		return null;
	}
}
