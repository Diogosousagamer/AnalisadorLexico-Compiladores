/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 25/08/2026
* Ultima alteracao...: 25/08/2026
* Nome...............: TuplaLexema
* Funcao.............: Classe que designa as tuplas contendo o lexema
                       e o token correspondente.
                     
*************************************************************** */

package model;

public class TuplaLexema {
	private Token token;
	private String lexema;

	public TuplaLexema(Token token, String lexema) {
		this.token = token;
		this.lexema = lexema;
	}

	public Token getToken() {
		return token;
	}

	public String getLexema() {
		return lexema;
	}
}