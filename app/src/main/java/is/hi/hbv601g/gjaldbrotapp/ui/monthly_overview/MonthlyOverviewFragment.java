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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import is.hi.hbv601g.gjaldbrotapp.R;

public class MonthlyOverviewFragment extends Fragment {

    private MonthlyOverviewViewModel mMonthlyOverviewViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        mMonthlyOverviewViewModel =
                new ViewModelProvider(this).get(MonthlyOverviewViewModel.class);
        View view = inflater.inflate(R.layout.fragment_monthly_overview, container, false);
        view.setBackgroundColor(Color.WHITE);

        GraphView graph = (GraphView) view.findViewById(R.id.overview_graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0,3),
                new DataPoint(1,2),
                new DataPoint(2, 5),
                new DataPoint(3, 5),
                new DataPoint(4, 5),
                new DataPoint(5, 5),
                new DataPoint(6, 5),
        });
        series.setSpacing(30);
        series.setAnimated(true);
        series.setColor(Color.RED);
        graph.addSeries(series);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);

        return view;
    }
}