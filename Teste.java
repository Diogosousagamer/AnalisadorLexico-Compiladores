public class Teste {
	public static void main(String[] args) {
		// Instancias do automato
		Automato automato = new Automato();

		// Estados do automato
		Estado e0 = new Estado(0, true, false);
		Estado e1 = new Estado(1, false, true);
		Estado e2 = new Estado(2, false, false);

		// Adicao dos simbolos do automato
		automato.addSimbolo('0');
		automato.addSimbolo('1');

		// Adicao dos estados do automato
		automato.addEstado(e0);
		automato.addEstado(e1);
		automato.addEstado(e2);

		// Transicoes do automato
		automato.addTransicao(e0, '0', e0);
		automato.addTransicao(e0, '1', e1);
		automato.addTransicao(e1, '0', e0);
		automato.addTransicao(e1, '1', e2);
		automato.addTransicao(e2, '0', e2);
		automato.addTransicao(e2, '1', e1);

		Estado estAtual = automato.funcaoDeTransicaoEstendida("0101011");

		if (estAtual != null) {
			System.out.println("Estado atual: " + estAtual.getId());

			if (estAtual.ehFinal()) {
				System.out.println("Cadeia reconhecida no automato!");
			}
			else {
				System.out.println("Cadeia nao faz parte do automato!");
			}
		} 
		else {
			System.out.println("Erro no reconhecimento da cadeia!");
		}
	}
}