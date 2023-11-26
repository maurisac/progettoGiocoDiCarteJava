package org.example;
import java.io.*;
import java.net.*;

import static org.example.Arena.*;

public class GameThread extends Thread{

    Arena arena;
    ClientHandler clientHandler1;
    ClientHandler clientHandler2;


    public GameThread(ClientHandler clientHandler1, ClientHandler clientHandler2){
        this.arena = new Arena(clientHandler1.getClientUsername(),clientHandler2.getClientUsername(),clientHandler1.getClientPunteggio(),clientHandler2.getClientPunteggio());
        this.clientHandler1 = clientHandler1;
        this.clientHandler2 = clientHandler2;
    }

    @Override
    public void run(){
        Socket socket1 = clientHandler1.getClientSocket();
        Socket socket2 = clientHandler2.getClientSocket();
        PrintWriter out1;
        PrintWriter out2;
        BufferedReader input1;
        BufferedReader input2;
        try {
            out1 = new PrintWriter(socket1.getOutputStream(), true);
            input1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            out2 = new PrintWriter(socket2.getOutputStream(), true);
            input2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

            Giocatore giocatore1 = inizializzaGiocatore(clientHandler1.getClientUsername(), clientHandler1.getClientPunteggio());
            Giocatore giocatore2 = inizializzaGiocatore(clientHandler2.getClientUsername(), clientHandler2.getClientPunteggio());
            inizioPartita(giocatore1, giocatore2, input1, out1, input2, out2); //Arena, 100 pv
            boolean terminaPartita = false;
            int ngiri = 1;
            do {
                out1.println("E' il tuo turno: ");
                input1.readLine();
                terminaPartita = mostraMenu(giocatore1, giocatore2, out1, out2, input1, input2, ngiri);
                if(!terminaPartita) {
                    out2.println("E' il tuo turno");
                    input2.readLine();
                    ngiri++;
                    terminaPartita = mostraMenu(giocatore2, giocatore1, out2, out1, input2, input1, ngiri);
                }
            } while (!terminaPartita);
            finePartita(giocatore1, giocatore2, out1, out2, input1, input2);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void inviaMessaggioComune(String messaggio, PrintWriter out1, PrintWriter out2, BufferedReader input1, BufferedReader input2){
        out1.println(messaggio);
        out2.println(messaggio);
        try {
            input1.readLine();
            input2.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
