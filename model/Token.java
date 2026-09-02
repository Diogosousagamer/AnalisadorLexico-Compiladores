/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 25/08/2026
* Ultima alteracao...: 02/09/2026
* Nome...............: Token
* Funcao.............: Classe enumerada que representa os tokens
                       que compoem uma determinada linguagem.
                     
*************************************************************** */

package model;

public enum Token {
	IDENTIFICADOR, OPERADOR_ARITMETICO, OPERADOR_RELACIONAL, OPERADOR_LOGICO, OPERADOR_ATRIBUICAO, PALAVRA_RESERVADA, 
    CARACTERE, CONSTANTE_NUMERICA, STRING, SIMBOLO_ESPECIAL
}