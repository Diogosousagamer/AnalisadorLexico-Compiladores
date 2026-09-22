/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 15/09/2026
* Ultima alteracao...: 21/09/2026
* Nome...............: LerArquivo
* Funcao.............: Classe que realiza a leitura de um arquivo .txt.
                     
*************************************************************** */

package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LerArquivo {
    // Arquivo a ser lido
	private File arquivo;

    /*
     * ***************************************************************
     * Metodo: LerArquivo
     * Funcao: inicializa uma nova instancia da classe LerArquivo
     * Parametros: File arquivo - arquivo a ser lido
     * Retorno: nenhum
     ****************************************************************/

	public LerArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

    /*
     * ***************************************************************
     * Metodo: lerArquivo
     * Funcao: le e retorna uma nova linha do arquivo
     * Parametros: int linha - indice da linha a ser lida
     * Retorno: String
     ****************************************************************/

	public String lerArquivo(int linha) {
       // Variavel reservada para guardar o resultado
       String res = "";
       
       // Tenta-se executar o seguinte codigo
       try {
           // Contador de linhas
           int acc = 0;

           // Abre-se o scanner para varrer o arquivo
           Scanner sc = new Scanner(arquivo);

           // Enquanto o arquivo ainda tiver linhas
           while (sc.hasNextLine()) {
               // Se o acumulador apontar pra linha procurada
               if (acc == linha) {
                   // Armazena a linha atual e sai do laco
                   res = sc.nextLine();
                   break;
               }
               
               // Caso nenhuma correspondencia for encontrada, 
               // passa pra proxima linha
               sc.nextLine();

               // Incrementa o contador
               acc++;
            }

            // Fecha o scanner
            sc.close();
        } 
       catch (FileNotFoundException ex) {
            // Em caso de excecao, sinaliza que o arquivo nao foi encontrado
            System.out.println("arquivo nao encontrado");
       }

       // Retorna a linha obtida
       return res; 
    }

    
  /*
   * ***************************************************************
   * Metodo: tamanhoArquivo
   * Funcao: retorna o tamanho do arquivo (em termos de linhas)
   * Parametros: nenhum parametro foi definido para esta funcao
   * Retorno: Integer
   ****************************************************************/

    public Integer tamanhoArquivo() {
        // Contador do tamanho
        int tamanho = 0;
     
        // O metodo tenta executar este bloco de codigo
        try {
            // Abre o scanner para varrer o arquivo
            Scanner sc = new Scanner(arquivo);

            while (sc.hasNextLine()) {
                // Incrementa o tamanho enquanto houverem linhas a serem computadas
                tamanho++;
                sc.nextLine();
            }

            // Fecha o scanner depois que a varredura foi concluida
            sc.close();
        } 
        catch (FileNotFoundException ex) {
            // Em caso de excecao, sinaliza que o arquivo nao foi encontrado
            System.out.println("arquivo nao encontrado");
        }

        // Imprime o tamanho (quantidade de linhas) do arquivo
        System.out.println(tamanho);

        // Retorna o tamanho do arquivo
        return tamanho;
    }
}