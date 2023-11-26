package org.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Random;

public class Mazzo extends Carta {

	int quantity =20;
	Carta[] mazzo = new Carta[quantity];

	public void generaMazzo() {
		File inputFile = new File("C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\Carte.txt");

		Scanner scannerDaFile = null;
		try {
			scannerDaFile = new Scanner(inputFile);
			Mazzo mazzo = this;
			for (int i = 0; scannerDaFile.hasNextLine() && i < quantity; i++) {
				this.mazzo[i] = new Carta();
				this.mazzo[i].setNome(scannerDaFile.nextLine());
				this.mazzo[i].setatk(Integer.valueOf(scannerDaFile.nextLine()));
				this.mazzo[i].setDef(Integer.valueOf(scannerDaFile.nextLine()));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scannerDaFile != null) {
				scannerDaFile.close();
			}

		}
	}

	public void stampaMazzo(PrintWriter out, BufferedReader input) {
		for(int c = 0; c < quantity; c++) {
			Carta carta = this.mazzo[c];
			carta.stampaCarta(out, input);
			System.out.println(" ");
		}
	}
	public Mazzo mischiaMazzo() {


		Random rnd = new Random();
		int randNumb1;
		int j = 0;
		Mazzo mazzoMischiato = new Mazzo();
		for(int i = 0; i<100; i++)
		{
			randNumb1 = rnd.nextInt(quantity);
			if (this.mazzo[randNumb1] != null)
			{
				mazzoMischiato.mazzo[j] = this.mazzo[randNumb1];
				j++;
				this.mazzo[randNumb1] = null;
			}
		}
		return mazzoMischiato;
	}
	public Carta popCarta() {
		int i;
		for(i = 19; this.mazzo[i] != null && i> 20-this.quantity; i--);
		if(this.quantity == 0){
			return null;
		}
		Carta newCard = this.mazzo[i];
		this.mazzo[i] = null;
		this.quantity--;
		return newCard;
	}
	public static Mazzo assegnaMazzo() {
		Mazzo newMazzo = new Mazzo();
		newMazzo.generaMazzo();
		newMazzo = newMazzo.mischiaMazzo();
		return newMazzo;
	}
}
