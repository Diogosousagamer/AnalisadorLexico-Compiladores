/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 25/08/2026
* Ultima alteracao...: 28/08/2026
* Nome...............: AnalisadorLexico
* Funcao.............: Classe que designa as operacoes do analisador lexico de um compilador.
                     
*************************************************************** */

package model;

import java.util.ArrayList;
import java.util.Hashtable;

public class AnalisadorLexico {
	private Automato automato;
	private String[] palavrasReservadas;

	// private Hashtable<> tabelaSimbolos;

	public AnalisadorLexico() {
		this.inicializarAutomato();
	}

	private void inicializarAutomato() {
		// Inicializa o vetor de palavras reservadas
		palavrasReservadas = new String[]{"absolute", "array", "begin", "case", "char", "const", "div", 
		                                  "do", "downto", "else", "end", "external", "file", "for", "forward", 
		                                  "func", "function", "goto", "if", "implementation", "integer", "interface", 
		                                  "interrupt", "label", "main", "nil", "of", "packed", "proc", "program", "real", 
		                                  "record", "repeat", "set", "shl", "shr", "string", "then", "to", "type", "unit",
		                                  "until", "uses", "var", "while", "with", "xor"};

		// Inicializa o automato
		automato = new Automato();


		// Sao criados, ao todo, vinte e cinco estados para o automato do analisador lexico
		criarEstados(25);

		// Os estados iniciais e finais do automato sao inicializados
		inicializarEstados();

		// Alimenta o alfabeto com os simbolos necessarios para o reconhecimento da linguagem
		criarAlfabeto();

		// Cria as transicoes necessarias para o funcionamento do automato
		criarTransicoes();
	}

	private void criarEstados(int quantidade) {
		for (int i = 0; i < quantidade; i++) {
			automato.addEstado(new Estado(i));
		}
	}

	private void inicializarEstados() {
		automato.definirEstadoInicial(0);

		int[] estadosFinais = {1, 2, 4, 7, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};

		for (int i = 0; i < estadosFinais.length; i++) {
			automato.definirEstadoFinal(estadosFinais[i]);
		}
	}

	private void criarAlfabeto() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
							"abcdefghijklmnopqrstuvwxyz" +
							"0123456789" +
							"+-*/=><" +
							"();:.,'\"";

		for (char c : caracteres.toCharArray()) {
			automato.addSimbolo(c);
		}
	}

	private void criarTransicoes() {
		// Conjunto de simbolos
		String caracteresVerbais = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
		String numeros = "0123456789";
		String simbolosEspeciais = "(;,)";
		String operadoresAritmeticos = "+-*/";
		String operadoresRelacionais = "><=";

		// Cria-se uma sequencia de transicoes para todos os caracteres verbais
		for (char c : caracteresVerbais.toCharArray()) {
			// Transicoes necessarias para identificador/palavras reservadas
			automato.addTransicao(0, c, 1);
			automato.addTransicao(1, c, 1);

			// Transicao necessaria para caractere
			automato.addTransicao(8, c, 9);

			// Transicao necessaria para string
			automato.addTransicao(11, c, 12);
			automato.addTransicao(12, c, 12);
		}

		// Cria-se uma sequencia de transicoes para todos os numeros
		for (char num : numeros.toCharArray()) {
			// Transicao necessaria para identificador
			automato.addTransicao(1, num, 1);

			// Transicoes necessarias para numeros inteiros
			automato.addTransicao(0, num, 2);
			automato.addTransicao(2, num, 2);

			// Transicao necessaria para numeros reais
			automato.addTransicao(4, num, 4);

			// Transicoes necessarias para notacao cientifica
			automato.addTransicao(5, num, 7);
			automato.addTransicao(6, num, 7);
			automato.addTransicao(7, num, 7);

			// Transicao necessaria para caractere
			automato.addTransicao(8, num, 9);

			// Transicoes necessarias para string
			automato.addTransicao(11, num, 12);
			automato.addTransicao(12, num, 12);
		}

		// Transicoes necessarias para o reconhecimento de um 
		automato.addTransicao(2, '.', 3);

		// Transicoes necessarias para o reconhecimento de uma notacao cientifica
		automato.addTransicao(4, 'e', 5);
		automato.addTransicao(5, '-', 6);

		// Transicoes necessarias para demarcar o inicio de um caractere
		automato.addTransicao(0, '\'', 8);
		automato.addTransicao(9, '\'', 10);

		// Transicoes necessarias para demarcar o inicio de uma string
		automato.addTransicao(0, '"', 11);
		automato.addTransicao(12, '"', 13);

		// Cria-se uma sequencia de transicoes para todos os simbolos especiais
		for (char s : simbolosEspeciais.toCharArray()) {
			// Transicao necessaria para ser reconhecido como um simbolo especial
			// propriamente dito
			automato.addTransicao(0, s, 14);

			// Transicao necessaria para ser reconhecido como um caractere
			automato.addTransicao(8, s, 9);

			// Transicao necessaria para ser reconhecido como parte de uma string
			automato.addTransicao(11, s, 12);
			automato.addTransicao(12, s, 12);
		}

		// Cria-se uma sequencia de transicoes para todos os operadores aritmeticos
		for (char op : operadoresAritmeticos.toCharArray()) {
			// Transicao necessaria para ser reconhecido como um operador propriamente dito
			automato.addTransicao(0, op, 15);

			// Transicao necessaria para o reconhecimento como caractere
			automato.addTransicao(8, op, 9);

			// Transicoes necessarias para o reconhecimento como parte de uma string
			automato.addTransicao(11, op, 12);
			automato.addTransicao(12, op, 12);
		}

		// Cria-se uma sequencia de transicoes para todos os operadores aritmeticos
		for (char r : operadoresRelacionais.toCharArray()) {
			// Transicao necessaria para o reconhecimento como caractere
			automato.addTransicao(8, r, 9);

			// Transicoes necessarias para o reconhecimento como parte de uma string
			automato.addTransicao(11, r, 12);
			automato.addTransicao(12, r, 12);
		}

		// Transicoes necesarias para o reconhecimento dos operadores relacionais
		automato.addTransicao(0, '>', 16);
		automato.addTransicao(0, '<', 18);
		automato.addTransicao(0, '=', 21);

		// Transicao necessaria para marcar o fim de um programa
		automato.addTransicao(0, '.', 24);

		// Transicoes necessarias para o reconhecimento dos operadores compostos (maior igual, menor igual e diferenca)
		automato.addTransicao(16, '=', 17);
		automato.addTransicao(18, '=', 19);
		automato.addTransicao(18, '>', 20);

		// Transicoes necessarias para o reconhecimento do operador de atribuicao (=)
		automato.addTransicao(0, ':', 22);
		automato.addTransicao(22, '=', 23);

		// Transicoes necessarias para o reconhecimento dos ultimos (; , \) caracteres especiais como char/string
		automato.addTransicao(8, ':', 9);
		automato.addTransicao(11, ':', 12);
		automato.addTransicao(12, ':', 12);

		automato.addTransicao(8, '.', 9);
		automato.addTransicao(11, '.', 12);
		automato.addTransicao(12, '.', 12);

		automato.addTransicao(8, '\\', 9);
		automato.addTransicao(11, '\\', 12);
		automato.addTransicao(12, '\\', 12);

		automato.addTransicao(8, '\\', 9);
		automato.addTransicao(11, '\\', 12);
		automato.addTransicao(12, '\\', 12);
	}

	public TuplaLexema analisarCodigo(String linha) {
		return null;
	}
}