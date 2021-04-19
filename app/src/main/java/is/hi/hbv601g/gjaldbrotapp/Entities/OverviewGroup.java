package is.hi.hbv601g.gjaldbrotapp.Entities;

public class OverviewGroup {
    private String mCategory;
    private int mAmount;
    private int mColor;
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

    public OverviewGroup(String category, int amount, int color) {
        this.mCategory = category;
        this.mAmount = amount;
        this.mColor = color;
        mIsVisible = true;
    }

    public OverviewGroup() {
        mIsVisible = true;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
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
