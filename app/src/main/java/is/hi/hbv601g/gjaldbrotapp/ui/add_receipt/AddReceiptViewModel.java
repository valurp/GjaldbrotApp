package is.hi.hbv601g.gjaldbrotapp.ui.add_receipt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddReceiptViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddReceiptViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add receipt fragment");
    }

    public LiveData<String> getText() { return mText; }
}
