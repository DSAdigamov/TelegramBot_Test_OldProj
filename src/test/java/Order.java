import java.util.ArrayList;

public class Order {
    private ArrayList<String> orders;
    private String employee;
    private String status;
    private String price;

    public Order(ArrayList<String> orders, String employee, String status, String price) {
        this.orders = orders;
        this.employee = employee;
        this.status = status;
        this.price = price;
    }

    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<String> orders) {
        this.orders = orders;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
