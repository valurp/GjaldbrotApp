package is.hi.hbv601g.gjaldbrotapp.ui.monthly_comparison;

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

public class MonthlyComparisonFragment extends Fragment {

    private MonthlyComparisonViewModel mMonthlyComparisonViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        mMonthlyComparisonViewModel =
                new ViewModelProvider(this).get(MonthlyComparisonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_month_comparison, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        mMonthlyComparisonViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}