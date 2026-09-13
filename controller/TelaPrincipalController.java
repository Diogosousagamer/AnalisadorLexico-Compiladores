/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 02/09/2026
* Ultima alteracao...: 13/09/2026
* Nome...............: TelaPrincipalController
* Funcao.............: Classe que gerencia as operacoes da TelaPrincipal.
                     
*************************************************************** */

package controller;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	@FXML private Button btnVoltar;
	@FXML private TextArea txtCodigo;
	@FXML private TextArea txtErros;
	@FXML private TextArea txtResultados;

	// Variaveis e instancias
	private AnalisadorLexico analisadorLexico;
	private File codigo;
	private File arquivoSaida;

	@FXML
	private void voltar(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaMenu.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);

		stage.setMaximized(false);
		stage.setMaximized(true);
	}

	@FXML
	private void abrirArquivo(ActionEvent event) {
		if (arquivoSaida != null && arquivoSaida.exists()) {
			try {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(arquivoSaida);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

	public void carregarArquivo(File codigo) {
		this.codigo = codigo;
		analisadorLexico = new AnalisadorLexico(this);

		realizarAnalise();
	}

	private void realizarAnalise() {
		ArrayList<TuplaLexema> tuplas = analisadorLexico.analisarCodigo(codigo);

		if (!tuplas.isEmpty()) {
			for (TuplaLexema t : tuplas) {
				String tupla = "<" + t.getLexema() + ", " + obterSimboloToken(t.getToken()) + ">";
				txtResultados.appendText(tupla + "\n");
				dormir();
			}

			escreverArquivoSaida();
		}
	}

	private void escreverArquivoSaida() {
		String tuplas = txtResultados.getText();

		if (!tuplas.isEmpty() && codigo != null) {
			String saida = "saida_" + codigo.getName();
			arquivoSaida = new File(codigo.getParent(), saida);

			if (arquivoSaida != null) {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoSaida))) {
					bw.write(tuplas);
					btnAbrirArquivo.setDisable(false);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String obterSimboloToken(Token token) {
		switch (token) {
			case IDENTIFICADOR:
			case PALAVRA_RESERVADA:
				return "id";

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

		return "";
	}

	private void dormir() {
		try {
			Thread.sleep(500);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}