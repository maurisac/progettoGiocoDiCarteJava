package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Carta {

    private String nome;
    private int atk;
    private int def;

    public void setNome(String nome){ this.nome = nome; }

    public String getNome() 
    {
        return this.nome;
    }

    public void setatk(int atk){ this.atk = atk; }
    public int getAtk() 
    {
        return this.atk;
    }

    public int getDef()
    {
        return this.def;
    }

    public void setDef(int def){this.def = def;}

    public void generateCarta(String nome, int atk, int def) {
        this.nome = nome;
        this.atk = atk;
        this.def = def;
    }

    public void stampaCarta(PrintWriter out, BufferedReader input) {
        out.println("Nome: " + this.getNome() + " Valore di attacco: " + this.getAtk() + " Valore di difesa: " + this.getDef());
        try {
            input.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}