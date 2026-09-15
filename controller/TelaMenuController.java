/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 02/09/2026
* Ultima alteracao...: 02/09/2026
* Nome...............: TelaMenuController
* Funcao.............: Classe que gerencia as operacoes da TelaMenu.
                     
*************************************************************** */

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class TelaMenuController {
	// Componentes da interface
	@FXML private Button btnEnviarArquivo;

	@FXML
	private void enviarArquivo(ActionEvent event) {
		try {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("Arquivos de Texto (*.txt)", "*.txt"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			File f = fc.showOpenDialog(stage);

			if (f != null) {
				carregarTelaPrincipal(event, f);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void carregarTelaPrincipal(ActionEvent event, File f) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPrincipal.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		TelaPrincipalController controller = loader.getController();
		controller.carregarArquivo(f);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);

		stage.setMaximized(false);
		stage.setMaximized(true);
	}
}