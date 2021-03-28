package is.hi.hbv601g.gjaldbrotapp.ui.all_receipts;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.HttpManager;
import is.hi.hbv601g.gjaldbrotapp.ui.all_receipts.dummy.DummyContent;

public class AllReceiptsFragment extends Fragment {

    private List<ReceiptItem> mReceiptItems;
    private MyRecieptRecyclerViewAdapter mAdapter;

    public AllReceiptsFragment() {
        mReceiptItems = new ArrayList<ReceiptItem>();
        mAdapter = new MyRecieptRecyclerViewAdapter(mReceiptItems);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchReceiptsTask().execute(null, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_all_receipts_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    private class FetchReceiptsTask extends AsyncTask<Void, Void, List<ReceiptItem>> {
        @Override
        protected List<ReceiptItem> doInBackground(Void... params) {
            return new HttpManager().fetchReceipts();
        }

        @Override
        protected void onPostExecute(List<ReceiptItem> items) {
            mReceiptItems.addAll(items); // TODO gera eitthvað í null response frá httpManager
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new MyRecieptRecyclerViewAdapter(mReceiptItems);
        } else {
            mAdapter.setItems(mReceiptItems);
        }
    }
}