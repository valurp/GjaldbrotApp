package is.hi.hbv601g.gjaldbrotapp.Entities;


public class Type {
    private int id;
    private String name;
    private int color;
    private int maxBudget;

    public int getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(int maxBudget) {
        this.maxBudget = maxBudget;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Type(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
