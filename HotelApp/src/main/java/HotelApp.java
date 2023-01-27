
import java.io.FileInputStream;
import java.io.*;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import oracle.jdbc.pool.OracleDataSource;


public class HotelApp {
    Connection conn; // obiekt Connection do nawiazania polaczenia z baza danych

    public static void main(String[] args) {
        HotelApp app = new HotelApp();

        try {
            app.setConnection(); // otwarcie polaczenia z BD
            app.mainWindow();
            app.closeConnection();// zamkniecie polaczenia z BD
        }
        catch (SQLException eSQL) {
            System.out.println("Blad przetwarzania SQL " + eSQL.getMessage());
        }
        catch (IOException eIO) {
            System.out.println("Nie mozna otworzyc pliku" );
        }
    }

    public void setConnection() throws SQLException, IOException { // metoda nawiazuje polaczenie
        String host = "ora4.ii.pw.edu.pl";
        String username = "z80";
        String password = "yh4w5t";
        String port = "1521";
        String serviceName = "pdb1.ii.pw.edu.pl";

        String connectionString = String.format(
                "jdbc:oracle:thin:%s/%s@//%s:%s/%s",
                username, password, host, port, serviceName);

        System.out.println (connectionString);
        OracleDataSource ods; // nowe zrodlo danych (klasa z drivera  Oracle)
        ods = new OracleDataSource();

        ods.setURL(connectionString);
        conn = ods.getConnection(); // nawiazujemy polaczenie z BD

        DatabaseMetaData meta = conn.getMetaData();

        System.out.println("Polaczenie do bazy danych nawiazane.");
        System.out.println("Baza danych:" + " " + meta.getDatabaseProductVersion());
    }

    public void closeConnection() throws SQLException { // zamkniecie polaczenia
        conn.close();
        System.out.println("Polaczenie z baza zamkniete poprawnie.");
    }

    public void mainWindow() throws SQLException {
        System.out.println("Wybierz polecenie: \nWyświetl gości: 1 \nDodaj gościa: 2");
        Scanner in = new Scanner(System.in);
        String request = in.nextLine();
        System.out.println("Wybrałeś polecenie " + request);
        switch (request){
            case "1":
                this.showGuests();
            case "2":
                this.addGuest();
        }
    }

    private void addGuest() throws SQLException{
        Statement statement = conn.createStatement();
        System.out.println("Wprowadź imię: ");
        Scanner in = new Scanner(System.in);
        String name = in.nextLine();
        System.out.println("Wprowadź nazwisko: ");
        String surname = in.nextLine();
        System.out.println("Wybierz pokój, wprowadzając jego numer:  ");
        ResultSet rs = statement.executeQuery("SELECT * FROM rooms WHERE occupied = 0");
        while(rs.next()){
            String room_id = rs.getString(1);
            String capacity = rs.getString(2);
            String cost = rs.getString(3);
            System.out.println("Numer pokoju: " + room_id + ", pojemność: " + capacity + ", koszt za noc: " + cost);

        }
        String room_id = in.nextLine();
        System.out.println("Wpisz ilość dni: ");
        String stay_length = in.nextLine();
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO current_guests (guest_id, name, surname) VALUES (?, ?, ?)");
        PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT INTO reservations values (?, ?, ?, ?)");
        ResultSet rs2 = statement.executeQuery("select max(guest_id) + 1 from current_guests");
        String guest_id = "";
        while (rs2.next()) {
            guest_id = rs2.getString(1);
        }
        preparedStatement.setString(1, guest_id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, surname);
        int executed1 = preparedStatement.executeUpdate();
        ResultSet rs3 = statement.executeQuery("SELECT max(reservation_id) + 1 from reservations");
        String res_id = "";
        while (rs3.next()) {
            res_id = rs3.getString(1);
        }
        preparedStatement1.setString(1, res_id);
        preparedStatement1.setString(2, room_id);
        preparedStatement1.setString(3, guest_id);
        preparedStatement1.setString(4, stay_length);
        int executed2 = preparedStatement1.executeUpdate();



    }

    public void showGuests() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM current_guests");
        System.out.println("Lista gości:");
        while(rs.next()){
            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
        }
    }

}
