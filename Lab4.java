import java.util.Scanner;
import java.sql.*;

public class Lab4 {

   static final String jdbcDriver = "com.mysql.jdbc.Driver";
   static final String dbAddress = "jdbc:mysql://10.0.10.3:3306/";
   static final String userPass = "?user=root&password=qwerty123";
   static final String dbName = "database";
   static final String userName = "root";
   static final String password = "qwerty123";

   static Connection con;

   public static void main(String[] args) {
      Scanner scan = new Scanner(System.in);
      boolean run = true;
      int operation;
      
      try {
         Class.forName(jdbcDriver);
         con = DriverManager.getConnection(dbAddress + dbName, userName, password);
         Statement  statement = con.createStatement();
         int myResult = statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS `" +dbName+ "`.`stuff` ( `id` INT NOT NULL AUTO_INCREMENT , `firstname` VARCHAR(64) NOT NULL , `surname` VARCHAR(64) NOT NULL , `position` VARCHAR(64) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;"
         );
         statement.close();
      } catch (ClassNotFoundException | SQLException e) {
         e.printStackTrace();
      }

      scan.nextLine();
      
      while(run) {
         System.out.println("\nCo chcesz zrobić?");
         System.out.println("1. Dodaj nowego pracownika");
         System.out.println("2. Usuń pracownika");
         System.out.println("3. Wyświetl listę pracowników");
         System.out.println("4. Zakończ pracę");
         System.out.print("Wybierz operacje: ");
         operation = scan.nextInt();
         scan.nextLine();

         switch(operation) {
            case 1: {
               System.out.println("Dodawanie nowego pracownika");
               System.out.print("Imie: ");
               String firstname = scan.nextLine();
               System.out.print("Nazwisko: ");
               String surname = scan.nextLine();
               System.out.print("Stanowisko: ");
               String position = scan.nextLine();

               try {
                  PreparedStatement prpStmt = con.prepareStatement("INSERT INTO stuff ( firstname, surname, position) VALUES (?, ?, ?)");
                    
                  prpStmt.setString(1, firstname);
                  prpStmt.setString(2, surname);
                  prpStmt.setString(3, position);
                    
                  prpStmt.execute();
                  System.out.println("Dodano nowego pracownika");
               } catch (SQLException e) {
                  e.printStackTrace();
               }
               break;
            }
            case 2: {
               System.out.println("Podaj id pracownika");
               System.out.print("Id pracownika: ");
               int id = scan.nextInt();

               try {
                  PreparedStatement prpStmt = con.prepareStatement("DELETE FROM stuff WHERE ID = ?");
                  
                  prpStmt.setInt(1, id);
                  
                  prpStmt.execute();
                  System.out.println("Usunięto pracownika");
               } catch (SQLException e) {
                  e.printStackTrace();
               }
               break;
            }
            case 3: {
               try {
                  System.out.println("Lista pracowników");
                  Statement  statement = con.createStatement();
                  ResultSet rs =  statement.executeQuery("SELECT * FROM stuff");
                  while(rs.next()) {
                     int id = rs.getInt("id");
                     String firstname = rs.getString("firstname");
                     String surname = rs.getString("surname");
                     String position = rs.getString("position");

                     System.out.println(id + " | " + firstname + " | " + surname + " | " + position);
                  }
                  statement.close();
               } catch (SQLException e) {
                  e.printStackTrace();
               }
               break;
            }
            case 4: {
               System.out.println("Zamykanie aplikacji");
               run = false;
               break;
            }
         }
      }
   }
}
