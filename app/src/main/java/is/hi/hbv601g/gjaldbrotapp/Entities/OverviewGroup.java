package is.hi.hbv601g.gjaldbrotapp.Entities;

public class OverviewGroup {
    private String category;
    private int amount;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

    public OverviewGroup(String category, int amount) {
        this.category = category;
        this.amount = amount;
    }

    public OverviewGroup() {
    }
}
