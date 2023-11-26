package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import static org.example.Campo.*;
import static org.example.Database.setPunteggio;
import static org.example.GameThread.inviaMessaggioComune;
import static org.example.Mazzo.assegnaMazzo;


public class Arena {

	private Giocatore player1;
	private Giocatore player2;
	private static Campo campoPlayer1;
	private static Campo campoPlayer2;
	
    public Arena(String username1, String username2, int punteggio1, int punteggio2){

		this.player1 = inizializzaGiocatore(username1, punteggio1);
		this.player2 = inizializzaGiocatore(username2, punteggio2);
		this.campoPlayer1 = new Campo();
		this.campoPlayer2 = new Campo();
	}

	public static Giocatore inizializzaGiocatore(String username, int punteggio){
		Giocatore giocatore = new Giocatore();
		giocatore.setUsername(username);
		giocatore.setPunteggio(punteggio);
		return giocatore;
	}

	public static void inizioPartita(Giocatore player1, Giocatore player2, BufferedReader input1, PrintWriter out1, BufferedReader input2, PrintWriter out2) {

		player1.setMazzo(assegnaMazzo()); 	//genera, mischia e returna un mazzo
		player2.setMazzo(assegnaMazzo());
        player1.sottraiPv(-100);
		player2.sottraiPv(-100);
		for(int i = 0; i<3; i++) 			//assegnazione carte alle mani dei giocatori
		{
			player1.getMano().drawCard(player1.getMazzo().popCarta(), out1, input1);
			player2.getMano().drawCard(player2.getMazzo().popCarta(), out2, input2);
		}
	}

	public static boolean mostraMenu(Giocatore player1, Giocatore player2, PrintWriter out1, PrintWriter out2, BufferedReader input1, BufferedReader input2, int ngiri) throws IOException {
		//l'interazione è solo per il primo player passato
		int number;
		boolean terminaPartita = false;
		String choice;
		Campo Appoggio = new Campo();
			boolean faseEvoca = false, terminaTurno = false, fasePescaggio = false;
			do {
				do {
					out1.println("\u001B[36m Giocatore: " + player1.getUsername() + " (1)Pesca \t (2)Evoca \t (3)Combatti \t (4)Passa \u001B[0m");
					choice = input1.readLine();
					if(!choice.isEmpty()) number = Integer.parseInt(choice);
					else{
						choice = input1.readLine();
						number = Integer.parseInt(choice);
					}
				} while (number < 1 || number > 4);
				switch (number) {
					case 1: {
						if (!(faseEvoca) && !(fasePescaggio)) {
							pescaggio(player1, out1, input1);
							fasePescaggio = true;
						} else {
							out1.println("Si può pescare solo come prima azione");
							input1.readLine();
						}
							break;
						}
					case 2: {
						if(!faseEvoca) {
							evoca(player1, out1, input1);
							faseEvoca = true;
						}else{
							out1.println("Si può evocare solo una volta per turno");
							input1.readLine();
						}
						break;
					}
					case 3: {
						if(ngiri >1) {
							terminaPartita = combatti(player1, player2, campoPlayer1, campoPlayer2, out1, out2, input1, input2); //attaccante,difensore
							terminaTurno = true;
						}
						else {
							out1.println("il primo giro non si può combattere");
							input1.readLine();
						}
						break;
					}
					case 4: {
						terminaTurno = true;
						break;
					}
				}
			} while (!terminaTurno); // si scambiano i giocatori fintanto che terminaTurno = true
			out1.println("il tuo turno è terminato");
			Appoggio = campoPlayer1;
			campoPlayer1 = campoPlayer2;
			campoPlayer2 = Appoggio;
			return terminaPartita;
	}

	public static void pescaggio(Giocatore player, PrintWriter out, BufferedReader input) throws IOException { //true mazzo=vuoto, false=riuscito
		Carta newCarta = player.getMazzo().popCarta();
		if(newCarta != null){
			player.getMano().drawCard(newCarta, out, input);
			out.println("Hai pescato :");
			input.readLine();
			player.getMano().stampaMano(out, input);
		}else {
			out.println("Finite le carte nel mazzo!");
			input.readLine();
		}
	}
	
	
	
	public static void evoca(Giocatore player, PrintWriter out, BufferedReader input) throws IOException {
        int sceltaCarta;
		String choice;
		do {
			player.getMano().stampaMano(out, input);
			out.println("\u001B[36m Scegliere una carta \u001B[0m");
			choice = input.readLine();
			if(!choice.isEmpty()) sceltaCarta = Integer.parseInt(choice);
			else{
				choice = input.readLine();
				sceltaCarta = Integer.parseInt(choice);
			}
		}while(sceltaCarta <1 || sceltaCarta > (player.getMano().getQuantita()+1) );
		int check = campoPlayer1.aggiungiCarta(player.getMano().getCarta(sceltaCarta - 1));
		if(check == 0) {
			player.getMano().rimuoviCarta(sceltaCarta - 1);
			out.println("Carta evocata! ");
			input.readLine();
			stampaCampo(campoPlayer1, out, input, player.getUsername());
		}
		else {
			out.println("Ci sono già 5 carte sul campo, impossibile evocarne altre");
			input.readLine();
		}
    }

