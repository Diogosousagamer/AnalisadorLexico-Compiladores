/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 02/10/2026
* Ultima alteracao...: 02/10/2026
* Nome...............: EntradaTabelaSimbolos
* Funcao.............: Classe que representa uma entrada na tabela de simbolos.
                     
*************************************************************** */

package model;

public class EntradaTabelaSimbolos {
	// Variaveis e instancias
	private String lexema;
	private Token token;
	private int linha;

	/*
     * ***************************************************************
     * Metodo: EntradaTabelaSimbolos
     * Funcao: cria uma nova instancia da classe EntradaTabelaSimbolos
     * Parametros: String lexema - lexema computado
     			   Token token - token representado pelo lexema
     			   int linha - linha onde o lexema foi identificado
     * Retorno: nenhum
     ****************************************************************/

	public EntradaTabelaSimbolos(String lexema, Token token, int linha) {
		this.lexema = lexema;
		this.token = token;
		this.linha = linha;
	}

	/*
     * ***************************************************************
     * Metodo: getLexema
     * Funcao: retorna o lexema armazenado na entrada
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: String
     ****************************************************************/

	public String getLexema() {
		return lexema;
	}

	/*
     * ***************************************************************
     * Metodo: getToken
     * Funcao: retorna o token do lexema armazenado na entrada
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: Token
     ****************************************************************/

	public Token getToken() {
		return token;
	}

	/*
     * ***************************************************************
     * Metodo: getLinha
     * Funcao: retorna a linha onde o lexema foi identificado
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: int
     ****************************************************************/

	public int getLinha() {
		return linha;
	}
}