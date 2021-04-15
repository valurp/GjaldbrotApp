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
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.OverviewGroup;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

public class MonthlyOverviewFragment extends Fragment {
    private String TAG = "MontlyOverview";

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_overview, container, false);
        view.setBackgroundColor(Color.WHITE);

        GraphView graph = (GraphView) view.findViewById(R.id.overview_graph);

        new FetchOverviewTask().execute();

        return view;
    }

    private class FetchOverviewTask extends AsyncTask<Void, Void, List<OverviewGroup>> {
        @Override
        public List<OverviewGroup> doInBackground(Void... params) {
            return ReceiptService.getInstance().fetchOverview();
        }
        @Override
        public void onPostExecute(List<OverviewGroup> result) {
            Log.i(TAG, "fetched data");
            for (OverviewGroup og : result) {
                Log.i(TAG, og.toString());
            }
        }
    }
}