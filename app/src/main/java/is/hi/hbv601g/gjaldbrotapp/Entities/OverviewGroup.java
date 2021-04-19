package is.hi.hbv601g.gjaldbrotapp.Entities;

public class OverviewGroup {
    private String mCategory;
    private int mAmount;
    private boolean mIsVisible;

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public void setAmount(int amount) {
        this.mAmount = amount;
    }

    public String getCategory() {
        return mCategory;
    }

    public int getAmount() {
        return mAmount;
    }

    public OverviewGroup(String category, int amount) {
        this.mCategory = category;
        this.mAmount = amount;
        mIsVisible = true;
    }

    public OverviewGroup() {
        mIsVisible = true;
    }

    @Override
    public String toString() {
        return mCategory+":"+mAmount;
    }

    public void setVisible() {
        mIsVisible = true;
    }

    public void setHidden() {
        mIsVisible = false;
    }

    public boolean isVisible() {
        return mIsVisible;
    }
}
