public class Customer
{
    long user_id;
    public String name;
    public String phone;
    public int card_id;
    int count;
    private boolean isOrdering;

    public boolean getOrdering() {
        return isOrdering;
    }

    public void setOrdering(boolean ordering) {
        isOrdering = ordering;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String type;

    public Customer(String name, String phone, String type) {
        this.name = name;
        this.phone = phone;
        this.type = type;
    }

    public String getType(){return type;}

    public int getCard_id() {
        return card_id;
    }



    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }



    public Customer(long user_id) {
        this.user_id = user_id;
    }

    //////
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void countMinus()
    {
        this.setCount(this.getCount() - 1);
    }


}
