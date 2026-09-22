/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 25/08/2026
* Ultima alteracao...: 21/09/2026
* Nome...............: AnalisadorLexico
* Funcao.............: Classe que designa as operacoes do analisador lexico de um compilador.
                     
*************************************************************** */

package model;

import controller.TelaPrincipalController;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class AnalisadorLexico {
	// Variaveis e instancias
	private Automato automato;
	private TelaPrincipalController controller;
	// private Hashtable<> tabelaSimbolos;
	private Hashtable<Integer, Token> listaTokens;

	private final String[] palavrasReservadas = {"absolute", "array", "begin", "case", "char", "const", "div", 
		                                  "do", "downto", "else", "end", "external", "file", "for", "forward", 
		                                  "func", "function", "goto", "if", "implementation", "integer", "interface", 
		                                  "interrupt", "label", "main", "nil", "of", "packed", "proc", "program", "real", 
		                                  "record", "repeat", "set", "shl", "shr", "string", "then", "to", "type", "unit",
		                                  "until", "uses", "var", "while", "with"};
	private final String[] operadoresLogicos = {"and", "or", "not", "xor"};
	private final int[] estadosFinais = {1, 2, 4, 7, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};

	public AnalisadorLexico(TelaPrincipalController controller) {
		this.controller = controller;
		this.inicializarAutomato();
	}

	private void inicializarAutomato() {
		// Inicializa o automato
		automato = new Automato();

		// Sao criados, ao todo, vinte e cinco estados para o automato do analisador lexico
		criarEstados(32);

		// Os estados iniciais e finais do automato sao inicializados
		inicializarEstados();

		// Alimenta o alfabeto com os simbolos necessarios para o reconhecimento da linguagem
		criarAlfabeto();

		// Cria as transicoes necessarias para o funcionamento do automato
		criarTransicoes();

		// Inicializa os tokens reconhecidos pelo automato do analisador lexico
		inicializarTokens();
	}

	private void criarEstados(int quantidade) {
		for (int i = 0; i < quantidade; i++) {
			automato.addEstado(new Estado(i));
		}
	}

	private void inicializarEstados() {
		automato.definirEstadoInicial(0);
		
		for (int estado : estadosFinais) {
			automato.definirEstadoFinal(estado);
		}
	}

	private void criarAlfabeto() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
							"abcdefghijklmnopqrstuvwxyz" +
							"0123456789" +
							"+-*/=><%" +
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
		String operadoresAritmeticos = "+-*%";
		String operadoresRelacionais = "><=";

		// Reconhecimento pro caractere vazio como parte de uma cadeia
		automato.addTransicao(8, ' ', 10);
		automato.addTransicao(12, ' ', 14);
		automato.addTransicao(14, ' ', 14);

		// Cria-se uma sequencia de transicoes para todos os caracteres verbais
		for (char c : caracteresVerbais.toCharArray()) {
			// Transicoes necessarias para identificador/palavras reservadas
			automato.addTransicao(0, c, 1);
			automato.addTransicao(1, c, 1);

			// Transicao necessaria para caractere
			automato.addTransicao(8, c, 10);

			// Transicao necessaria para string
			automato.addTransicao(12, c, 14);
			automato.addTransicao(14, c, 14);

			// Transicao necessaria para comentarios
			automato.addTransicao(28, c, 28);
			automato.addTransicao(29, c, 28);
			automato.addTransicao(30, c, 28);
		}

		// Cria-se uma sequencia de transicoes para todos os numeros
		for (char num : numeros.toCharArray()) {
			// Transicao necessaria para identificador
			automato.addTransicao(1, num, 1);

			// Transicoes necessarias para numeros inteiros
			automato.addTransicao(0, num, 2);
			automato.addTransicao(2, num, 2);

			// Transicao necessaria para numeros reais
			automato.addTransicao(3, num, 4);
			automato.addTransicao(4, num, 4);

			// Transicoes necessarias para notacao cientifica
			automato.addTransicao(5, num, 7);
			automato.addTransicao(6, num, 7);
			automato.addTransicao(7, num, 7);

			// Transicao necessaria para caractere
			automato.addTransicao(8, num, 10);

			// Transicoes necessarias para string
			automato.addTransicao(12, num, 14);
			automato.addTransicao(14, num, 14);

			// Transicoes necessarias para comentarios
			automato.addTransicao(28, num, 28);
			automato.addTransicao(29, num, 28);
			automato.addTransicao(30, num, 28);
		}

		// Transicao necessaria para delimitar uma casa decimal
		automato.addTransicao(2, '.', 3);

		// Transicoes necessarias para o reconhecimento de uma notacao cientifica
		automato.addTransicao(4, 'e', 5);
		automato.addTransicao(5, '-', 6);

		// Transicoes necessarias para demarcar o inicio de um caractere
		automato.addTransicao(0, '\'', 8);
		automato.addTransicao(8, '\'', 11);
		automato.addTransicao(8, '\\', 9);
		automato.addTransicao(9, '\'', 10);
		automato.addTransicao(9, '\\', 10);
		automato.addTransicao(10, '\'', 11);

		// Transicoes necessarias para demarcar o inicio de uma string
		automato.addTransicao(0, '"', 12);
		automato.addTransicao(12, '"', 15);
		automato.addTransicao(12, '\\', 13);
		automato.addTransicao(13, '"', 14);
		automato.addTransicao(13, '\\', 14);
		automato.addTransicao(14, '"', 15);

		// Cria-se uma sequencia de transicoes para todos os simbolos especiais
		for (char s : simbolosEspeciais.toCharArray()) {
			// Transicao necessaria para ser reconhecido como um simbolo especial
			// propriamente dito
			automato.addTransicao(0, s, 16);

			// Transicao necessaria para ser reconhecido como um caractere
			automato.addTransicao(8, s, 10);

			// Transicao necessaria para ser reconhecido como parte de uma string
			automato.addTransicao(12, s, 14);
			automato.addTransicao(14, s, 14);

			automato.addTransicao(28, s, 28);
			automato.addTransicao(29, s, 28);
			automato.addTransicao(30, s, 28);
		}

		// Cria-se uma sequencia de transicoes para todos os operadores aritmeticos
		for (char op : operadoresAritmeticos.toCharArray()) {
			// Transicao necessaria para ser reconhecido como um operador propriamente dito
			automato.addTransicao(0, op, 17);

			// Transicao necessaria para o reconhecimento como caractere
			automato.addTransicao(8, op, 10);

			// Transicoes necessarias para o reconhecimento como parte de uma string
			automato.addTransicao(12, op, 14);
			automato.addTransicao(14, op, 14);

			if (op != '*') {
				automato.addTransicao(28, op, 28);
				automato.addTransicao(29, op, 28);
				automato.addTransicao(30, op, 28);
			}
		}

		// Cria-se uma sequencia de transicoes para todos os operadores aritmeticos
		for (char r : operadoresRelacionais.toCharArray()) {
			// Transicao necessaria para o reconhecimento como caractere
			automato.addTransicao(8, r, 10);

			// Transicoes necessarias para o reconhecimento como parte de uma string
			automato.addTransicao(12, r, 14);
			automato.addTransicao(14, r, 14);

			automato.addTransicao(28, r, 28);
			automato.addTransicao(29, r, 28);
			automato.addTransicao(30, r, 28);
		}

		// Transicoes necesarias para o reconhecimento dos operadores relacionais
		automato.addTransicao(0, '>', 18);
		automato.addTransicao(0, '<', 20);
		automato.addTransicao(0, '=', 23);

		// Transicao necessaria para marcar o fim de um programa
		automato.addTransicao(0, '.', 26);

		// Transicoes necessarias para o reconhecimento dos operadores compostos (maior igual, menor igual e diferenca)
		automato.addTransicao(18, '=', 19);
		automato.addTransicao(20, '=', 21);
		automato.addTransicao(20, '>', 22);

		// Transicoes necessarias para o reconhecimento do operador de atribuicao (=)
		automato.addTransicao(0, ':', 24);
		automato.addTransicao(24, '=', 25);

		automato.addTransicao(28, '\'', 28);
		automato.addTransicao(29, '\'', 28);
		automato.addTransicao(30, '\'', 28);

		automato.addTransicao(28, '"', 28);
		automato.addTransicao(29, '"', 28);
		automato.addTransicao(30, '"', 28);

		automato.addTransicao(28, ' ', 28);
		automato.addTransicao(29, ' ', 28);
		automato.addTransicao(30, ' ', 28);

		// Transicoes necessarias para o tratamento de comentarios
		automato.addTransicao(0, '/', 27);
		automato.addTransicao(27, '*', 28);
		automato.addTransicao(28, '/', 29);
		automato.addTransicao(29, '*', 30);
		automato.addTransicao(28, '*', 30);
		automato.addTransicao(30, '/', 31);
	}

	private void inicializarTokens() {
		listaTokens = new Hashtable<>();

		listaTokens.putIfAbsent(1, Token.IDENTIFICADOR);
		listaTokens.putIfAbsent(2, Token.CONSTANTE_NUMERICA);
		listaTokens.putIfAbsent(4, Token.CONSTANTE_NUMERICA);
		listaTokens.putIfAbsent(7, Token.CONSTANTE_NUMERICA);
		listaTokens.putIfAbsent(11, Token.CARACTERE);
		listaTokens.putIfAbsent(15, Token.STRING);
		listaTokens.putIfAbsent(16, Token.SIMBOLO_ESPECIAL);
		listaTokens.putIfAbsent(17, Token.OPERADOR_ARITMETICO);
		listaTokens.putIfAbsent(18, Token.OPERADOR_RELACIONAL);
		listaTokens.putIfAbsent(19, Token.OPERADOR_RELACIONAL);
		listaTokens.putIfAbsent(20, Token.OPERADOR_RELACIONAL);
		listaTokens.putIfAbsent(21, Token.OPERADOR_RELACIONAL);
		listaTokens.putIfAbsent(22, Token.OPERADOR_RELACIONAL);
		listaTokens.putIfAbsent(23, Token.OPERADOR_RELACIONAL);
		listaTokens.putIfAbsent(24, Token.SIMBOLO_ESPECIAL);
		listaTokens.putIfAbsent(25, Token.OPERADOR_ATRIBUICAO);
		listaTokens.putIfAbsent(26, Token.SIMBOLO_ESPECIAL);
		listaTokens.putIfAbsent(27, Token.OPERADOR_ARITMETICO);
	}

	public ArrayList<TuplaLexema> analisarCodigo(File f) {
		String linha = "";
		ArrayList<TuplaLexema> tuplas = new ArrayList<>();
		LerArquivo leituraArquivo = new LerArquivo(f);
		int tamanhoArquivo = leituraArquivo.tamanhoArquivo();

		String lexema = "";
		Estado estadoAnterior = automato.getEstadoInicial().copy();

		for (int i = 0; i < tamanhoArquivo; i++) {
			linha = leituraArquivo.lerArquivo(i);

			if (linha != null) {
				linha += "~";
				char[] simbolos = linha.toCharArray();
				int contador = 0;

				while (contador < simbolos.length) {
					char simbolo = simbolos[contador];

					if (Character.isWhitespace(simbolo) && estadoAnterior.ehInicial()) {
						contador++;
						continue;
					}

					Estado estado = automato.funcaoDeTransicao(simbolo);

					boolean reconhecido = estado == null && estadoAnterior.ehFinal() && !lexema.isEmpty();
					boolean temErro = estado == null && !estadoAnterior.ehFinal() && !lexema.isEmpty();
					boolean computarLexema = estado != null;

					if (reconhecido) {
						Token token = identificarToken(estadoAnterior.getId(), lexema);
						TuplaLexema par = new TuplaLexema(token, lexema);
						tuplas.add(par);

						lexema = "";
						estadoAnterior = automato.getEstadoInicial().copy();
					}
					else if (temErro) {
						lexema = "";
						estadoAnterior = automato.getEstadoInicial().copy();
						contador++;
					}
					else if (computarLexema) {
						lexema += simbolo;
						estadoAnterior = estado;
						contador++;
					}
					else {
						contador++;
					}
				}
			}
		}

		return tuplas;
	}

	private Token identificarToken(int estado, String lexema) {
		Token token = listaTokens.get(estado);

		if (token == Token.IDENTIFICADOR) {
			if (verificarPalavraReservada(lexema)) {
				token = Token.PALAVRA_RESERVADA;
			}
			else if (verificarOperadorLogico(lexema)) {
				token = Token.OPERADOR_LOGICO;
			}
		}

		return token;
	}

	private boolean verificarPalavraReservada(String lexema) {
		for (String p : palavrasReservadas) {
			if (lexema.equals(p)) {
				return true;
			}
		}

		return false;
	} 

	private boolean verificarOperadorLogico(String lexema) {
		for (String op : operadoresLogicos) {
			if (lexema.equals(op)) {
				return true;
			}
		}

		return false;
	}
}