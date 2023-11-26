package org.example;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class ServerProgetto extends Database{
	private static volatile List<ClientHandler> listaClient = new ArrayList<>();
	public ServerProgetto(String configFile) {
		super(configFile);
	}

	public static void main (String[] args) throws IOException{
		while(true) {
			ServerSocket listener = new ServerSocket(9090);
			System.out.println("Server is running");
			Socket socket1 = listener.accept();
			PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
			BufferedReader input1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
			String myusername;
			do {
				String messaggio = "(1)Login \t (2)Registrati";
				int number;
				do {
					out1.println(messaggio);
					String scelta = input1.readLine();
					number = Integer.valueOf(scelta);
				} while (number != 1 && number != 2);
				if (number == 2) myusername = registraGiocatore(out1, input1);
				else myusername = loginGiocatore(out1, input1);
			} while (myusername.equals(""));
			ClientHandler clientHandler = new ClientHandler(socket1, myusername, getPunteggio(myusername), listaClient);
			clientHandler.start();
			listener.close();
		}
	}

	public static String loginGiocatore(PrintWriter out1, BufferedReader input1) throws IOException {

		String check = "";
		int number = 0;
		do {
			try {
				check = loginDB(out1, input1);
				if (check.equals("")){
					do {
						out1.println("(1)riprova (0)indietro");
						String scelta = input1.readLine();
						number = Integer.valueOf(scelta);
					}while(number <0 || number >1);
					}
				else number = -1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}while(number != 0 && number != -1);
		if(number == 0) return "";
		return check;
	}
	public static String registraGiocatore (PrintWriter out1, BufferedReader input1) throws IOException {

		String scelta, myusername, mypassword;
		int number = 0;
		out1.println("Inserire lo Username: ");
		myusername = input1.readLine();
		out1.println("Inserire la password");
		mypassword = input1.readLine();
		do {
			try {
				int check = registerdb(myusername, mypassword, out1, input1);
				if (check == 1) {
					do {
						out1.println("(1)riprova (0)indietro");
						scelta = input1.readLine();
						number = Integer.valueOf(scelta);
					}while(number <0 || number >1);
				}
				else number = -1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}while(number != 0 && number != -1);
		if (number == -1) return "";
		return myusername;
	}
}