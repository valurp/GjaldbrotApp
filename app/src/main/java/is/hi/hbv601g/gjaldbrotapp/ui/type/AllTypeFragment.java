package is.hi.hbv601g.gjaldbrotapp.ui.type;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
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
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.HttpManager;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;
import is.hi.hbv601g.gjaldbrotapp.ui.add_receipt.AddManuallyFragment;
import is.hi.hbv601g.gjaldbrotapp.ui.all_receipts.dummy.DummyContent;

/**
 * Fragment sem að sækir og birtir allar kvittanir sem innskráður notandi hefur.
 */
public class AllTypeFragment extends Fragment {

    private List<Type> mTypeItems;
    private TypeRecyclerViewAdapter mAdapter;

    public AllTypeFragment() {
        mTypeItems = new ArrayList<Type>();
        mAdapter = new TypeRecyclerViewAdapter(mTypeItems);
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

        View view = inflater.inflate(R.layout.fragment_all_types_list, container, false);
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
    private class FetchReceiptsTask extends AsyncTask<Void, Void, List<Type>> {
        @Override
        protected List<Type> doInBackground(Void... params) {
            return ReceiptService.getInstance().fetchReceiptType();
        }

        @Override
        protected void onPostExecute(List<Type> items) {
            if(items == null) {
                Log.e("NULL ERROR", "items from receipt call are null");
                return;
            }
            mTypeItems.addAll(items); // TODO gera eitthvað í null response frá httpManager t.d. láta user logga sig aftur inn
            mAdapter.notifyDataSetChanged();
        }
    }

    private class TypeRecyclerViewAdapter extends RecyclerView.Adapter<TypeRecyclerViewAdapter.ViewHolder>{
        private static final String TAG = "ReceiptRecyclerAdapter";
        private List<Type> mValues;

        public TypeRecyclerViewAdapter(List<Type> items) {
            mValues = items;
        }

        @Override
        public TypeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_all_types, parent, false);
            return new TypeRecyclerViewAdapter.ViewHolder(view);
        }

        // Þessi aðferð populate-ar röð i með gögnum úr staki i úr listanum.
        @Override
        public void onBindViewHolder(final TypeRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(""+holder.mItem.getName());
            holder.mColorView.setText(""+holder.mItem.getColor());
            // holder.mEditButton.setOnClickListener(new ReceiptRecyclerViewAdapter.EditButtonOnClickListener(holder.mItem));
            holder.mDeleteButton.setText("Delete Type");
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mColorView;
            public final Button mDeleteButton;
            public Type mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.type_list_name);
                mColorView = (TextView) view.findViewById(R.id.type_list_color);
                mDeleteButton = (Button) view.findViewById(R.id.type_list_button);
            }
        }

        public void setItems(List<Type> typeItems) {
            mValues = typeItems;
            notifyDataSetChanged();
        }

        /*private class EditButtonOnClickListener implements View.OnClickListener {
            private ReceiptItem mReceiptItem;

            public EditButtonOnClickListener(ReceiptItem receiptItem) {
                //super();
                mReceiptItem = receiptItem;
            }

            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                Bundle bundle = ChangeReceiptFragment.createBundleFromReceipt(mReceiptItem);
                navController.navigate(R.id.action_nav_all_receipts_to_changeReceiptFragment, bundle);
            }
        }*/
    }
}