package is.hi.hbv601g.gjaldbrotapp.ui.monthly_overview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import is.hi.hbv601g.gjaldbrotapp.R;

public class MonthlyOverviewFragment extends Fragment {

    private MonthlyOverviewViewModel mMonthlyOverviewViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        mMonthlyOverviewViewModel =
                new ViewModelProvider(this).get(MonthlyOverviewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_monthly_overview, container, false);

        root.setBackgroundColor(Color.WHITE);

        final TextView textView = root.findViewById(R.id.text_monthly_overview);
        mMonthlyOverviewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}