	public static boolean combatti(Giocatore pAttaccante, Giocatore pDifensore, Campo attaccante, Campo difensore, PrintWriter out1, PrintWriter out2, BufferedReader input1, BufferedReader input2) throws IOException {
		boolean checkVita = false;
		if(!attaccante.getCampo().isEmpty()) {
			Carta cartaAttaccante = campoPlayer1.scegliCarta(attaccante, out1, input1, pAttaccante.getUsername());
			if (difensore.getCampo().isEmpty()) {
				checkVita = attaccoDiretto(pAttaccante.getUsername(), cartaAttaccante, pDifensore, out1, out2, input1, input2);
			} else {
				Carta cartaDifensore = campoPlayer2.scegliCarta(difensore, out1, input1, pDifensore.getUsername());

				if (cartaAttaccante != null && cartaDifensore != null) {
					int danno = cartaAttaccante.getAtk() - cartaDifensore.getDef();
					if (danno > 0) { //perdita difesa
						checkVita = pDifensore.sottraiPv(danno);
						difensore.rimuoviCarta(cartaDifensore); //perdita carta in difesa
						out1.println("il danno inflitto è di: " + danno + " pv. Rimangono " + pDifensore.getPv() + " pv");
						out2.println("il danno subito è di: " + danno + "pv " + pDifensore.getPv() + " pv");
						input1.readLine();
						input2.readLine();
					} else if (danno < 0) { //perdita attacco
						checkVita = pAttaccante.sottraiPv(-danno);
						attaccante.rimuoviCarta(cartaAttaccante);
						out1.println("il danno subito è di: " + danno + " pv " + pAttaccante.getPv() + " pv");
						out2.println("il danno inflitto è di: " + danno + "pv " + pAttaccante.getPv() + " pv");
						input1.readLine();
						input2.readLine();
					} else { //perdono entrambi
						attaccante.rimuoviCarta(cartaAttaccante);
						difensore.rimuoviCarta(cartaDifensore);
						stampaCampo(attaccante, out1, input1, pAttaccante.getUsername());
						stampaCampo(difensore, out2, input2, pDifensore.getUsername());
						inviaMessaggioComune("Scontro in parità. Entrambi i giocatori perdono la carta nello scontro", out1, out2, input1, input2);
					}
				}
			}
		}
		else {
			out1.println("Non hai carte sul campo per attaccare! ");
			input1.readLine();
		}
		if((pDifensore.getPv() <= 0) || (pAttaccante.getPv() <= 0)) checkVita = true;
		return checkVita;
	}

	public static boolean attaccoDiretto(String nomeAttaccante, Carta cartaAttaccante, Giocatore pDifensore, PrintWriter out1, PrintWriter out2, BufferedReader input1, BufferedReader input2){
		boolean result = pDifensore.sottraiPv(cartaAttaccante.getAtk());
		inviaMessaggioComune(nomeAttaccante + " ATTACCA DIRETTAMENTE: " + cartaAttaccante.getAtk() + " DANNI. RIMANENTI " + pDifensore.getPv() + " PV!", out1, out2, input1, input2);
		return result;
	}

	public static void finePartita(Giocatore player1, Giocatore player2, PrintWriter out1, PrintWriter out2, BufferedReader input1, BufferedReader input2) throws IOException {
		Giocatore giocatoreVincente = new Giocatore();
		Giocatore giocatorePerdente = new Giocatore();

		if (player1.sottraiPv(0) == true) {
			giocatoreVincente = player2;
			giocatorePerdente = player1;
			out1.println("HAI PERSO!! I TUOI PV SONO SCESI A ZERO!! PERDI ANCHE 5 PUNTI DALLA CLASSIFICA!! ");
			input1.readLine();
			out2.println("COMPLIMENTI TI SEI AGGIUDICATO LA VITTORIA!! GUADAGNI 10 PUNTI IN CLASSIFICA!! ");
			input2.readLine();
		}
		else {
			giocatoreVincente = player1;
			giocatorePerdente = player2;
			out2.println("HAI PERSO!! I TUOI PV SONO SCESI A ZERO!! PERDI ANCHE 5 PUNTI DALLA CLASSIFICA!! ");
			input2.readLine();
			out1.println("COMPLIMENTI TI SEI AGGIUDICATO LA VITTORIA!! GUADAGNI 10 PUNTI IN CLASSIFICA!! ");
			input1.readLine();
		}
		giocatoreVincente.increasePunteggio();
		setPunteggio(giocatoreVincente.getUsername(), giocatoreVincente.getPunteggio());
		giocatorePerdente.decreasePunteggio();
		setPunteggio(giocatorePerdente.getUsername(), giocatorePerdente.getPunteggio());
		out1.println("Chiudere connessione");
		out2.println("Chiudere connessione");
	}
}