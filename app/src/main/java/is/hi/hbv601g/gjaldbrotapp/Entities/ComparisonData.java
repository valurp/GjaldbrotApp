package is.hi.hbv601g.gjaldbrotapp.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.ui.monthly_overview.MonthlyOverviewFragment;

public class ComparisonData {
    public Date mStartDate;
    public Date mEndDate;
    public List<Group> mGroups;

    public ComparisonData() {
        mGroups = new ArrayList<>();
    }

    public Group addGroup(String name, int color) {
        Group group = new Group();
        group.mName = name;
        group.mColor = color;
        mGroups.add(group);
        return group;
    }

    public class Group {
        public String mName;
        public int mColor;
        public List<MonthAmount> mAmounts;
        public Group() {
            mAmounts = new ArrayList<>();
        }

        public void addMonthAmount(Date month, int amount) {
            MonthAmount monthAmount = new MonthAmount(month, amount);
            mAmounts.add(monthAmount);
        }
    }

    public class MonthAmount {
        public Date mMonth;
        public int mAmount;
        public MonthAmount(Date month, int amount) {
            mMonth = month;
            mAmount = amount;
        }
    }
}
