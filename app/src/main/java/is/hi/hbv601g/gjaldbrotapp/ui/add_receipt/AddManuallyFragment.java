package is.hi.hbv601g.gjaldbrotapp.ui.add_receipt;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddManuallyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddManuallyFragment extends Fragment {

    public AddManuallyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_manually, container, false);

        root.setBackgroundColor(Color.WHITE);

        Button createReceiptBtn = root.findViewById(R.id.buttonAdd);
        createReceiptBtn.setOnClickListener(new CreateReceiptListener());
        return root;
    }

    private class CreateReceiptListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            new CreateReceiptTask().execute(); // TODO lesa gögn úr EditText, sjá Login og RegisterFragment
        }
    }

    private class CreateReceiptTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        public Boolean doInBackground(Void... params) {
            return ReceiptService.getInstance().addReceipt(new ReceiptItem());
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