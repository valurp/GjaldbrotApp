package is.hi.hbv601g.gjaldbrotapp.ui.add_receipt;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.ReceiptEditFragment;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddManuallyFragment} factory method to
 * create an instance of this fragment.
 */
public class AddPhotoFragment extends Fragment {
    private static String TAG = "ADD_PHOTO_FRAGMENT";

    private ReceiptEditFragment mReceiptEditFragment;

    public AddPhotoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);
        view.setBackgroundColor(Color.WHITE);

        FragmentManager fm = getChildFragmentManager();
        mReceiptEditFragment = new ReceiptEditFragment();
        fm.beginTransaction().add(R.id.photo_dp_container, mReceiptEditFragment).commit();

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
            return ReceiptService.getInstance().addReceipt(params[0]);
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