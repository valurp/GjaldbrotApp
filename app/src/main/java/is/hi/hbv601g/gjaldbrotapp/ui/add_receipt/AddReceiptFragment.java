package is.hi.hbv601g.gjaldbrotapp.ui.add_receipt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import is.hi.hbv601g.gjaldbrotapp.R;

public class AddReceiptFragment extends Fragment {

    private AddReceiptViewModel mAddReceiptViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAddReceiptViewModel =
                new ViewModelProvider(this).get(AddReceiptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_receipt, container, false);
        return root;
    }
}
