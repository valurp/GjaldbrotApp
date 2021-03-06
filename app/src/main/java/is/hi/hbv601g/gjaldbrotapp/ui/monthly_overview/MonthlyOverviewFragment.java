package is.hi.hbv601g.gjaldbrotapp.ui.monthly_overview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.OverviewGroup;
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

public class MonthlyOverviewFragment extends Fragment {
    private String TAG = "MonthlyOverview";
    private GraphView mGraph;
    private List<OverviewGroup> mOverviewData;
    private TypeRecyclerAdapter mAdapter;

    public MonthlyOverviewFragment() {
        mOverviewData = new ArrayList<>();
        mAdapter = new TypeRecyclerAdapter(mOverviewData);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_overview, container, false);
        view.setBackgroundColor(Color.WHITE);

        mGraph = (GraphView) view.findViewById(R.id.overview_graph);
        mGraph.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) return "";
                return (int) value + " kr";
            }
            @Override
            public void setViewport(Viewport viewport) {

            }
        });

        RecyclerView legend = (RecyclerView) view.findViewById(R.id.overview_legend);
        legend.setLayoutManager(new LinearLayoutManager(getContext()));
        legend.setAdapter(mAdapter);

        new FetchOverviewTask().execute();

        return view;
    }

    private void updateGraph() {
        mGraph.removeAllSeries();
        int i = 0;
        int max = 0;
        int numOfDataPoints = 0;
        ArrayList<ColoredDataPoint> dataPoints = new ArrayList<ColoredDataPoint>();
        ArrayList<DataPoint> budgetDataPoints = new ArrayList<>();
        for (OverviewGroup group : mOverviewData) {
            if (group.isVisible()) {
                numOfDataPoints++;
                dataPoints.add(new ColoredDataPoint(i+1, group.getAmount(), group.getColor(), group.getMaxBudget()));
                budgetDataPoints.add(new DataPoint(i+1, group.getMaxBudget()));
                if (max < group.getAmount()) {
                    max = group.getAmount();
                }
                i++;
            }
        }
        BarGraphSeries<ColoredDataPoint> barGraphSeries =
                new BarGraphSeries<ColoredDataPoint>(dataPoints.toArray(new ColoredDataPoint[dataPoints.size()]));
        PointsGraphSeries<DataPoint> pointPointsGraphSeries =
                new PointsGraphSeries<>(budgetDataPoints.toArray(new DataPoint[budgetDataPoints.size()]));
        barGraphSeries.setValueDependentColor(new ValueDependentColor<ColoredDataPoint>() {
            @Override
            public int get(ColoredDataPoint data) {
                if (data.getY() > data.getMaxBudget()) {
                    return Color.RED;
                }
                return data.getColor();
            }
        });
        barGraphSeries.setSpacing(20);
        mGraph.getViewport().setYAxisBoundsManual(true);
        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinY(0);
        mGraph.getViewport().setMaxY(max + max*0.2);
        mGraph.getViewport().setMinX(0);
        mGraph.getViewport().setMaxX(numOfDataPoints+1);

        mGraph.addSeries(barGraphSeries);
        mGraph.addSeries(pointPointsGraphSeries);
    }

    private class FetchOverviewTask extends AsyncTask<Void, Void, List<OverviewGroup>> {
        @Override
        public List<OverviewGroup> doInBackground(Void... params) {
            return ReceiptService.getInstance().fetchOverview();
        }
        @Override
        public void onPostExecute(List<OverviewGroup> result) {
            Log.i(TAG, "fetched data");
            mOverviewData.addAll(result);
            mAdapter.notifyDataSetChanged();
            updateGraph();
        }
    }

    private class ColoredDataPoint extends DataPoint {
        private int mColor;
        private int mMaxBudget;
        public ColoredDataPoint(int x, int y, int color, int maxBudget) {
            super(x, y);
            mColor = color;
            mMaxBudget = maxBudget;
        }

        public int getColor() {
            return mColor;
        }
        public int getMaxBudget() { return mMaxBudget; }
    }

    private class TypeRecyclerAdapter extends RecyclerView.Adapter<TypeRecyclerAdapter.ViewHolder>{
        private List<OverviewGroup> mValues;

        public TypeRecyclerAdapter(List<OverviewGroup> values) {
            mValues = values;
        }

        @Override
        public TypeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.type_layout, parent, false);
            return new TypeRecyclerAdapter.ViewHolder(view);
        }

        public void onBindViewHolder(final TypeRecyclerAdapter.ViewHolder holder, int position) {
            holder.mTextView.setText(mValues.get(position).getCategory());
            if (mValues.get(position).getAmount() > mValues.get(position).getMaxBudget()) {
                holder.mView.setBackgroundColor(Color.argb(150, 255, 0, 0));
            }
            holder.mColor.setBackgroundColor(mValues.get(position).getColor());
            holder.mButton.setOnClickListener(new TypeRecyclerAdapter.ShowButtonOnClickListener(holder.mButton, position));
        }

        public int getItemCount() { return mValues.size(); }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView mColor;
            TextView mTextView;
            Button mButton;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(R.id.overview_legend_title);
                mColor = (TextView) view.findViewById(R.id.overview_legend_color);
                mButton = (Button) view.findViewById(R.id.show_hide_type_button);
            }
        }

        private class ShowButtonOnClickListener implements View.OnClickListener {
            private int mPosition;
            private Button mButton;
            public ShowButtonOnClickListener(Button button, int position){
                mButton = button;
                mPosition = position;
            }

            @Override
            public void onClick(View view) {
                Log.i(TAG, mPosition+"");

                if (mButton.getText().toString().toLowerCase().equals("hide")) {
                    mButton.setText("Show");
                    mOverviewData.get(mPosition).setHidden();
                }
                else {
                    mButton.setText("Hide");
                    mOverviewData.get(mPosition).setVisible();
                }
                updateGraph();
            }
        }
    }
}