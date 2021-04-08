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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
                NavHostFragment navHostFragment =
                        (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_nav_add_receipt_to_addManuallyFragment);
            }
        });

        photo = root.findViewById(R.id.add_receipt_photo);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_nav_add_receipt_to_addManuallyFragment);
            }
        });


        return root;
    }
}
