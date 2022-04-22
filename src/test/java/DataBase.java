import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;

public class DataBase
{
    public final Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Cafe", "postgres", "root");

    public DataBase() throws SQLException {
    }

    public void CreateDatabase(Connection conn)
    {
        {
            try {
                PreparedStatement prsment = conn.prepareStatement(
                        "CREATE TABLE \"Employee\" (\n" +
                                " \"employee_id\" serial,\n" +
                                " \"bonus_card_id\" integer,\n" +
                                " \"name\" varchar(30),\n" +
                                " \"phone_number\" varchar(30),\n" +
                                " \"profession\" varchar(30),\n" +
                                " PRIMARY KEY (\"employee_id\")\n" +
                                "); \n" +
                                "CREATE TABLE \"Bonus_card\" (\n" +
                                " \"bonus_card_id\" integer,\n" +
                                " \"type\" varchar(30),\n" +
                                " \"discount\" decimal,\n" +
                                " PRIMARY KEY (\"bonus_card_id\")\n" +
                                ");\n" +
                                "CREATE TABLE \"Customer\" (\n" +
                                " \"customer_id\" bigint,\n" +
                                " \"bonus_card_id\" integer,\n" +
                                " \"name\" varchar(30),\n" +
                                " \"phone_number\" varchar(30),\n" +
                                " PRIMARY KEY (\"customer_id\")\n" +
                                ");\n" +
                                "CREATE TABLE \"Food\" (\n" +
                                " \"food_id\" serial,\n" +
                                " \"name\" varchar(30),\n" +
                                " \"price\" decimal,\n" +
                                " PRIMARY KEY (\"food_id\")\n" +
                                ");\n" +
                                "CREATE TABLE \"Order\" (\n" +
                                " \"order_id\" serial,\n" +
                                " \"customer_id\" integer,\n" +
                                " \"food_id\" integer,\n" +
                                " \"employee_id\" integer,\n" +
                                " \"status_id\" integer,\n" +
                                " \"comment\" varchar(50),\n" +
                                " PRIMARY KEY (\"order_id\")\n" +
                                ");\n" +
                                "CREATE TABLE \"Status\" (\n" +
                                " \"status_id\" integer,\n" +
                                " \"status\" varchar(30),\n" +
                                " PRIMARY KEY (\"status_id\")\n" +
                                ");"
                );
                ResultSet res = prsment.executeQuery();


                System.out.println("База данных создана: \n");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void InsertEmloyee(Connection conn, int bonus_card_id, String name, String phone_number, String profession)
    {
        try {
            PreparedStatement prsment = conn.prepareStatement(
                    "INSERT INTO \"Employee\" (bonus_card_id,name, phone_number, profession ) VALUES (?,?,?,?);");
            prsment.setInt(1, bonus_card_id);
            prsment.setString(2, name);
            prsment.setString(3, phone_number);
            prsment.setString(4, profession);
            prsment.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addSomeEmployees()
    {
        InsertEmloyee(conn, 4, "Василий Уткин", "+7(093)393-13-95", "официант");
        InsertEmloyee(conn, 4, "Воробьев Николай", "+7(642)994-91-66", "официант");
        InsertEmloyee(conn, 4, "Журавлева Эвелина", "+7(52)470-17-94", "Менеджер");
        InsertEmloyee(conn, 4, "Дроздов Кирилл", "+7(2403)083-63-67", "Бармен");
        InsertEmloyee(conn, 4, "Шилов Роман", "+7(51)679-93-25", "Бармен");
    }

    public void InserBonusCard(Connection conn, int bonus_card_id, String type, double discount)
    {
        try {
            PreparedStatement prsment = conn.prepareStatement(
                    "INSERT INTO \"Bonus_card\" (bonus_card_id,type ,discount ) VALUES (?,?,?);");
            prsment.setInt(1, bonus_card_id);
            prsment.setString(2, type);
            prsment.setDouble(3, discount);
            prsment.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addSomeBonusCard()
    {
        InserBonusCard(conn,1,"Обычный", 1);
        InserBonusCard(conn,2,"Бронзовый", 0.9);
        InserBonusCard(conn,3,"Студенческий", 0.8);
        InserBonusCard(conn,4,"staff", 0.7);
    }

    public void InsertFood(Connection conn, String name, double price)
    {
        try {
            PreparedStatement prsment = conn.prepareStatement(
                    "INSERT INTO \"Food\" (name, price ) VALUES (?,?);");
            prsment.setString(1, name);
            prsment.setDouble(2, price);
            prsment.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addSomeFood(Connection conn)
    {
        InsertFood(conn, "Лазанья", 390);
        InsertFood(conn,"Карбонара", 400);
        InsertFood(conn,"Картошка с котлетками", 450);
        InsertFood(conn,"Суп томатный", 250);
        InsertFood(conn,"Лосось игристый", 600);
        InsertFood(conn,"чизкейк", 200);
        InsertFood(conn,"Чай индийский", 15);

    }



    public void InsertStatus(int status_id,String status)
    {
        try {
            PreparedStatement prsment = conn.prepareStatement(
                    "INSERT INTO \"Status\" (status_id,status ) VALUES (?,?);");
            prsment.setInt(1, status_id);
            prsment.setString(2, status);
            prsment.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addSomeStatuses()
    {
        InsertStatus(1, "Не готов");
        InsertStatus(2, "Почти готов");
        InsertStatus(3, "Готов");

    }


    public void InsertOrder(String selectedFood, long customer_id) throws SQLException {
        PreparedStatement prsment2 = conn.prepareStatement("select food_id from \"Food\" where name = ?");
        prsment2.setString(1, selectedFood);
        ResultSet resultSet2 = prsment2.executeQuery();
        resultSet2.next();
        int food_id = resultSet2.getInt("food_id");

        PreparedStatement prsment3 = conn.prepareStatement("select employee_id from \"Employee\" where profession = 'официант' order by random() limit 1");
        ResultSet resultSet3 = prsment3.executeQuery();
        resultSet3.next();
        int employee_id = resultSet3.getInt("employee_id");
        //не готов
        int status_id = 1;

        try {
            PreparedStatement prsment = conn.prepareStatement(
                    "INSERT INTO \"Order\" (customer_id, food_id, employee_id, status_id) VALUES (?,?,?,?);");
            prsment.setLong(1, customer_id);
            prsment.setInt(2, food_id);
            prsment.setInt(3, employee_id);
            prsment.setInt(4, status_id);
            //prsment.setString(5, comment);
            prsment.executeUpdate();
        } catch (SQLException throwables ) {
            throwables.printStackTrace();
        }
    }

    public void addCustomer(long customer_id, int bonus_car_id, String name, String phone_number) throws SQLException {
        try {
            PreparedStatement prsment = conn.prepareStatement(
                    "INSERT INTO \"Customer\" (customer_id,bonus_card_id , name, phone_number ) VALUES (?,?,?,?);");
            prsment.setLong(1, customer_id);
            prsment.setInt(2, bonus_car_id);
            prsment.setString(3, name);
            prsment.setString(4, phone_number);
            prsment.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public boolean CheckForCustomer(long Customer_id) throws SQLException {
        PreparedStatement prsment = conn.prepareStatement("select exists (select customer_id from \"Customer\" where customer_id = ? )tbl1");
        prsment.setLong(1, Customer_id);
        ResultSet result = prsment.executeQuery();
        result.next();
        return result.getBoolean(1);

    }

    public Customer ShowCustome(long cutomer_id) throws SQLException {
        String name = null;
        String phone_number = null;
        String type = null;
        try {
            PreparedStatement prsment = conn.prepareStatement("SELECT name, phone_number, type from \"Customer\" inner join \"Bonus_card\" using(bonus_card_id) where customer_id = ?");
            prsment.setLong(1, cutomer_id);
            ResultSet res = prsment.executeQuery();
            res.next();
            name = res.getString("name");
            phone_number = res.getString("phone_number");
            type = res.getString("type");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new Customer(name, phone_number, type);
    }

    public ArrayList<String> ShowEmployee() throws SQLException {

            PreparedStatement prsment = conn.prepareStatement("SELECT name, phone_number, profession from \"Employee\" ");
            ResultSet res = prsment.executeQuery();
            ArrayList<String> arrayListEmployeers = new ArrayList<>();
            while (res.next()) {
                String phone_number = res.getString("phone_number");
                String name = res.getString("name");
                String profession = res.getString("profession");
                arrayListEmployeers.add(new String("Имя сотрудника: " + name + "\n" +
                        "Номер телефона: " + phone_number + "\n" +
                        "Профессия: " + profession));

            }
        return arrayListEmployeers;
    }

    public ArrayList<ArrayList<String>> ShowMenu() throws SQLException {
        String name;
        String price;
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        matrix.add(names);
        matrix.add(prices);
        try {
            PreparedStatement prsment = conn.prepareStatement("SELECT name, price from \"Food\" order by food_id asc");
            ResultSet res = prsment.executeQuery();
            while (res.next())
            {
                name = res.getString("name");
                price = res.getString("price");
                matrix.get(0).add(name);
                matrix.get(1).add(price);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return matrix;
    }

    public ArrayList<ArrayList<String>> ShowOrder(long customer_id) {
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        ArrayList<String> nameFoodList = new ArrayList<>();
        ArrayList<String> kolvoList = new ArrayList<>();
        ArrayList<String> summaList = new ArrayList<>();
        ArrayList<String> discountList = new ArrayList<>();
        matrix.add(nameFoodList);
        matrix.add(kolvoList);
        matrix.add(summaList);
        matrix.add(discountList);
        try {
            PreparedStatement prsment = conn.prepareStatement(
                    "select \"Food\".name, count(food_id) as kolvo, sum(price) as summa, discount from \"Food\"\n" +
                    "inner join \"Order\" using(food_id)\n" +
                    "inner join \"Customer\" using(customer_id)\n" +
                    "inner join \"Bonus_card\" using(bonus_card_id)"+
                    "where customer_id = ? \n" +
                    "group by \"Food\".name, \"Bonus_card\".discount \n" +
                    "order by summa desc");
            prsment.setLong(1,customer_id);
            ResultSet res = prsment.executeQuery();
            while (res.next())
            {
                matrix.get(0).add(res.getString("name"));
                matrix.get(1).add(String.valueOf(res.getLong("kolvo")));
                matrix.get(2).add(String.valueOf(res.getDouble("summa")));
                matrix.get(3).add(String.valueOf(res.getDouble("discount")));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return matrix;
    }
}
