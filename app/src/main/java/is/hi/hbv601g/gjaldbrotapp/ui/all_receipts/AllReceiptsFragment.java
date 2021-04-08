package is.hi.hbv601g.gjaldbrotapp.ui.all_receipts;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.HttpManager;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;
import is.hi.hbv601g.gjaldbrotapp.ui.add_receipt.AddManuallyFragment;
import is.hi.hbv601g.gjaldbrotapp.ui.all_receipts.dummy.DummyContent;

/**
 * Fragment sem að sækir og birtir allar kvittanir sem innskráður notandi hefur.
 */
public class AllReceiptsFragment extends Fragment {

    private List<ReceiptItem> mReceiptItems;
    private ReceiptRecyclerViewAdapter mAdapter;

    public AllReceiptsFragment() {
        mReceiptItems = new ArrayList<ReceiptItem>();
        mAdapter = new ReceiptRecyclerViewAdapter(mReceiptItems);
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

    private class ReceiptRecyclerViewAdapter extends RecyclerView.Adapter<ReceiptRecyclerViewAdapter.ViewHolder>{
        private static final String TAG = "ReceiptRecyclerAdapter";
        private List<ReceiptItem> mValues;

        public ReceiptRecyclerViewAdapter(List<ReceiptItem> items) {
            mValues = items;
        }

        @Override
        public ReceiptRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_all_receipts, parent, false);
            return new ReceiptRecyclerViewAdapter.ViewHolder(view);
        }

        // Þessi aðferð populate-ar röð i með gögnum úr staki i úr listanum.
        @Override
        public void onBindViewHolder(final ReceiptRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mDateView.setText(""+holder.mItem.getFormattedDate());
            holder.mTypeView.setText(""+holder.mItem.getType());
            holder.mAmountView.setText(""+holder.mItem.getAmount());
            holder.mEditButton.setOnClickListener(new ReceiptRecyclerViewAdapter.EditButtonOnClickListener(holder.mItem.getId()));
            holder.mEditButton.setText("Edit receipt");
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTypeView;
            public final TextView mAmountView;
            public final TextView mDateView;
            public final Button mEditButton;
            public ReceiptItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTypeView = (TextView) view.findViewById(R.id.receipt_list_type);
                mAmountView = (TextView) view.findViewById(R.id.receipt_list_amount);
                mDateView = (TextView) view.findViewById(R.id.receipt_list_date);
                mEditButton = (Button) view.findViewById(R.id.receipt_list_button);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mAmountView.getText() + "'";
            }
        }

        public void setItems(List<ReceiptItem> receiptItems) {
            mValues = receiptItems;
            notifyDataSetChanged();
        }

        private class EditButtonOnClickListener implements View.OnClickListener {
            private int mId;

            public EditButtonOnClickListener(int id) {
                //super();
                mId = id;
            }

            @Override
            public void onClick(View v) {
                AddManuallyFragment addManually = new AddManuallyFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.nav_host_fragment, addManually)
                        .addToBackStack("")
                        .commit();
            }
        }
    }
}