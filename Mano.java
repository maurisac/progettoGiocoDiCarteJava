package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Mano extends Carta{

	private Carta[] arrayCarte = new Carta[5];
	private int quantita = -1;

	public void rimuoviCarta(int posizione){
		if(posizione >0 && posizione < quantita){
		for(int i=posizione; i<quantita; i++)
			if(this.arrayCarte[i+1] != null)this.arrayCarte[i] = this.arrayCarte[i+1];
		this.arrayCarte[quantita] = null;
		quantita --;
		}
	}

	public Carta getCarta(int posizione){
		if(posizione <0 && posizione > quantita  ) return null;
		return this.arrayCarte[posizione];
	}

	public int getQuantita(){
		return this.quantita;
	}

	public void drawCard(Carta newCarta,PrintWriter out, BufferedReader input)
	{
		if(this.quantita < 4)
			arrayCarte[++quantita] = newCarta;
		else{
			out.println("Massimo numero di carte in mano");
			try {
				input.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void stampaMano(PrintWriter out, BufferedReader input) {
		for(int i = 0; i < this.quantita; i++) {
			out.print("(" + (i+1) + ") ");
			this.arrayCarte[i].stampaCarta(out, input);
		}
	}

	public void flushCards() {
		this.quantita = -1;
	}

/*	public Mano copyMano(){
		Mano appoggio = new Mano();
		for(int i= 0; i < this.quantità+1; i++){
			appoggio.arrayCarte[i] = this.arrayCarte[i];
		}
		appoggio.quantità = this.quantità;
		return appoggio;
	}
*/

}

