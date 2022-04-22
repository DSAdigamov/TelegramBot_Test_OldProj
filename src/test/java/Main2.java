import java.sql.*;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Cafe", "postgres", "root")) {

                Scanner scanner = new Scanner(System.in);

                DataBase DB = new DataBase();


                //DB.CreateDatabase(conn);
                //DB.addSomeEmployees(conn);
                //DB.addSomeBonusCard();
                //DB.addSomeFood(conn);
                //DB.addSomeStatuses();
                DB.ShowOrder(449945016);
                System.out.println(DB.ShowOrder(449945016).get(0).get(0));



            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

