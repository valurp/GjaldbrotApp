package is.hi.hbv601g.gjaldbrotapp.ui.monthly_comparison;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import is.hi.hbv601g.gjaldbrotapp.Entities.ComparisonData;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

public class MonthlyComparisonFragment extends Fragment {
    private static final String TAG = "ComparsionFragment";
    private ComparisonData mComparisonData;
    private GraphView mGraphView;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_month_comparison, container, false);

        mGraphView = (GraphView) root.findViewById(R.id.comparison_graph);

        new FetchComparisonTask().execute();

        return root;
    }

    public void populateGraph() {
        for (ComparisonData.Group group : mComparisonData.mGroups) {
            DataPoint[] dataPoints = new DataPoint[group.mAmounts.size()];
            for(int i = 0; i < group.mAmounts.size(); i++) {
                dataPoints[i] = new DataPoint(i, group.mAmounts.get(i).mAmount);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            series.setColor(group.mColor);
            mGraphView.addSeries(series);
        }

        mGraphView.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) return "";
                return (int) value + " kr";
            }
            @Override
            public void setViewport(Viewport viewport) {

            }
        });
    }

    private class FetchComparisonTask extends AsyncTask<Void, Void, ComparisonData> {
        @Override
        public ComparisonData doInBackground(Void... params) {
            return ReceiptService.getInstance().fetchComparison();
        }

        @Override
        public void onPostExecute(ComparisonData result) {
            if (result == null) {
                return;
            }
            Log.i(TAG, result.mGroups.toString());
            mComparisonData = result;
            populateGraph();
        }
    }
}
