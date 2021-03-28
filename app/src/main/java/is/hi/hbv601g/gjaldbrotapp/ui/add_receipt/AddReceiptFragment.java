package is.hi.hbv601g.gjaldbrotapp.ui.add_receipt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

        root.setBackgroundColor(Color.WHITE);

        man = root.findViewById(R.id.add_receipt_manually);

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddManuallyFragment addManually = new AddManuallyFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.nav_host_fragment, addManually);
                ft.commit();
            }
        });

        photo = root.findViewById(R.id.add_receipt_photo);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPhotoFragment addPhoto = new AddPhotoFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.nav_host_fragment, addPhoto);
                ft.commit();
            }
        });


        return root;
    }
}
