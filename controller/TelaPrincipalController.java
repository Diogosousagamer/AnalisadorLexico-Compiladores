package controller;

import java.io.File;
import javafx.fxml.FXML;
import model.AnalisadorLexico;

public class TelaPrincipalController {
	private AnalisadorLexico analisadorLexico;
	private File f;

	public void carregarArquivo(File f) {
		this.f = f;
	}
}