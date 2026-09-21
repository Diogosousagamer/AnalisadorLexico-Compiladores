/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 15/09/2026
* Ultima alteracao...: 15/09/2026
* Nome...............: Compilador - Mini Pascal (Principal)
* Funcao.............: Executa a aplicacao.
                     
*************************************************************** */

import controller.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Principal extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		TelaMenuController TelaMenuController = new TelaMenuController();
		TelaPrincipalController TelaPrincipalController = new TelaPrincipalController();

		Parent root = FXMLLoader.load(getClass().getResource("/view/TelaMenu.fxml"));
		Scene scene = new Scene(root);

		scene.getStylesheets().add(getClass().getResource("util/menu.css").toExternalForm());
		Font.loadFont(getClass().getResourceAsStream("/util/VCR_OSD_MONO_1.001.ttf"), 18);

		Image icon = new Image(getClass().getResource("/img/compiler.png").toExternalForm());

		stage.setTitle("Compilador - Mini Pascal");
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.setMaximized(true);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}