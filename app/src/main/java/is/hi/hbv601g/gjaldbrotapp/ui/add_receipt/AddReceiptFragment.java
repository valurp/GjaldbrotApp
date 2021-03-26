package is.hi.hbv601g.gjaldbrotapp.ui.add_receipt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import is.hi.hbv601g.gjaldbrotapp.R;

public class AddReceiptFragment extends Fragment {

    Button man;
    Button photo;

    private AddReceiptViewModel mAddReceiptViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAddReceiptViewModel =
                new ViewModelProvider(this).get(AddReceiptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_receipt, container, false);

        photo = root.findViewById(R.id.add_receipt_photo);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPhotoFragment addPhotoFragment = new AddPhotoFragment();
                System.out.println("Button Clicked");
            }
         });

        man = root.findViewById(R.id.add_receipt_manually);

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked");
            }
        });

        return root;
    }
}
