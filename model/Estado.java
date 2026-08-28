/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 19/08/2026
* Ultima alteracao...: 28/08/2026
* Nome...............: Estado
* Funcao.............: Classe que designa as operacoes de um estado em um automato.
                     
*************************************************************** */

package model;

public class Estado {
	// Variaveis e instancias
	private int id;
	private boolean ehFinal;
	private boolean ehInicial;

    public Estado(int id) {
        this.id = id;
        this.ehInicial = false;
        this.ehFinal = false;
    }

	/*
     * ***************************************************************
     * Metodo: Estado
     * Funcao: inicializa uma nova instancia da classe Estado
     * Parametros: int id - identificador do estado
                   boolean ehInicial - sinaliza que o estado eh inicial
                   boolean ehFinal - sinaliza que o estado eh final
     * Retorno: nenhum
     ****************************************************************/

	public Estado(int id, boolean ehInicial, boolean ehFinal) {
		this.id = id;
		this.ehInicial = ehInicial;
		this.ehFinal = ehFinal;
	}

	/*
     * ***************************************************************
     * Metodo: getId
     * Funcao: retorna o identificador do estado
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: int
     ****************************************************************/
	
	public int getId() {
    	return id;
	}

    public void setEhInicial(boolean ehInicial) {
        this.ehInicial = ehInicial;
    }

	/*
     * ***************************************************************
     * Metodo: ehInicial
     * Funcao: retorna se o estado eh inicial
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: boolean
     ****************************************************************/

	public boolean ehInicial() {
    	return ehInicial;
	}

    public void setEhFinal(boolean ehFinal) {
        this.ehFinal = ehFinal;
    }

	/*
     * ***************************************************************
     * Metodo: ehFinal
     * Funcao: retorna se o estado eh final
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: boolean
     ****************************************************************/

	public boolean ehFinal() {
    	return ehFinal;
	}
}
