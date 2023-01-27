
import java.io.FileInputStream;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        while(true) {
            System.out.println("--------------------------");
            System.out.println("Wybierz polecenie: \nWyświetl gości: 1 \nDodaj gościa: 2 \nOblicz koszt pobytu: 3 \nDodaj dodatkową usługę: 4");
            Scanner in = new Scanner(System.in);
            String request = in.nextLine();
            System.out.println("Wybrałeś polecenie " + request);
            switch (request) {
                case "1":
                    this.showGuests();
                    break;
                case "2":
                    this.addGuest();
                    break;
                case "3":
                    this.calculateCost();
                    break;
                case "4":
                    this.addExtraService();
            }
        }
    }

    private String getIdGuest() throws SQLException{
        Statement statement = conn.createStatement();
        System.out.println("Wpisz numer id gościa:");
        Scanner in = new Scanner(System.in);
        String guest_id = in.nextLine();
        return guest_id;
    }

    private void showList(ArrayList<String> listOfWords) throws SQLException{
        Statement statement = conn.createStatement();
        System.out.println("Wybierz " + listOfWords.get(0)+ ", wprowadzając " + listOfWords.get(1) + " numer:  ");
        ResultSet rs = statement.executeQuery(listOfWords.get(2));
        while(rs.next()){
            String first = rs.getString(1);
            String second = rs.getString(2);
            String third = rs.getString(3);
            System.out.println(listOfWords.get(3) + first + listOfWords.get(4) +second + listOfWords.get(5) + third);

        }
    }
    private void addExtraService() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO guest_service VALUES (?, ?, ?)");
        String guest_id = this.getIdGuest();
        ArrayList<String> listOfWords = new ArrayList<String>(Arrays.asList("usługę", "jej", "SELECT * FROM extra_service", "Numer serwisu: ", ", nazwa: ", ", koszt: " ));
        showList(listOfWords);
        String service_id = scanner.nextLine();
        System.out.println("Wybierz ilość: ");
        String quantity = scanner.nextLine();
        preparedStatement.setString(1, service_id);
        preparedStatement.setString(2, guest_id);
        preparedStatement.setString(3, quantity);
        int executedUpdate = preparedStatement.executeUpdate();
        System.out.println("Dodano usługę.");



    }

    private void calculateCost() throws SQLException{
        String guest_id = this.getIdGuest();
        PreparedStatement preparedStatement = conn.prepareStatement("select re.stay_length * ro.cost + nvl(g.quantity * es.cost, 0) from reservations re join rooms ro using(room_id) left join guest_service g on(re.guest_id = g.guest_id) left join extra_service es on(es.service_id = g.service_id)  where re.guest_id = ?");
        preparedStatement.setString(1, guest_id);
        ResultSet rs = preparedStatement.executeQuery();
        String cost = "";
        while(rs.next()){
            cost = rs.getString(1);
        }
        System.out.println("Koszt pobytu jest równy: " + cost);

    }

    private void addGuest() throws SQLException{
        Statement statement = conn.createStatement();
        System.out.println("Wprowadź imię: ");
        Scanner in = new Scanner(System.in);
        String name = in.nextLine();
        System.out.println("Wprowadź nazwisko: ");
        String surname = in.nextLine();
        ArrayList<String> listOfWords = new ArrayList<String>(Arrays.asList("pokój", "jego", "SELECT * FROM rooms WHERE occupied = 0", "Numer pokoju: ", ", pojemność: ", ", koszt za noc: " ));
        showList(listOfWords);
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
        System.out.println("Dodano gościa");



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
