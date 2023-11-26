package org.example;

public class Giocatore {

	private int pv = 0;
	private Mano mano = new Mano();
	private int punteggio = 0;
	private int partiteGiocate = 0;
	private int password = 0;
	private String username = "null";
	private boolean searchinGame = false;

	private Mazzo mioMazzo;

	public void generaGiocatore(String myusername, int punteggio){
		this.username = myusername;
		this.punteggio = punteggio;
	}

	public Mazzo getMazzo(){
		return mioMazzo;
	}
	public void setMazzo(Mazzo mazzo){this.mioMazzo = mazzo;}
	public void setMano(Mano mano){this.mano = mano;}

	public Mano getMano() {
		//return this.mano.copyMano(); //forse non necessario
		return this.mano;
	}

	public int getPunteggio() {
		return this.punteggio;
	}

	public int getPartiteGiocate() {
		return this.partiteGiocate;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public int getPassword() {
		return this.password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}

	public int getPv(){
		return this.pv;
	}

	public boolean sottraiPv(int quantità)  //false ancora vivo, true ha perso.
	{	
		this.pv -= quantità;
		if(this.pv > 0)
			return false;
		else
			return true;
	}

	public void increasePunteggio() {
		this.punteggio += 10;
	}

	public void decreasePunteggio()
	{
		this.punteggio -= 5;
	}

	public void increasePartiteGiocate() {
		this.partiteGiocate ++;
	}
	
}