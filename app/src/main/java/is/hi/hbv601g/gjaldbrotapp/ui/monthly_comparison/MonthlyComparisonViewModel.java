package is.hi.hbv601g.gjaldbrotapp.ui.monthly_comparison;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonthlyComparisonViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MonthlyComparisonViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
        // TODO finna út eitthvað network kall
    }

    public LiveData<String> getText() {
        return mText;
    }
}