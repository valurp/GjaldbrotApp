package is.hi.hbv601g.gjaldbrotapp.ui.all_receipts;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.R;

import is.hi.hbv601g.gjaldbrotapp.ui.all_receipts.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ReceiptItem}.
 */
public class MyRecieptRecyclerViewAdapter extends RecyclerView.Adapter<MyRecieptRecyclerViewAdapter.ViewHolder> {

    private List<ReceiptItem> mValues;

    public MyRecieptRecyclerViewAdapter(List<ReceiptItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_all_receipts, parent, false);
        return new ViewHolder(view);
    }

    // Þessi aðferð populate-ar röð i með gögnum úr staki i úr listanum.
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDateView.setText(""+mValues.get(position).getFormattedDate());
        holder.mTypeView.setText(""+mValues.get(position).getType());
        holder.mAmountView.setText(""+mValues.get(position).getAmount());
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
        public ReceiptItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTypeView = (TextView) view.findViewById(R.id.receipt_list_type);
            mAmountView = (TextView) view.findViewById(R.id.receipt_list_amount);
            mDateView = (TextView) view.findViewById(R.id.receipt_list_date);
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
}