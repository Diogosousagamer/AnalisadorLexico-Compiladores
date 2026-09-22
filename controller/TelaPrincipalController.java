/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 02/09/2026
* Ultima alteracao...: 21/09/2026
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

		Platform.runLater(() -> {
			escreverCodigo();
			realizarAnalise();
		});
	}

	private void escreverCodigo() {
		try (BufferedReader br = new BufferedReader(new FileReader(codigo))) {
			String linha = "";

			while ((linha = br.readLine()) != null) {
				txtCodigo.appendText(linha + "\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void realizarAnalise() {
		ArrayList<TuplaLexema> tuplas = analisadorLexico.analisarCodigo(codigo);

		if (!tuplas.isEmpty()) {
			for (TuplaLexema t : tuplas) {
				String tupla = "<" + t.getLexema() + ", " + obterSimboloToken(t.getToken()) + ">";
				txtResultados.appendText(tupla + "\n");
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

		return "";
	}
}