/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 02/09/2026
* Ultima alteracao...: 02/10/2026
* Nome...............: TelaPrincipalController
* Funcao.............: Classe que gerencia as operacoes da TelaPrincipal.
                     
*************************************************************** */

package controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.AnalisadorLexico;
import model.Token;
import model.TuplaLexema;

public class TelaPrincipalController {
	// Componentes da interface
	@FXML private Button btnAbrirArquivo;
	@FXML private Button btnExcluirArquivo;
	@FXML private Button btnVoltar;
	@FXML private TextArea txtCodigo;
	@FXML private TextArea txtErros;
	@FXML private TextArea txtResultados;

	// Variaveis e instancias
	private AnalisadorLexico analisadorLexico;
	private File codigo;
	private File arquivoSaida;

	/*
     * ***************************************************************
     * Metodo: voltar
     * Funcao: volta para a tela inicial (TelaMenu)
     * Parametros: ActionEvent event - evento gerado ao clicar no botao
     * Retorno: void
     ****************************************************************/

	@FXML
	private void voltar(ActionEvent event) throws IOException {
		// Carrega o arquivo FXML da TelaPrincipal
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaMenu.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		// Obtem a janela e troca a cena (tela)
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);

		// Mantem a tela maximizada
		stage.setMaximized(false);
		stage.setMaximized(true);
	}

	/*
     * ***************************************************************
     * Metodo: abrirArquivo
     * Funcao: abre o arquivo contendo os lexemas e tokens do programa
     * Parametros: ActionEvent event - evento gerado ao clicar no botao
     * Retorno: void
     ****************************************************************/

	@FXML
	private void abrirArquivo(ActionEvent event) {
		// Se o arquivo de saida nao for nulo e existir
		if (arquivoSaida != null && arquivoSaida.exists()) {
			// O metodo tenta executar o seguinte bloco de codigo
			try {
				// Abre o arquivo se o Desktop suporta-lo
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(arquivoSaida);
				}
			}
			catch (IOException e) {
				// Em caso de excecao, ela eh rastreada na pilha de execucao
				e.printStackTrace();
			}	
		}
	}

	/*
     * ***************************************************************
     * Metodo: excluirArquivo
     * Funcao: exclui o arquivo contendo os lexemas e tokens do programa
     * Parametros: ActionEvent event - evento gerado ao clicar no botao
     * Retorno: void
     ****************************************************************/

	@FXML
	private void excluirArquivo(ActionEvent event) {
		// O metodo tenta executar o seguinte bloco de codigo
		try {
			// Volta pra tela inicial se o arquivo nao for nulo, existir e for deletado
			if (arquivoSaida != null && arquivoSaida.exists() && arquivoSaida.delete()) {
				voltar(event);
			}
		}
		catch (IOException e) {
			// Em caso de excecao, ela eh rastreada na pilha de execucao
			e.printStackTrace();
		}
	}

	public void exibirErro(int linha, String erro) {
		txtErros.appendText("Linha (" + Integer.toString(linha) + "): " + erro + "\n");
	}

	/*
     * ***************************************************************
     * Metodo: carregarArquivo
     * Funcao: carrega um arquivo para ser compilado
     * Parametros: File codigo - codigo a ser compilado
     * Retorno: void
     ****************************************************************/

	public void carregarArquivo(File codigo) {
		// Carrega o arquivo de codigo
		this.codigo = codigo;

		// Inicializa o analisador lexico
		analisadorLexico = new AnalisadorLexico(this);

		Platform.runLater(() -> {
			// Escreve o codigo fonte dentro da interface
			escreverCodigo();

			// Realiza a analise lexica do codigo
			realizarAnalise();
		});
	}

	/*
     * ***************************************************************
     * Metodo: escreverCodigo
     * Funcao: insere o codigo contido no arquivo dentro do txtCodigo
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void escreverCodigo() {
		// O metodo tenta executar as seguintes operacoes de leitura com o BufferedReader
		try (BufferedReader br = new BufferedReader(new FileReader(codigo))) {
			// Variavel usada para guardar a linha
			String linha = "";

			// Enquanto ainda houverem linhas a serem lidas
			while ((linha = br.readLine()) != null) {
				// Insere as linhas dentro do txtCodigo mais uma quebra-linha
				txtCodigo.appendText(linha + "\n");
			}
		}
		catch (IOException e) {
			// Em caso de excecao, ela eh rastreada na pilha de execucao
			e.printStackTrace();
		}
	}

	/*
     * ***************************************************************
     * Metodo: realizarAnalise
     * Funcao: realiza a analise do codigo
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void realizarAnalise() {
		// Obtem a lista de tuplas a partir da analise lexica
		ArrayList<TuplaLexema> tuplas = analisadorLexico.analisarCodigo(codigo);

		// Se tivermos obtido tuplas
		if (!tuplas.isEmpty()) {			
			for (TuplaLexema t : tuplas) {
				// Formata a tupla contendo o lexema e o token, concatenando-a dentro do txtResultados
				String tupla = "<" + t.getLexema() + ", " + obterSimboloToken(t.getToken()) + ">";
				txtResultados.appendText(tupla + "\n");
			}

			// Usa as informacoes obtidas para gerar o arquivo de saida
			escreverArquivoSaida();
		}
	}

	/*
     * ***************************************************************
     * Metodo: escreverArquivoSaida
     * Funcao: gera um arquivo contendo a analise completa do codigo
     * Parametros: nenhum parametro foi definido para esta funcao
     * Retorno: void
     ****************************************************************/

	private void escreverArquivoSaida() {
		// Obtem as tuplas listadas no txtResultados
		String tuplas = txtResultados.getText();

		// Se a lista de tuplas nao estiver vazia e o arquivo do codigo fonte nao for nulo
		if (!tuplas.isEmpty() && codigo != null) {
			// Cria-se um novo arquivo a ser salvo na mesma pasta do arquivo fonte
			// e com um nome pre-definido
			String saida = "saida_" + codigo.getName();
			arquivoSaida = new File(codigo.getParent(), saida);

			// Se o arquivo de saida nao for nulo
			if (arquivoSaida != null) {
				// O metodo tenta executar as seguintes operacoes de escrita com um BufferedWriter
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoSaida))) {
					// Escreve as tuplas dentro do arquivo
					bw.write(tuplas);

					// Ativa os botoes para abrir/excluir o arquivo de saida
					btnAbrirArquivo.setDisable(false);
					btnExcluirArquivo.setDisable(false);
				}
				catch (IOException e) {
					// Em caso de excecao, ela eh rastreada na pilha de execucao
					e.printStackTrace();
				}
			}
		}
	}

	/*
     * ***************************************************************
     * Metodo: obterSimboloToken
     * Funcao: retorna uma abreviacao correspondente ao token
     * Parametros: Token token - token 
     * Retorno: String
     ****************************************************************/

	private String obterSimboloToken(Token token) {
		// O switch/case retorna o simbolo correspondente ao token
		// passado como parametro
		switch (token) {
			case IDENTIFICADOR:
				return "id";

			case PALAVRA_RESERVADA:
				return "palavra_reservada";

			case CONSTANTE_NUMERICA:
				return "num";

			case CARACTERE:
				return "char";

			case STRING:
				return "string";

			case OPERADOR_ARITMETICO:
			case OPERADOR_RELACIONAL:
			case OPERADOR_LOGICO:
			case OPERADOR_ATRIBUICAO:
				return "op";

			case SIMBOLO_ESPECIAL:
				return "simbolo_especial";
		}

		// Retorna uma string vazia caso nenhuma correspondencia
		// tiver sido encontrada
		return "";
	}
}