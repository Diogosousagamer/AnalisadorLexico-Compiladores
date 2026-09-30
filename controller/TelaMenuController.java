/* ***************************************************************
* Autores............: Davi Gabrielli Santos
                       Diogo Oliveira de Sousa
                       Gustavo Henrique Oliveira Fernandes
* Matricula..........: 202410855 / 202411226 / 202410104
* Inicio.............: 02/09/2026
* Ultima alteracao...: 23/09/2026
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

	/*
     * ***************************************************************
     * Metodo: enviarArquivo
     * Funcao: envia um arquivo a ser compilado
     * Parametros: ActionEvent event - evento gerado ao clicar no botao
     * Retorno: void
     ****************************************************************/

	@FXML
	private void enviarArquivo(ActionEvent event) {
		// O metodo tenta executar o seguinte bloco de codigo
		try {
			// Abre a janela para selecionar o arquivo (mais especificamente, txt)
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("Arquivos de Texto (*.txt)", "*.txt"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			File f = fc.showOpenDialog(stage);

			if (f != null) {
				// Carrega a TelaPrincipal se algum arquivo tiver sido selecionado
				carregarTelaPrincipal(event, f);
			}
		}
		catch (IOException e) {
			// Em caso de excecao, ela eh rastreada na pilha de execucao
			e.printStackTrace();
		}
	}

	/*
     * ***************************************************************
     * Metodo: carregarTelaPrincipal
     * Funcao: carrega a TelaPrincipal para compilar o arquivo
     * Parametros: ActionEvent event - evento gerado ao clicar no botao
     			   File f - arquivo a ser enviado
     * Retorno: void
     ****************************************************************/

	private void carregarTelaPrincipal(ActionEvent event, File f) throws IOException {
		// Carrega uma nova cena contendo a TelaPrincipal
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPrincipal.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		// Acessa o controller da TelaPrincipal para que o arquivo seja carregado
		TelaPrincipalController controller = loader.getController();
		controller.carregarArquivo(f);

		// Troca a cena na janela
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);

		// Mantem a tela maximizada
		stage.setMaximized(false);
		stage.setMaximized(true);
	}
}