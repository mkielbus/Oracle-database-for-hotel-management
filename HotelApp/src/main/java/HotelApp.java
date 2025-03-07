
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import oracle.jdbc.datasource.impl.OracleDataSource;



public class HotelApp {
    Connection conn;

    private static boolean checkIfNotNegativeNumber(String text, boolean include_zero) {
        try {
            int number = Integer.parseInt(text);
            if(include_zero)
                return number >= 0;
            else
                return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean checkIfNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        HotelApp app = new HotelApp();

        try {
            app.setConnection();
            app.mainWindow();
            app.closeConnection();
        }
        catch (SQLException eSQL) {
            System.out.println("Blad przetwarzania SQL " + eSQL.getMessage());
        }
        catch (IOException eIO) {
            System.out.println("Nie mozna otworzyc pliku" );
        }
    }

    public void setConnection() throws SQLException, IOException {
        String host = "localhost";
        String username = "system";
        String password = "password";
        String port = "1521";
        String serviceName = "oraclePDB";

        String connectionString = String.format(
                "jdbc:oracle:thin:%s/%s@//%s:%s/%s",
                username, password, host, port, serviceName);

        System.out.println (connectionString);
        OracleDataSource ods;
        ods = new OracleDataSource();

        ods.setURL(connectionString);
        conn = ods.getConnection();

        DatabaseMetaData meta = conn.getMetaData();

        System.out.println("Polaczenie do bazy danych nawiazane.");
        System.out.println("Baza danych:" + " " + meta.getDatabaseProductVersion());
    }

    public void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Polaczenie z baza zamkniete poprawnie.");
    }

