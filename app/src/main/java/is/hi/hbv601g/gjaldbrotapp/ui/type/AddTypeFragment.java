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
import android.widget.EditText;
import android.widget.Toast;

import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import is.hi.hbv601g.gjaldbrotapp.Entities.ReceiptItem;
import is.hi.hbv601g.gjaldbrotapp.Entities.Type;
import is.hi.hbv601g.gjaldbrotapp.R;
import is.hi.hbv601g.gjaldbrotapp.ReceiptEditFragment;
import is.hi.hbv601g.gjaldbrotapp.Services.ReceiptService;
import is.hi.hbv601g.gjaldbrotapp.ui.add_receipt.AddManuallyFragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class AddTypeFragment extends Fragment {
    private static String TAG = "ADD_TYPE_FRAGMENT";

    private AddTypeFragment mAddTypeFragment;

    private EditText mNameType;
    private Button createTypeBtn;
    private int colorSelected;
    private ColorPickerDialogBuilder colorPickerDialogBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_type, container, false);
        view.setBackgroundColor(Color.WHITE);

        // FragmentManager fm = getChildFragmentManager();
        // mAddTypeFragment = new AddTypeFragment();
        // fm.beginTransaction().add(R.id.type_dp_container, mAddTypeFragment).commit();

        mNameType = (EditText) view.findViewById(R.id.addType_text);

        ColorPickerView colorPickerView = view.findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            /**
             * Change color by changing the color weel.
             * @param selectedColor
             */
            @Override
            public void onColorChanged(int selectedColor) {
                // Handle on color change
                colorSelected = selectedColor;
                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
            }
        });
        /**
         * Gets the
         */
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                colorSelected = selectedColor;
                Toast.makeText(getActivity(), "selectedColor: " + Integer.toHexString(selectedColor).toUpperCase(), Toast.LENGTH_LONG).show();
            }
        });

        createTypeBtn = (Button) view.findViewById(R.id.AddType_button);
        createTypeBtn.setOnClickListener(new AddTypeFragment.CreateTypeListener());

        return view;
    }
    private class CreateTypeListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            try {
                new AddTypeFragment.CreateTypeTask().execute();
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }
    private class CreateTypeTask extends AsyncTask<Type, Void, Boolean> {
        @Override
        public Boolean doInBackground(Type... params) {
            Log.e("Name", mNameType.getText().toString());
            Log.e("Color", Integer.toHexString(colorSelected));
            return ReceiptService.getInstance().addType(mNameType.getText().toString(), colorSelected);
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