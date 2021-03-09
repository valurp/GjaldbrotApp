package is.hi.hbv601g.gjaldbrotapp.ui.monthly_overview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonthlyOverviewViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MonthlyOverviewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is monthly overview");
    }

    public LiveData<String> getText() {
        return mText;
    }
}