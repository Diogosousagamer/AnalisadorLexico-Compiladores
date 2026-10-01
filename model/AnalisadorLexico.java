/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 25/08/2026
* Ultima alteracao...: 30/09/2026
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

	// Arranjo de palavras reservadas
	private final String[] palavrasReservadas = {"absolute", "array", "begin", "case", "char", "const", "div", 
		                                  "do", "downto", "else", "end", "external", "file", "for", "forward", 
		                                  "func", "function", "goto", "if", "implementation", "integer", "interface", 
		                                  "interrupt", "label", "main", "nil", "of", "packed", "proc", "program", "real", 
		                                  "record", "repeat", "set", "shl", "shr", "string", "then", "to", "type", "unit",
		                                  "until", "uses", "var", "while", "with"};

    // Arranjo de operadores logicos
	private final String[] operadoresLogicos = {"and", "or", "not", "xor"};

	// Arranjo de estados finais
	private final int[] estadosFinais = {1, 2, 4, 7, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};

	/*
     * ***************************************************************
     * Metodo: AnalisadorLexico
     * Funcao: cria uma nova instancia da classe AnalisadorLexico
     * Parametros: TelaPrincipalController controller - instancia do controller
                   da TelaPrincipal
     * Retorno: nenhum
     ****************************************************************/

	public AnalisadorLexico(TelaPrincipalController controller) {
		// Inicializa o controller 
		this.controller = controller;

		// Inicializa o automato
		this.inicializarAutomato();
	}

	/*
     * ***************************************************************
     * Metodo: inicializarAutomato
     * Funcao: inicializa os parametros do automato responsavel pela analise lexica
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

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

	/*
     * ***************************************************************
     * Metodo: criarEstados
     * Funcao: cria os estados do automato responsavel pela analise lexica
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void criarEstados(int quantidade) {
		// Cria os estados correspondentes a quantidade informada
		for (int i = 0; i < quantidade; i++) {
			automato.addEstado(new Estado(i));
		}
	}

	/*
     * ***************************************************************
     * Metodo: inicializarEstados
     * Funcao: inicializa os estados iniciais e finais 
               do automato responsavel pela analise lexica
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void inicializarEstados() {
		// Define 0 como o estado inicial
		automato.definirEstadoInicial(0);
		
		// Define cada estado parte do vetor estadosFinais como final
		for (int estado : estadosFinais) {
			automato.definirEstadoFinal(estado);
		}
	}

	/*
     * ***************************************************************
     * Metodo: criarAlfabeto
     * Funcao: cria o alfabeto do automato responsavel pela analise lexica
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void criarAlfabeto() {
		// Conjunto de caracteres que fazem parte da linguagem
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
							"abcdefghijklmnopqrstuvwxyz" +
							"0123456789" +
							"+-*/=><%" +
							"();:.,-'\"";

		// Insere cada caractere no alfabeto do automato
		for (char c : caracteres.toCharArray()) {
			automato.addSimbolo(c);
		}
	}

	/*
     * ***************************************************************
     * Metodo: criarTransicoes
     * Funcao: cria as transicoes do automato responsavel pela analise lexica
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void criarTransicoes() {
		// Conjunto de simbolos
		String caracteresVerbais = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
		String numeros = "0123456789";
		String simbolosEspeciais = "(;,)";
		String operadoresAritmeticos = "+-*%";
		String operadoresRelacionais = "><=";

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

		// Reconhecimento pro caractere vazio como parte de uma cadeia
		automato.addTransicao(8, ' ', 10);
		automato.addTransicao(12, ' ', 14);
		automato.addTransicao(14, ' ', 14);

		// Reconhecimento pra caracteres adicionais: dois pontos, traco e sinal de igualdade
		automato.addTransicao(12, ':', 14);
		automato.addTransicao(14, ':', 14);

		automato.addTransicao(12, '=', 14);
		automato.addTransicao(14, '=', 14);

		automato.addTransicao(12, '-', 14);
		automato.addTransicao(14, '-', 14);

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

		// Transicoes necessarias para o tratamento de comentarios
		automato.addTransicao(28, '\'', 28);
		automato.addTransicao(29, '\'', 28);
		automato.addTransicao(30, '\'', 28);

		automato.addTransicao(28, '"', 28);
		automato.addTransicao(29, '"', 28);
		automato.addTransicao(30, '"', 28);

		automato.addTransicao(0, '/', 27);
		automato.addTransicao(27, '*', 28);
		automato.addTransicao(28, '/', 29);
		automato.addTransicao(29, '*', 30);
		automato.addTransicao(28, '*', 30);
		automato.addTransicao(30, '/', 31);
		automato.addTransicao(28, ' ', 28);
		automato.addTransicao(28, ' ', 29);
		automato.addTransicao(29, ' ', 28);
		automato.addTransicao(30, ' ', 28);
	}

	/*
     * ***************************************************************
     * Metodo: inicializarTokens
     * Funcao: inicializa os tokens reconhecidos pelo automato responsavel pela analise lexica
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void inicializarTokens() {
		// Inicializa uma tabela hash contendo
		// * Um estado final servindo de chave
		// * Um token reconhecido pelo estado como valor
		listaTokens = new Hashtable<>();

		// Cada token reconhecido eh inserido com os estados responsaveis por tal
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

	/*
     * ***************************************************************
     * Metodo: analisarCodigo
     * Funcao: realiza a analise lexica do codigo passado como parametro
     * Parametros: File f - arquivo a ser analisado
     * Retorno: ArrayList<TuplaLexema>
     ****************************************************************/

	public ArrayList<TuplaLexema> analisarCodigo(File f) {
		// Variavel encarregada de armazenar cada linha a ser computada
		String linha = "";

		// Lista de tuplas contendo os lexemas e tokens do programa a ser analisado
		ArrayList<TuplaLexema> tuplas = new ArrayList<>();

		// Classe responsavel pela leitura do arquivo
		LerArquivo leituraArquivo = new LerArquivo(f);

		// Tamanho do arquivo
		int tamanhoArquivo = leituraArquivo.tamanhoArquivo();

		// String usada para a construcao do lexema
		String lexema = "";
		Estado estadoAnterior = automato.getEstadoInicial().copy();

		// Faz uma varredura completa do arquivo
		for (int i = 0; i < tamanhoArquivo; i++) {
			// Obtem a proxima linha
			linha = leituraArquivo.lerArquivo(i);

			// Se a linha obtida nao for nula
			if (linha != null) {
				// Adiciona um caractere vazio na linha pra delimitar a analise
				linha += " ";

				// Converte a cadeia da linha em um vetor de caracteres a serem computados
				char[] simbolos = linha.toCharArray();

				// Contador pra percorrer o vetor de caracteres
				int contador = 0;

				// Enquanto o contador nao atingir o limite do vetor de caracteres
				while (contador < simbolos.length) {
					// Obtem um novo simbolo contido na posicao apontada pelo contador
					char simbolo = simbolos[contador];


					// Incrementa o contador se o caractere for um espaco em branco e o estado
					// acessado anteriormente for inicial
					if (Character.isWhitespace(simbolo) && estadoAnterior.ehInicial()) {
						contador++;
						continue;
					}

					// Executa a funcao de transicao para obter um novo estado atraves do simbolo
					// computado
					Estado estado = automato.funcaoDeTransicao(simbolo);

					// Booleanas para verificar se o lexema foi reconhecido, possui um erro lexico ou ainda
					// precisa ser preenchido
					boolean reconhecido = estado == null && estadoAnterior.ehFinal() && !lexema.isEmpty();
					boolean temErro = estado == null && !estadoAnterior.ehFinal() && !lexema.isEmpty();
					boolean computarLexema = estado != null;

					// Se o lexema foi reconhecido
					if (reconhecido) {
						// Identifica o token, cria uma tupla contendo o lexema e o token
						// e a adiciona na lista de tuplas
						Token token = identificarToken(estadoAnterior.getId(), lexema);
						TuplaLexema par = new TuplaLexema(token, lexema);
						tuplas.add(par);

						// Reseta o lexema e o estado anterior
						lexema = "";
						estadoAnterior = automato.getEstadoInicial().copy();
					}
					else if (temErro) { // Porem se o lexema possuir algum erro
						// Reseta o lexema e o estado anterior e incrementa o contador
						lexema = "";
						estadoAnterior = automato.getEstadoInicial().copy();
						contador++;
					}
					else if (computarLexema) { // Porem se o lexema tiver de ser computado
						// Concatena o simbolo ao lexema, o estado atual passa a ser o estado anterior
						// e incrementa o contador
						lexema += simbolo;
						estadoAnterior = estado;
						contador++;
					}					
			     }
			}
		}

		// Retorna a lista de tuplas (lexema-token)
		return tuplas;
	}

	/*
     * ***************************************************************
     * Metodo: identificarToken
     * Funcao: identifica o token com base no lexema e estado atuais
     * Parametros: int estado - id do estado atual
     			   String lexema - lexema construido
     * Retorno: Token
     ****************************************************************/

	private Token identificarToken(int estado, String lexema) {
		// Obtem o token reconhecido a partir do estado fornecido
		Token token = listaTokens.get(estado);

		// Se o token for um identificador, precisaremos verificars
		if (token == Token.IDENTIFICADOR) {
			// Se o lexema eh uma palavra reservada
			if (verificarPalavraReservada(lexema)) {
				token = Token.PALAVRA_RESERVADA;
			}
			else if (verificarOperadorLogico(lexema)) { // Ou se eh um operador logico (and, or, not)
				token = Token.OPERADOR_LOGICO;
			}
		}

		// O token obtido eh retornado
		return token;
	}

	/*
     * ***************************************************************
     * Metodo: verificarPalavraReservada
     * Funcao: verifica se o lexema eh uma palavra reservada
     * Parametros: String lexema - lexema a ser verificado
     * Retorno: boolean
     ****************************************************************/

	private boolean verificarPalavraReservada(String lexema) {
		// Percorre o vetor de palavras reservadas
		for (String p : palavrasReservadas) {
			// Retorna true se algum lexema for correspondente
			// a uma das palavras reservadas existentes na linguagem
			if (lexema.equals(p)) {
				return true;
			}
		}

		// Retorna false caso nenhuma correspondencia
		// tiver sido encontrada
		return false;
	} 

	/*
     * ***************************************************************
     * Metodo: verificarOperadorLogico
     * Funcao: verifica se o lexema eh um operador logico
     * Parametros: String lexema - lexema a ser verificado
     * Retorno: boolean
     ****************************************************************/

	private boolean verificarOperadorLogico(String lexema) {
		// Percorre o vetor de operadores logicos
		for (String op : operadoresLogicos) {
			// Retorna true se algum lexema for correspondente 
			// a um dos operadores logicos existentes na linguagem
			if (lexema.equals(op)) {
				return true;
			}
		}

		// Retorna false caso nenhuma correspondencia
		// tiver sido encontrada
		return false;
	}
}