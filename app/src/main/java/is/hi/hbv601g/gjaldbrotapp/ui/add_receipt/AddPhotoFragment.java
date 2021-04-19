package is.hi.hbv601g.gjaldbrotapp.ui.add_receipt;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.MainActivity;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddManuallyFragment} factory method to
 * create an instance of this fragment.
 */
public class AddPhotoFragment extends Fragment {
    private static String TAG = "ADD_PHOTO_FRAGMENT";

    private ImageView imageView;
    private Button addPhotobtn;

    public AddPhotoFragment() {
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);
        view.setBackgroundColor(Color.WHITE);

        addPhotobtn = (Button) view.findViewById(R.id.photo_btn_add);
        imageView = (ImageView) view.findViewById(R.id.display_capture);
        addPhotobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            }
        });
        return view;
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