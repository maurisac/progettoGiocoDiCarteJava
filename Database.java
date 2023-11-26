package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Database {

    private String configFile;

    public Database(String configFile) {
        this.configFile = configFile;
    }
    public String readConfig(int i) {
        try {
            // Carica il file XML di configurazione
            // Assicurati di utilizzare il percorso completo del file XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(configFile));

            // Ottieni l'elemento radice
            Element root = document.getDocumentElement();

            // Leggi i dati di configurazione
            String dbURL = root.getElementsByTagName("url").item(0).getTextContent();
            String dbUsername = root.getElementsByTagName("username").item(0).getTextContent();
            String dbPassword = root.getElementsByTagName("password").item(0).getTextContent();

            if(i == 0)return dbUsername;
            if(i == 1)return dbURL;
            if(i == 2)return dbPassword;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String loginDB(PrintWriter out1, BufferedReader input1) throws IOException {

        int i = 0;
        String myusername = "";
        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword)) {
            out1.println("INSERIRE USERNAME");
            myusername = input1.readLine();
            String query = "SELECT * FROM giocatori WHERE Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, myusername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                out1.print("Username non valido ");
                return "";
            }
            out1.println("INSERIRE PASSWORD");
            String mypassword = input1.readLine();
            query = "SELECT * FROM giocatori WHERE Password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, mypassword);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                out1.print("Password non valida ");
                return "";
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM giocatori WHERE Username = ? AND Password = ?");
            preparedStatement.setString(1, myusername);
            preparedStatement.setString(2, mypassword);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                out1.print("Le credenziali non corrispondono ");
                return "";
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out1.print("Benvenuto " + myusername + " ");
        return myusername;
    }


    public static int registerdb(String myusername, String mypassword, PrintWriter out1, BufferedReader input1) {

        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(int i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword);

            String query = "INSERT INTO giocatori (Username, Password) VALUES (?, ?)";

            // Crea un oggetto PreparedStatement per eseguire la query
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, myusername);
            preparedStatement.setString(2, mypassword);


            // Esegui la query di inserimento
            int righeInserite = preparedStatement.executeUpdate();

            if (righeInserite > 0) {
                out1.print("Registrazione completata ");
                return 0;
            } else {
                out1.print("Registrazione fallita");
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int getPunteggio(String myusername) {
        int punteggio = 0;
        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(int i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM giocatori WHERE Username = ?");
            preparedStatement.setString(1, myusername);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Leggi i dati dalla riga
                punteggio = resultSet.getInt("Punteggio");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return punteggio;
    }

    public static int getPartiteGiocate(String myusername) {
        int partiteGiocate = 0;
        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(int i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM giocatori WHERE Username = ?");
            preparedStatement.setString(1, myusername);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Leggi i dati dalla riga
                partiteGiocate = resultSet.getInt("PartiteGiocate");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partiteGiocate;
    }

    public static void setPunteggio(String myusername, int nuovoValore) {
        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(int i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword);
            String selectQuery = "SELECT * FROM giocatori WHERE username = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);

            selectStatement.setString(1, myusername);
            ResultSet resultSet = selectStatement.executeQuery();

            // Ora puoi elaborare i dati nella riga risultante.
            if (resultSet.next()) {

                int punteggio = nuovoValore;

                String updateQuery = "UPDATE giocatori SET Punteggio = ? WHERE Username = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                // Imposta i nuovi valori
                updateStatement.setInt(1, nuovoValore);
                updateStatement.setString(2, myusername);

                // Esegui l'aggiornamento
                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Riga aggiornata con successo!");
                } else {
                    System.out.println("Nessuna riga aggiornata.");
                }

                updateStatement.close();
            } else {
                System.out.println("Nessuna riga trovata con l'identificatore specificato.");
            }

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setPartiteGiocate(String myusername, int nuovoValore) {
        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(int i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword);
            String selectQuery = "SELECT * FROM giocatori WHERE username = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);

            selectStatement.setString(1, myusername);
            ResultSet resultSet = selectStatement.executeQuery();

            // Ora puoi elaborare i dati nella riga risultante.
            if (resultSet.next()) {

                int punteggio = nuovoValore;

                String updateQuery = "UPDATE giocatori SET PartiteGiocate = ? WHERE Username = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                // Imposta i nuovi valori
                updateStatement.setInt(1, nuovoValore);
                updateStatement.setString(2, myusername);

                // Esegui l'aggiornamento
                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Riga aggiornata con successo!");
                } else {
                    System.out.println("Nessuna riga aggiornata.");
                }

                updateStatement.close();
            } else {
                System.out.println("Nessuna riga trovata con l'identificatore specificato.");
            }

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static Giocatore[] getClassifica(){

        Giocatore [] classifica = new Giocatore[5];
        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(int i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword);
            String selectQuery = "SELECT * FROM giocatori ORDER BY Punteggio DESC LIMIT 5";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = selectStatement.executeQuery();

            // Leggi i dati dalla query e aggiungi gli utenti all'array
            int i= 0;
            while (resultSet.next()) {
                String nome = resultSet.getString("Username");
                int punteggio = resultSet.getInt("Punteggio");

                classifica[i] = new Giocatore();
                classifica[i].setUsername(nome);
                classifica[i].setPunteggio(punteggio);
                i++;
            }
            // Ora l'array "classifica" contiene i primi 5 utenti con i punteggi più alti in ordine decrescente.

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classifica;
    }

    public static Giocatore loadGiocatore(String myusername){

        Giocatore mioGiocatore = new Giocatore();
        int punteggio = 0;
        String dbusername = null, jdbcURL = null, dbpassword = null;
        String configFile = "C:\\Users\\user\\Desktop\\OOP\\Progetto OOP\\src\\main\\java\\org\\example\\config.xml";
        Database configReader = new Database(configFile);
        for(int i=0; i<3; i++){
            if(i == 0) dbusername = configReader.readConfig(i);
            if(i == 1) jdbcURL = configReader.readConfig(i);
            if(i == 2) dbpassword = configReader.readConfig(i);
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, dbusername, dbpassword);
            String query = "SELECT * FROM giocatori WHERE Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, myusername);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                punteggio = resultSet.getInt("Punteggio");
            }
            mioGiocatore.generaGiocatore(myusername, punteggio);

        }catch (SQLException e) {
            e.printStackTrace();
        }
            return mioGiocatore;
    }
}