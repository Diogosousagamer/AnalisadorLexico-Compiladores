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
	// private Hashtable<> tabelaSimbolos;

	public AnalisadorLexico() {
		this.inicializarAutomato();
	}

	private void inicializarAutomato() {
		automato = new Automato();

		// Estados e transicoes
	}

	public ArrayList<> analisarCodigo(String linha) {
		ArrayList<TuplaLexema> conjuntoTuplas = new ArrayList<>();
	}
}