package org.example;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientProgetto {
	public static void main(String[] args) throws IOException {
		String serverAddress = "localhost";
		int serverPort = 9090;
		try (Socket s = new Socket(serverAddress, serverPort);
			 PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			 BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			 Scanner scanner = new Scanner(System.in)
		) {
			String serverResponse;
			String clientMessage;

			System.out.println("Connesso al server.");

			while (true) {
				serverResponse = input.readLine();
				if (serverResponse == null) {
					break; // Connessione chiusa dal server
				}
				System.out.println("Messaggio dal server: " + serverResponse);

				// Lascia che il client inserisca una risposta
				System.out.print(": ");
				clientMessage = scanner.nextLine();
				out.println(clientMessage); // Invia la risposta al server

				if ("Chiudere connessione".equals(serverResponse) || "Chiudere connessione".equals(clientMessage)) {
					s.close();
					scanner.close();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}