package is.hi.hbv601g.gjaldbrotapp.ui.monthly_overview;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.OverviewGroup;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

public class MonthlyOverviewFragment extends Fragment {
    private String TAG = "MontlyOverview";
    private GraphView mGraph;
    private List<OverviewGroup> mOverviewData;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_overview, container, false);
        view.setBackgroundColor(Color.WHITE);

        mGraph = (GraphView) view.findViewById(R.id.overview_graph);
        mGraph.getGridLabelRenderer().setHighlightZeroLines(true);
        mGraph.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) return "matur";
                return (int) value + " kr";
            }
            @Override
            public void setViewport(Viewport viewport) {

            }
        });
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(3);

        new FetchOverviewTask().execute();

        return view;
    }

    private void updateGraph() {
        int i = 0;
        ArrayList<ColoredDataPoint> dataPoints = new ArrayList<ColoredDataPoint>();
        for (OverviewGroup group : mOverviewData) {
            if (group.isVisible()) {
                dataPoints.add(new ColoredDataPoint(i, group.getAmount(), Color.rgb(255, 100, 100)));
                i++;
            }
        }
        BarGraphSeries<ColoredDataPoint> barGraphSeries =
                new BarGraphSeries<ColoredDataPoint>(dataPoints.toArray(new ColoredDataPoint[dataPoints.size()]));
        barGraphSeries.setValueDependentColor(new ValueDependentColor<ColoredDataPoint>() {
            @Override
            public int get(ColoredDataPoint data) {
                return data.getColor();
            }
        });
        barGraphSeries.setSpacing(20);
        mGraph.addSeries(barGraphSeries);
    }

    private class FetchOverviewTask extends AsyncTask<Void, Void, List<OverviewGroup>> {
        @Override
        public List<OverviewGroup> doInBackground(Void... params) {
            return ReceiptService.getInstance().fetchOverview();
        }
        @Override
        public void onPostExecute(List<OverviewGroup> result) {
            Log.i(TAG, "fetched data");
            mOverviewData = result;
            updateGraph();
        }
    }

    private class ColoredDataPoint extends DataPoint {
        private int mColor;
        public ColoredDataPoint(int x, int y, int color) {
            super(x, y);
            mColor = color;
        }

        public ColoredDataPoint(double x, double y, int color) {
            super(x, y);
            mColor = color;
        }

        public ColoredDataPoint(int x, double y, int color) {
            super(x, y);
            mColor = color;
        }

        public ColoredDataPoint(double x, int y, int color) {
            super(x, y);
            mColor = color;
        }

        public int getColor() {
            return mColor;
        }
    }
}