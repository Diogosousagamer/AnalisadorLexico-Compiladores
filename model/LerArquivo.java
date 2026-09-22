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

	public LerArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	public String lerArquivo(int linha) {
       String res = "";
       
       try {
           int acc = 0;
           Scanner sc = new Scanner(arquivo);

           while (sc.hasNextLine()) {
               if (acc == linha) {
                   res = sc.nextLine();
                   break;
               }
               
               sc.nextLine();
               acc++;
            }

           sc.close();
        } 
       catch (FileNotFoundException ex) {
            System.out.println("arquivo nao encontrado");
       }

       return res; 
    }

  public Integer tamanhoArquivo() {
     int tamanho = 0;
     
     try {
        Scanner sc = new Scanner(arquivo);
        
        while (sc.hasNextLine()) {
            tamanho++;
            sc.nextLine();
        }
        
        sc.close();
     } 
     catch (FileNotFoundException ex) {
        System.out.println("arquivo nao encontrado");
     }

     System.out.println(tamanho);
     return tamanho;
  }
}