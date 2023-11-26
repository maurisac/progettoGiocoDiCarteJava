package org.example;
import java.io.*;
import java.net.*;
import java.util.List;
import static org.example.Database.getClassifica;
import static org.example.Database.getPunteggio;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final String username;
    private final int punteggio;
    private volatile List<ClientHandler> listaClient;


    public ClientHandler(Socket clientSocket, String username, int punteggio, List<ClientHandler> mainlistaClient) {
        this.clientSocket = clientSocket;
        this.username = username;
        this.punteggio = punteggio;
        this.listaClient = mainlistaClient;

    }
    @Override
    public void run() {
        int number;
        PrintWriter out1;
        BufferedReader input1;
        do {
            do {
                try {
                    out1 = new PrintWriter(this.clientSocket.getOutputStream(), true);
                    input1 = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                    String messaggio = ("\u001B[36m \t BENVENUTO NELL'APPLICAZIONE \t\t (1)Nuova Partita \t (2)Profilo e Classifica \t (3)Regolamento \t (0)Esci \u001B[0m");
                    inviaMessaggio(messaggio, out1);
                    String choice = input1.readLine();
                        while(choice == null || choice.equals("")) choice = input1.readLine();
                        number = Integer.parseInt(choice);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } while (number < 0 || number > 3);
            switch (number) {
                case 1: {
                    try{
                        if (listaClient.isEmpty()) aggiungiPrimo(out1, input1);
                        else aggiungiSecondo(out1, input1);
                    }catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case 2: {
                    profiloClassifica(this.username, out1, input1);
                    break;
                }
                case 3: {
                    regolamento(out1, input1);
                    break;
                }
                case 0: {
                    out1.println("Chiudere connessione");
                    try {
                        input1.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }while(true);
    }
    public void aggiungiPrimo(PrintWriter out, BufferedReader input) throws InterruptedException, IOException {
        listaClient.add(this);
        out.println("In attesa di altri giocatori...");
        try {
            input.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(listaClient.size() < 2);
        out.println("Partita trovata ");
        input.readLine();
        ClientHandler client1 = listaClient.get(0);
        ClientHandler client2 = listaClient.get(1);
        Thread gameThread = new GameThread(client1, client2);
        gameThread.start();
        if(listaClient.get(0) != null)listaClient.remove(1);
        gameThread.join();
    }


    public void aggiungiSecondo(PrintWriter out1, BufferedReader input1) throws InterruptedException, IOException {
        listaClient.add(this);
        while(listaClient.size() < 2);
        out1.println("Partita trovata: attendi il turno dell'avversario ...");
        input1.readLine();
        while (true);
    }

    public static void profiloClassifica(String myusername, PrintWriter out1, BufferedReader input1){

        Giocatore[] classifica = getClassifica();
        int mypunteggio = getPunteggio(myusername);
        inviaMessaggio(" PROFILO: \t    username: " + myusername + " punteggio:" + mypunteggio+ "\t\t CLASSIFICA: \t (1) " + classifica[0].getUsername() + " punteggio: " + classifica[0].getPunteggio() +"\t (2) " + classifica[1].getUsername() + " punteggio: " + classifica[1].getPunteggio() + "\t (3) " + classifica[2].getUsername() + " punteggio: " + classifica[2].getPunteggio() + "\t (4) " + classifica[3].getUsername() + " punteggio: " + classifica[3].getPunteggio() + "\t (5) " + classifica[4].getUsername() + " punteggio: " + classifica[4].getPunteggio() + " ", out1);
        try {
            input1.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void regolamento(PrintWriter out1, BufferedReader input1) {
        try {
            inviaMessaggio("Ordine delle azioni consentite: Pescaggio, Evocazione, Combattimento, Termine del turno. E' possibile effettuare una sola azione", out1);
            input1.readLine();
            inviaMessaggio("Combattimento: Entrambi i giocatori partono con 500 pv, si seleziona la carta con cui si vuole attaccare e la carta bersaglio. il danno sarà calcolato come attacco-difesa", out1);
            input1.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void inviaMessaggio(String messaggio, PrintWriter out1) {
        out1.println(messaggio);
    }

    public Socket getClientSocket(){
        return this.clientSocket;
    }

    public String getClientUsername(){
        return this.username;
    }

    public int getClientPunteggio(){
        return this.punteggio;
    }

}

