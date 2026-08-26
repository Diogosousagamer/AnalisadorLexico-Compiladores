/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 25/08/2026
* Ultima alteracao...: 25/08/2026
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
		palavrasReservadas = new String[]{"absolute", "array", "begin", "case", "char", "const", "div", 
		                                  "do", "dowto", "else", "end", "external", "file", "for", "forward", 
		                                  "func", "function", "goto", "if", "implementation", "integer", "interface", 
		                                  "interrupt", "label", "main", "nil", "of", "packed", "proc", "program", "real", 
		                                  "record", "repeat", "set", "shl", "shr", "string", "then", "to", "type", "unit",
		                                  "until", "uses", "var", "while", "with", "xor"};

		automato = new Automato();


		// Estados e transicoes
	}

	public TuplaLexema analisarCodigo(String linha) {
		return null;
	}
}