    public void mainWindow() throws SQLException {
        Scanner in = new Scanner(System.in);
        boolean cont = true;
        while(cont) {
            System.out.println("--------------------------");
            System.out.println("Wybierz polecenie wpisując odpowiednią cyfrę: ");
            System.out.println("Wyświetl gości: 1 \nDodaj gościa: 2 \nOblicz koszt pobytu: 3 \nDodaj dodatkową usługę: 4 \nWyświetl roczny budżet: 5 \nDodaj ocenę hotelowi: 6 \nWyświetl listę pracowników: 7 \nWymelduj gościa: 8\nWyjdź z programu: 9");
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
                    break;
                case "5":
                    this.showAnnualBudget();
                    break;
                case "6":
                    this.addReview();
                    break;
                case "7":
                    this.showEmployees();
                    break;
                case "8":
                    this.deleteGuest();
                    break;
                default:
                    cont = false;
            }
        }
        in.close();
    }

    private void deleteGuest() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("Delete from current_guests where guest_id = ?");
        PreparedStatement preparedStatement1 = conn.prepareStatement("Delete from reservations where guest_id = ?");
        PreparedStatement preparedStatement2 = conn.prepareStatement("Delete from guest_service where guest_id = ?");
        String guest_id = this.getIdGuest();
        preparedStatement.setString(1, guest_id);
        preparedStatement1.setString(1, guest_id);
        preparedStatement2.setString(1, guest_id);
        int executedUpdate1 = preparedStatement1.executeUpdate();
        int executedUpdate2 = preparedStatement2.executeUpdate();
        int executedUpdate = preparedStatement.executeUpdate();
        System.out.println("Wymeldowano.");

    }

    private void showEmployees() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select e.name, e.surname, d.name as department, es.shift_date  from employees e join departments d using(department_id) join emp_schedule es on(e.employee_id = es.employee_id)");
        while(rs.next()){
            System.out.println("Imię i nazwisko: " + rs.getString(1) + " " + rs.getString(2) + ", departament: " + rs.getString(3) + ", zmiana: " + rs.getString(4));
        }
    }

    private void addReview() throws SQLException{
        Statement statement = conn.createStatement();
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO reviews VALUES (?, ?, ?)");
        Scanner sc = new Scanner(System.in);
        String guest_id = this.getIdGuest();
        System.out.println("Wystaw ocenę w skali 1-5");
        String review = sc.nextLine();
        boolean is_correct_number = checkIfNotNegativeNumber(review, false);
        while(!is_correct_number || Integer.parseInt(review) > 5) {
            System.out.println("Podaj poprawną ocenę: ");
            review = sc.nextLine();
            is_correct_number = checkIfNotNegativeNumber(review, false);
        }
        ResultSet rs = statement.executeQuery("select max(review_id) + 1 from reviews");
        String review_id = "";
        while (rs.next()) {
            review_id = rs.getString(1);
        }
        preparedStatement.setString(1, review_id);
        preparedStatement.setString(2, review);
        preparedStatement.setString(3, guest_id);
        int executedUpdate = preparedStatement.executeUpdate();
        System.out.println("Dodano ocenę.");

    }

    private void showAnnualBudget() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("select budget from budget b where extract(year from b.year) = ?");
        Scanner sc = new Scanner(System.in);
        System.out.println("Wpisz rok: (od 2020)");
        String year = sc.nextLine();
        boolean is_correct_number = checkIfNotNegativeNumber(year, false);
        while(!is_correct_number || Integer.parseInt(year) < 2020) {
            System.out.println("Podaj poprawny rok: ");
            year = sc.nextLine();
            is_correct_number = checkIfNotNegativeNumber(year, false);
        }
        preparedStatement.setString(1, year);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            System.out.println("Roczny budżet za rok "+ year + " wynosi: " + rs.getString(1));
        }


    }

    private String getIdGuest() throws SQLException{
        Statement statement = conn.createStatement();
        System.out.println("Wpisz numer id gościa:");
        Scanner in = new Scanner(System.in);
        String guest_id = in.nextLine();
        boolean is_correct_number = checkIfNumber(guest_id);
        while(!is_correct_number) {
            System.out.println("Podaj poprawne id: ");
            guest_id = in.nextLine();
            is_correct_number = checkIfNumber(guest_id);
        }
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
        boolean is_correct_number = checkIfNumber(service_id);
        while(!is_correct_number) {
            System.out.println("Podaj poprawne id: ");
            service_id = scanner.nextLine();
            is_correct_number = checkIfNumber(service_id);
        }
        System.out.println("Wybierz ilość: ");
        String quantity = scanner.nextLine();
        is_correct_number = checkIfNotNegativeNumber(service_id, false);
        while(!is_correct_number) {
            System.out.println("Podaj poprawną ilość: ");
            quantity = scanner.nextLine();
            is_correct_number = checkIfNotNegativeNumber(quantity, false);
        }
        preparedStatement.setString(1, service_id);
        preparedStatement.setString(2, guest_id);
        preparedStatement.setString(3, quantity);
        int executedUpdate = preparedStatement.executeUpdate();
        System.out.println("Dodano usługę.");



    }

    private void calculateCost() throws SQLException{
        String guest_id = this.getIdGuest();
        PreparedStatement preparedStatement = conn.prepareStatement("select getCostForGuest(?) from dual");
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
        boolean is_correct_number = checkIfNotNegativeNumber(room_id, true);
        while(!is_correct_number) {
            System.out.println("Podaj poprawny numer pokoju: ");
            room_id = in.nextLine();
            is_correct_number = checkIfNotNegativeNumber(room_id, true);
        }
        System.out.println("Wpisz ilość dni: ");
        String stay_length = in.nextLine();
        is_correct_number = checkIfNotNegativeNumber(stay_length, false);
        while(!is_correct_number) {
            System.out.println("Podaj poprawną ilość dni: ");
            stay_length = in.nextLine();
            is_correct_number = checkIfNotNegativeNumber(stay_length, false);
        }
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
        ResultSet rs = statement.executeQuery("SELECT guest_id, name, surname, room_id FROM current_guests join reservations using(guest_id)");
        System.out.println("Lista gości:");
        while(rs.next()){
            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + ", numer pokoju:  " + rs.getString(4));
        }
    }

}
