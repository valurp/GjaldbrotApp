package is.hi.hbv601g.gjaldbrotapp.ui.all_receipts;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.graphics.Color;
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
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;
import is.hi.hbv601g.gjaldbrotapp.ui.all_receipts.dummy.DummyContent;

/**
 * Fragment sem að sækir og birtir allar kvittanir sem innskráður notandi hefur.
 */
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
        view.setBackgroundColor(Color.WHITE);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    // Innri klasi sem að sækir kvittanir í gegnum ReceiptService og lætur mAdapter fyrir
    // recycler view vita að ný gögn hafi borist.
    private class FetchReceiptsTask extends AsyncTask<Void, Void, List<ReceiptItem>> {
        @Override
        protected List<ReceiptItem> doInBackground(Void... params) {
            return ReceiptService.getInstance().fetchReceipts();
        }

        @Override
        protected void onPostExecute(List<ReceiptItem> items) {
            if(items == null) {
                Log.e("NULL ERROR", "items from receipt call are null");
                return;
            }
            mReceiptItems.addAll(items); // TODO gera eitthvað í null response frá httpManager t.d. láta user logga sig aftur inn
            mAdapter.notifyDataSetChanged();
        }
    }
}