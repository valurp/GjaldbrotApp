package is.hi.hbv601g.gjaldbrotapp.ui.type;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.ReceiptEditFragment;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;
import is.hi.hbv601g.gjaldbrotapp.ui.add_receipt.AddManuallyFragment;

public class AddTypeFragment extends Fragment {
    private static String TAG = "ADD_TYPE_FRAGMENT";

    private AddTypeFragment mAddTypeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_type, container, false);
        view.setBackgroundColor(Color.WHITE);

        FragmentManager fm = getChildFragmentManager();
        mAddTypeFragment = new AddTypeFragment();
        fm.beginTransaction().add(R.id.manually_dp_container, mAddTypeFragment).commit();

        Button createReceiptBtn = (Button) view.findViewById(R.id.manually_btn_add);
        createReceiptBtn.setOnClickListener(new AddTypeFragment.CreateTypeListener());

        return view;
    }
    private class CreateTypeListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            try {
                // Type type = mAddTypeFragment.getType();
                // new AddTypeFragment.CreateTypeTask().execute(type);
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }
    private class CreateTypeTask extends AsyncTask<Type, Void, Boolean> {
        @Override
        public Boolean doInBackground(Type... params) {
            return ReceiptService.getInstance().addType("String and shit", 1);
        }
        @Override
        public void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_addManuallyFragment_to_nav_all_receipts);
            }
            else {
                Log.e("CREATE RECEIPT", "Error creating receipt");
            }
        }
    }
}