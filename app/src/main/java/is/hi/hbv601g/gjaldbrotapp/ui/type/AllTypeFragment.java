package is.hi.hbv601g.gjaldbrotapp.ui.type;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

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
        private static final String TAG = "TypeRecyclerAdapter";
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
            holder.mColorView.setBackgroundColor(holder.mItem.getColor());
            holder.mDeleteButton.setOnClickListener(new DeleteButtonOnClickListener(holder.mItem.getId()));
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

        private class DeleteButtonOnClickListener implements View.OnClickListener {
            private int mId;
            public DeleteButtonOnClickListener(int id) {
                super();
                mId = id;
            }
            @Override
            public void onClick(View v) {
                new DeleteReceiptTypeTask().execute(mId);
            }
        }

        private class DeleteReceiptTypeTask extends AsyncTask<Integer, Void, Boolean> {
            @Override
            public Boolean doInBackground(Integer... params) {
                return ReceiptService.getInstance().deleteType(params[0]);
            }
            @Override
            public void onPostExecute(Boolean result) {


            }
        }
    }
}