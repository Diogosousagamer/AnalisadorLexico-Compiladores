package model;

public class Estado {
	private int id;
	private boolean ehFinal;
	private boolean ehInicial;

	public Estado(int id, boolean ehInicial, boolean ehFinal) {
		this.id = id;
		this.ehInicial = ehInicial;
		this.ehFinal = ehFinal;
	}

	public int getId() {
    	return id;
	}

	public boolean ehFinal() {
    	return ehFinal;
	}

	public boolean ehInicial() {
    	return ehInicial;
	}

}
