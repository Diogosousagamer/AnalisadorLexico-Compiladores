package controller;

import java.io.File;
import java.io.IOException;
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

public class TelaPrincipalController {
	// Componentes da interface
	@FXML private Button btnVoltar;
	@FXML private TextArea txtCodigo;
	@FXML private TextArea txtErros;
	@FXML private TextArea txtResultados;

	// Variaveis e instancias
	private AnalisadorLexico analisadorLexico;
	private File codigo;

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

	public void carregarArquivo(File codigo) {
		this.codigo = codigo;
	}
}