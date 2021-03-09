package is.hi.hbv601g.gjaldbrotapp.Entities;

public class ReceiptItem {

    private int id;
    private int amount;
    private String type;
    //Private ??? date

    public ReceiptItem(int id, int amount, String type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }
    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }





}
