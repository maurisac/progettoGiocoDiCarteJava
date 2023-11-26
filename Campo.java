package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Campo {
    private ArrayList<Carta> campo;

    public Campo(){
        campo = new ArrayList<>();
    }

    public int aggiungiCarta(Carta carta) {
        if(campo.size() < 5) {
            campo.add(carta);
            return 0;
        }
        else return 1;
    }

    public void rimuoviCarta(Carta carta) {
        campo.remove(carta);
    }

    public static void stampaCampo(Campo miocampo, PrintWriter out, BufferedReader input1, String username) throws IOException {
        String str = "Carte sul campo: ";
        out.println("Campo di " + username);
        input1.readLine();
        for (int i = 0; i < miocampo.campo.size(); i++) {
            Carta carta = miocampo.campo.get(i);
            str += carta.getNome() + " (Atk: " + carta.getAtk() + ", Def: " + carta.getDef() + ") \t";
            out.println(str);
            input1.readLine();
        }
    }

    public Carta scegliCarta(Campo miocampo, PrintWriter out, BufferedReader input1, String username) throws IOException {
        int scelta = 1;
        String choice;
        do{
            stampaCampo(miocampo, out, input1, username);
            out.println("\u001B[36m Scegliere una carta da 1 a \u001B[0m" + miocampo.campo.size());
            choice = input1.readLine();
            if(!choice.isEmpty()) scelta = Integer.parseInt(choice);
            else{
                choice = input1.readLine();
                Integer.parseInt(choice);
            }
        }while((scelta < 1 && scelta > miocampo.campo.size()) && scelta != 1);
        return miocampo.campo.get(scelta - 1);
    }
    public ArrayList<Carta> getCampo(){
        return this.campo;
    }

    public static void assignCampo(Campo campo1, Campo copia){
        int valore = campo1.campo.size();
        if(valore != 0) {
            for (int i = 0; i < valore; i++)
                copia.campo.set(i, campo1.campo.get(i));
        }
    }

    public int size(){
        return this.campo.size();
    }
}
