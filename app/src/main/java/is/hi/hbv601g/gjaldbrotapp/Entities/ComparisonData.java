package is.hi.hbv601g.gjaldbrotapp.Entities;

import java.util.Date;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.ui.monthly_overview.MonthlyOverviewFragment;

public class ComparisonData {
    private Date mStartDate;
    private Date mEndDate;
    private List<Group> mGroups;

    public class Group {
        public String mName;
        public int mColor;
        public List<MonthAmount> mAmounts;
        public Group() {

        }
    }

    public class MonthAmount {
        public Date month;
        public int mAmount;
        public MonthAmount() {

        }
    }
}
