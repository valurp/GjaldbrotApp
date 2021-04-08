package is.hi.hbv601g.gjaldbrotapp.ui.all_receipts;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.ReceiptEditFragment;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;
import is.hi.hbv601g.gjaldbrotapp.ui.add_receipt.AddManuallyFragment;

public class ChangeReceiptFragment extends Fragment {
    private static String TAG = "CHANGE_RECEIPT_FRAGMENT";

    public static Bundle createBundleFromReceipt(ReceiptItem receiptItem) {
        return new Bundle(); // add attributes from the receiptItem
    }

    private ReceiptEditFragment mReceiptEditFragment;

    public ChangeReceiptFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_receipt, container, false);
        view.setBackgroundColor(Color.WHITE);

        Log.i(TAG, getArguments().getString("amount"));

        FragmentManager fm = getChildFragmentManager();
        mReceiptEditFragment = new ReceiptEditFragment();// pass in receipt item from the bundle
        fm.beginTransaction().add(R.id.edit_receipt_container, mReceiptEditFragment).commit();

        Button createReceiptBtn = (Button) view.findViewById(R.id.edit_receipt_btn);
        createReceiptBtn.setOnClickListener(new CreateReceiptListener());
        return view;
    }

    private class CreateReceiptListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            try {
                ReceiptItem receiptItem = mReceiptEditFragment.getReceiptItem();
                new CreateReceiptTask().execute(receiptItem);
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private class CreateReceiptTask extends AsyncTask<ReceiptItem, Void, Boolean> {
        @Override
        public Boolean doInBackground(ReceiptItem... params) {
            return false;
            //return ReceiptService.getInstance().addReceipt(params[0]);
        }
        @Override
        public void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                // TODO reroute to all_receipts, need callback to parent activity
                return;
            }
            else {
                Log.e("CREATE RECEIPT", "Error creating receipt");
            }
        }
    }
}